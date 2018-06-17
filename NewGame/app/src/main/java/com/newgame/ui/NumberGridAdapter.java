package com.newgame.ui;

import java.util.LinkedHashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.newgame.R;

public class NumberGridAdapter  extends BaseAdapter {
    private Context context;
    private int selectedPos;
    private int backselectedPos = 0;
    private String[] options;
    public String selectedGame = "";
    
    private LinkedHashMap<String, String> setcountAmount;
    public NumberGridAdapter(Context context, LinkedHashMap<String, String> setcountAmount) {
		this.context = context;
		this.setcountAmount=setcountAmount;
        selectedPos = -1;
	}

    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder = convertView == null ? new ViewHolder() : (ViewHolder) convertView.getTag();

		if (convertView == null) {
            convertView = inflater.inflate(R.layout.number_option_layout, null);
            holder.genderImg = (ImageView) convertView.findViewById(R.id.gender_image);
            holder.checkedImg = (ImageView) convertView.findViewById(R.id.checked_image);
            holder.rightCrossImg = (ImageView) convertView.findViewById(R.id.cross_right_image);
            holder.leftCrossImg = (ImageView) convertView.findViewById(R.id.cross_image);
            holder.ageTxt = (TextView) convertView.findViewById(R.id.age_text);
            holder.countTxt = (TextView) convertView.findViewById(R.id.count_text);
            convertView.setTag(holder);
        }

        holder.checkedImg.setVisibility(position == selectedPos ? View.VISIBLE: View.GONE);
        if (backselectedPos == 0) {
        	holder.genderImg.setImageResource(R.drawable.num_round_image);
        	holder.ageTxt.setText((CharSequence) getItem(position));
        	holder.leftCrossImg.setVisibility(View.GONE);
        	holder.rightCrossImg.setVisibility(View.VISIBLE);
		}else if(backselectedPos == 1){
        	holder.genderImg.setImageResource(R.drawable.num_round_green_image);
        	 holder.ageTxt.setText((CharSequence) getItem(position));
        	 holder.leftCrossImg.setVisibility(View.GONE);
         	holder.rightCrossImg.setVisibility(View.GONE);
		}else{
        	holder.genderImg.setImageResource(R.drawable.num_round_red_image);
        	holder.ageTxt.setText((CharSequence) getItem(position));
        	holder.leftCrossImg.setVisibility(View.VISIBLE);
        	holder.rightCrossImg.setVisibility(View.GONE);
		}

        if (setcountAmount.containsKey(options[position])) {
        	holder.countTxt.setText(""+setcountAmount.get(options[position]));
		}else{
			holder.countTxt.setText("");
		}

        return convertView;
	}
 
	@Override
	public int getCount() {
		return options == null ? 0 : options.length;
	}
 
	@Override
	public Object getItem(int position) {
		return options[position];
	}
 
	@Override
	public long getItemId(int position) {
		return 0;
	}

    public int getCurrentSelectedPos() {
        return this.selectedPos;
    }

    public void updatePos(int selectedPos) {
        this.selectedPos = selectedPos;
        notifyDataSetChanged();
    }
    
    public void updateGridItems(String[] options){
    	this.options=options;
    	notifyDataSetChanged();
    }
    public void updateBackbg(int type){
    	this.backselectedPos=type;
    	notifyDataSetChanged();
    }

    
    public void selectedGame(String selectedGame){
    	this.selectedGame=selectedGame;
    }
    private static class ViewHolder {
        ImageView genderImg;
        ImageView checkedImg;
        ImageView rightCrossImg;
        ImageView leftCrossImg;
        TextView ageTxt;
        TextView countTxt;
    }
}