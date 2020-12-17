package com.itdev181.final181;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FeedViewer extends AppCompatActivity implements RSSLinkAdapter.ItemClicked{
    RecyclerView rvRss;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<String> titles;
    ArrayList<String> links;
    ArrayList<RSSLink> items;

    TextView tvTitle;
    String link;
    String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_viewer);

        tvTitle = findViewById(R.id.tvTitle);

        titles = new ArrayList<String>();
        links = new ArrayList<String>();
        items = new ArrayList<RSSLink>();

        Intent intent = getIntent();
        link = intent.getStringExtra("link");
        title = intent.getStringExtra("title");
        tvTitle.setText(title);
        new ProcessInBackground().execute(link);

        rvRss = findViewById(R.id.rvRss);
        rvRss.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        rvRss.setLayoutManager(layoutManager);

        myAdapter = new RSSLinkAdapter(FeedViewer.this, items);
        rvRss.setAdapter(myAdapter);
    }

    public InputStream getInputStream(URL url){
        try{
            return url.openConnection().getInputStream();
        }catch (IOException e){
            return null;
        }
    }

    @Override
    public void onItemClicked(int position) {
        Uri uri;
        if(links.size()>0)
            uri = Uri.parse(links.get(position));
        else
            uri = Uri.parse("https://www.google.com/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public class ProcessInBackground extends AsyncTask<String, String, Exception>{
        ProgressDialog progressDialog = new ProgressDialog(FeedViewer.this);
        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Loading rss feed...");
            progressDialog.show();
        }

        @Override
        protected Exception doInBackground(String... strings) {
            try{
                URL url = new URL(strings[0]);

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                factory.setNamespaceAware(false);

                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(getInputStream(url), "UTF_8");

                boolean insideItem = false;

                int eventType = xpp.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT){
                    if(eventType == XmlPullParser.START_TAG){
                        if(xpp.getName().equalsIgnoreCase("item")){
                            insideItem = true;
                        }
                        else if(xpp.getName().equalsIgnoreCase("title")){
                            if(insideItem){
                                titles.add(xpp.nextText());
                            }
                        }
                        else if(xpp.getName().equalsIgnoreCase("link")){
                            if(insideItem){
                                links.add(xpp.nextText());
                            }
                        }
                    }
                    if(eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
                        insideItem = false;
                    }

                    eventType = xpp.next();
                }
            }catch (MalformedURLException e){
                exception = e;
            }catch(XmlPullParserException e){
                exception = e;
            }catch(IOException e){
                exception = e;
            }

            return exception;
        }

        @Override
        protected void onPostExecute(Exception e) {
            super.onPostExecute(e);

            if(titles.size() == 0){
                try {
                    items.add(new RSSLink(new URL("https://www.google.com/"), "If you're seeing this item, the link you entered was not a parseable RSS feed."));
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                }
            }else{
                for (int i = 0; i < titles.size(); i++) {
                    try {
                        items.add(new RSSLink(new URL(links.get(i)), titles.get(i)));
                    } catch (MalformedURLException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            rvRss.setAdapter(new RSSLinkAdapter(FeedViewer.this, items));

            progressDialog.dismiss();
        }
    }
}