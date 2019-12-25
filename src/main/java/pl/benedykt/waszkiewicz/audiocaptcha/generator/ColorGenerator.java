package pl.benedykt.waszkiewicz.audiocaptcha.generator;

import android.graphics.Color;

import java.security.SecureRandom;
import java.util.Random;

import pl.benedykt.waszkiewicz.audiocaptcha.Configuration;

public class ColorGenerator {

    private static final Random RAND = new SecureRandom();
    private static final String TAG = ColorGenerator.class.getName();

    private static ColorGenerator instance;
    private Configuration configuration;

    private int bacgroundColor;
    private int textColor;

    private ColorGenerator(){
        configuration = Configuration.getInstance();
        if (instance != null) {
            throw new IllegalStateException("Cannot create new instance, please use getInstance method instead.");
        }
    }

    public static ColorGenerator getInstance() {
        if(instance == null ){
            instance = new ColorGenerator();
        }
        return instance;
    }

    public void generateColors(){
         bacgroundColor = Color.argb(255,RAND.nextInt(255),RAND.nextInt(255),RAND.nextInt(255));


        double contrastRatio;
        do {
            textColor = Color.argb(255,RAND.nextInt(255),RAND.nextInt(255),RAND.nextInt(255));
            contrastRatio = checkContrastRatio(bacgroundColor, textColor);

        } while (contrastRatio < configuration.getMinColorContrastRatio() && contrastRatio > configuration.getMaxColorContrastRatio());

//        Log.println(Log.DEBUG,TAG, "Bacground: R="+ ((bacgroundColor >> 16) & 0xff) +" G=" + ((bacgroundColor >> 8) & 0xff) + " B="+(bacgroundColor & 0xff));
//        Log.println(Log.DEBUG,TAG, "Text: R="+ ((textColor >> 16) & 0xff) +" G=" + ((textColor >> 8) & 0xff) + " B="+(textColor & 0xff));
//        Log.println(Log.DEBUG,TAG, String.valueOf(contrastRatio));
    }

    private double checkContrastRatio(int bacgroundColor,int textColor){

        double bgIntensity = 0.21 * ((bacgroundColor >> 16) & 0xff) + 0.72 * ((bacgroundColor >> 8) & 0xff) + 0.07 * (bacgroundColor & 0xff);
        double textIntensity = 0.21 * ((textColor >> 16) & 0xff) + 0.72 * ((textColor >> 8) & 0xff) + 0.07 * (textColor & 0xff);
        //Log.println(Log.DEBUG,TAG, "Intensity: "+ bgIntensity);
        if(bgIntensity > textIntensity)
            return (bgIntensity + 0.05) / (textIntensity + 0.05);
        else
            return (textIntensity + 0.05) / (bgIntensity + 0.05);

//        (0.21 × R) + (0.72 × G) + (0.07 × B)
//        (L1 + 0.05) / (L2 + 0.05)

    }

//    private int increaseContrast(int textColor){
//        double ratio = 5;
//        int brightness = 10;
//        int rCorrected = (int) ratio * (((textColor >> 16) & 0xff)-128) + 128 + brightness;
//        int gCorrected = (int) ratio * (((textColor >> 8) & 0xff)-128) + 128 + brightness;
//        int bCorrected = (int) ratio * ((textColor & 0xff)-128) + 128 + brightness;
//        return Color.argb(255,rCorrected,gCorrected,bCorrected);
//    }



    public int getBackgroundColor(){
        return bacgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }
}
