package by.hut.flat.calendar.advanced.flats;

import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Flat;
import by.hut.flat.calendar.core.DBAdapter.tables.QuerySyntaxException;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.list.complex.Entry;
import by.hut.flat.calendar.widget.list.complex.EntryView;
import android.content.Context;

public class AdvancedFlat extends Entry {
	
	private DBAdapter db;
	private Flat flatDB;
	private int FlatID;
	private String address;
	private String shortAddress;
	private boolean active;
	private boolean useShortAddressSausage;
	
	private AdvancedFlatView flatView;
	
	
	private boolean invariant(){
		return db != null
				&& flatDB != null
				&& address != null;
	}
	
	private AdvancedFlat(Context context, String address,String shortAddress){
		super(context);
		assert context != null;
		assert address != null;
		this.address = address;
		this.shortAddress = shortAddress;
		this.useShortAddressSausage = true;
		this.active = true;
		db = new DBAdapter(this.context);
		flatDB = new Flat(db);
	}
	
	public static AdvancedFlat valueOf(Context context, String address,String shortAddress){
		return new AdvancedFlat(context,address,shortAddress);
	}
	
	public AdvancedFlat(Context context, int FlatID){
		super(context);
		assert context != null;
		db = new DBAdapter(this.context);
		flatDB = new Flat(db);
		this.FlatID = FlatID;
		initFlat();
		flatView.init();
		assert invariant();
	}
	
	private void initFlat(){
		String[] flatData = flatDB.getData(FlatID);
		address = flatData[Flat.ADDRESS];
		active = (Utils.Int(flatData[Flat.ACTIVE]) == 1);
		useShortAddressSausage = (Utils.Int(flatData[Flat.USE_SHORT_ADDRESS_SAUSAGE]) == 1);
		shortAddress = flatData[Flat.SHORT_ADDRESS];
	}
	
	@Override
	public void swapPosition(Entry entry) {
		try {
			flatDB.swapPositions(this.FlatID, ((AdvancedFlat)entry).FlatID);
		} catch (QuerySyntaxException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected EntryView createEntryView() {
		flatView = new AdvancedFlatView(context,this);
		return flatView;
	}
	
	public int getFlatID(){
		assert invariant();
		return this.FlatID;
	}
	
	public String getAddress(){
		assert invariant();
		return this.address;
	}

	public String getShortAddress() {
		assert invariant();
		return this.shortAddress;
	}
	
	public boolean getActive(){
		assert invariant();
		return this.active;
	}
	
	public boolean getUseShortAddressSausage(){
		assert invariant();
		return this.useShortAddressSausage;
	}
	
	public void setAddress(String address){
		assert address != null;
		assert address.length() > 0;
		this.address = address;
		assert flatView != null;
		flatView.setAddress(address);
	}
	
	public void setShortAddress(String shortAddress){
		assert shortAddress != null;
		this.shortAddress = shortAddress;
	}
	
	public void setUseShortAddressSausage(boolean useShortAddressSausage){
		this.useShortAddressSausage = useShortAddressSausage;
	}
	
	public void setActive(boolean active){
		this.active = active;
		assert flatView != null;
		flatView.setActive(active);
	}
	
	@Override
	public void save(){
		assert address != null;
		assert db != null;
		assert flatDB != null;
		if (FlatID < 1){ /* add new flat */
			FlatID = (int) flatDB.addFlat(address);
			initView();
			flatView.init();
		}
		flatDB.updateFlat(FlatID, new int[]{Flat.ADDRESS,Flat.SHORT_ADDRESS,Flat.USE_SHORT_ADDRESS_SAUSAGE,Flat.ACTIVE}, new String[]{address,shortAddress,boolToString(useShortAddressSausage),boolToString(active)});
		Config.INST.reInit();
		invariant();
	}
	
	private String boolToString(boolean bool){
		return ((bool) ? "1" : "0");
	}

	

}
