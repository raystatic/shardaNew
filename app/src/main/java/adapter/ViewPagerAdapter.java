package adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shardatech.shardauniversity.R;
import com.squareup.picasso.Picasso;

import databaseTable.HomeActivityViewPagerImageTable;

/**
 * Created by sharda on 7/18/2017.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    Cursor cursor;

    public void swapCursor(Cursor cursor){
        this.cursor=cursor;
        notifyDataSetChanged();
    }

    public ViewPagerAdapter(Context mContext, Cursor cursor) {
        this.mContext = mContext;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor==null?0:cursor.getCount();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.home_screen_slider_item, container, false);

        ImageView imageView     = (ImageView) itemView.findViewById(R.id.img_pager_item);
        ImageView imageLocation = (ImageView) itemView.findViewById(R.id.iv_location);
        TextView pagerTitle     = (TextView) itemView.findViewById(R.id.tv_pager_image_title);
        TextView pagerTime      = (TextView) itemView.findViewById(R.id.tv_pager_image_time);
        TextView pagerAddress   = (TextView) itemView.findViewById(R.id.tv_pager_image_add);

        cursor.moveToPosition(position);
        Log.d("position",position+"");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position)
                {
                    case 0: openBrowser("https://www.sharda.ac.in/");
                        break;
                    case 1: openBrowser("http://www.du.ac.in");
                        break;
                    case 2: openBrowser("http://www.iitd.ac.in");
                        break;
                    case 3: openBrowser("http://keshav.du.ac.in");
                        break;
                }
            }
        });



        String url = (cursor.getString(cursor.getColumnIndex(HomeActivityViewPagerImageTable.HOME_PAGER_IMAGE_URL)));

        pagerTitle.setText(cursor.getString(cursor.getColumnIndex(HomeActivityViewPagerImageTable.HOME_PAGER_IMAGE_TITLE)));
        pagerTime.setText(cursor.getString(cursor.getColumnIndex(HomeActivityViewPagerImageTable.HOME_PAGER_IMAGE_TIME)));
        pagerAddress.setText(cursor.getString(cursor.getColumnIndex(HomeActivityViewPagerImageTable.HOME_PAGER_IMAGE_ADDRESS)));

        if (url.length()>0){
            Picasso.with(mContext)
                    .load(url)
                    .placeholder(R.drawable.default_bg_icon)
                    .into(imageView);
        }
        else {
            Picasso.with(mContext)
                    .load(R.drawable.ic_menu_gallery)
                    .placeholder(R.drawable.default_bg_icon)
                    .into(imageView);
        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public void openBrowser(String url)
    {
        Uri uri=Uri.parse(url);
        Intent intent =new Intent(Intent.ACTION_VIEW,uri);
        mContext.startActivity(intent);
    }
}