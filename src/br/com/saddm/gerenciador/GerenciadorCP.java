package br.com.saddm.gerenciador;

import java.util.Random;

import android.app.Activity;

public class GerenciadorCP extends Activity{
	int salt;
	
	public GerenciadorCP() {
		this.salt = criarSalt();		
	}
	private int criarSalt() {
		int salt;
		Random randomSalt = new Random();
		
		salt = randomSalt.nextInt(500);
		
		System.out.println(salt);
		return salt;
	}
	public int getSalt() {
		return salt;
	}
	public void setSalt(int salt) {
		this.salt = salt;
	}
	
}
