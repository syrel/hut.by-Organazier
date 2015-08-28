package by.hut.flat.calendar.core.DBAdapter.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Calendar;

import android.content.Context;
import android.os.Environment;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.internal.Task;

public class BackupCreate extends Task{

	private Context context;
	private DBAdapter db;
	
	public BackupCreate(Context context) {
		super(context);
		this.context = context;
		db = new DBAdapter(this.context);
		db.open();
	}

	private boolean isExternalStorageAvail() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	@Override
	protected Boolean doInBackground(Void... arg0){
		File dbFile = new File(db.getDB().getPath());
		db.close();
		if (!isExternalStorageAvail()) return false;
		Calendar c = Calendar.getInstance();
		String dbName = c.get(Calendar.DAY_OF_MONTH)+"th at "+c.get(Calendar.HOUR_OF_DAY)+"."+c.get(Calendar.MINUTE)+"."+c.get(Calendar.SECOND)+".db";
		File exportDir = new File(Environment.getExternalStorageDirectory(), "FlatCalendar/"+c.get(Calendar.YEAR)+"_"+(c.get(Calendar.MONTH)+1));
		if (!exportDir.exists()) {
			exportDir.mkdirs();
		}
		File file = new File(exportDir, dbName);
		try {
			file.createNewFile();
			this.copyFile(dbFile, file);
			return true;
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private void copyFile(File src, File dst) throws IOException {
		@SuppressWarnings("resource")
		FileChannel inChannel = new FileInputStream(src).getChannel();
		@SuppressWarnings("resource")
		FileChannel outChannel = new FileOutputStream(dst).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		}
		finally {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		}
	}

}
