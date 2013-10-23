package com.fugitahyodo.saddm;

import java.text.DateFormat;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class GerenciadorCadastroChaveAleatoria extends SaddmActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gerenciador_cadastro_chave_aleatoria);

		//		Intent intent = getIntent();
		//		String chaveAleatoria = intent.getStringExtra(Constants.CHAVE_ALEATORIA);

		// usar metodo
		String chaveAleatoria = "asdfihasdiouahsfasdvffsagaergsra";

		TextView textViewChaveAleatoria = (TextView) findViewById(R.id.gerenciador_cadastro_chave_aleatoria_chave);
		textViewChaveAleatoria.setText(chaveAleatoria);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void gerarAleatorio(View view) {
		String chaveAleatoria = new Date().toString();

		TextView textViewChaveAleatoria = (TextView) findViewById(R.id.gerenciador_cadastro_chave_aleatoria_chave);
		textViewChaveAleatoria.setText(chaveAleatoria);
	}

	public void voltar(View view) {

	}

	public void confirmar(View view) {

	}
}
