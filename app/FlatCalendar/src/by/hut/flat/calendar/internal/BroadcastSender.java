package by.hut.flat.calendar.internal;

import android.content.Context;
import android.content.Intent;

public class BroadcastSender {
	
	public static void send(Context context, String activityName, String[] commands, String[] params){
		assert context != null;
		assert activityName != null;
		assert activityName.length() > 0;
		assert commands.length > 0;
		assert commands.length == params.length;
		
		Intent intent = new Intent(activityName);
		for (int i = 0; i < commands.length; i++){
			intent.putExtra(commands[i],params[i]);
		}
        context.sendBroadcast(intent);
	}
}
