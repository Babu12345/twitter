package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class EditActivity extends AppCompatActivity {

    private TwitterClient client;
    EditText etReply;
    long uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSupportActionBar().setTitle("Edit Tweet");


        etReply = (EditText) findViewById(R.id.etReply);
        etReply.setText("@"+ getIntent().getStringExtra("screen_name") + " ");
        uid = getIntent().getLongExtra("uid",0);

        client = TwitterApp.getRestClient(getApplicationContext());

        Toast.makeText(this, getIntent().getStringExtra("screen_name"),Toast.LENGTH_SHORT).show();

    }



    public void onEdit(View v){


        client.replyTweet(etReply.getText().toString(),uid,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);


                finish();


//                hideProgressBar();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
//                hideProgressBar();
                Toast.makeText(getApplicationContext(), "Cannot Tweet",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
//                hideProgressBar();
                Toast.makeText(getApplicationContext(), "Cannot Tweet",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
//                hideProgressBar();
                Toast.makeText(getApplicationContext(), errorResponse.toString(),Toast.LENGTH_SHORT).show();

            }
        });


    }
}
