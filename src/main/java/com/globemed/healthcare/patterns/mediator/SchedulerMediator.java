package com.globemed.healthcare.patterns.mediator;

import com.globemed.healthcare.model.Appointment;
import com.globemed.healthcare.model.PatientRecord;
import com.globemed.healthcare.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface SchedulerMediator {
    boolean bookAppointment(String patientId, String doctorId, LocalDateTime dateTime, String reason);
    void cancelAppointment(String appointmentId);
    List<Appointment> getAppointmentsForDoctor(String doctorId);
    List<Appointment> getAppointmentsForPatient(String patientId);
    boolean isSlotAvailable(String doctorId, LocalDateTime dateTime);
    List<User> getAvailableDoctors();
    List<PatientRecord> getRegisteredPatients();
}
