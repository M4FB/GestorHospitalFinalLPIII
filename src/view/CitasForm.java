package view;

import controller.CitasControlador;
import controller.PacienteControlador;
import model.Cita;
import model.Paciente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;


public class CitasForm extends JFrame {
    private JComboBox<String> patientComboBox;
    private JComboBox<String> specialtyComboBox;
    private JTextField dateField;
    private JTable appointmentTable;

    public CitasForm() {
        setTitle("Gestión de Citas");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        // Formulario para registrar citas
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Registrar Nueva Cita"));

        patientComboBox = new JComboBox<>();
        loadPatients();

        specialtyComboBox = new JComboBox<>(new String[]{"Cardiología", "Dermatología", "Pediatría", "Neurología"});

        dateField = new JTextField();

        JButton addButton = new JButton("Registrar Cita");
        addButton.addActionListener(this::addAppointment);

        formPanel.add(new JLabel("Paciente:"));
        formPanel.add(patientComboBox);
        formPanel.add(new JLabel("Especialidad:"));
        formPanel.add(specialtyComboBox);
        formPanel.add(new JLabel("Fecha (YYYY-MM-DD):"));
        formPanel.add(dateField);
        formPanel.add(new JLabel());
        formPanel.add(addButton);

        panel.add(formPanel, BorderLayout.NORTH);

        // Tabla de citas
        appointmentTable = new JTable();
        loadAppointments();

        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        //Botones para editar y eliminar
        JPanel buttonPanel = new JPanel();
        JButton editButton = new JButton("Editar Cita");
        JButton deleteButton = new JButton("Eliminar Cita");

        editButton.addActionListener(this::editAppointment);
        deleteButton.addActionListener(this::deleteAppointment);

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadPatients() {
        List<Paciente> patients = PacienteControlador.getAllPatients();
        for (Paciente patient : patients) {
            patientComboBox.addItem(patient.getId() + " - " + patient.getName());
        }
    }

    private void loadAppointments() {
        List<Cita> citas = CitasControlador.getAllAppointments();
        String[] columns = {"ID", "Paciente", "Fecha", "Especialidad"};
        String[][] data = new String[citas.size()][4];

        for (int i = 0; i < citas.size(); i++) {
            Cita cita = citas.get(i);
            data[i][0] = String.valueOf(cita.getId());
            data[i][1] = CitasControlador.getPatientNameById(cita.getPatientId());
            data[i][2] = cita.getDate();
            data[i][3] = cita.getSpecialty();
        }

        appointmentTable.setModel(new javax.swing.table.DefaultTableModel(data, columns));
    }

    private void addAppointment(ActionEvent e) {
        String patientInfo = (String) patientComboBox.getSelectedItem();
        if (patientInfo == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un paciente.");
            return;
        }
        int patientId = Integer.parseInt(patientInfo.split(" - ")[0]);
        String date = dateField.getText();
        String specialty = (String) specialtyComboBox.getSelectedItem();

        CitasControlador.addAppointment(new Cita(0, patientId, date, specialty));
        JOptionPane.showMessageDialog(this, "Cita registrada exitosamente.");
        loadAppointments();
    }

    private void editAppointment(ActionEvent e) {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una cita.");
            return;
        }

        int appointmentId = Integer.parseInt((String) appointmentTable.getValueAt(selectedRow, 0));
        String currentDate = (String) appointmentTable.getValueAt(selectedRow, 2);
        String currentSpecialty = (String) appointmentTable.getValueAt(selectedRow, 3);

        String newDate = JOptionPane.showInputDialog(this, "Ingrese la nueva fecha (YYYY-MM-DD):", currentDate);
        String newSpecialty = (String) JOptionPane.showInputDialog(this, "Seleccione la nueva especialidad:", "Especialidad", JOptionPane.QUESTION_MESSAGE, null, new String[]{"Cardiología", "Dermatología", "Pediatría", "Neurología"}, currentSpecialty);

        if (newDate == null || newSpecialty == null || newDate.isEmpty() || newSpecialty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Operación cancelada o campos vacíos.");
            return;
        }

        try {
            Cita updatedCita = new Cita(appointmentId, Integer.parseInt(((String) patientComboBox.getSelectedItem()).split(" - ")[0]), newDate, newSpecialty);
            CitasControlador.updateAppointment(updatedCita);
            JOptionPane.showMessageDialog(this, "Cita actualizada exitosamente.");
            loadAppointments();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar la cita.");
        }
    }

    private void deleteAppointment(ActionEvent e) {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una cita.");
            return;
        }

        int appointmentId = Integer.parseInt((String) appointmentTable.getValueAt(selectedRow, 0));
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar esta cita?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            CitasControlador.deleteAppointment(appointmentId);
            JOptionPane.showMessageDialog(this, "Cita eliminada exitosamente.");
            loadAppointments();
        }
    }

}