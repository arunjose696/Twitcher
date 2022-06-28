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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import edu.ovgu.twitcher.repository.BirdRepository;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Workbook;

public class ListBirds extends AppCompatActivity    implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{
    private FoldingCellListAdapter adapter;
    private List<Bird> birdList,birdListUnfltered;

    private ProgressBar  progressBar;
    private MaterialDatePicker dateRangePicker;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    String adminUser;
    NavigationView navigationView;

    @Override
    public void onClick(View view) {


    }

    private void setNavigationViewListener() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.export: {
                try {
                    export();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.filter_date:
                dateRangePicker.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.delete:
        }
        //close navigation drawer
        return true;
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        progressBar=findViewById(R.id.progressBar2);
        dateRangePicker= MaterialDatePicker.Builder.dateRangePicker().build();

        dateRangePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long,Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long,Long> selection) {
                Date d1=new Date((Long)selection.first);
                Date d2=new Date((Long)selection.second);
                Log.i("selection first",d1.toString());
                filterDate(d1,d2);

            }
        });

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        adapter = new FoldingCellListAdapter(this, birdList);

        // set elements to adapter
        theListView.setAdapter(adapter);
        getBirds();
        setNavigationViewListener();


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


        adminUser = getIntent().getStringExtra("SESSION_ID_user");

        if(adminUser == null) {
            navigationView.getMenu().setGroupVisible(R.id.admin,false);
        } else if(adminUser.equals("admin")) {
            navigationView.getMenu().setGroupVisible(R.id.admin,true);
        }
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

    public void filterDate(Date from, Date to){
        if(birdListUnfltered==null){
            birdListUnfltered=birdList.stream()
                    .collect(Collectors.toList());
        }
        List<Bird> filteredBirds = birdListUnfltered.stream().filter(sub -> sub.getDate().after(from) && sub.getDate().before(to)).collect(Collectors.toList());
        for (Bird bird:filteredBirds) {
            Log.i("birdlist",bird.getBirdName());


        }


        birdList.clear();
        birdList.addAll(filteredBirds);
        adapter.notifyDataSetChanged();
        Log.i("filteredBirds",filteredBirds.toString());
        Log.i("birdListUnfltered",birdListUnfltered.toString());
        Log.i("birdList",birdList.toString());


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