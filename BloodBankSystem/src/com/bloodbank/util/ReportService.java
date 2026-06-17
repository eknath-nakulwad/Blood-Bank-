package com.bloodbank.util;

import com.bloodbank.dao.*;
import com.bloodbank.model.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * ReportService mirrors all reports described in the project:
 *  - Donor Report
 *  - Staff Report
 *  - Issue Report
 *  - Daily Blood Collection Report
 *  - Blood Inventory Summary
 */
public class ReportService {

    private static final DonorDAO         donorDAO     = new DonorDAO();
    private static final StaffDAO         staffDAO     = new StaffDAO();
    private static final BloodIssueDAO    issueDAO     = new BloodIssueDAO();
    private static final BloodDonationDAO donationDAO  = new BloodDonationDAO();
    private static final BloodInventoryDAO inventoryDAO = new BloodInventoryDAO();

    private static final String SEP = "─".repeat(90);

    // ── Donor Report ─────────────────────────────────────────
    public static void printDonorReport() throws SQLException {
        List<Donor> donors = donorDAO.getAllDonors();
        System.out.println("\n" + SEP);
        System.out.printf("  %-12s %-25s %-6s %-8s %-15s %-12s%n",
                "DonorNo", "Name", "Gender", "Blood", "Contact", "LastDonated");
        System.out.println(SEP);
        for (Donor d : donors) {
            System.out.printf("  %-12s %-25s %-6s %-8s %-15s %-12s%n",
                    d.getDonorNumber(), d.getName(), d.getGender(),
                    d.getBloodGroup(), d.getContactNo(),
                    d.getLastDonated() != null ? d.getLastDonated().toString() : "Never");
        }
        System.out.println(SEP);
        System.out.println("  Total donors: " + donors.size());
        System.out.println(SEP + "\n");
    }

    // ── Staff Report ─────────────────────────────────────────
    public static void printStaffReport() throws SQLException {
        List<Staff> staffList = staffDAO.getAllStaff();
        System.out.println("\n" + SEP);
        System.out.printf("  %-10s %-25s %-12s %-20s %-10s%n",
                "EmpNo", "Name", "Contact", "Designation", "Role");
        System.out.println(SEP);
        for (Staff s : staffList) {
            System.out.printf("  %-10s %-25s %-12s %-20s %-10s%n",
                    s.getEmployeeNumber(), s.getName(), s.getContactNo(),
                    s.getDesignation(), s.getRole());
        }
        System.out.println(SEP);
        System.out.println("  Total staff: " + staffList.size());
        System.out.println(SEP + "\n");
    }

    // ── Issue Report ──────────────────────────────────────────
    public static void printIssueReport(LocalDate from, LocalDate to) throws SQLException {
        List<BloodIssue> issues = issueDAO.getIssuesByDateRange(from, to);
        System.out.println("\n  === Issue Report: " + from + " to " + to + " ===");
        System.out.println(SEP);
        System.out.printf("  %-5s %-12s %-12s %-8s %-8s %-12s%n",
                "ID", "Date", "PatientNo", "Blood", "Units", "IssueType");
        System.out.println(SEP);
        for (BloodIssue i : issues) {
            System.out.printf("  %-5d %-12s %-12s %-8s %-8d %-12s%n",
                    i.getId(), i.getIssueDate(), i.getPatientNo(),
                    i.getBloodGroup(), i.getNoOfUnits(), i.getIssueType());
        }
        System.out.println(SEP);
        System.out.println("  Total issues: " + issues.size());
        System.out.println(SEP + "\n");
    }

    // ── Daily Blood Collection Report ─────────────────────────
    public static void printDailyCollectionReport(LocalDate date) throws SQLException {
        List<BloodDonation> donations = donationDAO.getDonationsByDate(date);
        System.out.println("\n  === Daily Blood Collection: " + date + " ===");
        System.out.println(SEP);
        System.out.printf("  %-5s %-12s %-12s %-10s%n",
                "ID", "DonorNo", "BloodUnit", "Units");
        System.out.println(SEP);
        int total = 0;
        for (BloodDonation d : donations) {
            System.out.printf("  %-5d %-12s %-12s %-10d%n",
                    d.getId(), d.getDonorNumber(), d.getBloodUnitNo(), d.getNoOfUnits());
            total += d.getNoOfUnits();
        }
        System.out.println(SEP);
        System.out.println("  Total units collected today: " + total);
        System.out.println(SEP + "\n");
    }

    // ── Inventory Summary Report ──────────────────────────────
    public static void printInventorySummary() throws SQLException {
        List<String[]> rows = inventoryDAO.getInventorySummary();
        System.out.println("\n  === Blood Inventory Summary ===");
        System.out.println(SEP);
        System.out.printf("  %-12s %-10s%n", "Blood Group", "Total Units");
        System.out.println(SEP);
        for (String[] row : rows) {
            System.out.printf("  %-12s %-10s%n", row[0], row[1]);
        }
        System.out.println(SEP + "\n");
    }
}
