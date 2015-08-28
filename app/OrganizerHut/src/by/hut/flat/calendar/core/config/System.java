package by.hut.flat.calendar.core.config;

import java.text.SimpleDateFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.tables.Flat;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Print;
import android.content.Context;
import android.content.pm.ApplicationInfo;

public class System extends Preferences {
	private static final String TAG = "system";
	
	private static final String BUILD_TIME_NAME = "build_time";
	private static final String FLATS_NUM_NAME = "flats_num";
	
	public Date TODAY;
	public String BUILD_TIME;
	public int FLATS_NUM;
		
	public final boolean OK;
	
	public System(Context context) {
		super(TAG, context);
		
		this.TODAY = readToday();
		this.BUILD_TIME = readBuildTime();
		this.FLATS_NUM = readFlatsNum();
		this.OK = true;
	}
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	@Override
	public void reInit() {
		this.BUILD_TIME = initBuildTime();
		this.FLATS_NUM = initFlatsNum();
	}
	
	/*------------------------------------------------------------
	-------------------------- C H E C K S -----------------------
	------------------------------------------------------------*/
	@Override
	public void check(){
		if(!BUILD_TIME.equals(getBuildTime())) Config.INST.REINIT = true;
		if (Config.INST.REINIT){
			reInit();
		}
	}
	/*------------------------------------------------------------
	----------------------------- R E A D ------------------------
	------------------------------------------------------------*/

	private Date readToday(){
		return new Date();
	}
	
	private String readBuildTime(){
		String buildTime = this.getString(BUILD_TIME_NAME);
		if (buildTime.equals(NULL)) buildTime = initBuildTime();
		return buildTime;
	}
	private int readFlatsNum(){
		int flatsNum = this.getInt(FLATS_NUM_NAME);
		if (flatsNum == NONE) flatsNum = initFlatsNum();
		return flatsNum;
	}
	
	/*------------------------------------------------------------
	----------------------------- I N I T ------------------------
	------------------------------------------------------------*/
	
	private String initBuildTime(){
		String buildTime = getBuildTime();
		save(BUILD_TIME_NAME, buildTime);
		return buildTime;
	}
	
	private int initFlatsNum(){
		int flatsNum = getFlatsNum();
		this.save(FLATS_NUM_NAME, flatsNum);
		return flatsNum;
	}
	
	
	private String getBuildTime(){
		String buildTime = NULL;
		try{
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(Config.PACKAGE_NAME, 0);
			ZipFile zipFile = new ZipFile(appInfo.sourceDir);
			ZipEntry zipEntry = zipFile.getEntry("classes.dex");
			long time = zipEntry.getTime();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
			buildTime = simpleDateFormat.format(new java.util.Date(time));
		}
		catch(Exception e){
			Print.err("buildTime: "+e);
		}
		if (buildTime.equals(NULL)){
			Print.err("buildTime: "+buildTime);
		}
		return buildTime;
	}
	
	private int getFlatsNum(){
		if (!getBoolean(Database.TAG,Database.INIT_NAME)) return 0;
		DBAdapter db = new DBAdapter(context);
		db.open();
		Flat flatDB = new Flat(db);
		int flatsNum = flatDB.getFlatNum(true);
		db.close();
		return flatsNum;
	}

}
