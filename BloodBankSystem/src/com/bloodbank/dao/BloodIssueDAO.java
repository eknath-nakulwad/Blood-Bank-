package com.bloodbank.dao;

import com.bloodbank.model.BloodIssue;
import com.bloodbank.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BloodIssueDAO {

    private final BloodInventoryDAO inventoryDAO = new BloodInventoryDAO();

    /**
     * Issues blood to a patient AND decrements inventory in one atomic transaction.
     * Throws if insufficient stock exists.
     */
    public boolean issueBlood(BloodIssue issue) throws SQLException {
        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);
        try {
            // Decrement stock first — will throw if insufficient
            boolean stocked = inventoryDAO.decrementStock(con, issue.getBloodUnitNo(), issue.getNoOfUnits());
            if (!stocked) {
                throw new SQLException("Insufficient blood stock for unit: " + issue.getBloodUnitNo());
            }

            String sql = "INSERT INTO blood_issue (issue_date, patient_no, blood_unit_no, blood_group, " +
                         "no_of_units, issue_type, issued_by) VALUES (?,?,?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDate(1,   Date.valueOf(issue.getIssueDate()));
                ps.setString(2, issue.getPatientNo());
                ps.setString(3, issue.getBloodUnitNo());
                ps.setString(4, issue.getBloodGroup());
                ps.setInt(5,    issue.getNoOfUnits());
                ps.setString(6, issue.getIssueType());
                ps.setString(7, issue.getIssuedBy());
                ps.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public List<BloodIssue> getAllIssues() throws SQLException {
        List<BloodIssue> list = new ArrayList<>();
        String sql = "SELECT * FROM blood_issue ORDER BY issue_date DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st  = con.createStatement();
             ResultSet rs  = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<BloodIssue> getIssuesByPatient(String patientNo) throws SQLException {
        List<BloodIssue> list = new ArrayList<>();
        String sql = "SELECT * FROM blood_issue WHERE patient_no=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, patientNo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    /** Issue report for a date range — used in daily/monthly reports. */
    public List<BloodIssue> getIssuesByDateRange(LocalDate from, LocalDate to) throws SQLException {
        List<BloodIssue> list = new ArrayList<>();
        String sql = "SELECT * FROM blood_issue WHERE issue_date BETWEEN ? AND ? ORDER BY issue_date";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private BloodIssue mapRow(ResultSet rs) throws SQLException {
        BloodIssue i = new BloodIssue();
        i.setId(rs.getInt("id"));
        i.setIssueDate(rs.getDate("issue_date").toLocalDate());
        i.setPatientNo(rs.getString("patient_no"));
        i.setBloodUnitNo(rs.getString("blood_unit_no"));
        i.setBloodGroup(rs.getString("blood_group"));
        i.setNoOfUnits(rs.getInt("no_of_units"));
        i.setIssueType(rs.getString("issue_type"));
        i.setIssuedBy(rs.getString("issued_by"));
        return i;
    }
}
