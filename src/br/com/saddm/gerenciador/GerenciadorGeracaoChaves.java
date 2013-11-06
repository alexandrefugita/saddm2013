package br.com.saddm.gerenciador;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.fugitahyodo.saddm.R;
import com.fugitahyodo.saddm.SaddmActivity;
import com.fugitahyodo.saddm.SaddmApplication;


//TODO  esse é controlor
public class GerenciadorGeracaoChaves extends SaddmActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gerenciador_gerador_chaves);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void gerarChaves (View view) {
		SharedPreferences sharedpref = this.getSharedPreferences(SaddmApplication.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
		String name = sharedpref.getString("nome", null);
		String cpf = sharedpref.getString("cpf", null);
		String dataNasc = sharedpref.getString("dataNascimento", null);
		System.out.println(name + "\n" + cpf + "\n" + dataNasc);
	
		GerenciadorCP gerCp = new GerenciadorCP();
		
		gerCp.gerarChaves(name + cpf + dataNasc, "123455678@A");
		
	}
}
