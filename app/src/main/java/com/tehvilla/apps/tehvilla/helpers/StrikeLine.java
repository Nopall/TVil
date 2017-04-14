package com.tehvilla.apps.tehvilla.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tehvilla.apps.tehvilla.R;

/**
 * Created by AkhmadNaufal on 2/15/17.
 */

public class StrikeLine extends TextView {
    private int mColor;
    private Paint paint;

    public StrikeLine (Context context) {
        super(context);
        init(context);
    }

    public StrikeLine (Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StrikeLine (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        Resources resources = context.getResources();
        //Color
        mColor = resources.getColor(R.color.md_red_A400);

        paint = new Paint();
        paint.setColor(mColor);
        //Width
        paint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, 25, getWidth(), 25, paint);
    }
}