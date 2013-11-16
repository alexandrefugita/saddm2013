package com.fugitahyodo.saddm;

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
		PasswordValidator passValidator;
		CPFValidator	  cpfValidator;
			
		EditText nome = (EditText) findViewById(R.id.gerenciador_cadastro_campo_nome);
		EditText cpf = (EditText) findViewById(R.id.gerenciador_cadastro_campo_cpf);
		EditText dataNascimento = (EditText) findViewById(R.id.gerenciador_cadastro_campo_datanasc);
		EditText senha = (EditText) findViewById(R.id.gerenciador_cadastro_campo_senha);
		EditText confirmaSenha = (EditText) findViewById(R.id.gerenciador_cadastro_campo_confirmasenha);

		passValidator =  new PasswordValidator(senha.getText().toString());
		cpfValidator = new CPFValidator(cpf.getText().toString());
		
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

		//Validacao de senha
		// Tamanho e padrão
		if (!passValidator.isPassOk()) {
			validacao = false;
			Toast.makeText(GerenciadorCadastro.this,
					passValidator.getMessage(), Toast.LENGTH_SHORT).show();

		} else if (!senha.getText().toString()
				.equals(confirmaSenha.getText().toString())) {
			validacao = false;
			Toast.makeText(GerenciadorCadastro.this,
					"Confirmação de senha não confere",
					Toast.LENGTH_SHORT).show();

		}
		
		//Se todos os campos são validos, continua  processo de cadastro
		if (validacao) {
			Toast.makeText(GerenciadorCadastro.this, "OK!!!!!!111",
					Toast.LENGTH_SHORT).show();

			/// salvar dados do cadastro no sistema
			Editor editor = this.getSharedPreferences(SaddmApplication.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE).edit();
			editor.putString("nome", nome.getText().toString());  //TODO usar o recurso R.string para setar esses nomes
			editor.putString("cpf", cpf.getText().toString());
			editor.putString("dataNascimento", dataNascimento.getText().toString());
			editor.commit();
			
			// Null nos Validator TODO criar class static
			
			
			// Guardar Profile do User
			GerenciadorArquivos.writeUserProfile(nome.getText().toString().replaceAll(" ",""),cpf.getText().toString(),dataNascimento.getText().toString());
			
			// chamar intent do aleatorio passando o parametro
			Intent intent = new Intent(GerenciadorCadastro.this, GerenciadorCadastroChaveAleatoria.class);
//			intent.putExtra(Constants.CHAVE_ALEATORIA, chaveAleatoria);
		    startActivity(intent);
		}
		
		passValidator = null;
		
	}
	
	
	

	private boolean isTamanhoNulo(EditText palavra) {

		if (palavra.getText().length() == 0 || palavra == null) {
			return true;
		}

		return false;
	}
}
