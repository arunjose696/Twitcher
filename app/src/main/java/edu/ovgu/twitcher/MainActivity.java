package edu.ovgu.twitcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

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
            case R.id.ioc_button:
                Intent intent3= new Intent(MainActivity.this,IOCBirdList.class);
                startActivity(intent3);

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
    }
}