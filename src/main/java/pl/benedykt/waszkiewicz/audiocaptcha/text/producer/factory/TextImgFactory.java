package pl.benedykt.waszkiewicz.audiocaptcha.text.producer.factory;

import pl.benedykt.waszkiewicz.audiocaptcha.text.producer.BlurTextImgProducer;
import pl.benedykt.waszkiewicz.audiocaptcha.text.producer.DashTextImgProducer;
import pl.benedykt.waszkiewicz.audiocaptcha.text.producer.DefaultTextImgProducer;
import pl.benedykt.waszkiewicz.audiocaptcha.text.producer.HollowTextImgProducer;
import pl.benedykt.waszkiewicz.audiocaptcha.text.producer.TextImgProducer;
import pl.benedykt.waszkiewicz.audiocaptcha.text.producer.TriangleTextImgProducer;

public class TextImgFactory {
    public TextImgProducer getTextImgProducer(TextImgType type) {
        switch (type) {
            case HOLLOW:
                return new HollowTextImgProducer();
            case DASH:
                return new DashTextImgProducer();
            case TRIANGLE:
                return new TriangleTextImgProducer();
            case BLUR:
                return new BlurTextImgProducer();
            default:
                return new DefaultTextImgProducer();
        }
    }
}
