package by.hut.flat.calendar.dialog.floating.select.cleanings;

import android.content.Context;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.list.simple.List;

public class CleaningList extends List{
	private Context context;
	
	public CleaningList(Context context) {
		super(context);
		this.context = context;
		this.initList();
	}

	@Override
	protected void initList() {
		CleaningEntry add = new CleaningEntry(context,Utils.getString(context, R.string.menu_flat_cleaning_add),R.id.cleaning_add);
		CleaningEntry delete = new CleaningEntry(context,Utils.getString(context, R.string.menu_flat_cleaning_delete),R.id.cleaning_delete);
		delete.hideLine();
		add(add);
		add(delete);
	}
}
