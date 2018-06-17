package com.newgame.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.newgame.R;
import com.newgame.commons.CommonUtils;
import com.newgame.uihelper.TransactionsFragment;

public class TransactionsActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_transactions);
		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new SampleListFragment(CommonUtils.selectedGame,CommonUtils.colorCode)).commit();
			
			
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, new TransactionsFragment(CommonUtils.selectedGame,CommonUtils.colorCode)).commit();
		}
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case android.R.id.home:
	            finish();
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }
}
