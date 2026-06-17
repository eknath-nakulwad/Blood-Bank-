package com.bloodbank.ui;

import com.bloodbank.auth.AuthService;
import com.bloodbank.dao.*;
import com.bloodbank.model.*;
import com.bloodbank.util.DBConnection;
import com.bloodbank.util.ReportService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

/**
 * Console-based UI for Blood Bank & Donor Management System.
 * Mirrors all modules described in the project report:
 *   Master Forms  : Donor, Staff, Blood Inventory, Patient
 *   Transactions  : Blood Donation, Blood Issue
 *   Reports       : Donor, Staff, Issue, Daily Collection, Inventory
 */
public class MainMenu {

    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DonorDAO          donorDAO     = new DonorDAO();
    private static final StaffDAO          staffDAO     = new StaffDAO();
    private static final BloodInventoryDAO inventoryDAO = new BloodInventoryDAO();
    private static final PatientDAO        patientDAO   = new PatientDAO();
    private static final BloodDonationDAO  donationDAO  = new BloodDonationDAO();
    private static final BloodIssueDAO     issueDAO     = new BloodIssueDAO();

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║   Blood Bank & Donor Management System       ║");
        System.out.println("║   M.S. Bidve Engineering College, Latur      ║");
        System.out.println("╚══════════════════════════════════════════════╝");

