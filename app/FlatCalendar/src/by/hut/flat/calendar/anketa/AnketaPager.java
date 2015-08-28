package by.hut.flat.calendar.anketa;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.dbStructure;
import by.hut.flat.calendar.core.DBAdapter.tables.Debt;
import by.hut.flat.calendar.core.DBAdapter.tables.Interval;
import by.hut.flat.calendar.core.DBAdapter.tables.IntervalAnketa;
import by.hut.flat.calendar.core.DBAdapter.tables.Money;
import by.hut.flat.calendar.core.DBAdapter.tables.Payment;
import by.hut.flat.calendar.core.DBAdapter.tables.Prepay;
import by.hut.flat.calendar.dialog.Dialog;
import by.hut.flat.calendar.flat.FlatActivity;
import by.hut.flat.calendar.internal.BroadcastSender;
import by.hut.flat.calendar.internal.InfiniteViewPager;
import by.hut.flat.calendar.internal.SamplePagerAdapter;
import by.hut.flat.calendar.sausage.SausageActivity;
import by.hut.flat.calendar.utils.Date;
import by.hut.flat.calendar.utils.DateView;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.utils.Print;

public class AnketaPager extends InfiniteViewPager implements Visitor{
	private List<View> pages;
	private Context context;
	private DBAdapter db;
	private Interval intervalDB;
	private IntervalAnketa intervalAnketaDB;
	private Payment paymentDB;
	private Prepay prepayDB;
	private Debt debtDB;
	private Money moneyDB;
	
	private int FlatID;
	private int currentPage = 0;

	private int[] intervalIDs;
	private int[][] intervalData;
	private String[][] intervalAnketaData;
	private int[][] payments;
	private int[][] debts;
	private int[][] prepays;
	private int[] intervalType;
	private LinearLayout[] framesContainer;
	/**
	 * 0 - view, 1 - edit
	 */
	private int[] pageStates;
	
	private OnPageSelectedListener onPageSelectedListener;
	private OnModeChangedListener mOnModeChangedListener;
	private OnLongClickListener onLongClickLister;
	private SamplePagerAdapter pagerAdapter;
	
	public interface OnPageSelectedListener {
		public void onPageSelected(int index);
	}
	
	public interface OnModeChangedListener {
		public void onModeChanged(int mode, int pageIndex);
	}
	
