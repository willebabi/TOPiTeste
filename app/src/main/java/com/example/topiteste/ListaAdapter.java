package com.example.topiteste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListaAdapter extends BaseAdapter {
    private Context mContext;
    private List<Items> data;
    private List<Items> data_exibe;
    private LayoutInflater layoutInflater;
    private View.OnClickListener handler = null;

    public ListaAdapter (Context mContext, List<Items> data, View.OnClickListener handler) {
        this.mContext = mContext;
        this.data = data;
        this.data_exibe = data;
        this.layoutInflater = LayoutInflater.from(mContext);
        this.handler = handler;
    }

    @Override
    public int getCount() {
        return data_exibe.size();
    }

    @Override
    public Object getItem(int arg0) {
        return data_exibe.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return data_exibe.get(arg0).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHelper itemHelper = new ItemHelper();

        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.items,null);
            itemHelper.linha = (LinearLayout) convertView.findViewById(R.id.linha);
            itemHelper.nmrepos = (TextView) convertView.findViewById(R.id.nmrepos);
            itemHelper.descricao = (TextView) convertView.findViewById(R.id.descricao);
            itemHelper.img1 = (ImageView) convertView.findViewById(R.id.img1);
            itemHelper.num1 = (TextView) convertView.findViewById(R.id.num1);
            itemHelper.img2 = (ImageView) convertView.findViewById(R.id.img2);
            itemHelper.num2 = (TextView) convertView.findViewById(R.id.num2);
            itemHelper.img3 = (ImageView) convertView.findViewById(R.id.img3);
            itemHelper.usernm = (TextView) convertView.findViewById(R.id.usernm);
            itemHelper.nome = (TextView) convertView.findViewById(R.id.nome);

        } else {
            itemHelper = (ItemHelper) convertView.getTag();
        }

        if (data_exibe != null && data_exibe.get(position) != null) {
            Items objectItem = data_exibe.get(position);
            itemHelper.nmrepos.setText(objectItem.getName());
            itemHelper.descricao.setText(objectItem.getDescription());
            itemHelper.num1.setText(String.valueOf((int) objectItem.getSize()));
            itemHelper.num2.setText(String.valueOf((int) objectItem.getStargazers_count()));
            itemHelper.usernm.setText(objectItem.getName());
            itemHelper.nome.setText(objectItem.getFull_name());

            Picasso.with(mContext).load(objectItem.getAvatar_url()).into(itemHelper.img3);
        }

        convertView.setTag(itemHelper);
        return convertView;
    }

    public class ItemHelper {
        public LinearLayout linha;
        public ImageView img1, img2, img3;
        public TextView nmrepos, descricao, num1, num2, usernm, nome;
        public Items items;
    }

    public Filter getFilter() {
        Filter filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                data_exibe = (List<Items>) results.values; // Valores filtrados.
                notifyDataSetChanged();  // Notifica a lista de alteração
            }

            @Override
            protected FilterResults performFiltering(CharSequence filtro) {
                FilterResults results = new FilterResults();
                //se não foi realizado nenhum filtro insere todos os itens.
                if (filtro == null || filtro.length() == 0) {
                    results.count = data.size();
                    results.values = data;
                } else {
                    //cria um array para armazenar os objetos filtrados.
                    List<Items> data_filtrados = new ArrayList<Items>();

                    //percorre toda lista verificando se contem a palavra do filtro na descricao do objeto.
                    for (int i = 0; i < data.size(); i++) {
                        Items dado = data.get(i);

                        filtro = filtro.toString().toLowerCase();
                        String condicao = "";
                        condicao = dado.getFull_name().toLowerCase();

                        if (condicao.contains(filtro)) {
                            //se conter adiciona na lista de itens filtrados.
                            data_filtrados.add(dado);
                        }
                    }
                    // Define o resultado do filtro na variavel FilterResults
                    results.count = data_filtrados.size();
                    results.values = data_filtrados;
                }
                return results;
            }
        };
        return filter;
    }
}
