package app.km.in.dailybharat;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Sexy_Virus on 19/02/18.
 */

public class VerticalPagerAdapter extends PagerAdapter {

    String mResources[] = {"Taking nothing away from the young captain, Graeme Smith said South Africa could have looked at JP Duminy, Hashim Amla or AB de Villiers to stand in as captain for the ODIs",
            "As Australia vice-captain Alex Blackwell retires from international and state cricket, her team-mates and opponents - across countries - paid tributes on Twitter to the 34-year old's path-breaking career"};
    String Title[]={"Markram wasn't the right choice as captain - Graeme Smith","'A fighter, leader, record-breaker'"};
    String image[]={"http://a2.espncdn.com/combiner/i?img=%2Fi%2Fcricket%2Fcricinfo%2F1135675_1296x729.jpg","http://a.espncdn.com/combiner/i?img=%2Fi%2Fcricket%2Fcricinfo%2F1112132_1296x729.jpg"};
    Context mContext;
    LayoutInflater mLayoutInflater;
    List<NewsDetailsModel> News;
    String Category;

    public VerticalPagerAdapter(Context context, List<NewsDetailsModel> newsDetailsArrayList, String category) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.News= newsDetailsArrayList;
        this.Category=category;
    }

    @Override
    public int getCount() {
        return News.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.content_main_card, container, false);

        final TextView label = (TextView) itemView.findViewById(R.id.textView);
        final TextView title = (TextView) itemView.findViewById(R.id.textViewMain);
        final ImageView imageView = (ImageView)itemView.findViewById(R.id.imageView);
        final CardView card= (CardView) itemView.findViewById(R.id.card_view);
        final TextView cat= itemView.findViewById(R.id.category);
        final TextView timestamp= itemView.findViewById(R.id.time);
        final TextView author= itemView.findViewById(R.id.author);
        final RelativeLayout button= itemView.findViewById(R.id.list_container);
        final CardView shareButton= itemView.findViewById(R.id.shareCard);
        label.setText(News.get(position).getNews_description());
        title.setText(News.get(position).getNews_title());
        cat.setText(Category);
        if( News.get(position).getNews_publishedAt().contains("+") || News.get(position).getNews_publishedAt().contains("-")){
            try{
                timestamp.setText(""+parseDate(News.get(position).getNews_publishedAt(), "yyyy-MM-dd'T'HH:mm:ssXXX"));}
            catch (Exception e){
                timestamp.setText(News.get(position).getNews_publishedAt());
            }
        }else if(News.get(position).getNews_publishedAt().contains("Z")){
            try{
                timestamp.setText(""+parseDate(News.get(position).getNews_publishedAt(), "yyyy-MM-dd'T'HH:mm:ss'Z'"));}
            catch (Exception e){
                timestamp.setText(News.get(position).getNews_publishedAt());
            }
        }
        if(News.get(position).getNews_author().equals("null"))
            author.setText("No Author");
        else
            author.setText(News.get(position).getNews_author());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra(WebActivity.BUNDLE_IMAGE_ID, News.get(position).getNews_url());
                mContext.startActivity(intent);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenShotAndShareImage(MainActivity.rootView);
            }
        });

        //  Toast.makeText(mContext, ""+label.getText().toString()+"   "+title.getText().toString().trim(), Toast.LENGTH_LONG).show();
      //  int imageDimension = (int) mContext.getResources().getDimension(R.dimen.image_size);
        /*Picasso.with(mContext)
               // .load("https://source.unsplash.com/random")
                .load(News.get(position).getNews_urlToImage())
                .resize(imageDimension, imageDimension)
                .centerCrop()
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        assert imageView != null;
                        imageView.setImageBitmap(bitmap);
                        Palette.from(bitmap)
                                .generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                        //Palette.Swatch textSwatch = palette.getMutedSwatch();
                                        if (textSwatch == null) {
                                          //  Toast.makeText(mContext, "Null swatch :(", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        int color = (textSwatch.getRgb() & 0x00FFFFFF) | 0xFF000000;
                                   //     Toast.makeText(mContext, ""+color, Toast.LENGTH_SHORT).show();
                                      //  card.setCardBackgroundColor(color);
                                      //  title.setTextColor(textSwatch.getTitleTextColor());
                                      //  label.setTextColor(textSwatch.getBodyTextColor());
                                    }
                                });

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        imageView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });*/

        Glide.with(mContext).load(News.get(position).getNews_urlToImage()).centerCrop().placeholder(R.drawable.default1).error(R.drawable.notfound).thumbnail(1).into(imageView);

        container.addView(itemView);

        return itemView;
    }

    private void takeScreenShotAndShareImage(View view) {
        Bitmap screenShot= getScreenShot(view);
        File file= store(screenShot,"DailyBharat.png");
        shareImage(file);

    }
    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static File store(Bitmap bm, String fileName){
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() ;
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private void shareImage(File file){
        Uri uri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".my.package.name.provider", file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            mContext.startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }


    public static Date parseDate(String strDate, String pattern) {
        /* Return object of Date */
        Date date = null;
        /* Get DateFormatter for given pattern */
        try {
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            /* Parse String to java.util.Date */
            date = dateFormat.parse(strDate);
        } catch (ParseException ex) {
            ex.printStackTrace();
            date = null;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        /* Print the StringDate, Pattern and converted java Date */
        System.out.println("Date: " + strDate + ", Pattern: " + pattern + " -> java.util.Date[" + date + "]");
        return date;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}

