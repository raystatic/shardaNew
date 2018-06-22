/*
package adapter;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.sharda.shardauniversity.R;
import com.squareup.picasso.Picasso;

import databaseTable.HomeActivityViewPagerImageTable;
import method.CircleTransform;

public class HomeActivityPagerCursorAdapter extends CursorAdapter
{
    private LayoutInflater mInflater;
    private Context mContext;
    private Resources res;
    Typeface face_thin;
    Typeface face_medium,face_heading;



    public HomeActivityPagerCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        res = mContext.getResources();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder_News holder = new ViewHolder_News();
        View view = mInflater.inflate(R.layout.home_viewpager_item, null);
        holder.image = (ImageView) view.findViewById(R.id.img_pager_item);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        ViewHolder_News holder = (ViewHolder_News) view.getTag();

        String url =cursor.getString(cursor.getColumnIndex(HomeActivityViewPagerImageTable.HOME_PAGER_IMAGE_URL));
        if (url.length()>0){
            Picasso.with(context)
                    .load(url).placeholder(R.drawable.default_bg_icon)
                    .into(holder.image);
        }
        else {
            Picasso.with(context)
                    .load(R.drawable.ic_menu_gallery).placeholder(R.drawable.default_bg_icon)
                    .into(holder.image);
        }
    }




    private    static  class   ViewHolder_News{
        ImageView image;
    }



}
*/
