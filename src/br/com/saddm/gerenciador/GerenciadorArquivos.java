package br.com.saddm.gerenciador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.os.Environment;


public class GerenciadorArquivos extends Activity {

	
	public static void writeUserProfile(String name, String cpf, String dataNascimento) {
		final String appDirectory = "/Saddm2013/Profile/";  //TODO como setar para geral

		try {
			File profileRoot = new File(Environment.getExternalStorageDirectory(), appDirectory);
			
			if(!profileRoot.exists()) {
				profileRoot.mkdirs();
			}
			
			File profile = new File(profileRoot, "profile.txt");
			FileWriter profileWriter = new FileWriter(profile);
			profileWriter.write("name " + name + " ");
			profileWriter.write("cpf " + cpf + " ");
			profileWriter.write("dataN " + dataNascimento + " ");
			
			profileWriter.flush();
			profileWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static String readUserProfile() {
		final String appDirectory = "/Saddm2013/Profile/"; //TODO
		final String line, name, cpf, dataNasc;
		int start, end;
		
		if(isExternalStorageReadable()) {
			try {
				File profile = new File(Environment.getExternalStorageDirectory(), appDirectory + "profiLe.txt");
				FileReader profileReader = new FileReader(profile);
				BufferedReader bufReader =  new BufferedReader (profileReader,8);
				if(bufReader != null) {
					line = bufReader.readLine();
					//Get nome
					start = line.lastIndexOf("name ") + 5;
					end = line.indexOf(" ", start);
					name = line.substring(start, end);
					//Get cpf
					start = line.lastIndexOf("cpf ") + 4;
					end = line.indexOf(" ", start);
					cpf = line.substring(start, end);
					//Get datNasc
					start = line.lastIndexOf("dataN ") + 6;
					dataNasc = line.substring(start);
					System.out.println("Info = " + name + cpf + dataNasc );
					profileReader.close();
					bufReader.close();
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
			File profileRoot = new File(Environment.getExternalStorageDirectory(), appDirectory + folder);
			
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
	
	public static byte[] readSignature(String signature) {
		//final String appDirectory = "/Saddm2013/Assinaturas/";
		try {
			File sigPath = new File(signature);
			FileInputStream sigfis = new FileInputStream(sigPath);
			byte[] sigToVerify = new byte[sigfis.available()];
			sigfis.read(sigToVerify);
			sigfis.close();
			return sigToVerify;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//Escreve Salt no cartao de memoria
	public static void writeSalt(String salt) {
		final String appDirectory = "/Saddm2013/Salt/";  //TODO
		try {
			File profileRoot = new File(Environment.getExternalStorageDirectory(), appDirectory);
			
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
		final String appDirectory = "/Saddm2013/Salt/";  //TODO
		String salt;
		if(isExternalStorageReadable()) {
			try {
				File profile = new File(Environment.getExternalStorageDirectory(), appDirectory +"salt.txt");
				FileReader profileReader = new FileReader(profile);
				BufferedReader bufReader =  new BufferedReader(profileReader);
				salt = bufReader.readLine();
				System.out.println("Salt =" + salt);
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
		final String appDirectory = "/Saddm2013/";
		
		//Confere o acesso ao cartão externo
		//Caso afirmativo prossegue com a escrita do arquivo
		if(isExternalStorageWritable()) {
			try {
				//Pega path externo
				File root = new File(Environment.getExternalStorageDirectory(), appDirectory + "Chaves");
				
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
	
	public static byte[] readPublicKey() {
		String sFileName = "suepk";
		final String appDirectory = "/Saddm2013/";
		
		try {
			File pub = new File (Environment.getExternalStorageDirectory(), appDirectory + "Chaves/" +sFileName);
			FileInputStream keyfis = new FileInputStream(pub);
			byte[] key = new byte[keyfis.available()];
			keyfis.read(key);
			keyfis.close();
			return key;
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}


	/* Checks if external storage is available for read and write */
	private static  boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	private static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}




}
