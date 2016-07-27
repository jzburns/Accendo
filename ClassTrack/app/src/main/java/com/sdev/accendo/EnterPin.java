package com.sdev.accendo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EnterPin extends AppCompatActivity {

    private Boolean done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView txt = (TextView) findViewById(R.id.textView3);
        txt.setText("On CREATE called");

        EditText editText = (EditText) findViewById(R.id.editText);
        editText.requestFocusFromTouch();

        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                TextView txt = (TextView) findViewById(R.id.textView3);
                txt.setText("Starting");
                  new LongOperation().execute("");
            }
            return handled;
        }
    });
    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(EnterPin.this);
        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < 2; i++) {
                try {
                    Thread.sleep(1000);
                    /*
                     * Volley JSON request here
                     */
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
                publishProgress();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
           TextView txt = (TextView) findViewById(R.id.textView3);
           txt.setText("Finished Executed ");
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            done = Boolean.TRUE;
            Intent intent_name = new Intent();
            intent_name.setClass(getApplicationContext(),StartNewSession.class);
            startActivity(intent_name);
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    @Override
   public void onResume() {
        super.onResume();
         EditText editText = (EditText) findViewById(R.id.editText);
         editText.requestFocus();
    }

}

