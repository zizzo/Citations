package com.faraday.citations;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class CitationsDB extends SQLiteAssetHelper {
	private static final String DATABASE_NAME = "citations.db";
	private static final int DATABASE_VERSION = 1;

	public CitationsDB(Context context) {
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/** 
	 * Get a random citation from the specified category
	 * @author Gabriele Lanaro 
	 */
	public Citation getRandomCitation(Category category) {
		SQLiteDatabase db = getReadableDatabase();
		
		// Get a random citation in a category
		Cursor cursor = db.rawQuery(
				"select * from Citations where category_id = ? " +
		        "order by random() limit 1",
				new String[] { Integer.toString(category.ordinal()) });
		cursor.moveToFirst();
		int citationId = cursor.getInt(cursor.getColumnIndex("id"));
		int categoryId = cursor.getInt(cursor.getColumnIndex("category_id"));
		Category citationCategory = Category.fromOrdinal(categoryId);
		cursor.close();
		
		String lang = getLanguage();
		
		// Get the citation text
		cursor = db.rawQuery(
				"select * from CitationsData where citation_id = ? and" +
		        " language like ?",
				new String [] {Integer.toString(citationId), lang + "%"});
		cursor.moveToFirst();
		String citationText = cursor.getString(cursor.getColumnIndex("value"));
		String citationAuthor = cursor.getString(cursor.getColumnIndex("author"));
		cursor.close();
		db.close();
		return new Citation(citationId, citationText, citationAuthor, citationCategory);
	};

	/**
	 * Get a citation by its unique ID
	 * 
	 * @param id
	 * @return
	 */
	public Citation getCitation(Integer id) {
		SQLiteDatabase db = getReadableDatabase();
		
		// Get a random citation in a category
		Cursor cursor = db.rawQuery(
				"select * from Citations where id = ? ",
				new String[] { Integer.toString(id) });
		cursor.moveToFirst();
		int categoryId = cursor.getInt(cursor.getColumnIndex("category_id"));
		Category citationCategory = Category.fromOrdinal(categoryId);
		cursor.close();
		
		String lang = getLanguage();
		
		// Get the citation text
		cursor = db.rawQuery(
				"select * from CitationsData where citation_id = ? and" +
		        " language like ?",
				new String [] {Integer.toString(id), lang + "%"});
		cursor.moveToFirst();
		String citationText = cursor.getString(cursor.getColumnIndex("value"));
		String citationAuthor = cursor.getString(cursor.getColumnIndex("author"));
		cursor.close();
		db.close();
		return new Citation(id, citationText, citationAuthor, citationCategory);
	}
	
	private String getLanguage() 
	{
		return Locale.getDefault().getLanguage();
	}

	public List<Citation> getCitations(Category category) {
		List<Citation> ret = new ArrayList<Citation>();
		// We need to do a huge join here
		
		
		return ret;
	}

}
