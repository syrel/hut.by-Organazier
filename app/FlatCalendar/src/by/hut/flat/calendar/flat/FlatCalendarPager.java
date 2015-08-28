package by.hut.flat.calendar.flat;


import java.util.ArrayList;
import java.util.List;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBParser;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.dialog.Dialog;
import by.hut.flat.calendar.dialog.DialogBook;
import by.hut.flat.calendar.dialog.DialogConvert;
import by.hut.flat.calendar.dialog.DialogFree;
import by.hut.flat.calendar.dialog.DialogRent;
import by.hut.flat.calendar.flat.FlatCalendar;
import by.hut.flat.calendar.internal.AnimationsViewPager;
import by.hut.flat.calendar.internal.BroadcastSender;
import by.hut.flat.calendar.internal.InfinitePagerAdapter;
import by.hut.flat.calendar.internal.InfiniteViewPager;
import by.hut.flat.calendar.internal.SamplePagerAdapter;
import by.hut.flat.calendar.main.MainActivity;
import by.hut.flat.calendar.sausage.SausageActivity;
import by.hut.flat.calendar.utils.CalendarHelper;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.layout.ObservableScrollView;
import by.hut.flat.calendar.widget.layout.ObservableScrollView.ScrollViewListener;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A {@link ViewPager} that allows pseudo-infinite paging with a wrap-around
 * effect. Should be used with an {@link InfinitePagerAdapter}.
 * 
 */
public class FlatCalendarPager extends InfiniteViewPager implements OnClickListener {
	private final static int verticalPagerDelta = (int)Utils.convertDpToPx(20, Config.INST.DISPLAY.DPI); 	//30 dp
	private final static int scrollAccuracy = (int)Utils.convertDpToPx(10, Config.INST.DISPLAY.DPI);		// 10 dp
	
	private List<View> pages;
	private List<FlatCalendar> calendars;
	private int currentPage = 0; // TODO initialization
	private Context context;
	private DBAdapter db;
	private DBParser dbParser;
	
	private OnPrepareMenuListener mOnPrepareMenuListener;
	
	public interface OnPrepareMenuListener{
		public void onPrepareMenu(View view,int menuID);
	}
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	
	public FlatCalendarPager(Context context) {
		super(context);
	}
	
