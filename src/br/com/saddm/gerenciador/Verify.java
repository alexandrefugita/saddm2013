package br.com.saddm.gerenciador;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.fugitahyodo.saddm.R;

import br.com.saddm.MainActivity;
import br.com.saddm.model.Arquivo;
import br.com.saddm.util.Base64Coder;
import br.com.thinkti.android.filechooser.FileChooser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Verify extends Activity{
	private static final int FILE_CHOOSER = 11;
	
	private String fileSelected;
	private String sigSelected;
	private String pubKey;
	private int signaturefile = 0;
	private Context mContext;
	private ProgressDialog mDialog;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verify);
		
		mContext = this;
	}
	
	public void chooseFile(View view) {
	    	Intent intent = new Intent(this, FileChooser.class);
	    	ArrayList<String> extensions = new ArrayList<String>();
	    	extensions.add(".pdf");
	    	extensions.add(".txt");
	    	extensions.add(".png");
	    	extensions.add(".jpeg");
	    	extensions.add(".jpg");
	    	intent.putStringArrayListExtra("filterFileExtension", extensions);
	    	signaturefile = 0;
	    	startActivityForResult(intent, FILE_CHOOSER);

	}
	
	public void chooseSignature(View view) {
    	Intent intent = new Intent(this, FileChooser.class);
    	ArrayList<String> extensions = new ArrayList<String>();
       	extensions.add(".sig");
    	intent.putStringArrayListExtra("filterFileExtension", extensions);
    	signaturefile = 1;
    	startActivityForResult(intent, FILE_CHOOSER);
	}
	
	public void choosePubKey(View view) {
    	Intent intent = new Intent(this, FileChooser.class);
    	ArrayList<String> extensions = new ArrayList<String>();
       	extensions.add(".pub");
    	intent.putStringArrayListExtra("filterFileExtension", extensions);
    	signaturefile = 2;
    	startActivityForResult(intent, FILE_CHOOSER);
	}
	    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == FILE_CHOOSER) && (resultCode == -1)) {
	    	if(signaturefile  == 1)	 {
	    		this.sigSelected = data.getStringExtra("fileSelected");
	    		signaturefile = 0;
	    		//Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
	    	} else if (signaturefile == 0 ) {
	    		this.fileSelected = data.getStringExtra("fileSelected");
	    		signaturefile = 0;
	    	} else if (signaturefile == 2) {
	    		this.pubKey = data.getStringExtra("fileSelected");
	    		signaturefile = 0;
	    	}
	    }
	    		
	}
	
	public void verifica(View view) {
		GerenciadorCP geCP = new GerenciadorCP();
		System.out.println(fileSelected + "\n" +  sigSelected + "\n" + pubKey);
		
		if(geCP.verifica(fileSelected, sigSelected, pubKey)) {
			System.out.println("Assinatura est� okay");
			Toast.makeText(Verify.this,
					"Assinatura � do arquivo selecionado",
					Toast.LENGTH_SHORT).show(); 
		} else {
			System.out.println("Assinatura ou Arquivo n�o est�o certos");
			Toast.makeText(Verify.this,
					"Assinatura, Arquivo Assinado ou Chave P�blica n�o est�o certos",
					Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public void verificar(View view) {
		AsyncTask<Void, Void, Boolean> at = new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

				mDialog = new ProgressDialog(mContext);
				mDialog.setMessage("Aguarde...");
				mDialog.show();
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				File f = new File(fileSelected);
				FileInputStream fis;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				try {
					fis = new FileInputStream(f);
					byte[] buf = new byte[1024];
					for (int readNum; (readNum = fis.read(buf)) != -1;) {
						baos.write(buf, 0, readNum);
					}
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				String arquivoSerializado = new String(Base64Coder.encode(baos.toByteArray()));

				
				File sig = new File(sigSelected);
				FileInputStream fisSignature;
				ByteArrayOutputStream baosSignature = new ByteArrayOutputStream();
				try {
					fisSignature = new FileInputStream(sig);
					byte[] bufSignature = new byte[1024];
					for (int readNum; (readNum = fisSignature.read(bufSignature)) != -1;) {
						baosSignature.write(bufSignature, 0, readNum);
					}
					fisSignature.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				String assinaturaSerializada = new String(Base64Coder.encode(baosSignature.toByteArray()));
				
				String remetente = sigSelected.substring(sigSelected.length() - 15, sigSelected.length() - 4);
				System.out.println(remetente);
				
				Arquivo arquivo = new Arquivo();
				arquivo.setRemetente(remetente);
				arquivo.setArquivoSerializado(arquivoSerializado);
				arquivo.setAssinatura(assinaturaSerializada);

				Boolean asyncResult = false;
				GerenciadorConexao gc = new GerenciadorConexao();
				try {
					asyncResult = gc.verifyArquivo(arquivo);
				} catch (Exception e) {
					e.printStackTrace();
				}

				return asyncResult;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				if(result) {
					System.out.println("Assinatura est� okay");
					Toast.makeText(Verify.this,
							"Assinatura é do arquivo selecionado",
							Toast.LENGTH_SHORT).show(); 
				} else {
					System.out.println("Assinatura ou Arquivo n�o est�o certos");
					Toast.makeText(Verify.this,
							"Assinatura, Arquivo Assinado ou Chave Pública não estão certos",
							Toast.LENGTH_SHORT).show();
				}
				
				mDialog.dismiss();
			}
		}.execute();

		try {
			Toast.makeText(Verify.this, "Mensagem " + at.get() + " cadastrada", Toast.LENGTH_SHORT).show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void voltar(View view) {
		Intent intent = new Intent(Verify.this, MainActivity.class);
		startActivity(intent);
	}

}
