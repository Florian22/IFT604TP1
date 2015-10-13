package commun;

import java.util.ArrayList;
import java.util.List;

public class Match {
	private int id;
	private String equipeA;
    private String equipeB;
    private int periode;
    private Double chronometre;
    private List<String> compteursA;
    private List<String> compteursB;
    private List<Penalite> penalitesA;
    private List<Penalite> penalitesB;
	
    public Match(String equipeA, String equipeB, int id) {
    	this.id = id;
		this.equipeA = equipeA;
		this.equipeB = equipeB;
		this.periode = 1;
		this.chronometre = 20.0;
		this.compteursA = new ArrayList<String>();
		this.compteursB = new ArrayList<String>();
		this.penalitesA = new ArrayList<Penalite>();
		this.penalitesB = new ArrayList<Penalite>();
	}
    
    public void scoreEquipeA(String compteur){
    	compteursA.add(compteur);
    }
    
    public void scoreEquipeB(String compteur){
    	compteursB.add(compteur);
    }
    
    public void penaliteEquipeA(String joueur, int minutes){
    	double liberation = Double.max(0.0, chronometre - minutes);
    	penalitesA.add(new Penalite(joueur, liberation));
    }

	public int getId() {
		return id;
	}
	
	public String toString(){
		return equipeA + " VS " + equipeB;
		
	}
}
