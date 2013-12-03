package br.com.saddm.gerenciador;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.com.saddm.model.Arquivo;
import br.com.saddm.model.ArquivoContainer;
import br.com.saddm.util.Constants;
import br.com.saddm.util.DateFormatTransform;

/**
 * Classe que efetua todas as conexoes com o servidor. Os metodos recebem os atributos que serao usados
 * como parametros nas queries, e retornam usualmente uma instancia de ArquivoContainer (que possui um
 * ou mais Arquivos de resultado de busca) ou um codigo de status da requisicao efetuada.
 * @author alexandre
 *
 */
public class GerenciadorConexao {

	private ArquivoContainer ac;
	
	/**
	 * Obtem um Arquivo, destinado a um usuario especifico. Como medida de seguranca, deve-se fornecer
	 * tambem a data em que o arquivo foi postado e o nome do remetente.
	 * @param id Id do Arquivo a ser baixado
	 * @param milis data de postagem do Arquivo no servidor, no formato yyyyMMddhhmmss
	 * @param remetente nome do remetente do Arquivo
	 * @return ArquivoContainer, com o Arquivo correspondente ou com uma lista vazia em caso de erro
	 * @throws Exception
	 */
	public ArquivoContainer getArquivoById(int id, String milis, String remetente) throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();

		HttpUriRequest req = new HttpGet(String.format(Constants.urlGet, id, milis, remetente));
		req.addHeader("Accept", "text/xml");

		HttpResponse res = client.execute(req);

		InputStream in = res.getEntity().getContent();

		ac = new ArquivoContainer();

		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {
				boolean bArquivoSerializado = false;
				boolean bAssinatura = false;
				boolean bData = false;
				boolean bDestinatario = false;
				boolean bEntregue = false;
				boolean bId = false;
				boolean bMensagem = false;
				boolean bRemetente = false;
				
				Arquivo bufferArquivo = new Arquivo();
				List<Arquivo> bufferLista = new ArrayList<Arquivo>();
				StringBuilder bufferArquivoSerializado;
				StringBuilder bufferAssinatura;
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", new Locale("en", "US"));
				
				@Override
				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
					System.out.println("Start Element :" + qName);

					if (qName.equalsIgnoreCase("arquivoserializado")) {
						bufferArquivoSerializado = new StringBuilder();
						bArquivoSerializado = true;
					}
					if (qName.equalsIgnoreCase("assinatura")) {
						bufferAssinatura = new StringBuilder();
						bAssinatura = true;
					}
					if (qName.equalsIgnoreCase("data")) {
						bData = true;
					}
					if (qName.equalsIgnoreCase("destinatario")) {
						bDestinatario = true;
					}
					if (qName.equalsIgnoreCase("entregue")) {
						bEntregue = true;
					}
					if (qName.equalsIgnoreCase("id")) {
						bId = true;
					}
					if (qName.equalsIgnoreCase("mensagem")) {
						bMensagem = true;
					}
					if (qName.equalsIgnoreCase("remetente")) {
						bRemetente = true;
					}
				}

				@Override
				public void endElement(String uri, String localName, String qName) throws SAXException {
					// se terminou arquivocontainer, termina
					if (qName.equalsIgnoreCase("arquivocontainer")) {
						ac.setArquivo(bufferLista);
					}
					
					// se terminou arquivo, adiciona um novo arquivo no container e zera o buffer 
					if (qName.equalsIgnoreCase("arquivo")) {
						bufferLista.add(bufferArquivo);
						bufferArquivo = new Arquivo();
					}
					
					// se terminou o arquivo serializado, monta byte array e salva no buffer
					if (qName.equalsIgnoreCase("arquivoserializado")) {
						bufferArquivo.setArquivoSerializado(bufferArquivoSerializado.toString());
						bArquivoSerializado = false;
					}
					
					// se terminou a assinatura, monta byte array e salva no buffer
					if (qName.equalsIgnoreCase("assinatura")) {
						bufferArquivo.setAssinatura(bufferAssinatura.toString());
						bAssinatura = false;
					}
				}

