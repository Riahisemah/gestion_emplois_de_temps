package gestion_des_emplois.models;


public class Seance {
	private int id;
    private String classe;
    private String matiere;
    private String jour;
    private String heure;
    private String enseignant ; 
    private Enseignant enseignant1 ;
    public Seance(String classe, String matiere, String jour, String heure, String enseignant) {
        this.classe = classe;
        this.matiere = matiere;
        this.jour = jour;
        this.heure = heure;
        this.enseignant = enseignant ;
    }

    // Getters et Setters
    
    
    public Seance() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }  
    
    
    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(String enseignant) {
        this.enseignant = enseignant;
    }
    
    public Enseignant getEnseignant1() {
        return enseignant1;
    }

    public void setEnseignan1t(Enseignant enseignant) {
        this.enseignant1 = enseignant;
    }

}
