package by.hut.flat.calendar.core.config;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import android.content.Context;

public class Main extends Preferences {
	private static final String TAG = "main";
	
	private static final String TAB_HOST_HEIGHT_NAME = "tab_host_height";

	public int TAB_HOST_HEIGHT;
	
	public final boolean OK;
	
	public Main(Context context) {
		super(TAG, context);
		
		this.TAB_HOST_HEIGHT = readTabHostHeight();
		
		check();
		this.OK = true;
	}
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	@Override
	public void reInit() {
		this.TAB_HOST_HEIGHT = initTabHostHeight();
	}
	
	/*------------------------------------------------------------
	-------------------------- C H E C K S -----------------------
	------------------------------------------------------------*/
	@Override
	public void check() {
		if (Config.INST.REINIT){
			reInit();
		}
	}
	/*------------------------------------------------------------
	----------------------------- R E A D ------------------------
	------------------------------------------------------------*/
	private int readTabHostHeight(){
		int tabHostHeight = this.getInt(TAB_HOST_HEIGHT_NAME);
		if (tabHostHeight == NONE) tabHostHeight = this.initTabHostHeight();
		return tabHostHeight;
	}
	
	/*------------------------------------------------------------
	----------------------------- I N I T ------------------------
	------------------------------------------------------------*/
	private int initTabHostHeight(){
		int tabHostHeight = (int) resources.getDimension(R.dimen.default_main_tab_host_height);
		save(TAB_HOST_HEIGHT_NAME, tabHostHeight);
		return tabHostHeight;
	}
}
