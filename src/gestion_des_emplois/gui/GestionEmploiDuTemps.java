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

/**
 * Main class for managing the timetable (Emploi du Temps) of teachers and courses.
 * This class provides a GUI interface for adding, modifying, and deleting teachers and courses.
 */
public class GestionEmploiDuTemps extends JFrame {
    private static final long serialVersionUID = 1L; // Serialization ID
    private EnseignantDAO enseignantDAO = new EnseignantDAO(); // DAO for teacher operations
    private SeanceDAO seanceDAO = new SeanceDAO(); // DAO for course session operations

    private DefaultTableModel teacherTableModel; // Table model for teachers
    private DefaultTableModel courseTableModel; // Table model for courses
    private CardLayout cardLayout; // Layout for switching panels
    private JPanel mainPanel; // Main panel to hold teacher and course panels
    private JComboBox<String> comboMatriculeEns;

    /**
     * Constructor to initialize the main frame and its components.
     */
    public GestionEmploiDuTemps() {
        // Configure the main window
        setTitle("Gestion des Emplois du Temps");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Define background colors
        Color backgroundColor = new Color(240, 240, 240);
        Color panelColor = new Color(255, 255, 255);

        // Create the main panel with CardLayout for switching views
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create the teacher and course panels
        JPanel teacherPanel = createTeacherPanel(panelColor);
        JPanel coursePanel = createCoursePanel(panelColor);

        // Add both panels to the main panel
        mainPanel.add(teacherPanel, "TeacherPanel");
        mainPanel.add(coursePanel, "CoursePanel");

        // Add the main panel to the frame
        add(mainPanel, BorderLayout.CENTER);

        // Create a button panel for switching views
        JPanel buttonPanel = new JPanel();
        JButton btnShowTeachers = new JButton("Afficher les enseignants");
        JButton btnShowSessions = new JButton("Afficher les séances");

        // Add action listeners to switch panels
        btnShowTeachers.addActionListener(e -> switchPanel("TeacherPanel"));
        btnShowSessions.addActionListener(e -> switchPanel("CoursePanel"));

        // Add buttons to the button panel
        buttonPanel.add(btnShowTeachers);
        buttonPanel.add(btnShowSessions);
        add(buttonPanel, BorderLayout.NORTH);

        // Make the window visible
        setVisible(true);
    }

    /**
     * Switches between the teacher and course panels with a fade effect.
     *
     * @param panelName The name of the panel to switch to.
     */
    private void switchPanel(String panelName) {
        // Fade out the current panel
        fadeOut(mainPanel, () -> {
            cardLayout.show(mainPanel, panelName);
            // Fade in the new panel
            fadeIn(mainPanel);
        });
    }

