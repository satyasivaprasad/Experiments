package com.newgame.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.newgame.R;
import com.newgame.commons.CommonUtils;
import com.newgame.commons.DateFormats;
import com.newgame.database.DatabaseHelper;
import com.newgame.database.Queries;
import com.newgame.network.ApplicationThread;
import com.newgame.network.Config;
import com.newgame.network.GamesLiveData;
import com.newgame.network.Log;
import com.newgame.ui.GameSelectionFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    public static String numIMEI = "";
    List<HashMap> recordList = null;
    private FrameLayout veriLayout;
    private static ProgressDialog progressDialog;
    private DatabaseHelper initialDB = null;
    private static SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initialDB = new DatabaseHelper(this);
        try {
            initialDB.createDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String dateTime = DateFormats.getCurrentDatetimePrefix();

        String dates[] = null;
        if (dateTime.length() > 0) {
            dates = dateTime.split(" ");
        }

        initialDB.updateSatus(Queries.getInstance().deletePreviousData(dates[0]));

        prefs = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        boolean isLogin = prefs.getBoolean("isLogin", false);

        if (isLogin) {
            FragmentManager fm = getSupportFragmentManager();
            GameSelectionFragment newFragment = new GameSelectionFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(android.R.id.content, newFragment).commit();
        } else {
            boolean isExisted = DatabaseHelper.isRecordExisted(Queries.getInstance().checkExistance("user_details"), this);

            if (isExisted) {
                if (CommonUtils.isNetworkAvailable(WelcomeActivity.this)) {
                    FragmentManager fm = getSupportFragmentManager();
                    VerficationFragment newFragment = new VerficationFragment();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(android.R.id.content, newFragment).commit();
                } else {
                    CommonUtils.showToast("No internet", WelcomeActivity.this);
                }
            }

            setContentView(R.layout.activity_welcome);
            veriLayout = (FrameLayout) findViewById(R.id.container);

            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            numIMEI = telephonyManager.getDeviceId();
            TextView imeiNumberTxt = (TextView) findViewById(R.id.imeiNumberTxt);
            imeiNumberTxt.setText(numIMEI);
        }

    }

    public void onConfirmClick(View v) {
        if (CommonUtils.isNetworkAvailable(WelcomeActivity.this)) {
            updateData();
        } else {
            CommonUtils.showToast("No internet", WelcomeActivity.this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateData() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        numIMEI = telephonyManager.getDeviceId();
        numIMEI = "358158071578052";
        GamesLiveData.getAllTransactionDetails(new ApplicationThread.OnComplete<String>(ApplicationThread.OnComplete.UI) {

            @Override
            public void run() {
                Log.i("Response MEssage jsonData...", "Response MEssage jsonData...First " + result.toString());
                if (!success || null == result) {
                    progressDialog.dismiss();
                    progressDialog = null;
                    Toast.makeText(WelcomeActivity.this, "Error while updating transaction", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Log.i("Response MEssage jsonData...", result.toString());
                        progressDialog.dismiss();
                        progressDialog = null;

                        JSONArray arr = new JSONArray(result.toString());
                        if (arr != null) {
                            JSONObject jObject = arr.getJSONObject(0);
                            recordList = new ArrayList<HashMap>();
                            HashMap<String, String> dataMap = new HashMap<String, String>();
                            dataMap.put("AgentID", jObject.getString("AgentID"));
                            dataMap.put("UserName", jObject.getString("UserName"));
                            dataMap.put("Password", jObject.getString("Password"));
                            dataMap.put("MobileNo", jObject.getString("MobileNo"));
                            dataMap.put("TabID", numIMEI);
                            recordList.add(dataMap);
                        }


                        ApplicationThread.dbPost(this.getClass().getName(), "insert..EachEntryDetails..", new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                initialDB.insertData("user_details", recordList, WelcomeActivity.this);

                                ApplicationThread.uiPost(WelcomeActivity.this.getClass().getName(), "show/hide progress", new Runnable() {
                                    @Override
                                    public void run() {
                                        FragmentManager fm = getSupportFragmentManager();
                                        VerficationFragment newFragment = new VerficationFragment();
                                        FragmentTransaction ft = fm.beginTransaction();
                                        ft.add(android.R.id.content, newFragment).commit();
                                    }
                                });
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        progressDialog = null;
                        Toast.makeText(WelcomeActivity.this, "Not able to get user details", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        }, String.format(Config.live_url + Config.GetUserNameAndPassword, numIMEI));
    }


    public static class VerficationFragment extends Fragment {

        private EditText userName, password;
        private Button veriClick;

        public VerficationFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.verification_main, container, false);
            v.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                }
            });
            userName = (EditText) v.findViewById(R.id.editText3);
            password = (EditText) v.findViewById(R.id.passwordEdit);
            Button veriClick = (Button) v.findViewById(R.id.btnContinue);
            veriClick.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (CommonUtils.isNetworkAvailable(getActivity())) {
                        if (!TextUtils.isEmpty(userName.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {
                            CommonUtils.hideKeyboard(getActivity(), password);
                            loginVerify(userName.getText().toString(), password.getText().toString());
                        } else {
                            Toast.makeText(getActivity(), "Please enter proper user name, and password", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        CommonUtils.showToast("No internet", getActivity());
                    }
                }
            });
            return v;
        }


        private void loginVerify(final String username, final String password) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait..");
                progressDialog.setCancelable(false);
            }
            progressDialog.show();
            TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

            numIMEI = telephonyManager.getDeviceId();
            numIMEI = "351558071527586";
            GamesLiveData.GetGenericData(new ApplicationThread.OnComplete<String>(ApplicationThread.OnComplete.UI) {

                @Override
                public void run() {
                    Log.i("Response MEssage jsonData...", "Response MEssage jsonData...First " + result.toString());
                    if (!success || null == result) {
                        progressDialog.dismiss();
                        progressDialog = null;
                        Toast.makeText(getActivity(), "Error while updating transaction", Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            Log.i("Response MEssage jsonData...", result.toString());
                            progressDialog.dismiss();
                            progressDialog = null;

                            if (result.toString().contains("User not valid.")) {
                                Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_LONG).show();
                            } else {
                                prefs.edit().putBoolean("isLogin", true).commit();
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                GameSelectionFragment newFragment = new GameSelectionFragment();
                                FragmentTransaction ft = fm.beginTransaction();
                                ft.add(android.R.id.content, newFragment).commit();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                    }

                }
            }, String.format(Config.live_url + Config.CheckUserNameAndPassword, username, password, numIMEI));
        }

    }


}
