package edu.ovgu.twitcher;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import edu.ovgu.twitcher.ml.LiteModelAiyVisionClassifierBirdsV13;

public class Classifier {
    public int compare(Category c1, Category c2) {
        // compare two instance of `Score` and return `int` as result.
        return (int)(c1.getScore()-c2.getScore());
    }

    public static void classify(ImageView imageView, Context context, TextInputEditText inputName){


        try {

            LiteModelAiyVisionClassifierBirdsV13 model = LiteModelAiyVisionClassifierBirdsV13.newInstance(context);

            // Creates inputs for reference.
            
            Bitmap bitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();
            TensorImage image = TensorImage.fromBitmap(bitmap);

            // Runs model inference and gets result.
            LiteModelAiyVisionClassifierBirdsV13.Outputs outputs = model.process(image);
            List<Category> probability = outputs.getProbabilityAsCategoryList();



            Collections.sort(probability, (o1, o2) -> Float.compare(o2.getScore() ,o1.getScore()));


            inputName.setText( probability.get(0).getLabel());




            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
}
