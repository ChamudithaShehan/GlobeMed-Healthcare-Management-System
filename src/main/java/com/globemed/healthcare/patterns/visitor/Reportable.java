package com.globemed.healthcare.patterns.visitor;


public interface Reportable {
    String accept(ReportVisitor visitor);
}
