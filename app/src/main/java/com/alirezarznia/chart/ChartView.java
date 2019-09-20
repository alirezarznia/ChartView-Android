package com.alirezarznia.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

public class ChartView extends AppCompatImageView {
    private Paint paint;
    private Paint paintLine;
    private static final int DATA_COUNT=2000;
    private float[] data = new float[DATA_COUNT];
    private int xStep =100;
    private float lastDownX=0;
    private float ofset=0;
    private float maxValue =Float.MIN_VALUE;
    private float partialMin= Float.MAX_VALUE;
    private float partialMax= Float.MIN_VALUE;
    private boolean visScale=true;
    private boolean visAxis=true;
    private boolean visNumber=true;
    private boolean visScroll=false;
    /*

     */
    /*

     */

    public void setVisScroll(boolean visScroll) {
        this.visScroll = visScroll;
        if(visScroll){
            autoScroll();
        }
    }

    public void setVisScale(boolean b){
        visScale = b;
    }
    public void setVisAxis(boolean b){
        visAxis = b;
    }
    public void setVisNumber(boolean b){
        visNumber = b;
    }
    private void generateRandomData(){
        for (int i = 0; i < DATA_COUNT; i++) {
            data[i] = (int) (Math.random()*100.f);
           // data[i] = (float) Math.sin(i)*100 +100;
           // data[i] =200+i;
            maxValue=max(maxValue , data[i]);
        }

    }
    private float max(float x, float y){
        if(x>y) return x;
        return  y;
    }

    /*

     */
    public ChartView(Context context) {
        super(context);
        init();
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    /*

     */

    private void init(){
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        paintLine = new Paint();
        paintLine.setColor(Color.GRAY);
        paintLine.setAntiAlias(true);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);
        paintLine.setTextAlign(Paint.Align.CENTER);
        paintLine.setTextSize(40);
        generateRandomData();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*

         */
        float startChart =  (ofset/xStep);
        int startIndex = (int) (ofset/xStep);
        float width = getWidth();
        int possibleRender= (int)(width/xStep);
        int endChart = possibleRender + startIndex + 1;
        if (endChart >= DATA_COUNT) endChart = DATA_COUNT-1;
        computePartialMin(startIndex, endChart);
        computePartialMax(startIndex, endChart);
        /*

         */
        float verticalAxisStep=getHeight()/10.f;
        float halfHeight = getHeight()/2;
        float halfWidth = getWidth()/2;
        if(visAxis) {
            canvas.drawLine(0, halfHeight, getWidth(), halfHeight, paintLine);
            canvas.drawLine(halfWidth, 0, halfWidth, getHeight(), paintLine);
        }
        for (int i = startIndex; i <=endChart ; i++) {
            /*

             */
            if(visAxis) {
                canvas.drawLine((i - startChart) * xStep, halfHeight - 30,
                        (i - startChart) * xStep, halfHeight + 30, paintLine);
            }
            int skipCount = (int) (200.0f/xStep);
            if(visNumber && visAxis) {
                if (i % skipCount == 0) {
                    canvas.drawText("" + i, (i - startChart) * xStep, halfHeight + 100, paintLine);
                }
            }
            /*
            
             */

        }

/*

 */
        for (int i = 0; i < 12; i++) {
            if(visAxis) {
                canvas.drawLine(halfWidth - 30, i * verticalAxisStep,
                        halfWidth + 30, i * verticalAxisStep, paintLine);
               if(visNumber)
                canvas.drawText(String.format("%.2f", conputeInverseY(i * verticalAxisStep)),
                        halfWidth - 100, i * verticalAxisStep, paintLine);
            }


        }
        /*

         */
        /*

         */
        for (int i = startIndex + 1; i <= endChart; i++) {
            canvas.drawLine((i - startChart - 1) * xStep, computeY(data[i-1]),
                    (i - startChart) * xStep, computeY(data[i]), paint);
        }

    }
    /*

     */

    private void computePartialMin(int x, int y){
        partialMin= Float.MAX_VALUE;
        for (int i = x; i <=y ; i++) {
            if(data[i]<partialMin)
                partialMin = data[i];
        }
    }
    private void computePartialMax(int x, int y){
        partialMax= Float.MIN_VALUE;
        for (int i = x; i <=y ; i++) {
            if(data[i]>partialMax)
                partialMax = data[i];
        }
    }

    private  float computeY(float value){
        float height = getHeight();
        float partialHeight= partialMax-partialMin;
        if(visScale){
            return (partialMax-value)/partialHeight*height;
        }
        return  value;
    }

    /*

     */
    private float conputeInverseY(float value){
        float height = getHeight();
        float partialHeight= partialMax-partialMin;
        if(visScale) {
            return partialMax - value * partialHeight / height;
        }
        return value;
    }

    /*

     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{

                lastDownX=event.getX();
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                float curX = event.getX();
                float newOfset=ofset + (lastDownX - curX);
                if(newOfset >=0 && newOfset< DATA_COUNT*xStep) {
                    ofset +=(lastDownX - curX);
                    lastDownX = curX;
                    invalidate();
                }
                break;
            }
        }
        return true;
    }

    private void autoScroll(){
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(30);
                        if(ofset >=0 && ofset< DATA_COUNT*xStep && visScroll) {
                            ofset += 5;
                            postInvalidate();
                        }
                        else{
                            break;

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
        thread.start();
    }
}
