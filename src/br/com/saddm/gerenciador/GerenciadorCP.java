package br.com.saddm.gerenciador;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.util.Random;


public class GerenciadorCP {
	protected String salt, sSeed;
	protected PrivateKey chavePrivada = null;
	protected PublicKey chavePublica;
	
	public void gerarChaves(String info, String pass) {
		this.salt = this.criarSalt();
		sSeed = this.salt + info + pass;
				
		try {
			Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "SC");
			
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "Crypto"); // source of randomness
			// em caso de um seed espeficifico podesse utilziar random.setSeed(seed);
			//exemplo teste random.setSeed(201);
			random.setSeed(sSeed.getBytes());
			
			keyGen.initialize(256, random); 
			KeyPair pair = keyGen.generateKeyPair(); // Por segurança destruir o random depois de gerar as chaves
			
			PrivateKey priv = pair.getPrivate();
			PublicKey pub = pair.getPublic(); 
			
			GerenciadorArquivos.writePublicKeyOnDisk(pub.getEncoded());
			GerenciadorArquivos.writeSalt(this.salt);
			
			System.out.println(priv);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	private void gerarChavePrivada(String pass) {
		try{
			System.out.println("gerarChavePrivada");
			sSeed = GerenciadorArquivos.readUserProfile() + GerenciadorArquivos.readSalt() + pass;
			Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "SC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "Crypto");
			random.setSeed(sSeed.getBytes());
			keyGen.initialize(256, random);
			KeyPair pair= keyGen.generateKeyPair();
			this.chavePrivada = pair.getPrivate();
		}
			catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sign(String fileSelected, String pass) {
		// pegar informações em file
		this.gerarChavePrivada(pass);
		
		try {
			
			//Criando instancia de assinatura
			Signature sign = Signature.getInstance("SHA1WITHECDSA", "SC");
			
			if (chavePrivada != null) {
				sign.initSign(chavePrivada);
			} else {
				System.out.println("Chave Privada Nula");
			}
			
			//Acesso ao arquivo selecionado para assinar
			FileInputStream fis = new FileInputStream(fileSelected);
			
			//Leitura do arquivo em parte 
			BufferedInputStream bufin =  new BufferedInputStream(fis);
			byte[] buffer =  new byte[2048];
			int len;
			while ((len = bufin.read(buffer)) >= 0 ) {          
				sign.update(buffer, 0 , len);
				
			};
			bufin.close();
			
			// Assintatura 
			byte[] realSig = sign.sign();
			GerenciadorArquivos.writeFileAppFolder(realSig, "Assinaturas","sig");
			
			//Destroi chave privada
			this.chavePrivada = null;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
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
	
	//Getters and Setters

}
