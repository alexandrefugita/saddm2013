package br.com.saddm.gerenciador;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Random;

public class GerenciadorCP {
	protected String salt, sSeed;
	protected PrivateKey chavePrivada;
	protected PublicKey chavePublica;
	
	
	public void gerarChaves(String info, String pass) {
		this.criarSalt();
		sSeed = salt + info + pass;
			
		try {
		
			// EC = algoritmo a ser utilizado
			// SunEC = Provider - responsável pela API
			Provider p = Security.getProvider("Sun");
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA",p);
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN"); // source of randomness
		
			random.setSeed(sSeed.getBytes());
			
			keyGen.initialize(256, random); 
			//random = null; // Por segurança destruir o random depois de gerar as chaves
			
			KeyPair pair = keyGen.generateKeyPair(); 
			
			PrivateKey chavePrivada = pair.getPrivate();
			PublicKey chavePublica = pair.getPublic();
			
			System.out.println(chavePrivada);
			System.out.println(chavePublica);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String criarSalt() {
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
	
	//Getters and Setters

}
