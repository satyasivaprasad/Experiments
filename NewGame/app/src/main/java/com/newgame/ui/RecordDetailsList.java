package com.newgame.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.newgame.R;
import com.newgame.commons.AppKeys;


public class RecordDetailsList extends CursorAdapter{
	
	private final LayoutInflater mInflater;
	private int gameType;
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = 2;
	
    public RecordDetailsList(Context context, Cursor c, int gameType) {
		super(context,c,0);
		// TODO Auto-generated constructor stub
		mInflater=LayoutInflater.from(context);
		this.gameType=gameType;
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
		TextView numberTxt = (TextView)view.findViewById(R.id.number);
		numberTxt.setText(""+cursor.getString(cursor.getColumnIndex(AppKeys.DB_NUMBER)));
		numberTxt.setTextColor(gameType);
		
		TextView amountTxt = (TextView)view.findViewById(R.id.amount);
		amountTxt.setText(""+cursor.getString(cursor.getColumnIndex(AppKeys.DB_Amount)));
		amountTxt.setTextColor(gameType);
		
		TextView timeTxt = (TextView)view.findViewById(R.id.time);
		String ss = cursor.getString(cursor.getColumnIndex(AppKeys.DB_Time));
		timeTxt.setText(""+ss);
		timeTxt.setTextColor(gameType);
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		final View view=mInflater.inflate(R.layout.record_hostory_page,parent,false); 
        return view;
	}
}
