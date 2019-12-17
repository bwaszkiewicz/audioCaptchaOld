package pl.benedykt.waszkiewicz.audiocaptcha.text.backgrounds;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface BackgroundProducer {
    public Canvas getBackground(int width, int height, Canvas canvas, int backgroundColor);
}
