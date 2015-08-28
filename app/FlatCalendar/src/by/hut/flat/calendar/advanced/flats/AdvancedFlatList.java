package by.hut.flat.calendar.advanced.flats;

import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Flat;
import by.hut.flat.calendar.widget.list.complex.List;
import by.hut.flat.calendar.widget.list.complex.ListView;

import android.content.Context;

public class AdvancedFlatList extends List{
	private DBAdapter db;
	private Flat flatDB;
	private int[] flatIDs;

	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	
	public AdvancedFlatList(Context context) {
		super(context);
		db = new DBAdapter(context);
		flatDB = new Flat(db);
		initFlats();
	}

	@Override
	protected ListView createListView() {
		return new AdvancedFlatListView(context, this);
	}
	
	private void initFlats(){
		flatIDs = flatDB.getFlatIDs(false);
		AdvancedFlat flat = null;
		for (int i = 0; i < flatIDs.length; i++){
			flat = new AdvancedFlat(context,flatIDs[i]);
			this.initEntry(flat);
		}
	}
	

	
}
