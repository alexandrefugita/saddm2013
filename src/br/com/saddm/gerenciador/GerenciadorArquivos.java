package br.com.saddm.gerenciador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.os.Environment;


public class GerenciadorArquivos extends Activity {
	
	
	public static void writeUserProfile(String name, String cpf, String dataNascimento) {
		final String appDirectory = "/Saddm2013/";  //TODO como setar para geral

		try {
			File profileRoot = new File(Environment.getDataDirectory(), appDirectory + "Profile");
			
			if(!profileRoot.exists()) {
				profileRoot.mkdirs();
			}
			
			File profile = new File(profileRoot, "profile.txt");
			FileWriter profileWriter = new FileWriter(profile);
			
			profileWriter.write(name + " ");
			profileWriter.write(cpf + " ");
			profileWriter.write(dataNascimento + " ");
			
			profileWriter.flush();
			profileWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static String readUserProfile() {
		final String appDirectory = "/Saddm2013/"; //TODO
		final String name, cpf, dataNasc;
		
		if(isExternalStorageReadable()) {
			try {
				File profile = new File(Environment.getExternalStorageDirectory(), appDirectory + "Saddm2013/Profile/profie");
				FileReader profileReader = new FileReader(profile);
				BufferedReader bufReader =  new BufferedReader(profileReader);
				if(bufReader != null) {
					name = bufReader.readLine();
					cpf = bufReader.readLine();
					dataNasc = bufReader.readLine();
					System.out.println(name + "\n" + cpf + "\n" + dataNasc);
					return name + cpf + dataNasc;
				}
			} catch( Exception e){
				e.printStackTrace();
			}
		}
		
		
		return null;
	}
	
	public static void writeFileAppFolder(byte[] fileToBeWritten, String folder, String fileName) {
		final String appDirectory = "/Saddm2013/";  //TODO
		
		try {
			File profileRoot = new File(Environment.getDataDirectory(), appDirectory + folder);
			
			if(!profileRoot.exists()) {
				profileRoot.mkdirs();
			}
			
			File sFile = new File(profileRoot,fileName);
			FileOutputStream fos = new FileOutputStream(sFile);
			fos.write(fileToBeWritten);
			fos.flush();
			fos.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//Escreve Salt no cartao de memoria
	public static void writeSalt(String salt) {
		final String appDirectory = "/Saddm2013/";  //TODO
		try {
			File profileRoot = new File(Environment.getDataDirectory(), appDirectory + "Salt");
			
			if(!profileRoot.exists()) {
				profileRoot.mkdirs();
			}
			
			File saltFile = new File(profileRoot, "salt.txt");
			FileWriter saltWriter = new FileWriter(saltFile);
			
			saltWriter.write(salt);
			
			saltWriter.flush();
			saltWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Le o salt do arquivo
	public static String readSalt() {
		final String appDirectory = "/Saddm2013/";  //TODO
		String salt;
		if(isExternalStorageReadable()) {
			try {
				File profile = new File(Environment.getExternalStorageDirectory(), appDirectory +"Salt/salt");
				FileReader profileReader = new FileReader(profile);
				BufferedReader bufReader =  new BufferedReader(profileReader);
				salt = bufReader.readLine();
				System.out.println(salt);
				bufReader.close(); 
				return salt;
			} catch( Exception e){
				e.printStackTrace();
			} 
		}

		return "";
	}
	
	public static void writePublicKeyOnDisk(byte[] key) {
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
	public static  boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}




}
