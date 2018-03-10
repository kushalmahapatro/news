package app.km.in.dailybharat;

/**
 * Created by Sexy_Virus on 08/03/18.
 */

public class ImagesModel {
    private String imageUrl;
    private String newsTitle;

    public ImagesModel(String url, String title){
        this.imageUrl=url;
        this.newsTitle=title;
    }

    public String getImageUrl(){
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNewsTitle(){
        return this.newsTitle;
    }

    public void setNewsTitle(String title){
        this.newsTitle=title;
    }
}
