package by.hut.flat.calendar.advanced.search;


import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.IntervalAnketa;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.list.anketa.AnketaList;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class SearchActivity  extends SherlockActivity implements OnClickListener {
	public static final String ACTIVITY_TAG = "by.hut.flat.calendar.advanced.search.SearchActivity";
	
	private Button searchButton;
	private EditText searchField;
	private LinearLayout container;
	
	private Context context;
	private DBAdapter db;
	private IntervalAnketa iaDB;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!Config.INST.isRunning(this,ACTIVITY_TAG)){
			Config.INST.addToRuningActivitiesList(ACTIVITY_TAG);
		}
		else {
			finish();
		}
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayUseLogoEnabled(false);
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_striped));
		getSupportActionBar().setTitle(Utils.getString(this, R.string.activity_search_name));
		init();
	}
	
	private void init(){
		setContentView(R.layout.search_activity);
		System.loadLibrary("android_sqlite");
		this.context = this;
		db = new DBAdapter(context);
		iaDB = new IntervalAnketa(db);
		searchButton = (Button) findViewById(R.id.search_button);
		searchButton.setOnClickListener(this);
		searchField = (EditText) findViewById(R.id.search_field);
		container = (LinearLayout) findViewById(R.id.search_container);
	}
	
	private void search(String searchQuery){
		container.removeAllViews();
		if (searchQuery.equals("") || searchQuery == null){
			return;
		}
		if (searchQuery.length() <= 1){
			return;
		}

		ArrayList<String[]> result = iaDB.searchAnkets(Config.INST.SYSTEM.TODAY,searchQuery);
		AnketaList list = new AnketaList(context,result);
		container.addView(list.getView());
	}

	@Override
	public void onClick(View arg0) {
		String search = searchField.getText().toString();
		search(search);
	}
	
	public static void show(Context context){
		Intent intent = new Intent(context, SearchActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
		context.startActivity(intent);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		Config.INST.removeFromRuningActivitiesList(ACTIVITY_TAG);
		super.onDestroy();
	}

	
}
