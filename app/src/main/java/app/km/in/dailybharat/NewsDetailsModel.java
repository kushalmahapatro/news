package app.km.in.dailybharat;

/**
 * Created by prasant.p on 1/25/2018.
 */
public class NewsDetailsModel
{
    private int news_id;
    private String news_chenal_name;
    private String news_author;
    private String news_title;
    private String news_description;
    private String news_url;
    private String news_urlToImage;
    private String news_publishedAt;

    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public String getNews_chenal_name() {
        return news_chenal_name;
    }

    public void setNews_chenal_name(String news_chenal_name) {
        this.news_chenal_name = news_chenal_name;
    }

    public String getNews_author() {
        return news_author;
    }

    public void setNews_author(String news_author) {
        this.news_author = news_author;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_description() {
        return news_description;
    }

    public void setNews_description(String news_description) {
        this.news_description = news_description;
    }

    public String getNews_url() {
        return news_url;
    }

    public void setNews_url(String news_url) {
        this.news_url = news_url;
    }

    public String getNews_urlToImage() {
        return news_urlToImage;
    }

    public void setNews_urlToImage(String news_urlToImage) {
        this.news_urlToImage = news_urlToImage;
    }

    public String getNews_publishedAt() {
        return news_publishedAt;
    }

    public void setNews_publishedAt(String news_publishedAt) {
        this.news_publishedAt = news_publishedAt;
    }
}
