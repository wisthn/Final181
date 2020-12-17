package com.itdev181.final181;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements LinkAdapter.ItemClicked{
    RecyclerView rvLinks;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;

    EditText etName;
    EditText etURL;
    Button btnSubmit;
    Button btnSave;

    ArrayList<Link> links;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etURL = findViewById(R.id.etURL);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSave = findViewById(R.id.btnSave);

        rvLinks = findViewById(R.id.rvLinks);
        rvLinks.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        rvLinks.setLayoutManager(layoutManager);

        links = new ArrayList<Link>();

        loadLinks();

        if(links.size() == 0) {
            try {
                links.add(new Link(new URL("https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml"), "The New York Times"));
                links.add(new Link(new URL("https://www.nasa.gov/rss/dyn/breaking_news.rss"), "NASA Breaking News"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        myAdapter = new LinkAdapter(this, links);
        rvLinks.setAdapter(myAdapter);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etName.getText().length() > 0 && etURL.getText().length() > 0){
                    try {
                        links.add(new Link(new URL(etURL.getText().toString()), etName.getText().toString()));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLinks();
            }
        });
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(this, FeedViewer.class);
        intent.putExtra("link", links.get(position).getUrl().toString());
        intent.putExtra("title", links.get(position).getName().toString());
        startActivity(intent);
    }

    public void saveLinks(){
        try{
            FileOutputStream file = openFileOutput("Links.txt", MODE_PRIVATE);
            OutputStreamWriter outputFile = new OutputStreamWriter(file);

            for(int i=0; i<links.size(); i++){
                outputFile.write(links.get(i).getUrl().toString() + "," + links.get(i).getName().toString() + "\n");
            }

            outputFile.flush();
            outputFile.close();

            Toast.makeText(MainActivity.this, "Successfully saved!", Toast.LENGTH_SHORT).show();
        }catch(IOException e){
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void loadLinks(){
        links.clear();
        File file = getApplicationContext().getFileStreamPath("Links.txt");
        String lineFromFile;

        if(file.exists()){
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("Links.txt")));
                while((lineFromFile = reader.readLine()) != null){
                    StringTokenizer tokens = new StringTokenizer(lineFromFile, ",");
                    Link link = new Link(new URL(tokens.nextToken()), tokens.nextToken());
                    links.add(link);
                }
                reader.close();
            }catch (IOException e){
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}