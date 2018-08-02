package com.newgame.ui;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newgame.R;
import com.newgame.app.MainInfoActivity;
import com.newgame.commons.CommonUtils;
import com.newgame.models.GameDetailsResult;
import com.newgame.models.GameJsonDataResult;
import com.newgame.network.ApplicationThread;
import com.newgame.network.Config;
import com.newgame.network.GamesLiveData;
import com.newgame.network.Log;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link GameSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameSelectionFragment extends Fragment {
 
    private Spinner facilitySpinner;

    private View rootView;
	private ProgressDialog progressDialog;
    private String[] arrGames;
    private List<GameJsonDataResult> gameTimingsList;
    public static List<GameDetailsResult> gamesList;
    private String gameId;
    private Button veriClick;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     */

    public static GameSelectionFragment newInstance(int param1) {
        GameSelectionFragment fragment = new GameSelectionFragment();
  
        return fragment;
    }

    public GameSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_gameselection, container, false);
        facilitySpinner = (Spinner)rootView.findViewById(R.id.placeOrderSpin);
//		facilitySpinner.setAdapter(new CommonArrayAdapter(getActivity(),getActivity().getResources().getStringArray(R.array.games)));

		veriClick = (Button)rootView.findViewById(R.id.btnContinue);
		
		getGameTimingsDetails();
		
		facilitySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				gameId = String.valueOf(gameTimingsList.get(position).getGameID());
//				getGameTimingsDetails();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		veriClick.setClickable(false);
		veriClick.setEnabled(false);
		veriClick.setFocusable(false);
        veriClick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (CommonUtils.isNetworkAvailable(getActivity())) {
					
					if (null != gameId) {
						if (null != arrGames && arrGames.length > 0 && gameTimingsList != null && gameTimingsList.size() > 0) {
							Intent mIntent = new Intent(getActivity(), MainInfoActivity.class);
							GameJsonDataResult gameObj = null;
							for (int i = 0; i < gameTimingsList.size(); i++) {

								if (gameId.equalsIgnoreCase(gameTimingsList.get(i).getGameID())) {
									gameObj = gameTimingsList.get(i);
								}
							}

							if (gameObj == null) {
								gameObj = gameTimingsList.get(0);
							}
							mIntent.putExtra("GAMEOBJ", gameObj);
							mIntent.putExtra("GAMEID", gameId);
							mIntent.putExtra("gamesArr", arrGames);
							startActivity(mIntent);
						} else {
							Toast.makeText(getActivity(), "Games information not available", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getActivity(), "Please select game", Toast.LENGTH_SHORT).show();
					}
					
				}else {
					CommonUtils.showToast("No internet", getActivity());
				}
			}
		});
        
        getGameDetails();
        return rootView;
    }

	public void getGameDetails(){
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
	                	 Log.i("Response MEssage...", result.toString());
	                	 Type collectionType = new TypeToken<Collection<GameDetailsResult>>(){}.getType();
	                	 Gson googleJson = new Gson();
	                	  gamesList = googleJson.fromJson((String) result,collectionType);
	                
                         Log.i("GameDetails size ..",""+gamesList.size());
                         arrGames = new String[gamesList.size()];
                         for (int i = 0; i < gamesList.size(); i++) {
							arrGames[i] = gamesList.get(i).getName();
						}

                         progressDialog.dismiss();
                         progressDialog = null;
	                }
	                catch (Exception e){
	                    e.printStackTrace();
	                    progressDialog.dismiss();
						Toast.makeText(getActivity(), "Not able to get games information", Toast.LENGTH_SHORT).show();
	                }

	            }
	        }, Config.live_url+Config.games_URL);
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
//                		 facilitySpinner.setAdapter(new CommonArrayAdapter(getActivity(),arrGames));
                		 facilitySpinner.setAdapter(new GameWithTimesAdapter(getActivity(), gameTimingsList));

	                	veriClick.setClickable(true);
	            		veriClick.setEnabled(true);
	            		veriClick.setFocusable(true);
	                }
	                catch (Exception e){
	                    e.printStackTrace();
	                    
	                }
	                finally{
	                	progressDialog.dismiss();
	                }

	            }
	        }, String.format(Config.live_url+Config.GAMETIMINGS_URL) );
	}
}
