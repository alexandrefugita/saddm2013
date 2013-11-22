package com.fugitahyodo.saddm;

import java.util.ArrayList;

import br.com.thinkti.android.filechooser.FileChooser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SendFile extends Activity{
	private static final int FILE_CHOOSER = 11;
	private String fileSelected;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_file);
	}
	
	public void chooseFile(View view) {
    	Intent intent = new Intent(this, FileChooser.class);
    	ArrayList<String> extensions = new ArrayList<String>();
    	extensions.add(".pdf");
    	extensions.add(".txt");
    	extensions.add(".png");
    	extensions.add(".jpeg");
    	extensions.add(".jpg");
    	intent.putStringArrayListExtra("filterFileExtension", extensions);
    	startActivityForResult(intent, FILE_CHOOSER);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == FILE_CHOOSER) && (resultCode == -1)) {
	    
	    		this.fileSelected = data.getStringExtra("fileSelected");
	    	
	    		//Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
	    }
	}
	
}
