package herreroMaria.vista;

import herreroMaria.servicio.GestorEtiquetasService;
import herreroMaria.util.ConfigManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class EtiquetadoView extends JFrame {

    public EtiquetadoView() {
        setTitle("Gestor de etiquetas para Gmail");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelSuperior.setBackground(new Color(230, 240, 255));

        JLabel titulo = new JLabel("Gestor de etiquetas para Gmail");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel autora = new JLabel("María Herrero");
        autora.setFont(new Font("Arial", Font.PLAIN, 12));
        autora.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelSuperior.add(titulo);
        panelSuperior.add(Box.createRigidArea(new Dimension(0, 5)));
        panelSuperior.add(autora);

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new GridLayout(5, 1, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCentral.setBackground(new Color(245, 245, 245));

        JLabel etiquetaDone = crearEtiquetaConColor("Número de correos para 'Done': 3", new Color(144, 238, 144));
        JLabel etiquetaToBeDone = crearEtiquetaConColor("Número de correos para 'To Be Done': 3", new Color(255, 228, 181));
        JLabel etiquetaWorkInProgress = crearEtiquetaConColor("Número de correos para 'Work in Progress': 1", new Color(255, 255, 153));

        panelCentral.add(etiquetaDone);
        panelCentral.add(etiquetaToBeDone);
        panelCentral.add(etiquetaWorkInProgress);

        JButton botonEtiquetar = crearBotonEstilizado();

        botonEtiquetar.addActionListener(_ -> {
            try {
                String host = ConfigManager.getProperty("imap.host");
                String user = ConfigManager.getProperty("imap.user");
                String password = ConfigManager.getProperty("imap.password");

                System.out.println("Iniciando proceso de etiquetado...");

                GestorEtiquetasService servicio = new GestorEtiquetasService(host, user, password);
                servicio.etiquetarCorreos();
                servicio.cerrar();

                JOptionPane.showMessageDialog(EtiquetadoView.this, "Correos etiquetados correctamente.");
                System.out.println("Proceso finalizado correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(EtiquetadoView.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(new Color(245, 245, 245));
        panelBoton.add(botonEtiquetar);

        panelCentral.add(panelBoton);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
    }

    private JLabel crearEtiquetaConColor(String texto, Color fondo) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setOpaque(true);
        etiqueta.setBackground(fondo);
        etiqueta.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return etiqueta;
    }

    private JButton crearBotonEstilizado() {
        JButton boton = new JButton("Clasificar correos") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 20, 20);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 20, 20);

                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                Rectangle2D r = fm.getStringBounds(this.getText(), g2);
                int x = (this.getWidth() - (int) r.getWidth()) / 2;
                int y = (this.getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
                g2.drawString(this.getText(), x, y);

                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
            }
        };

        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setPreferredSize(new Dimension(250, 40));
        boton.setBackground(new Color(100, 149, 237));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(70, 130, 180));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(100, 149, 237));
            }
        });

        return boton;
    }
}