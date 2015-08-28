package by.hut.flat.calendar.launcher.widget;

import by.hut.flat.calendar.R;
import by.hut.flat.calendar.core.Config;
import by.hut.flat.calendar.root.LinuxShell;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.RemoteViews;

public class TorchWidget extends AppWidgetProvider {

	public static String ACTION_WIDGET_RECEIVER = "ActionReceiverWidget";
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		// Создаем новый RemoteViews
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_torch_layout);

		// Подготавливаем Intent для Broadcast
		Intent active = new Intent(context, TorchWidget.class);
		active.setAction(ACTION_WIDGET_RECEIVER);
		active.putExtra("value", "ein");

		// создаем наше событие
		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context,0, active, 0);

		// регистрируем наше событие
		remoteViews.setOnClickPendingIntent(R.id.torch,actionPendingIntent);

		// обновляем виджет
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		// Ловим наш Broadcast, проверяем и выводим сообщение
		final String action = intent.getAction();
		
		if (ACTION_WIDGET_RECEIVER.equals(action)) {
			SharedPreferences sharedPrefs = context.getSharedPreferences(Config.PACKAGE_NAME, Context.MODE_PRIVATE);
			boolean on = sharedPrefs.getBoolean("widget_torch_state", false);
			if (!on)LinuxShell.execute("echo 1 > /sys/class/camera/flash/rear_flash");
			else LinuxShell.execute("echo 0 > /sys/class/camera/flash/rear_flash");
			if(!on)on = true;
			else on = false;
			Editor editor = sharedPrefs.edit();
			editor.putBoolean("widget_torch_state", on);
			editor.commit();
		}
		super.onReceive(context, intent);
	}

}