	public AnketaPager(Context context) {
		super(context);
	}
	public AnketaPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		db = new DBAdapter(this.context);
	}

	private OnLoadListener mOnLoadListener;
	public interface OnLoadListener{
		public void onLoad(AnketaPager pager);
	}
	
	/*------------------------------------------------------------
	---------------------------- I N I T -------------------------
	------------------------------------------------------------*/

	public void initPages(int FlatID,final int[] intervalIDs) {
		/* initalize runnable */
		intervalAnketaDB = new IntervalAnketa(db);
		intervalDB = new Interval(db);
		paymentDB = new Payment(db);
		debtDB = new Debt(db);
		prepayDB = new Prepay(db);
		moneyDB = new Money(db);
		
		this.FlatID = FlatID;
		this.intervalIDs = intervalIDs;
		intervalData = new int[this.intervalIDs.length][];
		intervalAnketaData = new String[this.intervalIDs.length][];
		payments = new int[this.intervalIDs.length][];
		prepays = new int[this.intervalIDs.length][];
		debts = new int[this.intervalIDs.length][];
		intervalType = new int[this.intervalIDs.length];
		framesContainer = new LinearLayout[this.intervalIDs.length];
		pageStates = new int[this.intervalIDs.length];
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
		        Looper.prepare();
				pages = new ArrayList<View>();
				db.open();
				for (int i = 0; i < intervalIDs.length; i++){
					LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View page = inflater.inflate(R.layout.anketa_activity_page, null);
					initPage(page,i);
					pages.add(page);
				}
				/* work after initializing all cells */
				post(new Runnable() {
					@Override
					public void run() {
						/* after creating cells adding them to UI */
						db.close();
						initPager();
						if (mOnLoadListener != null) mOnLoadListener.onLoad(AnketaPager.this);
						for (int i = 0,length = pageStates.length;i < length; i++){
							if (pageStates[i] == 1 && mOnModeChangedListener != null){
								mOnModeChangedListener.onModeChanged(1,i);
							}
						}
					}
				});
			}
		};
		new Thread(runnable).start();
	}
	
	/**
	 * Initializes anketa single page. Default is page view
	 * @param page
	 * @param index
	 */
	private void initPage(View page,int index){
		intervalData[index] = intervalDB.getData(intervalIDs[index]);
		intervalAnketaData[index] = intervalAnketaDB.getData(intervalIDs[index]);
		intervalType[index] = Utils.Int(intervalAnketaData[index][IntervalAnketa.TYPE]);
		payments[index] = Utils.getRow(paymentDB.getPaymentsByIntervalID(intervalIDs[index]),0);
		prepays[index] = Utils.getRow(prepayDB.getPrepaysByIntervalID(intervalIDs[index]), 0);
		debts[index] = Utils.getRow(debtDB.getDebtsByIntervalID(intervalIDs[index]), 0);
		assert intervalType[index] == dbStructure.RENT || intervalType[index] == dbStructure.BOOK;

		Date dateFrom = new Date(intervalData[index][Interval.DATE_FROM]);
		Date dateTo = new Date(intervalData[index][Interval.DATE_TO]);
		
		String startDate = DateView.toAnketa(dateFrom);
		String finishDate = DateView.toAnketa(dateTo);
		TextView flat = (TextView)page.findViewById(R.id.flat);
		TextView dates = (TextView)page.findViewById(R.id.dates);

		String flatText = context.getResources().getString(R.string.flat_calendar_header_flat) + FlatID+"    ("+dateFrom.daysUntil(dateTo)+")";
		flat.setText(flatText);

		String datesText = startDate+" - "+finishDate;
		dates.setText(datesText);
		
		/* initializes type text */
		initTypeInfo(page,index);
		
		framesContainer[index] = (LinearLayout) page.findViewById(R.id.container);
		if(isAnketaFilled(index))initViewPage(page,index);
		else {
			initEditPage(page,index);
			if (index == 0){
				int resID = getResources().getIdentifier(AnketaPagerEditFrame.PREFIX+"tel_left", "id", Config.PACKAGE_NAME);
				EditText telephoneLeft = (EditText) framesContainer[index].findViewById(resID);
				Utils.showKeyboardOnCreate(telephoneLeft);
			}
			
		}
	}
	
	private boolean isAnketaFilled(int index){
		if (intervalAnketaData[index][IntervalAnketa.TELEPHONE_LEFT].length() > 0) return true;
		if (intervalAnketaData[index][IntervalAnketa.TELEPHONE_RIGHT].length() > 0) return true;
		if (intervalAnketaData[index][IntervalAnketa.NAME_LEFT].length() > 0) return true;
		if (intervalAnketaData[index][IntervalAnketa.NAME_RIGHT].length() > 0) return true;
		if (intervalAnketaData[index][IntervalAnketa.SETTLEMENT].length() > 0) return true;
		if (intervalAnketaData[index][IntervalAnketa.EVICTION].length() > 0) return true;
		if (intervalAnketaData[index][IntervalAnketa.ELSE_INFO].length() > 0) return true;
		if (intervalAnketaData[index][IntervalAnketa.ANKETA].length() > 0) return true;
		if (payments[index] != null && payments[index][Payment.PRICE] > 0) return true;
		if (payments[index] != null && payments[index][Payment.MONEY_SUMM] > 0) return true;
		if (prepays[index] != null && prepays[index][Prepay.MONEY_PREPAY] > 0) return true;
		if (debts[index] != null && debts[index][Debt.MONEY_DEBT] > 0) return true;
		return false;
	}
	
	
	/**
	 * Inits anketa view page
	 * @param page
	 * @param index
	 */
	private void initViewPage(View page,int index){
		assert framesContainer[index] != null;
		
		initViewScrollBackround(page, index);
		framesContainer[index].removeAllViews();
		AnketaPagerFrame frame = new AnketaPagerFrame(page.getContext());
		
		setFrameParams(frame,index);
		
		if (onLongClickLister != null){
			((LinearLayout)page.findViewById(R.id.container)).setOnLongClickListener(onLongClickLister);
		}
		framesContainer[index].addView(frame,0);
		
		pageStates[index] = 0;
		
	}
	
	/**
	 * Initializes type info text on the right part of the screen
	 * @param page
	 * @param index
	 */
	private void initTypeInfo(View page,int index){
		TextView type = (TextView)page.findViewById(R.id.type);
		type.setText(context.getResources().getString((intervalType[index] == dbStructure.RENT) ? R.string.anketa_type_rent : R.string.anketa_type_book));		
	}
	
	/**
	 * Initializes ScrollView's background depending on anketa's type 
	 * @param page
	 * @param index
	 */
	private void initViewScrollBackround(View page, int index) {
		ScrollView scroll = (ScrollView) page.findViewById(R.id.scroll);
		scroll.setBackgroundResource((intervalType[index] == dbStructure.RENT) ? R.drawable.anketa_background_gradient_rent : R.drawable.anketa_background_gradient_book);
	}
	
	/**
	 * Initializes ScrollView's background depending on anketa's type 
	 * @param page
	 * @param index
	 */
	private void initEditScrollBackround(View page, int index) {
		ScrollView scroll = (ScrollView) page.findViewById(R.id.scroll);
		scroll.setBackgroundResource((intervalType[index] == dbStructure.RENT) ? R.drawable.anketa_background_gradient_rent : R.drawable.anketa_background_gradient_book);
	}
	
	/**
	 * Inits anketa edit page
	 * @param page
	 * @param index
	 */
	private void initEditPage(View page, int index){
		assert framesContainer[index] != null;
		
		initEditScrollBackround(page,index);
		framesContainer[index].removeAllViews();
		AnketaPagerEditFrame frame = new AnketaPagerEditFrame(page.getContext());
		
		setFrameParams(frame,index);
		
		framesContainer[index].addView(frame,0);
		pageStates[index] = 1;
	}
	
	private void setFrameParams(Settable frame,int index) {
		frame.setTelephoneLeft(intervalAnketaData[index][IntervalAnketa.TELEPHONE_LEFT]);
		frame.setTelephoneRight(intervalAnketaData[index][IntervalAnketa.TELEPHONE_RIGHT]);
		frame.setNameLeft(intervalAnketaData[index][IntervalAnketa.NAME_LEFT]);
		frame.setNameRight(intervalAnketaData[index][IntervalAnketa.NAME_RIGHT]);
		frame.setPrice((payments[index] != null && payments[index].length > 0) ? payments[index][Payment.PRICE] : 0);
		frame.setSumm((payments[index] != null && payments[index].length > 0) ? payments[index][Payment.MONEY_SUMM] : 0);
		frame.setDebt((debts[index] != null) ? debts[index][Debt.MONEY_DEBT] : 0);
		frame.setPrepay((prepays[index] != null) ? prepays[index][Prepay.MONEY_PREPAY] : 0);
		frame.setSettlement(intervalAnketaData[index][IntervalAnketa.SETTLEMENT]);
		frame.setEviction(intervalAnketaData[index][IntervalAnketa.EVICTION]);
		frame.setElseInfo(intervalAnketaData[index][IntervalAnketa.ELSE_INFO]);
		frame.setAnketa(intervalAnketaData[index][IntervalAnketa.ANKETA]);
	}
	
	public void convertType(int index){
		if (intervalType[index] == dbStructure.BOOK){
			intervalType[index] = dbStructure.RENT;
		}
		else if (intervalType[index] == dbStructure.RENT){
			intervalType[index] = dbStructure.BOOK;
		}
		initTypeInfo(pages.get(index),index);
		if(pageStates[index] == 0)initViewScrollBackround(pages.get(index),index);
		if(pageStates[index] == 1)initEditScrollBackround(pages.get(index),index);
	}

	private void initPager(){
		pagerAdapter = new SamplePagerAdapter(pages);
		this.setAdapter(pagerAdapter);
		this.setOnPageChangeListener(new OnPageChangeListener(){
			@Override
			public void onPageSelected(int position) {
				currentPage = position;
				if (onPageSelectedListener != null) onPageSelectedListener.onPageSelected(position);
			}
			
			@Override public void onPageScrollStateChanged(int arg0) {}
			@Override public void onPageScrolled(int arg0, float arg1, int arg2) {}
		});
	}

	
	/*------------------------------------------------------------
	-------------------------- G E T T E R S ---------------------
	------------------------------------------------------------*/
	public int getCurrentPage(){
		return this.currentPage;
	}
	
	public Date getDateFrom(int index){
		return new Date(intervalData[index][Interval.DATE_FROM]);
	}
	
	public Date getDateTo(int index){
		return new Date(intervalData[index][Interval.DATE_TO]);
	}
	
	/*------------------------------------------------------------
	-------------------------- S E T T E R S ---------------------
	------------------------------------------------------------*/
	public void setOnPageSelectedListener(OnPageSelectedListener l){
		this.onPageSelectedListener = l;
	}
	
	public void setOnModeChangedListener(OnModeChangedListener l){
		this.mOnModeChangedListener = l;
	}
	
	public void setOnLongClickListener(OnLongClickListener l){
		this.onLongClickLister = l;
		if (pages != null){
			for (View page : pages){
				((LinearLayout)page.findViewById(R.id.container)).setOnLongClickListener(onLongClickLister);
			}
		}
	}
	
	public void setOnLoadListener(OnLoadListener l){
		mOnLoadListener = l;
	}
	
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	protected void switchToEdit(){
		initEditPage(pages.get(currentPage),currentPage);
	}
	
	protected void switchToView(){
		initViewPage(pages.get(currentPage),currentPage);
	}
	
	@Override
	public void save() {
		AnketaPagerEditFrame frame = (AnketaPagerEditFrame) framesContainer[currentPage].getChildAt(0);
		assert frame != null;
		frame.accept(this);
		switchToView();
	}
	
	@Override
	public void visit(AnketaPagerEditFrame frame) {
		ArrayList<Integer> changedParamIDs = new ArrayList<Integer>();
		ArrayList<String> changedParamValues = new ArrayList<String>();
				
		saveCheckTelephoneLeft(frame, changedParamIDs, changedParamValues);
		saveCheckTelephoneRight(frame, changedParamIDs, changedParamValues);
		saveCheckNameLeft(frame, changedParamIDs, changedParamValues);
		saveCheckNameRight(frame, changedParamIDs, changedParamValues);
		saveCheckSettlement(frame, changedParamIDs, changedParamValues);
		saveCheckEviction(frame, changedParamIDs, changedParamValues);
		saveCheckElseInfo(frame, changedParamIDs, changedParamValues);
		saveCheckAnketa(frame, changedParamIDs, changedParamValues);
		
		if (changedParamIDs.size() > 0 && changedParamValues.size() > 0 && changedParamIDs.size() == changedParamValues.size()){
			db.open();
			intervalAnketaDB.updateByID(
				IntervalAnketa.IID,
				Utils.Int(intervalAnketaData[currentPage][IntervalAnketa.IID]),
				Utils.toInt(Utils.arrayListToArray(changedParamIDs)),
				Utils.arrayListToArray(changedParamValues)
			);
			db.close();
		}
		db.open();
		boolean price = savePrice(frame);
		boolean summ = saveSumm(frame);
		boolean debt = saveDebt(frame);
		boolean prepay = savePrepay(frame);
		if(debt || prepay){
			int[] iData = intervalDB.getData(intervalIDs[currentPage]);
			Date dateFrom = new Date(iData[Interval.DATE_FROM]);
			Date dateTo = new Date(iData[Interval.DATE_TO]);
			
			BroadcastSender.send(context, FlatActivity.ACTIVITY_TAG,
					new String[]{Dialog.ACTION_DO,Dialog.PARAM_FLAT_ID,Dialog.PARAM_FIRSTDATE,Dialog.PARAM_LASTDATE},
					new String[]{"refresh_flat",""+FlatID,dateFrom.toString(),dateTo.toString()});
			BroadcastSender.send(context, SausageActivity.ACTIVITY_TAG,
					new String[]{Dialog.ACTION_DO,Dialog.PARAM_FLAT_ID,Dialog.PARAM_FIRSTDATE,Dialog.PARAM_LASTDATE},
					new String[]{"refresh_flat",""+FlatID,dateFrom.toString(),dateTo.toString()});
		}
		if (price || summ){
			BroadcastSender.send(context, SausageActivity.ACTIVITY_TAG, new String[]{"do"}, new String[]{"refresh_warnings"});
		}
		db.close();
	}
	
	
	/*------------------------------------------------------------
	------------------------- P R I V A T E ----------------------
	------------------------------------------------------------*/
	/**
	 * Saves debt and takes controll of exceptions
	 * @param frame
	 */
	private boolean savePrepay(AnketaPagerEditFrame frame){
		int prepay = (prepays[currentPage] != null) ? prepays[currentPage][Prepay.MONEY_PREPAY] : 0;
		int prepayOld = prepay;
		
		try {
			prepay = frame.getPrepay();
		} catch (MoneyParseException e) {
			switch (e.TYPE){
				case MoneyParseException.NUMERIC:{
					Print.toast(context, "Задаток должен состоять из цифр!");
					break;
				}
				case MoneyParseException.NEGATIVE:{
					Print.toast(context, "Задаток должен быть положительным!");
					break;
				}
			}
		}
		if (prepay != prepayOld){
			if (prepays[currentPage] != null){
				if (prepay == 0){
					moneyDB.removeMoney(prepays[currentPage][Prepay.MID]);
					prepays[currentPage] = null;
					return true;
				}
				prepayDB.updatePrepay(prepays[currentPage][Prepay.MID], prepay);
				prepays[currentPage][Prepay.MONEY_PREPAY] = prepay;
				return true;
			}
			else {
				assert prepay > 0;
				prepayDB.addPrepay(intervalIDs[currentPage], prepay);
				prepays[currentPage] = Utils.getRow(prepayDB.getPrepaysByIntervalID(intervalIDs[currentPage]),0);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Saves debt and takes controll of exceptions
	 * @param frame
	 */
	private boolean saveDebt(AnketaPagerEditFrame frame){
		int debt = (debts[currentPage] != null) ? debts[currentPage][Debt.MONEY_DEBT] : 0;
		int debtOld = debt;
		
		try {
			debt = frame.getDebt();
		} catch (MoneyParseException e) {
			switch (e.TYPE){
				case MoneyParseException.NUMERIC:{
					Print.toast(context, "Долг должен состоять из цифр!");
					break;
				}
				case MoneyParseException.NEGATIVE:{
					Print.toast(context, "Долг должен быть положительным!");
					break;
				}
			}
		}
		if (debt != debtOld){
			if (debts[currentPage] != null){
				if (debt == 0){
					moneyDB.removeMoney(debts[currentPage][Debt.MID]);
					debts[currentPage] = null;
					return true;
				}
				debtDB.updateDebt(debts[currentPage][Debt.MID], debt);
				debts[currentPage][Debt.MONEY_DEBT] = debt;
				return true;
			}
			else {
				assert debt > 0;
				debtDB.addDebt(intervalIDs[currentPage], debt);
				debts[currentPage] = Utils.getRow(debtDB.getDebtsByIntervalID(intervalIDs[currentPage]),0);
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Saves price and takes controll of exceptions
	 * @param frame
	 */
	private boolean savePrice(AnketaPagerEditFrame frame) {
		int price = payments[currentPage][Payment.PRICE];
		
		try {
			price = frame.getPrice();
		} catch (MoneyParseException e) {
			switch (e.TYPE){
				case MoneyParseException.NUMERIC:{
					Print.toast(context, "Цена должна состоять из цифр!");
					break;
				}
				case MoneyParseException.NEGATIVE:{
					Print.toast(context, "Цена должна быть положительной!");
					break;
				}
			}
		}
		if (price != payments[currentPage][Payment.PRICE]){
			payments[currentPage][Payment.PRICE] = price;
			paymentDB.updatePrice(payments[currentPage][Payment.MID], price);
			return true;
		}
		return false;
	}
	
	/**
	 * Saves summ and takes controll of exceptions
	 * @param frame
	 */
	private boolean saveSumm(AnketaPagerEditFrame frame) {
		int summ = payments[currentPage][Payment.MONEY_SUMM];
		
		try {
			summ = frame.getSumm();
		} catch (MoneyParseException e) {
			switch (e.TYPE){
				case MoneyParseException.NUMERIC:{
					Print.toast(context, "Сумма должна состоять из цифр!");
					break;
				}
				case MoneyParseException.NEGATIVE:{
					Print.toast(context, "Сумма должна быть положительной!");
					break;
				}
			}
		}
		if (summ != payments[currentPage][Payment.MONEY_SUMM]){
			paymentDB.updateSumm(payments[currentPage][Payment.MID], summ);
			payments[currentPage][Payment.MONEY_SUMM] = summ;
			return true;
		}
		return false;
	}
	/**
	 * Check if it's neccessary to save telephone left
	 * @param frame
	 * @param ids
	 * @param values
	 */
	private void saveCheckTelephoneLeft(AnketaPagerEditFrame frame,ArrayList<Integer> ids,ArrayList<String> values) {
		saveCheckChangeParamValue(ids, values, frame.getTelephoneLeft(), IntervalAnketa.TELEPHONE_LEFT);
	}
	
	/**
	 * Check if it's neccessary to save telephone right
	 * @param frame
	 * @param ids
	 * @param values
	 */
	private void saveCheckTelephoneRight(AnketaPagerEditFrame frame,ArrayList<Integer> ids,ArrayList<String> values) {
		saveCheckChangeParamValue(ids, values, frame.getTelephoneRight(), IntervalAnketa.TELEPHONE_RIGHT);
	}
	
	/**
	 * Check if it's neccessary to save name left
	 * @param frame
	 * @param ids
	 * @param values
	 */
	private void saveCheckNameLeft(AnketaPagerEditFrame frame,ArrayList<Integer> ids,ArrayList<String> values) {
		saveCheckChangeParamValue(ids, values, frame.getNameLeft(), IntervalAnketa.NAME_LEFT);
	}
	
	/**
	 * Check if it's neccessary to save name right
	 * @param frame
	 * @param ids
	 * @param values
	 */
	private void saveCheckNameRight(AnketaPagerEditFrame frame,ArrayList<Integer> ids,ArrayList<String> values) {
		saveCheckChangeParamValue(ids, values, frame.getNameRight(), IntervalAnketa.NAME_RIGHT);
	}
	
	/**
	 * Check if it's neccessary to save settlement
	 * @param frame
	 * @param ids
	 * @param values
	 */
	private void saveCheckSettlement(AnketaPagerEditFrame frame,ArrayList<Integer> ids,ArrayList<String> values) {
		saveCheckChangeParamValue(ids, values, frame.getSettlement(), IntervalAnketa.SETTLEMENT);
	}
	
	/**
	 * Check if it's neccessary to save eviction
	 * @param frame
	 * @param ids
	 * @param values
	 */
	private void saveCheckEviction(AnketaPagerEditFrame frame,ArrayList<Integer> ids,ArrayList<String> values) {
		saveCheckChangeParamValue(ids, values, frame.getEviction(), IntervalAnketa.EVICTION);
	}
	
	/**
	 * Check if it's neccessary to save else info
	 * @param frame
	 * @param ids
	 * @param values
	 */
	private void saveCheckElseInfo(AnketaPagerEditFrame frame,ArrayList<Integer> ids,ArrayList<String> values) {
		saveCheckChangeParamValue(ids, values, frame.getElseInfo(), IntervalAnketa.ELSE_INFO);
	}
	
	/**
	 * Check if it's neccessary to save anketa
	 * @param frame
	 * @param ids
	 * @param values
	 */
	private void saveCheckAnketa(AnketaPagerEditFrame frame,ArrayList<Integer> ids,ArrayList<String> values) {
		saveCheckChangeParamValue(ids, values, frame.getAnketa (), IntervalAnketa.ANKETA);
	}
	
	/**
	 * Checks if it's neccessary to save interval param, and if yes,
	 * add's it's ID and value to save list
	 * @param ids
	 * @param values
	 * @param value
	 * @param id
	 */
	private void saveCheckChangeParamValue(ArrayList<Integer> ids, ArrayList<String> values, String value, int id) {
		if (!value.equals(intervalAnketaData[currentPage][id])){
			ids.add(id);
			values.add(value);
			intervalAnketaData[currentPage][id] = value;
		}
	}
}
