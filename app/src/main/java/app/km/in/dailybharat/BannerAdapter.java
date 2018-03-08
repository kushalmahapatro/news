package app.km.in.dailybharat;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Sexy_Virus on 08/03/18.
 */

public class BannerAdapter extends PagerAdapter {

    private ArrayList<String> images, title;
    private LayoutInflater inflater;
    private Context context;

    public BannerAdapter(Context context, ArrayList<String> images, ArrayList<String> title) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
        this.title= title;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.starting_banner_child, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);
        TextView myTitle= myImageLayout.findViewById(R.id.headingNews);
        myTitle.setText(title.get(position));
        Glide.with(context).load(images.get(position)).thumbnail(1).centerCrop().into(myImage);
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
