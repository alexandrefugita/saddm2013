package com.fugitahyodo.saddm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;


public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void efetuarLogin(View view) 
	{
	    Intent intent = new Intent(MainActivity.this, GerenciadorPrincipal.class);
	    startActivity(intent);
	}
	public void cadastrar(View view) {
		Intent intent = new Intent(MainActivity.this, GerenciadorCadastro.class);
	    startActivity(intent);
	}
	
	public void sign(View view) {
		Intent intent = new Intent(MainActivity.this, Signer.class);
	    startActivity(intent);
	}
	
	public void verify(View view) {
		Intent intent = new Intent(MainActivity.this, Verify.class);
	    startActivity(intent);
	}
	
	public void senhaAle(View view) {
		Intent intent = new Intent(MainActivity.this, GerenciadorCadastroChaveAleatoria.class);
	    startActivity(intent);
	}
	
}
