package br.com.saddm.gerenciador;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.EncodedKeySpec;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

import org.spongycastle.asn1.eac.ECDSAPublicKey;
import org.spongycastle.asn1.x509.Certificate;
import org.spongycastle.crypto.util.PublicKeyFactory;


public class GerenciadorCP {
	protected String salt, sSeed;
	protected PrivateKey chavePrivada;
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
			random.setSeed(201);
			System.out.println("Seed bytes = " + sSeed.getBytes("utf-8"));
			keyGen.initialize(256, random); 
			KeyPair pair = keyGen.generateKeyPair(); // Por segurança destruir o random depois de gerar as chaves
			
			PrivateKey priv = pair.getPrivate();
			PublicKey pub = pair.getPublic(); 
			
			System.out.println("Chave publica =" + pub);
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
			sSeed = GerenciadorArquivos.readSalt() + GerenciadorArquivos.readUserProfile() + pass;
			Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "SC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "Crypto");
			random.setSeed(201);
			keyGen.initialize(256, random);
			KeyPair pair= keyGen.generateKeyPair();
			this.chavePrivada = pair.getPrivate();
			System.out.println("Chave privada na assinatura =" + chavePrivada);
		}
			catch(Exception e) {
			e.printStackTrace();
		}
	}
	// Metodo de assinar um arquivo
	public void sign(String fileSelected, String pass) {
		// pegar informações em file
		this.gerarChavePrivada(pass);
		
		try {
			
			//Criando instancia de assinatura
			Signature sign = Signature.getInstance("SHA1WITHECDSA", "SC");
			
			if (chavePrivada != null) {
				System.out.println("Chave privada durante assinatura = " + chavePrivada );
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
			GerenciadorArquivos.writeFileAppFolder(realSig, "Assinaturas","signature.sig");
			
			//Destroi chave privada
			this.chavePrivada = null;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	// Metodo de Verificação de assinatura
	public boolean verifica (String fileSelected, String signature) {
		Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
		System.out.println("Entrou Verifica");
		//byte[] temp;
		
		try {
			// get da publickey do cartao sd
			KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", "SC");
			chavePublica = keyFactory.generatePublic(new X509EncodedKeySpec(GerenciadorArquivos.readPublicKey()));
			//temp = GerenciadorArquivos.readPublicKey();
			
			System.out.println("Pegou chave Publica = " + chavePublica);
			// le arquivo de assinatura
			byte[] sigToVerify = GerenciadorArquivos.readSignature(signature);
			
			Signature sig =  Signature.getInstance("SHA1WITHECDSA", "SC");
			sig.initVerify(chavePublica);
			
			/* Verifica o arquivo	 */
			System.out.println(fileSelected);
			FileInputStream datafis = new FileInputStream(fileSelected);
            BufferedInputStream bufin = new BufferedInputStream(datafis);

            byte[] buffer = new byte[1024];
            int len;
            while (bufin.available() != 0) {
                len = bufin.read(buffer);
                sig.update(buffer, 0, len);
                };

            bufin.close();


            boolean verifies = sig.verify(sigToVerify);
             System.out.println("Verificação da Assinatura = " + verifies );
            return verifies;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
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
