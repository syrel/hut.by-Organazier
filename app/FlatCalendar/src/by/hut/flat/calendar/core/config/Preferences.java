package by.hut.flat.calendar.core.config;

import by.hut.flat.calendar.core.Config;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;

public abstract class Preferences implements IPreferences{

	protected static final String NULL = "null";
	protected static final int NONE = -1;
	
	protected final Context context;
	private final SharedPreferences sharedPrefs;
	protected final Resources resources;
	private final String tag;
	
	public Preferences(String tag,Context context){
		this.tag = tag;
		this.context = context;
		sharedPrefs = this.context.getSharedPreferences(Config.PACKAGE_NAME, Context.MODE_PRIVATE);
		resources = this.context.getResources();
	}
	
	protected int getInt(String name){
		return sharedPrefs.getInt(name(name), NONE);
	}
	
	protected long getLong(String name){
		return sharedPrefs.getLong(name(name), NONE);
	}
	
	protected String getString(String name){
		return sharedPrefs.getString(name(name), NULL);
	}
	
	protected boolean getBoolean(String name){
		return sharedPrefs.getBoolean(name(name), false);
	}
	
	protected boolean getBoolean(String tag,String name){
		return sharedPrefs.getBoolean(name(tag,name), false);
	}
	
	protected void save(String name, int value){
		Editor editor = sharedPrefs.edit();
		editor.putInt(name(name), value);
		editor.commit();
	}
	
	protected void save(String name, String value){
		Editor editor = sharedPrefs.edit();
		editor.putString(name(name), value);
		editor.commit();
	}
	
	protected void save(String name, long value){
		Editor editor = sharedPrefs.edit();
		editor.putLong(name(name), value);
		editor.commit();
	}
	
	protected void save(String name, boolean value){
		Editor editor = sharedPrefs.edit();
		editor.putBoolean(name(name), value);
		editor.commit();
	}
	
	private String name(String name){
		return tag+'_'+name;
	}
	
	private String name(String tag,String name){
		return tag+'_'+name;
	}
}
