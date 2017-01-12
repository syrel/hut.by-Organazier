package by.hut.flat.calendar.widget.list.complex;

import android.content.Context;
import android.view.ViewGroup;

public abstract class EntryAdd implements Visitor {
	
	private EntryAddView addView;
	protected final Context context;

	private IListControllerObserver controllerObserver;
	
	public EntryAdd(Context context){
		assert context != null;
		this.context = context;
		initView();
	}
	
	private void initView(){
		addView = createEntryAddView();
	}
	
	protected abstract EntryAddView createEntryAddView();
	
	public ViewGroup getView(){
		assert addView != null;
		return addView;
	}
	
	@Override
	public void save(){
		addView.accept(this);
	}
	
	/**
	 * Override to clear
	 */
	public void clearAllFields(){
		
	}
	
	public void registerControllerObserver(IListControllerObserver controllerObserver){
		assert controllerObserver != null;
		this.controllerObserver = controllerObserver;
	}
	
	public void removeActivityObserver(){
		assert controllerObserver == null;
		this.controllerObserver = null;
	}

	public void notifyEntryAdd(Entry entry){
		if (controllerObserver != null){
			controllerObserver.notifyAddEntry(entry);
		}
	}
	
	/**
	 * Override to visit
	 */
	@Override
	public abstract void visit(EntryAddView addView);

	@Override
	public void visit(EntryEditView editView) {}
	
}
