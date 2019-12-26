package pl.benedykt.waszkiewicz.audiocaptcha.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.PresetReverb;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import pl.benedykt.waszkiewicz.audiocaptcha.Configuration;
import pl.benedykt.waszkiewicz.audiocaptcha.R;
import pl.benedykt.waszkiewicz.audiocaptcha.audio.EffectsManager;
import pl.benedykt.waszkiewicz.audiocaptcha.generator.CodeGenerator;
import pl.benedykt.waszkiewicz.audiocaptcha.text.backgrounds.factory.BackgroundType;
import pl.benedykt.waszkiewicz.audiocaptcha.text.producer.factory.TextImgType;
import pl.benedykt.waszkiewicz.audiocaptcha.text.renderer.CaptchaRenderer;

public class CaptchaViewController extends AppCompatActivity implements ViewController {


    private View captchaLayout;
    private ImageView imageView;
    private EditText inputEditText;
    private Button submitButton;
    private Button refreshButton;
    private Button playButton;

    private CodeGenerator codeGenerator;
    private static final Random RAND = new SecureRandom();
    private Configuration configuration;
    private MediaPlayer player;
    private TextToSpeech mTextToSpeech;

    private String code;
    private Boolean isChecked = false;


    private Thread speakThread;
    private volatile Boolean isVoice = false;
    private volatile Boolean isStopVoice = false;

    private static final String TAG = TextCaptchaViewControllerImpl.class.getName();

    public CaptchaViewController(View layout){
        this.captchaLayout = layout;
        this.imageView = captchaLayout.findViewById(R.id.img_captcha);
        this.inputEditText = captchaLayout.findViewById(R.id.et_captcha_input);
        this.submitButton = captchaLayout.findViewById(R.id.btn_submit);
        this.refreshButton = captchaLayout.findViewById(R.id.btn_refresh);
        this.playButton = captchaLayout.findViewById(R.id.btn_play);

        this.codeGenerator = CodeGenerator.getInstance();
        this.configuration = Configuration.getInstance();
        code = codeGenerator.getSequence();
        draw();
    }

