package com.globemed.healthcare.dao;

import com.globemed.healthcare.model.Appointment;
import com.globemed.healthcare.util.SqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDao {
	public List<Appointment> listAll() {
		String sql = "SELECT appointment_id, patient_id, doctor_id, appointment_datetime, status, reason FROM appointments";
		List<Appointment> result = new ArrayList<>();
		try (Connection conn = SqlDataSource.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Appointment a = new Appointment(
					rs.getString("appointment_id"),
					rs.getString("patient_id"),
					rs.getString("doctor_id"),
					rs.getTimestamp("appointment_datetime").toLocalDateTime(),
					rs.getString("reason")
				);
				a.setStatus(rs.getString("status"));
				result.add(a);
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void insert(Appointment a) {
		String sql = "INSERT INTO appointments(appointment_id, patient_id, doctor_id, appointment_datetime, status, reason) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = SqlDataSource.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, a.getAppointmentId());
			ps.setString(2, a.getPatientId());
			ps.setString(3, a.getDoctorId());
			ps.setTimestamp(4, java.sql.Timestamp.valueOf(a.getAppointmentDateTime()));
			ps.setString(5, a.getStatus());
			ps.setString(6, a.getReason());
			ps.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void cancel(String appointmentId) {
		String sql = "UPDATE appointments SET status='Cancelled' WHERE appointment_id=?";
		try (Connection conn = SqlDataSource.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, appointmentId);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isDoctorAvailable(String doctorId, LocalDateTime dateTime) {
		String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id=? AND status='Scheduled' AND appointment_datetime BETWEEN ? AND ?";
		try (Connection conn = SqlDataSource.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, doctorId);
			ps.setTimestamp(2, java.sql.Timestamp.valueOf(dateTime.minusMinutes(59)));
			ps.setTimestamp(3, java.sql.Timestamp.valueOf(dateTime.plusMinutes(59)));
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) == 0;
				}
			}
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
} 