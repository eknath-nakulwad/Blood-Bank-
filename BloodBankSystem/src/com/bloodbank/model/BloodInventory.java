package com.bloodbank.model;

import java.time.LocalDate;

// ── Blood Inventory ──────────────────────────────────────────
public class BloodInventory {
    private String bloodUnitNo;
    private String bloodGroup;
    private int noOfUnits;
    private String packType;
    private LocalDate collectionDate;
    private LocalDate expiryDate;

    public BloodInventory() {}

    public BloodInventory(String bloodUnitNo, String bloodGroup, int noOfUnits,
                          String packType, LocalDate collectionDate, LocalDate expiryDate) {
        this.bloodUnitNo    = bloodUnitNo;
        this.bloodGroup     = bloodGroup;
        this.noOfUnits      = noOfUnits;
        this.packType       = packType;
        this.collectionDate = collectionDate;
        this.expiryDate     = expiryDate;
    }

    public String getBloodUnitNo()        { return bloodUnitNo; }
    public String getBloodGroup()         { return bloodGroup; }
    public int getNoOfUnits()             { return noOfUnits; }
    public String getPackType()           { return packType; }
    public LocalDate getCollectionDate()  { return collectionDate; }
    public LocalDate getExpiryDate()      { return expiryDate; }

    public void setBloodUnitNo(String v)       { bloodUnitNo = v; }
    public void setBloodGroup(String v)        { bloodGroup = v; }
    public void setNoOfUnits(int v)            { noOfUnits = v; }
    public void setPackType(String v)          { packType = v; }
    public void setCollectionDate(LocalDate v) { collectionDate = v; }
    public void setExpiryDate(LocalDate v)     { expiryDate = v; }

    @Override
    public String toString() {
        return String.format("Blood[%s | %s | Units:%d | Expires:%s]",
                bloodUnitNo, bloodGroup, noOfUnits, expiryDate);
    }
}
