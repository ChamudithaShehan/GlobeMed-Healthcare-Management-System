package com.globemed.healthcare.patterns.memento;

import java.util.Stack;

public class PatientRecordCaretaker {
    private final Stack<PatientRecordMemento> mementoStack = new Stack<>();

    public void saveState(PatientRecordMemento memento) {
        mementoStack.push(memento);
    }

    public PatientRecordMemento undo() {
        if (!mementoStack.isEmpty()) {
            return mementoStack.pop();
        }
        return null;
    }
}
