package gestion_des_emplois.gui;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import gestion_des_emplois.SeanceDAO;
import gestion_des_emplois.models.Seance;

/**
 * This class provides a GUI interface for querying and managing course sessions (séances de cours).
 * It allows users to search for sessions by class and subject, delete sessions by ID, and view weekly timetables by class.
 */
public class RequetesInterface {

    private JDialog dialog; // Use JDialog instead of JFrame
    private JComboBox<String> classComboBox, classSearchComboBox; // Combo boxes for class selection
    private JTable table; // Table to display session data
    private JTextField idField; // Text field for entering session ID
    private JTextField txtContact; // Text field for entering the subject ("Matière")
    private DefaultTableModel tableModel; // Table model for managing table data
    private SeanceDAO seanceDAO = new SeanceDAO(); // DAO for session operations

    /**
     * Constructor to initialize the interface and its components.
     *
     * @param parent The parent JFrame to which this dialog belongs.
     */
    public RequetesInterface(JFrame parent) {
        // Initialize the dialog
        dialog = new JDialog(parent, "Requetes Interface", true); // true makes it modal
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(800, 600);
        dialog.setLayout(new GridBagLayout());
        dialog.getContentPane().setBackground(new Color(240, 240, 240)); // Light gray background

        // Create a title label for the first section
        JLabel titreLabel = new JLabel("Les séances de cours dans la semaine d'une matière dans une classe");
        titreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titreLabel.setForeground(new Color(50, 50, 50));
        addComponent(dialog, titreLabel, 0, 0, 2, 1);

        // Class ComboBox and Label
        JLabel classLabel = new JLabel("Classe");
        addComponent(dialog, classLabel, 0, 1, 1, 1);

        classComboBox = new JComboBox<>(new String[]{"6eme", "5eme", "4eme", "3eme", "2eme", "1ere"});
        addComponent(dialog, classComboBox, 0, 2, 1, 1);

        // Subject JTextField for the "Matière"
        JLabel subjectLabel = new JLabel("Matière");
        addComponent(dialog, subjectLabel, 1, 1, 1, 1);

        txtContact = new JTextField(15);
        addComponent(dialog, txtContact, 1, 2, 1, 1);

        // Search Button
        JButton searchButton = createButton("CHERCHER");
        addComponent(dialog, searchButton, 2, 2, 1, 1);

        // Table for displaying results
        String[] columnNames = {"ID", "Classe", "Matière", "Jour", "Heure", "Nom enseignant", "Contact enseignant"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        // Set a preferred size for the JScrollPane to limit the height of the table
        tableScrollPane.setPreferredSize(new Dimension(750, 200)); // Width: 750, Height: 200
        addComponent(dialog, tableScrollPane, 0, 3, 3, 1); // Adjusted gridheight to 1

        // ID Field and Delete Button
        JLabel idLabel = new JLabel("ID");
        addComponent(dialog, idLabel, 0, 7, 1, 1);

        idField = new JTextField(10);
        addComponent(dialog, idField, 1, 7, 1, 1);

        JButton deleteButton = createButton("SUPPRIMER");
        addComponent(dialog, deleteButton, 2, 7, 1, 1);

        // Title label for the second section
        JLabel titreLabel1 = new JLabel("Emploi de temps de la semaine par classe");
        titreLabel1.setFont(new Font("Arial", Font.BOLD, 16));
        titreLabel1.setForeground(new Color(50, 50, 50));
        addComponent(dialog, titreLabel1, 0, 8, 2, 1);

        // Class ComboBox for weekly timetable search
        JLabel classSearchLabel = new JLabel("Classe");
        addComponent(dialog, classSearchLabel, 0, 9, 1, 1);

        classSearchComboBox = new JComboBox<>(new String[]{"6eme", "5eme", "4eme", "3eme", "2eme", "1ere"});
        addComponent(dialog, classSearchComboBox, 0, 10, 1, 1);

        // Weekly search button
        JButton weeklySearchButton = createButton("CHERCHER");
        addComponent(dialog, weeklySearchButton, 1, 10, 1, 1);

        // Action Listeners for buttons
        searchButton.addActionListener(e -> searchTimetable());
        deleteButton.addActionListener(e -> deleteRecord());
        weeklySearchButton.addActionListener(e -> searchByClasse());

        // Center the dialog relative to the parent window
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true); // Make the dialog visible
    }

