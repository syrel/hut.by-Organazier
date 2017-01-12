package by.hut.flat.calendar.core.DBAdapter.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.utils.Print;

public class Table implements ITable {
	private final String tableName;
	private final String createScript;
	private final String[] tableStructure;
	protected final DBAdapter db;
	protected final boolean initOpen;
	private boolean exist;

	protected boolean invariant(){
		return this.createScript != null
				&& this.tableName != null
				&& this.tableStructure != null
				&& this.createScript.length() > 0
				&& this.tableName.length() > 0
				&& this.tableStructure.length > 0
				&& this.db != null;
	}
	
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	
	protected Table(DBAdapter adapter, String tableName, String createScript, String[] tableStructure){
		this.tableName = tableName;
		this.createScript = createScript;
		this.tableStructure = tableStructure;
		this.db = adapter;
		this.initOpen = db.isOpen();
		assert invariant();
		init();
	}
	
	private void init(){
		db.open();
		this.initIsExist();
		if(!initOpen) db.close();
	}

    private void initIsExist() {
        Cursor cursor = db.getDB().rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '" + this.tableName + "'", null);
        if (cursor.getCount() > 0) {
            this.exist = true;
            cursor.close();
            return;
        }
        cursor.close();
        this.exist = false;
    }

    /*------------------------------------------------------------
    ------------------------- G E T T E R S ----------------------
    ------------------------------------------------------------*/
	@Override
	public boolean isExist(){
		assert invariant();
		return this.exist;
	}
	@Override
	public String getName(){
		assert invariant();
		return this.tableName;
	}
	
	protected DBAdapter getDB(){
		assert invariant();
		return this.db;
	}
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	/**
	 * 
	 */
	@Override
	public void create() throws TableAlreadyExistsException, QuerySyntaxException{
		assert invariant();
		if (this.isExist())	throw new TableAlreadyExistsException(this);
		db.open();
		db.getDB().execSQL("PRAGMA foreign_keys=ON;");
		db.getDB().execSQL(createScript);
		initIsExist();
		if (!this.isExist()) throw new QuerySyntaxException(this,createScript);
		if(!initOpen)db.close();
		assert invariant();
	}
	/**
	 * 
	 */
	@Override
	public void drop() throws TableNotExistsException, QuerySyntaxException{
		assert invariant();
		if (!this.isExist()) throw new TableNotExistsException(this);
		String query = "DROP TABLE IF EXISTS " + this.tableName;
		db.open();
		db.exec(query);
		initIsExist();
		if (this.isExist()) throw new QuerySyntaxException(this,query);
		if(!initOpen)db.close();
		assert invariant();
	}
	/*------------------------------------------------------------
	---------------------- I N S E R T I O N S -------------------
	------------------------------------------------------------*/
	/**
	 * 
	 */
	@Override
	public void insert() throws TableNotExistsException{};
	
	/**
	 * 
	 */
	@Override
	public long insert(int[] paramIDs, String[] paramValues){
		assert invariant();
		assert paramIDs != null;
		assert paramIDs.length > 0;
		assert paramValues != null;
		assert paramValues.length > 0;
		assert paramIDs.length == paramValues.length;
		if (!this.isExist()) Print.log(new TableNotExistsException(this).getStackTrace());
		ContentValues contentValues = new ContentValues();
		for (int i = 0; i < paramIDs.length; i++){
			assert paramIDs[i] < this.tableStructure.length;
			assert paramIDs[i] >= 0;
			contentValues.put(this.tableStructure[paramIDs[i]], paramValues[i]);
		}
		assert contentValues.size() > 0;
		db.open();
		long index = db.getDB().insert(this.tableName, null,contentValues);
		if(!initOpen)db.close();
		return index;
	}
	
