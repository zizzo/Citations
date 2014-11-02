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
	
	/** the citation database */
	private CitationsDB db; 
	
	/** For every category we have a list of citations to be displayed */
	private Map<Category, List<Citation>> citations;
	
	/** For every category we have an integer that represent at which citation we're at */
	private Map<Category, Integer> citationPointer;
	
	private Category currentCategory;
	
	
	public CitationState(Context context) {
		db = new CitationsDB(context);
		citations = new HashMap<Category, List<Citation>>();
		citationPointer = new HashMap<Category, Integer>();
		currentCategory = Category.INSPIRING; // Default
		
		// Initialize internal list of citations for each category
		for (Category category : Category.values()) {
			citations.put(category, db.getCitations(category));
			citationPointer.put(category, 0);
		}
		
	}
	
	
	public Citation nextCitation() {
		Integer newCitationPtr = citationPointer.get(currentCategory);
		// Increase it by one and cycle it
		newCitationPtr = (newCitationPtr + 1) % citations.get(currentCategory).size();
		citationPointer.put(currentCategory, newCitationPtr);
		
		return getCurrentCitation();
	}
	
	public Citation previousCitation() {
		Integer newCitationPtr = citationPointer.get(currentCategory);
		// Decrease by one and cycle it
		Integer len = citations.get(currentCategory).size();
		newCitationPtr = (newCitationPtr - 1 + len) % len;
		citationPointer.put(currentCategory, newCitationPtr);
		
		return getCurrentCitation();
	}
	
	
	public Category getCurrentCategory() {
		return currentCategory;
	}
	
	public void setCurrentCategory(Category category){
		currentCategory = category;
	}

	
	public Citation getCurrentCitation() {
		return citations.get(currentCategory).get(citationPointer.get(currentCategory));
	}
	
	public void setCurrentCitation(Citation citation) {
		setCurrentCategory(citation.getCategory());
		List<Citation> cits = citations.get(currentCategory);
		// We have to find the citation and set it as current using the pointer
		for (Integer i=0; i < cits.size(); i++) {
			if (cits.get(i).getId() == citation.getId()) {
				citationPointer.put(currentCategory, i);
				break;
			}
		}
	}

	
}
