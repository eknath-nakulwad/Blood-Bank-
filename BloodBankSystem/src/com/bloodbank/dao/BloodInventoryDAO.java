package com.bloodbank.dao;

import com.bloodbank.model.BloodInventory;
import com.bloodbank.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BloodInventoryDAO {

    public boolean addBloodUnit(BloodInventory b) throws SQLException {
        String sql = "INSERT INTO blood_inventory (blood_unit_no, blood_group, no_of_units, " +
                     "pack_type, collection_date, expiry_date) VALUES (?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, b.getBloodUnitNo());
            ps.setString(2, b.getBloodGroup());
            ps.setInt(3,    b.getNoOfUnits());
            ps.setString(4, b.getPackType());
            ps.setDate(5,   Date.valueOf(b.getCollectionDate()));
            ps.setDate(6,   Date.valueOf(b.getExpiryDate()));

            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateUnits(String bloodUnitNo, int units) throws SQLException {
        String sql = "UPDATE blood_inventory SET no_of_units=? WHERE blood_unit_no=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, units);
            ps.setString(2, bloodUnitNo);
            return ps.executeUpdate() > 0;
        }
    }

    /** Decrement stock — called during blood issue (transaction-safe). */
    public boolean decrementStock(Connection con, String bloodUnitNo, int units) throws SQLException {
        String sql = "UPDATE blood_inventory SET no_of_units = no_of_units - ? " +
                     "WHERE blood_unit_no=? AND no_of_units >= ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, units);
            ps.setString(2, bloodUnitNo);
            ps.setInt(3, units);
            return ps.executeUpdate() > 0;
        }
    }

    /** Increment stock — called after blood donation. */
    public boolean incrementStock(Connection con, String bloodUnitNo, int units) throws SQLException {
        String sql = "UPDATE blood_inventory SET no_of_units = no_of_units + ? WHERE blood_unit_no=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, units);
            ps.setString(2, bloodUnitNo);
            return ps.executeUpdate() > 0;
        }
    }

    public BloodInventory findByUnitNo(String bloodUnitNo) throws SQLException {
        String sql = "SELECT * FROM blood_inventory WHERE blood_unit_no=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, bloodUnitNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public List<BloodInventory> getAllInventory() throws SQLException {
        List<BloodInventory> list = new ArrayList<>();
        String sql = "SELECT * FROM blood_inventory ORDER BY blood_group";
        try (Connection con = DBConnection.getConnection();
             Statement st  = con.createStatement();
             ResultSet rs  = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    /** Summary report: total units per blood group. */
    public List<String[]> getInventorySummary() throws SQLException {
        List<String[]> rows = new ArrayList<>();
        String sql = "SELECT blood_group, SUM(no_of_units) as total FROM blood_inventory GROUP BY blood_group";
        try (Connection con = DBConnection.getConnection();
             Statement st  = con.createStatement();
             ResultSet rs  = st.executeQuery(sql)) {
            while (rs.next()) {
                rows.add(new String[]{ rs.getString("blood_group"), rs.getString("total") });
            }
        }
        return rows;
    }

    private BloodInventory mapRow(ResultSet rs) throws SQLException {
        BloodInventory b = new BloodInventory();
        b.setBloodUnitNo(rs.getString("blood_unit_no"));
        b.setBloodGroup(rs.getString("blood_group"));
        b.setNoOfUnits(rs.getInt("no_of_units"));
        b.setPackType(rs.getString("pack_type"));
        b.setCollectionDate(rs.getDate("collection_date").toLocalDate());
        b.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
        return b;
    }
}
