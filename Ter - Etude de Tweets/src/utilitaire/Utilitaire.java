package utilitaire;

import java.util.HashMap;
import java.util.ArrayList;

public class Utilitaire {
    public static HashMap<String, Integer> factorLevels(ArrayList<String> tab) {
	int i;
	HashMap<String, Integer> res = new HashMap<String, Integer>();

	for (i=0; i<tab.size(); i++) {
	    String key = tab.get(i);
	    if (res.containsKey(key)) {
		res.put(key, res.get(key)+1);
	    } else {
		res.put(key, 1);
	    }
	}

	return res;
    }
}