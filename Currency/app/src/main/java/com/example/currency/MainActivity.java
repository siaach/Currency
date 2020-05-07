package com.example.currency;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Document doc;
    private Thread SecondThread;
    private Runnable runnable;

    private ListView listView;
    private CustomArrayAdapter adapter;
    private List<ListItemClass> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


    }

    private void init () {
        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        adapter = new CustomArrayAdapter(this,R.layout.list_item_1, arrayList,getLayoutInflater());
        listView.setAdapter(adapter);

        runnable = new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        };
        SecondThread = new Thread(runnable);
        SecondThread.start();

        /*ListItemClass items = new ListItemClass();
        items.setData_1("Dollar");
        items.setData_2("213" );
        items.setData_3("657");
        items.setData_4("78");
        arrayList.add(items);
        items = new ListItemClass();
        items.setData_1("Dollar");
        items.setData_2("98" );
        items.setData_3("6" );
        items.setData_4("456");
        arrayList.add(items);
        adapter.notifyDataSetChanged();

         */
    }

    private void getWeb () {
        try {
            doc = Jsoup.connect("https://cbr.ru/currency_base/daily/").get();

            Elements tables = doc.getElementsByTag("tbody");
            Element currency = tables.get(0);
            /*
            Elements elements_from_table = currency.children();
            Element dollar = elements_from_table.get(0);
            Elements dollar_elements = dollar.children();


             */

            Log.d("MyLog", "Tbody size: "+ currency.children().get(0).text());

            for (int i = 0; i < currency.childrenSize(); i++) {
                ListItemClass items;
                items = new ListItemClass();
                items.setData_1(currency.children().get(i).child(1).text());
                items.setData_2(currency.children().get(i).child(2).text());
                items.setData_3(currency.children().get(i).child(3).text());
                items.setData_4(currency.children().get(i).child(4).text());
                arrayList.add(items);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
