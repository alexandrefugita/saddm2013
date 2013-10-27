package br.com.saddm.pks;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;


public class GenSigECC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String teste = "Teste";
		
		//if (args.length != 1) {
		//	System.out.println("Usage: GenSig nameOfFileToSign");
		/*} else */ try {
			//rest of the code goes here
			
			// EC = algoritmo a ser utilizado
			// SunEC = Provider - responsável pela API
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC","SunEC");
			
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN"); // source of randomness
			// em caso de um seed espeficifico podesse utilziar random.setSeed(seed);
			//exemplo teste random.setSeed(201);
			long seed = 12;
			random.setSeed(seed);
			
			keyGen.initialize(256, random); 
			KeyPair pair = keyGen.generateKeyPair(); // Por segurança destruir o random depois de gerar as chaves
			
			PrivateKey priv = pair.getPrivate();
			PublicKey pub = pair.getPublic();
			
			System.out.println(priv);
			
			Signature ECC = Signature.getInstance("SHA512withECDSA", "SunEC");
			
			ECC.initSign(priv);
			
			FileInputStream fis = new FileInputStream(teste);
			BufferedInputStream bufin =  new BufferedInputStream(fis);
			byte[] buffer =  new byte[1024];
			int len;
			
			while ((len = bufin.read(buffer)) >= 0 ) {           // ??
				ECC.update(buffer, 0 , len);
				
			};
			bufin.close();
			
			byte[] realSig = ECC.sign();
			
			/* save the signature in a file */ 
			FileOutputStream sigfos = new FileOutputStream("sig");
			sigfos.write(realSig);
			sigfos.close();
			
			/* save the public key in a file */
			
			byte[] key = pub.getEncoded();
			FileOutputStream keyfos = new FileOutputStream("suepk");
			keyfos.write(key);
			keyfos.close();
			
			
		} catch (Exception e) {
			System.err.println("Caught execption "+ e.toString());
		}
	}

}
