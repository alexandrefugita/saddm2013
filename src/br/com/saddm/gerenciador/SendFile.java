package br.com.saddm.gerenciador;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import br.com.saddm.model.Arquivo;
import br.com.saddm.util.Base64Coder;
import br.com.thinkti.android.filechooser.FileChooser;

import com.fugitahyodo.saddm.R;

public class SendFile extends Activity{
	private static final int FILE_CHOOSER = 11;

	private Context mContext;
	private ProgressDialog mDialog;
	private String pathFileSelected;
	private String pathAssinatura;
	private int tipoArquivo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_file);

		mContext = this;
	}
	
	public void chooseFile(View view) {
		tipoArquivo = 1;
		
		Intent intent = new Intent(this, FileChooser.class);
		ArrayList<String> extensions = new ArrayList<String>();
		extensions.add(".pdf");
		extensions.add(".txt");
		extensions.add(".png");
		extensions.add(".jpeg");
		extensions.add(".jpg");
		intent.putStringArrayListExtra("filterFileExtension", extensions);
		startActivityForResult(intent, FILE_CHOOSER);
	}

	public void chooseSignature(View view) {
		tipoArquivo = 2;
		
		Intent intent = new Intent(this, FileChooser.class);
		ArrayList<String> extensions = new ArrayList<String>();
		extensions.add(".sig");
		intent.putStringArrayListExtra("filterFileExtension", extensions);
		startActivityForResult(intent, FILE_CHOOSER);
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == FILE_CHOOSER) && (resultCode == -1)) {
			
			// escolher arquivo
			if(tipoArquivo == 1) {
				this.pathFileSelected = data.getStringExtra("fileSelected");
			}
			
			// escollher assinatura
			else if (tipoArquivo == 2) {
				this.pathAssinatura = data.getStringExtra("fileSelected");
			}
			
			//Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
		}
	}

	
	
	
	public void enviar(View view) {

		AsyncTask<Void, Void, String> at = new AsyncTask<Void, Void, String>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

				mDialog = new ProgressDialog(mContext);
				mDialog.setMessage("Aguarde...");
				mDialog.show();
			}

			@Override
			protected String doInBackground(Void... params) {
				File f = new File(pathFileSelected);
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

				
				File sig = new File(pathAssinatura);
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
				
				Arquivo arquivo = new Arquivo();
				arquivo.setMensagem(f.getName());
				arquivo.setRemetente("31080728899");
				arquivo.setDestinatario("destinatario");
				arquivo.setArquivoSerializado(arquivoSerializado);
				arquivo.setAssinatura(assinaturaSerializada);

				String asyncResult = null;
				GerenciadorConexao gc = new GerenciadorConexao();
				try {
					asyncResult = gc.postArquivo(arquivo);
				} catch (Exception e) {
					e.printStackTrace();
				}

				return asyncResult;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				mDialog.dismiss();
			}
		}.execute();

		try {
			Toast.makeText(SendFile.this, "Mensagem " + at.get() + " cadastrada", Toast.LENGTH_SHORT).show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
