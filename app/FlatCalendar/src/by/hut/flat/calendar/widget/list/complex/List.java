package by.hut.flat.calendar.widget.list.complex;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.view.ViewGroup;

public abstract class List implements IListSubject,IEntrySubject{
	private ListView listView;
	
	private IListObserver observer;
	private IListControllerObserver controllerObserver;
	private ArrayList<Entry> entries;
	private int size = 0;
	
	protected final Context context;
	
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	
	public List(Context context){
		assert context != null;
		this.context = context;
		entries = new ArrayList<Entry>();
		listView = createListView();
	}

	protected abstract ListView createListView();
	
	protected void initEntry(Entry entry){
		assert entry != null;
		assert entries != null;
		assert !entries.contains(entry);
		entries.add(entry);
		entry.registerEntrySubject(this);
		size++;
		this.notifyObserverAddEntryView(entry.getView());
	}
	
	private void goUp(Entry entry){
		assert entry != null;
		assert entries != null;
		assert entries.contains(entry);
		int index = entries.indexOf(entry);
		if (index == 0) return;
		Collections.swap(entries,index,index-1);
		notifyObserverGoUp(entry.getView());
		entry.swapPosition(entries.get(index));
	}
	
	private void goDown(Entry entry){
		assert entry != null;
		assert entries != null;
		assert entries.contains(entry);
		int index = entries.indexOf(entry);
		if (index == entries.size()-1) return;
		Collections.swap(entries,index,index+1);
		notifyObserverGoDown(entry.getView());
		entry.swapPosition(entries.get(index));
	}

	public int size(){
		return size;
	}
	
	@Override
	public void registerObserver(IListObserver observer) {
		assert observer != null;
		assert this.observer == null;
		this.observer = observer;
	}
	
	@Override
	public void removeObserver(IListObserver observer) {
		assert observer != null;
		assert this.observer != null;
		assert this.observer.equals(observer);
		this.observer = null;
	}
	
	public void registerControllerObserver(IListControllerObserver controllerObserver){
		assert controllerObserver != null;
		this.controllerObserver = controllerObserver;
	}
	
	public void removeControllerObserver(){
		assert controllerObserver == null;
		this.controllerObserver = null;
	}

	@Override
	public void notifyObserverAddEntryView(EntryView entryView) {
		assert entryView != null;
		if (observer != null){
			observer.notifyAddEntryView(entryView);
		}
	}
	
	@Override
	public void notifyObserverGoUp(EntryView entryView) {
		assert entryView != null;
		if (observer != null){
			observer.notifyGoUp(entryView);
		}
	}

	@Override
	public void notifyObserverGoDown(EntryView entryView) {
		assert entryView != null;
		if (observer != null){
			observer.notifyGoDown(entryView);
		}
	}
	
	public ViewGroup getView(){
		return listView;
	}

	public void add(Entry entry) {
		initEntry(entry);
	}

	@Override
	public void notifyGoUp(Entry entry) {
		assert entry != null;
		this.goUp(entry);
	}

	@Override
	public void notifyGoDown(Entry entry) {
		assert entry != null;
		this.goDown(entry);
	}

	@Override
	public void notifyEditEntry(Entry entry) {
		if (controllerObserver != null){
			controllerObserver.notifyEditEntry(entry);
		}
	}
}
