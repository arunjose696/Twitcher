package edu.ovgu.twitcher;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.BreakIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.ovgu.twitcher.repository.BirdRepository;

public class AddBird extends AppCompatActivity  implements View.OnClickListener
{


    private DatePickerDialog datePickerDialog;
    private TextInputEditText dateInput;
    private TextInputEditText inputName;
    private Switch additionalOptionsSwitch;
    private TextInputLayout additionalOptionLayout;
    private FloatingActionButton submitButton;
    private TimePickerDialog timePickerDialog;
    private TextInputEditText timeInput;

    public static ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_ACTION_CODE=1;
    private FloatingActionButton cameraButton;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private TextInputLayout dropdownLayout;
    private BirdRepository birdRepo;
    private TextInputEditText notes,wikiLink;
    private AutoCompleteTextView category;
    ProgressDialog pd;

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.dateInput:
                openDatePicker(view);
                break;
            case R.id.timeInput:
                openTimePicker(view);
                break;
            case R.id.submit_btn:
                Toast.makeText(this, "Bird Added", Toast.LENGTH_SHORT).show();
                SimpleDateFormat formatter4=new SimpleDateFormat("MMM dd yyyy");
                SimpleDateFormat fmt=new SimpleDateFormat("HH:mm");
                try {
                    birdRepo.saveBird(new Bird(R.drawable.twitcher, inputName.getText().toString(), formatter4.parse(dateInput.getText().toString()),  fmt.parse(timeInput.getText().toString()),  wikiLink.getText().toString(),  category.getText().toString(),  notes.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Intent intent= new Intent(AddBird.this,MainActivity.class);


                startActivity(intent);
                finish();
                break;
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
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.add_bird);
        initDatePicker();
        dateInput = findViewById(R.id.dateInput);
        inputName=findViewById(R.id.inputName);
        imageView=findViewById(R.id.BirdImage);
        dateInput.setText(getTodaysDate());
        dateInput.setOnClickListener(this);
        additionalOptionsSwitch=findViewById(R.id.AdditionalOptionsSwitch);
        additionalOptionLayout=findViewById(R.id.AdditionalOptions);

        initTimePicker();
        timeInput = findViewById(R.id.timeInput);
        timeInput.setText(getTodaysTime());
        timeInput.setOnClickListener(this);

        cameraButton=findViewById(R.id.camera_btn);
        submitButton=findViewById(R.id.submit_btn);
        dropdownLayout=findViewById(R.id.list_dropdown);
        notes=findViewById(R.id.notes);
        category=findViewById(R.id.categoryInput);
        wikiLink=findViewById(R.id.wikiLinkInput);

        birdRepo=BirdRepository.getInstance();

        Log.i("Yeah" , "999999999999999999999999999999999");
        //Code for uploading image
        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");

        String[] myStringArray = new String[]{"small", "big", "large"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.dropdownlayout,myStringArray);
        category.setAdapter(arrayAdapter);

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

        submitButton.setOnClickListener(this);



        additionalOptionsSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                        if(additionalOptionLayout.getVisibility()== View.GONE){
                            wikiLink.setText("https://en.wikipedia.org/wiki/"+inputName.getText().toString());
                            additionalOptionLayout.setVisibility(View.VISIBLE);
                            dropdownLayout.setVisibility(View.VISIBLE);
                        }
                        else{
                            wikiLink.setText("");
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

    private void initDatePicker() {
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

    private void initTimePicker() {
        boolean is24HView = true;
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeInput.setText(hourOfDay + ":" + minute );

            }
        };
        Calendar c = Calendar.getInstance();
        int lastSelectedHour = c.get(Calendar.HOUR_OF_DAY);
        int lastSelectedMinute = c.get(Calendar.MINUTE);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        // Create TimePickerDialog:
        timePickerDialog = new TimePickerDialog(this, style, timeSetListener, lastSelectedHour, lastSelectedMinute, is24HView);

    }

    public String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String getTodaysTime() {
        Date time = Calendar.getInstance().getTime();
        int hour = time.getHours();
        int minutes = time.getMinutes();
        return hour + ":" + minutes;
    }

    public String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    public String getMonthFormat(int month) {
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

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    public void openTimePicker(View view) { timePickerDialog.show(); }

}

