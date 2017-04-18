package comqq.example.asus_pc.facedemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by asus-pc on 2017/4/18.
 */

public class FaceView extends View {
    private Paint paint;
    public Rect rect;
    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        Log.e("Main","rect-1");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("Main","rect1");
        if (rect!=null) {
            canvas.drawRect(rect, paint);
            Log.e("Main","rect2");
        }
    }
    public void drawRect(Rect rect){
        this.rect=rect;
        Log.e("Main","rect0");
        invalidate();
    }
}
