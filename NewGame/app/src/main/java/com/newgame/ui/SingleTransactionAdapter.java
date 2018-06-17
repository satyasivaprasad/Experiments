package com.newgame.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.newgame.R;
import com.newgame.commons.AppKeys;
import com.newgame.commons.CommonUtils;

public class SingleTransactionAdapter extends CursorAdapter{
	
	private final LayoutInflater mInflater;
	private int gameType;
	private Context mContext;
    @SuppressLint("NewApi") public SingleTransactionAdapter(Context context, Cursor c, int gameType) {
		super(context,c,0);
		// TODO Auto-generated constructor stub
		mInflater=LayoutInflater.from(context);
		this.gameType=gameType;
		this.mContext=context;
	}
	
	@Override
	public void bindView(View view, Context context, final Cursor cursor) {
		
		TextView numberTxt = (TextView)view.findViewById(R.id.number);
		numberTxt.setText(""+cursor.getString(cursor.getColumnIndex(AppKeys.DB_NUMBER)));
		numberTxt.setTextColor(CommonUtils.giveColorCode(Integer.parseInt(cursor.getString(cursor.getColumnIndex("GameType"))), mContext));
		
		TextView amountTxt = (TextView)view.findViewById(R.id.amount);
		amountTxt.setText(""+cursor.getString(cursor.getColumnIndex(AppKeys.DB_Amount)));
		amountTxt.setTextColor(CommonUtils.giveColorCode(Integer.parseInt(cursor.getString(cursor.getColumnIndex("GameType"))), mContext));
		
		TextView timeTxt = (TextView)view.findViewById(R.id.time);
		String ss = cursor.getString(cursor.getColumnIndex(AppKeys.DB_Time));
		timeTxt.setText(""+ss);
		timeTxt.setTextColor(CommonUtils.giveColorCode(Integer.parseInt(cursor.getString(cursor.getColumnIndex("GameType"))), mContext));
		
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		final View view=mInflater.inflate(R.layout.record_hostory_page,parent,false); 
        return view;
	}
}