package by.hut.flat.calendar.core.DBAdapter;

import java.util.ArrayList;

import by.hut.flat.calendar.core.DBAdapter.tables.ITable;
import by.hut.flat.calendar.core.DBAdapter.tables.QuerySyntaxException;
import by.hut.flat.calendar.core.DBAdapter.tables.TableAlreadyExistsException;
import by.hut.flat.calendar.core.DBAdapter.tables.TableNotExistsException;

public class dbUpdate{
	
	private DBAdapter db;
	private dbStructure structure;
	
	private boolean invariant(){
		return db != null;
	}
	/*------------------------------------------------------------
	-------------------- C O N S T R U C T O R S -----------------
	------------------------------------------------------------*/
	public dbUpdate(DBAdapter adapter){
		db = adapter;
		structure = new dbStructure(this.db);
	}
	
	/*------------------------------------------------------------
	------------------------- A C T I O N S ----------------------
	------------------------------------------------------------*/
	/******************************************************************************************/
    /***************************************dbFullFormat***************************************/
	/******************************************************************************************/
	public boolean dbFullFormat() throws TableNotExistsException, TableAlreadyExistsException, QuerySyntaxException{
		alertD("Starting full format...");
		alertD("Beginning deleting all tables...");
		dbTablesDrop();
		alertD(" + All Tables deleted!");
		alertD("Beginning creating all tables...");
		dbTablesCreate();
		alertD(" + All Tables created!");
		alertD("Beginning inserting all data in tables...");
		dbTablesInsert();
		alertD(" + All data inserted!");
		alertD(" * Full format completed!");
		return true;
	}
	/******************************************************************************************/
    /***************************************dbTableCreate**************************************/
	/******************************************************************************************/

	/**
	 * Creates all defined tables from dbStructure class.</br>
	 * @throws TableAlreadyExistsException 
	 * @throws QuerySyntaxException 
	 */
	public void dbTablesCreate() throws TableAlreadyExistsException, QuerySyntaxException{
		assert invariant();
		ArrayList<ITable> tables = structure.getTables();
		for (ITable table : tables){
			table.create();
		}
		assert invariant();
	}
	/******************************************************************************************/
    /***************************************dbTableInsert**************************************/
	/******************************************************************************************/
    /**
     * Inserts default data in all defined tables.</br>
     * <b><i>Uses fast 'bulk' insert method.</i></b>
     * @return {@code True} if operation was succesfull, otherwise {@code False}
     * @throws TableNotExistsException 
     */
	public void dbTablesInsert() throws TableNotExistsException{
		assert invariant();
		ArrayList<ITable> tables = structure.getTables();
		for (ITable table : tables){
			table.insert();
		}
		assert invariant();
	}
    /*************************************************************/
    /*************************dbTableDelete***********************/
    /*************************************************************/
    /**
     * Deletes all tables in database, according to dbStructure class.</br>
     * @throws TableNotExistsException 
     * @throws QuerySyntaxException 
     */
    public void dbTablesDrop() throws TableNotExistsException, QuerySyntaxException{
    	assert invariant();
		ArrayList<ITable> tables = structure.getTables();
		for (ITable table : tables){
			table.drop();
		}
		assert invariant();
    }
    
    /**
     * Checks if there are no tables in db
     * @return
     */
    public boolean isClean(){
    	assert invariant();
		ArrayList<ITable> tables = structure.getTables();
    	boolean clean = true;
    	for (ITable table : tables){
    		if (table.isExist()){
    			clean = false;
    			break;
    		}
    	}
    	return clean;
    }
    /**********************************************************************************/
	/************************************ S T A T I C *********************************/
	/**********************************************************************************/
    
    public void alertD(String log){
    	/*if (ctx != null){
    		BroadcastSender.send(ctx, Dialog.ACTIVITY_TAG, new String[]{"do","log"}, new String[]{"log",log});
    	}
    	else {
    		android.util.Log.d("dbUpdate",log);	
    	}*/
    }
}
