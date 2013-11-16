package br.com.saddm.gerenciador;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.fugitahyodo.saddm.GerenciadorCadastroChaveAleatoria;
import com.fugitahyodo.saddm.MainActivity;
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
//		SharedPreferences sharedpref = this.getSharedPreferences(SaddmApplication.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
//		String name = sharedpref.getString("nome", null);
//		String cpf = sharedpref.getString("cpf", null);
//		String dataNasc = sharedpref.getString("dataNascimento", null);
		EditText pass = (EditText) findViewById(R.id.gerenciador_cadastro_campo_senha);
		GerenciadorCP gerCp = new GerenciadorCP();
		
		gerCp.gerarChaves("12345678@A");
	}
	
	public void testeChave(View view) {
		
	}
	
	public void voltar(View view) {
		Intent intent = new Intent(GerenciadorGeracaoChaves.this, MainActivity.class);
		startActivity(intent);
	}
}
