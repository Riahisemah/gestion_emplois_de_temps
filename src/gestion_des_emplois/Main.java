package gestion_des_emplois;



import javax.swing.SwingUtilities;

import gestion_des_emplois.gui.GestionEmploiDuTemps;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GestionEmploiDuTemps().setVisible(true);
        });
    }
}