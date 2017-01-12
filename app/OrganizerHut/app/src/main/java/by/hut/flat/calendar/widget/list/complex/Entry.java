package by.hut.flat.calendar.widget.list.complex;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class Entry implements IEntry, IEntryObserver, OnClickListener {
	protected final Context context;
	
	private IEntrySubject subject;
	private EntryView entryView;
		
	public Entry(Context context){
		assert context != null;
		this.context = context;
		initView();
	}
	
	protected void initView(){
		entryView = createEntryView();
		entryView.setOnClickListener(this);
	}
	
	protected abstract EntryView createEntryView();
	
	@Override
	public EntryView getView(){
		return this.entryView;
	}
	@Override
	public void registerEntrySubject(IEntrySubject subject){
		assert subject != null;
		assert this.subject == null;
		this.subject = subject;
	}
	@Override
	public void removeEntrySubject(IEntrySubject subject){
		assert subject != null;
		assert this.subject != null;
		this.subject.equals(subject);
		this.subject = null;
	}
	@Override
	public void clickGoUp() {
		this.notifyObserverGoUp();
	}
	
	@Override
	public void clickGoDown() {
		this.notifyObserverGoDown();
	}

	@Override
	public void notifyObserverGoUp() {
		if (this.subject != null){
			this.subject.notifyGoUp(this);
		}
	}

	@Override
	public void notifyObserverGoDown() {
		if (this.subject != null){
			this.subject.notifyGoDown(this);
		}
	}
	
	@Override
	public void notifyObserverEditEntry() {
		if (this.subject != null){
			this.subject.notifyEditEntry(this);
		}
	}

	public abstract void swapPosition(Entry entry);
	public abstract void save();
	
	@Override
	public void onClick(View v) {
		this.notifyObserverEditEntry();
	}

}