    @Override
    public void init() {

        mTextToSpeech = new TextToSpeech(captchaLayout.getContext(), new TextToSpeech.OnInitListener() {
            int result;

            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS)
                    result = mTextToSpeech.setLanguage(Locale.UK);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported");
                } else {
                    submitButton.setEnabled(true);
                }
            }
        });

        if(useOnlyNumbers()) inputEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        if(useOnlyUpperCase())inputEditText.setFilters(new InputFilter[] {new InputFilter.AllCaps()});


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
    }

    @Override
    public Boolean isChecked() {
        return isChecked;
    }

    public void play() {
        if (!isVoice)
            speak();
        else
            stop();
    }

    public void submit() {
        String test = code.replaceAll("\\s+", "");
        Log.println(Log.ERROR, TAG, "code: '" + test + "'");
        // closeKeyboard();
        if (test.equals(inputEditText.getText().toString())) {
            Toast.makeText(captchaLayout.getContext(), "Correct", Toast.LENGTH_SHORT).show();
            captchaLayout.setVisibility(captchaLayout.GONE);
            isChecked = true;
        } else {
            Toast.makeText(captchaLayout.getContext(), "You missed", Toast.LENGTH_SHORT).show();
        }
    }

    public void refresh() {
        code = codeGenerator.getSequence();
        draw();
    }

    private void draw(){
        View v = new CaptchaRenderer(imageView.getContext(), 200, 60, BackgroundType.FLAT, drawTextImageType(), code);
        Bitmap bitmap = Bitmap.createBitmap(200,60, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        imageView.setImageBitmap(bitmap);
    }

    private TextImgType drawTextImageType(){

        List<TextImgType> textImgTypes = new ArrayList<>();

        if(configuration.getUseBlurTextFilter()) textImgTypes.add(TextImgType.BLUR);
        if(configuration.getUseDashTextFilter()) textImgTypes.add(TextImgType.DASH);
        if(configuration.getUseDefaultTextFilter()) textImgTypes.add(TextImgType.DEFAULT);
        if(configuration.getUseHollowTextFilter()) textImgTypes.add(TextImgType.HOLLOW);
        if(configuration.getUseTriangleTextFilter()) textImgTypes.add(TextImgType.TRIANGLE);

        return textImgTypes.get(RAND.nextInt(textImgTypes.size()));
    }

    private void speak() {

        checkAudio();

        playButton.setBackground(ContextCompat.getDrawable(captchaLayout.getContext(), R.drawable.ic_stop));

        final File outputFile = tempOutputFile();
        saveNoiseToFile(outputFile);

        mTextToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) { }

            @Override
            public void onDone(String utteranceId) {

                if (player == null) {
                    player = MediaPlayer.create(captchaLayout.getContext(), Uri.parse(utteranceId));
                }

                speakThread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            isVoice = true;
                            player.start();

                            EffectsManager manager = new EffectsManager(player.getAudioSessionId());
                            manager.applayEffects();

                            PresetReverb mReverb = new PresetReverb(1,player.getAudioSessionId());
                            mReverb.setPreset(PresetReverb.PRESET_MEDIUMHALL);
                            mReverb.setEnabled(true);


                            player.setAuxEffectSendLevel(1.0f);
                            player.setLooping(true);
                            String[] sequence = code.split(" ");
                            for (String character : sequence) {
                                if (isStopVoice) {
                                    onStop();
                                    return;
                                }
                                mTextToSpeech.setPitch(1.0f);
                                mTextToSpeech.setSpeechRate(1.0f);
                                mTextToSpeech.speak(character, TextToSpeech.QUEUE_ADD, null, null);
                                mTextToSpeech.playSilentUtterance(1000, TextToSpeech.QUEUE_ADD, null);
                            }
                            boolean speakingEnd;
                            do {
                                if (isStopVoice) {
                                    onStop();
                                    return;
                                }
                                speakingEnd = mTextToSpeech.isSpeaking();
                            } while (speakingEnd);
                            if (player != null) {
                                player.release();
                                player = null;
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Exception until player running: " + e.getMessage());
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                playButton.setBackground(ContextCompat.getDrawable(captchaLayout.getContext(), R.drawable.ic_play));
                            }
                        });
                        isVoice = false;
                    }

                    private void onStop() {
                        mTextToSpeech.stop();
                        player.stop();
                        player.reset();
                        player.release();
                        player = null;
                        isStopVoice = false;
                        isVoice = false;
                    }
                });
                speakThread.start();
                outputFile.delete();
            }
            @Override
            public void onError(String utteranceId) {
                Log.println(Log.DEBUG, TAG, "Occured error when created synteziedToFile, utteranceId: " + utteranceId);
            }
        });

    }

    private void stop() {
        if (speakThread.isAlive()) {
            isStopVoice = true;
            playButton.setBackground(ContextCompat.getDrawable(captchaLayout.getContext(), R.drawable.ic_play));
        }
    }

    private void checkAudio() {
        AudioManager audio = (AudioManager) captchaLayout.getContext().getSystemService(Context.AUDIO_SERVICE);

        switch (audio.getStreamVolume(AudioManager.STREAM_MUSIC)) {
            case 0:
                Toast.makeText(captchaLayout.getContext(), "You have a muted sound.", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(captchaLayout.getContext(), "You have very low volume.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private File tempOutputFile(){
        final File outputDir = captchaLayout.getContext().getCacheDir();

        File outputFile = null;
        try {
            outputFile = File.createTempFile("tempTTS", ".wav", outputDir);
        } catch (IOException e) {
            Log.println(Log.DEBUG, TAG, "Occured error when created temp file: " + e.getMessage());
        }
        return  outputFile;
    }

    private void saveNoiseToFile(File outputFile){
        Uri uri = Uri.fromFile(outputFile);

        String noiseText;
        StringBuilder noiseTextBuilder = new StringBuilder();
        for(int i=0;i<35;i++) {
            char x = RAND.nextBoolean() ? (char) (RAND.nextInt(26) + 97) : (char) (RAND.nextInt(10));
            noiseTextBuilder.append(x);
        }

        noiseText = noiseTextBuilder.toString();

        mTextToSpeech.setPitch(0.9f);
        mTextToSpeech.setSpeechRate(0.3f);
        mTextToSpeech.synthesizeToFile(noiseText,null, outputFile, uri.toString());
    }

    private Boolean useOnlyNumbers(){
        return !configuration.getGenerateLowerCases() && !configuration.getGenerateUpperCases() && configuration.getGenerateNumbers();
    }

    private Boolean useOnlyUpperCase(){
        return !configuration.getGenerateLowerCases() && !configuration.getGenerateUpperCases();
    }
}
