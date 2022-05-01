package edu.ovgu.twitcher;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class AddBird extends AppCompatActivity  implements View.OnClickListener
{


    private DatePickerDialog datePickerDialog;
    private TextInputEditText dateInput;
    private TextInputEditText inputName;
    private Switch additionalOptionsSwitch;
    private TextInputLayout additionalOptionLayout;

    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_ACTION_CODE=1;
    private FloatingActionButton cameraButton;
    ActivityResultLauncher<Intent> activityResultLauncher;

    private TextInputLayout dropdownLayout;


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.dateInput:
                Toast.makeText(this, "there I am", Toast.LENGTH_SHORT).show();
                openDatePicker(view);


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncher.launch(cameraIntent);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bird);
        initDatePicker();
        dateInput = findViewById(R.id.dateInput);
        inputName=findViewById(R.id.inputName);
        imageView=findViewById(R.id.BirdImage);
        dateInput.setText(getTodaysDate());
        dateInput.setOnClickListener(this);
        additionalOptionsSwitch=findViewById(R.id.AdditionalOptionsSwitch);
        additionalOptionLayout=findViewById(R.id.AdditionalOptions);

        cameraButton=findViewById(R.id.floating_action_button);

        dropdownLayout=findViewById(R.id.list_dropdown);

        Log.i("Yeah" , "999999999999999999999999999999999");

        cameraButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    activityResultLauncher.launch(cameraIntent);
                }
            }
        });



        additionalOptionsSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Classifier.classify(imageView,getApplicationContext(), inputName);
                        Log.i("Yeah" , "Is Not Selected");
                        if(additionalOptionLayout.getVisibility()== View.GONE){
                            additionalOptionLayout.setVisibility(View.VISIBLE);
                            dropdownLayout.setVisibility(View.VISIBLE);
                        }
                        else{
                            additionalOptionLayout.setVisibility(View.GONE);
                            dropdownLayout.setVisibility(View.GONE);
                        }

                        // how do i toggle visibility of mExplanation text in my QuizActivity.java from here?
                    }
                }
        );

        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK && result.getData()!=null){

                    Bundle bundle= result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");

                    imageView.setImageBitmap(bitmap);

                    Classifier.classify(imageView,getApplicationContext(),inputName);
                }
            }
        });


    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateInput.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }
    public String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }


    public String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    public String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

}

