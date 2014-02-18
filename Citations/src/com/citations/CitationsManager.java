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
				context.getString(R.string.inspiringAlcott1),
				context.getString(R.string.inspiringAli1),
				context.getString(R.string.inspiringAnonymous1),
				context.getString(R.string.inspiringAristophanes1),
				context.getString(R.string.inspiringAristotle1),
				context.getString(R.string.inspiringAristotle2),
				context.getString(R.string.inspiringAshe1),
				context.getString(R.string.inspiringAurelius1),
				context.getString(R.string.inspiringBach1),
				context.getString(R.string.inspiringBacon1),
				context.getString(R.string.inspiringBaker1),
				context.getString(R.string.inspiringBalfour1),
				context.getString(R.string.inspiringBallou1),
				context.getString(R.string.inspiringBarr1),
				context.getString(R.string.inspiringBarr2),
				context.getString(R.string.inspiringBarry1),
				context.getString(R.string.inspiringBeecher1),
				context.getString(R.string.inspiringBerle1),
				context.getString(R.string.inspiringBreathnach1),
				context.getString(R.string.inspiringBrooks1),
				context.getString(R.string.inspiringBuddha1),
				context.getString(R.string.inspiringBurnett1),
				context.getString(R.string.inspiringCaesar1),
				context.getString(R.string.inspiringCaesar2),
				context.getString(R.string.inspiringCampbell1),
				context.getString(R.string.inspiringCampbell2),
				context.getString(R.string.inspiringCamus1),
				context.getString(R.string.inspiringCheGuevara1),
				context.getString(R.string.inspiringCheGuevara2),
				context.getString(R.string.inspiringChesterfield1),
				context.getString(R.string.inspiringChesterton1),
				context.getString(R.string.inspiringChurchill1),
				context.getString(R.string.inspiringChurchill2),
				context.getString(R.string.inspiringCoelho1),
				context.getString(R.string.inspiringCopeland1),
				context.getString(R.string.inspiringDayne1),
				context.getString(R.string.inspiringDean1),
				context.getString(R.string.inspiringDemocritus1),
				context.getString(R.string.inspiringDeshimaru1),
				context.getString(R.string.inspiringDiamandis1),
				context.getString(R.string.inspiringDickinson1),
				context.getString(R.string.inspiringDiderot1),
				context.getString(R.string.inspiringDisraeli1),
				context.getString(R.string.inspiringEdison1),
				context.getString(R.string.inspiringEinstein1),
				context.getString(R.string.inspiringEliot1),
				context.getString(R.string.inspiringElliot1),
				context.getString(R.string.inspiringErasmus1),
				context.getString(R.string.inspiringEsopo1),
				context.getString(R.string.inspiringFord1),
				context.getString(R.string.inspiringFrance1),
				context.getString(R.string.inspiringFrank1),
				context.getString(R.string.inspiringFrank2),
				context.getString(R.string.inspiringFranklin1),
				context.getString(R.string.inspiringFrost1),
				context.getString(R.string.inspiringFuller1),
				context.getString(R.string.inspiringGandhi1),
				context.getString(R.string.inspiringGide1),
				context.getString(R.string.inspiringGoethe1),
				context.getString(R.string.inspiringGoethe2),
				context.getString(R.string.inspiringGoethe3),
				context.getString(R.string.inspiringGrey1),
				context.getString(R.string.inspiringHare1),
				context.getString(R.string.inspiringHayes1),
				context.getString(R.string.inspiringHeschel1),
				context.getString(R.string.inspiringHill1),
				context.getString(R.string.inspiringJames1),
				context.getString(R.string.inspiringJefferson1),
				context.getString(R.string.inspiringJesus1),
				context.getString(R.string.inspiringJobs1),
				context.getString(R.string.inspiringJohnson1),
				context.getString(R.string.inspiringJordan1),
				context.getString(R.string.inspiringKeller1),
				context.getString(R.string.inspiringKelly1),
				context.getString(R.string.inspiringKennedy1),
				context.getString(R.string.inspiringKeynes1),
				context.getString(R.string.inspiringKincade1),
				context.getString(R.string.inspiringKing1),
				context.getString(R.string.inspiringKrutch1),
				context.getString(R.string.inspiringLarcom1),
				context.getString(R.string.inspiringLec1),
				context.getString(R.string.inspiringLucado1),
				context.getString(R.string.inspiringLuther1),
				context.getString(R.string.inspiringMerton1),
				context.getString(R.string.inspiringMeyer1),
				context.getString(R.string.inspiringMorgan1),
				context.getString(R.string.inspiringMorley1),
				context.getString(R.string.inspiringNietzsche1),
				context.getString(R.string.inspiringOnassis1),
				context.getString(R.string.inspiringPaine1),
				context.getString(R.string.inspiringPascal1),
				context.getString(R.string.inspiringPeale1),
				context.getString(R.string.inspiringPele1),
				context.getString(R.string.inspiringPlato1),
				context.getString(R.string.inspiringPlutarch1),
				context.getString(R.string.inspiringPopeJohnXXIII1),
				context.getString(R.string.inspiringPrior1),
				context.getString(R.string.inspiringProudhon1),
				context.getString(R.string.inspiringQuarles1),
				context.getString(R.string.inspiringQubein1),
				context.getString(R.string.inspiringReagan1),
				context.getString(R.string.inspiringRobbins1),
				context.getString(R.string.inspiringRobbins2),
				context.getString(R.string.inspiringRobbins3),
				context.getString(R.string.inspiringRohn1),
				context.getString(R.string.inspiringRoosevelt1),
				context.getString(R.string.inspiringRoosevelt2),
				context.getString(R.string.inspiringRoosevelt3),
				context.getString(R.string.inspiringRoosevelt4),
				context.getString(R.string.inspiringRudolph1),
				context.getString(R.string.inspiringRuth1),
				context.getString(R.string.inspiringSFrancesco1),
				context.getString(R.string.inspiringSchiller1),
				context.getString(R.string.inspiringSchuller1),
				context.getString(R.string.inspiringSchumann1),
				context.getString(R.string.inspiringSedaris1),
				context.getString(R.string.inspiringSeneca1),
				context.getString(R.string.inspiringShaw1),
				context.getString(R.string.inspiringShing1),
				context.getString(R.string.inspiringShue1),
				context.getString(R.string.inspiringSivananda1),
				context.getString(R.string.inspiringSpecter1),
				context.getString(R.string.inspiringStevenson1),
				context.getString(R.string.inspiringSuyin1),
				context.getString(R.string.inspiringTagore1),
				context.getString(R.string.inspiringTertulliano1),
				context.getString(R.string.inspiringTwain1),
				context.getString(R.string.inspiringTzu1),
				context.getString(R.string.inspiringUeshiba1),
				context.getString(R.string.inspiringUltang1),
				context.getString(R.string.inspiringVinci1),
				context.getString(R.string.inspiringWeinstein1),
				context.getString(R.string.inspiringWharton1),
				context.getString(R.string.inspiringYen1),
				context.getString(R.string.inspiringYoung1),
				context.getString(R.string.inspiringYoungblood1), };

		return inspiringStrings;
	}

	/**
	 * @return the array with the strings for the category
	 */
	private String[] getLifeCategoryStrings()
	{
		String[] lifeStrings = { context.getString(R.string.lifeAdams1),
				context.getString(R.string.lifeAdler1),
				context.getString(R.string.lifeAleichem1),
				context.getString(R.string.lifeAllen1),
				context.getString(R.string.lifeAllen2),
				context.getString(R.string.lifeAngelou1),
				context.getString(R.string.lifeAsimov1),
				context.getString(R.string.lifeAurelius1),
				context.getString(R.string.lifeAurelius2),
				context.getString(R.string.lifeBach1),
				context.getString(R.string.lifeBacon1),
				context.getString(R.string.lifeBarrie1),
				context.getString(R.string.lifeBattaglia1),
				context.getString(R.string.lifeBeauvoir1),
				context.getString(R.string.lifeBerlin1),
				context.getString(R.string.lifeBillings1),
				context.getString(R.string.lifeBronte1),
				context.getString(R.string.lifeBuddha1),
				context.getString(R.string.lifeBuddha2),
				context.getString(R.string.lifeButler1),
				context.getString(R.string.lifeButler2),
				context.getString(R.string.lifeButterworth1),
				context.getString(R.string.lifeButturini1),
				context.getString(R.string.lifeByrne1),
				context.getString(R.string.lifeCampbell1),
				context.getString(R.string.lifeCampbell2),
				context.getString(R.string.lifeCampbell3),
				context.getString(R.string.lifeChaplin1),
				context.getString(R.string.lifeCheGuevara1),
				context.getString(R.string.lifeChekhov1),
				context.getString(R.string.lifeChesterton1),
				context.getString(R.string.lifeCicero1),
				context.getString(R.string.lifeColette1),
				context.getString(R.string.lifeConfucius1),
				context.getString(R.string.lifeCovey1),
				context.getString(R.string.lifeCurie1),
				context.getString(R.string.lifeDarwin1),
				context.getString(R.string.lifeDeBono1),
				context.getString(R.string.lifeDickinson1),
				context.getString(R.string.lifeDyer1),
				context.getString(R.string.lifeDyer2),
				context.getString(R.string.lifeEinstein1),
				context.getString(R.string.lifeEllis1),
				context.getString(R.string.lifeEmerson1),
				context.getString(R.string.lifeEmerson2),
				context.getString(R.string.lifeEmerson3),
				context.getString(R.string.lifeEmerson4),
				context.getString(R.string.lifeEmerson5),
				context.getString(R.string.lifeEminem1),
				context.getString(R.string.lifeEuripides1),
				context.getString(R.string.lifeEuripides2),
				context.getString(R.string.lifeEvans1),
				context.getString(R.string.lifeFeather1),
				context.getString(R.string.lifeForster1),
				context.getString(R.string.lifeForster2),
				context.getString(R.string.lifeFrost1),
				context.getString(R.string.lifeFrost2),
				context.getString(R.string.lifeGandhi1),
				context.getString(R.string.lifeGandhi2),
				context.getString(R.string.lifeGasset1),
				context.getString(R.string.lifeGoethe1),
				context.getString(R.string.lifeGoethe2),
				context.getString(R.string.lifeGoethe3),
				context.getString(R.string.lifeGreen1),
				context.getString(R.string.lifeHagen1),
				context.getString(R.string.lifeHamilton1),
				context.getString(R.string.lifeHarris1),
				context.getString(R.string.lifeHazlitt1),
				context.getString(R.string.lifeHendrix1),
				context.getString(R.string.lifeHepburn1),
				context.getString(R.string.lifeHerbert1),
				context.getString(R.string.lifeHill1),
				context.getString(R.string.lifeHoltz1),
				context.getString(R.string.lifeHopkins1),
				context.getString(R.string.lifeHorace1),
				context.getString(R.string.lifeHorace2),
				context.getString(R.string.lifeHorney1),
				context.getString(R.string.lifeHoward1),
				context.getString(R.string.lifeHubbard1),
				context.getString(R.string.lifeIngelow1),
				context.getString(R.string.lifeJames1),
				context.getString(R.string.lifeJames2),
				context.getString(R.string.lifeJames3),
				context.getString(R.string.lifeJames4),
				context.getString(R.string.lifeJames5),
				context.getString(R.string.lifeJames6),
				context.getString(R.string.lifeJung1),
				context.getString(R.string.lifeKeller1),
				context.getString(R.string.lifeKennedy1),
				context.getString(R.string.lifeKennedy2),
				context.getString(R.string.lifeKennedy3),
				context.getString(R.string.lifeKent1),
				context.getString(R.string.lifeKierkegaard1),
				context.getString(R.string.lifeKierkegaard2),
				context.getString(R.string.lifeLangtry1),
				context.getString(R.string.lifeLebowitz1),
				context.getString(R.string.lifeLee1),
				context.getString(R.string.lifeLeigh1),
				context.getString(R.string.lifeLennon1),
				context.getString(R.string.lifeLeopardi1),
				context.getString(R.string.lifeLindbergh1),
				context.getString(R.string.lifeLongworth1),
				context.getString(R.string.lifeMarley1),
				context.getString(R.string.lifeMcLaughlin1),
				context.getString(R.string.lifeMiller1),
				context.getString(R.string.lifeMiller2),
				context.getString(R.string.lifeMizner1),
				context.getString(R.string.lifeMorris1),
				context.getString(R.string.lifeMoses1),
				context.getString(R.string.lifeMoses2),
				context.getString(R.string.lifeNewman1),
				context.getString(R.string.lifeNietzsche1),
				context.getString(R.string.lifeNin1),
				context.getString(R.string.lifeOsler1),
				context.getString(R.string.lifeOsteen1),
				context.getString(R.string.lifePlato1),
				context.getString(R.string.lifeRobinson1),
				context.getString(R.string.lifeRogers1),
				context.getString(R.string.lifeRoosevelt1),
				context.getString(R.string.lifeRubinstein1),
				context.getString(R.string.lifeRuskin1),
				context.getString(R.string.lifeRussell1),
				context.getString(R.string.lifeSandburg1),
				context.getString(R.string.lifeSandburg2),
				context.getString(R.string.lifeSartre1),
				context.getString(R.string.lifeSchulz1),
				context.getString(R.string.lifeSchweitzer1),
				context.getString(R.string.lifeSeneca1),
				context.getString(R.string.lifeSeneca2),
				context.getString(R.string.lifeSeneca3),
				context.getString(R.string.lifeShaw1),
				context.getString(R.string.lifeShaw2),
				context.getString(R.string.lifeSinger1),
				context.getString(R.string.lifeSmiles1),
				context.getString(R.string.lifeSocrates1),
				context.getString(R.string.lifeSocrates2),
				context.getString(R.string.lifeStein1),
				context.getString(R.string.lifeSterne1),
				context.getString(R.string.lifeStevenson1),
				context.getString(R.string.lifeStevenson2),
				context.getString(R.string.lifeStevenson3),
				context.getString(R.string.lifeSwift1),
				context.getString(R.string.lifeTagore1),
				context.getString(R.string.lifeTagore2),
				context.getString(R.string.lifeThoreau1),
				context.getString(R.string.lifeThoreau2),
				context.getString(R.string.lifeTiburzi1),
				context.getString(R.string.lifeTolstoy1),
				context.getString(R.string.lifeTrump1),
				context.getString(R.string.lifeTwain1),
				context.getString(R.string.lifeTwain2),
				context.getString(R.string.lifeTwain3),
				context.getString(R.string.lifeVinci1),
				context.getString(R.string.lifeVinci2),
				context.getString(R.string.lifeWallace1),
				context.getString(R.string.lifeWaller1),
				context.getString(R.string.lifeWest1),
				context.getString(R.string.lifeWilde1),
				context.getString(R.string.lifeWilde2),
				context.getString(R.string.lifeWilde3),
				context.getString(R.string.lifeWilde4),
				context.getString(R.string.lifeWright1),
				context.getString(R.string.lifecummings1), };

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
