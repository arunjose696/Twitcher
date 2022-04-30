package edu.ovgu.twitcher;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class AddBird extends AppCompatActivity  implements View.OnClickListener
{


    private DatePickerDialog datePickerDialog;
    private TextInputEditText dateInput;
    private Switch additionalOptionsSwitch;
    private TextInputLayout additionalOptionLayout;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bird);
        initDatePicker();
        dateInput = findViewById(R.id.dateInput);
        dateInput.setText(getTodaysDate());
        dateInput.setOnClickListener(this);
        additionalOptionsSwitch=findViewById(R.id.AdditionalOptionsSwitch);
        additionalOptionLayout=findViewById(R.id.AdditionalOptions);
        dropdownLayout=findViewById(R.id.list_dropdown);
        Log.i("Yeah" , "999999999999999999999999999999999");
        additionalOptionsSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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

