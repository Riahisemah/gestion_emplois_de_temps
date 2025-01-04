package gestion_des_emplois.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import gestion_des_emplois.SeanceDAO;
import gestion_des_emplois.models.Seance;

public class RequetesInterface {

    private JFrame frame;
    private JComboBox<String> classComboBox, classSearchComboBox;
    private JTable table;
    private JTextField idField;
    private JTextField txtContact;  // Text field for the subject ("Matière")
    private DefaultTableModel tableModel;
    private SeanceDAO seanceDAO = new SeanceDAO();

    public RequetesInterface() {
        // Initialize the main frame
        frame = new JFrame("Timetable Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(255, 192, 192)); // Light pink background

        JLabel titreLabel = new JLabel("Les seances de cours dans la semaine d'une matière dans une classe");
        titreLabel.setBounds(20, 20, 400, 10);
        frame.add(titreLabel);

        // Class ComboBox and Label
        JLabel classLabel = new JLabel("Classe");
        classLabel.setBounds(20, 40, 60, 25);
        frame.add(classLabel);

        classComboBox = new JComboBox<>(new String[]{"6eme", "5eme", "4eme", "3eme","2eme","1ere"});
        classComboBox.setBounds(20, 80, 100, 25);
        frame.add(classComboBox);

        // Subject JTextField for the "Matière"
        JLabel subjectLabel = new JLabel("Matière");
        subjectLabel.setBounds(150, 40, 60, 25);
        frame.add(subjectLabel);

        txtContact = new JTextField();
        txtContact.setBounds(150, 80, 100, 25);
        frame.add(txtContact);

        // Search Button
        JButton searchButton = new JButton("CHERCHER");
        searchButton.setBounds(300, 80, 100, 25);
        frame.add(searchButton);

        // Table for displaying results
        String[] columnNames = {"ID", "Classe", "Matière", "Jour", "Heure", "Nom enseignant", "Contact enseignant"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBounds(20, 120, 750, 150);
        frame.add(tableScrollPane);

        // ID Field and Delete Button
        JLabel idLabel = new JLabel("ID");
        idLabel.setBounds(20, 300, 20, 25);
        frame.add(idLabel);

        idField = new JTextField();
        idField.setBounds(50, 300, 100, 25);
        frame.add(idField);

        JButton deleteButton = new JButton("SUPPRIMER");
        deleteButton.setBounds(160, 300, 120, 25);
        frame.add(deleteButton);

        JLabel titreLabel1 = new JLabel("Emploi de temps de la semaine par classe");
        titreLabel1.setBounds(20, 330, 400, 20);
        frame.add(titreLabel1);

        // Class ComboBox for weekly timetable search
        JLabel classSearchLabel = new JLabel("Classe");
        classSearchLabel.setBounds(20, 360, 60, 25);
        frame.add(classSearchLabel);

        classSearchComboBox = new JComboBox<>(new String[]{"6eme", "5eme", "4eme", "3eme","2eme","1ere"});
        classSearchComboBox.setBounds(100, 360, 100, 25);
        frame.add(classSearchComboBox);

        // Weekly search button
        JButton weeklySearchButton = new JButton("CHERCHER");
        weeklySearchButton.setBounds(200, 360, 100, 25);
        frame.add(weeklySearchButton);

        // Action Listeners for buttons
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchTimetable();  // Call searchTimetable with text from txtContact
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRecord();
            }
        });

        weeklySearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	searchByClasse();
            }
        });
    }

    // Method to search the timetable based on the selected class and subject
    private void searchTimetable() {
        String selectedClass = (String) classComboBox.getSelectedItem();
        String selectedSubject = txtContact.getText();  // Get the "Matière" from the JTextField
        
        // Clear previous data before displaying new data
        tableModel.setRowCount(0);

        try {
            // Fetch data from the DAO
            if (!selectedSubject.isEmpty() && !selectedClass.isEmpty()) {
                List<Seance> seances = seanceDAO.getSessionsByClassAndSubject(selectedClass, selectedSubject);
                if(!seances.isEmpty()) {
                // Add the results to the table
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
                txtContact.setText("");
                classComboBox.setSelectedIndex(0);
                }
             else {
                JOptionPane.showMessageDialog(frame, "Aucune séance trouvée ", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
            } else {
                JOptionPane.showMessageDialog(frame, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error fetching data from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

// Method to delete a session by its ID
private void deleteRecord() {
    String idToDelete = idField.getText();  // Get the ID from the input field

    // Check if the ID field is empty
    if (idToDelete.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Veuillez entrer un ID pour supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Use the DAO to delete the session from the database
    try {
        boolean isDeleted = seanceDAO.deleteSeanceById(idToDelete);
        System.out.println(isDeleted);
        if (isDeleted) {
            // If deletion was successful, remove the row from the table as well
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(idToDelete)) {
                    tableModel.removeRow(i);  // Remove the row from the JTable

                    break;
                }
            }
            
            JOptionPane.showMessageDialog(frame, "Séance supprimée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            idField.setText("");
            tableModel.setRowCount(0);

        } else {
            JOptionPane.showMessageDialog(frame, "Aucune séance trouvée avec cet ID.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(frame, "Erreur lors de la suppression de la séance.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}


private void searchByClasse() {
    String selectedClass = (String) classSearchComboBox.getSelectedItem();
    
    // Clear previous data before displaying new data
    tableModel.setRowCount(0);

    try {
        // Fetch data from the DAO
        if ( !selectedClass.isEmpty()) {
            List<Seance> seances = seanceDAO.searchByClass(selectedClass);
            if (!seances.isEmpty()) {
            // Add the results to the table
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
            
            classSearchComboBox.setSelectedIndex(0);

            }
            		else {
            JOptionPane.showMessageDialog(frame, "Aucune séance trouvée", "Erreur", JOptionPane.WARNING_MESSAGE);
        }}else {
            JOptionPane.showMessageDialog(frame, "Veuillez remplir  le champs.", "Erreur", JOptionPane.WARNING_MESSAGE);
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(frame, "Error fetching data from the database.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}



    // Method to return the JPanel for embedding the interface into another JFrame
    public JPanel getRequetesPanel() {
        return (JPanel) frame.getContentPane();
    }

    public static void main(String[] args) {
        // Display the Requetes Interface
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RequetesInterface();
            }
        });
    }
}
