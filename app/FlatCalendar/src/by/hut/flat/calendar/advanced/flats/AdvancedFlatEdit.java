package by.hut.flat.calendar.advanced.flats;

import by.hut.flat.calendar.widget.list.complex.EntryEdit;
import by.hut.flat.calendar.widget.list.complex.EntryEditView;
import android.content.Context;

public class AdvancedFlatEdit extends EntryEdit{

	private final AdvancedFlat flat;
	private AdvancedFlatEditView editView;
		
	public AdvancedFlatEdit(Context context, AdvancedFlat flat){
		super(context, flat);
		this.flat = flat;
		editView.initView(this);
	}
	
	@Override
	protected EntryEditView createEntryEditView() {
		editView = new AdvancedFlatEditView(context);
		return editView;
	}
	
	@Override
	public void visit(EntryEditView entryEditView) {
		flat.setAddress(editView.getAddress());
		flat.setShortAddress(editView.getShortAddress());
		flat.setActive(editView.getActive());
		flat.setUseShortAddressSausage(editView.getUseShortAddressSausage());
		flat.save();
		this.notifyEntryEditSave();
	}
	
	public AdvancedFlat getFlat() {
		return this.flat;
	}
}
