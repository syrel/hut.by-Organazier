package by.hut.flat.calendar.advanced.maiden;

import android.content.Context;
import by.hut.flat.calendar.widget.list.complex.EntryAdd;
import by.hut.flat.calendar.widget.list.complex.EntryAddView;

public class AdvancedMaidenAdd extends EntryAdd {

	private AdvancedMaidenAddView addView;
	
	public AdvancedMaidenAdd(Context context) {
		super(context);
	}

	@Override
	protected EntryAddView createEntryAddView() {
		addView = new AdvancedMaidenAddView(context);
		return addView;
	}

	@Override
	public void visit(EntryAddView maidenAddView) {
		String fio = addView.getFIO();
		if (fio == null || fio.length() == 0) return;
		
		int[] work = new int[7];
		work[0] = addView.getMonday() ? 1 : 0;
		work[1] = addView.getTuesday() ? 1 : 0;
		work[2] = addView.getWednesday() ? 1 : 0;
		work[3] = addView.getThursday() ? 1 : 0;
		work[4] = addView.getFriday() ? 1 : 0;
		work[5] = addView.getSaturday() ? 1 : 0;
		work[6] = addView.getSunday() ? 1 : 0;
		
		AdvancedMaiden maiden = AdvancedMaiden.valueOf(context, fio, work);
		maiden.save();
		
		this.notifyEntryAdd(maiden);
		addView.clearAllFields();
	}

}
