package com.example.uattendandroid;

import android.app.Activity;
import android.content.Intent;

public class QrActivity extends Activity {
	//this activity is not needed currently as the QR Scan result can be handled on the MainActivity
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		
		if (scanResult != null) {
			// handle scan result
			
		  }
		  // else continue with any other code you need in the method
	}
}