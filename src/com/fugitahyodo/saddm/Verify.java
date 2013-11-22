package com.fugitahyodo.saddm;

import java.util.ArrayList;

import br.com.saddm.gerenciador.GerenciadorCP;
import br.com.saddm.gerenciador.GerenciadorGeracaoChavePub;
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
	private String pubKey;
	private int signaturefile = 0;
		
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
	    	signaturefile = 0;
	    	startActivityForResult(intent, FILE_CHOOSER);

	}
	
	public void chooseSignature(View view) {
    	Intent intent = new Intent(this, FileChooser.class);
    	ArrayList<String> extensions = new ArrayList<String>();
       	extensions.add(".sig");
    	intent.putStringArrayListExtra("filterFileExtension", extensions);
    	signaturefile = 1;
    	startActivityForResult(intent, FILE_CHOOSER);
	}
	
	public void choosePubKey(View view) {
    	Intent intent = new Intent(this, FileChooser.class);
    	ArrayList<String> extensions = new ArrayList<String>();
       	extensions.add(".pub");
    	intent.putStringArrayListExtra("filterFileExtension", extensions);
    	signaturefile = 2;
    	startActivityForResult(intent, FILE_CHOOSER);
	}
	    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == FILE_CHOOSER) && (resultCode == -1)) {
	    	if(signaturefile  == 1)	 {
	    		this.sigSelected = data.getStringExtra("fileSelected");
	    		signaturefile = 0;
	    		//Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
	    	} else if (signaturefile == 0 ) {
	    		this.fileSelected = data.getStringExtra("fileSelected");
	    		signaturefile = 0;
	    	} else if (signaturefile == 2) {
	    		this.pubKey = data.getStringExtra("fileSelected");
	    		signaturefile = 0;
	    	}
	    }
	    		
	}
	
	public void verifica(View view) {
		GerenciadorCP geCP = new GerenciadorCP();
		System.out.println(fileSelected + "\n" +  sigSelected + "\n" + pubKey);
		if(geCP.verifica(fileSelected, sigSelected, pubKey)) {
			System.out.println("Assinatura está okay");
			Toast.makeText(Verify.this,
					"Assinatura é do arquivo selecionado",
					Toast.LENGTH_SHORT).show(); 
		} else {
			System.out.println("Assinatura ou Arquivo não estão certos");
			Toast.makeText(Verify.this,
					"Assinatura, Arquivo Assinado ou Chave Pública não estão certos",
					Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public void voltar(View view) {
		Intent intent = new Intent(Verify.this, MainActivity.class);
		startActivity(intent);
	}

}
