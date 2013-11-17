package com.fugitahyodo.saddm;

import java.util.ArrayList;

import br.com.saddm.gerenciador.GerenciadorCP;
import br.com.saddm.validator.PasswordValidator;
import br.com.thinkti.android.filechooser.FileChooser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Verify extends Activity{
	private static final int FILE_CHOOSER = 11;
	
	private String fileSelected;
	private String sigSelected;
	private boolean signaturefile = false;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verify);
		
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
	    	signaturefile = false;
	    	startActivityForResult(intent, FILE_CHOOSER);

	}
	
	public void chooseSignature(View view) {
    	Intent intent = new Intent(this, FileChooser.class);
    	ArrayList<String> extensions = new ArrayList<String>();
       	extensions.add(".sig");
    	intent.putStringArrayListExtra("filterFileExtension", extensions);
    	signaturefile = true;
    	startActivityForResult(intent, FILE_CHOOSER);
}
	    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == FILE_CHOOSER) && (resultCode == -1)) {
	    	if(signaturefile)	 {
	    		this.sigSelected = data.getStringExtra("fileSelected");
	    		signaturefile = false;
	    		//Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
	    	} else {
	    		this.fileSelected = data.getStringExtra("fileSelected");
	    	}
	    }
	    		
	}
	
	public void verifica(View view) {
		GerenciadorCP geCP = new GerenciadorCP();
		
		if(geCP.verifica(fileSelected, sigSelected)) {
			System.out.println("Assinatura está okay");
		} else {
			System.out.println("Assinatura ou Arquivo não estão certos");
		}
		
	}
	
	public void voltar(View view) {
		Intent intent = new Intent(Verify.this, MainActivity.class);
		startActivity(intent);
	}

}
