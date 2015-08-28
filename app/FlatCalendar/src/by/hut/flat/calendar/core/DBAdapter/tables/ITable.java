package by.hut.flat.calendar.core.DBAdapter.tables;

public interface ITable {
	/**
	 * 
	 * @return
	 */
	public String getName();
	/**
	 * 
	 * @return
	 */
	public boolean isExist();
	/**
	 * Creates table
	 * @throws TableAlreadyExistsException
	 * @throws QuerySyntaxException 
	 */
	public void create() throws TableAlreadyExistsException, QuerySyntaxException;
	/**
	 * 
	 * @throws TableNotExistsException
	 * @throws QuerySyntaxException
	 */
	public void drop() throws TableNotExistsException, QuerySyntaxException;
	/**
	 * 
	 * @throws TableNotExistsException
	 */
	public void insert() throws TableNotExistsException;
	/**
	 * 
	 * @param paramIDs
	 * @param paramValues
	 * @return
	 * @throws TableNotExistsException
	 */
	public long insert(int[] paramIDs, String[] paramValues) throws TableNotExistsException;
	
	/**
	 * 
	 * @param paramIDs
	 * @param paramValues
	 * @param dataType
	 * @return
	 * @throws TableNotExistsException
	 */
	public long[] bulkInsert(int[] paramIDs, Object[][] paramValues, String dataType) throws TableNotExistsException;
	
	/**
	 * 
	 * @param keyID
	 * @param keyValue
	 * @param paramIDs
	 * @param paramValues
	 */
	public void updateByID(int keyID, int keyValue, int[] paramIDs, String[] paramValues);
	
	/**
	 * 
	 * @param keyID
	 * @param keyValue
	 */
	public void removeByID(int keyID, int keyValue);
}
