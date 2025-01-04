package gestion_des_emplois.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import gestion_des_emplois.EnseignantDAO;
import gestion_des_emplois.SeanceDAO;
import gestion_des_emplois.models.Enseignant;
import gestion_des_emplois.models.Seance;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestionEmploiDuTemps extends JFrame {
	 /**
	 * frame one
	 */
	private static final long serialVersionUID = 1L;
	private EnseignantDAO enseignantDAO = new EnseignantDAO();
     private SeanceDAO seanceDAO = new SeanceDAO();

	    private DefaultTableModel teacherTableModel;
	 // Ajouter au début de la classe
	    private DefaultTableModel courseTableModel;  // Make sure this is declared here

	    public GestionEmploiDuTemps() {
	        // Configuration de la fenêtre principale
	        setTitle("Gestion des Emplois du Temps");
	        setSize(1000, 600);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLayout(new BorderLayout());

	        // Couleur de fond
	        Color backgroundColor = new Color(255, 200, 200);

	        // Panel principal
	        JPanel mainPanel = new JPanel();
	        mainPanel.setLayout(new GridLayout(2, 1, 10, 10)); // GridLayout pour deux panels
	        mainPanel.setBackground(backgroundColor);

	        // ======== Formulaire d'enregistrement des enseignants ========
	        JPanel teacherPanel = new JPanel();
	        teacherPanel.setLayout(null);
	        teacherPanel.setPreferredSize(new Dimension(480, 270)); // Taille du panel des enseignants
	        teacherPanel.setBorder(BorderFactory.createTitledBorder(
	                BorderFactory.createLineBorder(Color.BLACK),
	                "Formulaire d'enregistrement des enseignants",
	                TitledBorder.DEFAULT_JUSTIFICATION,
	                TitledBorder.DEFAULT_POSITION,
	                new Font("Arial", Font.BOLD, 14)
	        ));
	        teacherPanel.setBackground(backgroundColor);

	        // Champs enseignants
	        JLabel lblMatricule = new JLabel("Matricule:");
	        lblMatricule.setBounds(20, 30, 100, 25);
	        teacherPanel.add(lblMatricule);

	        JTextField txtMatricule = new JTextField();
	        txtMatricule.setBounds(130, 30, 150, 25);
	        teacherPanel.add(txtMatricule);

	        JButton btnChercher = new JButton("CHERCHER");
	        btnChercher.setBounds(300, 30, 120, 25);
	        teacherPanel.add(btnChercher);

	        JLabel lblNom = new JLabel("Nom:");
	        lblNom.setBounds(20, 70, 100, 25);
	        teacherPanel.add(lblNom);

	        JTextField txtNom = new JTextField();
	        txtNom.setBounds(130, 70, 150, 25);
	        teacherPanel.add(txtNom);

	        JLabel lblContact = new JLabel("Contact:");
	        lblContact.setBounds(20, 110, 100, 25);
	        teacherPanel.add(lblContact);

	        JTextField txtContact = new JTextField();
	        txtContact.setBounds(130, 110, 150, 25);
	        teacherPanel.add(txtContact);

	        JButton btnEnregistrerEns = new JButton("ENREGISTRER");
	        btnEnregistrerEns.setBounds(20, 150, 130, 25);
	        teacherPanel.add(btnEnregistrerEns);

	        JButton btnModifierEns = new JButton("MODIFIER");
	        btnModifierEns.setBounds(160, 150, 130, 25);
	        teacherPanel.add(btnModifierEns);

	        JButton btnSupprimerEns = new JButton("SUPPRIMER");
	        btnSupprimerEns.setBounds(300, 150, 130, 25);
	        teacherPanel.add(btnSupprimerEns);

	        // Tableau des enseignants
	        String[] teacherColumns = {"Matricule", "Nom", "Contact"};
	        teacherTableModel = new DefaultTableModel(teacherColumns, 0);
	        JTable teacherTable = new JTable(teacherTableModel);
	        JScrollPane teacherScrollPane = new JScrollPane(teacherTable);
	        teacherScrollPane.setBounds(500, 20, 440, 150); // Adjust height to fit content
	        teacherPanel.add(teacherScrollPane);

	        // Dynamically update the teacher table
	        refreshTeacherTable();
	        btnChercher.addActionListener(e -> {
	            String matricule = txtMatricule.getText();
	            if (!matricule.isEmpty()) {
	                Enseignant enseignant = enseignantDAO.rechercherEnseignant(matricule);
	                if (enseignant != null) {
	                    // Nettoyer la table avant d'afficher les résultats
	                    teacherTableModel.setRowCount(0);

	                    // Ajouter l'enseignant trouvé dans le tableau
	                    teacherTableModel.addRow(new Object[]{enseignant.getNom(),enseignant.getMatricule() ,enseignant.getContact()});
	                } else {
	                    JOptionPane.showMessageDialog(this, "Aucun enseignant trouvé avec ce matricule.", "Erreur", JOptionPane.ERROR_MESSAGE);

	                    // Réinitialiser la table pour afficher tous les enseignants
	                    refreshTeacherTable();
	                }
	            } else {
	                JOptionPane.showMessageDialog(this, "Veuillez entrer un matricule.", "Erreur", JOptionPane.WARNING_MESSAGE);
	            }
	        });

	        btnEnregistrerEns.addActionListener(e -> {
	            String matricule = txtMatricule.getText();
	            String nom = txtNom.getText();
	            String contact = txtContact.getText();
	            if (!matricule.isEmpty() && !nom.isEmpty() && !contact.isEmpty()) {
	                enseignantDAO.enregistrerEnseignant(nom, matricule, contact);
	                JOptionPane.showMessageDialog(this, "Enseignant enregistré avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
	                refreshTeacherTable();
	            } else {
	                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.WARNING_MESSAGE);
	            }
	        });

	        btnModifierEns.addActionListener(e -> {
	            String matricule = txtMatricule.getText();
	            String nom = txtNom.getText();
	            String contact = txtContact.getText();
	            if (!matricule.isEmpty() && !nom.isEmpty() && !contact.isEmpty()) {
	                enseignantDAO.modifierEnseignant(matricule, nom, contact);
	                txtMatricule.setText("");
	                txtNom.setText("");
	                txtContact.setText("");
	                refreshTeacherTable();

	                JOptionPane.showMessageDialog(this, "Enseignant modifié avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);

	                
	                } else {
	                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.WARNING_MESSAGE);
	            }
	        });

	        btnSupprimerEns.addActionListener(e -> {
	            String matricule = txtMatricule.getText();
	            if (!matricule.isEmpty()) {
	                enseignantDAO.supprimerEnseignant(matricule);
	                JOptionPane.showMessageDialog(this, "Enseignant supprimé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
	                refreshTeacherTable();
	            } else {
	                JOptionPane.showMessageDialog(this, "Veuillez entrer un matricule.", "Erreur", JOptionPane.WARNING_MESSAGE);
	            }
	        });

	    


        
        
        
        
        
        // ======== Formulaire d'enregistrement des séances de cours ========
        JPanel coursePanel = new JPanel();
        coursePanel.setLayout(null);
        coursePanel.setPreferredSize(new Dimension(480, 270)); // Taille du panel des cours
        coursePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                "Formulaire d'enregistrement des séances de cours",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 14)
        ));
        coursePanel.setBackground(backgroundColor);
     // Champs cours
        JLabel lblClasse = new JLabel("Classe:");
        lblClasse.setBounds(20, 30, 100, 25);
        coursePanel.add(lblClasse);

        JComboBox<String> comboClasse = new JComboBox<>(new String[]{"6eme", "5eme", "4eme", "3eme","2eme","1ere"});
        comboClasse.setBounds(130, 30, 150, 25);
        coursePanel.add(comboClasse);

        JLabel lblMatiere = new JLabel("Matière:");
        lblMatiere.setBounds(20, 70, 100, 25);
        coursePanel.add(lblMatiere);

        JTextField txtMatiere = new JTextField();
        txtMatiere.setBounds(130, 70, 150, 25);
        coursePanel.add(txtMatiere);

        JLabel lblJour = new JLabel("Jour:");
        lblJour.setBounds(20, 110, 100, 25);
        coursePanel.add(lblJour);

        // Remplacer le champ texte par un JComboBox pour les jours
        JComboBox<String> comboJour = new JComboBox<>(new String[]{
            "LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI", "SAMEDI", "DIMANCHE"
        });
        comboJour.setBounds(130, 110, 150, 25);
        coursePanel.add(comboJour);

        JLabel lblHeure = new JLabel("Heure:");
        lblHeure.setBounds(20, 150, 100, 25);
        coursePanel.add(lblHeure);

        // Remplacer le champ texte par un JComboBox pour les heures
        JComboBox<String> comboHeure = new JComboBox<>(new String[]{
            "1ere H", "2eme H", "3eme H", "4eme H", "1ere et 2eme H", "3eme et 4eme H"
        });
        comboHeure.setBounds(130, 150, 150, 25);
        coursePanel.add(comboHeure);

     // Champ pour le matricule de l'enseignant
        JLabel lblMatriculeEns = new JLabel("Matricule enseignant:");
        lblMatriculeEns.setBounds(20, 190, 150, 25);
        coursePanel.add(lblMatriculeEns);

        // Ajouter un JComboBox pour les matricules des enseignants
        JComboBox<String> comboMatriculeEns = new JComboBox<>();
        comboMatriculeEns.setBounds(180, 190, 150, 25);
        coursePanel.add(comboMatriculeEns);

        // Fonction pour rafraîchir les données du JComboBox


        // Appeler la fonction pour peupler les données
        enseignantDAO.refreshEnseignantComboBox(comboMatriculeEns);

        JButton btnEnregistrerCours = new JButton("ENREGISTRER");
        btnEnregistrerCours.setBounds(20, 230, 130, 25);
        coursePanel.add(btnEnregistrerCours);

        JButton btnRequetes = new JButton("REQUETES");
        btnRequetes.setBounds(160, 230, 130, 25);
        coursePanel.add(btnRequetes);
        

        // Define table columns
        String[] courseColumns = {"Classe", "Matière", "Jour", "Heure", "Enseignant"};
        
        // Initialize courseTableModel before using it in any method
        courseTableModel = new DefaultTableModel(courseColumns, 0);

        // Create JTable for course data
        JTable courseTable = new JTable(courseTableModel);
        JScrollPane courseScrollPane = new JScrollPane(courseTable);
        courseScrollPane.setBounds(500, 20, 440, 150);
        coursePanel.add(courseScrollPane);

        // Populate JComboBox with teacher data
        enseignantDAO.refreshEnseignantComboBox(comboMatriculeEns);


        // Call the function to refresh course data
        refreshTableSeance();    


        // Ajouter les panels au panel principal
        mainPanel.add(teacherPanel);
        mainPanel.add(coursePanel);

        // Ajouter le panel principal à la fenêtre
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Rendre la fenêtre visible
        setVisible(true);
        
        
        
     // Within the GestionEmploiDuTemps class, modify the part where you handle the "REQUETES" button click

     // Action for the "REQUETES" button
        btnRequetes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new JFrame for the RequetesInterface
                JFrame requetesFrame = new JFrame("Requetes Interface");
                requetesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                requetesFrame.setSize(900, 450); // Same size as the panel in RequetesInterface
                requetesFrame.setLocationRelativeTo(null); // Center the window
                
                // Get the JPanel from the RequetesInterface
                RequetesInterface requetesInterface = new RequetesInterface();
                
                // Set the panel (you can also use requetesInterface.getRequetesPanel() here if needed)
                requetesFrame.add(requetesInterface.getRequetesPanel());
                
                // Show the new frame
                requetesFrame.setVisible(true);
            }
        });


        
        
        
        
        
        btnEnregistrerCours.addActionListener(e -> {
            // Get data from the form fields
            String classe = (String) comboClasse.getSelectedItem();
            String matiere = txtMatiere.getText();
            String jour = (String) comboJour.getSelectedItem();
            String heure = (String) comboHeure.getSelectedItem();
            String matriculeEns = (String) comboMatriculeEns.getSelectedItem();
            
            // Validate the data
            if (classe != null && !matiere.isEmpty() && jour != null && heure != null && matriculeEns != null) {
                // Call the DAO to save the session
              seanceDAO.enregistrerSeance(classe, matiere, jour, heure, matriculeEns);

                // Clear the fields after saving
                comboClasse.setSelectedIndex(0);
                txtMatiere.setText("");
                comboJour.setSelectedIndex(0);
                comboHeure.setSelectedIndex(0);
                comboMatriculeEns.setSelectedIndex(0);

                // Refresh the table to show the new data
                refreshTableSeance();

                // Show success message
                JOptionPane.showMessageDialog(this, "Séance enregistrée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Show error message if data is invalid
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        

    }
	    
 private void refreshTableSeance() {
	 courseTableModel.setRowCount(0);

     // Create an instance of SeanceDAO
     java.util.List<Seance> seances = seanceDAO.getAllSeances();
        // Ensure courseTableModel is initialized here


            // Add data to the table
            for (Seance seance : seances) {
             	System.out.println("seance");

             	System.out.println(seance);

                courseTableModel.addRow(new Object[]{
                    seance.getClasse(),
                    seance.getMatiere(),
                    seance.getJour(),
                    seance.getHeure(),
                    seance.getEnseignant()
                });
            }

    }
	    // Method to refresh the teacher table
	    private void refreshTeacherTable() {
	        teacherTableModel.setRowCount(0);
	        java.util.List<Enseignant> enseignants = enseignantDAO.getAllEnseignants();
	        for (Enseignant enseignant : enseignants) {
	            teacherTableModel.addRow(new Object[]{
	            		enseignant.getNom(),
	            		enseignant.getMatricule() ,
	            		enseignant.getContact()
	            		});
	        }
	    }

	    

}
