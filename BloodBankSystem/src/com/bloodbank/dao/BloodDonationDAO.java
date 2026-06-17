package com.bloodbank.dao;

import com.bloodbank.model.BloodDonation;
import com.bloodbank.model.BloodIssue;
import com.bloodbank.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// ── Blood Donation DAO ────────────────────────────────────────
public class BloodDonationDAO {

    private final BloodInventoryDAO inventoryDAO = new BloodInventoryDAO();
    private final DonorDAO donorDAO = new DonorDAO();

    /**
     * Records a donation AND increments inventory in a single transaction.
     * Enforces 90-day minimum interval between donations.
     */
    public boolean recordDonation(BloodDonation donation) throws SQLException {
        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);
        try {
            // Check 90-day rule
            if (donation.getTimeElapsed() < 90) {
                throw new SQLException("Donor is not eligible: less than 90 days since last donation.");
            }

            // Insert donation record
            String sql = "INSERT INTO blood_donation (donation_date, donor_number, blood_unit_no, " +
                         "no_of_units, age_at_donation, time_elapsed) VALUES (?,?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDate(1,   Date.valueOf(donation.getDonationDate()));
                ps.setString(2, donation.getDonorNumber());
                ps.setString(3, donation.getBloodUnitNo());
                ps.setInt(4,    donation.getNoOfUnits());
                ps.setInt(5,    donation.getAgeAtDonation());
                ps.setInt(6,    donation.getTimeElapsed());
                ps.executeUpdate();
            }

            // Increment blood inventory
            inventoryDAO.incrementStock(con, donation.getBloodUnitNo(), donation.getNoOfUnits());

            // Update donor's last_donated date
            donorDAO.updateLastDonated(donation.getDonorNumber(), donation.getDonationDate());

            con.commit();
            return true;

        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public List<BloodDonation> getAllDonations() throws SQLException {
        List<BloodDonation> list = new ArrayList<>();
        String sql = "SELECT * FROM blood_donation ORDER BY donation_date DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st  = con.createStatement();
             ResultSet rs  = st.executeQuery(sql)) {
            while (rs.next()) {
                BloodDonation d = new BloodDonation();
                d.setId(rs.getInt("id"));
                d.setDonationDate(rs.getDate("donation_date").toLocalDate());
                d.setDonorNumber(rs.getString("donor_number"));
                d.setBloodUnitNo(rs.getString("blood_unit_no"));
                d.setNoOfUnits(rs.getInt("no_of_units"));
                d.setAgeAtDonation(rs.getInt("age_at_donation"));
                d.setTimeElapsed(rs.getInt("time_elapsed"));
                list.add(d);
            }
        }
        return list;
    }

    /** Daily collection report for a given date. */
    public List<BloodDonation> getDonationsByDate(java.time.LocalDate date) throws SQLException {
        List<BloodDonation> list = new ArrayList<>();
        String sql = "SELECT * FROM blood_donation WHERE donation_date=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BloodDonation d = new BloodDonation();
                d.setId(rs.getInt("id"));
                d.setDonationDate(rs.getDate("donation_date").toLocalDate());
                d.setDonorNumber(rs.getString("donor_number"));
                d.setBloodUnitNo(rs.getString("blood_unit_no"));
                d.setNoOfUnits(rs.getInt("no_of_units"));
                list.add(d);
            }
        }
        return list;
    }
}
