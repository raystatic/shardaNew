package adapter;

import android.annotation.SuppressLint;
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

import databaseTable.CampusDirectoryHeadingTable;

/**
 * Created by sharda on 9/18/2017.
 */

public class DirectoryFilterActivityAdapter extends RecyclerViewCursorAdapter<DirectoryFilterActivityAdapter.ViewHolder> {
    Context mContext;
    Cursor mCursor;
    HashMap<Integer, Integer> categoryIdHashmap;
    String color;

    public DirectoryFilterActivityAdapter(Context context, Cursor cursor, HashMap<Integer, Integer> categoryIdHashmap,String color) {
        super(context, cursor);
        this.mCursor=cursor;
        this.mContext=context;
        this.categoryIdHashmap=categoryIdHashmap;
        this.color = color;
    }

    @Override
    public void onBindViewHolder(DirectoryFilterActivityAdapter.ViewHolder viewHolder, Cursor cursor) {
        viewHolder.setData(cursor);
    }

    @Override
    public DirectoryFilterActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.campus_directory_filter_screen, parent, false);
        DirectoryFilterActivityAdapter.ViewHolder vh = new DirectoryFilterActivityAdapter.ViewHolder(itemView);
        return vh;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        Cursor cursor;
        CheckBox checkBoxMapFilter;
        TextView tvFilterName;
        String category, categoryName;
        int id;

        @SuppressLint("NewApi")
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            this.checkBoxMapFilter      = (CheckBox) itemView.findViewById(R.id.checkBoxMapFilter);
            this.tvFilterName           = (TextView) itemView.findViewById(R.id.tv_map_main_place_category);
        }


        public void setData(Cursor cursor) {
            this.cursor         = cursor;
            if (categoryIdHashmap.containsKey(cursor.getInt(cursor.getColumnIndex(CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_Id)))){
                this.checkBoxMapFilter.setChecked(true);
            }
            this.category = cursor.getString(cursor.getColumnIndex(CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_NAME));
            this.categoryName= cursor.getString(cursor.getColumnIndex(CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_NAME));
            this.tvFilterName.setText(cursor.getString(cursor.getColumnIndex(CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_NAME)));
            this.id = cursor.getInt(cursor.getColumnIndex(CampusDirectoryHeadingTable.CAMPUS_DIRECTORY_HEADING_Id));
        }

        @Override
        public void onClick(View v) {
            if (checkBoxMapFilter.isChecked()){
                checkBoxMapFilter.setChecked(false);
                categoryIdHashmap.remove(id);
            }
            else{
                checkBoxMapFilter.setChecked(true);
                categoryIdHashmap.put(id,id);
            }
        }
    }
}
