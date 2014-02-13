/**
 * 
 */
package com.citations;

import java.util.HashMap;
import java.util.LinkedHashMap;

import android.content.Context;

/**
 * @author luigi
 * 
 *         The class is aimed at fetching the needed strings to be used in the
 *         app. It is somewhat not very straightforward the process of adding a
 *         new citation: add the citation to the whole strings files and then
 *         remember to add it here. Yet there is from a programming point of
 *         view much easier as here you don't need to worry at all about the
 *         language which is managed automatically by Android.
 */
public class CitationsManager
{
	private final Context context;
	private String categoryInUse;

    private final LinkedHashMap<String, String[]> categories = new LinkedHashMap<String, String[]>();
    private final HashMap<String, Integer> colormap = new HashMap<String, Integer>();


	public CitationsManager(Context context)
	{
		this.context = context;
		categories.put("loveCategory", getLoveCategoryStrings());
		categories.put("politicsCategory", getPoliticsCategoryStrings());
		categories.put("funCategory", getFunCategoryStrings());
		categories.put("lifeCategory", getLifeCategoryStrings());
		categories.put("inspiringCategory", getInspiringCategoryStrings());

        // Populating color map
        colormap.put("lifeCategory", context.getResources().getColor(R.color.lifeCategoryColor));
        colormap.put("inspiringCategory", context.getResources().getColor(R.color.inspiringCategoryColor));
		colormap.put("loveCategory",
				context.getResources().getColor(R.color.loveCategoryColor));
		colormap.put("politicsCategory",
				context.getResources().getColor(R.color.politicsCategoryColor));
		colormap.put("funCategory",
				context.getResources().getColor(R.color.funCategoryColor));
	}

	// @SuppressWarnings("serial")

	/**
	 * @return a random string within the category in use
	 */
	public String getRandomStringInCategory()
	{
		String[] citationsOfCategory = categories.get(categoryInUse);
		int upperMaximum = citationsOfCategory.length - 1;
		int randS = StaticData.randInt(0, upperMaximum);

		return citationsOfCategory[randS];
	}

	/**
	 * @return a completely random string
	 */
	public String getRandomString()
	{
		int upperMaximum = categories.size() - 1;
		int randC = StaticData.randInt(0, upperMaximum);
		switch (randC)
		{
		case 0:
			setCategoryInUse("lifeCategory");
			break;

		case 1:
			setCategoryInUse("inspiringCategory");
			break;

		case 2:
			setCategoryInUse("loveCategory");
			break;

		case 3:
			setCategoryInUse("funCategory");
			break;

		case 4:
			setCategoryInUse("politicsCategory");
			break;

		default:
			break;
		}

		return getRandomStringInCategory();
	}

	public void setCategoryInUse(String category)
	{
		categoryInUse = category;
	}

	public String getCategoryInUse()
	{
		return categoryInUse;
	}

    public Integer getCategoryInUseColor() { return colormap.get(getCategoryInUse()); }

	/**
	 * @return the array with the strings for the category
	 */
	private String[] getInspiringCategoryStrings()
	{
		String[] inspiringStrings = {
				context.getString(R.string.inspiringLiKaShing1),
				context.getString(R.string.inspiringAnonymous1),
				context.getString(R.string.inspiringAristotele1),
				context.getString(R.string.inspiringBacon1),
				context.getString(R.string.inspiringCaesar1),
				context.getString(R.string.inspiringCaesar2),
				context.getString(R.string.inspiringCheGuevara1),
				context.getString(R.string.inspiringCheGuevara2),
				context.getString(R.string.inspiringChesterfield1),
				context.getString(R.string.inspiringChesterton1),
				context.getString(R.string.inspiringChurchill1),
				context.getString(R.string.inspiringChurchill2),
				context.getString(R.string.inspiringCoelho1),
				context.getString(R.string.inspiringEinstein1),
				context.getString(R.string.inspiringEsopo1),
				context.getString(R.string.inspiringFord1),
				context.getString(R.string.inspiringGoethe1),
				context.getString(R.string.inspiringGoethe2),
				context.getString(R.string.inspiringJesus1),
				context.getString(R.string.inspiringJohnson1),
				context.getString(R.string.inspiringKennedy1),
				context.getString(R.string.inspiringKing1),
				context.getString(R.string.inspiringLec1),
				context.getString(R.string.inspiringNietzsche1),
				context.getString(R.string.inspiringRoosvelt1),
				context.getString(R.string.inspiringSeneca1),
				context.getString(R.string.inspiringSFrancesco1),
				context.getString(R.string.inspiringShaw1),
				context.getString(R.string.inspiringTertulliano1),
				context.getString(R.string.inspiringTwain1),
				context.getString(R.string.inspiringUltang1) };

		return inspiringStrings;
	}

	/**
	 * @return the array with the strings for the category
	 */
	private String[] getLifeCategoryStrings()
	{
		String[] lifeStrings = { context.getString(R.string.lifeLeopardi1),
				context.getString(R.string.lifeBacon1),
				context.getString(R.string.lifeBattaglia1),
				context.getString(R.string.lifeButturini1),
				context.getString(R.string.lifeCheGuevara1),
				context.getString(R.string.lifeChineseProverb1),
				context.getString(R.string.lifeDeBono1),
				context.getString(R.string.lifeDelacroix1),
				context.getString(R.string.lifeGandhi1),
				context.getString(R.string.lifeGoethe1),
				context.getString(R.string.lifeMizner1),
				context.getString(R.string.lifeTiburzi1),
				context.getString(R.string.lifeWest1),
				context.getString(R.string.lifeWilde1),
				context.getString(R.string.lifeWilde2),
				context.getString(R.string.lifeWilde3) };

		return lifeStrings;
	}

	/**
	 * @return the strings about politics
	 */
	private String[] getPoliticsCategoryStrings()
	{
		String[] politicsStrings = {
				context.getString(R.string.politicsDante1),
				context.getString(R.string.politicsPlato1) };

		return politicsStrings;
	}

	/**
	 * @return the strings about love
	 */
	private String[] getLoveCategoryStrings()
	{
		String[] loveStrings = { context.getString(R.string.loveTzu1),
				context.getString(R.string.loveWilde1) };

		return loveStrings;
	}


	/**
	 * @return fun category strings
	 */
	private String[] getFunCategoryStrings()
	{
		String[] funCategory = { context.getString(R.string.funTomlin1),
				context.getString(R.string.funTurner1) };

		return funCategory;
	}


}// end CitationsData
