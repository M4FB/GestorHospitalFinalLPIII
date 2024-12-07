package model;

public class Doctor {
    private int id;
    private String medicalID;
    private String name;
    private String specialty;    // Especialidad del médico

    public Doctor(int id, String medicalID, String name, String specialty) {
        this.id = id;
        this.medicalID = medicalID;
        this.name = name;
        this.specialty = specialty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedicalID() {
        return medicalID;
    }

    public void setMedicalID(String medicalID) {
        this.medicalID = medicalID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}