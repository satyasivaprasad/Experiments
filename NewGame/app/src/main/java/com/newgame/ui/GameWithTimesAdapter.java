package com.newgame.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.newgame.R;
import com.newgame.commons.CommonUtils;
import com.newgame.models.GameJsonDataResult;

public class GameWithTimesAdapter extends BaseAdapter {

	private static List<GameJsonDataResult> searchArray;
	
	private LayoutInflater myInflater;
	public GameWithTimesAdapter(Context context, List<GameJsonDataResult> myArray){
		searchArray = myArray;
		myInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return searchArray.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return searchArray.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = myInflater.inflate(R.layout.game_time_item, null);
			holder.nametxt = (TextView)convertView.findViewById(R.id.game_name);
			holder.timetxt = (TextView)convertView.findViewById(R.id.game_time);
			holder.nametxt.setText(""+searchArray.get(position).getName());
			holder.timetxt.setText(""+ CommonUtils.getTimeFormattedStr(searchArray.get(position).getOpenTime()) + "	-	"
			+ CommonUtils.getTimeFormattedStr(searchArray.get(position).getOpenEndTime()) + "	-	" + 
					CommonUtils.getTimeFormattedStr(searchArray.get(position).getCloseEndTime()));
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		
		return convertView;
	}

	public class ViewHolder{
		
		TextView nametxt;
		TextView timetxt;
	
	}
}
