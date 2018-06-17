package com.newgame.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newgame.R;
import com.newgame.database.DatabaseHelper;
import com.newgame.database.Queries;
import com.newgame.models.GameTransactionsModel;
import com.newgame.uihelper.SampleListFragment;

import java.util.ArrayList;
import java.util.TreeSet;

public class EachTransAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_FOOTER = 2;
    private DatabaseHelper recordInsertDB = null;
    private ArrayList<GameTransactionsModel> mData = new ArrayList<GameTransactionsModel>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;
    private Context mContext;
    private SampleListFragment mFragment;

    public EachTransAdapter(Context context, SampleListFragment mFragment) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        recordInsertDB = new DatabaseHelper(context);
        this.mContext = context;
        this.mFragment = mFragment;
    }

    public void addItem(final GameTransactionsModel item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final GameTransactionsModel item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public GameTransactionsModel getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.record_hostory_page, null);
                    holder.numberTxt = (TextView) convertView.findViewById(R.id.number);
                    holder.amountTxt = (TextView) convertView.findViewById(R.id.amount);
                    holder.timeTxt = (TextView) convertView.findViewById(R.id.time);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.headerlayout, null);
                    holder.headerTxt = (TextView) convertView.findViewById(R.id.textSeparator);
                    holder.totAmt = (TextView) convertView.findViewById(R.id.totAmount);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (rowType == TYPE_ITEM) {
            holder.numberTxt.setText("" + mData.get(position).getNumber());
            holder.amountTxt.setText("" + mData.get(position).getEachAmount());
            holder.timeTxt.setText("" + mData.get(position).getTime());
            holder.numberTxt.setTextColor(mData.get(position).getTextColor());
            holder.amountTxt.setTextColor(mData.get(position).getTextColor());
            holder.timeTxt.setTextColor(mData.get(position).getTextColor());
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    mFragment.designConfirmDialog(mData.get(position).getNumber(), mData.get(position).getEachAmount(), mData.get(position).getEntryId());
                }
            });
        } else {
            holder.headerTxt.setText("" + mData.get(position).getGameName());
            holder.totAmt.setText("" + mData.get(position).getGameName());
            holder.headerTxt.setTextColor(mData.get(position).getTextColor());
            String totalValue = recordInsertDB.getCountValue(Queries.getInstance().getTotalSumByGameType(mData.get(position).getGameType()));
            if (null != totalValue && totalValue.length() > 0) {
                holder.totAmt.setVisibility(View.VISIBLE);
                holder.totAmt.setText("" + totalValue);
            } else {
                holder.totAmt.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    public static class ViewHolder {
        TextView numberTxt;
        TextView amountTxt;
        TextView timeTxt;
        TextView headerTxt;
        TextView totAmt;
    }
}
