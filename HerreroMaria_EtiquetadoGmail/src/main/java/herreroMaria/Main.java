package herreroMaria;

import herreroMaria.vista.EtiquetadoView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EtiquetadoView gui = new EtiquetadoView();
            gui.setVisible(true);
        });
    }
}