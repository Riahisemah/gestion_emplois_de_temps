package gestion_des_emplois;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gestion_des_emplois.models.Enseignant;
import gestion_des_emplois.models.Seance;

public class SeanceDAO {
	

    public  List<Seance> getAllSeances() {
        List<Seance> seances = new ArrayList<>();
        String sql = "SELECT * FROM seances";
        	try (
        			Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Seance seance = new Seance(
                    resultSet.getString("classe"),
                    resultSet.getString("matiere"),
                    resultSet.getString("jour"),
                    resultSet.getString("heure"),
                    resultSet.getString("enseignant")

                );
                System.out.println(seance);
                seances.add(seance);
            }
        } catch (SQLException e) {
        	System.out.println("Dao Seance");
            e.printStackTrace();
        }
        return seances;
    }

    public boolean enregistrerSeance(String classe, String matiere, String jour, String heure, String enseignant) {
        // Vérifier si la classe est déjà occupée
        if (isClasseOccupied(classe, jour, heure)) {
            System.out.println("La classe est déjà occupée à cette heure.");
            return false; // Retourne false si la classe est occupée
        }

        // Vérifier si l'enseignant est déjà occupé
        if (isEnseignantOccupied(enseignant, jour, heure)) {
            System.out.println("L'enseignant est déjà occupé à cette heure.");
            return false; // Retourne false si l'enseignant est occupé
        }

        // Si tout est bon, insérer la séance
        String sql = "INSERT INTO seances (classe, matiere, jour, heure, enseignant) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, classe);
            statement.setString(2, matiere);
            statement.setString(3, jour);
            statement.setString(4, heure);
            statement.setString(5, enseignant);
            statement.executeUpdate();
            return true; // Retourne true si l'insertion est réussie
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Retourne false en cas d'erreur
        }
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
    



public List<Seance> getSessionsByClassAndSubject(String classe, String matiere) {
    List<Seance> sessions = new ArrayList<>();
    String query = "SELECT seances.id, seances.classe, seances.matiere, seances.jour, " +
                   "seances.heure, enseignants.nom, enseignants.contact " +
                   "FROM seances " +
                   "JOIN enseignants ON seances.enseignant = enseignants.matricule " +
                   "WHERE seances.classe = ? AND seances.matiere = ?";

    try (
    		Connection connection = DatabaseConnection.getConnection();
    		PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        // Set parameters for the query
        preparedStatement.setString(1, classe);
        preparedStatement.setString(2, matiere);

        // Execute the query and process the result set
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
        	System.out.println(resultSet);
            // Create a Seance object from the current row
            Seance seance = new Seance();
            seance.setId(resultSet.getInt("id"));
            seance.setClasse(resultSet.getString("classe"));
            seance.setMatiere(resultSet.getString("matiere"));
            seance.setJour(resultSet.getString("jour"));
            seance.setHeure(resultSet.getString("heure"));

            // Create an Enseignant object from the result set
            Enseignant enseignant = new Enseignant();
            enseignant.setNom(resultSet.getString("nom"));
            enseignant.setContact(resultSet.getString("contact"));

            // Set the enseignant object in the seance
            seance.setEnseignan1t(enseignant);
            System.out.println(seance);
            // Add the session to the list
            sessions.add(seance);
        }
    } catch (SQLException e) {
        e.printStackTrace();  // Handle exceptions
    }

    return sessions;  // Return the list of sessions
}

//In SeanceDAO
public boolean deleteSeanceById(String id) throws SQLException {
 // Assuming you have a valid connection `conn`
 String sql = "DELETE FROM seances WHERE id = ?";  // Adjust the query based on your actual table structure

 try (
 		Connection connection = DatabaseConnection.getConnection();

		 PreparedStatement stmt = connection.prepareStatement(sql)) {
     stmt.setString(1, id);
     int affectedRows = stmt.executeUpdate();
     
     return affectedRows > 0;  // If no rows were affected, deletion failed
 }
}



public List<Seance> searchByClass(String classe) {
    List<Seance> sessions = new ArrayList<>();
    String query = "SELECT seances.id, seances.classe, seances.matiere, seances.jour, " +
                   "seances.heure, enseignants.nom, enseignants.contact " +
                   "FROM seances " +
                   "JOIN enseignants ON seances.enseignant = enseignants.matricule " +
                   "WHERE seances.classe = ?";

    try (
    		Connection connection = DatabaseConnection.getConnection();
    		PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        // Set parameters for the query
        preparedStatement.setString(1, classe);
        // Execute the query and process the result set
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            // Create a Seance object from the current row
            Seance seance = new Seance();
            seance.setId(resultSet.getInt("id"));
            seance.setClasse(resultSet.getString("classe"));
            seance.setMatiere(resultSet.getString("matiere"));
            seance.setJour(resultSet.getString("jour"));
            seance.setHeure(resultSet.getString("heure"));

            // Create an Enseignant object from the result set
            Enseignant enseignant = new Enseignant();
            enseignant.setNom(resultSet.getString("nom"));
            enseignant.setContact(resultSet.getString("contact"));

            // Set the enseignant object in the seance
            seance.setEnseignan1t(enseignant);
            System.out.println(seance);
            // Add the session to the list
            sessions.add(seance);
        }
    } catch (SQLException e) {
        e.printStackTrace();  // Handle exceptions
    }

    return sessions;  // Return the list of sessions
}







}
