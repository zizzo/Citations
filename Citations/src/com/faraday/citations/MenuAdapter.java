package com.faraday.citations;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.faraday.citations.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class MenuAdapter extends ArrayAdapter<Integer>
{

	private final List<Integer> items;
	private static Context context;
	private final LayoutInflater mLayoutInflater;
	private final int layoutResourceId;

	// Matches the categories
	private final List<Integer> strList = Arrays.asList(R.string.menu_lovecategory,
			R.string.menu_inspiringcategory,
			R.string.menu_funcategory,
			R.string.menu_politicscategory, 
			R.string.menu_lifecategory);

	private static final List<Integer> iconList = Arrays.asList(
			R.drawable.love_cat,
			R.drawable.inspiring_cat,
			R.drawable.fun_cat,
			R.drawable.politics_cat,
			R.drawable.life_cat
		);

	public MenuAdapter(Context ctx, int layoutResourceId, Integer[] data)
	{
		super(ctx, layoutResourceId, data);
		items = new ArrayList<Integer>(Arrays.asList(data));
		context = ctx;
		mLayoutInflater = LayoutInflater.from(ctx);
		this.layoutResourceId = layoutResourceId;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mLayoutInflater.inflate(layoutResourceId, parent, false);
		}
		
		TextView textViewItem = (TextView) convertView.findViewById(R.id.nav_TextView);
		textViewItem.setText(strList.get(position));

		ImageView imViewItem = (ImageView) convertView.findViewById(R.id.nav_ImageView);
		imViewItem.setImageResource(iconList.get(position));

		// TODO Auto-generated method stub
		return convertView;
	}

}
