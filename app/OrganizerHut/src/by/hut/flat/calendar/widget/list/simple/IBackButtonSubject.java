package by.hut.flat.calendar.widget.list.simple;

public interface IBackButtonSubject {
	public void registerObserver(IBackButtonObserver observer);
	public void removeObserver(IBackButtonObserver observer);
	public void notifyObserversBackButton();
}