				@Override
				public void characters(char ch[], int start, int length) throws SAXException {
					if (bArquivoSerializado) {
						bufferArquivoSerializado.append(ch, start, length);
					}
					if (bAssinatura) {
						bufferAssinatura.append(ch, start, length);
					}
					if (bData) {
						try {
							bufferArquivo.setData(dateFormat.parse(new String(ch, start, length)));
							bData = false;
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					if (bDestinatario) {
						bufferArquivo.setDestinatario(new String(ch, start, length));
						bDestinatario = false;
					}
					if (bEntregue) {
						bufferArquivo.setEntregue(new String(ch, start, length).equals("1") ? true : false);
						bEntregue = false;
					}
					if (bId) {
						bufferArquivo.setId(Integer.parseInt(new String(ch, start, length)));
						bId = false;
					}
					if (bMensagem) {
						bufferArquivo.setMensagem(new String(ch, start, length));
						bMensagem = false;
					}
					if (bRemetente) {
						bufferArquivo.setRemetente(new String(ch, start, length));
						bRemetente = false;
					}
				}
			};

			saxParser.parse(in, handler);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ac;
		
		
		
		
		

//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", new Locale("en", "US"));
//
//		RegistryMatcher m = new RegistryMatcher();
//		m.bind(Date.class, new DateFormatTransform(format));
//
//		Serializer ser = new Persister(m);
//		ArquivoContainer ac = ser.read(ArquivoContainer.class, in);
//		in.close();
//
//		return ac;
	}

	/**
	 * Obtem uma lista com os Arquivos disponiveis para download enderecados a um dado usuario.
	 * Sao disponiveis os Arquivos em que o destinatario e o login o usuario, e que ainda nao
	 * tenham sido baixados nenhuma vez.
	 * @param destinatario identificador do usuario, que e o destinatario do Arquivo
	 * @return ArquivoContainer contendo a lista de Arquivos disponiveis. Caso nao haja Arquivos
	 * a serem baixados, a lista e vazia.
	 * @throws Exception
	 */
	public ArquivoContainer getArquivosByDestinatario(String destinatario) throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();

		HttpUriRequest req = new HttpGet(String.format(Constants.urlGetList, destinatario));
		req.addHeader("Accept", "text/xml");

		HttpResponse res = client.execute(req);

		InputStream in = res.getEntity().getContent();

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", new Locale("en", "US"));

		RegistryMatcher m = new RegistryMatcher();
		m.bind(Date.class, new DateFormatTransform(format));

		Serializer ser = new Persister(m);
		ArquivoContainer ac = ser.read(ArquivoContainer.class, in);
		in.close();

		return ac;
	}
	/**
	 * Envia um Arquivo ao servidor, para disponibiliza-lo para download a um destinatario.
	 * O Arquivo enviado deve conter, alem da mensagem, a assinatura digital e os nomes do
	 * remetente e do destinatario.
	 * @param arquivo Arquivo a ser enviado, com os dados acima descritos preenchidos
	 * @return Id do Arquivo cadastrado no servidor em caso de sucesso, e 0 (ou o html de
	 * retorno) em caso de erro
	 * @throws Exception
	 */
	public String postArquivo(Arquivo arquivo) throws Exception {
		arquivo.setEntregue(false);

		Serializer ser = new Persister();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ser.write(arquivo, baos);

		DefaultHttpClient client = new DefaultHttpClient();

		HttpPost req = new HttpPost(Constants.urlPost);
		req.setEntity(new StringEntity(new String(baos.toByteArray(), "UTF-8")));
		req.addHeader("Content-type", "text/xml");
		req.addHeader("Accept", "plain/text");

		HttpResponse res = client.execute(req);

		InputStream in = res.getEntity().getContent();
		int i = -1;
		String ret = "";
		byte[] buf = new byte[1024];
		while ((i = in.read(buf)) > -1) {
			ret += new String(buf, 0, i);
		}

		return ret;
	}
	
	public boolean verifyArquivo(Arquivo arquivo) throws Exception {
		Serializer ser = new Persister();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ser.write(arquivo, baos);

		DefaultHttpClient client = new DefaultHttpClient();

		HttpPost req = new HttpPost(Constants.urlVerify);
		req.setEntity(new StringEntity(new String(baos.toByteArray(), "UTF-8")));
		req.addHeader("Content-type", "text/xml");
		req.addHeader("Accept", "plain/text");

		HttpResponse res = client.execute(req);

		InputStream in = res.getEntity().getContent();
		int i = -1;
		String ret = "";
		byte[] buf = new byte[1024];
		while ((i = in.read(buf)) > -1) {
			ret += new String(buf, 0, i);
		}

		if (ret.equalsIgnoreCase("true")) {
			return true;
		}
		if (ret.equalsIgnoreCase("false")) {
			return false;
		}
		
		return false;
	}
}