	public FlatCalendarPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		db = new DBAdapter(this.context);
		db.open();
		initPages();
	}
	
	
	
	/*------------------------------------------------------------
	---------------------------- I N I T -------------------------
	------------------------------------------------------------*/
	private void initDBParser(){
		Date initDate = new Date().setDay(1);
		Date startDate = new CalendarHelper(initDate).getFirstCalendarDate();
		Date finishDate = startDate.findDate(FlatCalendar.rowsNum*7-1);
		dbParser = new DBParser(db,startDate,finishDate);
	}
	
	private void initPages() {
		/* initalize runnable */
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				pages = new ArrayList<View>();
				calendars = new ArrayList<FlatCalendar>();
				/* init and parse data */
				initDBParser();
				
				for (int index = 0,length = dbParser.getFlatNum(); index < length; index++){
					LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View page = inflater.inflate(R.layout.flat_activity_page, null);
					initPage(page);
					page.setTag(""+(index+1));

					/* init flat header info */
					TextView flatInfo = (TextView) page.findViewById(R.id.flat);
					TextView shortAddressInfo = (TextView) page.findViewById(R.id.short_address);
					TextView monthInfo = (TextView) page.findViewById(R.id.month);
					TextView yearInfo = (TextView) page.findViewById(R.id.year);
					String flatInfoText = context.getResources().getString(R.string.flat_calendar_header_flat) +" "+ (index+1);
					flatInfo.setText(flatInfoText);
					shortAddressInfo.setText(dbParser.getFlatShortAddress(index));
					
					FlatCalendar calendar = (FlatCalendar) page.findViewById(R.id.calendar);
					calendar.initialize(dbParser.getFlatID(index),dbParser);
					monthInfo.setText(Config.INST.STRINGS.MONTHS_OF_YEAR[calendar.getMonth()-1].toUpperCase());
					yearInfo.setText(""+calendar.getYear());
					
					calendars.add(calendar);
					pages.add(page);
				}
				/* work after initializing all cells */
				post(new Runnable() {
					@Override
					public void run() {
						/* after creating cells adding them to UI */
						db.close();
						initPager();
						BroadcastSender.send(context, SausageActivity.ACTIVITY_TAG, new String[]{"do"}, new String[]{SausageActivity.ACTION_FLAT_CALENDAR_LOADED});
						BroadcastSender.send(context, MainActivity.ACTIVITY_TAG, new String[]{"do"}, new String[]{MainActivity.ACTION_FLAT_CALENDAR_LOADED});
					}
				});
			}
		};
		new Thread(runnable).start();
	}
	private void initPage(View page){
		FrameLayout pageHeader = (FrameLayout) page.findViewById(R.id.header);
		setHeight(pageHeader,Config.INST.CALENDAR.HEADER_HEIGHT);

		LinearLayout pageFooter = (LinearLayout) page.findViewById(R.id.footer);
		setHeight(pageFooter,Config.INST.CALENDAR.FOOTER_HEIGHT);
		Button convert = (Button) page.findViewById(R.id.convert);
		convert.setOnClickListener(this);
		Button free = (Button) page.findViewById(R.id.free);
		free.setOnClickListener(this);
		Button book = (Button) page.findViewById(R.id.book);
		book.setOnClickListener(this);
		Button rent = (Button) page.findViewById(R.id.rent);
		rent.setOnClickListener(this);
		Button cleanings = (Button) page.findViewById(R.id.cleanings);
		cleanings.setOnClickListener(this);
		
		ObservableScrollView verticalScroll = (ObservableScrollView) page.findViewById(R.id.vertical_pager);
		verticalScroll.setOnScrollChangedViewListener(new ScrollViewListener(){
			@Override
			public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
				int dy = oldy - y;

            	if (Math.abs(x-oldx) > scrollAccuracy){
            		return;
            	}
            	
            	boolean change = false;
				if (dy > verticalPagerDelta){ 					// next month
            		calendars.get(currentPage).nextMonth();
            		change = true;
            	}
				
            	else if (dy < -verticalPagerDelta){				// prev month
            		calendars.get(currentPage).prevMonth();
            		change = true;
            	}
				if (change){
					TextView monthInfo = (TextView) pages.get(currentPage).findViewById(R.id.month);
	        		TextView yearInfo = (TextView) pages.get(currentPage).findViewById(R.id.year);
	        		monthInfo.setText(Config.INST.STRINGS.MONTHS_OF_YEAR[calendars.get(currentPage).getMonth()-1].toUpperCase());
	        		yearInfo.setText(""+calendars.get(currentPage).getYear());
	        		scrollView.reInitScrollEvent();
				}
			}
		});
	}

	private void initPager(){
		this.setPageTransformer(true, AnimationsViewPager.getAnimation(this.context,-1));
		if (pages.size() > 0){
			PagerAdapter adapter = new SamplePagerAdapter(pages);
			PagerAdapter wrappedAdapter = new InfinitePagerAdapter(adapter);
			if (pages.size() >= 4) this.setAdapter(wrappedAdapter);
			else this.setAdapter(adapter);
		}
		this.setOnPageChangeListener(new OnPageChangeListener(){
			@Override
			public void onPageSelected(int position) {
				currentPage = position % pages.size();
			}
			@Override public void onPageScrollStateChanged(int position) {}
			@Override public void onPageScrolled(int arg0, float arg1, int arg2) {}
			
		});
	}
	private void setHeight(View view, int height){
		android.view.ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.height = height;
		view.setLayoutParams(layoutParams);
	}
	
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	/**
	 * Redraws cells from firstDate to lastDate
	 * @param FlatID
	 * @param firstDate
	 * @param lastDate
	 */
	protected void reDraw(int FlatID,Date firstDate,Date lastDate){
		calendars.get(dbParser.getIndex(FlatID)).reDraw(firstDate,lastDate);
	}
	
	@Override
	public void onClick(View view) {
		int selectedNumber = calendars.get(currentPage).getSelectedCellNumber();
		if (selectedNumber > 0){
			Date firstDate = calendars.get(currentPage).getFirstSelectedCellDate();
			Date lastDate = (selectedNumber == 2) ?
					calendars.get(currentPage).getLastSelectedCellDate() :
					firstDate.findDate(1);
				
			switch(view.getId()){
				case R.id.free:{
					if (selectedNumber == 1){
						free(firstDate);
					}
					break;
				}
				case R.id.convert:{
					if (selectedNumber == 1){
						convert(firstDate);
					}
					break;
				}
				case R.id.book:{
					book(firstDate,lastDate);
					break;
				}
				case R.id.rent:{
					rent(firstDate,lastDate);
					break;
				}
				case R.id.cleanings:{
					if (selectedNumber == 1){
						cleanings(view);
					}
					break;
				}
			}
		}
	}
	
	protected void unmarkSelected(){
		calendars.get(currentPage).unmarkSelected();
		calendars.get(currentPage).resetSelection();
	}
	
	public void setOnPrepareMenuListener(OnPrepareMenuListener l){
		mOnPrepareMenuListener = l;
	}
	/*------------------------------------------------------------
	-------------------------- G E T T E R S ---------------------
	------------------------------------------------------------*/
	protected int getSelectedCellNumber(){
		return calendars.get(currentPage).getSelectedCellNumber();
	}
	
	protected Date getFirstSelectedCellDate(){
		return calendars.get(currentPage).getFirstSelectedCellDate();
	}
	
	protected int getCurrentFlatID(){
		return dbParser.getFlatID(currentPage);
	}
	
	protected int getFirstCellBackground(){
		return calendars.get(currentPage).firstCell.getCellBackground();
	}
	
	protected int getFirstCellState(){
		return calendars.get(currentPage).firstCell.getCellState();
	}
	/*------------------------------------------------------------
	------------------------- P R I V A T E ----------------------
	------------------------------------------------------------*/
	private void convert(Date date){
		int FlatID = dbParser.getFlatID(currentPage);
		if (calendars.get(currentPage).firstCell.getCellState() > 0){
			DialogConvert.show(context, new String[]{Dialog.PARAM_FLAT_ID,Dialog.PARAM_DATE}, new String[]{""+FlatID,date.toString()});
		}
	}
	
	private void free(Date date){
		int FlatID = dbParser.getFlatID(currentPage);
		if (calendars.get(currentPage).firstCell.getCellState() > 0){
			DialogFree.show(context, new String[]{Dialog.ACTION_QUESTION,Dialog.PARAM_FLAT_ID,Dialog.PARAM_DATE}, new String[]{"Free?",""+FlatID,date.toString()});	
		}
	}
	
	private void book(Date firstDate,Date lastDate){
		int FlatID = dbParser.getFlatID(currentPage);		
		DialogBook.show(context, new String[]{Dialog.ACTION_QUESTION,Dialog.PARAM_FLAT_ID,Dialog.PARAM_FIRSTDATE,Dialog.PARAM_LASTDATE},
				new String[]{"Book?",""+FlatID,firstDate.toString(),lastDate.toString()});
	}
	private void rent(Date firstDate,Date lastDate){
		int FlatID = dbParser.getFlatID(currentPage);					
		DialogRent.show(context, new String[]{Dialog.ACTION_QUESTION,Dialog.PARAM_FLAT_ID,Dialog.PARAM_FIRSTDATE,Dialog.PARAM_LASTDATE},
				new String[]{"Rent?",""+FlatID,firstDate.toString(),lastDate.toString()});
	}
	
	private void cleanings(View view){
		if (mOnPrepareMenuListener != null)mOnPrepareMenuListener.onPrepareMenu(view,R.id.cleaning);
	}
}
