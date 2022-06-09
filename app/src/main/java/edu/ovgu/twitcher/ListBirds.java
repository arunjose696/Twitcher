package edu.ovgu.twitcher;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.ramotion.foldingcell.FoldingCell;

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
    private FoldingCellListAdapter adapter;
    private List<Bird> birdList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem= menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search for birds");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.list_birds);
        birdList=new ArrayList<Bird>();
        // get our list view
        ListView theListView = findViewById(R.id.mainListView);





        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        adapter = new FoldingCellListAdapter(this, birdList);

        // set elements to adapter
        theListView.setAdapter(adapter);
        getBirds();


        // set on click event listener to list view
        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                // toggle clicked cell state
                ((FoldingCell) view).toggle(true);
                // register in adapter that state for selected cell is toggled
                adapter.registerToggle(pos);
            }
        });


    }

    private void openLink(String link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public  void getBirds() {



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
                                                    adapter.setmItems(birdList);
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