package br.com.saddm.gerenciador;

import br.com.saddm.SaddmActivity;

import com.fugitahyodo.saddm.R;
import com.fugitahyodo.saddm.R.layout;
import com.fugitahyodo.saddm.R.menu;

import android.os.Bundle;
import android.view.Menu;

public class GerenciadorPrincipal extends SaddmActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gerenciador_principal);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
}
