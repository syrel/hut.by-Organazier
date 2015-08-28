package by.hut.flat.calendar.advanced.maiden;

import android.content.Context;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Maiden;
import by.hut.flat.calendar.widget.list.complex.List;
import by.hut.flat.calendar.widget.list.complex.ListView;

public class AdvancedMaidenList extends List {
	private final DBAdapter db;
	private final Maiden maidenDB;
	private int[] maidenIDs;
	
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	
	public AdvancedMaidenList(Context context) {
		super(context);
		db = new DBAdapter(context);
		maidenDB = new Maiden(db);
		initMaidens();
	}

	@Override
	protected ListView createListView() {
		return new AdvancedMaidenListView(context, this);
	}
	
	private void initMaidens(){
		maidenIDs = maidenDB.getMaidenIDs(false);
		AdvancedMaiden maiden = null;
		for (int i = 0; i < maidenIDs.length; i++){
			maiden = new AdvancedMaiden(context,maidenIDs[i]);
			this.initEntry(maiden);
		}
	}

}
