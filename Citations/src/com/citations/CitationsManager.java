/**
 * 
 */
package com.citations;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.net.Uri;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

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

    private final LinkedHashMap<String, String[]> categories = new LinkedHashMap<String, String[]>();
    private final HashMap<String, Integer> colormap = new HashMap<String, Integer>();

	private final String LOVE_CATEGORY = "loveCategory";
	private final String POLITICS_CATEGORY = "politicsCategory";
	private final String FUN_CATEGORY = "funCategory";
	private final String LIFE_CATEGORY = "lifeCategory";
	private final String INSPIRING_CATEGORY = "inspiringCategory";


	public CitationsManager(Context context)
	{
		this.context = context;
		categories.put(LOVE_CATEGORY, getLoveCategoryStrings());
		categories.put(POLITICS_CATEGORY, getPoliticsCategoryStrings());
		categories.put(FUN_CATEGORY, getFunCategoryStrings());
		categories.put(LIFE_CATEGORY, getLifeCategoryStrings());
		categories.put(INSPIRING_CATEGORY, getInspiringCategoryStrings());

        // Populating color map
		colormap.put(LIFE_CATEGORY,
				context.getResources().getColor(R.color.lifeCategoryColor));
		colormap.put(INSPIRING_CATEGORY,
				context.getResources().getColor(R.color.inspiringCategoryColor));
		colormap.put(LOVE_CATEGORY,
				context.getResources().getColor(R.color.loveCategoryColor));
		colormap.put(POLITICS_CATEGORY,
				context.getResources().getColor(R.color.politicsCategoryColor));
		colormap.put(FUN_CATEGORY,
				context.getResources().getColor(R.color.funCategoryColor));
	}

	// @SuppressWarnings("serial")

	/**
	 * @return a random string within the category in use
	 */
	public String getRandomStringInCategory(String categoryInUse)
	{
		String[] citationsOfCategory = categories.get(categoryInUse);
		int upperMaximum = citationsOfCategory.length - 1;
		int randS = StaticData.randInt(0, upperMaximum);

		return citationsOfCategory[randS];
	}

	/**
	 * @return a completely random string and category
	 */
	public String[] getRandomString()
	{
		int upperMaximum = categories.size() - 1;
		int randC = StaticData.randInt(0, upperMaximum);
		String[] citAndCat = new String[2];
		switch (randC)
		{
		case 0:
			citAndCat[0] = getRandomStringInCategory(LIFE_CATEGORY);
			citAndCat[1] = LIFE_CATEGORY;
			break;

		case 1:
			citAndCat[0] = getRandomStringInCategory(INSPIRING_CATEGORY);
			citAndCat[1] = INSPIRING_CATEGORY;
			break;

		case 2:
			citAndCat[0] = getRandomStringInCategory(LOVE_CATEGORY);
			citAndCat[1] = LOVE_CATEGORY;
			break;

		case 3:
			citAndCat[0] = getRandomStringInCategory(FUN_CATEGORY);
			citAndCat[1] = FUN_CATEGORY;
			break;

		case 4:
			citAndCat[0] = getRandomStringInCategory(POLITICS_CATEGORY);
			citAndCat[1] = POLITICS_CATEGORY;
			break;

		default:
			citAndCat[0] = getRandomStringInCategory(INSPIRING_CATEGORY);
			citAndCat[1] = INSPIRING_CATEGORY;
			break;
		}

		return citAndCat;
	}
	
	public void shareOnTwitter(Context context, String[] citation)
	{
		String tweetText = citation[0] + "\n" + citation[1];
		String tweetUrl = "https://twitter.com/intent/tweet?text=" + tweetText
				+ "&related=LuigiTiburzi,gabrielelanaro,Fra_Pochetti";
		Uri uri = Uri.parse(tweetUrl);
		Intent intentTweet = new Intent(Intent.ACTION_VIEW, uri);
		intentTweet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intentTweet);
	}

	public void shareOnFacebook(Context context, String[] citation,
			String categoryType)
	{
		Bitmap bitmap = drawBitmap(context, citation, categoryType);
		storeImage(bitmap, "imageToShare.png");
		String imagePath = Environment.getExternalStorageDirectory().toString()
				+ File.separator + "imageToShare.png";
		shareOnFb(imagePath, context);
	}

	public void shareGeneric(Context context, String[] citation)
	{
		String shareMessage = citation[0] + "\n" + citation[1];
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
		context.startActivity(Intent.createChooser(shareIntent, "Share...")
				.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

	public Bitmap drawBitmap(Context context, String[] citation,
			String categoryInUse)
	{

		Bitmap bitmap = Bitmap.createBitmap(500, 300, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);

		// Paint paint = new Paint();
		// paint.setTextAlign(Align.CENTER);
		// paint.setTextSize(18);

		c.drawColor(getCategoryInUseColor(categoryInUse));

		String bitmapText = citation[0] + "\n" + citation[1];

		TextPaint tp = new TextPaint();
		tp.setColor(Color.BLACK);
		tp.setTextSize(20);
		tp.setTextAlign(Align.CENTER);
		tp.setAntiAlias(true);
		StaticLayout sl = new StaticLayout(bitmapText, tp, 500,
				Layout.Alignment.ALIGN_NORMAL, 1, 0, false);

		// c.translate(250, 150);
		sl.draw(c);

		// c.drawText(bitmapText, 250, 150, paint);

		c.drawBitmap(bitmap, 0, 0, null);

		return bitmap;
	}

	public Integer getCategoryInUseColor(String categoryInUse)
	{
		return colormap.get(categoryInUse);
	}

	public void storeImage(Bitmap bitmap, String filename)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);

		File f = new File(Environment.getExternalStorageDirectory()
				+ File.separator + filename);
		try
		{
			f.createNewFile();
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());
			fo.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}// end storeImage

	public void shareOnFb(String imagePath, Context context)
	{
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("image/png");
		shareIntent.putExtra(Intent.EXTRA_STREAM,
				Uri.fromFile(new File(imagePath)));
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent,
				0);
		for (final ResolveInfo app : activityList)
		{
			if ((app.activityInfo.name).contains("facebook.katana"))
			{
				final ActivityInfo activity = app.activityInfo;
				final ComponentName name = new ComponentName(
						activity.applicationInfo.packageName, activity.name);
				shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				shareIntent.setComponent(name);
				context.startActivity(shareIntent);
				break;
			}
		}

	}// end shareOnFb

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
				context.getString(R.string.inspiringPauling1),
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
				context.getString(R.string.politicsActon1),
				context.getString(R.string.politicsAdams1),
				context.getString(R.string.politicsAdams2),
				context.getString(R.string.politicsAdams3),
				context.getString(R.string.politicsAesop1),
				context.getString(R.string.politicsAgnew1),
				context.getString(R.string.politicsAlighieri1),
				context.getString(R.string.politicsAlighieri2),
				context.getString(R.string.politicsAristotle1),
				context.getString(R.string.politicsAristotle2),
				context.getString(R.string.politicsArmour1),
				context.getString(R.string.politicsBaldwin1),
				context.getString(R.string.politicsBaruch1),
				context.getString(R.string.politicsBesant1),
				context.getString(R.string.politicsBierce1),
				context.getString(R.string.politicsBlack1),
				context.getString(R.string.politicsBonaparte1),
				context.getString(R.string.politicsBonaparte2),
				context.getString(R.string.politicsBoorstin1),
				context.getString(R.string.politicsBrandeis1),
				context.getString(R.string.politicsBrandeis2),
				context.getString(R.string.politicsBush1),
				context.getString(R.string.politicsByrne1),
				context.getString(R.string.politicsCamus1),
				context.getString(R.string.politicsCarter1),
				context.getString(R.string.politicsCastro1),
				context.getString(R.string.politicsCastro2),
				context.getString(R.string.politicsChanning1),
				context.getString(R.string.politicsChayefsky1),
				context.getString(R.string.politicsChomsky1),
				context.getString(R.string.politicsChurchill1),
				context.getString(R.string.politicsChurchill2),
				context.getString(R.string.politicsChurchill3),
				context.getString(R.string.politicsChurchill4),
				context.getString(R.string.politicsChurchill5),
				context.getString(R.string.politicsChurchill6),
				context.getString(R.string.politicsClarke1),
				context.getString(R.string.politicsCleveland1),
				context.getString(R.string.politicsClift1),
				context.getString(R.string.politicsColeridge1),
				context.getString(R.string.politicsDaley1),
				context.getString(R.string.politicsDante1),
				context.getString(R.string.politicsDarrow1),
				context.getString(R.string.politicsDavies1),
				context.getString(R.string.politicsDisraeli1),
				context.getString(R.string.politicsDurbin1),
				context.getString(R.string.politicsEban1),
				context.getString(R.string.politicsEinstein1),
				context.getString(R.string.politicsEisenhower1),
				context.getString(R.string.politicsFields1),
				context.getString(R.string.politicsFriedman1),
				context.getString(R.string.politicsFriedman2),
				context.getString(R.string.politicsFriedman3),
				context.getString(R.string.politicsFromm1),
				context.getString(R.string.politicsFrost1),
				context.getString(R.string.politicsFulbright1),
				context.getString(R.string.politicsFuller1),
				context.getString(R.string.politicsGalbraith1),
				context.getString(R.string.politicsGalbraith2),
				context.getString(R.string.politicsGallagher1),
				context.getString(R.string.politicsGandhi1),
				context.getString(R.string.politicsGandhi2),
				context.getString(R.string.politicsGardner1),
				context.getString(R.string.politicsGaulle1),
				context.getString(R.string.politicsGaulle2),
				context.getString(R.string.politicsGaulle3),
				context.getString(R.string.politicsGibran1),
				context.getString(R.string.politicsGilbert1),
				context.getString(R.string.politicsGingrich1),
				context.getString(R.string.politicsGladstone1),
				context.getString(R.string.politicsGlasow1),
				context.getString(R.string.politicsGlass1),
				context.getString(R.string.politicsGoldman1),
				context.getString(R.string.politicsGoldman2),
				context.getString(R.string.politicsGregory1),
				context.getString(R.string.politicsGross1),
				context.getString(R.string.politicsHamilton1),
				context.getString(R.string.politicsHannan1),
				context.getString(R.string.politicsHarding1),
				context.getString(R.string.politicsHart1),
				context.getString(R.string.politicsHesburgh1),
				context.getString(R.string.politicsHighet1),
				context.getString(R.string.politicsHoover1),
				context.getString(R.string.politicsHubbard1),
				context.getString(R.string.politicsJefferson1),
				context.getString(R.string.politicsJefferson2),
				context.getString(R.string.politicsJefferson3),
				context.getString(R.string.politicsJefferson4),
				context.getString(R.string.politicsJohnson1),
				context.getString(R.string.politicsJr1),
				context.getString(R.string.politicsKennedy1),
				context.getString(R.string.politicsKennedy2),
				context.getString(R.string.politicsKennedy3),
				context.getString(R.string.politicsKennedy4),
				context.getString(R.string.politicsKissinger1),
				context.getString(R.string.politicsLamartine1),
				context.getString(R.string.politicsLarson1),
				context.getString(R.string.politicsLavater1),
				context.getString(R.string.politicsLevant1),
				context.getString(R.string.politicsLiebling1),
				context.getString(R.string.politicsLuce1),
				context.getString(R.string.politicsLuce2),
				context.getString(R.string.politicsMachiavelli1),
				context.getString(R.string.politicsMarcos1),
				context.getString(R.string.politicsMarshall1),
				context.getString(R.string.politicsMarx1),
				context.getString(R.string.politicsMencken1),
				context.getString(R.string.politicsMill1),
				context.getString(R.string.politicsMiller1),
				context.getString(R.string.politicsMorley1),
				context.getString(R.string.politicsMorris1),
				context.getString(R.string.politicsNader1),
				context.getString(R.string.politicsOConnor1),
				context.getString(R.string.politicsONeill1),
				context.getString(R.string.politicsORourke1),
				context.getString(R.string.politicsObama1),
				context.getString(R.string.politicsOrben1),
				context.getString(R.string.politicsPerot1),
				context.getString(R.string.politicsPickering1),
				context.getString(R.string.politicsPike1),
				context.getString(R.string.politicsPlato1),
				context.getString(R.string.politicsPollack1),
				context.getString(R.string.politicsQuayle1),
				context.getString(R.string.politicsRandolph1),
				context.getString(R.string.politicsReagan1),
				context.getString(R.string.politicsReagan2),
				context.getString(R.string.politicsReagan3),
				context.getString(R.string.politicsReich1),
				context.getString(R.string.politicsRickover1),
				context.getString(R.string.politicsRogers1),
				context.getString(R.string.politicsRogers2),
				context.getString(R.string.politicsRogers3),
				context.getString(R.string.politicsRomano1),
				context.getString(R.string.politicsRoosevelt1),
				context.getString(R.string.politicsRoosevelt2),
				context.getString(R.string.politicsRoosevelt3),
				context.getString(R.string.politicsRoosevelt4),
				context.getString(R.string.politicsRosten1),
				context.getString(R.string.politicsSchroder1),
				context.getString(R.string.politicsSecondat1),
				context.getString(R.string.politicsShaw1),
				context.getString(R.string.politicsSherman1),
				context.getString(R.string.politicsSimon1),
				context.getString(R.string.politicsSpooner1),
				context.getString(R.string.politicsStein1),
				context.getString(R.string.politicsSteinem1),
				context.getString(R.string.politicsStinnett1),
				context.getString(R.string.politicsStone1),
				context.getString(R.string.politicsThatcher1),
				context.getString(R.string.politicsThatcher2),
				context.getString(R.string.politicsThomas1),
				context.getString(R.string.politicsThompson1),
				context.getString(R.string.politicsTocqueville1),
				context.getString(R.string.politicsTruman1),
				context.getString(R.string.politicsTruman2),
				context.getString(R.string.politicsTwain2),
				context.getString(R.string.politicsUdall1),
				context.getString(R.string.politicsVaughan1),
				context.getString(R.string.politicsVidal1),
				context.getString(R.string.politicsVidal2),
				context.getString(R.string.politicsWallace1),
				context.getString(R.string.politicsWhite1),
				context.getString(R.string.politicsWhite2),
				context.getString(R.string.politicsWill1),
				context.getString(R.string.politicsWill2),
				context.getString(R.string.politicsWilson1),
				context.getString(R.string.politicsWilson2),
				context.getString(R.string.politicsZedong1), };

		return politicsStrings;
	}

	/**
	 * @return the strings about love
	 */
	private String[] getLoveCategoryStrings()
	{
		String[] loveStrings = { context.getString(R.string.loveAndrews1),
				context.getString(R.string.loveAngelis1),
				context.getString(R.string.loveAniston1),
				context.getString(R.string.loveAquinas1),
				context.getString(R.string.loveAristotle1),
				context.getString(R.string.loveAssisi1),
				context.getString(R.string.loveAugustine1),
				context.getString(R.string.loveBach1),
				context.getString(R.string.loveBaldwin1),
				context.getString(R.string.loveBaldwin2),
				context.getString(R.string.loveBall1),
				context.getString(R.string.loveBalzac1),
				context.getString(R.string.loveBalzac2),
				context.getString(R.string.loveBarca1),
				context.getString(R.string.loveBarrymore1),
				context.getString(R.string.loveBergerac1),
				context.getString(R.string.loveBergman1),
				context.getString(R.string.loveBernhardt1),
				context.getString(R.string.loveBreton1),
				context.getString(R.string.loveBreton2),
				context.getString(R.string.loveBronte1),
				context.getString(R.string.loveBrothers1),
				context.getString(R.string.loveBruyere1),
				context.getString(R.string.loveLytton1),
				context.getString(R.string.loveBuscaglia1),
				context.getString(R.string.loveBuscaglia2),
				context.getString(R.string.loveBuscaglia3),
				context.getString(R.string.loveBuscaglia4),
				context.getString(R.string.loveByron1),
				context.getString(R.string.loveCampbell1),
				context.getString(R.string.loveCampbell2),
				context.getString(R.string.loveCarlyle1),
				context.getString(R.string.loveChesterton1),
				context.getString(R.string.loveChesterton2),
				context.getString(R.string.loveChrist1),
				context.getString(R.string.loveChrist2),
				context.getString(R.string.loveCoelho1),
				context.getString(R.string.loveCole1),
				context.getString(R.string.loveColton1),
				context.getString(R.string.loveDiana1),
				context.getString(R.string.loveDickens1),
				context.getString(R.string.loveEinstein1),
				context.getString(R.string.loveEinstein2),
				context.getString(R.string.loveEliot1),
				context.getString(R.string.loveEliot2),
				context.getString(R.string.loveEllington1),
				context.getString(R.string.loveEllis1),
				context.getString(R.string.loveEmerson1),
				context.getString(R.string.loveEuripides1),
				context.getString(R.string.loveEuripides2),
				context.getString(R.string.loveFoch1),
				context.getString(R.string.loveFosdick1),
				context.getString(R.string.loveFrancis1),
				context.getString(R.string.loveFranklin1),
				context.getString(R.string.loveFromm1),
				context.getString(R.string.loveFromm2),
				context.getString(R.string.loveFromm3),
				context.getString(R.string.loveFrost1),
				context.getString(R.string.loveGabor1),
				context.getString(R.string.loveGalsworthy1),
				context.getString(R.string.loveGandhi1),
				context.getString(R.string.loveGarland1),
				context.getString(R.string.loveGibran1),
				context.getString(R.string.loveGibran2),
				context.getString(R.string.loveGibran3),
				context.getString(R.string.loveGoethe1),
				context.getString(R.string.loveGoethe2),
				context.getString(R.string.loveGogh1),
				context.getString(R.string.loveHepburn1),
				context.getString(R.string.loveHepburn2),
				context.getString(R.string.loveHubbard1),
				context.getString(R.string.loveHugo1),
				context.getString(R.string.loveHugo2),
				context.getString(R.string.loveHugo3),
				context.getString(R.string.loveHurston1),
				context.getString(R.string.loveJavan1),
				context.getString(R.string.loveJones1),
				context.getString(R.string.loveJoubert1),
				context.getString(R.string.loveJr1),
				context.getString(R.string.loveJr2),
				context.getString(R.string.loveJr3),
				context.getString(R.string.loveKeats1),
				context.getString(R.string.loveKeller1),
				context.getString(R.string.loveKesey1),
				context.getString(R.string.loveLamartine1),
				context.getString(R.string.loveLennon1),
				context.getString(R.string.loveLennon2),
				context.getString(R.string.loveLewis1),
				context.getString(R.string.loveLongfellow1),
				context.getString(R.string.loveLover1),
				context.getString(R.string.loveMacLaine1),
				context.getString(R.string.loveMandino1),
				context.getString(R.string.loveMencken1),
				context.getString(R.string.loveMencken2),
				context.getString(R.string.loveMencken3),
				context.getString(R.string.loveMenninger1),
				context.getString(R.string.loveMiller1),
				context.getString(R.string.loveMonson1),
				context.getString(R.string.loveMoody1),
				context.getString(R.string.loveMuller1),
				context.getString(R.string.loveMurdoch1),
				context.getString(R.string.loveNathan1),
				context.getString(R.string.loveNietzsche1),
				context.getString(R.string.loveNietzsche2),
				context.getString(R.string.loveNin1),
				context.getString(R.string.loveOvid1),
				context.getString(R.string.loveParker1),
				context.getString(R.string.lovePascal1),
				context.getString(R.string.lovePicasso1),
				context.getString(R.string.lovePlato1),
				context.getString(R.string.lovePlato2),
				context.getString(R.string.lovePoe1),
				context.getString(R.string.lovePym1),
				context.getString(R.string.loveRabelais1),
				context.getString(R.string.loveRand1),
				context.getString(R.string.loveRilke1),
				context.getString(R.string.loveRobbins1),
				context.getString(R.string.loveRochefoucauld1),
				context.getString(R.string.loveRochefoucauld2),
				context.getString(R.string.loveRochefoucauld3),
				context.getString(R.string.loveRochefoucauld4),
				context.getString(R.string.loveRoosevelt1),
				context.getString(R.string.loveRowland1),
				context.getString(R.string.loveRudner1),
				context.getString(R.string.loveRussell1),
				context.getString(R.string.loveSagan1),
				context.getString(R.string.loveSatir1),
				context.getString(R.string.loveSegal1),
				context.getString(R.string.loveSeneca1),
				context.getString(R.string.loveSeneca2),
				context.getString(R.string.loveShakespeare1),
				context.getString(R.string.loveShakespeare2),
				context.getString(R.string.loveShaw1),
				context.getString(R.string.loveShaw2),
				context.getString(R.string.loveSmith1),
				context.getString(R.string.loveStendhal1),
				context.getString(R.string.loveStevenson1),
				context.getString(R.string.loveTagore1),
				context.getString(R.string.loveTennyson1),
				context.getString(R.string.loveTeresa1),
				context.getString(R.string.loveTeresa2),
				context.getString(R.string.loveTeresa3),
				context.getString(R.string.loveTeresa4),
				context.getString(R.string.loveThoreau1),
				context.getString(R.string.loveTillich1),
				context.getString(R.string.loveTzu1),
				context.getString(R.string.loveTzu2),
				context.getString(R.string.loveTzu3),
				context.getString(R.string.loveUnamuno1),
				context.getString(R.string.loveUstinov1),
				context.getString(R.string.loveValery1),
				context.getString(R.string.loveVoltaire1),
				context.getString(R.string.loveWilde1),
				context.getString(R.string.loveWilde2),
				context.getString(R.string.loveWilde3),
				context.getString(R.string.loveYoung1), };

		return loveStrings;
	}


	/**
	 * @return fun category strings
	 */
	private String[] getFunCategoryStrings()
	{
		String[] funCategory = { context.getString(R.string.funAdams1),
				context.getString(R.string.funAdams2),
				context.getString(R.string.funAllen1),
				context.getString(R.string.funAllen2),
				context.getString(R.string.funAllen3),
				context.getString(R.string.funAsimov1),
				context.getString(R.string.funAuden1),
				context.getString(R.string.funAugustine1),
				context.getString(R.string.funBall1),
				context.getString(R.string.funBankhead1),
				context.getString(R.string.funBarry1),
				context.getString(R.string.funBenchley1),
				context.getString(R.string.funBerle1),
				context.getString(R.string.funBerle2),
				context.getString(R.string.funBerra1),
				context.getString(R.string.funBerra2),
				context.getString(R.string.funBillings1),
				context.getString(R.string.funBombeck1),
				context.getString(R.string.funBombeck2),
				context.getString(R.string.funBombeck3),
				context.getString(R.string.funBombeck4),
				context.getString(R.string.funBrenner1),
				context.getString(R.string.funBrooks1),
				context.getString(R.string.funBuffett1),
				context.getString(R.string.funBunuel1),
				context.getString(R.string.funBurns1),
				context.getString(R.string.funCarlin1),
				context.getString(R.string.funCarlin2),
				context.getString(R.string.funCarlin3),
				context.getString(R.string.funCarlin4),
				context.getString(R.string.funCarrey1),
				context.getString(R.string.funClinton1),
				context.getString(R.string.funConnolly1),
				context.getString(R.string.funCoolidge1),
				context.getString(R.string.funCosby1),
				context.getString(R.string.funCosby2),
				context.getString(R.string.funCosby3),
				context.getString(R.string.funDangerfield1),
				context.getString(R.string.funDangerfield2),
				context.getString(R.string.funDangerfield3),
				context.getString(R.string.funDawkins1),
				context.getString(R.string.funDiller1),
				context.getString(R.string.funDiller2),
				context.getString(R.string.funDiller3),
				context.getString(R.string.funDisney1),
				context.getString(R.string.funEastwood1),
				context.getString(R.string.funEinstein1),
				context.getString(R.string.funFields1),
				context.getString(R.string.funFields2),
				context.getString(R.string.funFields3),
				context.getString(R.string.funFranklin1),
				context.getString(R.string.funFry1),
				context.getString(R.string.funGabor1),
				context.getString(R.string.funGuisewite1),
				context.getString(R.string.funHackett1),
				context.getString(R.string.funHedberg1),
				context.getString(R.string.funHedberg2),
				context.getString(R.string.funHedberg3),
				context.getString(R.string.funHedberg4),
				context.getString(R.string.funHedberg5),
				context.getString(R.string.funHepburn1),
				context.getString(R.string.funHerford1),
				context.getString(R.string.funHope1),
				context.getString(R.string.funHubbard1),
				context.getString(R.string.funHugo1),
				context.getString(R.string.funKissinger1),
				context.getString(R.string.funLamarr1),
				context.getString(R.string.funLevant1),
				context.getString(R.string.funLewis1),
				context.getString(R.string.funLewis2),
				context.getString(R.string.funMarcos1),
				context.getString(R.string.funMarquis1),
				context.getString(R.string.funMarquis2),
				context.getString(R.string.funMartin1),
				context.getString(R.string.funMartin2),
				context.getString(R.string.funMarx1),
				context.getString(R.string.funMarx2),
				context.getString(R.string.funMarx3),
				context.getString(R.string.funMarx4),
				context.getString(R.string.funMead1),
				context.getString(R.string.funMencken1),
				context.getString(R.string.funMencken2),
				context.getString(R.string.funMencken3),
				context.getString(R.string.funMilligan1),
				context.getString(R.string.funMilligan2),
				context.getString(R.string.funOrben1),
				context.getString(R.string.funORourke1),
				context.getString(R.string.funORourke2),
				context.getString(R.string.funORourke3),
				context.getString(R.string.funPeter1),
				context.getString(R.string.funPeter2),
				context.getString(R.string.funPeter3),
				context.getString(R.string.funPhilips1),
				context.getString(R.string.funReagan1),
				context.getString(R.string.funReagan2),
				context.getString(R.string.funReagan3),
				context.getString(R.string.funRivers1),
				context.getString(R.string.funRivers2),
				context.getString(R.string.funRivers3),
				context.getString(R.string.funRock1),
				context.getString(R.string.funRogers1),
				context.getString(R.string.funRogers2),
				context.getString(R.string.funRogers3),
				context.getString(R.string.funRogers4),
				context.getString(R.string.funRoth1),
				context.getString(R.string.funRudner1),
				context.getString(R.string.funRussell1),
				context.getString(R.string.funRussell2),
				context.getString(R.string.funSahl1),
				context.getString(R.string.funSandburg1),
				context.getString(R.string.funSchulz1),
				context.getString(R.string.funSchwarzenegger1),
				context.getString(R.string.funSchwarzenegger2),
				context.getString(R.string.funSeinfeld1),
				context.getString(R.string.funSeinfeld2),
				context.getString(R.string.funShaw1),
				context.getString(R.string.funShaw2),
				context.getString(R.string.funSkelton1),
				context.getString(R.string.funSowell1),
				context.getString(R.string.funStengel1),
				context.getString(R.string.funStone1),
				context.getString(R.string.funTomlin1),
				context.getString(R.string.funTomlin2),
				context.getString(R.string.funTurner1),
				context.getString(R.string.funTwain1),
				context.getString(R.string.funTwain2),
				context.getString(R.string.funTwain3),
				context.getString(R.string.funTwain4),
				context.getString(R.string.funVaughan1),
				context.getString(R.string.funVoltaire1),
				context.getString(R.string.funWagner1),
				context.getString(R.string.funWells1),
				context.getString(R.string.funWest1),
				context.getString(R.string.funWest2),
				context.getString(R.string.funWhite1),
				context.getString(R.string.funWhitman1),
				context.getString(R.string.funWilliams1),
				context.getString(R.string.funWilliams2),
				context.getString(R.string.funWright1),
				context.getString(R.string.funWright2),
				context.getString(R.string.funWright3),
				context.getString(R.string.funWright4),
				context.getString(R.string.funWright5),
				context.getString(R.string.funWright6),
				context.getString(R.string.funYoungman1), };

		return funCategory;
	}


}// end CitationsData
