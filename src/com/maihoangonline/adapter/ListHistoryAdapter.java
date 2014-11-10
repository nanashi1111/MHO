package com.maihoangonline.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.maihoangonline.mho.R;
import com.maihoangonline.models.Transaction;

public class ListHistoryAdapter extends ArrayAdapter<Transaction>{
	
	Context c;
	ArrayList<Transaction> listTransaction;

	public ListHistoryAdapter(Context context, int resource,
			ArrayList<Transaction> objects) {
		super(context, resource, objects);
		this.c=context;
		this.listTransaction = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		LayoutInflater inf = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inf.inflate(R.layout.row_history_transaction, null);
		TextView tvDes = (TextView)v.findViewById(R.id.history_description);
		tvDes.setText(listTransaction.get(position).getDescription());
		TextView tvDate = (TextView)v.findViewById(R.id.history_date);
		tvDate.setText(listTransaction.get(position).getDate());
		return v;
	}

}
