package com.bloodbank.model;

import java.time.LocalDate;

public class Donor {
    private String donorNumber;
    private String name;
    private LocalDate dob;
    private String gender;
    private String address;
    private String city;
    private String pin;
    private String state;
    private String contactNo;
    private String emailId;
    private String bloodGroup;
    private String fathersName;
    private String occupation;
    private LocalDate lastDonated;
    private boolean isActive;

    public Donor() {}

    public Donor(String donorNumber, String name, LocalDate dob, String gender,
                 String address, String city, String pin, String state,
                 String contactNo, String emailId, String bloodGroup,
                 String fathersName, String occupation) {
        this.donorNumber  = donorNumber;
        this.name         = name;
        this.dob          = dob;
        this.gender       = gender;
        this.address      = address;
        this.city         = city;
        this.pin          = pin;
        this.state        = state;
        this.contactNo    = contactNo;
        this.emailId      = emailId;
        this.bloodGroup   = bloodGroup;
        this.fathersName  = fathersName;
        this.occupation   = occupation;
        this.isActive     = true;
    }

    // ---- Getters ----
    public String getDonorNumber()  { return donorNumber; }
    public String getName()         { return name; }
    public LocalDate getDob()       { return dob; }
    public String getGender()       { return gender; }
    public String getAddress()      { return address; }
    public String getCity()         { return city; }
    public String getPin()          { return pin; }
    public String getState()        { return state; }
    public String getContactNo()    { return contactNo; }
    public String getEmailId()      { return emailId; }
    public String getBloodGroup()   { return bloodGroup; }
    public String getFathersName()  { return fathersName; }
    public String getOccupation()   { return occupation; }
    public LocalDate getLastDonated(){ return lastDonated; }
    public boolean isActive()       { return isActive; }

    // ---- Setters ----
    public void setDonorNumber(String v)   { donorNumber = v; }
    public void setName(String v)          { name = v; }
    public void setDob(LocalDate v)        { dob = v; }
    public void setGender(String v)        { gender = v; }
    public void setAddress(String v)       { address = v; }
    public void setCity(String v)          { city = v; }
    public void setPin(String v)           { pin = v; }
    public void setState(String v)         { state = v; }
    public void setContactNo(String v)     { contactNo = v; }
    public void setEmailId(String v)       { emailId = v; }
    public void setBloodGroup(String v)    { bloodGroup = v; }
    public void setFathersName(String v)   { fathersName = v; }
    public void setOccupation(String v)    { occupation = v; }
    public void setLastDonated(LocalDate v){ lastDonated = v; }
    public void setActive(boolean v)       { isActive = v; }

    @Override
    public String toString() {
        return String.format("Donor[%s | %s | %s | %s]",
                donorNumber, name, bloodGroup, contactNo);
    }
}
