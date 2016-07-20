package com.sdev.accendo;

import android.Manifest;
import android.app.PendingIntent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.content.IntentFilter.MalformedMimeTypeException;

public class MainActivity extends AppCompatActivity {
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private TextView mText;
    private int mCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
         * callback for the decrement
         */
        Button but = (Button) findViewById(R.id.button_dec);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCount += (mCount > 0) ? -1 : 0;
                TextView text = (TextView) findViewById(R.id.textView2);
                text.setText(String.valueOf(mCount));
            }
        });

        /*
         * callback for the increment
         */
        but = (Button) findViewById(R.id.button_inc);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCount++;
                TextView text = (TextView) findViewById(R.id.textView2);
                text.setText(String.valueOf(mCount));
            }
        });

        mCount = 0;
        TextView text = (TextView) findViewById(R.id.textView2);
        text.setText(String.valueOf(mCount));

        this.setupNFC();
    }

    private void setupNFC() {
        mAdapter = NfcAdapter.getDefaultAdapter(this);

        TextView text = (TextView) findViewById(R.id.textView3);

        if(mAdapter == null){
            text.setText("No NFC Adapter Found");
        }
        else {
            text.setText("NFC Adapter Found");
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.NFC);
            if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
                text.setText("Permission Granted");
            } else {
                text.setText("Permission NOT Granted");
            }

        }

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT /*|
                        Intent.FLAG_ACTIVITY_SINGLE_TOP*/), PendingIntent.FLAG_UPDATE_CURRENT);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

        try {
            ndef.addDataType("*/*");
        } catch (MalformedMimeTypeException e) {
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
        mCount++;
        TextView text = (TextView) findViewById(R.id.textView2);
        text.setText(String.valueOf(mCount));

        /*
        extract the tag data
         */
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs != null) {
           NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                msgs[i] = (NdefMessage) rawMsgs[i];
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mAdapter != null)
            mAdapter.disableForegroundDispatch(this);
    }

    private void debugMsg(String str) {
        TextView text = (TextView) findViewById(R.id.textView3);
        text.setText(str);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mAdapter != null)
            mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
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
}
