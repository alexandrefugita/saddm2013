package br.com.saddm.gerenciador;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import br.com.saddm.MainActivity;
import br.com.saddm.SaddmActivity;
import br.com.saddm.validator.PasswordValidator;

import com.fugitahyodo.saddm.R;


//TODO  esse � controlor
public class GerenciadorGeracaoChavePub extends SaddmActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gerenciador_gerador_chave_pub);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void gerarChaves (View view) {
		boolean validacao = true;
//		SharedPreferences sharedpref = this.getSharedPreferences(SaddmApplication.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
//		String name = sharedpref.getString("nome", null);
//		String cpf = sharedpref.getString("cpf", null);
//		String dataNasc = sharedpref.getString("dataNascimento", null);
		String pass = ((EditText) findViewById(R.id.gerador_chave_campo_senha)).getText().toString();
		String passConf = ((EditText) findViewById(R.id.gerador_chave_campo_senha_confirmacao)).getText().toString();
		String passAle = ((EditText) findViewById(R.id.gerador_chave_campo_senha_aleatoria)).getText().toString();
		
		PasswordValidator passValidator = new PasswordValidator(pass);
		
		//Validacao de senha
		// Tamanho e padr�o
		if (!passValidator.isPassOk()) {
			validacao = false;
			Toast.makeText(GerenciadorGeracaoChavePub.this,
					passValidator.getMessage(), Toast.LENGTH_SHORT).show();

		} else if (!pass.equals(passConf)) {
			validacao = false;
			Toast.makeText(GerenciadorGeracaoChavePub.this,
					"Confirmação de senha não confere",
					Toast.LENGTH_SHORT).show();
		}
		
		if(passAle.length() == 0) {
			validacao = false;
			Toast.makeText(GerenciadorGeracaoChavePub.this,
					"Senha Aleat�ria deve ser preenchida",
					Toast.LENGTH_SHORT).show(); 
		} else if (passAle.length() != 4) {
			validacao = false;
			Toast.makeText(GerenciadorGeracaoChavePub.this,
					"Senha Aleat�ria n�o est� no tamanho correto",
					Toast.LENGTH_SHORT).show(); 
		}
		
		if(validacao) {
			GerenciadorCP gerCp = new GerenciadorCP();
			gerCp.gerarChaves(pass, passAle);
			validacao = false;
			
			Toast.makeText(GerenciadorGeracaoChavePub.this,
					"Gera��o de Chave P�blica executada com sucesso",
					Toast.LENGTH_SHORT).show(); 
		}
	}
	
	public void voltar(View view) {
		Intent intent = new Intent(GerenciadorGeracaoChavePub.this, MainActivity.class);
		startActivity(intent);
	}
}
