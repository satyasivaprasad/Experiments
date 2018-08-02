package com.newgame.uihelper;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newgame.R;
import com.newgame.commons.CommonUtils;
import com.newgame.database.DatabaseHelper;
import com.newgame.models.AllTransactionsModel;
import com.newgame.network.ApplicationThread;
import com.newgame.network.Config;
import com.newgame.network.GamesLiveData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class TransactionsFragment extends Fragment {

    Dialog dialog;
    private View rootView;
    private ListView recordDetailsList;
    private DatabaseHelper recordInsertDB = null;
    private TransactionsAdapter eachTransAdapter;
    //    private TextView totTxt;
    private String type;
    private int colorCode;
    //    private ArrayList<GameTransactionsModel> arrTransdataList = null;
    private View footerView;
    private ProgressDialog progressDialog;
    private int positionToMove = 0;
    private List<AllTransactionsModel> transactionsList;
    private LinkedHashMap<Integer, List<AllTransactionsModel>> dataMap = new LinkedHashMap<>();

    public TransactionsFragment() {

    }

    public TransactionsFragment(String type, int colorCode) {
        this.type = type;
        this.colorCode = colorCode;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.trans_layout, null);
        recordInsertDB = new DatabaseHelper(getActivity());
        recordDetailsList = (ListView) rootView.findViewById(R.id.statusList);
        footerView = getActivity().getLayoutInflater().inflate(R.layout.total_footer, null);
//	    recordDetailsList.addFooterView(footerView);
        positionToMove = 0;
        getAllTransactions();

//		totTxt = (TextView)footerView.findViewById(R.id.totAmount);

        recordDetailsList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

//				designConfirmDialog(arrTransdataList.get(position).getNumber(),arrTransdataList.get(position).getEachAmount(),arrTransdataList.get(position).getEntryId());
            }
        });


        return rootView;
    }

    public void getAllTransactions() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();

        GamesLiveData.getAllTransactionDetails(new ApplicationThread.OnComplete<String>(ApplicationThread.OnComplete.UI) {

            @Override
            public void run() {
                if (!success || null == result) {
                    progressDialog.dismiss();
                    return;
                }
                try {
                    Log.i("Response MEssage...", result.toString());
                    Type collectionType = new TypeToken<Collection<AllTransactionsModel>>() {
                    }.getType();
                    Gson googleJson = new Gson();
                    transactionsList = googleJson.fromJson((String) result, collectionType);
                    refreshData();
                    consolidateTransactionsData();
                    progressDialog.dismiss();
                    progressDialog = null;
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }
        }, Config.live_url + Config.SendBulkData_URL);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recordInsertDB = new DatabaseHelper(getActivity());
    }

    public void refreshData() {
        eachTransAdapter = new TransactionsAdapter(getActivity(), this);
        int preVItem = 0;
        for (int i = 0; i < transactionsList.size(); i++) {
            if (preVItem != transactionsList.get(i).getTransactionID()) {
                preVItem = transactionsList.get(i).getTransactionID();
                eachTransAdapter.addSectionHeaderItem(transactionsList.get(i));
            }
            eachTransAdapter.addItem(transactionsList.get(i));

            if (i == transactionsList.size() - 1) {
                TextView dateTxt = (TextView) footerView.findViewById(R.id.totDate);
                TextView amountTxt = (TextView) footerView.findViewById(R.id.totAmount);
                dateTxt.setText("" + CommonUtils.dateString(transactionsList.get(i).getDate()));
                amountTxt.setVisibility(View.VISIBLE);
                Log.v(TransactionsFragment.class.getSimpleName(), "### check.." + transactionsList.get(i).getTransactionTotal());
                amountTxt.setText("" + transactionsList.get(i).getTransactionTotal());
                recordDetailsList.addFooterView(footerView);
            }
        }
        recordDetailsList.setAdapter(eachTransAdapter);
    }

    private void updateData(final HashMap<String, String> dataMap) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();

        GamesLiveData.uploadWithJson(dataMap, new ApplicationThread.OnComplete<String>(ApplicationThread.OnComplete.UI) {

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
                        if (result.toString().contains("1") || result.toString().contains("Success")) {
                            getAllTransactions();
                            eachTransAdapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "Data updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Sorry this game is not avoilable at this time to update", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                        progressDialog = null;

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }

            }
        }, Config.live_url + Config.SendBulkData_URL);
    }

    public void deleteData(final String id, final int pos) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();

        GamesLiveData.deleteDatafromServer(new ApplicationThread.OnComplete<String>(ApplicationThread.OnComplete.UI) {

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
                        if (result.toString().contains("1") || result.toString().contains("Success")) {
                            getAllTransactions();
                            eachTransAdapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "Record deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Sorry this game is not avoilable at this time to delete", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }

            }
        }, Config.live_url + Config.SendBulkData_URL + "/" + id);
    }

    public void designConfirmDialog(final int pos, final String selectedNum, final String selectedAmount, final String selectedEntryId) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_amount);
        dialog.setCanceledOnTouchOutside(true);
        EditText numtXT = (EditText) dialog.findViewById(R.id.numberEdit);
        final EditText amountTxt = (EditText) dialog.findViewById(R.id.amountEdit);
        numtXT.setText("" + selectedNum);
        amountTxt.setText("" + selectedAmount);
        positionToMove = pos;
        dialog.findViewById(R.id.okBtn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                final String numIMEI = telephonyManager.getDeviceId();
                HashMap<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("ID", selectedEntryId);
                dataMap.put("Amount", amountTxt.getText().toString());
                dataMap.put("Number", selectedNum);
                dataMap.put("TabID", numIMEI);
                updateData(dataMap);
//	            recordInsertDB.updateSatus(Queries.getInstance().updateNumStatus(selectedEntryId, selectedNum, amountTxt.getText().toString()));
            }
        });
        dialog.findViewById(R.id.cancelBtn).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private LinkedHashMap consolidateTransactionsData() {
        int transID = 0;
        dataMap.clear();
        List<AllTransactionsModel> allTransactionsModelList = new ArrayList<>();
        for (int iTransactionPosition = 0; iTransactionPosition < transactionsList.size(); iTransactionPosition++) {
            if (iTransactionPosition == 0) {
                transID = transactionsList.get(iTransactionPosition).getTransactionID();
            }
            if (iTransactionPosition != 0 && transID != transactionsList.get(iTransactionPosition).getTransactionID()) {
                dataMap.put(transID, allTransactionsModelList);
                allTransactionsModelList.clear();
                transID = transactionsList.get(iTransactionPosition).getTransactionID();
            }
            allTransactionsModelList.add(transactionsList.get(iTransactionPosition));
            if (iTransactionPosition == transactionsList.size() - 1) {
                dataMap.put(transID, allTransactionsModelList);
            }
        }

        return dataMap;
    }
}
