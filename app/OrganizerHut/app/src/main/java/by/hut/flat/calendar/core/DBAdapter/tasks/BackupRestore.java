package by.hut.flat.calendar.core.DBAdapter.tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.content.Context;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.core.DBAdapter.DBAdapter;
import by.hut.flat.calendar.internal.Task;

public class BackupRestore extends Task {

	private Context context;
	private String toBeRestored;
	private String restoredIn;
	
	public BackupRestore(Context context, String toBeRestored) {
		super(context);
		this.context = context;
		DBAdapter db = new DBAdapter(this.context);
		db.open();
		this.restoredIn = db.getDB().getPath();
		db.close();
		this.toBeRestored = toBeRestored;
	}
	
	@Override
	protected Boolean doInBackground(Void... arg0){
		File dbFile = new File(restoredIn);
		File file = new File(toBeRestored);
		try {
			dbFile.createNewFile();
			this.copyFile(file, dbFile);
			Config.INST.reInit();
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
