package com.example.uattendandroid;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.widget.TextView;


public class NfcActivity extends Activity {
   
	TextView tv1;
    IntentFilter[] filters;
    String[][] techs;
    PendingIntent pendingIntent;
    NfcAdapter adapter;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.layout_nfc);

		tv1 = (TextView)findViewById(R.id.textView1);
		tv1.setText("test");
		
        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter mifare = new IntentFilter((NfcAdapter.ACTION_TECH_DISCOVERED));
        filters = new IntentFilter[] { mifare };
        techs = new String[][] { new String[] {  NfcA.class.getName() } };
        adapter = NfcAdapter.getDefaultAdapter(this);
	}
	
	public void onPause() {
        super.onPause();
        adapter.disableForegroundDispatch(this);
    }
	
	public void onResume() {
        super.onResume();
        adapter.enableForegroundDispatch(this, pendingIntent, filters, techs);
    }
	
	public void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        byte[] id = tag.getId();
        ByteBuffer wrapped = ByteBuffer.wrap(id);
        wrapped.order(ByteOrder.LITTLE_ENDIAN);
        int signedInt = wrapped.getInt();
        long number = signedInt & 0xffffffffl;
        tv1.setText("Tag detected: " + number);
        
        //do stuff here
        
        
    }
}