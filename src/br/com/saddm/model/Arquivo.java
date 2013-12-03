package br.com.saddm.model;

import java.util.Date;

import org.simpleframework.xml.Default;

/**
 * Model base dos Arquivos transportados no sistema. Possui, alem do proprio arquivo
 * (serializado em vetor de bytes), uma mensagem - tipicamente o nome do arquivo -, 
 * os nomes do remetente e do destinatario, a data de postagem do arquivo no servidor, 
 * um indicador se o arquivo ja foi entregue ou nao, e a assinatura digital.
 * @author alexandre
 *
 */
@Default(required=false)
public class Arquivo {
	private int id;
    private String mensagem;
    private Date data;
    private String remetente;
    private String destinatario;
    private boolean entregue;
    private String arquivoSerializado;
    private String assinatura;
    
    public int getId() {
    	return id;
    }
    
    public void setId(int id) {
    	this.id = id;
    }
    
    public String getMensagem() {
    	return mensagem;
    }
    
    public void setMensagem(String mensagem) {
    	this.mensagem = mensagem;
    }
    
    public Date getData() {
    	return data;
    }
    
    public void setData(Date data) {
    	this.data = data;
    }
 
    public String getRemetente() {
		return remetente;
	}

	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public boolean isEntregue() {
		return entregue;
	}

	public void setEntregue(boolean entregue) {
		this.entregue = entregue;
	}

	public String getArquivoSerializado() {
		return arquivoSerializado;
	}
	
	public void setArquivoSerializado(String arquivoSerializado) {
		this.arquivoSerializado = arquivoSerializado;
	}

	public String getAssinatura() {
		return assinatura;
	}

	public void setAssinatura(String assinatura) {
		this.assinatura = assinatura;
	}
}
