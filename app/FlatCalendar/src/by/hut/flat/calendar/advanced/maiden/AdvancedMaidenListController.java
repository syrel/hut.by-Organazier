package by.hut.flat.calendar.advanced.maiden;

import android.content.Context;
import by.hut.flat.calendar.widget.list.complex.Entry;
import by.hut.flat.calendar.widget.list.complex.EntryAdd;
import by.hut.flat.calendar.widget.list.complex.EntryEdit;
import by.hut.flat.calendar.widget.list.complex.List;
import by.hut.flat.calendar.widget.list.complex.ListController;

public class AdvancedMaidenListController extends ListController{

	public AdvancedMaidenListController(Context context) {
		super(context);
	}
	
	@Override
	protected List createList() {
		return new AdvancedMaidenList(context);
	}

	@Override
	protected EntryAdd createAdd() {
		return new AdvancedMaidenAdd(context);
	}

	@Override
	protected EntryEdit createEdit(Entry entry) {
		return new AdvancedMaidenEdit(context, (AdvancedMaiden)entry);
	}

}
