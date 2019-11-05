package pl.benedykt.waszkiewicz.audiocaptcha.controller;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import pl.benedykt.waszkiewicz.audiocaptcha.R;

public class AudioCaptchaViewControllerImpl extends AppCompatActivity implements ViewController {

    private View captchaLayout;
    private EditText inputEditText;
    private Button submitButton;
    private Button refreshButton;

    private static final String TAG = AudioCaptchaViewControllerImpl.class.getName();

    public AudioCaptchaViewControllerImpl(View layout){
        this.captchaLayout = layout;
        this.inputEditText = captchaLayout.findViewById(R.id.audioCaptchaInput);
        this.submitButton = captchaLayout.findViewById(R.id.audioCaptchaSubmit);
        this.refreshButton = captchaLayout.findViewById(R.id.audioCaptchaRefresh);
    }

    @Override
    public void init() {

        inputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO input code handle
                Log.println(Log.DEBUG,TAG,"inputEditText");
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO submit handle
                Log.println(Log.DEBUG,TAG,"submitButton");
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO refresh hadle
                Log.println(Log.DEBUG,TAG,"refreshButton");
            }
        });

    }

    @Override
    public Boolean submit() {
        return null;
    }

    @Override
    public void refresh() {

    }
}
