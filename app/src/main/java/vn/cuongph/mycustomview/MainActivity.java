package vn.cuongph.mycustomview;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    RelativeLayout mRelativeLayout;

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRelativeLayout = findViewById(R.id.fram);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addText();
            }
        });

        findViewById(R.id.btn_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });

        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(MainActivity.this);
                disableAllForcus();

            }
        });

    }


    private void addText(){
        disableAllForcus();
        counter++;
        final TextSticker textArt1 = new TextSticker(getApplicationContext());
        textArt1.setColorText(Color.RED);
        textArt1.setId(counter);
        mRelativeLayout.addView(textArt1);
        textArt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textArt1.bringToFront();
                disableAllForcus();
            }
        });

    }

    private void addImage(){
        disableAllForcus();
        counter++;
        final ImagerSticker imageArt = new ImagerSticker(this, "http://www.stickpng.com/assets/images/580b585b2edbce24c47b2a1f.png");
        mRelativeLayout.addView(imageArt);
        imageArt.setId(counter);
        imageArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageArt.bringToFront();
                disableAllForcus();
            }
        });

    }

    private void disableAllForcus() {
        int count = mRelativeLayout.getChildCount();
        Log.e("cuongph", "count : " + count);
        for (int i = 0; i < count; i++) {
            if (mRelativeLayout.getChildAt(i) instanceof TextSticker) {
                TextSticker textArt = (TextSticker) findViewById(mRelativeLayout.getChildAt(i).getId());
                textArt.disableAll();
                if (textArt.textViewArt.getText().toString().isEmpty()) {
                    this.mRelativeLayout.removeView(textArt);
                }
                hideKeyboard(this);
            } else if (mRelativeLayout.getChildAt(i) instanceof ImagerSticker){
                ImagerSticker clipArt = findViewById(mRelativeLayout.getChildAt(i).getId());
                clipArt.disableAll();
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        try {
            ((InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 2);
        } catch (Exception e) {
        }

    }
}
