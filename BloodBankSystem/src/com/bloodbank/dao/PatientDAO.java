package com.bloodbank.dao;

import com.bloodbank.model.Patient;
import com.bloodbank.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public boolean addPatient(Patient p) throws SQLException {
        String sql = "INSERT INTO patient (patient_no, name, dob, address, city, contact_no, " +
                     "pin, blood_group, request_date) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getPatientNo());
            ps.setString(2, p.getName());
            ps.setDate(3,   p.getDob() != null ? Date.valueOf(p.getDob()) : null);
            ps.setString(4, p.getAddress());
            ps.setString(5, p.getCity());
            ps.setString(6, p.getContactNo());
            ps.setString(7, p.getPin());
            ps.setString(8, p.getBloodGroup());
            ps.setDate(9,   Date.valueOf(p.getRequestDate()));

            return ps.executeUpdate() > 0;
        }
    }

    public boolean updatePatient(Patient p) throws SQLException {
        String sql = "UPDATE patient SET name=?, dob=?, address=?, city=?, contact_no=?, " +
                     "pin=?, blood_group=?, request_date=? WHERE patient_no=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getName());
            ps.setDate(2,   p.getDob() != null ? Date.valueOf(p.getDob()) : null);
            ps.setString(3, p.getAddress());
            ps.setString(4, p.getCity());
            ps.setString(5, p.getContactNo());
            ps.setString(6, p.getPin());
            ps.setString(7, p.getBloodGroup());
            ps.setDate(8,   Date.valueOf(p.getRequestDate()));
            ps.setString(9, p.getPatientNo());

            return ps.executeUpdate() > 0;
        }
    }

    public Patient findByPatientNo(String patientNo) throws SQLException {
        String sql = "SELECT * FROM patient WHERE patient_no=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, patientNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        }
        return null;
    }

    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patient ORDER BY request_date DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st  = con.createStatement();
             ResultSet rs  = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    private Patient mapRow(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setPatientNo(rs.getString("patient_no"));
        p.setName(rs.getString("name"));
        Date dob = rs.getDate("dob");
        if (dob != null) p.setDob(dob.toLocalDate());
        p.setAddress(rs.getString("address"));
        p.setCity(rs.getString("city"));
        p.setContactNo(rs.getString("contact_no"));
        p.setPin(rs.getString("pin"));
        p.setBloodGroup(rs.getString("blood_group"));
        p.setRequestDate(rs.getDate("request_date").toLocalDate());
        return p;
    }
}
