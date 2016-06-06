package icom.com.news;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

import icom.com.news.Helper.DateHelper;
import icom.com.news.Helper.DownloadImageTask;
import icom.com.news.Model.Rss;

/**
 * Created by Charlie on 11/30/2015.
 */
public class NewsAdapter  extends BaseAdapter {
    private final Context context;
    private final ArrayList<Rss> feeds;
    //ImageLoader imageLoader;

    public NewsAdapter(Context context, ArrayList<Rss> feeds){
        this.context=context;
        this.feeds=feeds;
        //imageLoader=new ImageLoader(context);
    }

    @Override
    public int getCount() {

        return feeds.size();
    }

    @Override
    public Object getItem(int index) {

        return feeds.get(index);
    }

    @Override
    public long getItemId(int index) {

        return (long)feeds.get(index).getId();
    }

    @Override
    public View getView(int index, View view, ViewGroup arg2) {
        ViewHolder holder;
        //View view=vi;
            if (view==null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.template_news, null);
                holder=new ViewHolder();
                holder.nImage=(ImageView)view.findViewById(R.id.img_news);
                holder.nTitle=(TextView)view.findViewById(R.id.title);
                holder.nDescription=(TextView)view.findViewById(R.id.d);
                //holder.nDate=(TextView)view.findViewById(R.id.time);
                holder.nCategory=(TextView)view.findViewById(R.id.category);
                holder.nInitial=(TextView)view.findViewById(R.id.initial);
                view.setTag(holder);
            }
            else{
                holder=(ViewHolder)view.getTag();
            }

            Rss feed=(Rss)getItem(index);

            if (feed != null) {
                Document document = Jsoup.parse(feed.getDescription());
                //document.select("img").removeAll();
                Element imageElement = document.select("img").first();
                String absoluteUrl = imageElement.attr("src");

                Element dE=document.select("div").get(1);
                dE.select("a").first().remove();
                dE.select("font").first().remove();

                String[] values = feed.getTitle().split("-");

                String source="";
                holder.nImage.setImageResource(R.drawable.news);
                //holder.nDate.setText(DateHelper.Format(feed.getPubDate()));
                holder.nCategory.setText(feed.getCategory());
                if (values.length == 2) {
                    source=(values[1]);
                    holder.nTitle.setText(values[0]);
                } else if (values.length > 2) {
                    source=(values[values.length - 1]);
                    holder.nTitle.setText(feed.getTitle().substring(0, (feed.getTitle().length() - (values[values.length - 1].length() + 1))));
                }


                //holder.nDescription.setText(description.substring(source.length()-1,300)+"...");
                //description=description.substring(source.length()-1,195).trim()+"...";
                holder.nDescription.setText(dE.text().substring(0,197)+"...");
                holder.nInitial.setText(source.toUpperCase());
                //title.setText(absoluteUrl);

                new DownloadImageTask(holder.nImage).execute("http:" + absoluteUrl);
                //imageLoader.DisplayImage("http:" + absoluteUrl,holder.nImage);
            }

        //}



        return view;
    }


    private static class ViewHolder {

        //private Map<String, Bitmap> mBitmapCache = new HashMap<String, Bitmap>();
        TextView nTitle;
        TextView nDate;
        TextView nCategory;
        ImageView nImage;
        TextView nDescription;
        TextView nInitial;

    }


}
