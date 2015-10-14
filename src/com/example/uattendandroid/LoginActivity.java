package com.example.uattendandroid;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;







import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.layout_login);

	}
	
	public void loginClick(View view) {
		EditText txtEmail = (EditText)findViewById(R.id.txtEmail);
		EditText txtPassword = (EditText)findViewById(R.id.txtPassword);
		
		String email = txtEmail.getText().toString();
		String password = txtPassword.getText().toString();
    	
    	//open URL
		try
		{
		    String stringUrl = getResources().getString(R.string.serverURLLogin) + email + "/" + password;
			new DownloadWebpageTask().execute(stringUrl);
		}
		catch (Exception e) {
	        //Log.e("login activity", "Can not read file: " + e.toString());
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
        	
        	if (result == "0")
        	{
        		tv1.setText("Failed to login");        		
        	}
        	else
        	{
        		//Intent intent = new Intent(this, NfcOptionsActivity.class);
        		//startActivity(intent);
        	}
       }
    }
	
	private String downloadUrl(String myurl) throws IOException {
	    InputStream is = null;
	    //retrieve first 500 characters
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

	        
	        
	        return convertStreamToString(is);

	    } finally {
	        if (is != null) {
	            is.close();
	        } 
	    }
	}

	private String convertStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
 
        return stringBuilder.toString();
    }
	
	
}