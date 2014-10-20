package com.maihoangonline.adapter;

import java.util.ArrayList;

import com.maihoangonline.mho.R;
import com.maihoangonline.models.Option;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListOptionAdapter extends ArrayAdapter<Option> {

	Context c;
	ArrayList<Option> list;

	public ListOptionAdapter(Context context, int textViewResourceId,
			ArrayList<Option> objects) {
		super(context, textViewResourceId, objects);
		c = context;
		list = objects;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		LayoutInflater inf = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inf.inflate(R.layout.row_option, null);
		ImageView ivIcon = (ImageView) v.findViewById(R.id.option_icon);
		ivIcon.setImageResource(list.get(position).getIcon());
		TextView tvOption = (TextView) v.findViewById(R.id.option_text);
		tvOption.setText(list.get(position).getName());
		return v;
	}

}
