package pl.benedykt.waszkiewicz.audiocaptcha.controller;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import pl.benedykt.waszkiewicz.audiocaptcha.R;

public class TextCaptchaViewControllerImpl extends AppCompatActivity implements ViewController {

    private View captchaLayout;
    private ImageView imageView;
    private EditText inputEditText;
    private Button submitButton;
    private Button refreshButton;

    private static final String TAG = TextCaptchaViewControllerImpl.class.getName();

    public TextCaptchaViewControllerImpl(View layout){
        this.captchaLayout = layout;
        imageView = captchaLayout.findViewById(R.id.textCaptchaImage);
        inputEditText = captchaLayout.findViewById(R.id.textCaptchaInput);
        submitButton = captchaLayout.findViewById(R.id.textCaptchaSubmit);
        refreshButton = captchaLayout.findViewById(R.id.textCaptchaRefresh);
    }

    @Override
    public void init() {

        inputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO input code handle
                Log.println(Log.DEBUG,TAG, "inputEditText");
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO submit handle
                Log.println(Log.DEBUG,TAG, "submitButton");
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO refresh handle
                Log.println(Log.DEBUG,TAG, "refreshButton");
            }
        });

    }

    @Override
    public void play() {

    }

    @Override
    public Boolean submit() {
        return null;
    }

    @Override
    public void refresh() {

    }
}
