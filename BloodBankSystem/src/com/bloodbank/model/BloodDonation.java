package com.bloodbank.model;

import java.time.LocalDate;

// ── Blood Donation (Transaction) ─────────────────────────────
public class BloodDonation {
    private int id;
    private LocalDate donationDate;
    private String donorNumber;
    private String bloodUnitNo;
    private int noOfUnits;
    private int ageAtDonation;
    private int timeElapsed; // days since last donation

    public BloodDonation() {}

    public BloodDonation(LocalDate donationDate, String donorNumber,
                         String bloodUnitNo, int noOfUnits,
                         int ageAtDonation, int timeElapsed) {
        this.donationDate  = donationDate;
        this.donorNumber   = donorNumber;
        this.bloodUnitNo   = bloodUnitNo;
        this.noOfUnits     = noOfUnits;
        this.ageAtDonation = ageAtDonation;
        this.timeElapsed   = timeElapsed;
    }

    public int getId()                  { return id; }
    public LocalDate getDonationDate()  { return donationDate; }
    public String getDonorNumber()      { return donorNumber; }
    public String getBloodUnitNo()      { return bloodUnitNo; }
    public int getNoOfUnits()           { return noOfUnits; }
    public int getAgeAtDonation()       { return ageAtDonation; }
    public int getTimeElapsed()         { return timeElapsed; }

    public void setId(int v)                   { id = v; }
    public void setDonationDate(LocalDate v)   { donationDate = v; }
    public void setDonorNumber(String v)       { donorNumber = v; }
    public void setBloodUnitNo(String v)       { bloodUnitNo = v; }
    public void setNoOfUnits(int v)            { noOfUnits = v; }
    public void setAgeAtDonation(int v)        { ageAtDonation = v; }
    public void setTimeElapsed(int v)          { timeElapsed = v; }
}
