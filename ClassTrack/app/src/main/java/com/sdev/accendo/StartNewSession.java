package com.sdev.accendo;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.TextView;

import java.util.Iterator;
import java.util.Set;

public class StartNewSession extends AppCompatActivity {
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private TextView mText;
    private int mCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
         this.setupNFC();
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
        mFilters = new IntentFilter[] {
                ndef,
        };
        mTechLists = new String[][] { new String[] { NfcF.class.getName() } };
        IntentFilter td = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mFilters = new IntentFilter[] {
                ndef, td
        };

        mTechLists = new String[][] { new String[] {
                NfcV.class.getName(),
                NfcF.class.getName(),
                NfcA.class.getName(),
                NfcB.class.getName()
        } };

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String msg = "";

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                debugMsg("has extras");
                Set<String> keys = bundle.keySet();
                Iterator<String> it = keys.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    msg += "[" + key + "=" + bundle.get(key)+"]";
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

            debugMsg("Found ID: " + hexdump);

        }

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

    @Override
    public void onResume() {
        super.onResume();

        if (mAdapter != null)
            mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
    }

}
