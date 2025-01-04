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

public void enregistrerSeance(String classe, String matiere, String jour, String heure, String enseignant) {
    String sql = "INSERT INTO seances (classe, matiere, jour, heure,enseignant) VALUES (?, ?, ?, ?, ?)";
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, classe);
        statement.setString(2, matiere);
        statement.setString(3, jour);
        statement.setString(4, heure);
        statement.setString(5, enseignant); // Add this line to set enseignant
        statement.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
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
