/**
 * 
 */
package com.citations;



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


	public CitationsManager(Context context)
	{
		this.context = context;
	}


	@SuppressWarnings("serial")
	private final LinkedHashMap<String, String[]> categories = new LinkedHashMap<String, String[]>()
	{
		{
			put("loveCategory", new String[0]);
			put("politicsCategory", new String[0]);
			put("funCategory", new String[0]);
			put("lifeCategory", getLifeCategoryStrings());
			put("inspiringCategory", getInspiringCategoryStrings());

		}
	};


	/**
	 * @param categoryInUse
	 *            category which is now in use
	 * @return a random string within the category in use
	 */
	public String getRandomStringInCategory()
	{
		String[] citationsOfCategory = categories.get(categoryInUse);
		int randS = (int) (Math.random() * (citationsOfCategory.length + 1));


		return citationsOfCategory[randS];
	}


	/**
	 * @return a completely random string
	 */
	public String getRandomString()
	{
		int randC = (int) (Math.random() * (categories.size() + 1));
		String[] category = categories.get(randC);

		int randS = (int) (Math.random() * (category.length + 1));

		return category[randS];
	}


	public void setCategoryInUse(String category)
	{
		categoryInUse = category;
	}


	public String getCategoryInUse()
	{
		return categoryInUse;
	}


	/**
	 * @return the array with the strings for the category
	 */
	private String[] getInspiringCategoryStrings()
	{
		String[] inspiringStrings = { context.getString(R.string.inspiringAnonymous1),
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
		String[] lifeStrings = { context.getString(R.string.lifeBacon1),
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


}// end CitationsData
