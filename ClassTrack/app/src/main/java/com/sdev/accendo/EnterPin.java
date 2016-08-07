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
import com.android.volley.toolbox.*;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;

public class EnterPin extends AppCompatActivity {

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
                validateUser(String.valueOf(v.getText()));
            }
            return handled;
        }
    });
    }

    private void debugMsg(String str) {
        TextView text = (TextView) findViewById(R.id.textView3);
        text.setText(str);
    }

    protected void validateUser (String pin) {

        CookieManager cookieManage = new CookieManager();
        CookieHandler.setDefault(cookieManage);

        RequestQueue queue = Volley.newRequestQueue(this);

        String cardid = getIntent().getExtras().getString("cardid");
        cardid = cardid.replace(" ", "");

        /*
         * validate the user
         */
        String url ="http://192.168.1.15:8000/accendo/validateuser/" + cardid + "/" + pin;

        this.debugMsg("Validating user: " + cardid + "PIN: " + pin);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String sessionid = response.getString("sessionid");
                            if (sessionid != null) {
                                Bundle b = new Bundle();
                                b.putString("sessionid", sessionid);
                                Intent i = new Intent(EnterPin.this, StartNewSession.class);
                                i.putExtras(b);
                                startActivity(i);
                                finish();
                            } else {
                                String errormsg = response.getString("error");
                                debugMsg("ERROR: " + errormsg);
                            }
                        } catch (org.json.JSONException e) {
                            debugMsg(e.getLocalizedMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        // Add the request to the RequestQueue.

        queue.add(jsObjRequest);
    }

    @Override
   public void onResume() {
        super.onResume();
         EditText editText = (EditText) findViewById(R.id.editText);
         editText.requestFocus();
    }

}

