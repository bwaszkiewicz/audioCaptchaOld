package pl.benedykt.waszkiewicz.audiocaptcha.controller;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.benedykt.waszkiewicz.audiocaptcha.Configuration;
import pl.benedykt.waszkiewicz.audiocaptcha.generator.CodeGenerator;
import pl.benedykt.waszkiewicz.audiocaptcha.R;
import pl.benedykt.waszkiewicz.audiocaptcha.text.backgrounds.BackgroundProducer;
import pl.benedykt.waszkiewicz.audiocaptcha.text.backgrounds.factory.BackgroundType;
import pl.benedykt.waszkiewicz.audiocaptcha.text.backgrounds.FlatColorBackgroundProducer;
import pl.benedykt.waszkiewicz.audiocaptcha.text.producer.DefaultTextImgProducer;
import pl.benedykt.waszkiewicz.audiocaptcha.text.producer.TextImgProducer;
import pl.benedykt.waszkiewicz.audiocaptcha.text.producer.factory.TextImgType;
import pl.benedykt.waszkiewicz.audiocaptcha.text.renderer.CaptchaRenderer;

public class TextCaptchaViewControllerImpl extends AppCompatActivity implements ViewController {

    private View captchaLayout;
    private ImageView imageView;
    private EditText inputEditText;
    private Button submitButton;
    private Button refreshButton;

    private CodeGenerator codeGenerator;
    private static final Random RAND = new SecureRandom();
    private Configuration configuration;

    private String code;
    private Boolean isChecked = false;

    private static final String TAG = TextCaptchaViewControllerImpl.class.getName();

    public TextCaptchaViewControllerImpl(View layout){
        this.captchaLayout = layout;
        imageView = captchaLayout.findViewById(R.id.textCaptchaImage);
        inputEditText = captchaLayout.findViewById(R.id.textCaptchaInput);
        submitButton = captchaLayout.findViewById(R.id.textCaptchaSubmit);
        refreshButton = captchaLayout.findViewById(R.id.textCaptchaRefresh);

        this.codeGenerator = CodeGenerator.getInstance();
        this.configuration = Configuration.getInstance();
        code = codeGenerator.getSequence();
        draw();
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
                submit();
                Log.println(Log.DEBUG,TAG, "submitButton");
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
//                Log.println(Log.DEBUG,TAG, "refreshButton");
            }
        });

    }

    @Override
    public Boolean submitCheck() {
        return isChecked;
    }

    @Override
    public void play() {

    }

    @Override
    public Boolean submit() {
        String test = code.replaceAll("\\s+", "");
        Log.println(Log.ERROR, TAG, "code: '" + test + "'");
       // closeKeyboard();
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
        draw();
    }

    @Override
    public Boolean isChacked() {
        return isChecked;
    }

    private void draw(){

        BackgroundProducer backgroundProducer = new FlatColorBackgroundProducer();
        TextImgProducer textImgProducer = new DefaultTextImgProducer();


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
}
