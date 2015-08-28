package by.hut.flat.calendar.advanced;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import by.hut.flat.calendar.dialog.DialogBackupCreate;
import by.hut.flat.calendar.widget.list.simple.Entry;
import by.hut.flat.calendar.widget.list.simple.EntryView;
import by.hut.flat.calendar.widget.list.simple.IEntryView;

public class BackupCreate extends Entry implements OnClickListener{

	private Context context;
	private IEntryView headerView;
	
	public BackupCreate(Context context) {
		super(context,"Сделать бэкап");
		this.context = context;
		buildView();
	}

	private void buildView() {
		headerView = new EntryView(context,this,false);
		headerView.setOnClickListener(this);
		this.setHeaderView(this.headerView);
	}

	@Override
	public void onClick(View v) {
		DialogBackupCreate.show(context);
	}

}
