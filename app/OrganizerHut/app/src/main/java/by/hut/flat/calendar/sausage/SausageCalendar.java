
package by.hut.flat.calendar.sausage;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.advanced.search.SearchActivity;
import by.hut.flat.calendar.advanced.today.TodayActivity;
import by.hut.flat.calendar.anketa.AnketaActivity;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBParser;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.flat.FlatActivity;
import by.hut.flat.calendar.internal.BroadcastSender;
import by.hut.flat.calendar.main.MainActivity;
import by.hut.flat.calendar.sausage.SausageTask.OnTaskCompletedListener;
import by.hut.flat.calendar.statistics.StatisticsActivity;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.layout.ObservableHorizontalScrollView;
import by.hut.flat.calendar.widget.layout.ObservableHorizontalScrollView.HorizontalScrollViewListener;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Locale;

public class SausageCalendar extends LinearLayout implements OnTaskCompletedListener{

	private Date startDate;
	private Date finishDate;
	
	protected Sausage[] sausages;
	protected Button[] buttons;
	private Button buttonMonth;
	private Button buttonYear;
	private ObservableHorizontalScrollView scrollView;
	private LinearLayout sausageContainer;
	private LinearLayout buttonsContainer;
	private Context context;
	protected DBAdapter db;
	private DBParser dbParser;
	private SausageDay sausageDay;
	private SausageMonth sausageMonth;

	private int tasksComplete = 0;
	private boolean flatCalendarLoaded = false;
	private int flatNum;
	
	private Date mCurrentScrollDate;
	
	protected boolean invariant(){
		return  context != null
				&& sausages != null
				&& sausageContainer != null
				&& buttons != null
				&& buttons.length > 0
				&& buttonsContainer != null
				&& db != null;
	}
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public SausageCalendar(Context context){
		this(context,null);
	}
	
