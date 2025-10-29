
CREATE TABLE IF NOT EXISTS users (
  username VARCHAR(100) PRIMARY KEY,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL
);


CREATE TABLE IF NOT EXISTS patients (
  patient_id VARCHAR(20) PRIMARY KEY,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  date_of_birth DATE NOT NULL,
  gender VARCHAR(20) NOT NULL,
  address VARCHAR(255) NOT NULL,
  phone_number VARCHAR(50) NOT NULL,
  email VARCHAR(255) NOT NULL,
  medical_history TEXT NOT NULL,
  treatment_plan TEXT NOT NULL
);


CREATE TABLE IF NOT EXISTS appointments (
  appointment_id VARCHAR(20) PRIMARY KEY,
  patient_id VARCHAR(20) NOT NULL,
  doctor_id VARCHAR(100) NOT NULL,
  appointment_datetime TIMESTAMP NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'Scheduled',
  reason VARCHAR(255) NOT NULL,
  CONSTRAINT fk_appointments_patient
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_appointments_doctor
    FOREIGN KEY (doctor_id) REFERENCES users(username)
      ON UPDATE CASCADE ON DELETE RESTRICT
);


INSERT INTO users (username, password_hash, role) VALUES
  ('doctor', 'pass123', 'Doctor');

INSERT INTO users (username, password_hash, role) VALUES
  ('nurse', 'pass123', 'Nurse');

INSERT INTO users (username, password_hash, role) VALUES
  ('pharmacist', 'pass123', 'Pharmacist');


INSERT INTO patients (
  patient_id, first_name, last_name, date_of_birth, gender, address, phone_number, email, medical_history, treatment_plan
) VALUES (
  'P001', 'Sahan', 'Palawatta', '2004-05-20', 'Male', '123 Main St, Anytown, USA', '555-1234', 'sahan@gmail.com', 'Allergic to penicillin.', ''
);

INSERT INTO patients (
  patient_id, first_name, last_name, date_of_birth, gender, address, phone_number, email, medical_history, treatment_plan
) VALUES (
  'P002', 'Kamal', 'Silva', '2002-08-15', 'Female', '456 Oak Ave, Anytown, USA', '555-5678', 'Kamal@gmail.com', 'No known allergies.', 'Annual check-up scheduled.'
);


INSERT INTO appointments (
  appointment_id, patient_id, doctor_id, appointment_datetime, status, reason
) VALUES (
  'A001', 'P001', 'doctor', '2025-08-21 10:00:00', 'Scheduled', 'Annual Checkup'
); 