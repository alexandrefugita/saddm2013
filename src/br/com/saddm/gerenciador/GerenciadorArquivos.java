package br.com.saddm.gerenciador;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;

public class GerenciadorArquivos {

	public void writePublicKeyOnDisk(byte[] key) {
		String sFileName = "suepk";
		
		//Confere o acesso ao cartão externo
		//Caso afirmativo prossegue com a escrita do arquivo
		if(isExternalStorageWritable()) {
			try {
				//Pega path externo
				File root = new File(Environment.getExternalStorageDirectory(), "Chaves");
				
				//Cria path, caso o mesmo não exista
				if (!root.exists()) {
					root.mkdirs();
				}
				//Set do caminho final do arquivo
				File file = new File(root, sFileName);
				
				// Escreve arquivo no caminho indicado
				FileOutputStream keyfos = new FileOutputStream(file);
				keyfos.write(key);
				keyfos.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Sem acesso ao cartão exteno");
		}
	}


	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}




}
