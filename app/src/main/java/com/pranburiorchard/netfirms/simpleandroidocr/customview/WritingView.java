package com.pranburiorchard.netfirms.simpleandroidocr.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.pranburiorchard.netfirms.simpleandroidocr.R;
import com.pranburiorchard.netfirms.simpleandroidocr.utils.BitmapUtils;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.imgrec.ImageRecognitionPlugin;
import org.neuroph.ocr.util.OCRUtilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by taweechai on 11/23/2017 AD.
 */

public class WritingView extends View {

    private Paint mPaint;
    private Path mPath;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Context context;

    private NeuralNetwork nnet;
    private ImageRecognitionPlugin imageRecognition;
    private Runnable loadDataRunnable = new Runnable() {
        public void run() {
            InputStream irs = getResources().openRawResource(R.raw.m1);
            nnet = NeuralNetwork.load(irs);
        }
    };

    public WritingView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public WritingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public void init() {
        loadData();
        // Init Neural Network instance
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(50);
        mPath = new Path();
        mCanvas = new Canvas();
        DisplayMetrics metrics = this.context.getResources().getDisplayMetrics();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(event.getX(), event.getY());
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    public void clear() {
        mPath.reset();
        invalidate();
    }

    public String testNN() {
        this.setDrawingCacheEnabled(true);
        //Below block of code use for local network testing
        /*InputStream is = null;
        try {
            is = getResources().getAssets().open("n2_1.png");// internal testing with static image
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap  bitmap = BitmapFactory.decodeStream(is);
        */
        Bitmap b2 = Bitmap.createScaledBitmap(BitmapUtils.cropCharacterArea(getDrawingCache()), 40, 40, false);
        Bitmap newBitmap = Bitmap.createBitmap(b2.getWidth(), b2.getHeight(), b2.getConfig());
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(b2, 0, 0, null);
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mathWithFriendsTmp";
        File dir = new File(dirPath);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, "HandWriting.bmp");
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            newBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setDrawingCacheEnabled(false);
        return characterVerify(file);
    }

    private String characterVerify(File image) {
        HashMap<String, Double> output = null;
        try {
            imageRecognition = (ImageRecognitionPlugin) nnet.getPlugin(ImageRecognitionPlugin.class);
            output = imageRecognition.recognizeImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getAnswer(output);
    }

    private void loadData() {
        new Thread(null, loadDataRunnable, "dataLoader", 320000).start();
    }

    private String getAnswer(HashMap<String, Double> output) {
        return OCRUtilities.getCharacter(output);
    }
}