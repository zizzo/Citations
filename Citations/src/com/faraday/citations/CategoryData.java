package com.faraday.citations;
import java.util.*;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CategoryData {
	
	private HashMap<Category, Integer> colorMap;
	private HashMap<Category, Bitmap> bitmapMap;
	
    public CategoryData (Context context) {
    	
    	Resources res = context.getResources();
    	colorMap = new HashMap<Category, Integer>();
    	bitmapMap = new HashMap<Category, Bitmap>();
    	
		// Populating color map
		colorMap.put(Category.LIFE,
			res.getColor(R.color.lifeCategoryColor));
		colorMap.put(Category.INSPIRING,
			res.getColor(R.color.inspiringCategoryColor));
		colorMap.put(Category.LOVE,
			res.getColor(R.color.loveCategoryColor));
		colorMap.put(Category.POLITICS,
			res.getColor(R.color.politicsCategoryColor));
		colorMap.put(Category.FUN,
			res.getColor(R.color.funCategoryColor));
		
		// Populating bitmaps
		bitmapMap.put(Category.LIFE,
			BitmapFactory.decodeResource(res, R.drawable.life_cat));
		bitmapMap.put(Category.INSPIRING, 
			BitmapFactory.decodeResource(res, R.drawable.inspiring_cat));
		bitmapMap.put(Category.LOVE,
			BitmapFactory.decodeResource(res, R.drawable.love_cat));
		bitmapMap.put(Category.POLITICS,
			BitmapFactory.decodeResource(res, R.drawable.politics_cat));
		bitmapMap.put(Category.FUN,
			BitmapFactory.decodeResource(res, R.drawable.fun_cat));
    }
    
    /**
     * Get the color associated with the category
     * @param category
     * @return
     */
	public int getColor(Category category) {
		
		return colorMap.get(category);
	}
	
	/**
	 * Get the icon (as a Bitmap object) associated with the category
	 * @param category
	 * @return
	 */
	public Bitmap getIcon(Category category) {

		return bitmapMap.get(category);
	}
}