        loginMenu();
        DBConnection.closeConnection();
        System.out.println("Goodbye!");
    }

    // ── LOGIN ─────────────────────────────────────────────────
    private static void loginMenu() {
        while (true) {
            System.out.println("\n[1] Login    [0] Exit");
            System.out.print("Choice: ");
            String ch = sc.nextLine().trim();
            if ("0".equals(ch)) return;
            if ("1".equals(ch)) {
                System.out.print("Email    : ");
                String email = sc.nextLine().trim();
                System.out.print("Password : ");
                String pass = sc.nextLine().trim();
                if (AuthService.login(email, pass)) {
                    mainMenu();
                }
            }
        }
    }

    // ── MAIN MENU ─────────────────────────────────────────────
    private static void mainMenu() {
        while (true) {
            System.out.println("\n╔══ MAIN MENU ══════════════════════════╗");
            System.out.println("║ [1] Donor Management                  ║");
            System.out.println("║ [2] Staff Management   (Admin)        ║");
            System.out.println("║ [3] Blood Inventory                   ║");
            System.out.println("║ [4] Patient Management                ║");
            System.out.println("║ [5] Blood Donation (Transaction)      ║");
            System.out.println("║ [6] Blood Issue    (Transaction)      ║");
            System.out.println("║ [7] Reports                           ║");
            System.out.println("║ [0] Logout                            ║");
            System.out.println("╚═══════════════════════════════════════╝");
            System.out.print("Choice: ");
            switch (sc.nextLine().trim()) {
                case "1" -> donorMenu();
                case "2" -> staffMenu();
                case "3" -> inventoryMenu();
                case "4" -> patientMenu();
                case "5" -> donationMenu();
                case "6" -> issueMenu();
                case "7" -> reportsMenu();
                case "0" -> { AuthService.logout(); return; }
                default  -> System.out.println("Invalid option.");
            }
        }
    }

    // ── DONOR MANAGEMENT ──────────────────────────────────────
    private static void donorMenu() {
        while (true) {
            System.out.println("\n── Donor Management ──");
            System.out.println("[1] Register Donor  [2] Update Donor  [3] Search by Name");
            System.out.println("[4] Search by Blood Group  [5] List All  [6] Delete Donor  [0] Back");
            System.out.print("Choice: ");
            switch (sc.nextLine().trim()) {
                case "1" -> registerDonor();
                case "2" -> updateDonor();
                case "3" -> searchDonorByName();
                case "4" -> searchDonorByBlood();
                case "5" -> listAllDonors();
                case "6" -> deleteDonor();
                case "0" -> { return; }
                default  -> System.out.println("Invalid option.");
            }
        }
    }

    private static void registerDonor() {
        try {
            System.out.println("\n=== Register Donor ===");
            System.out.print("Donor Number   : "); String dno  = sc.nextLine().trim();
            System.out.print("Name           : "); String name = sc.nextLine().trim();
            System.out.print("DOB (yyyy-MM-dd): "); LocalDate dob = LocalDate.parse(sc.nextLine().trim(), DATE_FMT);
            System.out.print("Gender (Male/Female/Other): "); String gender = sc.nextLine().trim();
            System.out.print("Address        : "); String addr = sc.nextLine().trim();
            System.out.print("City           : "); String city = sc.nextLine().trim();
            System.out.print("PIN            : "); String pin  = sc.nextLine().trim();
            System.out.print("State          : "); String state= sc.nextLine().trim();
            System.out.print("Contact No     : "); String cont = sc.nextLine().trim();
            System.out.print("Email ID       : "); String email= sc.nextLine().trim();
            System.out.print("Blood Group (A+/A-/B+/B-/AB+/AB-/O+/O-): "); String bg = sc.nextLine().trim();
            System.out.print("Father's Name  : "); String father = sc.nextLine().trim();
            System.out.print("Occupation     : "); String occ = sc.nextLine().trim();

            Donor d = new Donor(dno, name, dob, gender, addr, city, pin, state, cont, email, bg, father, occ);
            if (donorDAO.addDonor(d)) System.out.println("✔  Donor registered successfully.");
            else System.out.println("✗  Registration failed.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void updateDonor() {
        try {
            System.out.print("Enter Donor Number to update: ");
            String dno = sc.nextLine().trim();
            Donor d = donorDAO.findByDonorNumber(dno);
            if (d == null) { System.out.println("Donor not found."); return; }

            System.out.println("Current: " + d);
            System.out.print("New Name (enter to skip): "); String name = sc.nextLine().trim();
            System.out.print("New Contact No (enter to skip): "); String cont = sc.nextLine().trim();
            System.out.print("New Blood Group (enter to skip): "); String bg = sc.nextLine().trim();

            if (!name.isEmpty()) d.setName(name);
            if (!cont.isEmpty()) d.setContactNo(cont);
            if (!bg.isEmpty())   d.setBloodGroup(bg);

            if (donorDAO.updateDonor(d)) System.out.println("✔  Donor updated.");
            else System.out.println("✗  Update failed.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void searchDonorByName() {
        try {
            System.out.print("Enter name to search: ");
            String name = sc.nextLine().trim();
            List<Donor> list = donorDAO.searchByName(name);
            printDonorList(list);
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    private static void searchDonorByBlood() {
        try {
            System.out.print("Enter blood group: ");
            String bg = sc.nextLine().trim();
            List<Donor> list = donorDAO.searchByBloodGroup(bg);
            printDonorList(list);
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    private static void listAllDonors() {
        try { printDonorList(donorDAO.getAllDonors()); }
        catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    private static void deleteDonor() {
        try {
            System.out.print("Enter Donor Number to deactivate: ");
            String dno = sc.nextLine().trim();
            if (donorDAO.deleteDonor(dno)) System.out.println("✔  Donor deactivated.");
            else System.out.println("✗  Not found.");
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    private static void printDonorList(List<Donor> list) {
        if (list.isEmpty()) { System.out.println("No donors found."); return; }
        System.out.printf("  %-12s %-25s %-6s %-8s %-15s%n", "DonorNo","Name","Gender","Blood","Contact");
        list.forEach(d -> System.out.printf("  %-12s %-25s %-6s %-8s %-15s%n",
                d.getDonorNumber(), d.getName(), d.getGender(), d.getBloodGroup(), d.getContactNo()));
    }

    // ── STAFF MANAGEMENT ──────────────────────────────────────
    private static void staffMenu() {
        try { AuthService.requireAdmin(); }
        catch (SecurityException e) { System.out.println("⛔  " + e.getMessage()); return; }

        while (true) {
            System.out.println("\n── Staff Management (Admin) ──");
            System.out.println("[1] Add Staff  [2] Update Staff  [3] List All  [4] Delete Staff  [0] Back");
            System.out.print("Choice: ");
            switch (sc.nextLine().trim()) {
                case "1" -> addStaff();
                case "2" -> updateStaff();
                case "3" -> listAllStaff();
                case "4" -> deleteStaff();
                case "0" -> { return; }
                default  -> System.out.println("Invalid option.");
            }
        }
    }

    private static void addStaff() {
        try {
            System.out.println("\n=== Add Staff ===");
            System.out.print("Employee Number : "); String empNo = sc.nextLine().trim();
            System.out.print("Name            : "); String name  = sc.nextLine().trim();
            System.out.print("Date of Joining (yyyy-MM-dd): "); LocalDate doj = LocalDate.parse(sc.nextLine().trim(), DATE_FMT);
            System.out.print("Address         : "); String addr  = sc.nextLine().trim();
            System.out.print("Gender          : "); String gender= sc.nextLine().trim();
            System.out.print("Qualification   : "); String qual  = sc.nextLine().trim();
            System.out.print("Contact No      : "); String cont  = sc.nextLine().trim();
            System.out.print("Designation     : "); String desig = sc.nextLine().trim();
            System.out.print("Email ID        : "); String email = sc.nextLine().trim();
            System.out.print("Password        : "); String pass  = sc.nextLine().trim();
            System.out.print("Role (Admin/Staff): "); String role= sc.nextLine().trim();

            Staff s = new Staff(empNo, name, doj, addr, gender, qual, cont, desig, email, pass, role);
            if (staffDAO.addStaff(s)) System.out.println("✔  Staff added.");
            else System.out.println("✗  Failed.");
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    private static void updateStaff() {
        try {
            System.out.print("Employee Number to update: ");
            String empNo = sc.nextLine().trim();
            Staff s = staffDAO.findByEmployeeNumber(empNo);
            if (s == null) { System.out.println("Staff not found."); return; }
            System.out.print("New Designation (enter to skip): "); String d = sc.nextLine().trim();
            System.out.print("New Contact No (enter to skip): "); String c = sc.nextLine().trim();
            if (!d.isEmpty()) s.setDesignation(d);
            if (!c.isEmpty()) s.setContactNo(c);
            if (staffDAO.updateStaff(s)) System.out.println("✔  Staff updated.");
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    private static void listAllStaff() {
        try {
            List<Staff> list = staffDAO.getAllStaff();
            System.out.printf("  %-10s %-25s %-20s %-10s%n", "EmpNo","Name","Designation","Role");
            list.forEach(s -> System.out.printf("  %-10s %-25s %-20s %-10s%n",
                    s.getEmployeeNumber(), s.getName(), s.getDesignation(), s.getRole()));
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    private static void deleteStaff() {
        try {
            System.out.print("Employee Number to delete: ");
            String empNo = sc.nextLine().trim();
            if (staffDAO.deleteStaff(empNo)) System.out.println("✔  Staff removed.");
            else System.out.println("✗  Not found.");
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    // ── BLOOD INVENTORY ───────────────────────────────────────
    private static void inventoryMenu() {
        while (true) {
            System.out.println("\n── Blood Inventory ──");
            System.out.println("[1] Add Blood Unit  [2] View All  [3] Summary  [0] Back");
            System.out.print("Choice: ");
            switch (sc.nextLine().trim()) {
                case "1" -> addBloodUnit();
                case "2" -> viewAllInventory();
                case "3" -> { try { ReportService.printInventorySummary(); } catch (SQLException e) { System.out.println(e.getMessage()); } }
                case "0" -> { return; }
                default  -> System.out.println("Invalid option.");
            }
        }
    }

    private static void addBloodUnit() {
        try {
            System.out.print("Blood Unit No  : "); String uno  = sc.nextLine().trim();
            System.out.print("Blood Group    : "); String bg   = sc.nextLine().trim();
            System.out.print("No. of Units   : "); int units   = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Pack Type (Single/Double/Triple): "); String pack = sc.nextLine().trim();
            System.out.print("Collection Date (yyyy-MM-dd): "); LocalDate col = LocalDate.parse(sc.nextLine().trim(), DATE_FMT);
            LocalDate exp = col.plusDays(35);

            BloodInventory b = new BloodInventory(uno, bg, units, pack, col, exp);
            if (inventoryDAO.addBloodUnit(b))
                System.out.println("✔  Blood unit added. Expiry: " + exp);
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    private static void viewAllInventory() {
        try {
            List<BloodInventory> list = inventoryDAO.getAllInventory();
            System.out.printf("  %-12s %-8s %-8s %-10s %-12s%n","UnitNo","Blood","Units","Pack","Expiry");
            list.forEach(b -> System.out.printf("  %-12s %-8s %-8d %-10s %-12s%n",
                    b.getBloodUnitNo(), b.getBloodGroup(), b.getNoOfUnits(), b.getPackType(), b.getExpiryDate()));
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    // ── PATIENT MANAGEMENT ────────────────────────────────────
    private static void patientMenu() {
        while (true) {
            System.out.println("\n── Patient Management ──");
            System.out.println("[1] Register Patient  [2] List All  [0] Back");
            System.out.print("Choice: ");
            switch (sc.nextLine().trim()) {
                case "1" -> registerPatient();
                case "2" -> listAllPatients();
                case "0" -> { return; }
                default  -> System.out.println("Invalid option.");
            }
        }
    }

    private static void registerPatient() {
        try {
            System.out.print("Patient No     : "); String pno  = sc.nextLine().trim();
            System.out.print("Name           : "); String name = sc.nextLine().trim();
            System.out.print("DOB (yyyy-MM-dd, or press Enter to skip): "); String dobStr = sc.nextLine().trim();
            System.out.print("Address        : "); String addr = sc.nextLine().trim();
            System.out.print("City           : "); String city = sc.nextLine().trim();
            System.out.print("Contact No     : "); String cont = sc.nextLine().trim();
            System.out.print("PIN            : "); String pin  = sc.nextLine().trim();
            System.out.print("Blood Group    : "); String bg   = sc.nextLine().trim();

            LocalDate dob = dobStr.isEmpty() ? null : LocalDate.parse(dobStr, DATE_FMT);
            Patient p = new Patient(pno, name, dob, addr, city, cont, pin, bg, LocalDate.now());
            if (patientDAO.addPatient(p)) System.out.println("✔  Patient registered.");
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    private static void listAllPatients() {
        try {
            List<Patient> list = patientDAO.getAllPatients();
            System.out.printf("  %-12s %-25s %-8s %-15s%n","PatientNo","Name","Blood","Contact");
            list.forEach(p -> System.out.printf("  %-12s %-25s %-8s %-15s%n",
                    p.getPatientNo(), p.getName(), p.getBloodGroup(), p.getContactNo()));
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    // ── BLOOD DONATION TRANSACTION ────────────────────────────
    private static void donationMenu() {
        System.out.println("\n=== Record Blood Donation ===");
        try {
            System.out.print("Donor Number   : "); String dno = sc.nextLine().trim();
            Donor donor = donorDAO.findByDonorNumber(dno);
            if (donor == null) { System.out.println("Donor not found."); return; }

            // Calculate time elapsed
            int elapsed = 9999;
            if (donor.getLastDonated() != null) {
                elapsed = (int) ChronoUnit.DAYS.between(donor.getLastDonated(), LocalDate.now());
                System.out.println("Days since last donation: " + elapsed);
                if (elapsed < 90) {
                    System.out.println("⛔  Donor not eligible (needs 90+ days). Remaining: " + (90 - elapsed) + " days.");
                    return;
                }
            }

            System.out.print("Blood Unit No  : "); String uno   = sc.nextLine().trim();
            System.out.print("No. of Units   : "); int units    = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Donor Age      : "); int age      = Integer.parseInt(sc.nextLine().trim());

            BloodDonation donation = new BloodDonation(LocalDate.now(), dno, uno, units, age, elapsed);
            donationDAO.recordDonation(donation);
            System.out.println("✔  Donation recorded. Inventory updated.");

        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    // ── BLOOD ISSUE TRANSACTION ───────────────────────────────
    private static void issueMenu() {
        System.out.println("\n=== Issue Blood to Patient ===");
        try {
            System.out.print("Patient No       : "); String pno   = sc.nextLine().trim();
            System.out.print("Blood Unit No    : "); String uno   = sc.nextLine().trim();
            System.out.print("Blood Group      : "); String bg    = sc.nextLine().trim();
            System.out.print("No. of Units     : "); int units    = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Issue Type (Plasma/Platelets/RBC/Whole Blood): "); String type = sc.nextLine().trim();

            String issuedBy = AuthService.getCurrentUser().getEmployeeNumber();
            BloodIssue issue = new BloodIssue(LocalDate.now(), pno, uno, bg, units, type, issuedBy);
            issueDAO.issueBlood(issue);
            System.out.println("✔  Blood issued. Inventory decremented.");

        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    // ── REPORTS ───────────────────────────────────────────────
    private static void reportsMenu() {
        while (true) {
            System.out.println("\n── Reports ──");
            System.out.println("[1] Donor Report   [2] Staff Report   [3] Issue Report");
            System.out.println("[4] Daily Collection Report   [5] Inventory Summary   [0] Back");
            System.out.print("Choice: ");
            switch (sc.nextLine().trim()) {
                case "1" -> { try { ReportService.printDonorReport(); } catch (SQLException e) { System.out.println(e.getMessage()); } }
                case "2" -> { try { ReportService.printStaffReport(); } catch (SQLException e) { System.out.println(e.getMessage()); } }
                case "3" -> {
                    try {
                        System.out.print("From date (yyyy-MM-dd): "); LocalDate from = LocalDate.parse(sc.nextLine().trim(), DATE_FMT);
                        System.out.print("To date   (yyyy-MM-dd): "); LocalDate to   = LocalDate.parse(sc.nextLine().trim(), DATE_FMT);
                        ReportService.printIssueReport(from, to);
                    } catch (Exception e) { System.out.println(e.getMessage()); }
                }
                case "4" -> {
                    try {
                        System.out.print("Date (yyyy-MM-dd): "); LocalDate d = LocalDate.parse(sc.nextLine().trim(), DATE_FMT);
                        ReportService.printDailyCollectionReport(d);
                    } catch (Exception e) { System.out.println(e.getMessage()); }
                }
                case "5" -> { try { ReportService.printInventorySummary(); } catch (SQLException e) { System.out.println(e.getMessage()); } }
                case "0" -> { return; }
                default  -> System.out.println("Invalid option.");
            }
        }
    }
}
