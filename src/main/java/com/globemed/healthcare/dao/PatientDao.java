package com.globemed.healthcare.dao;

import com.globemed.healthcare.model.PatientRecord;
import com.globemed.healthcare.util.SqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PatientDao {
	public List<PatientRecord> listAll() {
		String sql = "SELECT patient_id, first_name, last_name, date_of_birth, gender, address, phone_number, email, medical_history, treatment_plan FROM patients";
		List<PatientRecord> result = new ArrayList<>();
		try (Connection conn = SqlDataSource.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				PatientRecord p = new PatientRecord(
					rs.getString("patient_id"),
					rs.getString("first_name"),
					rs.getString("last_name"),
					rs.getDate("date_of_birth").toLocalDate(),
					rs.getString("gender"),
					rs.getString("address"),
					rs.getString("phone_number"),
					rs.getString("email")
				);
				p.setMedicalHistory(rs.getString("medical_history"));
				p.setTreatmentPlan(rs.getString("treatment_plan"));
				result.add(p);
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void upsert(PatientRecord p) {
		String sql = "INSERT INTO patients(patient_id, first_name, last_name, date_of_birth, gender, address, phone_number, email, medical_history, treatment_plan) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE first_name=VALUES(first_name), last_name=VALUES(last_name), date_of_birth=VALUES(date_of_birth), gender=VALUES(gender), address=VALUES(address), phone_number=VALUES(phone_number), email=VALUES(email), medical_history=VALUES(medical_history), treatment_plan=VALUES(treatment_plan)";
		try (Connection conn = SqlDataSource.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, p.getPatientId());
			ps.setString(2, p.getFirstName());
			ps.setString(3, p.getLastName());
			ps.setDate(4, java.sql.Date.valueOf(p.getDateOfBirth()));
			ps.setString(5, p.getGender());
			ps.setString(6, p.getAddress());
			ps.setString(7, p.getPhoneNumber());
			ps.setString(8, p.getEmail());
			ps.setString(9, p.getMedicalHistory());
			ps.setString(10, p.getTreatmentPlan());
			ps.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteById(String patientId) {
		String sql = "DELETE FROM patients WHERE patient_id=?";
		try (Connection conn = SqlDataSource.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, patientId);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
} 