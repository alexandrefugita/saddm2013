package br.com.saddm.pks;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;


public class VerSig {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/* Verifica a DSA signature */
		
		if(args.length != 3) {
			System.out.println("Usage: VerfSign");
		} else try {
			
			// obtendo a chave publica 
			FileInputStream keyfis =  new FileInputStream(args[0]);
			byte[] encKey = new byte[keyfis.available()];
			keyfis.read(encKey);
			
			keyfis.close();
			
			//precisa da especificaçao da chave
			//como descovre isso qui em baixo???
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
			
			KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
			
			//KerFactory gera a chave publica a partir da especificacao, pq precisa disso?
			PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
			
			//input the signature bytes
			FileInputStream sigfis = new FileInputStream(args[1]);
			byte[] sigToVerify = new byte[sigfis.available()];
			sigfis.read(sigToVerify);
			sigfis.close();
			
			// Agora fazemos a Verificacao
			Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
			sig.initVerify(pubKey);
			
			// Pegando a data
			FileInputStream datafis = new FileInputStream(args[2]);
			BufferedInputStream bufin = new BufferedInputStream(datafis);
			
			byte[] buffer = new byte[1024];
			int len;
			while (bufin.available() != 0) {
				len = bufin.read(buffer);
				sig.update(buffer, 0, len);
			};
			
			bufin.close();
			
			boolean verifies = sig.verify(sigToVerify);
			
			System.out.println("Signature verifies: " + verifies);
			
		} catch (Exception e) {
			System.err.println("Caught exeception" + e.toString());
		}

	}

}
