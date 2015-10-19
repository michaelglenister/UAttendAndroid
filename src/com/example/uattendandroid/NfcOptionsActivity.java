package com.example.uattendandroid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.uattendandroid.LoginActivity.DownloadWebpageTask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class NfcOptionsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.layout_nfcoptions);

	    TextView user;
	    user = (TextView)findViewById(R.id.txtUser);
	    String lecturerID = getIntent().getExtras().getString("LECTURER_ID");
    	user.setText(lecturerID);
	    //open URL
	    try
  		{
  		    String stringUrl = getResources().getString(R.string.serverAPI) + "/api/mobile/" + lecturerID;
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
        	TextView tv2;
        	tv2 = (TextView)findViewById(R.id.textView2);
        	
        	result = result.replace("[", "");
        	//result = result.replace("]", "");
        	
        	List<String> listResult = new ArrayList<String>();
        	StringBuilder builder = new StringBuilder();
        	//String[] array = new String[]{result};
        	//array[0] = result;
        	boolean cont = true;
        	
			
        	for(int i = 0; i < 6; i++)
        	{
        		builder = new StringBuilder();
        		builder.append(result.substring(12, result.indexOf(",")));
        		result = result.substring(result.indexOf(","), result.indexOf("]"));
            	/*result = result.replace(result.substring(0, result.indexOf(",") + 1), "");
            	builder.append("," + result.substring(14, result.indexOf("}") - 1));
            	result = result.replace(result.substring(0, result.indexOf("}")), "");*/
            	/*if(result.startsWith(","))
            	{
            		result = result.substring(1, result.length() - 1);
            	}
            	else
            	{
            		cont = false;
            	}*/
            	listResult.add(builder.toString());
        	}
        	
        	
        	
        	/*while(cont)
        	{
        		
        	}*/
        	
        	/*for (int i = 0; i < result.length(); i++){
        	    char c = result.charAt(i);      
        	    if(c == '}')
        	    {
        	    	
        	    }
        	    else
        	    {
        	    	
        	    }
        	    listResult[i]
        	}*/
        	
        	/*int i = 0;
        	
        	while(i < 3)
        	{
        		listResult.add(result.substring(0, result.indexOf("}")));
        		result = result.replace(result.substring(0, result.indexOf("{")) + 1, "");
        		i++;
        	}*/
        	
        	/*StringBuilder builder = new StringBuilder();
        	for (String s : array)
        	{
        	  builder.append(s+" ");
        	}*/
        	//tv2.setText(builder.toString());
        	//tv2.setText(listResult.get(4));
        	tv2.setText(result);
        	
        	/*if (result == "0")
        	{      		
        	}
        	else
        	{
        		//Intent intent = new Intent(this, NfcOptionsActivity.class);
        		//startActivity(intent);
        	}*/
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
