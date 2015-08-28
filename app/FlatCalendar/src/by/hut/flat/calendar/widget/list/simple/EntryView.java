package by.hut.flat.calendar.widget.list.simple;

import by.hut.flat.calendar.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class EntryView extends FrameLayout implements IEntryView {

	private IEntry entry;
	private TextView nameView;
	private ImageView infoView;
	private ImageView arrowView;
		
	public EntryView(Context context, IEntry entry) {
		super(context);
		assert context != null;
		assert entry != null;
		this.entry = entry;
	}
	
	public EntryView(Context context, IEntry entry, boolean custom) {
		super(context);
		assert context != null;
		assert entry != null;
		this.entry = entry;
		if (!custom){
			useSimpleView();
		}
	}
	
	@Override
	public void useSimpleView(){
		setLayout(R.layout.advanced_activity_menu_entry);
        initNameView();
        initInfoView();
        initArrowView();
	}
	
	protected void setLayout(int resID){
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(resID, this);
        inflater = null;
	}
	
	private void initNameView(){
        nameView = (TextView)this.findViewById(R.id.name);
        this.setName(entry.getName());
		
	}
	
	private void initInfoView(){
		infoView = (ImageView) this.findViewById(R.id.info);
	}
	
	protected void setInfoImage(int resourceID){
		infoView.setImageResource(resourceID);
	}
	
	private void initArrowView(){
		arrowView = (ImageView) this.findViewById(R.id.arrow);
		if (entry.isLeaf()) arrowView.setVisibility(8);
	}
	
	@Override
	public void setOnClickListener(OnClickListener l){
		super.setOnClickListener(l);
	}

	@Override
	public View view() {
		return this;
	}

	private void setName(String str) {
		assert nameView != null;
		nameView.setText(str);
	}
	
	protected IEntry getEntry(){
		assert entry != null;
		return entry;
	}

	@Override
	public void setView(IEntryView entryView) {
		assert entryView != null;
		this.removeAllViews();
		addView(entryView.view());
	}

	@Override
	public void clear() {
		this.removeAllViews();
	}
	
	
}
