package app.km.in.dailybharat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Sexy_Virus on 08/03/18.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    ArrayList<ImagesModel> Images;
    Context mContext;

    public RecyclerAdapter(Context context,ArrayList<ImagesModel> images){
        this.mContext=context;
        this.Images=images;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_card,parent,false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(mContext).load(Images.get(position).getImageUrl()).thumbnail(1).centerCrop().into(holder.image);
    }

    @Override
    public int getItemCount() {
        return Images.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            image= itemView.findViewById(R.id.card_image);
        }
    }
}
