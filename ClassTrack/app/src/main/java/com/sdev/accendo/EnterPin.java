package com.sdev.accendo;

import android.app.ProgressDialog;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);
        //editText.requestFocusFromTouch();

    //EditText editText = (EditText) findViewById(R.id.editText);
    EditText editText = (EditText) findViewById(R.id.editText);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                new LongOperation().execute("");            }
            return handled;
        }
    });
    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(EnterPin.this);
        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
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
           txt.setText("Executed");
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

/**
    @Override

    Intent i = new Intent(this, StartNewSession.class);
    startActivity(i);
**/


    @Override
   public void onResume() {
        super.onResume();
         EditText editText = (EditText) findViewById(R.id.editText);
         editText.requestFocus();
    }

}

