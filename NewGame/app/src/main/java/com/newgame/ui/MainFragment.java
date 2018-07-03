package com.newgame.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newgame.R;
import com.newgame.app.MainInfoActivity;
import com.newgame.app.TransactionsActivity;
import com.newgame.commons.CommonArrayAdapter;
import com.newgame.commons.CommonUtils;
import com.newgame.commons.DateFormats;
import com.newgame.database.DatabaseHelper;
import com.newgame.database.Queries;
import com.newgame.models.GameJsonDataResult;
import com.newgame.models.GameTransactionsModel;
import com.newgame.models.SubGameStatus;
import com.newgame.network.ApplicationThread;
import com.newgame.network.Config;
import com.newgame.network.GamesLiveData;
import com.newgame.network.Log;
import com.newgame.uihelper.TwoWayAdapterView;
import com.newgame.uihelper.TwoWayGridView;
import com.newgame.validator.Field;
import com.newgame.validator.Form;
import com.newgame.validator.NotEmpty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class MainFragment extends Fragment {
    private static final String LOG_TAG = MainFragment.class.getName();
	private String[] numArray;
    private NumberGridAdapter numOneAdapter;
    private TwoWayGridView numberGridView;
    private RadioButton openRadio, pawnRadio,closeRadio, openPawnaRadio,  closePawnaRadio;
    private LinearLayout numLinear;
    private EditText numberEdit, amountEdit;
    private Button addBtn, bracketAddBtn;
    private DatabaseHelper recordInsertDB = null;
    private RecordDetailsList recordsAdapter;
    private Cursor dataCur;
    private LinkedHashMap<String, String> amountDetails;
    private TextView  titleHeader, totalAmount;
    public String transId = "";
    public static String prevtransId = "";
    private SharedPreferences appPrefs;
    private RelativeLayout add_amount_rel;
	private ProgressDialog progressDialog;
	protected String[] arrGames_status;
	private Bundle bndl;
	private GameJsonDataResult gtm;
    private ArrayList<GameTransactionsModel> arrTransdataList = null;
	private String game_id;
	private JSONArray jArray;
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	private String getendtime_str;
	private Calendar calNow = Calendar.getInstance();
    private Calendar calSet  ;
    private Spinner facilitySpinner;
	private static int alarm_count = 1;
    private String gameId;
    private List<GameJsonDataResult> gameTimingsList;
    private String[] arrGames;
    private RelativeLayout main_layout;
    private int gameIdInt = 0;
    private SubGameStatus subGameStatus;
    
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static MainFragment newInstance(int sectionNumber,String agentID, String agentName, String game_id, GameJsonDataResult gameObj, String[] arrGames) {
		MainFragment fragment = new MainFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		args.putString("AGENT_ID", agentID);
		args.putString("AGENT_NAME", agentName);
		args.putString("GAME_ID", game_id);
		args.putSerializable("GAMEOBJ",gameObj);
		args.putStringArray("gamesArr", arrGames);
		fragment.setArguments(args);
		return fragment;
	}

	public MainFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		recordInsertDB = new DatabaseHelper(getActivity());
		appPrefs = getActivity().getSharedPreferences("game_prefs", getActivity().MODE_PRIVATE);
		transId = appPrefs.getString("transId", "");
		final View rootView = inflater.inflate(R.layout.fragment_main_info,container, false);
        facilitySpinner = (Spinner)rootView.findViewById(R.id.placeOrderSpin);
        main_layout  = (RelativeLayout)rootView.findViewById(R.id.main_layout);
		Button veriClick = (Button)rootView.findViewById(R.id.btnContinue);
		Button transClick = (Button)rootView.findViewById(R.id.btnTransactions);
		addBtn = (Button)rootView.findViewById(R.id.okBtn);
		bracketAddBtn = (Button)rootView.findViewById(R.id.btnAddBracket);
		numLinear = (LinearLayout)rootView.findViewById(R.id.confirmLL);
		openRadio = (RadioButton)rootView.findViewById(R.id.open);
		pawnRadio = (RadioButton)rootView.findViewById(R.id.pawn);
		openPawnaRadio = (RadioButton)rootView.findViewById(R.id.openPawna);
		closePawnaRadio = (RadioButton)rootView.findViewById(R.id.closePawna);
		closeRadio = (RadioButton)rootView.findViewById(R.id.close);
//		openRadio.setChecked(true);
	    numberGridView = (TwoWayGridView)rootView.findViewById(R.id.numbers_1_grid);
	    bracketAddBtn.setVisibility(View.GONE);
	    numArray = getResources().getStringArray(R.array.num_1_array);
	    amountDetails = recordInsertDB.getOperations(Queries.getInstance().getSumDetails(CommonUtils.selectedGame, getArguments().getString("AGENT_ID"), game_id));
        numOneAdapter = new NumberGridAdapter(getActivity(),amountDetails);
        numOneAdapter.updateGridItems(numArray);
        numberGridView.setAdapter(numOneAdapter);
		CommonUtils.colorCode = getActivity().getResources().getColor(R.color.open);
		numLinear.setVisibility(View.VISIBLE);
        numberEdit = (EditText)rootView.findViewById(R.id.numberEdit);
        amountEdit = (EditText)rootView.findViewById(R.id.amountEdit);
        
      
        numberEdit.setEnabled(false);
        amountEdit.setEnabled(false);
        addBtn.setEnabled(false);
        
        add_amount_rel = (RelativeLayout)rootView.findViewById(R.id.add_amount_rel);
        totalAmount = (TextView) rootView.findViewById(R.id.totAmount);

        titleHeader = (TextView)rootView.findViewById(R.id.titleHeader);
        
        bndl = getArguments();
         gtm = (GameJsonDataResult) bndl.getSerializable("GAMEOBJ");
			arrGames = bndl.getStringArray("gamesArr");

         Log.i("", "Agent main : "+gtm.getBracketEndTime());
          game_id = getArguments()
  				.getString("GAME_ID");
    	if (transId.length() == 0) {
            transId = recordInsertDB.getMaxcp_event_compl_idFromTable("EachEntryDetails","TR","TransId");
            SharedPreferences.Editor editor = appPrefs.edit();
	          editor.putString("transId", transId);
	          editor.commit();
		}

        numberGridView.setOnItemClickListener(new TwoWayAdapterView.OnItemClickListener() {
            public void onItemClick(TwoWayAdapterView parent, View view, int position, long id) {
            	boolean subGameEnableStatus;
            	int selectesGame = Integer.parseInt(CommonUtils.selectedGame);
            	
            	switch (selectesGame) {
					case 1:
						subGameEnableStatus = subGameStatus.getOpenStatus().equalsIgnoreCase("disable") ? false : true;
						break;
					case 2:
						subGameEnableStatus = subGameStatus.getCloseStatus().equalsIgnoreCase("disable") ? false : true;
						break;
	
					case 3:
						subGameEnableStatus = subGameStatus.getBracketStatus().equalsIgnoreCase("disable") ? false : true;
						break;
	
					case 4:
						subGameEnableStatus = subGameStatus.getOPanaStatus().equalsIgnoreCase("disable") ? false : true;
						break;
	
					case 5:
						subGameEnableStatus = subGameStatus.getCpanaStatus().equalsIgnoreCase("disable") ? false : true;
						break;
						
					default:
						subGameEnableStatus = true;
						break;
				}
       		
            	if (subGameEnableStatus) {
            		 if (numOneAdapter.getCurrentSelectedPos() == -1 || numOneAdapter.getCurrentSelectedPos() != position) {
                     	numOneAdapter.updatePos(position);
                     } else {
                     	numOneAdapter.updatePos(-1);
                     }
                     int selectedPos = numOneAdapter.getCurrentSelectedPos();
                     if(pawnRadio.isChecked()){
                     	numberEdit.setText(""+String.valueOf(selectedPos));
                     	amountEdit.setFocusable(true);
                     	amountEdit.setCursorVisible(true);
                     	amountEdit.requestFocus();
                     	CommonUtils.showKeyboard(getActivity(), amountEdit);
                     }else{
						 String text = (selectedPos == 9) ? "0" : String.valueOf(selectedPos + 1);
						 numberEdit.setText(text);
						 amountEdit.setFocusable(true);
						 amountEdit.setCursorVisible(true);
						 amountEdit.requestFocus();
						 CommonUtils.showKeyboard(getActivity(), amountEdit);
                     }
                     
                     if (CommonUtils.selectedGame.equalsIgnoreCase(Config.BRACKET_ID)) {
     					designConfirmDialog(""+String.valueOf((selectedPos < 10) ? "0"+selectedPos : selectedPos));
     				}
				} else {
					Toast.makeText(getActivity(), "Selected game not available at this time", Toast.LENGTH_SHORT).show();
				}
             }
        });
        veriClick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				uploadData();
			}
			
		});
        
        openRadio.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateUI("OPEN");
			}
		});
		
		pawnRadio.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						updateUI("BRACKET");
					}
		});

		closeRadio.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateUI("CLOSE");
			}
		});
		
		openPawnaRadio.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateUI("O PAWNA");
			}
		});

		closePawnaRadio.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateUI("C PAWNA");
			}
		});
        
		
		bracketAddBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				designConfirmDialog("0");
			}
		});
		
		addBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (validateInputDetails(rootView)) {
					
					Log.v(LOG_TAG, "#### check..."+getProperTime(getendtime_str)+"....."+"...."+getProperTime(sdf.format(new Date())));
					
					if (getProperTime(sdf.format(new Date())) < getProperTime(getendtime_str)) {
						addRecordToDb(numberEdit.getText().toString(), amountEdit.getText().toString());
					} else {
						Toast.makeText(getActivity(), "Sorry This game is not avoilable at this Time", Toast.LENGTH_SHORT).show();
					}
//					try {
//						Log.i("", "Time Compare : "+sdf.parse(getendtime_str)+ "...."+getendtime_str);
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					if (getendtime_str.compareTo(sdf.format(new Date())) == 1) {
//						addRecordToDb(numberEdit.getText().toString(), amountEdit.getText().toString());
//					}else {
//						addRecordToDb(numberEdit.getText().toString(), amountEdit.getText().toString());
//						Toast.makeText(getActivity(), "Sorry This game is not avoilable at this Time", Toast.LENGTH_SHORT).show();
//					}
					
				}
				
			}
		});
	
		transClick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (CommonUtils.isNetworkAvailable(getActivity())) {
					
					startActivity(new Intent(getActivity(),TransactionsActivity.class));
					}else {
						CommonUtils.showToast("No internet", getActivity());
					}
				
			}
		});
			
		System.out.println("check game id "+game_id);
		if (null != arrGames) {
    		 facilitySpinner.setAdapter(new CommonArrayAdapter(getActivity(),arrGames));
		}
		if (game_id.equalsIgnoreCase("1")) {
			main_layout.setBackgroundColor(Color.parseColor("#FBE5CE"));
			numberGridView.setBackgroundColor(Color.parseColor("#FBE5CE"));
			facilitySpinner.setSelection(0);
		}else if (game_id.equalsIgnoreCase("2")) {
			main_layout.setBackgroundColor(Color.parseColor("#cefbe5"));
			numberGridView.setBackgroundColor(Color.parseColor("#cefbe5"));
			facilitySpinner.setSelection(1);
		}else if (game_id.equalsIgnoreCase("3")) {
			main_layout.setBackgroundColor(Color.parseColor("#E9D1DC"));
			numberGridView.setBackgroundColor(Color.parseColor("#E9D1DC"));
			facilitySpinner.setSelection(2);
		}else {
			main_layout.setBackgroundColor(Color.parseColor("#cee4fb"));
			numberGridView.setBackgroundColor(Color.parseColor("#cee4fb"));
			facilitySpinner.setSelection(3);
		}
		
		 facilitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					game_id = String.valueOf(GameSelectionFragment.gamesList.get(position).getGameID());
					gameIdInt = Integer.parseInt(game_id);

					if (game_id.equalsIgnoreCase("1")) {
						main_layout.setBackgroundColor(Color.parseColor("#FBE5CE"));
						numberGridView.setBackgroundColor(Color.parseColor("#FBE5CE"));
					}else if (game_id.equalsIgnoreCase("2")) {
						main_layout.setBackgroundColor(Color.parseColor("#cefbe5"));
						numberGridView.setBackgroundColor(Color.parseColor("#cefbe5"));
					}else if (game_id.equalsIgnoreCase("3")) {
						main_layout.setBackgroundColor(Color.parseColor("#E9D1DC"));
						numberGridView.setBackgroundColor(Color.parseColor("#E9D1DC"));
					}else {
						main_layout.setBackgroundColor(Color.parseColor("#cee4fb"));
						numberGridView.setBackgroundColor(Color.parseColor("#cee4fb"));
					}
					getGameTimingsDetails();
					getGameStatus();
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
			});
		 
		 refreshData();
		 getGameStatus();
		return rootView;
	}
	
	public void getGameTimingsDetails(){
		if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
        GamesLiveData.getGameTimingDetails(new ApplicationThread.OnComplete<String>(ApplicationThread.OnComplete.UI) {
	        
				@Override
	            public void run() {
	                if (!success || null == result) {
	                    return;
	                }
	                try {
	                	gameTimingsList  = (List<GameJsonDataResult>) result;
	                	gtm = gameTimingsList.get(gameIdInt);
	                }
	                catch (Exception e){
	                    e.printStackTrace();
	                    
	                }
	                finally{
	                	progressDialog.dismiss();
	                }

	            }
	        }, String.format(Config.live_url+Config.GAMETIMINGS_URL, game_id) );
	}
	
	public void uploadData() {
		if (CommonUtils.isNetworkAvailable(getActivity())) {
			
			if (uploadConfirmDataToServer(transId)) {
				recordInsertDB.updateSatus(Queries.getInstance().updateStatus(transId, "finished"));
				prevtransId = transId;
		        transId = recordInsertDB.getMaxcp_event_compl_idFromTable("EachEntryDetails","TR","TransId");

				  SharedPreferences.Editor editor = appPrefs.edit();
		          editor.putString("transId", transId);
		          editor.commit();
		          
				numOneAdapter.updatePos(-1);
//				CommonUtils.selectedGame = "0";
				refreshData();
				Toast.makeText(getActivity(), "Data added successfully", Toast.LENGTH_SHORT).show();
			}
			
			
			}else {
				CommonUtils.showToast("No internet", getActivity());
			}
	}
	
	public void getGameStatus(){
		if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
        
		 GamesLiveData.getGameDetails(new ApplicationThread.OnComplete<String>(ApplicationThread.OnComplete.UI) {
	        
				@Override
	            public void run() {
	                if (!success || null == result) {
	                	progressDialog.dismiss();
	                    return;
	                }
	                try {
	                	 Type collectionType = new TypeToken<Collection<SubGameStatus>>(){}.getType();
	                	 Gson googleJson = new Gson();
	                	 List<SubGameStatus> gamesList = googleJson.fromJson((String) result,collectionType);
	                
	                	 Log.i("", "SubGameclass msg "+gamesList.get(0));
	                	 subGameStatus = gamesList.get(0);
	                	 handleUiStatus(gamesList.get(0));
	                	 
					}
	                catch (Exception e){
	                    e.printStackTrace();
	                    
	                }finally{
	                	progressDialog.dismiss();
	                }

	            }
	        }, String.format(Config.live_url+Config.SubGamesConfig_URL, game_id));
	}
	
	private void handleUiStatus(SubGameStatus subGameStatus) {
		openRadio.setEnabled(subGameStatus.getOpenStatus().equalsIgnoreCase("disable") ? false : true);
		pawnRadio.setEnabled(subGameStatus.getBracketStatus().equalsIgnoreCase("disable") ? false : true);
		closeRadio.setEnabled(subGameStatus.getCloseStatus().equalsIgnoreCase("disable") ? false : true);
		openPawnaRadio.setEnabled(subGameStatus.getOPanaStatus().equalsIgnoreCase("disable") ? false : true);
		closePawnaRadio.setEnabled(subGameStatus.getCpanaStatus().equalsIgnoreCase("disable") ? false : true);
		
		if (openRadio.isEnabled() == true) {
			setAlarm(gtm.getOpenEndTime(), 1);
			numberEdit.setEnabled(true);
	        amountEdit.setEnabled(true);
	        addBtn.setEnabled(true);
			openRadio.setChecked(true);
			updateUI("OPEN");
		}else if (pawnRadio.isEnabled() == true) {
			setAlarm(gtm.getOpanaEndTime(), 2);
			numberEdit.setEnabled(true);
	        amountEdit.setEnabled(true);
	        addBtn.setEnabled(true);
			pawnRadio.setChecked(true);
			updateUI("BRACKET");
		}else if (closeRadio.isEnabled() == true) {
			setAlarm(gtm.getCloseEndTime(), 3);
			numberEdit.setEnabled(true);
	        amountEdit.setEnabled(true);
	        addBtn.setEnabled(true);
			closeRadio.setChecked(true);
			updateUI("CLOSE");
		}else if (openPawnaRadio.isEnabled() == true) {
			setAlarm(gtm.getOpanaEndTime(), 4);
			numberEdit.setEnabled(true);
	        amountEdit.setEnabled(true);
	        addBtn.setEnabled(true);
			openPawnaRadio.setChecked(true);
			updateUI("O PAWNA");
		}else if (closePawnaRadio.isEnabled() == true) {
			setAlarm(gtm.getCloseEndTime(), 5);
			numberEdit.setEnabled(true);
	        amountEdit.setEnabled(true);
	        addBtn.setEnabled(true);
			closePawnaRadio.setChecked(true);
			updateUI("C PAWNA");
		}
		
    }
	
	
	private void setAlarm(String time,int alarm_no){
		
		 calSet = (Calendar) calNow.clone();
			int in = Integer.parseInt(time.substring(0, 2));
			if (in != 0) {
				in = in-1;
			}else {
				in = 23;
			}
			Log.i("", "int msg  " + in);
			calSet.set(Calendar.HOUR_OF_DAY, in);
	        calSet.set(Calendar.MINUTE, 50);
	        calSet.set(Calendar.SECOND, Integer.parseInt(time.substring(6, 8)));

		  Intent intent = new Intent(getActivity().getBaseContext(), AlarmReceiver.class);
		  intent.putExtra("alarm_no", alarm_no);
		  PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getBaseContext(), alarm_no, intent, 0);
		  AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), pendingIntent);
	  
	 }
	
	private void updateUI(String clickedButtonName){
		if (clickedButtonName.equalsIgnoreCase("OPEN")) {
			// TODO Auto-generated method stub
			add_amount_rel.setVisibility(View.VISIBLE);
			CommonUtils.selectedGame = Config.OPEN_ID;
			CommonUtils.selectedBg = 0;
			bracketAddBtn.setVisibility(View.GONE);
			CommonUtils.colorCode = getActivity().getResources().getColor(R.color.open);
			numOneAdapter.updateBackbg(0);
			numOneAdapter.updateGridItems(numArray);
			numberGridView.setVisibility(View.VISIBLE);
			titleHeader.setText(""+openRadio.getText().toString());
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(1);
			numberEdit.setFilters(FilterArray);
			getendtime_str = gtm.getOpenEndTime();
			refreshData();
		
			
		}else if (clickedButtonName.equalsIgnoreCase("BRACKET")) {
			// TODO Auto-generated method stub
			add_amount_rel.setVisibility(View.GONE);
			CommonUtils.selectedGame = Config.BRACKET_ID;
			CommonUtils.colorCode = getActivity().getResources().getColor(R.color.bracket);
			numOneAdapter.updateBackbg(1);
			CommonUtils.selectedBg=1;
			bracketAddBtn.setVisibility(View.VISIBLE);
			numOneAdapter.updateGridItems(getTwoDigitarray());
			numLinear.setVisibility(View.VISIBLE);
			numberGridView.setVisibility(View.VISIBLE);
			titleHeader.setText(""+pawnRadio.getText().toString());
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(3);
			numberEdit.setFilters(FilterArray);
			 getendtime_str = gtm.getBracketEndTime();
			refreshData();
			
		}else if (clickedButtonName.equalsIgnoreCase("CLOSE")) {
			// TODO Auto-generated method stub
			add_amount_rel.setVisibility(View.VISIBLE);
			CommonUtils.selectedGame = Config.CLOSE_ID;
			numOneAdapter.updateBackbg(2);
			CommonUtils.selectedBg=2;
			CommonUtils.colorCode = getActivity().getResources().getColor(R.color.close);
//			numOneAdapter.updateGridItems(numArray);	
			numberGridView.setVisibility(View.VISIBLE);
			numLinear.setVisibility(View.VISIBLE);
			bracketAddBtn.setVisibility(View.GONE);
			titleHeader.setText(""+closeRadio.getText().toString());
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(1);
			numberEdit.setFilters(FilterArray);
			getendtime_str = gtm.getCloseEndTime();
			refreshData();
		
			
		}else if (clickedButtonName.equalsIgnoreCase("O PAWNA")) {
			// TODO Auto-generated method stub
			add_amount_rel.setVisibility(View.VISIBLE);
			CommonUtils.colorCode = getActivity().getResources().getColor(R.color.general);
			CommonUtils.selectedGame = Config.OPANA_ID;
			CommonUtils.selectedBg=0;
			bracketAddBtn.setVisibility(View.GONE);
			numberGridView.setVisibility(View.GONE);
			numLinear.setVisibility(View.VISIBLE);
			titleHeader.setText("Open Pawna");
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(3);
			numberEdit.setFilters(FilterArray);
			getendtime_str = gtm.getOpanaEndTime();
//			designTypeDialog("Open Pawna");
			refreshData();
			
		} else if (clickedButtonName.equalsIgnoreCase("C PAWNA")) {
			add_amount_rel.setVisibility(View.VISIBLE);
			CommonUtils.colorCode = getActivity().getResources().getColor(R.color.general);
			CommonUtils.selectedGame = Config.CPANA_ID;
			CommonUtils.selectedBg=0;
			numberGridView.setVisibility(View.GONE);
			numLinear.setVisibility(View.VISIBLE);
			titleHeader.setText("Close Pawna");
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(3);
			numberEdit.setFilters(FilterArray);
			getendtime_str = gtm.getCpanaEndTime();
			refreshData();
			bracketAddBtn.setVisibility(View.GONE);

		}
		
	}
	
	 public boolean validateInputDetails(View flipper){
	        Form mForm = new Form(getActivity());
	        mForm.addField(Field.using((EditText) flipper.findViewById(R.id.numberEdit)).validate(NotEmpty.build(getActivity())));
	        mForm.addField(Field.using((EditText) flipper.findViewById(R.id.amountEdit)).validate(NotEmpty.build(getActivity())));
	        return (mForm.isValid()) ? true : false;
	    }

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainInfoActivity) activity).onSectionAttached(getArguments()
				.getInt(ARG_SECTION_NUMBER));
	}
	
	
