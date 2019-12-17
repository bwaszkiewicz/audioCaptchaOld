package pl.benedykt.waszkiewicz.audiocaptcha.text.noise;

import android.graphics.Canvas;

public interface NoiseProducer {
    Canvas getNoise(int width, int height, Canvas canvas);
}
