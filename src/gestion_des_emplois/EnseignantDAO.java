package gestion_des_emplois;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import gestion_des_emplois.models.Enseignant;

public class EnseignantDAO {
	
	public boolean enregistrerEnseignant(String nom, String matricule, String contact) {
	    String sql = "INSERT INTO enseignants (nom, matricule, contact) VALUES (?, ?, ?)";
	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setString(1, nom);
	        statement.setString(2, matricule);
	        statement.setString(3, contact);
	        statement.executeUpdate();
	        return true; // Return true if insertion is successful
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false; // Return false if there's an error
	    }
	}
    
    public Enseignant rechercherEnseignant(String matricule) {
        String sql = "SELECT * FROM enseignants WHERE matricule = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Définir le paramètre pour le matricule
            statement.setString(1, matricule);

            // Exécuter la requête et traiter le résultat
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Enseignant(
                        resultSet.getString("nom"),
                        resultSet.getString("matricule"),
                        resultSet.getString("contact")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retourner null si aucun enseignant ne correspond
    }
    
 
    // Method to get all teachers
    public List<Enseignant> getAllEnseignants() {
        List<Enseignant> enseignants = new ArrayList<>();
        String sql = "SELECT * FROM enseignants";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                enseignants.add(new Enseignant(
                        resultSet.getString("matricule"),
                        resultSet.getString("nom"),
                        resultSet.getString("contact")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enseignants;
    }

    // Method to modify teacher details
    public void modifierEnseignant(String matricule, String newNom, String newContact) {
        String sql = "UPDATE enseignants SET nom = ?, contact = ? WHERE matricule = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newNom);
            statement.setString(2, newContact);
            statement.setString(3, matricule);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Modification réussie.");
            } else {
                System.out.println("Aucune modification effectuée. Vérifiez le matricule.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete teacher by matricule
    public void supprimerEnseignant(String matricule) {
        String sql = "DELETE FROM enseignants WHERE matricule = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, matricule);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Enseignant supprimé avec succès.");
            } else {
                System.out.println("Aucun enseignant trouvé avec ce matricule.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void refreshEnseignantComboBox(JComboBox<String> comboBox) {
        comboBox.removeAllItems(); // Vider la liste déroulante
        try (Connection connection = DatabaseConnection.getConnection();
             java.sql.Statement statement = connection.createStatement()) {
            String query = "SELECT matricule FROM enseignants"; // Récupérer uniquement les matricules
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                comboBox.addItem(resultSet.getString("matricule")); // Ajouter chaque matricule dans la liste déroulante
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean isMatriculeDuplicate(String matricule) {
        String sql = "SELECT COUNT(*) FROM enseignants WHERE matricule = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, matricule);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }
    
    
    public boolean isEnseignantOccupied(String enseignant, String jour, String heure) {
        String sql = "SELECT COUNT(*) FROM seances WHERE enseignant = ? AND jour = ? AND heure = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, enseignant);
            statement.setString(2, jour);
            statement.setString(3, heure);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0; // Retourne true si l'enseignant est occupé
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retourne false si l'enseignant n'est pas occupé ou en cas d'erreur
    }
    
    public boolean isClasseOccupied(String classe, String jour, String heure) {
        String sql = "SELECT COUNT(*) FROM seances WHERE classe = ? AND jour = ? AND heure = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, classe);
            statement.setString(2, jour);
            statement.setString(3, heure);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0; // Retourne true si la classe est occupée
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retourne false si la classe n'est pas occupée ou en cas d'erreur
    }
    

    // Ajoutez d'autres méthodes pour modifier, supprimer et rechercher des enseignants
}