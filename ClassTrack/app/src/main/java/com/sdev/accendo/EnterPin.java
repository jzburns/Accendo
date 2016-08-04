package com.sdev.accendo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


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
                validateUser();
            }
            return handled;
        }
    });
    }

    private void debugMsg(String str) {
        TextView text = (TextView) findViewById(R.id.textView3);
        text.setText(str);
    }

    protected void validateUser () {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.1.15:8000/accendo/validateuser/0f2afe/0000/";

        this.debugMsg("Validating user");

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        debugMsg("RESPONSE: " + response.substring(0,50));
                        //Intent intent_name = new Intent();
                        //intent_name.setClass(getApplicationContext(),StartNewSession.class);
                        //startActivity(intent_name);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                debugMsg("ERROR: " + error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
   public void onResume() {
        super.onResume();
         EditText editText = (EditText) findViewById(R.id.editText);
         editText.requestFocus();
    }

}

