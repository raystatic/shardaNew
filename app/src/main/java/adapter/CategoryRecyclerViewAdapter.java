package adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shardatech.shardauniversity.R;
import com.shardatech.shardauniversity.SubCategoryActivity;
import com.squareup.picasso.Picasso;

import databaseTable.HomeActivityCategoryTable;

/**
 * Created by sharda on 7/18/2017.
 */

public class CategoryRecyclerViewAdapter extends RecyclerViewCursorAdapter<CategoryRecyclerViewAdapter.ViewHolder> {

    Context mContext;
    Cursor mCursor;

    public CategoryRecyclerViewAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mCursor=cursor;
        this.mContext=context;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        viewHolder.setData(cursor);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_screen_cat_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvCategoryItem;
        public ImageView ivCategoryImage;
        public RelativeLayout rlCategory;
        public FrameLayout categoryLayout;
        Cursor cursor;
        int categoryId;
        String categoryColor;
        String categoryName;
        String categoryCode;
        method.TriangleBackgroundView triangleBackground;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            tvCategoryItem = (TextView) v.findViewById(R.id.tv_category_item);
            ivCategoryImage = (ImageView) v.findViewById(R.id.iv_category_image);
//            rlCategory = (RelativeLayout) v.findViewById(R.id.rl_category);
            categoryLayout = (FrameLayout) v.findViewById(R.id.category_layout);
            triangleBackground = (method.TriangleBackgroundView) v.findViewById(R.id.triangle_background);
        }

        public void setData(Cursor cursor) {
            this.cursor = cursor;
            this.categoryId = cursor.getInt(cursor.getColumnIndex(HomeActivityCategoryTable.CATEGORY_Id));
            this.categoryName = cursor.getString(cursor.getColumnIndex(HomeActivityCategoryTable.CATEGORY_NAME));
            this.categoryColor = cursor.getString(cursor.getColumnIndex(HomeActivityCategoryTable.CATEGORY_COLOR));
            this.categoryCode = cursor.getString(cursor.getColumnIndex(HomeActivityCategoryTable.CATEGORY_CODE));

            String url = (cursor.getString(cursor.getColumnIndex(HomeActivityCategoryTable.CATEGORY_IMAGE_URL)));
            if (url.length() > 0) {
                Picasso.with(mContext)
                        .load(url)
                        .placeholder(R.drawable.default_bg_icon)
                        .into(ivCategoryImage);
            } else {
                Picasso.with(mContext)
                        .load(R.drawable.ic_menu_gallery)
                        .placeholder(R.drawable.default_bg_icon)
                        .into(ivCategoryImage);
            }
            categoryLayout.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            tvCategoryItem.setText(cursor.getString(cursor.getColumnIndex(HomeActivityCategoryTable.CATEGORY_NAME)));
            triangleBackground.setColors(Color.parseColor(cursor.getString(cursor.getColumnIndex(HomeActivityCategoryTable.CATEGORY_COLOR_BG_LIGHT))),
                    Color.parseColor(cursor.getString(cursor.getColumnIndex(HomeActivityCategoryTable.CATEGORY_COLOR_BG_DARK))),
                    categoryLayout.getMeasuredWidth(),
                    categoryLayout.getMeasuredHeight());
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == categoryLayout.getId()){
                Intent intent = new Intent(mContext, SubCategoryActivity.class);
                intent.putExtra(HomeActivityCategoryTable.CATEGORY_Id,categoryId);
                intent.putExtra(HomeActivityCategoryTable.CATEGORY_NAME,categoryName);
                intent.putExtra(HomeActivityCategoryTable.CATEGORY_COLOR,categoryColor);
                intent.putExtra(HomeActivityCategoryTable.CATEGORY_CODE,categoryCode);
                mContext.startActivity(intent);
            }
        }

    }
}