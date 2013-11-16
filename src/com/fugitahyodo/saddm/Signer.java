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
	
	private String fileSelected = "";
		
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
	
	public void sign(View view) {
		boolean validacao = true;
		String pass = ((EditText) findViewById(R.id.signer_campo_senha)).getText().toString();
		String passConf = ((EditText) findViewById(R.id.signer_campo_confirma_senha)).getText().toString();
		String passAle = ((EditText) findViewById(R.id.signer_campo_senha_aleatoria)).getText().toString();
		System.out.println(pass + " " + passConf);
		PasswordValidator valPass = new PasswordValidator(pass);
		
		if(!valPass.isPassOk()) {
			validacao = false;
			Toast.makeText(this,"A senha não está correta", Toast.LENGTH_SHORT).show();
			return;
		} else if (!pass.equals(passConf)) {
			validacao = false;
			Toast.makeText(this,"Confirmação de senha não confere", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(fileSelected.equals("")) {
			validacao = false;
			Toast.makeText(this, "Nenhum arquivo foi selecionado", Toast.LENGTH_SHORT).show();
		} 
		
		if(validacao) {
			GerenciadorCP gerCP = new GerenciadorCP();
			gerCP.sign(fileSelected, pass, passAle);
			Toast.makeText(this, "Arquivo Assinado", Toast.LENGTH_SHORT).show();
		} else {
			validacao = true;
		}
	}
	

	public void voltar(View view) {
		Intent intent = new Intent(Signer.this, MainActivity.class);
		startActivity(intent);
	}
	
	
}
