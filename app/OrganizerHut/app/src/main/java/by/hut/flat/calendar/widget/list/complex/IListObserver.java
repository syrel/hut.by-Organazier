package by.hut.flat.calendar.widget.list.complex;

public interface IListObserver {
	public void notifyAddEntryView(EntryView entryView);
	public void notifyGoUp(EntryView entryView);
	public void notifyGoDown(EntryView entryView);
}
