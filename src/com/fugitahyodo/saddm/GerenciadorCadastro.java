package com.fugitahyodo.saddm;

import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import br.com.saddm.gerenciador.GerenciadorArquivos;
import br.com.saddm.validator.CPFValidator;
import br.com.saddm.validator.PasswordValidator;

public class GerenciadorCadastro extends SaddmActivity {
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
		CPFValidator	  cpfValidator;
			
		String nome =((EditText) findViewById(R.id.gerenciador_cadastro_campo_nome)).getText().toString();
		String cpf = ((EditText) findViewById(R.id.gerenciador_cadastro_campo_cpf)).getText().toString();
		String dataNascimento = ((EditText) findViewById(R.id.gerenciador_cadastro_campo_datanasc)).getText().toString();

		cpfValidator = new CPFValidator(cpf);
		
		//Verifica se o campo nome é nulo
		if (isTamanhoNulo(nome)) {
			validacao = false;
			Toast.makeText(GerenciadorCadastro.this,
					"Nome deve ser preenchido", Toast.LENGTH_SHORT).show();
		}
		
		//Valida o campo cpf
		if (!cpfValidator.isCPF()) {
			validacao = false;
			Toast.makeText(GerenciadorCadastro.this, cpfValidator.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
		
		//Verifica se data de nascimento é nula
		if (isTamanhoNulo(dataNascimento)) {
			validacao = false;
			Toast.makeText(GerenciadorCadastro.this,
					"Data de nascimento deve ser preenchida",
					Toast.LENGTH_SHORT).show();
		}

		//Se todos os campos são validos, continua  processo de cadastro
		if (validacao) {
			Toast.makeText(GerenciadorCadastro.this, "Cadastro executado com sucesso",
					Toast.LENGTH_SHORT).show();

			// Guardar Profile do User
			GerenciadorArquivos.writeUserProfile(nome.replaceAll(" ",""),cpf,dataNascimento);
			GerenciadorArquivos.writeSalt(this.criarSalt());
			
			// chamar intent do aleatorio passando o parametro
			Intent intent = new Intent(GerenciadorCadastro.this, GerenciadorCadastroChaveAleatoria.class);
		    startActivity(intent);
		}		
	}
	
	public void voltar(View view) {
		Intent intent = new Intent(GerenciadorCadastro.this, MainActivity.class);
		startActivity(intent);
	}
	
	private String criarSalt() {
		String salt = "";
		int i, op;
		char c;
		Random random = new Random();
		
		for(i = 0; i < 40; i++) {
			op = random.nextInt(2);
			
			if (op == 0) {
				c = (char) ( random.nextInt(9) + 48 ); // gera numero
			} else if (op == 1) {
				c = (char) ( random.nextInt(25) + 97 );  // gera letra minuscula
			} else {
				c = (char) ( random.nextInt(25) + 65 );  // gera letra maiuscula
			}
				salt = salt + c;
		}
		
		salt = salt + random.nextLong();
		
		return salt;
	}
	
	private boolean isTamanhoNulo(String palavra) {

		if (palavra.length() == 0 || palavra == null) {
			return true;
		}

		return false;
	}
}
