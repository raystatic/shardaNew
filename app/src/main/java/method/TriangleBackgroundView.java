package method;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.shardatech.shardauniversity.R;

/**
 * Created by sharda on 1/16/2018.
 */

public class TriangleBackgroundView extends View {
    Paint paint;
    Paint bgPaint;
    int color1,color2;
    int h=0,w=0;

    public TriangleBackgroundView(Context context) {
        super(context);
        init();
    }

    public TriangleBackgroundView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public void setColors(int color1,int color2, int width, int height) {
        this.color1=color1;
        this.color2=color2;
        h=height;
        this.setMinimumHeight(h);
        init();
        invalidate();
    }

    public TriangleBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TriangleBackgroundView,
                0, 0
        );


        try{
            color1 = a.getColor(R.styleable.TriangleBackgroundView_color1, 0xff000000);
            color2 = a.getColor(R.styleable.TriangleBackgroundView_color2, 0xff000000);
        }
        finally {
            a.recycle();
        }
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color1);

        bgPaint= new Paint();
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setColor(color2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(h==0)
        h = getMeasuredHeight();
        if (w==0)
        w = getMeasuredWidth();

        canvas.drawRect(0,0,w,h,bgPaint);


        Path path = new Path();
        path.moveTo(0, h);
        path.lineTo(w, h);
        path.lineTo(0, 0);
        path.lineTo(0, h);
        path.close();
        canvas.drawPath(path,paint);
    }


    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }
}

