package br.com.saddm;

import android.app.Application;
import android.content.Context;

public class SaddmApplication extends Application {
	public static final String SHARED_PREFERENCES_KEY = "SaddmUP";
	
	private static Context context;

	public static Context getContext() {
		return context;
	}
	
	@Override
	public void	onCreate() {
		SaddmApplication.context = getApplicationContext();
		super.onCreate();
	}
}
