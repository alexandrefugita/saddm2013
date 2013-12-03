package br.com.saddm.gerenciador;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.fugitahyodo.saddm.R;

import br.com.saddm.SaddmActivity;
import br.com.saddm.model.Arquivo;
import br.com.saddm.model.ArquivoContainer;
import br.com.saddm.model.ArquivoListAdapter;
import br.com.saddm.util.Base64Coder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GerenciadorDestinatario extends SaddmActivity {
	private ArquivoListAdapter listAdapter;
	private ListView listaArquivos;
	private TextView mensagemListaVazia;

	private String mDestinatario;
	private String mId;
	private String mTimeMilis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gerenciador_destinatario);

		listaArquivos = (ListView) findViewById(R.id.gerenciador_destinatario_lista);
		mensagemListaVazia = (TextView) findViewById(R.id.gerenciador_destinatario_text_lista_vazia);

		listAdapter = new ArquivoListAdapter(this);
		listaArquivos.setAdapter(listAdapter);
		listaArquivos.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mDestinatario = ((TextView) view.findViewById(R.id.simpletext_remetente)).getText().toString();
				mTimeMilis = ((TextView) view.findViewById(R.id.simpletext_time_milis)).getText().toString();
				mId = ((TextView) view.findViewById(R.id.simpletext_id)).getText().toString();

				AsyncTask<Void, Void, List<Arquivo>> at = new AsyncTask<Void, Void, List<Arquivo>>() {
					@Override
					protected List<Arquivo> doInBackground(Void... params) {
						try {
							GerenciadorConexao gc = new GerenciadorConexao();
							ArquivoContainer ac = gc.getArquivoById(Integer.parseInt(mId), mTimeMilis, mDestinatario);
							return ac.getArquivo();
						} catch (Exception e) {
							e.printStackTrace();
						}

						return null;
					}
				}.execute();
				try {
					List<Arquivo> resultList = at.get();

					if (resultList != null) {
						Arquivo arquivo = resultList.get(0);
						// desserializar arquivo (transformar String em byte[], depois em arquivo na pasta)
	                	byte [] byteArrayArquivo = Base64Coder.decode(arquivo.getArquivoSerializado());
	        			GerenciadorArquivos.writeFileAppFolder(byteArrayArquivo, "Recebidos/Arquivos", arquivo.getMensagem());
				        
						// desserializar asinatura (transformar String em byte[], depois em arquivo na pasta)
	                	byte [] byteArrayAssinatura = Base64Coder.decode(arquivo.getAssinatura());
//	                	GerenciadorArquivos.writeFileAppFolder(byteArrayAssinatura, "Recebidos/Assinaturas", "Assinatura.sig");
	                	
	                	GerenciadorArquivos.writeFileAppFolder(byteArrayAssinatura, "Recebidos/Assinaturas", "Assinatura_" + arquivo.getRemetente() + ".sig");
						
						Toast.makeText(GerenciadorDestinatario.this, arquivo.getMensagem() + " criado",
								Toast.LENGTH_SHORT).show();
					} else {
						// nao tem arquivo: sinalizar erro
						Toast.makeText(GerenciadorDestinatario.this, "Erro no download do arquivo",
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void sincronizar(View view) {
		listAdapter.clear();

		AsyncTask<Void, Void, List<Arquivo>> at = new AsyncTask<Void, Void, List<Arquivo>>() {
			@Override
			protected List<Arquivo> doInBackground(Void... params) {
				try {
					GerenciadorConexao gc = new GerenciadorConexao();
					return gc.getArquivosByDestinatario("destinatario").getArquivo();
				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;
			}
		}.execute();
		try {
			List<Arquivo> resultList = at.get();

			if (resultList != null) {
				listAdapter.addArquivos(resultList);
				// atualizar lista
				listaArquivos.setVisibility(View.VISIBLE);
				mensagemListaVazia.setVisibility(View.GONE);
			} else {
				listaArquivos.setVisibility(View.GONE);
				mensagemListaVazia.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
