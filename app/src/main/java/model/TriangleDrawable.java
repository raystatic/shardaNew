package model;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

/**
 * Created by sharda on 1/16/2018.
 */

public class TriangleDrawable extends Drawable {

    private int[] themeColors;

    public TriangleDrawable(int[] themeColors) {
        this.themeColors = themeColors;
    }

    @Override
    public void draw(Canvas canvas) {

        // get drawable dimensions
        Rect bounds = getBounds();
        int w = bounds.right - bounds.left;
        int h = bounds.bottom - bounds.top;
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(themeColors[0]);
        canvas.drawRect(0,0,w,h,backgroundPaint);
        backgroundPaint.setColor(themeColors[1]);
        Path path = new Path();
        path.moveTo(0, h);
        path.lineTo(w, h);
        path.lineTo(0, 0);
        path.lineTo(0, h);
        path.close();
        canvas.drawPath(path,backgroundPaint);
    }

    @Override
    public void setAlpha(int alpha) {
    }
    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }
    @Override
    public int getOpacity() {
        return 0;
    }

}