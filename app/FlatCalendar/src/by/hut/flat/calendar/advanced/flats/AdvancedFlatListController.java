package by.hut.flat.calendar.advanced.flats;

import android.content.Context;
import by.hut.flat.calendar.widget.list.complex.Entry;
import by.hut.flat.calendar.widget.list.complex.EntryAdd;
import by.hut.flat.calendar.widget.list.complex.EntryEdit;
import by.hut.flat.calendar.widget.list.complex.List;
import by.hut.flat.calendar.widget.list.complex.ListController;

public class AdvancedFlatListController extends ListController{

	public AdvancedFlatListController(Context context) {
		super(context);
	}
	
	@Override
	protected List createList() {
		return new AdvancedFlatList(context);
	}

	@Override
	protected EntryAdd createAdd() {
		return new AdvancedFlatAdd(context);
	}

	@Override
	protected EntryEdit createEdit(Entry entry) {
		return new AdvancedFlatEdit(context, (AdvancedFlat)entry);
	}

}
