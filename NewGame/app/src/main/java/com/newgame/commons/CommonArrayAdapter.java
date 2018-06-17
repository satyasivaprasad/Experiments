package com.newgame.commons;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by admin on 16-03-2015.
 */
public class CommonArrayAdapter extends ArrayAdapter<String>{
    private static  CommonArrayAdapter mCommonArrayAdapter = null;
    public  ArrayAdapter spinnerArrayAdapter = null;
//    public  CommonArrayAdapter(){
//    }
    
    public CommonArrayAdapter(Context ctx, String[] objects)
    {
        super(ctx, android.R.layout.simple_spinner_item, objects);
    }
    
//    public static CommonArrayAdapter getInstance(){
//        if(mCommonArrayAdapter == null ) {
//            return (mCommonArrayAdapter = new CommonArrayAdapter());
//        }
//        return mCommonArrayAdapter;
//    }
    public ArrayAdapter getGenericAdapper(Context mContext,String[] dataToPopulate){
        spinnerArrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, dataToPopulate);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return  spinnerArrayAdapter;
    }
    

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View view = super.getView(position, convertView, parent);

        //we know that simple_spinner_item has android.R.id.text1 TextView:         

        /* if(isDroidX) {*/
            TextView text = (TextView)view.findViewById(android.R.id.text1);
            text.setTextColor(Color.parseColor("#000000"));//choose your color :)  
            text.setBackgroundColor(Color.parseColor("#ffffff"));
            text.setPadding(15, 35, 15, 35);
        /*}*/

        return view;

    }
}
