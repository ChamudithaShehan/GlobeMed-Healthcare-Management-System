package com.globemed.healthcare.patterns.memento;

import com.globemed.healthcare.model.PatientRecord;

// The Memento class stores the state of the PatientRecord.
public class PatientRecordMemento {
    private final PatientRecord patientRecordState;

    public PatientRecordMemento(PatientRecord patientRecordToSave) {
        // Create a deep copy of the state to save
        this.patientRecordState = patientRecordToSave.copy();
    }

    public PatientRecord getSavedState() {
        // Return a copy of the saved state to prevent modifications
        return patientRecordState.copy();
    }
}
