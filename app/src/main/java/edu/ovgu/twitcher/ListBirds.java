package edu.ovgu.twitcher;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListBirds extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<ListBirdsRecycler>birdList;
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
    }

    private void initData() {
        birdList = new ArrayList<>();
        birdList.add(new ListBirdsRecycler(R.drawable.twitcher, "Bird1"));
        birdList.add(new ListBirdsRecycler(R.drawable.twitcher, "Bird2"));
        birdList.add(new ListBirdsRecycler(R.drawable.twitcher, "Bird3"));
        birdList.add(new ListBirdsRecycler(R.drawable.twitcher, "Bird4"));

        birdList.add(new ListBirdsRecycler(R.drawable.twitcher, "Bird4"));
        birdList.add(new ListBirdsRecycler(R.drawable.twitcher, "Bird4"));
        birdList.add(new ListBirdsRecycler(R.drawable.twitcher, "Bird4"));
        birdList.add(new ListBirdsRecycler(R.drawable.twitcher, "Bird4"));
        birdList.add(new ListBirdsRecycler(R.drawable.twitcher, "Bird4"));

    }

}