package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {


    private TwitterClient client2;
    EditText etCompose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        getSupportActionBar().setTitle("Compose Item");


//        etCompose = (EditText) findViewById(R.id.etCompose);
//        etCompose.setText(getIntent().getStringExtra(ITEM_TEXT));


        client2 = TwitterApp.getRestClient(getApplicationContext());
    }

    public void onSubmit(View v){

        etCompose = (EditText) findViewById(R.id.etCompose);


        client2.sendTweet(etCompose.getText().toString(),new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);


                try {



                    Intent i = new Intent();


                    Tweet tweet = Tweet.fromJSON(response);


                    i.putExtra("tweet", Parcels.wrap(tweet));
                    setResult(RESULT_OK, i);


                    finish();

                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(), "Cannot Tweet",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getApplicationContext(), "Cannot Tweet",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(), "Cannot Tweet",Toast.LENGTH_SHORT).show();

            }
        });




    }



}
