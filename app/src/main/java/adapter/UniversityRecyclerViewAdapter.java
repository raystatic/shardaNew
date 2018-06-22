package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.R;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.shardatech.shardauniversity.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rahul on 9/5/18.
 */

public class UniversityRecyclerViewAdapter extends RecyclerView.Adapter<UniversityRecyclerViewAdapter.MyViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<String> image,icon,location,name,phone,enroll;

    public UniversityRecyclerViewAdapter(Context context, ArrayList<String> image, ArrayList<String> icon, ArrayList<String> location, ArrayList<String> name) {
        this.context = context;
        this.image = image;
        this.icon = icon;
        this.location = location;
        this.name = name;
//        this.phone = phone;
//        this.enroll = enroll;
        //inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.uni_adapter_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Picasso.with(context)
                .load(image.get(position))
                .fit()
                .into(holder.collegeImage);
        Picasso.with(context)
                .load(icon.get(position))
                .fit()
                .into(holder.icon);

//        holder.favorite.setFavorite(universityData.getUserLiked());
        holder.collegeName.setText(name.get(position));
        //Log.d("imageurl",universityData.getFeatureImage()+"");
        holder.location.setText(location.get(position));
        //holder.rating.setText();
        //holder.views.setText((r.nextInt(1000-400) + 400)+" views");
//        if(universityData.getOwnedType().equals("PUB"))
//            holder.type.setText("Public");
//        else
//            holder.type.setText("Private");
//        if(universityData.getAdmissionProcedure().equals("M"))
//            holder.merit.setText("Merit");
//        else
//            holder.merit.setText("Entrance");
    }

    @Override
    public int getItemCount() {
        return image.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        View parent;
        TextView collegeName;
        TextView location;
        ImageView icon;
        TextView merit;
        TextView type;
        TextView rating;
        ImageView collegeImage;
        TextView views;
        MaterialFavoriteButton favorite;
        RelativeLayout call;
        LinearLayout enroll;

        public MyViewHolder(View itemView) {
            super(itemView);

            favorite = itemView.findViewById(R.id.likeButton);
            call = itemView.findViewById(R.id.cal_btn);
            icon = itemView.findViewById(R.id.imageView2);
            merit = itemView.findViewById(R.id.tv_KM_tab4);
            type = itemView.findViewById(R.id.type);
            // views = view.findViewById(R.id.views);
            rating = itemView.findViewById(R.id.ratingtext);
            location = itemView.findViewById(R.id.location);
            collegeName = itemView.findViewById(R.id.textView);
            collegeImage = itemView.findViewById(R.id.imageView);
            enroll = itemView.findViewById(R.id.enrollLayout);
            this.parent = itemView;


        }
    }

}
