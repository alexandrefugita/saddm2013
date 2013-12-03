package br.com.saddm.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.fugitahyodo.saddm.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Classe auxiliar do Android para gerenciamento de listas, que herda da classe BaseAdapter.
 * Nela sao definidos os comportamentos para customizar a forma como uma lista de Arquivos
 * sao representadas em um ListView.
 * @author alexandre
 *
 */
public class ArquivoListAdapter extends BaseAdapter {

	private List<Arquivo> arquivos = new ArrayList<Arquivo>();
    private LayoutInflater inflater;
 
    public ArquivoListAdapter(Activity ctx) {
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    public void clear() {
        arquivos.removeAll(arquivos);
        notifyDataSetChanged();
    }
 
    public void addArquivos(List<Arquivo> arquivos) {
        this.arquivos.addAll(arquivos);
        notifyDataSetChanged();
    }
 
    @Override
    public int getCount() {
        return arquivos.size();
    }
 
    @Override
    public Object getItem(int position) {
        return arquivos.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Arquivo arquivo = arquivos.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.simpletext, null);
        }
        TextView textMensagem = (TextView) convertView.findViewById(R.id.simpletext_mensagem);
        textMensagem.setText(arquivo.getMensagem());
        
        TextView textRemetente = (TextView) convertView.findViewById(R.id.simpletext_remetente);
        textRemetente.setText(arquivo.getRemetente());
        
        String parsedData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US).format(arquivo.getData());
        TextView textData = (TextView) convertView.findViewById(R.id.simpletext_data);
        textData.setText(parsedData);
        
        TextView textId = (TextView) convertView.findViewById(R.id.simpletext_id);
        textId.setText(String.valueOf(arquivo.getId()));

        TextView textTimeMilis = (TextView) convertView.findViewById(R.id.simpletext_time_milis);
        textTimeMilis.setText(String.valueOf(arquivo.getData().getTime()));
        
        return convertView;
    }

}
