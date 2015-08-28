package by.hut.flat.calendar.widget.list.complex;

public interface Visitor {
	public void visit(EntryEditView editView);
	public void visit(EntryAddView addView);
	public void save();
}
