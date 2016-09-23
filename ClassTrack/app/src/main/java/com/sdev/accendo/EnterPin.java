package com.sdev.accendo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;

public class EnterPin extends AppCompatActivity {

    private String mHostPortApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView txt = (TextView) findViewById(R.id.textView3);
        txt.setText("On CREATE called");

        EditText editText = (EditText) findViewById(R.id.editText);

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
        mHostPortApp = getResources().getString(R.string.http_server) + ":" +
                getResources().getString(R.string.http_port) + "/" +
                getResources().getString(R.string.http_app);

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
        String url = mHostPortApp + "/validateuser/" + cardid + "/" + pin;

        this.debugMsg(mHostPortApp + " @ " + cardid + "PIN: " + pin);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String sessionid = response.getString("sessionid");
                            if (sessionid != null) {
                                Bundle b = new Bundle();
                                b.putString("sessionid", sessionid);

                                /*
                                 * depending on the role:
                                 */

                                String role = response.getString("role");
                                Intent i = null;

                                if(role.equals("student")) {
                                    i = new Intent(EnterPin.this, StudentActivity.class);
                                } else {
                                    i = new Intent(EnterPin.this, StartNewSession.class);
                                }
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

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

}

