package br.com.saddm.model;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.ElementList;
 
/**
 * Classe auxiliar de comunicacao entre o servidor e a aplicacao Android. As respostas
 * no formato XML sao parseadas utilizando o formato dessa classe como template, que 
 * e composta basicamente de uma lista com o(s) Arquivo(s) obtidos na requisicao.
 * @author alexandre
 *
 */
@Default(required=false)
public class ArquivoContainer {
	
	@ElementList(inline = true, entry = "arquivo", required = false)
	private List<Arquivo> arquivos = new ArrayList<Arquivo>();
	
    public List<Arquivo> getArquivo() {
        return arquivos;
    }
 
    public void setArquivo(List<Arquivo> lista) {
        this.arquivos = lista;
    }
}
