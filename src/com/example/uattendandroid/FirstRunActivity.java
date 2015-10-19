package com.example.uattendandroid;

import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FirstRunActivity extends Activity {
	Button   mButton;
	EditText mEdit;
	TextView mView;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.layout_firstrun);

	    mButton = (Button)findViewById(R.id.btnEnterNumber);
	    mEdit   = (EditText)findViewById(R.id.txtStudNumber);
	    		
	    mView = (TextView)findViewById(R.id.textView1);
	    
	    mButton.setOnClickListener(
	        new View.OnClickListener()
	        {
	            public void onClick(View view)
	            {
	            	/*if(mEdit.getText().toString().trim().isEmpty())
	            	{
	            		mView.setText("No Student Number Entered");
	            	}
	            	else
	            	{*/
	            		
		            	//mView.setText(mEdit.getText().toString());
		            	
		            	//write to a file
		        		try {
		        	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
		        	        outputStreamWriter.write(mEdit.getText().toString());
		        	        outputStreamWriter.close();
		        	    }
		        	    catch (IOException e) {
		        	        //Log.e("Exception", "File write failed: " + e.toString());
		        	    } 
		        		
		                //Log.v("EditText", mEdit.getText().toString());
		        		finish();
	            	//}
	            }
	        });
	}
	
	
}
