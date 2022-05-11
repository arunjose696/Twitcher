package edu.ovgu.twitcher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.ovgu.twitcher.repository.BirdRepository;

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
        BirdRepository birdRepository= BirdRepository.getInstance();
        Task<QuerySnapshot> birds =  birdRepository.getmFirestore().collection("birds").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Bird tempBird=document.toObject(Bird.class);
                                Log.d("Firestore/fetched", document.getId() + " => " + tempBird);

                                try {
                                    final File localFile= File.createTempFile(tempBird.getBirdName()+document.getId(),".jpg");
                                    BirdRepository.getStorageRef().child(tempBird.getBirdName()+document.getId()+".jpg").getFile(localFile)
                                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>(){
                                                @Override
                                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                                    tempBird.setBitmap(bitmap);
                                                    birdList.add(tempBird);
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i("Exception",e.getMessage());
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }




                            }

                        } else {
                            Log.d("Firestore/fetched", "Error getting documents: ", task.getException());
                        }
                    }
                });
        Log.d("check order", "just checking order ");



    }

}