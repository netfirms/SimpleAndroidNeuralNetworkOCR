package com.pranburiorchard.netfirms.simpleandroidocr.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by taweechai on 16/12/2560.
 */

public class BitmapUtils {

    public static Bitmap convertToBinaryImage(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);
                // use 128 as threshold, above -> white, below -> black
                if (gray > 128) {
                    gray = 255;
                } else {
                    gray = 0;
                }
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, gray, gray, gray));
            }
        }
        return bmOut;
    }

    public static Bitmap cropCharacterArea(Bitmap bitmap2) {
        int minX = bitmap2.getWidth(), maxX = 0, minY = bitmap2.getHeight(), maxY = 0;
        int color;
        int scalingVector = 3;
        for (int x = 0; x < bitmap2.getWidth(); x++) {
            for (int y = 0; y < bitmap2.getHeight(); y++) {
                color = bitmap2.getPixel(x, y);
                if (color == Color.BLACK) {
                    if (minY > y) {
                        minY = y - scalingVector;
                    }
                    if (maxY < y) {
                        maxY = y + scalingVector;
                    }
                    if (minX > x) {
                        minX = x - scalingVector;
                    }
                    if (maxX < x) {
                        maxX = x + scalingVector;
                    }
                }
                y += scalingVector;
            }
            x += scalingVector;
        }
        int width = maxX - minX;
        int height = maxY - minY;
        if (width > 0 && height > 0)
            bitmap2 = Bitmap.createBitmap(bitmap2, minX, minY, width, height);

        return bitmap2;
    }
}
