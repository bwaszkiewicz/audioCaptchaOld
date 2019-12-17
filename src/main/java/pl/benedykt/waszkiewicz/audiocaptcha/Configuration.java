package pl.benedykt.waszkiewicz.audiocaptcha;

import android.widget.Button;

public class Configuration {

    private static Configuration instance;

    private Configuration(){
        if (instance != null) {
            throw new IllegalStateException("Cannot create new instance, please use getInstance method instead.");
        }
    }

    public static Configuration getInstance() {
        if(instance == null ){
            instance = new Configuration();
        }
        return instance;
    }

    public static Builder builder() {
        return new Builder();
    }

    private Double minBackgroundTextColorContrastRatio = 4.0;
    private Boolean generateLetters = false;
    private Boolean generateNumbers = true;
    private Boolean caseSensitive = false;


    public static final class Builder{
        private Double minBackgroundTextColorContrastRatio;
        private Boolean generateLetters;
        private Boolean generateNumbers;

        public Builder minBackgroundTextColorContrastRatio(Double minContrastRatio){
            this.minBackgroundTextColorContrastRatio = minContrastRatio;
            return this;
        }

        public Builder generateLetters(Boolean generateLetters){
            this.generateLetters = generateLetters;
            return this;
        }

        public Builder generateNumbers(Boolean generateNumbers){
            this.generateNumbers = generateNumbers;
            return this;
        }

    }

}
