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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.firebase.client.Firebase;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URI;
import java.text.SimpleDateFormat;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit.RestAdapter;


public class MainActivity extends ActionBarActivity {
    private Button button;
    private TableRow pickerRow;
    private TableRow timeTextRow;
    private EditText nameField;
    private EditText groupField;
    private TextView lockedInTime;

    private SimpleDateFormat sdf;
    private GregorianCalendar calendar;
    private File file;
    private static int hour = 0;
    private static int minutes = 0;

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
        groupField = (EditText) findViewById(R.id.groupField);
        timeTextRow = (TableRow) findViewById(R.id.timeText);
        lockedInTime = (TextView) findViewById(R.id.lockedInTime);
        timeTextRow.setVisibility(View.GONE);
        calendar = new GregorianCalendar();
        Firebase.setAndroidContext(this);
        Firebase firebase = new Firebase("https://amber-heat-7766.firebaseio.com/");

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
            timeTextRow.setVisibility(View.VISIBLE);
            lockedInTime.setText("Time period set until: " + hour + ":" + minutes);
            nameField.setEnabled(false);
            groupField.setEnabled(false);

            if (hour < calendar.get(Calendar.HOUR_OF_DAY)
                    && minutes < calendar.get(Calendar.MINUTE)) {
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
            hour = hourOfDay;
            minutes = minute;
        }

    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpPost httpost = new HttpPost("127.0.0.1");

                StringEntity se = new StringEntity("rips.gg");
                httpost.setEntity(se);
                HttpResponse response = httpclient.execute(httpost);

                System.out.println(response.toString());
                //Do something with response...

            } catch (Exception e) {
                // show error
                e.printStackTrace();
            }
            return null;
        }
    }

}
