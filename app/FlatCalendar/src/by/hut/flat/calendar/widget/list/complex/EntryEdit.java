package by.hut.flat.calendar.widget.list.complex;

import android.content.Context;
import android.view.ViewGroup;

public abstract class EntryEdit implements Visitor{
	protected final Context context;

	private final Entry entry;
	private EntryEditView editView;
	
	private IListControllerObserver controllerObserver;
	
	public EntryEdit(Context context, Entry entry){
		assert context != null;
		assert entry != null;
		this.context = context;
		this.entry = entry;
		initView();
	}
	
	private void initView(){
		editView = createEntryEditView();
	}
	
	protected abstract EntryEditView createEntryEditView();
	
	public ViewGroup getView(){
		assert editView != null;
		return editView;
	}
	
	public void registerControllerObserver(IListControllerObserver controllerObserver){
		assert controllerObserver != null;
		this.controllerObserver = controllerObserver;
	}
	
	public void removeControllerObserver(){
		assert controllerObserver == null;
		this.controllerObserver = null;
	}
	
	public void notifyEntryEditSave(){
		if (controllerObserver != null){
			controllerObserver.notifyEditSaveEntry();
		}
	}
	
	public Entry getEntry() {
		return this.entry;
	}
	
	@Override
	public void save(){
		editView.accept(this);
	}

	/**
	 * Overide to visit
	 */
	@Override
	public abstract void visit(EntryEditView editView);

	@Override
	public void visit(EntryAddView addView) {}
}
