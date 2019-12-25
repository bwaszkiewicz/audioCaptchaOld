package pl.benedykt.waszkiewicz.audiocaptcha.controller;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.DynamicsProcessing;
import android.media.audiofx.PresetReverb;
import android.media.effect.Effect;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

import pl.benedykt.waszkiewicz.audiocaptcha.generator.CodeGenerator;
import pl.benedykt.waszkiewicz.audiocaptcha.R;

public class AudioCaptchaViewControllerImpl extends AppCompatActivity implements ViewController {

    private View captchaLayout;
    private EditText inputEditText;
    private Button submitButton;
    private Button playButton;
    private Button refreshButton;

    private TextToSpeech mTextToSpeech;
    private CodeGenerator codeGenerator;
    private MediaPlayer player;

    private String code;
    private Boolean isChecked = false;
    private volatile Boolean isVoice = false;
    private volatile Boolean isStopVoice = false;

    private static final Random RAND = new SecureRandom();

    private Thread speakThread;

    private static final String TAG = AudioCaptchaViewControllerImpl.class.getName();

    public AudioCaptchaViewControllerImpl(View layout) {
        this.captchaLayout = layout;
        this.inputEditText = captchaLayout.findViewById(R.id.audioCaptchaInput);
        this.submitButton = captchaLayout.findViewById(R.id.audioCaptchaSubmit);
        this.playButton = captchaLayout.findViewById(R.id.audioCaptchaPlay);
        this.refreshButton = captchaLayout.findViewById(R.id.audioCaptchaRefresh);
        this.codeGenerator = CodeGenerator.getInstance();
        code = codeGenerator.getSequence();
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

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });

        inputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO input code handle
                Log.println(Log.DEBUG, TAG, "inputEditText");
            }
        });


        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
                Log.println(Log.DEBUG, TAG, "refreshButton");
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = submit();
                Log.println(Log.DEBUG, TAG, "submitButton");
            }
        });
    }

    @Override
    public Boolean submitCheck() {
        return isChecked;
    }

    @Override
    public void play() {
        if (!isVoice)
            speak();
        else
            stop();
    }

    @Override
    public Boolean submit() {
        String test = code.replaceAll("\\s+", "");
        if (test.equals(inputEditText.getText().toString())) {
            Toast.makeText(captchaLayout.getContext(), "You got them right", Toast.LENGTH_SHORT).show();
            captchaLayout.setVisibility(captchaLayout.GONE);
            isChecked = true;
            return true;
        } else {
            Toast.makeText(captchaLayout.getContext(), "You missed", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void refresh() {
        code = codeGenerator.getSequence();
        speak();
    }

    @Override
    protected void onDestroy() {
        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }
        if (player != null)
            player.release();
        super.onDestroy();
    }

    @Override
    public Boolean isChacked() {
        return isChecked;
    }

    private void speak() {

        checkAudio();


        playButton.setBackground(ContextCompat.getDrawable(captchaLayout.getContext(), R.drawable.ic_stop));

        final File outputFile = tempOutputFile();

        Uri uri = Uri.fromFile(outputFile);

        String tst ="";
        StringBuilder tstbuilder = new StringBuilder();
        for(int i=0;i<35;i++) {
            tstbuilder.append((char) (RAND.nextInt(26) + 97));
        }

        tst = tstbuilder.toString();

        mTextToSpeech.setPitch(0.9f);
        mTextToSpeech.setSpeechRate(0.3f);
        mTextToSpeech.synthesizeToFile(tst,null, outputFile, uri.toString());

        mTextToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) { }

            @Override
            public void onDone(String utteranceId) {

                if (player == null) {
                    //player = MediaPlayer.create(captchaLayout.getContext(), R.raw.radio_tuning);
                    player = MediaPlayer.create(captchaLayout.getContext(), Uri.parse(utteranceId));
                }

                speakThread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            isVoice = true;
                            player.start();


                            DynamicsProcessing.Config.Builder builder = new DynamicsProcessing.Config.Builder(
                                    DynamicsProcessing.VARIANT_FAVOR_FREQUENCY_RESOLUTION,
                                    1,
                                    true,
                                    8, true, 8,true, 8, true);
                            builder.setPreferredFrameDuration(10);
                            DynamicsProcessing.Config config = builder.build();

                            DynamicsProcessing.Mbc mbc = config.getChannelByChannelIndex(0).getMbc();

                            for(int i=0;i<8;i++){
                                DynamicsProcessing.MbcBand mbcBand = mbc.getBand(i);
                                mbcBand.setAttackTime(10);
                                mbcBand.setReleaseTime(200);
                                mbcBand.setRatio(3.0f);
                                mbcBand.setKneeWidth(0.4f);
                                mbcBand.setThreshold(20.0f);
                                mbcBand.setNoiseGateThreshold(-30.0f);
                                mbcBand.setExpanderRatio(3.0f);
                                mbcBand.setPreGain(0);
                                mbcBand.setPostGain(0);
                            }


                            DynamicsProcessing dynamicsProcessing = new DynamicsProcessing(0,player.getAudioSessionId(),config);
                            dynamicsProcessing.setEnabled(true);

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

    private void stop() {
        if (speakThread.isAlive()) {
            isStopVoice = true;
            playButton.setBackground(ContextCompat.getDrawable(captchaLayout.getContext(), R.drawable.ic_play));
        }
    }

    private File tempOutputFile(){
        final File outputDir = captchaLayout.getContext().getCacheDir();

        File outputFile = null;
        try {
            outputFile = File.createTempFile("tempTTS", ".wav", outputDir);
        } catch (IOException e) {

        }
        return  outputFile;
    }
}
