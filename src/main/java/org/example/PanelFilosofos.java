package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.Image;
import java.util.Objects;
import javax.swing.ImageIcon;

public class PanelFilosofos extends JPanel {
    private final Filosofos[] filosofos;
    private final Palillos[] palillos;
    private final int RADIO_FILOSOFO = 30;
    private final int numFilosofos;
    private final Point[] posiciones;
    private Image fondo;


    public PanelFilosofos(Filosofos[] filosofos, Palillos[] palillos, int numFilosofos) {
        this.filosofos = filosofos;
        this.palillos = palillos;
        this.numFilosofos = numFilosofos;
        this.posiciones = new Point[numFilosofos + 1];
        setBackground(Color.gray);
        this.fondo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/imagen_oriental.jpg"))).getImage();
        calcularPosiciones();
    }

    private void calcularPosiciones() {
        int centroX = 780;
        int centroY = 430;

        // Radio dinámico según cantidad
        int RADIO = switch (numFilosofos) {
            case 5, 6 -> 260;
            case 7, 8 -> 280;
            case 9, 10 -> 300;
            case 11, 12 -> 330;
            case 13, 14 -> 360;
            case 15 -> 380;
            default -> 260;
        };

        double paso = 360.0 / numFilosofos;
        for (int i = 1; i <= numFilosofos; i++) {
            double angulo = Math.toRadians(90 + (i - 1) * paso);
            int x = (int) (centroX + RADIO * Math.cos(angulo));
            int y = (int) (centroY - RADIO * Math.sin(angulo));
            posiciones[i] = new Point(x, y);
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (fondo != null) {
            g2.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }


        // ===== Dibujar palillos entre filósofos =====
        for (int i = 1; i <= numFilosofos; i++) {
            Point p1 = posiciones[i];
            Point p2 = posiciones[(i == numFilosofos) ? 1 : i + 1];
            Palillos palillo = palillos[i];

            // Calcular punto medio entre filósofos
            int midX = (p1.x + p2.x) / 2;
            int midY = (p1.y + p2.y) / 2;

            // Ángulo del palillo (entre los dos filósofos)
            double angulo = Math.atan2(p2.y - p1.y, p2.x - p1.x);

            // Dibujar palillo como rectángulo inclinado
            dibujarPalillo(g2, midX, midY, angulo, palillo.enUso);
        }

        //Dibujar Filosofos
        for (int i = 1; i <= numFilosofos; i++) {
            Point p = posiciones[i];
            Filosofos f = filosofos[i];
            boolean comiendo = f.comiendo;


            // Cabeza del filósofo
            g2.setColor(comiendo ? new Color(255, 230, 180) : new Color(255, 200, 150));
            g2.fillOval(p.x - RADIO_FILOSOFO, p.y - RADIO_FILOSOFO, RADIO_FILOSOFO * 2, RADIO_FILOSOFO * 2);
            g2.setColor(Color.BLACK);
            g2.drawOval(p.x - RADIO_FILOSOFO, p.y - RADIO_FILOSOFO, RADIO_FILOSOFO * 2, RADIO_FILOSOFO * 2);

            // Sombrero (tipo chino)
            int[] xSombrero = {p.x - 35, p.x, p.x + 35};
            int[] ySombrero = {p.y - 25, p.y - 48, p.y - 25};
            g2.setColor(new Color(180, 140, 30));
            g2.fillPolygon(xSombrero, ySombrero, 3);
            g2.setColor(Color.BLACK);
            g2.drawPolygon(xSombrero, ySombrero, 3);

            // Ojos rasgados
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(p.x - 10, p.y - 5, p.x - 2, p.y - 5);
            g2.drawLine(p.x + 2, p.y - 5, p.x + 10, p.y - 5);


            // ===== BIGOTES COMO EN LA IMAGEN =====
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            // Bigote izquierdo
            Path2D bigoteIzq = new Path2D.Double();
            bigoteIzq.moveTo(p.x - 6, p.y + 4); // inicio más arriba (casi a la altura de los ojos)
            bigoteIzq.curveTo(
                    p.x - 18, p.y + 5,    // primer control (curva hacia afuera)
                    p.x - 1, p.y + 50,   // segundo control (curva hacia abajo)
                    p.x - 17, p.y + 35   // punto final (abajo y ligeramente afuera)
            );
            g2.draw(bigoteIzq);

            // Bigote derecho
            Path2D bigoteDer = new Path2D.Double();
            bigoteDer.moveTo(p.x + 6, p.y + 4);
            bigoteDer.curveTo(
                    p.x + 18, p.y + 5,
                    p.x + 1, p.y + 50,
                    p.x + 17, p.y + 35
            );
            g2.draw(bigoteDer);



            // ===== Boca más pequeña y más abajo =====
            g2.drawArc(p.x - 6, p.y + 8, 12, 6, 0, -180);



            // ===== TAZÓN ASIÁTICO COMO EN LA IMAGEN =====
            double anguloPlato = Math.toRadians(90 + (i - 1) * (360.0 / numFilosofos));
            int px = (int) (p.x - 55 * Math.cos(anguloPlato));
            int py = (int) (p.y + 90 * Math.sin(anguloPlato));


            // Color del tazón según estado
            Color colorTazon = comiendo ? Color.GREEN.darker() : Color.white;


            // Borde superior (boca del tazón)
            g2.setColor(Color.white);
            g2.fillOval(px - 18, py + 1, 36, 12);
            g2.setColor(colorTazon);
            g2.fillOval(px - 18, py - 8, 36, 12); // borde superior

            g2.setColor(Color.white);
            g2.drawOval(px - 18, py - 8, 36, 12); // contorno

            // Interior del tazón (óvalo más pequeño)
            if (f.comiendo) {
                g2.setColor(new Color(0, 150, 0)); // verde mientras come
            } else if (f.comidas > 0) {
                g2.setColor(Color.white); // negro cuando termina
            } else {
                g2.setColor(Color.black); // blanco antes de comer
            }
            g2.fillOval(px - 14, py - 4, 28, 8);
            g2.setColor(Color.black);
            g2.drawOval(px - 14, py - 4, 28, 8);


            // Vapor (si está comiendo)
            if (comiendo) {

                g2.setColor(Color.BLACK);
                for (int s = 0; s < 3; s++) {
                    int sx = px - 8 + s * 8;
                    g2.drawArc(sx, py - 22, 5, 15, 25, 165);
                }
            }

            // Nombre o etiqueta del filósofo
            // Etiqueta F#
            g2.setColor(Color.BLACK);
            g2.drawString("F" + i, p.x - 6, p.y + RADIO_FILOSOFO + 15);
        }

    }

    /** Dibuja un palillo entre filósofos */
    private void dibujarPalillo(Graphics2D g2, int x, int y, double angulo, boolean enUso) {
        int largo = 60;
        int ancho = 6;

        AffineTransform at = g2.getTransform();
        g2.translate(x, y);
        g2.rotate(angulo);

        g2.setColor(enUso ? Color.GREEN.darker() : new Color(139, 69, 19)); // verde si en uso, café si no
        g2.fillRect(-largo / 2, -ancho / 2, largo, ancho);
        g2.setColor(Color.BLACK);
        g2.drawRect(-largo / 2, -ancho / 2, largo, ancho);

        g2.setTransform(at);
    }
}
