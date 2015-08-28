package by.hut.flat.calendar.widget.list.complex;

public interface IListSubject {
	public void registerObserver(IListObserver observer);
	public void removeObserver(IListObserver observer);
	public void notifyObserverAddEntryView(EntryView flatView);
	public void notifyObserverGoUp(EntryView entryView);
	public void notifyObserverGoDown(EntryView entryView);
}
