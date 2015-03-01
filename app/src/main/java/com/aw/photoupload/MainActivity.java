package com.aw.photoupload;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;

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
    private TextView lockedInTime;
    private SimpleDateFormat sdf;
    private GregorianCalendar calendar;
    private File file;

    //Azure stuff
    public static final String storageConnectionString =
            "DefaultEndpointsProtocol=http;" +
                    "AccountName=photoupload30;" +
                    "AccountKey=DDee9X64JwLJEDuJig2sg2PkUh7d8Kr/NFqOYdPLBweybIdEh6qgRioWDpGYvPd1cdk/8U8J7H0N6cD4rFIX4w==";

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
        lockedInTime = (TextView) findViewById(R.id.lockedInTime);
        timeTextRow.setVisibility(View.GONE);
        calendar = new GregorianCalendar();
        new MyTask().execute();

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
            Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File folder = new File("/sdcard/pics/");
            folder.mkdirs();
            if (timePickerFrom.getCurrentHour() <= calendar.get(Calendar.HOUR_OF_DAY)
                    && timePickerFrom.getCurrentMinute() <= calendar.get(Calendar.MINUTE)
                    && timePickerTo.getCurrentHour() >= calendar.get(Calendar.HOUR_OF_DAY)
                    && timePickerTo.getCurrentMinute() >= calendar.get(Calendar.MINUTE)) {
                pickerRow.setVisibility(View.GONE);
                timeTextRow.setVisibility(View.VISIBLE);
                int hourFrom = timePickerFrom.getCurrentHour();
                String ampmFrom = "am";
                String ampmTo = "am";
                int hourTo = timePickerTo.getCurrentHour();
                if (timePickerFrom.getCurrentHour() > 12) {
                    hourFrom = hourFrom % 12;
                    ampmFrom = "pm";
                }
                if (timePickerTo.getCurrentHour() > 12) {
                    hourTo = hourTo % 12;
                    ampmTo = "pm";
                }
                lockedInTime.setText("Time period set to: " + hourFrom + ":" + timePickerFrom.getCurrentMinute()
                        + ampmFrom + " to " + hourTo + ":" + timePickerTo.getCurrentMinute() + ampmTo);

                file = new File("/sdcard/pics/" + nameField.getText() + "_"
                        + sdf.format(new Date()) + ".png");

                Uri uriSavedImage = Uri.fromFile(file);
                imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
            }
            startActivityForResult(imageIntent, 0);

        }
    };


    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user

        }

    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try
            {
                CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
                CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
                CloudBlobContainer container = blobClient.getContainerReference("mycontainer");
                container.createIfNotExists();
                BlobContainerPermissions containerPermissions = new BlobContainerPermissions();
                containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
                container.uploadPermissions(containerPermissions);
            }
            catch (Exception e)
            {
                // Output the stack trace.
                e.printStackTrace();
            }
            return null;
        }
    }

}
