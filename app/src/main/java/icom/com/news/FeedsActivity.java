package icom.com.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import icom.com.news.Db.Repository;
import icom.com.news.Model.Rss;

public class FeedsActivity extends AppCompatActivity {
    ArrayList<Rss> rsses=null;
    NewsAdapter adapter;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.ic_clear_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        this.setTitle("Favourite News");
        context=this;
        rsses=(ArrayList<Rss>)new Repository(this).GetFavourites();
        adapter=new NewsAdapter(this,rsses);
        ListView list_news=(ListView)findViewById(R.id.list_news);

        list_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rss rss = rsses.get(position);
                Intent i = new Intent(context, NewsActivity.class);
                i.putExtra("title", rss.getTitle());
                i.putExtra("link", rss.getLink());
                i.putExtra("category", rss.getCategory());
                i.putExtra("description",rss.getDescription());
                i.putExtra("pubDate", rss.getPubDate());
                startActivity(i);
            }
        });
        list_news.setAdapter(adapter);

    }

}
