package pl.benedykt.waszkiewicz.audiocaptcha;

import android.view.View;

import pl.benedykt.waszkiewicz.audiocaptcha.controller.AudioCaptchaViewControllerImpl;
import pl.benedykt.waszkiewicz.audiocaptcha.controller.TextCaptchaViewControllerImpl;
import pl.benedykt.waszkiewicz.audiocaptcha.controller.ViewController;

public class AudioCaptcha {

    private ViewController viewController;

    public AudioCaptcha(View captchaLayout, AudioCaptcha.Version version){
        switch (version){
            case audio:
                this.viewController = new AudioCaptchaViewControllerImpl(captchaLayout);
                break;
            case text:
                this.viewController = new TextCaptchaViewControllerImpl(captchaLayout);
                break;
        }
        viewController.init();
    }

    public enum Version{
        audio,
        text
    }

    public Boolean getResult(){
        return viewController.isChacked();
    }
}


