package gestion_des_emplois.models;


public class Enseignant {
    private String nom;
    private String matricule;
    private String contact;

    public Enseignant(String nom, String matricule, String contact) {
        this.nom = nom;
        this.matricule = matricule;
        this.contact = contact;
    }

    public Enseignant() {
		// TODO Auto-generated constructor stub
	}

	// Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}