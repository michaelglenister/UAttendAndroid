package com.example.uattendandroid;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	SharedPreferences prefs = null;
	Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = getSharedPreferences("com.example.uattendandroid", MODE_PRIVATE);
		setContentView(R.layout.activity_main);
		
		//write to a file
		try {
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
	        outputStreamWriter.write("");
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        //Log.e("Exception", "File write failed: " + e.toString());
	    } 
		prefs.edit().putBoolean("firstrun", true).commit();
		//read from file
		try {
	        InputStream inputStream = openFileInput("config.txt");

	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                stringBuilder.append(receiveString);
	            }

	            inputStream.close();

	            //display string from file
	            TextView tv1;
	    		tv1 = (TextView)findViewById(R.id.textView1);
	    		tv1.setText("Hello " + stringBuilder.toString());
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	    } 
		catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }
	}

	protected void onResume() {
        super.onResume();

        try {
	        InputStream inputStream = openFileInput("config.txt");

	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";
	            StringBuilder stringBuilder = new StringBuilder();

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                stringBuilder.append(receiveString);
	            }

	            inputStream.close();

	            //display string from file
	            TextView tv1;
	    		tv1 = (TextView)findViewById(R.id.textView1);
	    		tv1.setText("Hello " + stringBuilder.toString());
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	    } 
		catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }
        
        
        if (prefs.getBoolean("firstrun", true)) {
            // this is the first run
            prefs.edit().putBoolean("firstrun", false).commit();//change first run to false
                		
            Intent intent = new Intent(this, FirstRunActivity.class);
    		startActivity(intent);
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void qrClick(View view) {
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
		
	}
	
	public void pinClick(View view) {
		//TextView tv1 = (TextView)findViewById(R.id.textView1);
		//tv1.setText("Pin click");
		Intent intent = new Intent(this, PinActivity.class);
		startActivity(intent);
	}
	
	public void nfcClick(View view) {
		Intent intent = new Intent(this, NfcActivity.class);
		startActivity(intent);
	}

	public void loginClick(View view) {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		TextView tv1;
		
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		  
		if (scanResult != null) {
			// handle scan result

			/* old method to get QR Code data, not working
			int startIndex = scanResult.toString().indexOf(":", scanResult.toString().indexOf(":") + 1);
			int endIndex = scanResult.toString().lastIndexOf("//");
			String qrLink = scanResult.toString().substring(startIndex, startIndex + 7);*/
			String QRcontents = "No result";
			StringBuilder stringBuilder = null;
			
			//new method
			if (resultCode == RESULT_OK) {
				QRcontents = intent.getStringExtra("SCAN_RESULT"); //this is the result
				
				try {
			        InputStream inputStream = openFileInput("config.txt");

			        if ( inputStream != null ) {
			            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			            String receiveString = "";
			            stringBuilder = new StringBuilder();

			            while ( (receiveString = bufferedReader.readLine()) != null ) {
			                stringBuilder.append(receiveString);
			            }
			            inputStream.close();
			        }
			    }
			    catch (FileNotFoundException e) {
			        //Log.e("login activity", "File not found: " + e.toString());
			    } 
				catch (IOException e) {
			        //Log.e("login activity", "Can not read file: " + e.toString());
			    }

				//open URL
				try
				{
					//use URL of web server, id->result from QR Code, num->student number from saved file
					//URL myURL = new URL("http://10.0.0.3:8888/SignIn.aspx?id=" + contents + "&num=" + stringBuilder.toString());			    
					/*URL myURL = new URL("http://10.0.0.3:8888/SignIn.aspx?id=8&num=4234523");
				    URLConnection myURLConnection = myURL.openConnection();
				    myURLConnection.connect();*/
					//http://10.0.0.3:8888/SignIn.aspx?id=1006&num=235346
					
				    String stringUrl = getResources().getString(R.string.serverURLSignIn) + "id=" + QRcontents + "&num=" + stringBuilder.toString();
					new DownloadWebpageTask().execute(stringUrl);
				}
				catch (Exception e) {
			        //Log.e("login activity", "Can not read file: " + e.toString());
			    }
			} 
			else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
				QRcontents = "Scan canceled";
			}
			//tv1.setText("," + contents + "&" + stringBuilder.toString() + ",");//qrLink, scanResult.toString()
		  }
	}
	
	private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        
        protected void onPostExecute(String result) {
        	TextView tv1;
        	tv1 = (TextView)findViewById(R.id.textView1);
        	tv1.setText(result);
       }
    }
	
	private String downloadUrl(String myurl) throws IOException {
	    InputStream is = null;
	    // first 500 characters retrieved
	    int len = 500;
	    
	    try {
	    	
	        URL url = new URL(myurl);
	        //URL url = new URL("http://10.0.0.3:8888/SignIn.aspx?id=1006&num=235346");
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000);//milliseconds
	        conn.setConnectTimeout(15000);//milliseconds
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        // Starts the query
	        conn.connect();
	        int response = conn.getResponseCode();
	        //Log.d(DEBUG_TAG, "The response is: " + response);
	        is = conn.getInputStream();
	        
	        return "oK";
	        
	    // Makes sure that the InputStream is closed after the app is
	    // finished using it.
	    } finally {
	        if (is != null) {
	            is.close();
	        } 
	    }
	}
}