	public SausageCalendar(Context context, AttributeSet attrs){
		super(context,attrs);
		this.context = context;
		this.isInEditMode();
		this.db = new DBAdapter(this.context);
		initView();
		initDates();
		initSausages();
		initButtons();
		addButtons();
	}
	/*------------------------------------------------------------
	---------------------------- I N I T -------------------------
	------------------------------------------------------------*/
	private void initView(){
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sausage_calendar, this);
		sausageContainer = (LinearLayout) this.findViewById(R.id.sausage_calendar_container);
		buttonsContainer = (LinearLayout) this.findViewById(R.id.sausage_buttons_container);
		scrollView = (ObservableHorizontalScrollView) this.findViewById(R.id.sausage_scroll);
		scrollView.setVisibility(GONE);
		scrollView.setScrollViewListener(new HorizontalScrollViewListener(){
			@Override
			public void onScrollChanged(ObservableHorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
				Date date = sausageDay.getDate(x);
				if (!date.equals(mCurrentScrollDate)){
			    	initButtonMonthYear(date);
			    	mCurrentScrollDate = date;
				}
			}

		});
	}
	
	private void initDates(){
		this.startDate = new Date().setDay(1).jumpMonth(-Config.INST.SAUSAGE.MONTHS_BEFORE_TODAY);
		this.finishDate = new Date();
		this.finishDate.setDay(this.finishDate.getDaysInMonth()).jumpMonth(Config.INST.SAUSAGE.MONTHS_AFTER_TODAY);

		db.open();
		dbParser = new DBParser(db,startDate,finishDate);
		db.close();
		flatNum = dbParser.getFlatNum();
	}
	
	private void initSausages(){
		sausageDay = new SausageDay(context, startDate, finishDate);
		sausageContainer.addView(sausageDay);
		sausages = new Sausage[flatNum];
		for (int i = 0; i < flatNum;i++){
			sausages[i] = new Sausage(dbParser,context,dbParser.getFlatID(i),startDate,finishDate);
			sausageContainer.addView(sausages[i]);
		}
		sausageMonth = new SausageMonth(context, startDate, finishDate);
		sausageContainer.addView(sausageMonth);
		initSausageTasks();
	}
	
	private void initSausageTasks() {
		/* launching asyncTasks to init and draw cells in background */
		SausageTask task = new SausageTask(sausageDay);
		task.setOnTaskCompletedListener(this);
		task.execute();
		for (int i = 0; i < flatNum;i++){
			task = new SausageTask(sausages[i]);
			task.setOnTaskCompletedListener(this);
			task.execute();
		}
		task = new SausageTask(sausageMonth);
		task.setOnTaskCompletedListener(this);
		task.execute();
	}

	private void initButtons(){
		this.buttons = new Button[dbParser.getFlatNum() + 2];
		assert buttons != null;
		buttonMonth = new Button(context);
		buttonMonth.setBackgroundResource(R.drawable.button_holo_50x50);
		buttonMonth.setTextColor(Color.WHITE);
		buttonMonth.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				StatisticsActivity.show(context);
			}
		});
		initButton(buttonMonth,0);
		for (int index = 1,length = dbParser.getFlatNum(); index <= length; index++) {
			Button button = new Button(context);
			button.setBackgroundResource(R.drawable.button_white_50x50);
			button.setText(Html.fromHtml("<br><big>"+index+"</big><br><small>"+(dbParser.getFlatUseShortAddress(index-1) ? "<font color=\"#5F5F5F\">"+dbParser.getFlatShortAddress(index-1)+"</font></small>":"")));
			button.setTag(""+(index-1));
			button.setTextColor(Color.BLACK);
			button.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					if (!flatCalendarLoaded) return;
					BroadcastSender.send(context, FlatActivity.ACTIVITY_TAG, new String[]{"do","FlatID"}, new String[]{"set_current_flat",v.getTag().toString()});
					BroadcastSender.send(context, MainActivity.ACTIVITY_TAG, new String[]{"do"}, new String[]{"Calendar"});
				}
			});
			button.setOnLongClickListener(new OnLongClickListener(){
				@Override
				public boolean onLongClick(View v) {
					AnketaActivity.show(context, dbParser.getFlatID(Utils.Int(v.getTag().toString())), Date.today().toString());
					return false;
				}
			});
			initButton(button,index);
		}
		buttonYear = new Button(context);
		buttonYear.setBackgroundResource(R.drawable.button_holo_50x50);
		buttonYear.setTextColor(Color.WHITE);
		buttonYear.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				SearchActivity.show(context);
			}
		});
		buttonYear.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View v) {
				TodayActivity.show(context);
				return false;
			}
		});
		initButton(buttonYear,dbParser.getFlatNum() + 1);
    	initButtonMonthYear(Config.INST.SYSTEM.TODAY);
	}

	private void addButtons(){
		assert buttonsContainer != null;
		for (View button : buttons){
			buttonsContainer.addView(button);
		}
	}

	private void initButton(Button aButton, int anIndex){
		aButton.setGravity(Gravity.CENTER);
		aButton.setMaxHeight(Config.INST.SAUSAGE.BUTTON_HEIGHT);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				Config.INST.SAUSAGE.BUTTON_WIDTH,
				Config.INST.SAUSAGE.BUTTON_HEIGHT, Gravity.NO_GRAVITY);
		aButton.setLayoutParams(layoutParams);
		aButton.setPadding(0, 0, 0, 0);
		buttons[anIndex] = aButton;
	}

	private void initButtonMonthYear(Date date) {
		String month = Config.INST.STRINGS.MONTHS_OF_YEAR[ date.month - 1 ];
		if (month.length() > 5){
			month = month.substring(0, 3);
		}
		buttonMonth.setText(month);
		buttonYear.setText(String.format(Locale.US, "%d", date.year));
	}
	
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	protected void reDraw(int FlatID,Date firstDate,Date lastDate){
		assert FlatID > 0;
		sausages[dbParser.getIndex(FlatID)].reDraw(firstDate,lastDate);
	}
	
	@Override
	public void onTaskCompleted() {
		tasksComplete++;
		if (tasksComplete == flatNum+2){
			scrollView.setVisibility(VISIBLE);
			scrollToToday();
			sendLoaded();
		}
	}
	
	/**
	 * Children sausages mark self as initialized. This is post-initialize method.
	 */
	protected void sendLoaded(){
		BroadcastSender.send(context, MainActivity.ACTIVITY_TAG, new String[]{"do"}, new String[]{"timeElapsed"});
		BroadcastSender.send(context, FlatActivity.ACTIVITY_TAG, new String[]{"do"}, new String[]{"load"});
	}
	
	public void scrollToToday(){
		scrollView.post(new Runnable() {
	        public void run() {
	        	int scrollTo = computeScrollToToday();
	        	scrollView.scrollTo(scrollTo, 0);
	        	mCurrentScrollDate = sausageDay.getDate(scrollTo);
	        	initButtonMonthYear(mCurrentScrollDate);
	        }
		});
	}

	public void setFlatCalendarLoaded(){
		this.flatCalendarLoaded = true;
	}
	/*------------------------------------------------------------
	-------------------------- G E T T E R S ---------------------
	------------------------------------------------------------*/
	private int computeScrollToToday() {
		return (this.startDate.daysUntil(Config.INST.SYSTEM.TODAY) - Config.INST.SAUSAGE.CELLS_NUM_BEFORE_TODAY) * Config.INST.SAUSAGE.CELL_WIDTH;
	}
}