//	public void handleVisibilities(int visibility){
//		numLinear.setVisibility(visibility);
//		
//	}
	
	private boolean uploadConfirmDataToServer(String transID) {
		arrTransdataList = new ArrayList<GameTransactionsModel>();
		arrTransdataList = recordInsertDB.getMainTransactionData(Queries.getInstance().getRecordDetailsBasedonTransID(transID));
		
		if (!arrTransdataList.isEmpty()) {
			 createGroupInServer(getActivity(),arrTransdataList);
			 send_conform(arrTransdataList);
		} else {
            Toast.makeText(getActivity(),"No records found to save",Toast.LENGTH_LONG).show();
            return false;
		}
	   return true;
	    //Line
	}
	
	private void send_conform(ArrayList<GameTransactionsModel> list){
		
		GamesLiveData.uploadData(jArray, new ApplicationThread.OnComplete<String>(ApplicationThread.OnComplete.UI) {
          
			@Override
            public void run() {
				Log.i("Response MEssage jsonData...", "Response MEssage jsonData...First "+result.toString());
                if (!success || null == result) {
                	 progressDialog.dismiss();
                     progressDialog = null;
                    Toast.makeText(getActivity(),"Error while storing data",Toast.LENGTH_LONG).show();
                } else {
                	 try {
                     	  Log.i("Response MEssage jsonData...", result.toString());
                          progressDialog.dismiss();
                          progressDialog = null;
                          
                          recordInsertDB.updateSatus(Queries.getInstance().updateStatus(transId, "finished"));

          				  prevtransId = transId;
          		          transId = recordInsertDB.getMaxcp_event_compl_idFromTable("EachEntryDetails","TR","TransId");
          
          				  SharedPreferences.Editor editor = appPrefs.edit();
          		          editor.putString("transId", transId);
          		          editor.commit();
          		         
          		          refreshData();
          				  Toast.makeText(getActivity(), "Data added successfully", Toast.LENGTH_SHORT).show();
          				} catch (Exception e) {
                         e.printStackTrace();
                         progressDialog.dismiss();
                         progressDialog = null;
                     }
                }
               
            }
        },Config.live_url+Config.SendBulkData_URL);
	}
	
	public JSONArray createGroupInServer(Activity activity,  ArrayList<GameTransactionsModel> groups) {
		  TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		    
		    String numIMEI = telephonyManager.getDeviceId();
		Log.v(LOG_TAG, "CHECK..List"+groups.size());
		JSONObject jResult = new JSONObject();
		 
		  jArray = new JSONArray();
	    try {

			    for (int i = 0; i < groups.size(); i++) {
			    	
			    	Log.v(LOG_TAG, "CHECK..Return"+groups.get(i).toString());
			    	JSONObject jGroup = new JSONObject();
			        jGroup.put("AgentID", groups.get(i).getUserId());
			        jGroup.put("GameID", game_id);
			        jGroup.put("GameTypeID", groups.get(i).getGameType());
			        jGroup.put("Number", groups.get(i).getNumber());
			        jGroup.put("Amount", groups.get(i).getTotalAmount());
			        jGroup.put("Createdtime", groups.get(i).getCreated_date());
			        jGroup.put("UpdatedTime", groups.get(i).getUpdated_Date());
//			        jGroup.put("TransactionID", groups.get(i).getTransactionId());
			        jGroup.put("Amount", groups.get(i).getEachAmount());
			        jGroup.put("TabID", numIMEI);
			        jArray.put(jGroup);
			        jGroup = null;
			    }

			    jResult.put("", jArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Log.v(LOG_TAG, "CHECK..Return"+jArray.toString());
	    return jArray;
	}
	
	public void  addRecordToDb(String number,  String amount){
		
          
			String dateTime = DateFormats.getCurrentDatetimePrefix();
			
			String dates[] = null;
			if (dateTime.length() > 0) {
				dates = dateTime.split(" ");
			}
		
			String timeStr = dates[1]+" "+dates[2];
		    HashMap eachEntryDataMap = new HashMap();
	        final List<HashMap> recordList = new ArrayList<HashMap>();
	        eachEntryDataMap.put("GameType",CommonUtils.selectedGame);
	        eachEntryDataMap.put("TransId",transId);
	        eachEntryDataMap.put("UserId",""+getArguments().getString("AGENT_ID"));
	        eachEntryDataMap.put("UserName",getArguments().getString("AGENT_NAME"));
	        eachEntryDataMap.put("GameTypeId",titleHeader.getText().toString());
	        eachEntryDataMap.put("Number",number);
	        eachEntryDataMap.put("Amount",amount);
	        eachEntryDataMap.put("Status","pending");
	        eachEntryDataMap.put("Time",timeStr);
	        eachEntryDataMap.put("Date",""+dates[0]);
	        eachEntryDataMap.put("Created_date",""+dateTime);
	        eachEntryDataMap.put("Updated_Date",""+dateTime);
	        eachEntryDataMap.put("MainGameId",""+game_id);
	        eachEntryDataMap.put("MainGameName",""+gtm.getName());
	        recordList.add(eachEntryDataMap);
	       
	        ApplicationThread.dbPost(this.getClass().getName(), "insert..EachEntryDetails..", new Runnable() {
	            @Override
	            public void run() {
	                // TODO Auto-generated method stub
	            	recordInsertDB.insertData("EachEntryDetails", recordList, getActivity());
	            	
	                ApplicationThread.uiPost(getActivity().getClass().getName(), "show/hide progress", new Runnable() {
	                    @Override
	                    public void run() {
	                    	numberEdit.setText("");
	                    	amountEdit.setText("");
	                    	numberEdit.setFocusable(true);
	                    	refreshData();
	                    	recordsAdapter.notifyDataSetChanged();
	                    }
	                });
	            }
	        });
	}

	public void   refreshData(){
		 Intent uiUpdateIntent = new Intent("update");
         LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(uiUpdateIntent);
		
         totalAmount.setTextColor(CommonUtils.colorCode);
		    String totalValue = recordInsertDB.getCountValue(Queries.getInstance().getTotalSumByGameType(CommonUtils.selectedGame, getArguments().getString("AGENT_ID"), game_id));
//		    Toast.makeText(getActivity(), "totalValue.."+totalValue, Toast.LENGTH_SHORT).show();
		    if (null != totalValue && totalValue.length() > 0) {
		    	totalAmount.setVisibility(View.VISIBLE);
		    	totalAmount.setText(""+totalValue);
			}else{
				totalAmount.setText("0");
			}
		    
         if (!CommonUtils.selectedGame.equalsIgnoreCase(Config.BRACKET_ID)) {
			
			dataCur = recordInsertDB.getdata(Queries.getInstance().getRecordDetails("EachEntryDetails",transId,"pending", getArguments().getString("AGENT_ID"), game_id));
//			if (null != dataCur && dataCur.getCount()>0) {
				recordsAdapter = new RecordDetailsList(getActivity(), dataCur,CommonUtils.colorCode);
//			}
			
				
			amountDetails.clear();
			numArray = getResources().getStringArray(R.array.num_1_array);
			amountDetails = recordInsertDB.getOperations(Queries.getInstance().getSumDetails(CommonUtils.selectedGame, getArguments().getString("AGENT_ID"), game_id));
			numOneAdapter = new NumberGridAdapter(getActivity(),amountDetails);
			numOneAdapter.updateGridItems(numArray);
			numberGridView.setAdapter(numOneAdapter);
			numOneAdapter.updateBackbg(CommonUtils.selectedBg);
		}else {
			dataCur = recordInsertDB.getdata(Queries.getInstance().getRecordDetails("EachEntryDetails",transId,"pending", 
					getArguments().getString("AGENT_ID"), game_id));
//			if (null != dataCur && dataCur.getCount()>0) {
				recordsAdapter = new RecordDetailsList(getActivity(), dataCur,CommonUtils.colorCode);
//			}

			amountDetails.clear();
			numArray = getResources().getStringArray(R.array.num_1_array);
			amountDetails = recordInsertDB.getOperations(Queries.getInstance().getSumDetails(CommonUtils.selectedGame, getArguments().getString("AGENT_ID"), game_id));
			numOneAdapter = new NumberGridAdapter(getActivity(),amountDetails);
			numOneAdapter.updateGridItems(getTwoDigitarray());
			numberGridView.setAdapter(numOneAdapter);
			numOneAdapter.updateBackbg(CommonUtils.selectedBg);
		}
		
	}
	
	
	 Dialog dialog;
	    public void designConfirmDialog(String selectedNum){
	        dialog = new Dialog(getActivity());
	        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        dialog.setContentView(R.layout.add_amount);
	        dialog.setCanceledOnTouchOutside(true);
	        int maxLength = 2;
	        InputFilter[] fArray = new InputFilter[1];
	        fArray[0] = new InputFilter.LengthFilter(maxLength);
	       
//	        dailognumberEdit.setFocusable(false);
	        final EditText dailogamountEdit = (EditText)dialog.findViewById(R.id.amountEdit);
	        dailogamountEdit.setFocusable(true);
	        dailogamountEdit.setEnabled(true);
	        dailogamountEdit.requestFocus();
	        dailogamountEdit.setCursorVisible(true);
	        
	        final EditText dailognumberEdit = (EditText)dialog.findViewById(R.id.numberEdit);
	        dailognumberEdit.setFilters(fArray);
	        dailognumberEdit.setText(""+selectedNum);
//	        dailognumberEdit.setFocusable(false);
	        ApplicationThread.uiPost(getActivity().getClass().getName(), "show/hide progress", new Runnable() {
                @Override
                public void run() {
                	 dailognumberEdit.setFocusable(true);
                }
            }, 50);
	        
	        dialog.findViewById(R.id.okBtn).setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                // TODO Auto-generated method stub
	            	Log.v(LOG_TAG, "#### check.1.."+getProperTime(getendtime_str)+"....."+"...."+getProperTime(sdf.format(new Date())));
					
					if (getProperTime(sdf.format(new Date())) < getProperTime(getendtime_str)) {
						if (dailognumberEdit.getText().toString().equalsIgnoreCase("") || dailognumberEdit.getText().toString().length() == 0) {
//		                	Toast.makeText(getActivity(), "Please enter number", Toast.LENGTH_SHORT).show();
		                	dailognumberEdit.setError("Please enter number");
							return;
						}else if (dailogamountEdit.getText().toString().equalsIgnoreCase("") || dailogamountEdit.getText().toString().length() == 0) {
//		                	Toast.makeText(getActivity(), "Please enter amount", Toast.LENGTH_SHORT).show();
		                	dailogamountEdit.setError("Please enter amount");
		                	return;
						}
		                dialog.dismiss();
						addRecordToDb(dailognumberEdit.getText().toString(), dailogamountEdit.getText().toString());
					} else {
						Toast.makeText(getActivity(), "Sorry This game is not available at this Time", Toast.LENGTH_SHORT).show();
					}
	            }
	        });
	        
	        dialog.findViewById(R.id.cancelBtn).setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                // TODO Auto-generated method stub
	                dialog.dismiss();
	            }
	        });
	        
