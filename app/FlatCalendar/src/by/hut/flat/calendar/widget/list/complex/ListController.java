package by.hut.flat.calendar.widget.list.complex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import by.hut.flat.calendar.R;

public abstract class ListController extends LinearLayout implements OnClickListener, IListControllerObserver{
	
	private static final String BUTTON_ADD_TAG = "add";
	private static final String BUTTON_ADD_SAVE_TAG = "add-save";
	private static final String BUTTON_EDIT_SAVE_TAG = "edit-save";
	
	protected final Context context;
	private LinearLayout container;
	private List list;
	private EntryAdd entryAdd;
	private EntryEdit entryEdit;
	private Button addButton;
	
	
	public ListController(Context context) {
		super(context);
		this.context = context;
		initView();
	}
	/*------------------------------------------------------------
	---------------------------- I N I T -------------------------
	------------------------------------------------------------*/
	private void initView(){
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_list_controller, this);
        inflater = null;
		
        list = createList();
		list.registerControllerObserver(this);
		entryAdd = createAdd();
		entryAdd.registerControllerObserver(this);
		
		container = (LinearLayout) this.findViewById(R.id.container);
		container.addView(list.getView());
		
		initButtons();
		
	}
	
	protected abstract List createList();
	protected abstract EntryAdd createAdd();
	protected abstract EntryEdit createEdit(Entry entry);
	
	private void initButtons(){
		addButton = (Button) this.findViewById(R.id.add);
		addButton.setOnClickListener(this);
		
		setAddButtonAdd();
	}
	
	private void setAddButtonAdd(){
		addButton.setTag(BUTTON_ADD_TAG);
		addButton.setText(context.getResources().getString(R.string.advanced_flat_add_button_add_text));
	}
	private void setAddButtonSave(){
		addButton.setTag(BUTTON_ADD_SAVE_TAG);
		addButton.setText(context.getResources().getString(R.string.advanced_flat_add_button_save_text));
	}
	
	private void setEditButtonSave(){
		addButton.setTag(BUTTON_EDIT_SAVE_TAG);
		addButton.setText(context.getResources().getString(R.string.advanced_flat_add_button_save_text));
	}
	
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	@Override
	public void onClick(View v) {
		if (v.getTag().equals(BUTTON_ADD_TAG)){
			addAction();
		}
		else if (v.getTag().equals(BUTTON_ADD_SAVE_TAG)){
			saveAddAction();
		}
		else if (v.getTag().equals(BUTTON_EDIT_SAVE_TAG)){
			saveEditAction();
		}
	}
	
	private void addAction(){
		assert entryAdd != null;
		container.removeAllViews();
		container.addView(entryAdd.getView());
		setAddButtonSave();
	}
	
	private void saveAddAction(){
		assert entryAdd != null;
		entryAdd.save();
	}
	
	private void saveEditAction(){
		assert entryEdit != null;
		entryEdit.save();
	}
	
	@Override
	public void notifyAddEntry(Entry entry) {
		container.removeAllViews();
		list.add(entry);
		setAddButtonAdd();
		container.addView(list.getView());
	}
	
	@Override
	public void notifyEditEntry(Entry entry) {
		entryEdit = createEdit(entry);
		entryEdit.registerControllerObserver(this);
		container.removeAllViews();
		setEditButtonSave();
		container.addView(entryEdit.getView());
	}
	

	@Override
	public void notifyEditSaveEntry() {
		container.removeAllViews();
		setAddButtonAdd();
		container.addView(list.getView());
	}
	
	public boolean actOnKeyDown(){
	    if (addButton.getTag().equals(BUTTON_ADD_SAVE_TAG) || addButton.getTag().equals(BUTTON_EDIT_SAVE_TAG)){
	    	container.removeAllViews();
	    	setAddButtonAdd();
	    	container.addView(list.getView());
	    	entryAdd.clearAllFields();
	    	return true;
	    }
	    else {
	    	return false;
	    }
	}
}
