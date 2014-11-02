package com.faraday.citations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

/**
 * Manages the state of the application, such as which citation is displayed, which citation goes next and so on.
 * 
 * @author Gabriele Lanaro
 *
 */
public class CitationState {
	
	CitationsDB db;
	Map<Category, List<Citation>> citations;
	
	public CitationState(Context context) {
		db = new CitationsDB(context);
		citations = new HashMap<Category, List<Citation>>();
		
		// Initialize internal list of citations
		for (Category category : Category.values()) {
			citations.put(category, db.getCitations(category));
		}
		
		
		
	}
	
}
