package com.fugitahyodo.saddm;

import java.util.Random;

import br.com.saddm.gerenciador.GerenciadorArquivos;
import br.com.saddm.gerenciador.GerenciadorGeracaoChavePub;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class GerenciadorCadastroSenhaAleatoria extends SaddmActivity {
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gerenciador_cadastro_senha_aleatoria);

		//		Intent intent = getIntent();
		//		String chaveAleatoria = intent.getStringExtra(Constants.CHAVE_ALEATORIA);
		String chaveAleatoria = "";  
		
		TextView textViewChaveAleatoria = (TextView) findViewById(R.id.gerenciador_cadastro_chave_aleatoria_chave_text);
		if(!chaveAleatoria.equals("")) {
			textViewChaveAleatoria.setText(chaveAleatoria);
		} else {
			textViewChaveAleatoria.setText("Pressione Gerar Nova Senha");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void gerarAleatorio(View view) {
		String chaveAleatoria = "";
		char c;
		int i, op;
		
		Random random = new Random();
		
		for(i = 0; i < 4; i++) {
			op = random.nextInt(2);
			if (op == 0) {
				c = (char) ( random.nextInt(9) + 48 ); // gera numero
			} else if (op == 1) {
				c = (char) ( random.nextInt(25) + 97 );  // gera letra minuscula
			} else {
				c = (char) ( random.nextInt(25) + 65 );  // gera letra maiuscula
			}
				chaveAleatoria = chaveAleatoria + c;
		}
		
		TextView textViewChaveAleatoria = (TextView) findViewById(R.id.gerenciador_cadastro_chave_aleatoria_chave_text);
		textViewChaveAleatoria.setText(chaveAleatoria);
	}

	public void voltar(View view) {
		Intent intent = new Intent(GerenciadorCadastroSenhaAleatoria.this, MainActivity.class);
		startActivity(intent);
	}

	public void confirmar(View view) {
		Intent intent = new Intent(GerenciadorCadastroSenhaAleatoria.this, GerenciadorGeracaoChavePub.class);
		startActivity(intent);
	}
}
