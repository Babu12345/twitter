package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
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

    MenuItem miActionProgressItem;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        getSupportActionBar().setTitle("Compose Tweet");


//        etCompose = (EditText) findViewById(R.id.etCompose);
//        etCompose.setText(getIntent().getStringExtra(ITEM_TEXT));


        client2 = TwitterApp.getRestClient(getApplicationContext());
    }

    public void onSubmit(View v){

        etCompose = (EditText) findViewById(R.id.etCompose);

        showProgressBar();

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

                hideProgressBar();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideProgressBar();
                Toast.makeText(getApplicationContext(), "Cannot Tweet",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                hideProgressBar();
                Toast.makeText(getApplicationContext(), "Cannot Tweet",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                hideProgressBar();
                Toast.makeText(getApplicationContext(), errorResponse.toString(),Toast.LENGTH_SHORT).show();

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose, menu);


        return true;
    }


    public void showProgressBar() {
        // Show progress item

        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }



}
