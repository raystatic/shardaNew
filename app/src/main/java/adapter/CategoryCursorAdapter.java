/*
package adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharda.shardauniversity.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import database.ApplicationContentProvider;
import method.Constant;


public class CategoryCursorAdapter extends CursorAdapter {
    Context mContext;

    public CategoryCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mContext = context;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
*/
/*
        TextView tvEventDate = (TextView) view.findViewById(R.id.textView);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        int imgType = cursor.getInt(cursor.getColumnIndex(EventListDetailsTable.Event_Type));
*//*


*/
/*
        switch (imgType) {
            case 2:
                Picasso.with(context)
                        .load(Constant.getSharedPrefPanchang(mContext, Constant.BASE_URL_OTHER) + Constant.NAVKAR_IMG_URL).placeholder(R.drawable.event_placeholder).transform(new CircleTransform())
                        .into(imageView);
                break;
            case 3:
                Picasso.with(context)
                        .load(Constant.getSharedPrefPanchang(mContext, Constant.BASE_URL_OTHER) + Constant.VIDHAN_IMG_URL).placeholder(R.drawable.event_placeholder).transform(new CircleTransform())
                        .into(imageView);
                break;
            case 4:
                Picasso.with(context)
                        .load(Constant.getSharedPrefPanchang(mContext, Constant.BASE_URL_OTHER) + Constant.KALYANAK_IMG_URL).placeholder(R.drawable.event_placeholder).transform(new CircleTransform())
                        .into(imageView);
                break;
            case 5:
                Picasso.with(context)
                        .load(Constant.getSharedPrefPanchang(mContext, Constant.BASE_URL_OTHER) + Constant.ABHISHEK_IMG_URL).placeholder(R.drawable.event_placeholder).transform(new CircleTransform())
                        .into(imageView);
                break;
            case 6:
                Picasso.with(context)
                        .load(Constant.getSharedPrefPanchang(mContext, Constant.BASE_URL_OTHER) + Constant.KSHAMA_IMG_URL).placeholder(R.drawable.event_placeholder).transform(new CircleTransform())
                        .into(imageView);
                break;
            case 7:
                Picasso.with(context)
                        .load(Constant.getSharedPrefPanchang(mContext, Constant.BASE_URL_OTHER) + Constant.PARYUSHAN_IMG_URL).placeholder(R.drawable.event_placeholder).transform(new CircleTransform())
                        .into(imageView);
                break;
            case 8:
                Picasso.with(context)
                        .load(Constant.getSharedPrefPanchang(mContext, Constant.BASE_URL_OTHER) + Constant.MAHARAAJ_IMG_URL).placeholder(R.drawable.event_placeholder).transform(new CircleTransform())
                        .into(imageView);
                break;
            case 9:
                Picasso.with(context)
                        .load(Constant.getSharedPrefPanchang(mContext, Constant.BASE_URL_OTHER) + Constant.JULUS_RATH_IMG_URL).placeholder(R.drawable.event_placeholder).transform(new CircleTransform())
                        .into(imageView);
                break;
            case 10:
                Picasso.with(context)
                        .load(Constant.getSharedPrefPanchang(mContext, Constant.BASE_URL_OTHER) + Constant.MELA_IMG_URL).placeholder(R.drawable.event_placeholder).transform(new CircleTransform())
                        .into(imageView);
                break;
            case 11:
                Picasso.with(context)
                        .load(Constant.getSharedPrefPanchang(mContext, Constant.BASE_URL_OTHER) + Constant.SANSKRITIK_IMG_URL).placeholder(R.drawable.event_placeholder).transform(new CircleTransform())
                        .into(imageView);
                break;
            default:
                Picasso.with(context)
                        .load(R.drawable.event_placeholder).transform(new CircleTransform())
                        .into(imageView);
                break;
*//*

        }




*/
/*
        String title = cursor.getString(cursor.getColumnIndex(EventListDetailsTable.Event_Title));
        String templeName = cursor.getString(cursor.getColumnIndex(EventListDetailsTable.Event_Temple_Nmae));
        String message = cursor.getString(cursor.getColumnIndex(EventListDetailsTable.Event_Full_Desc));
        Long eventDate = cursor.getLong(cursor.getColumnIndex(EventListDetailsTable.Event_Date));

        final int important = cursor.getInt(cursor.getColumnIndex(EventListDetailsTable.Event_Imp));
        if (important == 1) {
            Picasso.with(context).load(R.mipmap.star_blue).into(imp);
        } else {
            Picasso.with(context).load(R.mipmap.star_outline).into(imp);
        }

        imp.setTag(cursor.getInt(cursor.getColumnIndex(EventListDetailsTable.COLUMN_ID)));
        imp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int important_new = (important + 1) % 2;
                int id = (int) v.getTag();
                ContentValues values = new ContentValues();
                values.put(EventListDetailsTable.Event_Imp, important_new);
                mContext.getContentResolver().update(ApplicationContentProvider.CONTENT_URI.buildUpon().appendPath("" + id).build()
                        , values, null, null);
                notifyDataSetChanged();

            }
        });
*//*



//    }

    public View newView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup) {
        return LayoutInflater.from(paramContext).inflate(R.layout.category_view_item, paramViewGroup, false);
    }
}
*/
