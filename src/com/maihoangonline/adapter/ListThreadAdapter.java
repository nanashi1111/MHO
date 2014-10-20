package com.maihoangonline.adapter;

import java.util.ArrayList;

import com.maihoangonline.mho.R;
import com.maihoangonline.models.MHOThread;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListThreadAdapter extends ArrayAdapter<MHOThread> {

	Context c;
	ArrayList<MHOThread> listThread;

	public ListThreadAdapter(Context context, int resource,
			ArrayList<MHOThread> objects) {
		super(context, resource, objects);
		c = context;
		listThread = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		LayoutInflater inf = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inf.inflate(R.layout.row_thread, null);
		TextView tvThreadName = (TextView)v.findViewById(R.id.thread_name);
		tvThreadName.setText(listThread.get(position).getName());
		return v;
	}

}
