package com.newgame.uihelper;

import java.util.ArrayList;
import java.util.TreeSet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newgame.R;
import com.newgame.commons.CommonUtils;
import com.newgame.database.DatabaseHelper;
import com.newgame.database.Queries;
import com.newgame.models.AllTransactionsModel;

public class TransactionsAdapter extends BaseAdapter {

	private static final String LOG_TAG = TransactionsAdapter.class.getSimpleName();
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;
	private static final int TYPE_FOOTER = 2;
	private DatabaseHelper recordInsertDB = null;
	private ArrayList<AllTransactionsModel> mData = new ArrayList<AllTransactionsModel>();
	private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
	private TreeSet<Integer> sectionFooters = new TreeSet<Integer>();

	private LayoutInflater mInflater;
	private Context mContext;
	private TransactionsFragment mFragment;

	public TransactionsAdapter(Context context,TransactionsFragment mFragment) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		recordInsertDB = new DatabaseHelper(context);
		this.mContext=context;
		this.mFragment=mFragment;
	}

	public void addItem(final AllTransactionsModel item) {
		mData.add(item);
		notifyDataSetChanged();
	}

	public void addSectionHeaderItem(final AllTransactionsModel item) {
		mData.add(item);
		sectionHeader.add(mData.size() - 1);
		notifyDataSetChanged();
	}
	
	
	public void addSectionFooterItem(final AllTransactionsModel item) {
		mData.add(item);
		sectionFooters.add(mData.size() + 1);
		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		if (sectionHeader.contains(position)) {
			return TYPE_SEPARATOR;
		} else if (sectionFooters.contains(position)) {
			return TYPE_FOOTER;
		} else {
			return TYPE_ITEM;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public AllTransactionsModel getItem(int position) {
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
				convertView = mInflater.inflate(R.layout.trans_records_row, null);
				holder.numberTxt = (TextView)convertView.findViewById(R.id.number);
				holder.amountTxt = (TextView)convertView.findViewById(R.id.amount);
				holder.timeTxt = (TextView)convertView.findViewById(R.id.time);
				holder.timeTxt.setVisibility(View.GONE);
				holder.crossLeft = (ImageView) convertView.findViewById(R.id.cross_image);
				holder.deleteImg = (ImageView) convertView.findViewById(R.id.delete_image);
				holder.crossRight = (ImageView) convertView.findViewById(R.id.cross_right_image);
				holder.editedBy = (ImageView) convertView.findViewById(R.id.editby);
				break;
			case TYPE_SEPARATOR:
				convertView = mInflater.inflate(R.layout.headerlayout, null);
				holder.headerTxt = (TextView) convertView.findViewById(R.id.textSeparator);
				holder.totAmt = (TextView) convertView.findViewById(R.id.totAmount);
				holder.dateTxt = (TextView) convertView.findViewById(R.id.totDate);
				holder.datePart = (RelativeLayout) convertView.findViewById(R.id.descRel);
				break;
			case TYPE_FOOTER:
				convertView = mInflater.inflate(R.layout.total_footer, null);
			
				break;
			}
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (rowType == TYPE_ITEM) {
			Log.v(LOG_TAG, "#### check date..."+mData.get(position).getDate());
			holder.numberTxt.setText(""+mData.get(position).getNumber());
			holder.amountTxt.setText(""+mData.get(position).getAmount());
			holder.timeTxt.setText(""+""+ CommonUtils.dateString(mData.get(position).getDate()));
			holder.timeTxt.setVisibility(View.GONE);
			holder.numberTxt.setTextColor(CommonUtils.giveColorCode(mData.get(position).getGameTypeID(), mContext));
			holder.amountTxt.setTextColor(CommonUtils.giveColorCode(mData.get(position).getGameTypeID(), mContext));
			holder.timeTxt.setTextColor(CommonUtils.giveColorCode(mData.get(position).getGameTypeID(), mContext));
			Log.v(LOG_TAG, "#### Edited By "+mData.get(position).getEditBy());
			if (!TextUtils.isEmpty(mData.get(position).getEditBy()) && mData.get(position).getEditBy().length() > 0) {
				holder.editedBy.setVisibility(View.VISIBLE);
			} else {
				holder.editedBy.setVisibility(View.GONE);
			}
			
			holder.editedBy.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!TextUtils.isEmpty(mData.get(position).getEditBy()) && mData.get(position).getEditBy().length() > 0) {
						
//						 RelativeLayout customLayout = (RelativeLayout)
//								 LayoutInflater.from(mContext).inflate(R.layout.popup_custom_layout, null);
//
//						 QuickAction  quickAction = new QuickAction(mContext, R.style.PopupAnimation, customLayout, customLayout);
//						 quickAction.show(v);
						
						showEditByNames(mData.get(position).getEditBy());
					}
				}
			});
			
			if (mData.get(position).getGameTypeID() == 1) {
				holder.crossRight.setVisibility(View.VISIBLE);
				holder.crossLeft.setVisibility(View.GONE);
			}else if (mData.get(position).getGameTypeID() == 3) {
				holder.crossRight.setVisibility(View.GONE);
				holder.crossLeft.setVisibility(View.GONE);
			} else {
				holder.crossRight.setVisibility(View.GONE);
				holder.crossLeft.setVisibility(View.VISIBLE);
			}
			holder.deleteImg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mFragment.deleteData(String.valueOf(mData.get(position).getID()), position);
				}
			});
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mFragment.designConfirmDialog(position, String.valueOf(mData.get(position).getNumber()), String.valueOf(mData.get(position).getAmount()), String.valueOf(mData.get(position).getID()));
				}
			});
		}else if (rowType == TYPE_SEPARATOR) {
			holder.headerTxt.setText(""+mData.get(position).getAgentName()+" ("+mData.get(position).getGameName()+")");
//			holder.totAmt.setText(""+mData.get(position).getGameName());
			
			
			holder.headerTxt.setTextColor(CommonUtils.giveColorCode(mData.get(position).getGameID(), mContext));
			if (position == 0) {
				holder.datePart.setVisibility(View.GONE);
			} else {
				holder.datePart.setVisibility(View.VISIBLE);
			}
			int pos;
			pos = ((position - 1) < 0) ? 0 : (position -1);
		    String totalValue = recordInsertDB.getCountValue(Queries.getInstance().getTotalSumByTransactionType(String.valueOf(mData.get(pos).getTransactionID())));
		    Log.v("Transaction Adapter", "Check..."+mData.get(pos).getTransactionTotal());
//		    if (null != totalValue && totalValue.length() > 0) {
//		    	holder.totAmt.setVisibility(View.VISIBLE);
//		    	holder.totAmt.setText(""+mData.get(position).getTransactionTotal());
//			}else{
//				holder.totAmt.setVisibility(View.GONE);
//			}
		    holder.totAmt.setVisibility(View.VISIBLE);
		    Log.v(LOG_TAG, "#### check date1..."+mData.get(pos).getDate());
			holder.dateTxt.setText(""+CommonUtils.dateString(mData.get(pos).getDate()));
	    	holder.totAmt.setText(""+mData.get(pos).getTransactionTotal());
		}
		
		return convertView;
	}

	public static class ViewHolder {
		TextView numberTxt;
		TextView amountTxt;
		TextView timeTxt;
		TextView headerTxt;
		TextView totAmt;
		TextView dateTxt;
		RelativeLayout datePart;
		ImageView crossRight, crossLeft, deleteImg, editedBy;
	}
	
	 public AlertDialog alertDialog = null;
	 public void showEditByNames(final String editedBy) {
	        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
	        alertDialogBuilder.setTitle("Edited By");
	        alertDialogBuilder.setMessage(editedBy);
	        
	        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           @Override
	           public void onClick(DialogInterface arg0, int arg1) {
	        	   alertDialog.dismiss();
	           }
	        });
	        
	        alertDialog = alertDialogBuilder.create();
	        alertDialog.show();
	        alertDialog.setCancelable(true);
	        alertDialog.setCanceledOnTouchOutside(true);
	       	       
	  }
	
	
}
