package by.hut.flat.calendar.widget.list.complex;

public interface IEntry {
	/**
	 * 
	 * @return
	 */
	public EntryView getView();

	/**
	 * 
	 * @param subject
	 */
	public void registerEntrySubject(IEntrySubject subject);

	/**
	 * 
	 * @param subject
	 */
	public void removeEntrySubject(IEntrySubject subject);

	/**
	 * 
	 */
	public void clickGoUp();

	/**
	 * 
	 */
	public void clickGoDown();
}
