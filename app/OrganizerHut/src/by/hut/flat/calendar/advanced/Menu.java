package by.hut.flat.calendar.advanced;

import android.content.Context;
import by.hut.flat.calendar.widget.list.simple.Entry;
import by.hut.flat.calendar.widget.list.simple.List;
import by.hut.flat.calendar.widget.list.simple.SubList;

public class Menu extends List{

	private Context context;
	public Menu(Context context) {
		super(context);
		this.context = context;
		this.initList();
	}

	@Override
	protected void initList() {
		Search searchButton = new Search(context);
		this.add(searchButton);
		
		Flats flatsButton = new Flats(context);
		this.add(flatsButton);
		
		SubList employee = new SubList(context,"Персонал");
		Maiden maiden = new Maiden(context);
		Entry manager = new Entry(context,"Менеджеры");
		Entry programmer = new Entry(context,"Программисты");
		Entry director = new Entry(context,"Директор");
		employee.add(maiden);
		employee.add(manager);
		employee.add(programmer);
		employee.add(director);
		
		this.add(employee);
		
		SubList database = new SubList(context,"База данных");
		BackupCreate backup = new BackupCreate(context);
		database.add(backup);
		BackupRestore restore = new BackupRestore(context);
		database.add(restore);
		this.add(database);		
		
		Exit exitButton = new Exit(context);
		this.add(exitButton);
	}

}
