package com.bloodbank.auth;

import com.bloodbank.dao.StaffDAO;
import com.bloodbank.model.Staff;

import java.sql.SQLException;

/**
 * Manages login session.  In a console/desktop app there is one active
 * session per JVM run; swap this for HTTP session management if you move to a web layer.
 */
public class AuthService {

    private static Staff loggedInUser = null;
    private static final StaffDAO staffDAO = new StaffDAO();

    private AuthService() {}

    public static boolean login(String email, String password) {
        try {
            Staff s = staffDAO.authenticate(email, password);
            if (s != null) {
                loggedInUser = s;
                System.out.println("Login successful. Welcome, " + s.getName() + " [" + s.getRole() + "]");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
        }
        System.out.println("Invalid credentials.");
        return false;
    }

    public static void logout() {
        if (loggedInUser != null) {
            System.out.println("Logged out: " + loggedInUser.getName());
            loggedInUser = null;
        }
    }

    public static Staff getCurrentUser() { return loggedInUser; }
    public static boolean isLoggedIn()   { return loggedInUser != null; }

    /** Returns true only if the current user has Admin role. */
    public static boolean isAdmin() {
        return loggedInUser != null && "Admin".equalsIgnoreCase(loggedInUser.getRole());
    }

    /** Throws SecurityException if no user is logged in. */
    public static void requireLogin() {
        if (!isLoggedIn()) throw new SecurityException("You must be logged in to perform this action.");
    }

    /** Throws SecurityException if the current user is not an Admin. */
    public static void requireAdmin() {
        requireLogin();
        if (!isAdmin()) throw new SecurityException("Admin privileges required.");
    }
}
