package by.hut.flat.calendar.advanced;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.advanced.maiden.AdvancedMaidenActivity;
import by.hut.flat.calendar.widget.list.simple.Entry;
import by.hut.flat.calendar.widget.list.simple.EntryView;
import by.hut.flat.calendar.widget.list.simple.IEntryView;

public class Maiden extends Entry implements OnClickListener{
	private Context context;
	private IEntryView headerView;
	
	public Maiden(Context context) {
		super(context, context.getResources().getString(R.string.advanced_menu_maids));
		this.context = context;
		buildView();
	}
	
	private void buildView(){
		headerView = new EntryView(context,this,false);
		headerView.setOnClickListener(this);
		this.setHeaderView(this.headerView);
	}

	@Override
	public void onClick(View v) {
		AdvancedMaidenActivity.show(context);
	}
}