    /**
     * Helper method to add components to the container using GridBagConstraints.
     *
     * @param container  The container to which the component is added.
     * @param component  The component to add.
     * @param gridx      The column position.
     * @param gridy      The row position.
     * @param gridwidth  The number of columns the component spans.
     * @param gridheight The number of rows the component spans.
     */
    private void addComponent(Container container, Component component, int gridx, int gridy, int gridwidth, int gridheight) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        container.add(component, gbc);
    }

    /**
     * Helper method to create a styled button.
     *
     * @param text The text to display on the button.
     * @return The created button.
     */
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(100, 150, 255)); // Blue background
        button.setForeground(Color.WHITE); // White text
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(80, 130, 235)); // Darker blue on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 150, 255)); // Restore original color
            }
        });
        return button;
    }

    /**
     * Searches for sessions by class and subject and updates the table with the results.
     */
    private void searchTimetable() {
        String selectedClass = (String) classComboBox.getSelectedItem();
        String selectedSubject = txtContact.getText();
        tableModel.setRowCount(0); // Clear the table

        try {
            if (!selectedSubject.isEmpty() && !selectedClass.isEmpty()) {
                List<Seance> seances = seanceDAO.getSessionsByClassAndSubject(selectedClass, selectedSubject);
                if (!seances.isEmpty()) {
                    for (Seance seance : seances) {
                        tableModel.addRow(new Object[]{
                            seance.getId(),
                            seance.getClasse(),
                            seance.getMatiere(),
                            seance.getJour(),
                            seance.getHeure(),
                            seance.getEnseignant1().getNom(),
                            seance.getEnseignant1().getContact()
                        });
                    }
                    txtContact.setText(""); // Clear the subject field
                    classComboBox.setSelectedIndex(0); // Reset the class combo box
                } else {
                    JOptionPane.showMessageDialog(dialog, "Aucune séance trouvée", "Erreur", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(dialog, "Erreur lors de la récupération des données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes a session record by ID.
     */
    private void deleteRecord() {
        String idToDelete = idField.getText();

        if (idToDelete.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Veuillez entrer un ID pour supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean isDeleted = seanceDAO.deleteSeanceById(idToDelete);
            if (isDeleted) {
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (tableModel.getValueAt(i, 0).equals(idToDelete)) {
                        tableModel.removeRow(i);
                        break;
                    }
                }
                JOptionPane.showMessageDialog(dialog, "Séance supprimée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                idField.setText(""); // Clear the ID field
            } else {
                JOptionPane.showMessageDialog(dialog, "Aucune séance trouvée avec cet ID.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(dialog, "Erreur lors de la suppression de la séance.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Searches for sessions by class and updates the table with the results.
     */
    private void searchByClasse() {
        String selectedClass = (String) classSearchComboBox.getSelectedItem();
        tableModel.setRowCount(0); // Clear the table

        try {
            if (!selectedClass.isEmpty()) {
                List<Seance> seances = seanceDAO.searchByClass(selectedClass);
                if (!seances.isEmpty()) {
                    for (Seance seance : seances) {
                        tableModel.addRow(new Object[]{
                            seance.getId(),
                            seance.getClasse(),
                            seance.getMatiere(),
                            seance.getJour(),
                            seance.getHeure(),
                            seance.getEnseignant1().getNom(),
                            seance.getEnseignant1().getContact()
                        });
                    }
                    classSearchComboBox.setSelectedIndex(0); // Reset the class combo box
                } else {
                    JOptionPane.showMessageDialog(dialog, "Aucune séance trouvée", "Erreur", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Veuillez remplir le champ.", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(dialog, "Erreur lors de la récupération des données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Main method to launch the interface.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame parentFrame = new JFrame("Parent Frame");
            parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            parentFrame.setSize(700, 500);
            parentFrame.setVisible(true);
            new RequetesInterface(parentFrame);
        });
    }
}