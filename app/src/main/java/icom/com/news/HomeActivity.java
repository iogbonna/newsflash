package icom.com.news;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.LruCache;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import icom.com.news.Db.Repository;
import icom.com.news.Helper.Connector;
import icom.com.news.Helper.DateHelper;
import icom.com.news.Helper.XMLParser;
import icom.com.news.Model.Rss;
import icom.com.news.Util.Cache;
import icom.com.news.Util.GPSTracker;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private String finalUrl = "https://news.google.com/news?cf=all&hl=en&pz=1&output=rss&num=50";//"https://news.google.com/news?cf=all&output=rss&num=20&ned=us";
    private String add_url = "";
    private int offset = 1;
    private String pagination = "&start=";
    private String url = "http://lindaikeji.blogspot.com/feeds/posts/default";
    static final String KEY_ITEM = "item"; // parent node
    static final String KEY_TITLE = "title";
    static final String KEY_LINK = "link";
    static final String KEY_TAG = "tag";
    static final String KEY_PUBDate = "pubDate";
    static final String KEY_DESC = "description";
    static final String KEY_CAT = "category";

    private SwipeRefreshLayout swipeRefreshLayout;
    Context context;
    ListView list_news;
    NewsAdapter adapter;

    private EditText edtSeach;
    private MenuItem mSearchAction;

    private boolean isSearchOpened = false;
    private boolean loadingMore = false;
    private boolean initialLoad = true;

    private  double latitude=0;
    private double longitude=0;

    private String country;

    private LruCache<String, Bitmap> mMemoryCache;


    Toolbar toolbar;
    ArrayList<Rss> rsses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        //StrictMode.setThreadPolicy(policy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        String countryISOCode="";
        //String country="";
        TelephonyManager teleMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        if (teleMgr != null){
            countryISOCode = teleMgr.getSimCountryIso();
            Locale l=new Locale("",countryISOCode.toUpperCase());
            country=l.getDisplayCountry();
        }

        //add_url="&gl="+country;
        String query = null;
        try {
            query = URLEncoder.encode(country, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        add_url="&q="+query;
        this.setTitle("Home");
        //LoadCategory(url,"Home");
        //item.setTitle(country);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //double longitude = location.getLongitude();
        //double latitude = location.getLatitude();


        rsses=new ArrayList<Rss>();
        context=this;
        String xml=new Repository(context).GetLastest();
        adapter=new NewsAdapter(context,rsses);
        Cache.LoadCache();

        //instantiate the swipelayout
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        list_news=(ListView)findViewById(R.id.list_news);
        list_news.setAdapter(adapter);
        //add a footer to the listview
        //View footerView = ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listfooter, null, false);
        //list_news.addFooterView(footerView);
        
        //set what happens when an item in the listview is clicked
        list_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rss rss = rsses.get(position);
                Intent i = new Intent(context, NewsActivity.class);
                i.putExtra("title", rss.getTitle());
                i.putExtra("link", rss.getLink());
                i.putExtra("category", rss.getCategory());
                i.putExtra("pubDate", rss.getPubDate());
                i.putExtra("description", rss.getDescription());
                startActivity(i);
            }
        });


        //manage what actions as the user scrolls to the end of the page
        list_news.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                final int lastItem = firstVisibleItem + visibleItemCount;
                //Toast.makeText(context,visibleItemCount,Toast.LENGTH_LONG).show();
                if (lastItem == totalItemCount && totalItemCount > 0 && loadingMore ==false) {
                    loadingMore=true;
                    offset+=1;
                }
            }
        });

        if (xml != null)
            AddToFeeds(xml,new XMLParser());
        //Toast.makeText(context, country, Toast.LENGTH_LONG).show();
        //LoadNews(list_news);



        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);

                    LoadNews();
                }
            }
        );

    }




    private void LoadNews(){

        if (Connector.isConnectingToInternet(this)){
            rsses=new ArrayList<Rss>();
            new LoadNewsFeed().execute();

        }
        else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, "Mobile device disconnected.", Toast.LENGTH_LONG).show();

        }

        //list_news.setAdapter(adapter);
        //swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        if(isSearchOpened) {
            handleMenuSearch();
            return;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.menu_item_search);




        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);



        //return super.onCreateOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id==R.id.menu_item_search)
        {
            handleMenuSearch();
        }

        return super.onOptionsItemSelected(item);
    }

    private void LoadCategory(String url,String title){
        swipeRefreshLayout.setRefreshing(true);
        toolbar.setTitle(title);
        add_url=url;
        LoadNews();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sports) {
            // Handle the camera action
           LoadCategory("&topic=s","Sports");
        } else if (id == R.id.nav_entertainment) {
            LoadCategory("&topic=e", "Entertainment");
        } else if (id == R.id.nav_technology) {
            LoadCategory("&topic=tc", "Technology");
        } else if (id == R.id.nav_science) {
            LoadCategory("&topic=snc", "Science");
        } else if (id == R.id.nav_health) {
            LoadCategory("&topic=m", "Health");
        } else if (id == R.id.nav_favourites) {
            Intent i=new Intent(this,FeedsActivity.class);
            startActivity(i);
        }
        else if (id==R.id.nav_home){
            try {
                String query = URLEncoder.encode(country, "utf-8");
                url="&q="+query;
                LoadCategory(url,"Home");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        else if (id==R.id.nav_top_stories){
            LoadCategory("","World");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onRefresh() {
        rsses=new ArrayList<Rss>();
        initialLoad=true;
        LoadNews();
    }


    private class LoadNewsFeed extends AsyncTask<Void, Void, String> {
        XMLParser parser=new XMLParser();
        @Override
        protected String doInBackground(Void... params) {


            String xml=parser.getXmlFromUrl(finalUrl+add_url);
            new Repository(context).saveLastFeed(xml);
            return xml;
        }

        @Override
        protected void onPostExecute(String result) {
            AddToFeeds(result, parser);

            //initialLoad=false;
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private void AddToFeeds(String result,XMLParser parser){

        try {
            Document doc=parser.getDomElement(result);

            NodeList nl=doc.getElementsByTagName(KEY_ITEM);

            for (int i=0;i<nl.getLength(); i++){
                Element e=(Element)nl.item(i);
                Rss rss=new Rss();
                rss.setTitle(parser.getValue(e,KEY_TITLE));
                rss.setDescription(parser.getValue(e, KEY_DESC));
                rss.setLink(parser.getValue(e, KEY_LINK));
                rss.setPubDate(DateHelper.convertToDate(parser.getValue(e, KEY_PUBDate)));
                rss.setCategory(parser.getValue(e,KEY_CAT));
                rsses.add(rss);
            }
        }
        catch (NullPointerException e){

        }

        adapter=new NewsAdapter(context,rsses);
        list_news.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        /*if (initialLoad==true) {
            list_news.setAdapter(adapter);
            initialLoad=false;
        }
        else {
            adapter.notifyDataSetChanged();
            loadingMore=false;
        }*/
        swipeRefreshLayout.setRefreshing(false);
    }

    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar

        if(isSearchOpened){ //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_white_24dp));

            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            edtSeach = (EditText)action.getCustomView().findViewById(R.id.edtSearch); //the text editor

            //this is a listener to do a search when the user clicks on search button
            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        doSearch();
                        return true;
                    }
                    return false;
                }
            });


            edtSeach.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);


            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_clear_white_24dp));

            isSearchOpened = true;
        }
    }

    private void doSearch() {
        try {
            String query = URLEncoder.encode(edtSeach.getText().toString(), "utf-8");
            url="&q="+query;
            LoadCategory(url,"Search result");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Toast.makeText(this,edtSeach.getText().toString(),Toast.LENGTH_LONG).show();
    }
}
