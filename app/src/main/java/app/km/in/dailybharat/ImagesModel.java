package app.km.in.dailybharat;

/**
 * Created by Sexy_Virus on 08/03/18.
 */

public class ImagesModel {
    private String imageUrl;

    public ImagesModel(String url){
        this.imageUrl=url;
    }

    public String getImageUrl(){
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
