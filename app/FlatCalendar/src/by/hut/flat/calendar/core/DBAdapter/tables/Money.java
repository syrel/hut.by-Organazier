package by.hut.flat.calendar.core.DBAdapter.tables;

import java.util.Date;

import by.hut.flat.calendar.core.DBAdapter.DBAdapter;

public class Money extends Table {
	public static final String NAME = "money";
	public static final String CREATE = "CREATE TABLE " + NAME + " (" +
			"mid integer," +
			"amount integer," +
			"timestamp integer default '0',"+
			"CONSTRAINT money_pkey PRIMARY KEY (mid)," +
			"CONSTRAINT money_check CHECK (amount >= '0' OR amount IS NULL)" +
		");";
	public static final String[] DB = {
		"mid",				// 0
		"amount",			// 1
		"timestamp"			// 2
	};
	public static final int MID = 0, AMOUNT = 1, TIMESTAMP = 2;
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public Money(DBAdapter adapter) {
		super(adapter, NAME, CREATE, DB);
	}
	
	public long addMoney(int IntervalID,int amount){
		IntervalMoney intervalMoneyDB = new IntervalMoney(db);
		long mid = this.insert(new int[]{AMOUNT,TIMESTAMP}, new String[]{(amount > 0 ) ? ""+amount : "NULL",""+new Date().getTime()});
		intervalMoneyDB.addMoney(IntervalID, (int)mid);
		return mid;
	}
	
	public void updateAmount(int MoneyID, int amount){
		this.updateByID(MID, MoneyID, new int[]{AMOUNT,TIMESTAMP},new String[]{(amount > 0 ) ? ""+amount : "NULL",""+new Date().getTime()});
	}
	
	public void removeMoney(int MoneyID){
		Payment paymentDB = new Payment(db);
		Prepay prepayDB = new Prepay(db);
		Debt debtDB = new Debt(db);
		IntervalMoney imDB = new IntervalMoney(db);
		
		paymentDB.removePayment(MoneyID);
		prepayDB.removePrepay(MoneyID);
		debtDB.removeDebt(MoneyID);
		imDB.removeIntervalMoneyByMoneyID(MoneyID);
		this.removeByID(MID, MoneyID);
	}

}
