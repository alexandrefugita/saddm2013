package com.fugitahyodo.saddm;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends SaddmActivity {

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
}
