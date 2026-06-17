package com.bloodbank.dao;

import com.bloodbank.model.Staff;
import com.bloodbank.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {

    public boolean addStaff(Staff s) throws SQLException {
        String sql = "INSERT INTO staff (employee_number, name, date_of_joining, address, gender, " +
                     "qualification, contact_no, designation, email_id, password_hash, role) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, SHA2(?,256), ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1,  s.getEmployeeNumber());
            ps.setString(2,  s.getName());
            ps.setDate(3,    Date.valueOf(s.getDateOfJoining()));
            ps.setString(4,  s.getAddress());
            ps.setString(5,  s.getGender());
            ps.setString(6,  s.getQualification());
            ps.setString(7,  s.getContactNo());
            ps.setString(8,  s.getDesignation());
            ps.setString(9,  s.getEmailId());
            ps.setString(10, s.getPasswordHash()); // plain text passed; SHA2 applied in DB
            ps.setString(11, s.getRole());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateStaff(Staff s) throws SQLException {
        String sql = "UPDATE staff SET name=?, address=?, gender=?, qualification=?, " +
                     "contact_no=?, designation=?, email_id=?, role=? WHERE employee_number=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getAddress());
            ps.setString(3, s.getGender());
            ps.setString(4, s.getQualification());
            ps.setString(5, s.getContactNo());
            ps.setString(6, s.getDesignation());
            ps.setString(7, s.getEmailId());
            ps.setString(8, s.getRole());
            ps.setString(9, s.getEmployeeNumber());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteStaff(String employeeNumber) throws SQLException {
        String sql = "DELETE FROM staff WHERE employee_number=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, employeeNumber);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Authenticate: returns Staff object if credentials valid, null otherwise.
     * Password is SHA-256 hashed in-DB query.
     */
    public Staff authenticate(String emailId, String plainPassword) throws SQLException {
        String sql = "SELECT * FROM staff WHERE email_id=? AND password_hash=SHA2(?,256)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, emailId);
            ps.setString(2, plainPassword);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public Staff findByEmployeeNumber(String empNo) throws SQLException {
        String sql = "SELECT * FROM staff WHERE employee_number=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, empNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public List<Staff> getAllStaff() throws SQLException {
        List<Staff> list = new ArrayList<>();
        String sql = "SELECT * FROM staff ORDER BY name";
        try (Connection con = DBConnection.getConnection();
             Statement st  = con.createStatement();
             ResultSet rs  = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private Staff mapRow(ResultSet rs) throws SQLException {
        Staff s = new Staff();
        s.setEmployeeNumber(rs.getString("employee_number"));
        s.setName(rs.getString("name"));
        s.setDateOfJoining(rs.getDate("date_of_joining").toLocalDate());
        s.setAddress(rs.getString("address"));
        s.setGender(rs.getString("gender"));
        s.setQualification(rs.getString("qualification"));
        s.setContactNo(rs.getString("contact_no"));
        s.setDesignation(rs.getString("designation"));
        s.setEmailId(rs.getString("email_id"));
        s.setPasswordHash(rs.getString("password_hash"));
        s.setRole(rs.getString("role"));
        return s;
    }
}
