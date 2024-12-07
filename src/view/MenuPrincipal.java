package view;

import controller.CitasControlador;
import controller.DoctorControlador;
import controller.PacienteControlador;
import model.DatabaseConection;
import packageObserver.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MenuPrincipal extends JFrame implements Observer {
    private NotificacionManager notificationManager;
    private JTextArea notificationArea;

    public MenuPrincipal() {
        notificationManager = new NotificacionManager();
        notificationManager.addObserver(this);

        CitasControlador.setNotificationManager(notificationManager);
        PacienteControlador.setNotificationManager(notificationManager);
        DoctorControlador.setNotificationManager(notificationManager);

        setTitle("Gestor de Citas Médicas");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        // Cargar la imagen
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon("src/image.jpg");
            Image img = logoIcon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            e.printStackTrace();
        }

        logoLabel.setHorizontalAlignment(JLabel.CENTER); // Centrar la imagen

        JButton patientButton = new JButton("Gestión de Pacientes");
        JButton doctorButton = new JButton("Gestión de Doctores"); // Botón nuevo
        JButton appointmentButton = new JButton("Gestión de Citas");
        JButton notificationButton = new JButton("Ver Notificaciones");
        JButton exitButton = new JButton("Salir");

        patientButton.addActionListener((ActionEvent e) -> {
            new PacientesForm().setVisible(true);
        });

        doctorButton.addActionListener((ActionEvent e) -> {
            new DoctoresForm().setVisible(true); //Doctores
        });

        appointmentButton.addActionListener((ActionEvent e) -> {
            new CitasForm().setVisible(true);
        });

        notificationButton.addActionListener((ActionEvent e) -> showNotifications());

        exitButton.addActionListener((ActionEvent e) -> System.exit(0));

        panel.add(logoLabel); // Añadir el logo al panel
        panel.add(patientButton);
        panel.add(doctorButton); // Añadir el nuevo botón
        panel.add(appointmentButton);
        panel.add(notificationButton);
        panel.add(exitButton);

        add(panel);

        notificationArea = new JTextArea();
        notificationArea.setEditable(false);
    }

    public void showNotifications() {
        JFrame notificationFrame = new JFrame("Notificaciones");
        notificationFrame.setSize(400, 300);
        notificationFrame.add(new JScrollPane(notificationArea));
        notificationFrame.setLocationRelativeTo(this);
        notificationFrame.setVisible(true);
    }

    @Override
    public void update(String message) {
        notificationArea.append(message + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseConection.initializeDatabase(); // Inicializa la base de datos
            new MenuPrincipal().setVisible(true);
        });
    }
}
