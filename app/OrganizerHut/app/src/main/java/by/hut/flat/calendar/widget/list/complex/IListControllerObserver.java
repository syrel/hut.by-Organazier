package by.hut.flat.calendar.widget.list.complex;

public interface IListControllerObserver {
	public void notifyAddEntry(Entry entry);
	public void notifyEditSaveEntry();
	public void notifyEditEntry(Entry entry);
}
