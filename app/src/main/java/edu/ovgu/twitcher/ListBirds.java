package edu.ovgu.twitcher;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ramotion.foldingcell.FoldingCell;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import edu.ovgu.twitcher.repository.BirdRepository;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Workbook;

public class ListBirds extends AppCompatActivity    implements View.OnClickListener{
    private FoldingCellListAdapter adapter;
    private List<Bird> birdList;
    private FloatingActionButton exportButton;
    private ProgressBar  progressBar;

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.export_btn:
                try {

                    export();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

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
        exportButton=findViewById(R.id.export_btn);
        birdList=new ArrayList<Bird>();
        // get our list view
        ListView theListView = findViewById(R.id.mainListView);
        exportButton.setOnClickListener(this);
        progressBar=findViewById(R.id.progressBar2);





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

    public void filterDate(ArrayList<Bird> birdList){
        
        adapter.setmItems(birdList);
        adapter.notifyDataSetChanged();
    }

    public void export() throws IOException {
        try {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    PackageManager.PERMISSION_GRANTED);
        File filePath = new File(getExternalFilesDir(null), "ExcelFile.xlsx");
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
            HSSFSheet hssfSheet = hssfWorkbook.createSheet("Programmer World");
            for(int i=0;i<birdList.size();i++){

            Bitmap bitmap= birdList.get(i).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytesImage = byteArrayOutputStream.toByteArray();

            int intPictureIndex = hssfWorkbook.addPicture(bytesImage, Workbook.PICTURE_TYPE_JPEG);
            CreationHelper creationHelper = hssfWorkbook.getCreationHelper();
            int j=4*i+1;
            HSSFRow hssfRow = hssfSheet.createRow(j-1);
            HSSFCell hssfCell = hssfRow.createCell(0);
            hssfCell.setCellValue("birdList.get(i).getBirdName()");
            hssfCell.setCellValue(birdList.get(i).getBirdName());

            hssfRow = hssfSheet.createRow(j);
            hssfCell = hssfRow.createCell(2);
            hssfCell.setCellValue(birdList.get(i).getDate().toString());
            hssfRow = hssfSheet.createRow(j+1);
            hssfCell = hssfRow.createCell(2);
            hssfCell.setCellValue(birdList.get(i).getWikiLink());

            ClientAnchor clientAnchor = creationHelper.createClientAnchor();
            clientAnchor.setCol1(0);

            clientAnchor.setRow1(j);
            clientAnchor.setCol2(2);
            clientAnchor.setRow2(j+2);


        Drawing drawing = hssfSheet.createDrawingPatriarch();
        drawing.createPicture(clientAnchor, intPictureIndex);
        //hssfSheet.createRow(1).createCell(1);
                }
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        hssfWorkbook.write(fileOutputStream);
        if (fileOutputStream!=null){
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        hssfWorkbook.close();
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Toast.makeText(this, "Bird Data exported ", Toast.LENGTH_SHORT).show();
    }
        catch (Exception e){
    }



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