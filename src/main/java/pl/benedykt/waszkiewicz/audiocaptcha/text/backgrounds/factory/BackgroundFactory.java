package pl.benedykt.waszkiewicz.audiocaptcha.text.backgrounds.factory;


import pl.benedykt.waszkiewicz.audiocaptcha.text.backgrounds.BackgroundProducer;
import pl.benedykt.waszkiewicz.audiocaptcha.text.backgrounds.BlurBackgroundProducer;
import pl.benedykt.waszkiewicz.audiocaptcha.text.backgrounds.FlatColorBackgroundProducer;

public class BackgroundFactory {
    public BackgroundProducer getBackgroundProducer(BackgroundType type) {
        switch (type) {
            case BLUR:
                return new BlurBackgroundProducer();
            case FLAT:
                return new FlatColorBackgroundProducer();
            default:
                return new FlatColorBackgroundProducer();
        }
    }
}
