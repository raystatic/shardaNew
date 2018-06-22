package adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shardatech.shardauniversity.R;

import java.util.HashMap;

import databaseTable.MapTable;

/**
 * Created by sharda on 9/18/2017.
 */

public class MapFilterActivityAdapter extends RecyclerViewCursorAdapter<MapFilterActivityAdapter.ViewHolder> {
    Context mContext;
    Cursor mCursor;
    HashMap<String, String> categoryIdHashmap;

    public MapFilterActivityAdapter(Context context, Cursor cursor, HashMap<String,String> categoryIdHashmap) {
        super(context, cursor);
        this.mCursor=cursor;
        this.mContext=context;
        this.categoryIdHashmap=categoryIdHashmap;
    }

    @Override
    public void onBindViewHolder(MapFilterActivityAdapter.ViewHolder viewHolder, Cursor cursor) {
        viewHolder.setData(cursor);
    }

    @Override
    public MapFilterActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.places_map_filter_screen, parent, false);
        MapFilterActivityAdapter.ViewHolder vh = new MapFilterActivityAdapter.ViewHolder(itemView);
        return vh;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        Cursor cursor;
        CheckBox checkBoxMapFilter;
        TextView tvFilterName;
        String category, categoryName;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            this.checkBoxMapFilter      = (CheckBox) itemView.findViewById(R.id.checkBoxMapFilter);
            this.tvFilterName           = (TextView) itemView.findViewById(R.id.tv_map_main_place_category);
        }


        public void setData(Cursor cursor) {
            this.cursor         = cursor;
            if (categoryIdHashmap.containsKey(cursor.getString(cursor.getColumnIndex(MapTable.MAP_PLACE_CATEGORY)))){
                this.checkBoxMapFilter.setChecked(true);
            }
            this.category = cursor.getString(cursor.getColumnIndex(MapTable.MAP_PLACE_CATEGORY));
            this.categoryName= cursor.getString(cursor.getColumnIndex(MapTable.MAP_PLACE_CATEGORY_NAME));
            this.tvFilterName.setText(cursor.getString(cursor.getColumnIndex(MapTable.MAP_PLACE_CATEGORY_NAME)));
        }

        @Override
        public void onClick(View v) {
            if (checkBoxMapFilter.isChecked()){
                checkBoxMapFilter.setChecked(false);
                categoryIdHashmap.remove(category);
            }
            else{
                checkBoxMapFilter.setChecked(true);
                categoryIdHashmap.put(category,category);
            }
        }
    }
}
