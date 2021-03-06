package com.sdev.accendo;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class StartNewSession extends AppCompatActivity {
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private TextView mText;
    private String mSessionid;
    private String mFullevent;
    private String mHostPortApp;

    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    int clickCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_start_new_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /*
         * we are in this activity so we have a valid sessionid
         */

        mSessionid = getIntent().getExtras().getString("sessionid");

        this.debugMsg(mSessionid);
        this.setupNFC();
        this.initSession();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        ListView students = (ListView) findViewById(R.id.listViewStudents);
        students.setAdapter(adapter);

        mHostPortApp = getResources().getString(R.string.http_server) + ":" +
                getResources().getString(R.string.http_port) + "/" +
                getResources().getString(R.string.http_app);
    }

    protected void initSession() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String sessionid = getIntent().getExtras().getString("sessionid");

        /*
         * start a new session
         */
        String url = mHostPortApp + "/initsession/" + sessionid;

        this.debugMsg("Init Sessions: " + sessionid);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String fullevent = response.getString("event");
                            eventData(fullevent);
                        } catch (org.json.JSONException e) {
                            try {
                                String errormsg = response.getString("ERROR");
                                eventData(getResources().getString(R.string.NAS));
                            } catch (org.json.JSONException excp) {
                                eventData(excp.getLocalizedMessage());
                            }
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

    private void setupNFC() {
        mAdapter = NfcAdapter.getDefaultAdapter(this);

        TextView text = (TextView) findViewById(R.id.message_log);

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT /*|
                        Intent.FLAG_ACTIVITY_SINGLE_TOP*/), PendingIntent.FLAG_UPDATE_CURRENT);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[]{
                ndef,
        };
        mTechLists = new String[][]{new String[]{NfcF.class.getName()}};
        IntentFilter td = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mFilters = new IntentFilter[]{
                ndef, td
        };

        mTechLists = new String[][]{new String[]{
                NfcV.class.getName(),
                NfcF.class.getName(),
                NfcA.class.getName(),
                NfcB.class.getName()
        }};
        debugMsg("NFC setup complete");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        debugMsg("New Intent");
        String msg = "";

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                debugMsg("has extras");
                Set<String> keys = bundle.keySet();
                Iterator<String> it = keys.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    msg += "[" + key + "=" + bundle.get(key) + "]";
                }
            } else {
                debugMsg("bundle is null");
            }

            String hexdump = new String();
            byte[] tagId = (byte[]) bundle.get("android.nfc.extra.ID");
            for (int i = 0; i < tagId.length; i++) {
                String x = Integer.toHexString(((int) tagId[i] & 0xff));
                if (x.length() == 1) {
                    x = '0' + x;
                }
                hexdump += x + ' ';
            }

            /*
             * here we call sync session with each user card ID
             */
            debugMsg("Found ID: " + hexdump);
            this.attend(hexdump);
        }
    }

    protected void attend(String cardid) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String sessionid = getIntent().getExtras().getString("sessionid");

        /*
         * start a new session
         */
        cardid = cardid.replace(" ", "");
        String url = mHostPortApp + "/attend/" + sessionid + "/" + cardid;

        this.debugMsg("Init Sessions: " + sessionid);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String pcntage = response.getString("pcntage");
                            attendPct(pcntage);
                            studentData(response.getString("fname") + " " +
                                    response.getString("lname") + " " +
                                    response.getString("user_id"));
                        } catch (org.json.JSONException e) {
                            try {
                                String errormsg = response.getString("NAS");
                            } catch (org.json.JSONException excp) {
                                messageLog(excp.getLocalizedMessage());
                            }
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
    public void onPause() {
        super.onPause();

        if (mAdapter != null)
            mAdapter.disableForegroundDispatch(this);
    }

    private void debugMsg(String str) {
        TextView text = (TextView) findViewById(R.id.message_log);
        text.setText(str);
    }

    private void studentData(String str) {
        if (!listItems.contains((str))) {
            listItems.add(0, str);
            adapter.notifyDataSetChanged();
        }
    }

    private void eventData(String str) {
        TextView session = (TextView) findViewById(R.id.textViewSession);
        session.setText(str);
    }

    private void messageLog(String str) {
        TextView msg = (TextView) findViewById(R.id.message_log);
        msg.setText(str);
    }

    private void attendPct(String str) {
        //TextView text = (TextView) findViewById(R.id.attendPct);
        //text.setText(str);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mAdapter != null)
            mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
    }

}
