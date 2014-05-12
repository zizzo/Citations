package com.citations;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends ArrayAdapter<Integer> {

	private List<Integer> items;
	private Context context;
	private LayoutInflater mLayoutInflater;
	private int layoutResourceId;
	
	private static final List<String> strList = Arrays.asList("Love", 
			                                                  "Politics",
			                                                  "Fun",
			                                                  "Inspiring",
			                                                  "Life");
	private static final List<Integer> iconList = Arrays.asList(
			R.drawable.love_cat,
			R.drawable.politics_cat,
			R.drawable.fun_cat,
			R.drawable.inspiring_cat,
			R.drawable.life_cat);
	
	private static final List<Integer> colorList = Arrays.asList(
			R.color.loveCategoryColor,
			R.color.politicsCategoryColor,
			R.color.funCategoryColor,
			R.color.inspiringCategoryColor,
			R.color.lifeCategoryColor);
	
	
    public MenuAdapter(Context ctx, int layoutResourceId, Integer[] data) {
    	super(ctx, layoutResourceId, data);
        this.items = new ArrayList<Integer>(Arrays.asList(data));
        this.context = ctx;
        this.mLayoutInflater = LayoutInflater.from(ctx);
        this.layoutResourceId = layoutResourceId;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
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
