package pl.benedykt.waszkiewicz.audiocaptcha.text.producer;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import java.security.SecureRandom;
import java.util.Random;

public class BlurTextImgProducer implements TextImgProducer {

    private static final Random RAND = new SecureRandom();

    @Override
    public Canvas getText(int width, int height, String text, Canvas canvas, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Path path = new Path();
        BlurMaskFilter filter = new BlurMaskFilter(1.5f, drawBlurType());

        int tSize;
        int tAngle;
        int startX;
        int startY;
        double xPrim;
        double yPrim;
        do {
            startX = RAND.nextInt(45)+10;
            startY = RAND.nextInt(35)+10;
            tSize= RAND.nextInt(30+1)+15;
            tAngle = RAND.nextBoolean() ? RAND.nextInt(15) : -RAND.nextInt(15);

            paint.setColor(textColor);
            paint.setTextSize(tSize);

            Rect rect = new Rect();
            float[] ary = new float[text.length()];
            paint.getTextBounds(text, 0, text.length(), rect);
            paint.getTextWidths(text, 0, text.length(), ary);
            float textWidth = sumArray(ary);

            path.reset();
            paint.getTextPath(text, 0, text.length(), startX, startY, path);
            path.close();

            startX += textWidth;
            startY += tSize;
            xPrim = startX * Math.cos(Math.toRadians(tAngle)) - startY * Math.sin(Math.toRadians(tAngle));
            yPrim = startX * Math.sin(Math.toRadians(tAngle)) + startY * Math.cos(Math.toRadians(tAngle));
        } while(xPrim < width && yPrim < height && xPrim > 0 && yPrim > 0 );

        paint.setMaskFilter(filter);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0.5f);

        canvas.save();
        canvas.rotate(tAngle);
        canvas.translate(paint.getStrokeWidth() / 2, 0);
        canvas.drawPath(path, paint);
        canvas.translate(-paint.getStrokeWidth() / 2, 0);
        return canvas;

    }

    private BlurMaskFilter.Blur drawBlurType() {
        return BlurMaskFilter.Blur.values()[RAND.nextInt(BlurMaskFilter.Blur.values().length-1)];
    }

    private float sumArray(float[] array) {
        float sum = 0;
        for (float value : array) {
            sum += value;
        }
        return sum;
    }


}
