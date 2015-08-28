package by.hut.flat.calendar.widget.list.simple;

public interface IBackButtonObserver {
	public void registerSubject(IBackButtonSubject subject);
	public void removeSubject(IBackButtonSubject subject);
	public void notifyBackButton();
}
