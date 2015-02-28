package com.aw.photoupload;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class MainActivity extends ActionBarActivity {
    private Button button;
    private TimePicker timePickerFrom;
    private TimePicker timePickerTo;
    private TableRow pickerRow;
    private TableRow timeTextRow;
    private EditText nameField;
    private SimpleDateFormat sdf;
    private GregorianCalendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(cameraButtonOnClickListener);
        nameField = (EditText) findViewById(R.id.nameField);
        timePickerFrom = (TimePicker) findViewById(R.id.timePickerFrom);
        timePickerTo = (TimePicker) findViewById(R.id.timePickerTo);
        pickerRow = (TableRow) findViewById(R.id.timePickerRow);
        timeTextRow = (TableRow) findViewById(R.id.timeText);
        timeTextRow.setVisibility(View.GONE);
        calendar = new GregorianCalendar();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public View.OnClickListener cameraButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pickerRow.setVisibility(View.GONE);
            timeTextRow.setVisibility(View.VISIBLE);
            Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File folder = new File("/sdcard/pics/");
            folder.mkdirs();
            if (timePickerFrom.getCurrentHour() <= calendar.get(Calendar.HOUR_OF_DAY)
                    && timePickerFrom.getCurrentMinute() <= calendar.get(Calendar.MINUTE)
                    && timePickerTo.getCurrentHour() >= calendar.get(Calendar.HOUR_OF_DAY)
                    && timePickerTo.getCurrentMinute() >= calendar.get(Calendar.MINUTE)) {
                Uri uriSavedImage = Uri.fromFile(new File("/sdcard/pics/" + nameField.getText() + "_"
                        + sdf.format(new Date()) + ".png"));
                imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
            }
            startActivityForResult(imageIntent, 0);
        }
    };

}
