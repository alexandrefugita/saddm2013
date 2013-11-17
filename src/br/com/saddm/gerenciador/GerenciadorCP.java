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
import java.security.spec.X509EncodedKeySpec;


public class GerenciadorCP {
	protected long lSeed;
	protected PrivateKey chavePrivada;
	protected PublicKey chavePublica;
	
	public void gerarChaves(String pass, String passAle) {
		lSeed = changeInfoToSeed(GerenciadorArquivos.readSalt() + GerenciadorArquivos.readUserProfile() + pass + passAle);
		
		pass = null;
		
		try {
			Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "SC");
			
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "Crypto"); // source of randomness
			// em caso de um seed espeficifico podesse utilziar random.setSeed(seed);
			//exemplo teste random.setSeed(201);
			random.setSeed(lSeed);
			keyGen.initialize(256, random); 
			KeyPair pair = keyGen.generateKeyPair(); // Por segurança destruir o random depois de gerar as chaves
			
			chavePrivada = pair.getPrivate();
			chavePublica = pair.getPublic(); 
			
			GerenciadorArquivos.writePublicKeyOnDisk(chavePublica.getEncoded());
			
			System.out.println("Chave privada na geracao =" + chavePrivada);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	private void gerarChavePrivada(String pass, String passAle) {
		try{
			lSeed = this.changeInfoToSeed(GerenciadorArquivos.readSalt() + GerenciadorArquivos.readUserProfile() + pass + passAle);
			Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "SC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "Crypto");
			random.setSeed(lSeed);
			keyGen.initialize(256, random);
			KeyPair pair= keyGen.generateKeyPair();
			this.chavePrivada = pair.getPrivate();
			System.out.println("Chave privada na gerar priv =" + chavePrivada);
		}
			catch(Exception e) {
			e.printStackTrace();
		}
	}
	// Metodo de assinar um arquivo
	public void sign(String fileSelected, String pass, String passAle) {
		// pegar informações em file
		this.gerarChavePrivada(pass, passAle);
		
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
	
	private long changeInfoToSeed(String info) {
		System.out.println("Info to seed = " + info);
		long infoLong = 0;
		int i;
		
		for(i = 0; i < info.length(); i++) {
			infoLong = info.charAt(i) + infoLong;
		}
		
		return infoLong;
	}
	
	//Getters and Setters
	
}
