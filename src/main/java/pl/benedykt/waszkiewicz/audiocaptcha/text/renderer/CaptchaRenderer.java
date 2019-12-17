package pl.benedykt.waszkiewicz.audiocaptcha.text.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import pl.benedykt.waszkiewicz.audiocaptcha.ColorGenerator;
import pl.benedykt.waszkiewicz.audiocaptcha.text.backgrounds.BackgroundProducer;
import pl.benedykt.waszkiewicz.audiocaptcha.text.backgrounds.factory.BackgroundType;
import pl.benedykt.waszkiewicz.audiocaptcha.text.backgrounds.factory.BackgroundFactory;
import pl.benedykt.waszkiewicz.audiocaptcha.text.noise.NoiseProducer;
import pl.benedykt.waszkiewicz.audiocaptcha.text.noise.RectangleNoiseProducer;
import pl.benedykt.waszkiewicz.audiocaptcha.text.producer.TextImgProducer;
import pl.benedykt.waszkiewicz.audiocaptcha.text.producer.factory.TextImgFactory;
import pl.benedykt.waszkiewicz.audiocaptcha.text.producer.factory.TextImgType;

public class CaptchaRenderer extends View {

    private int width;
    private int height;

    private ColorGenerator colorGenerator = ColorGenerator.getInstance();
    private BackgroundProducer backgroundProducer;
    private TextImgProducer textImgProducer;
    private NoiseProducer noiseProducer;
    private String code;

    public CaptchaRenderer(Context context) {
        super(context);
    }

    public CaptchaRenderer(Context context, int width, int height, BackgroundType backgroundType, TextImgType textImgType, String code) {
        super(context);
        BackgroundFactory backgroundFactory = new BackgroundFactory();
        TextImgFactory textImgFactory = new TextImgFactory();

        this.width = width;
        this.height = height;
        this.backgroundProducer = backgroundFactory.getBackgroundProducer(backgroundType);
        this.textImgProducer = textImgFactory.getTextImgProducer(textImgType);
        this.noiseProducer = new RectangleNoiseProducer();
        this.code = code;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        colorGenerator.generateColors();
        canvas = backgroundProducer.getBackground(width, height, canvas, colorGenerator.getBackgroundColor());
        canvas = textImgProducer.getText(width, height, code, canvas, colorGenerator.getTextColor());
        noiseProducer.getNoise(width,height,canvas);
    }
}
