package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.Semaphore;

public class FilosofosOrientalesGUI extends JFrame {

    private PanelFilosofos panel;
    private JTextField inputFilosofos;
    private JButton btnIniciar;
    private Filosofos[] filosofos;
    private Palillos[] palillos;
    private Semaphore semaforo;



    public FilosofosOrientalesGUI() {
        setTitle("Problema de los Filósofos Orientales ");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de control arriba
        JPanel panelControl = new JPanel();
        panelControl.add(new JLabel("Número de Filósofos (5 - 15):"));
        inputFilosofos = new JTextField("5", 5);
        btnIniciar = new JButton("Iniciar");
        panelControl.add(inputFilosofos);
        panelControl.add(btnIniciar);
        add(panelControl, BorderLayout.NORTH);


        // Panel inicial (por defecto con 5 filósofos)
        iniciarSimulacion(5);

        // Acción del botón
        btnIniciar.addActionListener((ActionEvent e) -> {
            try {
                int n = Integer.parseInt(inputFilosofos.getText().trim());
                if (n < 5 || n > 15) {
                    JOptionPane.showMessageDialog(this, "El número debe ser entre 5 y 15");
                    return;
                }
                iniciarSimulacion(n);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Introduce un número válido");
            }
        });

        setVisible(true);

    }


    private void iniciarSimulacion(int n) {
        if (panel != null) remove(panel);

        semaforo = new Semaphore(n);
        palillos = new Palillos[n + 1];
        for (int i = 1; i <= n; i++) {
            palillos[i] = new Palillos(i);
        }

        filosofos = new Filosofos[n + 1];
        panel = new PanelFilosofos(filosofos, palillos, n);
        add(panel, BorderLayout.CENTER);

        for (int i = 1; i <= n; i++) {
            Palillos izq = palillos[i];
            Palillos der = (i == n) ? palillos[1] : palillos[i + 1];
            filosofos[i] = new Filosofos(i, semaforo, izq, der, panel);
        }

        revalidate();
        repaint();

        // Iniciar hilos
        SwingUtilities.invokeLater(() -> {
            for (int i = 1; i <= n; i++) {
                filosofos[i].start();
            }
        });
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(FilosofosOrientalesGUI::new);
    }

}


