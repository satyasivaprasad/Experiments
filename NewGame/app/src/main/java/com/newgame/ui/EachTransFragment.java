package com.newgame.ui;

import java.util.LinkedHashMap;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.newgame.R;
import com.newgame.commons.AppKeys;
import com.newgame.database.DatabaseHelper;
import com.newgame.database.Queries;

@SuppressLint("ValidFragment")
public class EachTransFragment extends Fragment {

	private View rootView;
	private ListView recordDetailsList;
	private Cursor dataCur;
	private LinkedHashMap<String, String> amountDetails;
	private DatabaseHelper recordInsertDB = null;
    private SingleTransactionAdapter recordsAdapter;
//    private TextView totTxt;
    private String type;
    private int colorCode;
    public String transId = "", agentId = "", mainGameId = "";
    private SharedPreferences appPrefs;
    private TextView titleTxt;
    public EachTransFragment(String type, int colorCode, String agentId, String mainGameId) {
    	this.type=type;
    	this.colorCode=colorCode;
    	this.agentId = agentId;
    	this.mainGameId = mainGameId;
	}
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }
    
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.single_tras_layout, null);
		recordInsertDB = new DatabaseHelper(getActivity());
		recordDetailsList = (ListView)rootView.findViewById(R.id.statusList);
		
		appPrefs = getActivity().getSharedPreferences("game_prefs", getActivity().MODE_PRIVATE);
		transId = appPrefs.getString("transId", "");

		refreshData(type, colorCode);
		
		recordDetailsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				Cursor selectedCursor =(Cursor) recordsAdapter.getItem(position);
				designConfirmDialog(selectedCursor);
			}
		});
		return rootView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		recordInsertDB = new DatabaseHelper(getActivity());
	}
	

	public void   refreshData(String selectedGame,int colorCode){
			dataCur = recordInsertDB.getdata(Queries.getInstance().getRecordDetails("EachEntryDetails",transId,"pending", agentId, mainGameId));
			recordsAdapter = new SingleTransactionAdapter(getActivity(), dataCur,colorCode);
			recordDetailsList.setAdapter(recordsAdapter);
	}

	Dialog dialog;
	public void designConfirmDialog(final Cursor selectedNum){
	    dialog = new Dialog(getActivity());
	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    dialog.setContentView(R.layout.add_amount);
	    dialog.setCanceledOnTouchOutside(true);
	    
	    EditText numtXT = (EditText)dialog.findViewById(R.id.numberEdit);
	    numtXT.setFocusable(false);
//	    numtXT.s(false);
	    final EditText amountTxt = (EditText)dialog.findViewById(R.id.amountEdit);
	    numtXT.setText(""+selectedNum.getString(selectedNum.getColumnIndex(AppKeys.DB_NUMBER)));
	    amountTxt.setText(""+selectedNum.getString(selectedNum.getColumnIndex(AppKeys.DB_Amount)));
	    
	    dialog.findViewById(R.id.okBtn).setOnClickListener(new View.OnClickListener() {
	
	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	            dialog.dismiss();
	            recordInsertDB.updateSatus(Queries.getInstance().updateNumStatus(selectedNum.getString(selectedNum.getColumnIndex(AppKeys.DB_EntryId)), "", amountTxt.getText().toString()));
	            refreshData(type, colorCode);
	        }
	    });
	    
	    dialog.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
	
	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	            dialog.dismiss();
	        }
	    });
	    dialog.show();
	}
}
