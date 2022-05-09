package edu.ovgu.twitcher;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListBirds extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<Bird>birdList;
    Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_birds);

        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(birdList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                 Toast.makeText(ListBirds.this, position+ " is selected successfully", Toast.LENGTH_SHORT).show();

                //handle click event

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    private void initData() {
        birdList = new ArrayList<>();
        birdList.add(new Bird(R.drawable.twitcher, "Bird1"));
        birdList.add(new Bird(R.drawable.twitcher, "Bird2"));
        birdList.add(new Bird(R.drawable.twitcher, "Bird3"));
        birdList.add(new Bird(R.drawable.twitcher, "Bird4"));

        birdList.add(new Bird(R.drawable.twitcher, "Bird4"));
        birdList.add(new Bird(R.drawable.twitcher, "Bird4"));
        birdList.add(new Bird(R.drawable.twitcher, "Bird4"));
        birdList.add(new Bird(R.drawable.twitcher, "Bird4"));
        birdList.add(new Bird(R.drawable.twitcher, "Bird4"));

    }

}