    /**
     * Fades out a panel by reducing its opacity.
     *
     * @param panel      The panel to fade out.
     * @param onComplete A callback to execute after the fade-out completes.
     */
    private void fadeOut(JPanel panel, Runnable onComplete) {
        Timer timer = new Timer(50, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 1.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f;
                if (opacity <= 0) {
                    opacity = 0;
                    panel.setVisible(false);
                    timer.stop();
                    onComplete.run();
                }
                panel.setBackground(new Color(240, 240, 240, (int) (opacity * 255)));
            }
        });
        timer.start();
    }

    /**
     * Fades in a panel by increasing its opacity.
     *
     * @param panel The panel to fade in.
     */
    private void fadeIn(JPanel panel) {
        panel.setVisible(true);
        Timer timer = new Timer(50, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 0.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity >= 1) {
                    opacity = 1;
                    timer.stop();
                }
                panel.setBackground(new Color(240, 240, 240, (int) (opacity * 255)));
            }
        });
        timer.start();
    }

    /**
     * Creates the teacher management panel.
     *
     * @param panelColor The background color of the panel.
     * @return The created teacher panel.
     */
    private JPanel createTeacherPanel(Color panelColor) {
        JPanel teacherPanel = new JPanel();
        teacherPanel.setLayout(new GridBagLayout());
        teacherPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                "Formulaire d'enregistrement des enseignants",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 16)
        ));
        teacherPanel.setBackground(panelColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

     // Add components for teacher management
        gbc.gridx = 0; gbc.gridy = 0; teacherPanel.add(new JLabel("Matricule:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; 
        JTextFieldWithPlaceholder txtMatricule = new JTextFieldWithPlaceholder("Entrez le matricule");
        txtMatricule.setColumns(20); // Augmenter la largeur à 20 colonnes
        teacherPanel.add(txtMatricule, gbc);
        gbc.gridx = 2; gbc.gridy = 0; 
        JButton btnChercher = new JButton("CHERCHER"); 
        teacherPanel.add(btnChercher, gbc);

        gbc.gridx = 0; gbc.gridy = 1; teacherPanel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; 
        JTextFieldWithPlaceholder txtNom = new JTextFieldWithPlaceholder("Entrez le nom");
        txtNom.setColumns(20); // Augmenter la largeur à 20 colonnes
        teacherPanel.add(txtNom, gbc);

        gbc.gridx = 0; gbc.gridy = 2; teacherPanel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; 
        JTextFieldWithPlaceholder txtContact = new JTextFieldWithPlaceholder("Entrez le contact");
        txtContact.setColumns(20); // Augmenter la largeur à 20 colonnes
        teacherPanel.add(txtContact, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; 
        JButton btnEnregistrerEns = new JButton("ENREGISTRER"); 
        teacherPanel.add(btnEnregistrerEns, gbc);
        gbc.gridx = 1; gbc.gridy = 3; 
        JButton btnModifierEns = new JButton("MODIFIER"); 
        teacherPanel.add(btnModifierEns, gbc);
        gbc.gridx = 2; gbc.gridy = 3; 
        JButton btnSupprimerEns = new JButton("SUPPRIMER"); 
        teacherPanel.add(btnSupprimerEns, gbc);

        // Add a refresh button above the table
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3;
        JButton btnRefresh = new JButton("Rafraîchir");
        teacherPanel.add(btnRefresh, gbc);

        // Table for displaying teachers
        String[] teacherColumns = {"Matricule", "Nom", "Contact"};
        teacherTableModel = new DefaultTableModel(teacherColumns, 0);
        JTable teacherTable = new JTable(teacherTableModel);
        JScrollPane teacherScrollPane = new JScrollPane(teacherTable);
        teacherScrollPane.setPreferredSize(new Dimension(400, 150));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3; teacherPanel.add(teacherScrollPane, gbc);

        // Dynamically update the teacher table
        refreshTeacherTable();

        // Add action listeners for buttons
        btnChercher.addActionListener(e -> {
            String matricule = txtMatricule.getText();
            if (!matricule.isEmpty()) {
                Enseignant enseignant = enseignantDAO.rechercherEnseignant(matricule);
                if (enseignant != null) {
                    teacherTableModel.setRowCount(0);
                    teacherTableModel.addRow(new Object[]{enseignant.getMatricule(), enseignant.getNom(), enseignant.getContact()});
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun enseignant trouvé avec ce matricule.", "Erreur", JOptionPane.ERROR_MESSAGE);
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

            // Check if any field is empty
            if (matricule.isEmpty() || nom.isEmpty() || contact.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return; // Exit the method if any field is empty
            }

            // Check if the matricule is a duplicate
            if (enseignantDAO.isMatriculeDuplicate(matricule)) {
                JOptionPane.showMessageDialog(this, "Le matricule existe déjà.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return; // Exit the method if the matricule is a duplicate
            }

            // Validate the contact field
            if (!contact.matches("\\d{8}")) { // Regex to check if contact is exactly 8 digits
                JOptionPane.showMessageDialog(this, "Le contact doit contenir exactement 8 chiffres.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return; // Exit the method if the contact is invalid
            }

            // Save the teacher if the matricule is not a duplicate and the contact is valid
            boolean isSaved = enseignantDAO.enregistrerEnseignant(nom, matricule, contact);

            if (isSaved) {
                // Show success message
                JOptionPane.showMessageDialog(this, "Enseignant enregistré avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);

                // Refresh the teacher table
                refreshTeacherTable();

                // Clear the text fields
                txtMatricule.setText("");
                txtNom.setText("");
                txtContact.setText("");

                // Rafraîchir la JComboBox des matricules des enseignants
                enseignantDAO.refreshEnseignantComboBox(comboMatriculeEns);
            } else {
                // Show error message if saving fails
                JOptionPane.showMessageDialog(this, "Une erreur s'est produite lors de l'enregistrement.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnModifierEns.addActionListener(e -> {
            String matricule = txtMatricule.getText();
            String nom = txtNom.getText();
            String contact = txtContact.getText();
            if (!matricule.isEmpty() && !nom.isEmpty() && !contact.isEmpty()) {
                enseignantDAO.modifierEnseignant(matricule, nom, contact);
                refreshTeacherTable();
                txtMatricule.setText("");
                txtNom.setText("");
                txtContact.setText("");
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

        // Add action listener for the refresh button
        btnRefresh.addActionListener(e -> {
            // Refresh the teacher table
            refreshTeacherTable();

            // Clear the text fields
            txtMatricule.setText("");
            txtNom.setText("");
            txtContact.setText("");
        });

        return teacherPanel;
    }
    /**
     * Creates the course management panel.
     *
     * @param panelColor The background color of the panel.
     * @return The created course panel.
     */
    private JPanel createCoursePanel(Color panelColor) {
        JPanel coursePanel = new JPanel();
        coursePanel.setLayout(new GridBagLayout());
        coursePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                "Formulaire d'enregistrement des séances de cours",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 16)
        ));
        coursePanel.setBackground(panelColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add components for course management
        gbc.gridx = 0; gbc.gridy = 0; coursePanel.add(new JLabel("Classe:"), gbc);
        JComboBox<String> comboClasse = new JComboBox<>(new String[]{"6eme", "5eme", "4eme", "3eme", "2eme", "1ere"});
        gbc.gridx = 1; gbc.gridy = 0; coursePanel.add(comboClasse, gbc);

        gbc.gridx = 0; gbc.gridy = 1; coursePanel.add(new JLabel("Matière:"), gbc);
        JTextFieldWithPlaceholder txtMatiere = new JTextFieldWithPlaceholder("Entrez la matière"); // Placeholder
        gbc.gridx = 1; gbc.gridy = 1; coursePanel.add(txtMatiere, gbc);

        gbc.gridx = 0; gbc.gridy = 2; coursePanel.add(new JLabel("Jour:"), gbc);
        JComboBox<String> comboJour = new JComboBox<>(new String[]{
            "LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI", "SAMEDI", "DIMANCHE"
        });
        gbc.gridx = 1; gbc.gridy = 2; coursePanel.add(comboJour, gbc);

        gbc.gridx = 0; gbc.gridy = 3; coursePanel.add(new JLabel("Heure:"), gbc);
        JComboBox<String> comboHeure = new JComboBox<>(new String[]{
            "1ere H", "2eme H", "3eme H", "4eme H", "1ere et 2eme H", "3eme et 4eme H"
        });
        gbc.gridx = 1; gbc.gridy = 3; coursePanel.add(comboHeure, gbc);

        gbc.gridx = 0; gbc.gridy = 4; coursePanel.add(new JLabel("Matricule enseignant:"), gbc);
        comboMatriculeEns = new JComboBox<>(); // Initialiser la JComboBox
        gbc.gridx = 1; gbc.gridy = 4; coursePanel.add(comboMatriculeEns, gbc);

        // Populate the teacher combo box with data
        enseignantDAO.refreshEnseignantComboBox(comboMatriculeEns);
        gbc.gridx = 0; gbc.gridy = 5; 
        JButton btnEnregistrerCours = new JButton("ENREGISTRER");
        coursePanel.add(btnEnregistrerCours, gbc);
        gbc.gridx = 1; gbc.gridy = 5; 
        JButton btnRequetes = new JButton("REQUETES");
        coursePanel.add(btnRequetes, gbc);

        // Table for displaying courses
        String[] courseColumns = {"Classe", "Matière", "Jour", "Heure", "Enseignant"};
        courseTableModel = new DefaultTableModel(courseColumns, 0);
        JTable courseTable = new JTable(courseTableModel);
        JScrollPane courseScrollPane = new JScrollPane(courseTable);
        courseScrollPane.setPreferredSize(new Dimension(400, 150));
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; coursePanel.add(courseScrollPane, gbc);
        refreshTableSeance();
        refreshTeacherTable();

        // Add action listeners for buttons
        btnRequetes.addActionListener(e -> {
            // Pass the parent JFrame (GestionEmploiDuTemps.this) to the RequetesInterface constructor
            new RequetesInterface(GestionEmploiDuTemps.this);
        });

        btnEnregistrerCours.addActionListener(e -> {
            String classe = (String) comboClasse.getSelectedItem();
            String matiere = txtMatiere.getText();
            String jour = (String) comboJour.getSelectedItem();
            String heure = (String) comboHeure.getSelectedItem();
            String matriculeEns = (String) comboMatriculeEns.getSelectedItem();

            // Vérifier si tous les champs sont remplis
            if (classe == null || matiere.isEmpty() || jour == null || heure == null || matriculeEns == null) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return; // Sortir de la méthode si un champ est vide
            }

            // Vérifier si la classe est déjà occupée à cette heure
            if (seanceDAO.isClasseOccupied(classe, jour, heure)) {
                JOptionPane.showMessageDialog(this, "La classe est déjà occupée à cette heure.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return; // Sortir de la méthode si la classe est occupée
            }

            // Vérifier si l'enseignant est déjà occupé à cette heure
            if (seanceDAO.isEnseignantOccupied(matriculeEns, jour, heure)) {
                JOptionPane.showMessageDialog(this, "L'enseignant est déjà occupé à cette heure.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return; // Sortir de la méthode si l'enseignant est occupé
            }

            // Enregistrer la séance si tout est valide
            boolean isSaved = seanceDAO.enregistrerSeance(classe, matiere, jour, heure, matriculeEns);

            if (isSaved) {
                JOptionPane.showMessageDialog(this, "Séance enregistrée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                comboClasse.setSelectedIndex(0);
                txtMatiere.setText("");
                comboJour.setSelectedIndex(0);
                comboHeure.setSelectedIndex(0);
                comboMatriculeEns.setSelectedIndex(0);
                refreshTableSeance(); // Rafraîchir la table des séances
            } else {
                JOptionPane.showMessageDialog(this, "Une erreur s'est produite lors de l'enregistrement.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        return coursePanel;
    }

    /**
     * Refreshes the course table with the latest data from the database.
     */
    private void refreshTableSeance() {
        courseTableModel.setRowCount(0);
        java.util.List<Seance> seances = seanceDAO.getAllSeances();
        for (Seance seance : seances) {
            courseTableModel.addRow(new Object[]{
                seance.getClasse(),
                seance.getMatiere(),
                seance.getJour(),
                seance.getHeure(),
                seance.getEnseignant()
            });
        }
    }

    /**
     * Refreshes the teacher table with the latest data from the database.
     */
    private void refreshTeacherTable() {
        teacherTableModel.setRowCount(0);
        java.util.List<Enseignant> enseignants = enseignantDAO.getAllEnseignants();
        for (Enseignant enseignant : enseignants) {
            teacherTableModel.addRow(new Object[]{
                enseignant.getNom(),
                enseignant.getMatricule(),
                enseignant.getContact()
            });
        }
    }

    /**
     * Main method to launch the application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GestionEmploiDuTemps::new);
    }
}

// Classe personnalisée pour les champs de texte avec placeholder
class JTextFieldWithPlaceholder extends JTextField {
    private String placeholder;

    public JTextFieldWithPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dessiner le placeholder si le texte est vide
        if (getText().isEmpty() && !isFocusOwner()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.GRAY); // Couleur du placeholder
            g2.setFont(getFont().deriveFont(Font.ITALIC)); // Style de police
            g2.drawString(placeholder, getInsets().left, g.getFontMetrics().getMaxAscent() + getInsets().top);
            g2.dispose();
        }
    }
}