	/**
	 * @return 
	 */
	@Override
	public long[] bulkInsert(int[] paramIDs, Object[][] paramValues, String dataType) throws TableNotExistsException {
		assert invariant();
		assert paramIDs != null;
		assert paramIDs.length > 0;
		assert paramValues != null;
		assert paramValues.length > 0;
		assert paramIDs.length == paramValues[0].length;
		assert dataType.equals("int") || dataType.equals("String");
		int type = dataType.equals("int") ? 0 : 1;
		int resultLength = paramValues[0].length;
		long[] resultIDs = new long[resultLength];
		
		if (!this.isExist())
            throw new TableNotExistsException(this);
		db.open();
		db.getDB().beginTransaction();
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO ");
			sql.append(this.tableName);
			sql.append(" (");
			StringBuilder names = new StringBuilder();
			StringBuilder params = new StringBuilder();
			for (int i = 0; i < paramIDs.length; i++){
				if (i != 0) {
					names.append(',');
					params.append(',');
				}
				names.append(this.tableStructure[paramIDs[i]]);
				params.append('?');
			}
			sql.append(names);
			sql.append(") VALUES(");
			sql.append(params);
			sql.append(')');
			SQLiteStatement statement = db.getDB().compileStatement(sql.toString());
			for (int i = 0; i < paramValues[0].length; i++){
				for (int j = 1; j <= paramIDs.length; j++){
					if (type == 0) statement.bindLong(j, ((Integer)paramValues[j-1][i]).longValue());
					else statement.bindString(j, (String)paramValues[j-1][i]);
				}
				resultIDs[i] = statement.executeInsert();
			}
			db.getDB().setTransactionSuccessful();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally {
			db.getDB().endTransaction();
		}
		if(!initOpen)
            db.close();
		return resultIDs;
	}

	@Override
	public void updateByID(int keyID, int keyValue, int[] paramIDs, String[] paramValues) {
		assert keyValue > 0;
		assert paramIDs.length > 0;
		assert paramValues.length > 0;
		assert paramIDs.length == paramValues.length;
		db.open();
		ContentValues args = new ContentValues();
		for (int i = 0; i < paramIDs.length; i++){
			args.put(tableStructure[paramIDs[i]], paramValues[i]);
		}
		db.getDB().update(tableName, args, tableStructure[keyID]+" = ?", new String[]{""+keyValue});
		if(!initOpen)db.close();
	}
	
	@Override
	public void removeByID(int keyID, int keyValue) {
		assert keyValue > 0;
		db.open();
		db.getDB().delete(tableName, tableStructure[keyID]+" = ?", new String[]{""+keyValue});
		if(!initOpen)db.close();
	}
	
	public void removeByID(int keyID, int[] keyValues){
		assert keyID >= 0;
		assert keyValues != null;
		assert keyValues.length > 0;
		db.open();
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("DELETE FROM ");
		strBuilder.append(tableName);
		strBuilder.append(" WHERE ");
		strBuilder.append(tableStructure[keyID]);
		strBuilder.append(" IN (");
		strBuilder.append(Print.toString(keyValues, ','));
		strBuilder.append(")");
		db.getDB().execSQL(strBuilder.toString());
		if(!initOpen)db.close();
	}
	/*------------------------------------------------------------
	---------------------- S E L E C T I O N S -------------------
	------------------------------------------------------------*/
	
	/*------------------------------------------------------------
	----------------------- P R O T E C T E D --------------------
	------------------------------------------------------------*/
	/**
	 * 
	 * @param cursor
	 * @return
	 */
	public static String[][] getResultString(Cursor cursor) {
		int colsNum = cursor.getColumnCount();

		List<String[]> resultList = new ArrayList<>();
		if (cursor.moveToFirst()) {
            do {
                String[] row = new String[colsNum];
            	for (int col = 0; col < colsNum; col++){
                    row[col] = cursor.getString(col);
            	}
                resultList.add(row);
			} while (cursor.moveToNext());
        }
		cursor.close();
		return resultList.toArray(new String[0][0]);
	}
	/**
	 * 
	 * @param cursor
	 * @return
	 */
	public static int[][] getResultInt(Cursor cursor) {
		int rowNum = cursor.getCount();
		int colsNum = cursor.getColumnCount();
		if (rowNum < 1) {
			cursor.close();
			return new int[0][0];
		}
		int[][] result = new int[rowNum][colsNum];
		if (cursor.moveToFirst()) {
			int row = 0;
            do {
            	for (int col = 0; col < colsNum; col++){
            		result[row][col] = cursor.getInt(col);
            	}
                row++;
            } while (cursor.moveToNext());
        }
		cursor.close();
		return result;
	}
}