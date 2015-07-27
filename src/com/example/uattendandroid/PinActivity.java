package com.example.uattendandroid;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PinActivity extends Activity {
	Button   mButton;
	EditText mEdit;
	TextView mView;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.layout_pin);

	    mButton = (Button)findViewById(R.id.btnEnterPin);
	    mEdit   = (EditText)findViewById(R.id.txtPin);

	    mButton.setOnClickListener(
	        new View.OnClickListener()
	        {
	            public void onClick(View view)
	            {
	            	mView = (TextView)findViewById(R.id.textView1);
	            	mView.setText("Processing...");
	                //Log.v("EditText", mEdit.getText().toString());
	            	
	            	StringBuilder stringBuilder = null;
	            	
	            	//read file
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
						//http://10.0.0.3:8888/SignIn.aspx?id=1006&num=235346
					    String stringUrl = getResources().getString(R.string.serverURL) + "id=" + mEdit.getText().toString() + "&num=" + stringBuilder.toString();
						new DownloadWebpageTask().execute(stringUrl);
					}
					catch (Exception e) {
				        //Log.e("login activity", "Can not read file: " + e.toString());
				    }
	            }
	        });
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

	    } finally {
	        if (is != null) {
	            is.close();
	        } 
	    }
	}
}
