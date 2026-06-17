package com.bloodbank.model;

import java.time.LocalDate;

public class Staff {
    private String employeeNumber;
    private String name;
    private LocalDate dateOfJoining;
    private String address;
    private String gender;
    private String qualification;
    private String contactNo;
    private String designation;
    private String emailId;
    private String passwordHash;
    private String role; // "Admin" or "Staff"

    public Staff() {}

    public Staff(String employeeNumber, String name, LocalDate dateOfJoining,
                 String address, String gender, String qualification,
                 String contactNo, String designation, String emailId,
                 String passwordHash, String role) {
        this.employeeNumber = employeeNumber;
        this.name           = name;
        this.dateOfJoining  = dateOfJoining;
        this.address        = address;
        this.gender         = gender;
        this.qualification  = qualification;
        this.contactNo      = contactNo;
        this.designation    = designation;
        this.emailId        = emailId;
        this.passwordHash   = passwordHash;
        this.role           = role;
    }

    public String getEmployeeNumber()   { return employeeNumber; }
    public String getName()             { return name; }
    public LocalDate getDateOfJoining() { return dateOfJoining; }
    public String getAddress()          { return address; }
    public String getGender()           { return gender; }
    public String getQualification()    { return qualification; }
    public String getContactNo()        { return contactNo; }
    public String getDesignation()      { return designation; }
    public String getEmailId()          { return emailId; }
    public String getPasswordHash()     { return passwordHash; }
    public String getRole()             { return role; }

    public void setEmployeeNumber(String v)   { employeeNumber = v; }
    public void setName(String v)             { name = v; }
    public void setDateOfJoining(LocalDate v) { dateOfJoining = v; }
    public void setAddress(String v)          { address = v; }
    public void setGender(String v)           { gender = v; }
    public void setQualification(String v)    { qualification = v; }
    public void setContactNo(String v)        { contactNo = v; }
    public void setDesignation(String v)      { designation = v; }
    public void setEmailId(String v)          { emailId = v; }
    public void setPasswordHash(String v)     { passwordHash = v; }
    public void setRole(String v)             { role = v; }

    @Override
    public String toString() {
        return String.format("Staff[%s | %s | %s | %s]",
                employeeNumber, name, designation, role);
    }
}
