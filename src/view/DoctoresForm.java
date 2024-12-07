package view;

import controller.DoctorControlador;
import model.Doctor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class DoctoresForm extends JFrame {
    private JTextField medicalIDField;
    private JTextField nameField;
    private JTextField specialtyField;
    private JTable doctorTable;

    public DoctoresForm() {
        setTitle("Gestión de Doctores");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Formulario para agregar doctores
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Doctor"));

        medicalIDField = new JTextField();
        nameField = new JTextField();
        specialtyField = new JTextField();

        JButton addButton = new JButton("Registrar Doctor");
        addButton.addActionListener(this::addDoctor);

        formPanel.add(new JLabel("ID Médico:"));
        formPanel.add(medicalIDField);
        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Especialidad:"));
        formPanel.add(specialtyField);
        formPanel.add(new JLabel());
        formPanel.add(addButton);

        panel.add(formPanel, BorderLayout.NORTH);

        // Tabla de doctores
        doctorTable = new JTable();
        loadDoctors();

        JScrollPane scrollPane = new JScrollPane(doctorTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botones para editar y eliminar doctores
        JPanel buttonPanel = new JPanel();
        JButton editButton = new JButton("Editar Doctor");
        JButton deleteButton = new JButton("Eliminar Doctor");

        editButton.addActionListener(this::editDoctor);
        deleteButton.addActionListener(this::deleteDoctor);

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadDoctors() {
        List<Doctor> doctors = DoctorControlador.getAllDoctors();
        String[] columns = {"ID", "ID Médico", "Nombre", "Especialidad"};
        String[][] data = new String[doctors.size()][4];

        for (int i = 0; i < doctors.size(); i++) {
            Doctor doctor = doctors.get(i);
            data[i][0] = String.valueOf(doctor.getId());
            data[i][1] = doctor.getMedicalID();
            data[i][2] = doctor.getName();
            data[i][3] = doctor.getSpecialty();
        }

        doctorTable.setModel(new javax.swing.table.DefaultTableModel(data, columns));
    }

    private void addDoctor(ActionEvent e) {
        String medicalID = medicalIDField.getText();
        String name = nameField.getText();
        String specialty = specialtyField.getText();

        if (medicalID.isEmpty() || name.isEmpty() || specialty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return;
        }

        DoctorControlador.addDoctor(medicalID, name, specialty);
        JOptionPane.showMessageDialog(this, "Doctor registrado exitosamente.");
        loadDoctors();
    }

    private void editDoctor(ActionEvent e) {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un doctor.");
            return;
        }

        int doctorId = Integer.parseInt((String) doctorTable.getValueAt(selectedRow, 0));
        String currentMedicalID = (String) doctorTable.getValueAt(selectedRow, 1);
        String currentName = (String) doctorTable.getValueAt(selectedRow, 2);
        String currentSpecialty = (String) doctorTable.getValueAt(selectedRow, 3);

        String newMedicalID = JOptionPane.showInputDialog(this, "Ingrese el nuevo ID Médico:", currentMedicalID);
        String newName = JOptionPane.showInputDialog(this, "Ingrese el nuevo nombre:", currentName);
        String newSpecialty = JOptionPane.showInputDialog(this, "Ingrese la nueva especialidad:", currentSpecialty);

        if (newMedicalID == null || newName == null || newSpecialty == null || newMedicalID.isEmpty() || newName.isEmpty() || newSpecialty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Operación cancelada o campos vacíos.");
            return;
        }
        DoctorControlador.updateDoctor(doctorId, newMedicalID, newName, newSpecialty);
        JOptionPane.showMessageDialog(this, "Doctor actualizado exitosamente.");
        loadDoctors();
    }

    private void deleteDoctor(ActionEvent e) {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un doctor.");
            return;
        }

        int doctorId = Integer.parseInt((String) doctorTable.getValueAt(selectedRow, 0));
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este doctor?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            DoctorControlador.deleteDoctor(doctorId);
            JOptionPane.showMessageDialog(this, "Doctor eliminado exitosamente.");
            loadDoctors();
        }
    }
}