package com.newgame.uihelper;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.newgame.database.DatabaseHelper;
import com.newgame.database.Queries;
import com.newgame.models.GameTransactionsModel;
import com.newgame.ui.EachTransAdapter;
import com.newgame.ui.RecordDetailsList;

import java.util.ArrayList;
import java.util.LinkedHashMap;


public class SampleListFragment extends Fragment {

    Dialog dialog;
    private View rootView;
    private ListView recordDetailsList;
    private Cursor dataCur;
    private LinkedHashMap<String, String> amountDetails;
    private DatabaseHelper recordInsertDB = null;
    private RecordDetailsList recordsAdapter;
    private EachTransAdapter eachTransAdapter;
    private TextView totTxt;
    private String type;
    private int colorCode;
    private ArrayList<GameTransactionsModel> arrTransdataList = null;

    public SampleListFragment() {

    }

    public SampleListFragment(String type, int colorCode) {
        this.type = type;
        this.colorCode = colorCode;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.samplelist, null);
        recordInsertDB = new DatabaseHelper(getActivity());
        recordDetailsList = (ListView) rootView.findViewById(R.id.statusList);
        View footerView = getActivity().getLayoutInflater().inflate(R.layout.total_footer, null);
//	    recordDetailsList.addFooterView(footerView);

        totTxt = (TextView) footerView.findViewById(R.id.totAmount);
        refreshData(type, colorCode);

        recordDetailsList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

//				designConfirmDialog(arrTransdataList.get(position).getNumber(),arrTransdataList.get(position).getEachAmount(),arrTransdataList.get(position).getEntryId());
            }
        });
        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recordInsertDB = new DatabaseHelper(getActivity());
    }

    public void refreshData(String selectedGame, int colorCode) {
        totTxt.setTextColor(colorCode);
        totTxt.setVisibility(View.GONE);

        arrTransdataList = new ArrayList<GameTransactionsModel>();
        arrTransdataList = recordInsertDB.getMainTransactionData(Queries.getInstance().getRecordDetailswithSeperator());
        eachTransAdapter = new EachTransAdapter(getActivity(), this);
        String preVItem = "0";
        for (int i = 0; i < arrTransdataList.size(); i++) {
            if (!preVItem.equalsIgnoreCase(arrTransdataList.get(i).gameType)) {
                preVItem = arrTransdataList.get(i).gameType;
                eachTransAdapter.addSectionHeaderItem(arrTransdataList.get(i));
            }
            eachTransAdapter.addItem(arrTransdataList.get(i));
        }
        recordDetailsList.setAdapter(eachTransAdapter);
    }

    public void designConfirmDialog(final String selectedNum, final String selectedAmount, final String selectedEntryId) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_amount);
        dialog.setCanceledOnTouchOutside(true);
        EditText numtXT = (EditText) dialog.findViewById(R.id.numberEdit);
        final EditText amountTxt = (EditText) dialog.findViewById(R.id.amountEdit);
        numtXT.setText("" + selectedNum);
        amountTxt.setText("" + selectedAmount);
        dialog.findViewById(R.id.okBtn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                recordInsertDB.updateSatus(Queries.getInstance().updateNumStatus(selectedEntryId, selectedNum, amountTxt.getText().toString()));
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
