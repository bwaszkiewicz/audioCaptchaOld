package pl.benedykt.waszkiewicz.audiocaptcha.audio;

import android.content.Context;

import java.io.File;
import java.io.IOException;

public class AudioManager {

    private final Context context;

    public AudioManager(Context context){
        this.context = context;
    }


    public Boolean stop(){
        if (speakThread.isAlive()) {
            isStopVoice = true;
            return true;
        } else {
            return false;
        }
    }


    private File createTempOutputFile(){
        final File outputDir = context.getCacheDir();

        File outputFile = null;
        try {
            outputFile = File.createTempFile("tempTTS", ".wav", outputDir);
        } catch (IOException e) {

        }
        return  outputFile;
    }
}
