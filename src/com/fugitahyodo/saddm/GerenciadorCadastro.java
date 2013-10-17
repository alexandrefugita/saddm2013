package com.fugitahyodo.saddm;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class GerenciadorCadastro extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gerenciador_cadastro);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void verificar(View view) {
		boolean validacao = true;
		
		EditText nome = (EditText) findViewById(R.id.gerenciador_cadastro_campo_nome);
		EditText cpf = (EditText) findViewById(R.id.gerenciador_cadastro_campo_cpf);
		EditText dataNascimento = (EditText) findViewById(R.id.gerenciador_cadastro_campo_datanasc);
		EditText senha = (EditText) findViewById(R.id.gerenciador_cadastro_campo_senha);
		EditText confirmaSenha = (EditText) findViewById(R.id.gerenciador_cadastro_campo_confirmasenha);
		
		if (nome.getText().length() == 0) {
			validacao = false;
			Toast.makeText(GerenciadorCadastro.this, "Nome deve ser preenchido",
					Toast.LENGTH_SHORT).show();
		}
		if (cpf.getText().length() == 0) {
			validacao = false;
			Toast.makeText(GerenciadorCadastro.this, "CPF deve ser preenchido",
					Toast.LENGTH_SHORT).show();
		}
		if (dataNascimento.getText().length() == 0) {
			validacao = false;
			Toast.makeText(GerenciadorCadastro.this, "Data de nascimento deve ser preenchida",
					Toast.LENGTH_SHORT).show();
		}
		
		if (senha.getText().length() == 0) {
			validacao = false;
			Toast.makeText(GerenciadorCadastro.this, "Senha deve ser preenchida",
					Toast.LENGTH_SHORT).show();
		}
		if (senha.getText().length() > 0 && senha.getText().length() < 10) {
			validacao = false;
			Toast.makeText(GerenciadorCadastro.this, "Senha deve ter ao menos 10 caracteres",
					Toast.LENGTH_SHORT).show();
		} else {
			// conferir caracteres ascii
			
			if (confirmaSenha.getText().length() == 0) {
				validacao = false;
				Toast.makeText(GerenciadorCadastro.this, "Confirmação de senha deve ser preenchida",
						Toast.LENGTH_SHORT).show();
			}
			if (!senha.getText().toString().equals(confirmaSenha.getText().toString())) {
				validacao = false;
				Toast.makeText(GerenciadorCadastro.this, "Confirmação de senha não confere",
						Toast.LENGTH_SHORT).show();
			}
		}
		
		if (validacao) {
			Toast.makeText(GerenciadorCadastro.this, "OK!!!!!!111",
					Toast.LENGTH_SHORT).show();
			
			// gerar senha
		}
	}
}
