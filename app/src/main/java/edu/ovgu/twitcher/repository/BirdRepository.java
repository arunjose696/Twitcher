package edu.ovgu.twitcher.repository;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.ByteArrayOutputStream;

import edu.ovgu.twitcher.AddBird;
import edu.ovgu.twitcher.Bird;
import edu.ovgu.twitcher.R;

public class BirdRepository {
    private Bird bird;
    private FirebaseFirestore mFirestore;

    public FirebaseFirestore getmFirestore() {
        return mFirestore;
    }

    public void setmFirestore(FirebaseFirestore mFirestore) {
        this.mFirestore = mFirestore;
    }

    public static FirebaseStorage getStorage() {
        return storage;
    }

    public static void setStorage(FirebaseStorage storage) {
        BirdRepository.storage = storage;
    }

    public static StorageReference getStorageRef() {
        return storageRef;
    }

    public static void setStorageRef(StorageReference storageRef) {
        BirdRepository.storageRef = storageRef;
    }

    public static void setInstance(BirdRepository instance) {
        BirdRepository.instance = instance;
    }

    private static FirebaseStorage storage;
    private static StorageReference storageRef;
    private static BirdRepository instance = null;

    public Bird getBird() {
        return bird;
    }

    public void setBird(Bird bird) {
        this.bird = bird;
    }

    private BirdRepository(){
        mFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }
    public static synchronized BirdRepository getInstance() {
        if (instance == null) {
            instance = new BirdRepository();


        }
        return instance;
    }

    public  String uploadImage(String fileName){
        //code for uploading image
        ImageView imageView=AddBird.imageView;
        StorageReference storageReference = storageRef.child(fileName+".jpg");

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        Log.i("download:URL",downloadUrl.toString());
                        //Do what you want with the url
                    }});

            }
        });
        return "something";
    }
    public String saveBird(Bird bird)  {
        DocumentReference document;
        mFirestore.collection(Bird.COLLECTION)
                .add(bird)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference docRef) {
                        uploadImage(bird.getBirdName()+docRef.getId());
                        Log.d("BirdRepo", "DocumentSnapshot added with ID: " + docRef.getId());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure( Exception e) {
                        Log.w("BirdRepo", "Error adding document", e);
                    }
                });

        return "1";
    }
}
