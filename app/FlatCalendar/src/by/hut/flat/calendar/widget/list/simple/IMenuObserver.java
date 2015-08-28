package by.hut.flat.calendar.widget.list.simple;

public interface IMenuObserver {
	public void notifyEntryAdd(IEntry entry);
	public void notifyEntryHeaderClicked(IEntryView entryView);
	public void notifyToCollapse();
}
