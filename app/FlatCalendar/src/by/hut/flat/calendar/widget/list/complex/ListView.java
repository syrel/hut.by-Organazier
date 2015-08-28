package by.hut.flat.calendar.widget.list.complex;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import by.hut.flat.calendar.R;

public abstract class ListView extends ScrollView implements IListObserver{
	
	private IListSubject subject;
	private ArrayList<EntryView> entryViews;
	private LinearLayout container;
	
	private int size = 0;
	
	protected final Context context;
	
	private boolean invariant(){
		return entryViews != null
				&& subject != null
				&& size == entryViews.size();
	}
	
	public ListView(Context context,IListSubject subject) {
		super(context);
		assert subject != null;
		assert context != null;
		this.context = context;
		this.subject = subject;
		this.subject.registerObserver(this);
		entryViews = new ArrayList<EntryView>();
		initView();
		invariant();
	}
	
	private void initView(){
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_list, this);
        inflater = null;
        container = (LinearLayout) this.findViewById(R.id.container);
	}

	private void initEntryView(EntryView entryView){
		assert entryView != null;
		assert invariant();
		entryViews.add(entryView);
		container.addView(entryView);
		size++;
		invariant();
	}
	
	private void goUp(EntryView entryView){
		assert entryView != null;
		assert entryView != null;
		assert entryViews.contains(entryView);
		assert container != null;
		int indexList = entryViews.indexOf(entryView);
		int indexView = container.indexOfChild(entryView);
		if (indexList == 0) return;
		Collections.swap(entryViews,indexList,indexList-1);
		container.removeViewAt(indexView);
		assert indexView >= 0;
		container.addView(entryView,indexView-1);
		this.invalidate();
	}
	
	private void goDown(EntryView entryView){
		assert entryView != null;
		assert entryViews != null;
		assert entryViews.contains(entryView);
		assert container != null;
		int indexList = entryViews.indexOf(entryView);
		int indexView = container.indexOfChild(entryView);
		if (indexList == entryViews.size()-1) return;
		Collections.swap(entryViews,indexList,indexList+1);
		container.removeViewAt(indexView);
		assert indexView >= 0;
		container.addView(entryView,indexView+1);
		this.invalidate();
	}
	
	@Override
	public void notifyAddEntryView(EntryView entryView) {
		initEntryView(entryView);
	}

	@Override
	public void notifyGoUp(EntryView entryView) {
		assert entryView != null;
		goUp(entryView);
	}

	@Override
	public void notifyGoDown(EntryView entryView) {
		assert entryView != null;
		goDown(entryView);
	}
}
