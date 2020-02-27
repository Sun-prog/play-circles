package com.example.rgb;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Canvas;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class CanvasView extends View implements ICanvasView{
    private GameManager gameManager;
private  static  int height;
private static  int width;
    private Paint paint;
    private Canvas canvas;
    private Toast toast;

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
initHeightAndWidth(context);
        initPaint();
        gameManager = new GameManager(this,width,height);
    }
    private void initPaint(){
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }
    private void initHeightAndWidth(Context context) {
        WindowManager windowManager = (WindowManager)this.getContext().getSystemService(Context.WINDOW_SERVICE);
        /*
        Display display1 = windowManager.getDefaultDisplay();
        Point point = new Point();
        // меняет координату внутри точки
        display.getSize(point);
        width = point.x;
        height = point.y;
*/
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealSize(size);
            }
        } catch (NoSuchMethodError err) { display.getSize(size); }
         width = size.x;
         height = size.y;

    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
       // gameManager.onDraw(canvas);
this.canvas = canvas;
gameManager.onDraw();
    }

    @Override
    public void drawCircle(SimpleCircle circle) {
        paint.setColor(circle.getColor());
canvas.drawCircle(circle.getX(),circle.getY(),circle.getRadius(),paint);
    }

    @Override
    public void redraw() {
        invalidate();
    }

    @Override
    public void showMessage(String text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        if (event.getAction()==MotionEvent.ACTION_MOVE){
            gameManager.onTouchEvent(x,y);
        }
        invalidate();
        return true;
    }
}
