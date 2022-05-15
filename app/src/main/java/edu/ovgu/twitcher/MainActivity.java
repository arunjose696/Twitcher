package edu.ovgu.twitcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button addBtn;
    Button listBtn;
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


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addBtn=findViewById(R.id.add_btn);
        addBtn.setOnClickListener(this);
        listBtn=findViewById(R.id.button2);
        listBtn.setOnClickListener(this);
    }
}