package edu.ovgu.twitcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button addBtn;
    Button listBtn;
    Button IOClistBtn;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_btn:
                Intent intent= new Intent(MainActivity.this,AddBird.class);
                startActivity(intent);
                break;
            case R.id.button2:
                Intent intent2= new Intent(MainActivity.this,ListBirds.class);
                startActivity(intent2);
                break;
            case R.id.ioc_button:
                Intent intent3= new Intent(MainActivity.this,IOCBirdList.class);
                startActivity(intent3);
                break;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        addBtn=findViewById(R.id.add_btn);
        addBtn.setOnClickListener(this);
        listBtn=findViewById(R.id.button2);
        listBtn.setOnClickListener(this);
        IOClistBtn=findViewById(R.id.ioc_button);
        IOClistBtn.setOnClickListener(this);

        TextView textView = findViewById(R.id.login);
        String text = "Are you Admin? Click Here ";
        SpannableString ss = new SpannableString(text);

        // creating clickable span to be implemented as a link
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            public void onClick(View v) {
                Intent intent4= new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent4);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#FF6200EE"));
            }
        };

        // setting the part of string to be act as a link
        ss.setSpan(clickableSpan1, 15, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}