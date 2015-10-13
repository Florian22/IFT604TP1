package server;

import commun.Match;
import commun.Penalite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LNH {
    private List<Match> matchs;
    
    public LNH() {
    	matchs = new ArrayList<Match>();
	}

	public void startMatch(Match m){
		matchs.add(m);
    }

	public Map<Integer, String> getMatchList() {
		Map<Integer, String> listeDesMatchs = new HashMap<Integer, String>();
		for(Match m : matchs)
			listeDesMatchs.put(m.getId(), m.toString());
		return listeDesMatchs;
	}
}


