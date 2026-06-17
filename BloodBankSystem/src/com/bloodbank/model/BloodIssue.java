package com.bloodbank.model;

import java.time.LocalDate;

public class BloodIssue {
    private int id;
    private LocalDate issueDate;
    private String patientNo;
    private String bloodUnitNo;
    private String bloodGroup;
    private int noOfUnits;
    private String issueType; // Plasma, Platelets, RBC, Whole Blood
    private String issuedBy;  // employee_number

    public BloodIssue() {}

    public BloodIssue(LocalDate issueDate, String patientNo, String bloodUnitNo,
                      String bloodGroup, int noOfUnits, String issueType, String issuedBy) {
        this.issueDate  = issueDate;
        this.patientNo  = patientNo;
        this.bloodUnitNo = bloodUnitNo;
        this.bloodGroup = bloodGroup;
        this.noOfUnits  = noOfUnits;
        this.issueType  = issueType;
        this.issuedBy   = issuedBy;
    }

    public int getId()               { return id; }
    public LocalDate getIssueDate()  { return issueDate; }
    public String getPatientNo()     { return patientNo; }
    public String getBloodUnitNo()   { return bloodUnitNo; }
    public String getBloodGroup()    { return bloodGroup; }
    public int getNoOfUnits()        { return noOfUnits; }
    public String getIssueType()     { return issueType; }
    public String getIssuedBy()      { return issuedBy; }

    public void setId(int v)                  { id = v; }
    public void setIssueDate(LocalDate v)     { issueDate = v; }
    public void setPatientNo(String v)        { patientNo = v; }
    public void setBloodUnitNo(String v)      { bloodUnitNo = v; }
    public void setBloodGroup(String v)       { bloodGroup = v; }
    public void setNoOfUnits(int v)           { noOfUnits = v; }
    public void setIssueType(String v)        { issueType = v; }
    public void setIssuedBy(String v)         { issuedBy = v; }
}