//	        
//	        TextView selectedNumTxt = (TextView)dialog.findViewById(R.id.selecednum_input);
//	        selectedNumTxt.setText(""+selectedNum);

	        dialog.show();
	    }
	    
	    
	    
	    Dialog typedialog;
	    public void designTypeDialog(String title){
	    	typedialog = new Dialog(getActivity());
	    	typedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    	typedialog.setContentView(R.layout.pawna_type_dialog);
	    	typedialog.setCanceledOnTouchOutside(true);
	    	typedialog.findViewById(R.id.okBtn).setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                // TODO Auto-generated method stub
	            	typedialog.dismiss();
	            }
	        });
	        
	    	typedialog.findViewById(R.id.cancelBtn).setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                // TODO Auto-generated method stub
	            	typedialog.dismiss();
	            }
	        });
	        
	        
	        TextView selectedNumTxt = (TextView)typedialog.findViewById(R.id.titleHeader);
	        selectedNumTxt.setText(""+title);

	        typedialog.show();
	    }
	    
	    
	    public long getProperTime(String inputStr) {
	    	String[] timeArr = inputStr.split(":");
	    	Calendar calendar = null;
	    	if (timeArr.length > 0) {
				calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArr[0]));
				calendar.set(Calendar.MINUTE, Integer.parseInt(timeArr[1]));
				Log.v(LOG_TAG, "### date "+calendar.getTime());
				calendar.getTimeInMillis();
			}  	
	    	
	    	return (calendar == null) ? 0 : calendar.getTimeInMillis();
	    }
	    public String[] getTwoDigitarray(){
	    	String[] twoDigitArr =   new String[100];
	    	for (int i = 0; i < 100; i++) {
				if (i < 10) {
					twoDigitArr[i] = String.format("0%d", i);
				}else{
					twoDigitArr[i] = String.valueOf(i);
				}
			}
	    	return twoDigitArr;
	    }
	    
}