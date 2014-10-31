package com.maihoangonline.adapter;

import java.util.ArrayList;
import com.maihoangonline.mho.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListCardTypeAdapter extends ArrayAdapter<String> {

	Context c;
	ArrayList<String> listCard;

	public ListCardTypeAdapter(Context context, int resource,
			ArrayList<String> objects) {
		super(context, resource, objects);
		c = context;
		listCard = objects;
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {
		View v;
		LayoutInflater inf = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inf.inflate(R.layout.row_card_type, null);
		TextView cardType = (TextView) v.findViewById(R.id.card);
		if (position == 0) {
			cardType.setText("Hãy chọn loại thẻ cào");
		} else {
			cardType.setText(listCard.get(position));
		}
		return v;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

}
