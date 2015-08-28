package by.hut.flat.calendar.widget.list.anketa;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.anketa.AnketaActivity;
import by.hut.flat.calendar.cell.MaidenBackground;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.core.DBAdapter.dbStructure;
import by.hut.flat.calendar.core.DBAdapter.tables.IntervalToday;
import by.hut.flat.calendar.internal.FastBitmapDrawable;
import by.hut.flat.calendar.utils.Dimension;
import by.hut.flat.calendar.utils.Utils;
import by.hut.flat.calendar.widget.list.simple.Entry;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class AnketaEntry extends Entry implements OnClickListener, OnLongClickListener{
	private static final int BOOK_BGCOLOR = 0xff109810;
	private static final int RENT_BGCOLOR = 0xff2885ad;
		
	private String telephoneLeft;
	private String telephoneRight;
	private String nameLeft;
	private String nameRight;
	private String flatAddress;
	private String date;
	private String price;
	private String cost;
	private String type;
	private String settlement;
	private String eviction;
	
	private int background;
	private boolean done;
	
	private Context context;
	private DBAdapter db;
	private int IntervalID;
	
	private AnketaEntryView view;
	private FrameLayout cleaning;
	private Button star;
	private LinearLayout anketa;
	
	public AnketaEntry(Context context,int IntervalID) {
		super(context);
		this.context = context;
		this.IntervalID = IntervalID;
		db = new DBAdapter(context);
		init();
	}
	
	
	private void init(){
		view = new AnketaEntryView(context,this);
		anketa = (LinearLayout) view.findViewById(R.id.anketa);
		cleaning = (FrameLayout) view.findViewById(R.id.cleaning);
		
		anketa.setOnClickListener(this);
		anketa.setOnLongClickListener(this);
		
		star = (Button) view.findViewById(R.id.star);
		star.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View v) {
				db.open();
				IntervalToday itDB = new IntervalToday(db);
				if (done){
					itDB.removeToday(IntervalID);
					done = false;
				}
				else {
					itDB.addToday(IntervalID);
					done = true;
				}
				db.close();				
				setToday(done);
				return false;
			}
		});
		
		ViewTreeObserver vto = view.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				Dimension dimension = new Dimension(cleaning.getWidth(), cleaning.getHeight());
				dimension.filter = false;
				Bitmap bitmap = Bitmap.createBitmap(dimension.width, dimension.height, Bitmap.Config.RGB_565);
				Canvas canvas = new Canvas(bitmap);
				
				Paint paint = new Paint();
				paint.setColor(getBackgroundColor());
				MaidenBackground.draw(canvas,paint,dimension,background);
				
				FastBitmapDrawable background = new FastBitmapDrawable(bitmap);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					cleaning.setBackground(background);
				}
				else {
					cleaning.setBackgroundDrawable(background);
				}
				
				ViewTreeObserver obs = view.getViewTreeObserver();
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					obs.removeOnGlobalLayoutListener(this);
				} else {
					obs.removeGlobalOnLayoutListener(this);
				}
			}
		});
		
		this.setHeaderView(view);
	}

	public void check(){
		if (telephoneLeft.length() == 0 && nameLeft.length() == 0) view.hideTelephoneNameLeft();
		if (telephoneRight.length() == 0 && nameRight.length() == 0) view.hideTelephoneNameRight();
		if (settlement.length() == 0) view.hideSettlement();
		if (eviction.length() == 0) view.hideEviction();
		if (price.length() == 0 || price.equals("0")) view.hidePrice();
		if (cost.length() == 0 || cost.equals("0")) view.hideCost();
	}
	
	public String getTelephoneLeft() {
		return telephoneLeft;
	}

	public void setTelephoneLeft(String telephoneLeft) {
		this.telephoneLeft = telephoneLeft;
		view.initTelephoneLeft();
	}

	public String getTelephoneRight() {
		return telephoneRight;
	}

	public void setTelephoneRight(String telephoneRight) {
		this.telephoneRight = telephoneRight;
		view.initTelephoneRight();
	}

	public String getNameLeft() {
		return nameLeft;
	}

	public void setNameLeft(String nameLeft) {
		this.nameLeft = nameLeft;
		view.initNameLeft();
	}

	public String getNameRight() {
		return nameRight;
	}

	public void setNameRight(String nameRight) {
		this.nameRight = nameRight;
		view.initNameRight();
	}

	public String getFlatAddress() {
		return flatAddress;
	}

	public void setFlatAddress(String flatAddress) {
		this.flatAddress = flatAddress;
		view.initFlatAddress();
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
		view.initDate();
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		if (price.equals("NULL"))price = "";
		this.price = price;
		view.initPrice();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		view.initBackground();
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		if (cost.equals("NULL")) cost = "";
		this.cost = cost;
		view.initCost();
	}
	
	public int getBackgroundColor(){
		if (Utils.Int(this.type) == dbStructure.BOOK){
			return BOOK_BGCOLOR;
		}
		else if (Utils.Int(this.type) == dbStructure.RENT){
			return RENT_BGCOLOR;
		}
		else {
			return 0x00000000;
		}
	}
	
	@Override
	public void onClick(View v) {
		AnketaActivity.show(context, IntervalID,false);
	}

	@Override
	public boolean onLongClick(View v) {
		AnketaActivity.show(context, IntervalID,true);
		return false;
	}

	public String getSettlement() {
		return settlement;
	}

	public void setSettlement(String settlement) {
		this.settlement = settlement;
		view.initSettlement();
	}

	public String getEviction() {
		return eviction;
	}

	public void setEviction(String eviction) {
		this.eviction = eviction;
		view.initEviction();
	}
	
	public void setEventEviction(){
		view.setEventEviction();
	}
	
	public void setEventSettlement(){
		view.setEventSettlement();
	}
	
	public void setMaidenBackground(int background){
		this.background = background;
	}
	
	public void setToday(boolean done){
		this.done = done;
		view.setToday(done);
	}


}
