package pl.benedykt.waszkiewicz.audiocaptcha.text.producer;

import android.graphics.Canvas;

public interface TextImgProducer {
    Canvas getText(int width, int height, String text, Canvas canvas, int textColor);
}
