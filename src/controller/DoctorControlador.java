package controller;

import model.DatabaseConection;
import model.Doctor;
import packageObserver.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorControlador {

    private static NotificacionManager notificationManager;

    public static void setNotificationManager(NotificacionManager manager) {
        notificationManager = manager;
    }

    public static void addDoctor(String medicalID, String name, String specialty) {
        try (Connection conn = DatabaseConection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO doctors (medicalID, name, specialty) VALUES (?, ?, ?)")) {
            stmt.setString(1, medicalID);
            stmt.setString(2, name);
            stmt.setString(3, specialty);
            stmt.executeUpdate();

            notificationManager.notifyObservers("Nuevo médico registrado: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        try (Connection conn = DatabaseConection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM doctors")) {

            while (rs.next()) {
                doctors.add(new Doctor(
                        rs.getInt("id"),
                        rs.getString("medicalID"),
                        rs.getString("name"),
                        rs.getString("specialty")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    public static void updateDoctor(int id, String medicalID, String name, String specialty) {
        try (Connection conn = DatabaseConection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE doctors SET medicalID = ?, name = ?, specialty = ? WHERE id = ?")) {
            stmt.setString(1, medicalID);
            stmt.setString(2, name);
            stmt.setString(3, specialty);
            stmt.setInt(4, id);
            stmt.executeUpdate();

            notificationManager.notifyObservers("Un médico fue editado: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDoctor(int id) {
        try (Connection conn = DatabaseConection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM doctors WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();

            notificationManager.notifyObservers("Un médico fue eliminado: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}