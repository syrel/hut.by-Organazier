package by.hut.flat.calendar.advanced.maiden;

import android.content.Context;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Employee;
import by.hut.flat.calendar.core.DBAdapter.tables.Maiden;
import by.hut.flat.calendar.core.DBAdapter.tables.QuerySyntaxException;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.list.complex.Entry;
import by.hut.flat.calendar.widget.list.complex.EntryView;

public class AdvancedMaiden extends Entry{

	private final DBAdapter db;
	private final Maiden maidenDB;
	private final Employee employeeDB;
	private int MaidenID;
	private String fio;
	private boolean monday;
	private boolean tuesday;
	private boolean wednesday;
	private boolean thursday;
	private boolean friday;
	private boolean saturday;
	private boolean sunday;
	private boolean active;
	//private int background;
	
	private AdvancedMaidenView maidenView;
	
	private boolean invariant(){
		return db != null
				&& maidenDB != null
				&& fio != null;
	}
	
	private AdvancedMaiden(Context context, String fio, int[] work){
		super(context);
		assert context != null;
		assert fio != null;
		this.fio = fio;
		this.active = true;
		monday = work[0] == 1;
		tuesday = work[1] == 1;
		wednesday = work[2] == 1;
		thursday = work[3] == 1;
		friday = work[4] == 1;
		saturday = work[5] == 1;
		sunday = work[6] == 1;
		db = new DBAdapter(this.context);
		maidenDB = new Maiden(db);
		employeeDB = new Employee(db);
	}
	
	public static AdvancedMaiden valueOf(Context context, String fio, int[] work) {
		return new AdvancedMaiden(context,fio,work);
	}
	
	
	public AdvancedMaiden(Context context,int MaidenID) {
		super(context);
		db = new DBAdapter(this.context);
		maidenDB = new Maiden(db);
		employeeDB = new Employee(db);
		this.MaidenID = MaidenID;
		initMaiden();
		maidenView.init();
		assert invariant();
	}

	private void initMaiden(){
		String[] eData = employeeDB.getData(MaidenID);
		String[] mData = maidenDB.getData(MaidenID);
		fio = eData[Employee.FIO];
		active = (Utils.Int(eData[Employee.ACTIVE]) == 1);
		monday = (Utils.Int(mData[Maiden.MON]) == 1);
		tuesday = (Utils.Int(mData[Maiden.TUE]) == 1);
		wednesday = (Utils.Int(mData[Maiden.WED]) == 1);
		thursday = (Utils.Int(mData[Maiden.THU]) == 1);
		friday = (Utils.Int(mData[Maiden.FRI]) == 1);
		saturday = (Utils.Int(mData[Maiden.SAT]) == 1);
		sunday = (Utils.Int(mData[Maiden.SUN]) == 1);
		//background = MathExt.Int(mData[Maiden.BACKGROUND]);
	}
	
	@Override
	public void swapPosition(Entry entry) {
		try {
			employeeDB.swapPositions(this.MaidenID, ((AdvancedMaiden)entry).MaidenID);
		} catch (QuerySyntaxException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected EntryView createEntryView() {
		maidenView = new AdvancedMaidenView(context,this);
		return maidenView;
	}

	@Override
	public void save(){
		assert fio != null;
		assert db != null;
		assert maidenDB != null;
		assert employeeDB != null;
		if (MaidenID < 1){ /* add new maid */
			MaidenID = (int) maidenDB.addMaiden(fio);
			initView();
			this.maidenView = (AdvancedMaidenView) this.getView();
			maidenView.init();
		}
		assert MaidenID > 0;
		maidenDB.updateMaiden(MaidenID, new int[]{Maiden.MON,Maiden.TUE,Maiden.WED,Maiden.THU,Maiden.FRI,Maiden.SAT,Maiden.SUN},
				new String[]{boolToString(monday),boolToString(tuesday),boolToString(wednesday),boolToString(thursday),boolToString(friday),boolToString(saturday),boolToString(sunday)});
		employeeDB.updateEmployee(MaidenID, new int[]{Employee.FIO,Employee.ACTIVE}, new String[]{fio,boolToString(active)});
		invariant();
	}

	public boolean getActive() {
		assert invariant();
		return this.active;
	}

	public void setActive(boolean active){
		this.active = active;
		assert maidenView != null;
		maidenView.setActive(active);
	}
	
	public int getID() {
		assert invariant();
		return this.MaidenID;
	}

	public String getFIO() {
		assert invariant();
		return this.fio;
	}
	
	public void setFIO(String fio){
		assert fio != null;
		assert fio.length() > 0;
		this.fio = fio;
		assert maidenView != null;
		maidenView.setFIO(fio);
	}
	
	private String boolToString(boolean bool){
		return ((bool) ? "1" : "0");
	}
	
	public boolean isMonday() {
		return monday;
	}

	public void setMonday(boolean monday) {
		this.monday = monday;
	}

	public boolean isTuesday() {
		return tuesday;
	}

	public void setTuesday(boolean tuesday) {
		this.tuesday = tuesday;
	}

	public boolean isWednesday() {
		return wednesday;
	}

	public void setWednesday(boolean wednesday) {
		this.wednesday = wednesday;
	}

	public boolean isThursday() {
		return thursday;
	}

	public void setThursday(boolean thursday) {
		this.thursday = thursday;
	}

	public boolean isFriday() {
		return friday;
	}

	public void setFriday(boolean friday) {
		this.friday = friday;
	}

	public boolean isSaturday() {
		return saturday;
	}

	public void setSaturday(boolean saturday) {
		this.saturday = saturday;
	}

	public boolean isSunday() {
		return sunday;
	}

	public void setSunday(boolean sunday) {
		this.sunday = sunday;
	}

}
