package com.bloodbank.model;

import java.time.LocalDate;

public class Patient {
    private String patientNo;
    private String name;
    private LocalDate dob;
    private String address;
    private String city;
    private String contactNo;
    private String pin;
    private String bloodGroup;
    private LocalDate requestDate;

    public Patient() {}

    public Patient(String patientNo, String name, LocalDate dob, String address,
                   String city, String contactNo, String pin,
                   String bloodGroup, LocalDate requestDate) {
        this.patientNo   = patientNo;
        this.name        = name;
        this.dob         = dob;
        this.address     = address;
        this.city        = city;
        this.contactNo   = contactNo;
        this.pin         = pin;
        this.bloodGroup  = bloodGroup;
        this.requestDate = requestDate;
    }

    public String getPatientNo()        { return patientNo; }
    public String getName()             { return name; }
    public LocalDate getDob()           { return dob; }
    public String getAddress()          { return address; }
    public String getCity()             { return city; }
    public String getContactNo()        { return contactNo; }
    public String getPin()              { return pin; }
    public String getBloodGroup()       { return bloodGroup; }
    public LocalDate getRequestDate()   { return requestDate; }

    public void setPatientNo(String v)       { patientNo = v; }
    public void setName(String v)            { name = v; }
    public void setDob(LocalDate v)          { dob = v; }
    public void setAddress(String v)         { address = v; }
    public void setCity(String v)            { city = v; }
    public void setContactNo(String v)       { contactNo = v; }
    public void setPin(String v)             { pin = v; }
    public void setBloodGroup(String v)      { bloodGroup = v; }
    public void setRequestDate(LocalDate v)  { requestDate = v; }

    @Override
    public String toString() {
        return String.format("Patient[%s | %s | %s | %s]",
                patientNo, name, bloodGroup, contactNo);
    }
}
