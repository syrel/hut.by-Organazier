package by.hut.flat.calendar.advanced.maiden;

import android.content.Context;
import by.hut.flat.calendar.widget.list.complex.EntryEdit;
import by.hut.flat.calendar.widget.list.complex.EntryEditView;

public class AdvancedMaidenEdit extends EntryEdit{

	private final AdvancedMaiden maiden;
	private AdvancedMaidenEditView editView;
	
	public AdvancedMaidenEdit(Context context, AdvancedMaiden maiden) {
		super(context, maiden);
		this.maiden = maiden;
		this.editView.initView(this);
	}

	@Override
	protected EntryEditView createEntryEditView() {
		editView = new AdvancedMaidenEditView(context);
		return editView;
	}

	@Override
	public void visit(EntryEditView entryEditView) {
		String fio = editView.getFIO();
		if (fio == null || fio.length() == 0) return;
		
		maiden.setMonday(editView.getMonday());
		maiden.setTuesday(editView.getTuesday());
		maiden.setWednesday(editView.getWednesday());
		maiden.setThursday(editView.getThursday());
		maiden.setFriday(editView.getFriday());
		maiden.setSaturday(editView.getSaturday());
		maiden.setSunday(editView.getSunday());
		maiden.setActive(editView.getActive());
		maiden.setFIO(fio);
		maiden.save();
		this.notifyEntryEditSave();
	}

	public AdvancedMaiden getMaiden() {
		assert maiden != null;
		return maiden;
	}

}
