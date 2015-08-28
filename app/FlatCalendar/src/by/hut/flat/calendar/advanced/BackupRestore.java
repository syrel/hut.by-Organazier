package by.hut.flat.calendar.advanced;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import by.hut.flat.calendar.dialog.DialogBackupRestore;
import by.hut.flat.calendar.widget.list.simple.Entry;
import by.hut.flat.calendar.widget.list.simple.EntryView;
import by.hut.flat.calendar.widget.list.simple.IEntryView;

public class BackupRestore extends Entry implements OnClickListener{

	private Context context;
	private IEntryView headerView;
	
	public BackupRestore(Context context) {
		super(context,"Восстановить бэкап");
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
		DialogBackupRestore.show(context);
	}

}
