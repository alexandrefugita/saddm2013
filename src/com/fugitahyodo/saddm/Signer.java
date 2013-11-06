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

public class Signer extends Activity{
	private static final int FILE_CHOOSER = 11;
	
	private String fileSelected;
	
	private String pass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signer);
		
	}
	
	public void chooseFile(View view) {
	    	Intent intent = new Intent(this, FileChooser.class);
	    	ArrayList<String> extensions = new ArrayList<String>();
	    	extensions.add(".pdf");
	    	extensions.add(".txt");
	    	intent.putStringArrayListExtra("filterFileExtension", extensions);
	    	startActivityForResult(intent, FILE_CHOOSER);
	}
	    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == FILE_CHOOSER) && (resultCode == -1)) {
	    		this.fileSelected = data.getStringExtra("fileSelected");
	    		Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
	    	}
	    		
	}
	
	public void sign(View view) {
		this.pass = findViewById(R.id.campo_senha).toString() ;
		System.out.println(pass);
		PasswordValidator valPass = new PasswordValidator(pass);
		if(!valPass.isPassOk()) {
			Toast.makeText(this,"A senha não está correta", Toast.LENGTH_SHORT).show();
			return;
		}
		if(fileSelected.isEmpty()) {
			GerenciadorCP gerCP = new GerenciadorCP();
			gerCP.sign(fileSelected, pass);
		} else {
			Toast.makeText(this, "Nenhum arquivo foi selecionado", Toast.LENGTH_SHORT).show();
			return;
		}
	}
	

	public String getFileSelected() {
		return fileSelected;
	}
	
	
}
