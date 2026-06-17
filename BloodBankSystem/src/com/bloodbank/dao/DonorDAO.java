package com.bloodbank.dao;

import com.bloodbank.model.Donor;
import com.bloodbank.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Donor — INSERT, UPDATE, DELETE, SELECT.
 * All DB operations are transaction-safe via PreparedStatement.
 */
public class DonorDAO {

    // ── INSERT ────────────────────────────────────────────────
    public boolean addDonor(Donor d) throws SQLException {
        String sql = "INSERT INTO donor (donor_number, name, dob, gender, address, city, pin, " +
                     "state, contact_no, email_id, blood_group, fathers_name, occupation) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1,  d.getDonorNumber());
            ps.setString(2,  d.getName());
            ps.setDate(3,    Date.valueOf(d.getDob()));
            ps.setString(4,  d.getGender());
            ps.setString(5,  d.getAddress());
            ps.setString(6,  d.getCity());
            ps.setString(7,  d.getPin());
            ps.setString(8,  d.getState());
            ps.setString(9,  d.getContactNo());
            ps.setString(10, d.getEmailId());
            ps.setString(11, d.getBloodGroup());
            ps.setString(12, d.getFathersName());
            ps.setString(13, d.getOccupation());

            return ps.executeUpdate() > 0;
        }
    }

    // ── UPDATE ────────────────────────────────────────────────
    public boolean updateDonor(Donor d) throws SQLException {
        String sql = "UPDATE donor SET name=?, dob=?, gender=?, address=?, city=?, pin=?, " +
                     "state=?, contact_no=?, email_id=?, blood_group=?, fathers_name=?, " +
                     "occupation=? WHERE donor_number=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1,  d.getName());
            ps.setDate(2,    Date.valueOf(d.getDob()));
            ps.setString(3,  d.getGender());
            ps.setString(4,  d.getAddress());
            ps.setString(5,  d.getCity());
            ps.setString(6,  d.getPin());
            ps.setString(7,  d.getState());
            ps.setString(8,  d.getContactNo());
            ps.setString(9,  d.getEmailId());
            ps.setString(10, d.getBloodGroup());
            ps.setString(11, d.getFathersName());
            ps.setString(12, d.getOccupation());
            ps.setString(13, d.getDonorNumber());

            return ps.executeUpdate() > 0;
        }
    }

    // ── DELETE ────────────────────────────────────────────────
    public boolean deleteDonor(String donorNumber) throws SQLException {
        String sql = "UPDATE donor SET is_active=FALSE WHERE donor_number=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, donorNumber);
            return ps.executeUpdate() > 0;
        }
    }

    // ── FIND BY ID ────────────────────────────────────────────
    public Donor findByDonorNumber(String donorNumber) throws SQLException {
        String sql = "SELECT * FROM donor WHERE donor_number=? AND is_active=TRUE";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, donorNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    // ── SEARCH BY NAME ────────────────────────────────────────
    public List<Donor> searchByName(String name) throws SQLException {
        String sql = "SELECT * FROM donor WHERE name LIKE ? AND is_active=TRUE";
        List<Donor> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // ── SEARCH BY BLOOD GROUP ─────────────────────────────────
    public List<Donor> searchByBloodGroup(String bloodGroup) throws SQLException {
        String sql = "SELECT * FROM donor WHERE blood_group=? AND is_active=TRUE";
        List<Donor> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, bloodGroup);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // ── LIST ALL ──────────────────────────────────────────────
    public List<Donor> getAllDonors() throws SQLException {
        String sql = "SELECT * FROM donor WHERE is_active=TRUE ORDER BY name";
        List<Donor> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st  = con.createStatement();
             ResultSet rs  = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    // ── UPDATE LAST DONATED DATE ──────────────────────────────
    public void updateLastDonated(String donorNumber, LocalDate date) throws SQLException {
        String sql = "UPDATE donor SET last_donated=? WHERE donor_number=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            ps.setString(2, donorNumber);
            ps.executeUpdate();
        }
    }

    // ── MAP ResultSet ROW → Donor ─────────────────────────────
    private Donor mapRow(ResultSet rs) throws SQLException {
        Donor d = new Donor();
        d.setDonorNumber(rs.getString("donor_number"));
        d.setName(rs.getString("name"));
        d.setDob(rs.getDate("dob").toLocalDate());
        d.setGender(rs.getString("gender"));
        d.setAddress(rs.getString("address"));
        d.setCity(rs.getString("city"));
        d.setPin(rs.getString("pin"));
        d.setState(rs.getString("state"));
        d.setContactNo(rs.getString("contact_no"));
        d.setEmailId(rs.getString("email_id"));
        d.setBloodGroup(rs.getString("blood_group"));
        d.setFathersName(rs.getString("fathers_name"));
        d.setOccupation(rs.getString("occupation"));
        Date ld = rs.getDate("last_donated");
        if (ld != null) d.setLastDonated(ld.toLocalDate());
        d.setActive(rs.getBoolean("is_active"));
        return d;
    }
}
