package com.example.topiteste;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private MaterialSearchBar edtPesq;
    private ListView listViewItems;
    private ListaAdapter adapter = null;
    private String url;

    private View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public void filtrar (String s) {
        //
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        listViewItems = (ListView) findViewById(R.id.listviewitems);
        edtPesq = (MaterialSearchBar) findViewById(R.id.searchBar);

        edtPesq.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filtrar(charSequence.toString());
                Log.w("DIGITADO: ",charSequence.toString());
                if (adapter != null) {
                    adapter.getFilter().filter(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.github.com/search/repositories").newBuilder();
        urlBuilder.addQueryParameter("page", "1");
        urlBuilder.addQueryParameter("q", "language:Java");
        urlBuilder.addQueryParameter("sort", "stars");
        url = urlBuilder.build().toString();

        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void run() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w("Falha: ", "Aquii...");
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Log.w("ret: ", myResponse);
                        //strJson = new StringBuilder();
                        //strJson.append(myResponse);

                        try {
                            JSONObject json = new JSONObject(myResponse.toString());

                            getDados(json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    private void getDados (JSONObject json) {
        JSONArray jsoa = null;
        List<Items> Itens = new ArrayList<Items>();
        try {
            jsoa = json.getJSONArray("items");
            for (int vi = 0; vi < jsoa.length(); vi++) {
                Items it = new Items();
                it.setId(Long.parseLong(jsoa.getJSONObject(vi).getString("id")));
                it.setName(jsoa.getJSONObject(vi).getString("name"));
                it.setFull_name(jsoa.getJSONObject(vi).getString("full_name"));
                it.setDescription(jsoa.getJSONObject(vi).getString("description"));
                it.setSize(jsoa.getJSONObject(vi).getInt("size"));
                it.setStargazers_count(jsoa.getJSONObject(vi).getInt("stargazers_count"));
                it.setAvatar_url(jsoa.getJSONObject(vi).getJSONObject("owner").getString("avatar_url"));
                Itens.add(it);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new ListaAdapter(this, Itens, handler);
        listViewItems.setAdapter(adapter);
    }

}
