package by.hut.flat.calendar.advanced.flats;

import by.hut.flat.calendar.widget.list.complex.EntryAdd;
import by.hut.flat.calendar.widget.list.complex.EntryAddView;
import android.content.Context;

public class AdvancedFlatAdd extends EntryAdd {
	
	private AdvancedFlatAddView addView;
	
	public AdvancedFlatAdd(Context context){
		super(context);
	}
	
	@Override
	protected EntryAddView createEntryAddView() {
		addView = new AdvancedFlatAddView(context);
		return addView;
	}

	@Override
	public void visit(EntryAddView flatAddView) {
		String address = addView.getAddress();
		String shortAddress = addView.getShortAddress();
		if (address == null || address.length() == 0) return;
		
		AdvancedFlat flat = AdvancedFlat.valueOf(context, address,shortAddress);
		flat.save();
		
		this.notifyEntryAdd(flat);
		addView.clearAllFields();
	}
	
}
