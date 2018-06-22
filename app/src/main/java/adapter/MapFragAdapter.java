package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shardatech.shardauniversity.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import method.CircleTransform;

/**
 * Created by sharda on 9/18/2017.
 */

public class MapFragAdapter extends RecyclerView.Adapter  {
    Context mContext;
    JSONArray arrayMaps;

    public MapFragAdapter(JSONArray arrayMaps, Context context) {
        this.arrayMaps = arrayMaps;
        this.mContext = context;
    }


    public JSONObject getJSON(int position){
        try {
            return arrayMaps.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateMapJson(JSONArray jsonArray){
        this.arrayMaps = jsonArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_map_sub_places, parent, false);
                return new MapPlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        JSONObject object = null;
        try {
            object = arrayMaps.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ((MapPlacesViewHolder) holder).tvMapCategoryItem.setText(object.optString("name"));
        String imageUrl = object.optString("place_image");
        if (imageUrl.length()==0){
            imageUrl="test";
        }
        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.location_on)
                .transform(new CircleTransform())
                .fit()
                .into(((MapPlacesViewHolder) holder).ivMapCategoryImage);
    }

    @Override
    public int getItemCount() {
        return arrayMaps.length();
    }


    public static class MapPlacesViewHolder extends RecyclerView.ViewHolder {
        TextView tvMapCategoryItem;
        ImageView ivMapCategoryImage;
        public MapPlacesViewHolder(View itemView) {
            super(itemView);
            this.tvMapCategoryItem = (TextView) itemView.findViewById(R.id.tv_map_category_item);
            this.ivMapCategoryImage = (ImageView) itemView.findViewById(R.id.iv_map_category_image);
        }
    }



}
