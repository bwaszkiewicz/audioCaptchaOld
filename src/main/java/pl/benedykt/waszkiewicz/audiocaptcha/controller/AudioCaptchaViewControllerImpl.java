package pl.benedykt.waszkiewicz.audiocaptcha.controller;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Locale;

import pl.benedykt.waszkiewicz.audiocaptcha.CodeGenerator;
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
            Toast.makeText(captchaLayout.getContext(), "you got them right", Toast.LENGTH_SHORT).show();
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

        if (player == null) {
            player = MediaPlayer.create(captchaLayout.getContext(), R.raw.radio_tuning);
        }

        speakThread = new Thread(new Runnable() {
            public void run() {
                try {
                    isVoice = true;
                    player.start();
                    player.setLooping(true);
                    String[] sequence = code.split(" ");
                    mTextToSpeech.setPitch(0.7f);
                    for (String character : sequence) {
                        if (isStopVoice) {
                            onStop();
                            return;
                        }
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
}
