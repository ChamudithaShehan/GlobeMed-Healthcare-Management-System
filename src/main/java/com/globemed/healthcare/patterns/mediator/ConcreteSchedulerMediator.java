package com.globemed.healthcare.patterns.mediator;

import com.globemed.healthcare.dao.AppointmentDao;
import com.globemed.healthcare.dao.PatientDao;
import com.globemed.healthcare.dao.UserDao;
import com.globemed.healthcare.model.Appointment;
import com.globemed.healthcare.model.PatientRecord;
import com.globemed.healthcare.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ConcreteSchedulerMediator implements SchedulerMediator {
	private final AppointmentDao appointmentDao;
	private final UserDao userDao;
	private final PatientDao patientDao;

	public ConcreteSchedulerMediator() {
		this.appointmentDao = new AppointmentDao();
		this.userDao = new UserDao();
		this.patientDao = new PatientDao();
	}

	@Override
	public boolean bookAppointment(String patientId, String doctorId, LocalDateTime dateTime, String reason) {
		if (!isSlotAvailable(doctorId, dateTime)) {
			System.out.println("Booking failed: Slot is not available.");
			return false;
		}

		String appointmentId = "A" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
		Appointment newAppointment = new Appointment(appointmentId, patientId, doctorId, dateTime, reason);
		appointmentDao.insert(newAppointment);
		System.out.println("Booking successful for appointment ID: " + appointmentId);
		return true;
	}

	@Override
	public void cancelAppointment(String appointmentId) {
		appointmentDao.cancel(appointmentId);
		System.out.println("Cancelled appointment ID: " + appointmentId);
	}

	@Override
	public List<Appointment> getAppointmentsForDoctor(String doctorId) {
		return appointmentDao.listAll().stream()
				.filter(a -> a.getDoctorId().equals(doctorId))
				.collect(Collectors.toList());
	}

	@Override
	public List<Appointment> getAppointmentsForPatient(String patientId) {
		return appointmentDao.listAll().stream()
				.filter(a -> a.getPatientId().equals(patientId))
				.collect(Collectors.toList());
	}

	@Override
	public boolean isSlotAvailable(String doctorId, LocalDateTime dateTime) {
		return appointmentDao.isDoctorAvailable(doctorId, dateTime);
	}

	@Override
	public List<User> getAvailableDoctors() {
		return userDao.listDoctors();
	}

	@Override
	public List<PatientRecord> getRegisteredPatients() {
		return patientDao.listAll();
	}
}
