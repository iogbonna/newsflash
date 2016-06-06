package icom.com.news.Model;

import java.util.Date;

/**
 * Created by Charlie on 11/30/2015.
 */
public class Rss {
    public String Title;
    public String Description;
    public String Link;
    public String Image;
    public int Id;
    public Date PubDate;
    public String Category;

    public Rss() {
    }

    public Rss(String title, String description, String link) {
        Title = title;
        Description = description;
        Link = link;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public Date getPubDate() {
        return PubDate;
    }

    public void setPubDate(Date pubDate) {
        PubDate = pubDate;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
