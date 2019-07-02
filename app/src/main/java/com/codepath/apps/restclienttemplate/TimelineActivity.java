package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {


    private TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    public final static int EDIT_REQUEST_CODE = 20;
    public final static String ITEM_TEXT = "itemText";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);


        // find the RecyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        // init the arraylist (data source)

        tweets = new ArrayList<>();
        // construct the adapter from the datasource

        tweetAdapter = new TweetAdapter(tweets);
        // RecyclerView setup (layour manager, use adapter)

        rvTweets.setLayoutManager(new LinearLayoutManager(this));



        //set the adapter
        rvTweets.setAdapter(tweetAdapter);

        client = TwitterApp.getRestClient(getApplicationContext());
        populateTimeline();

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    public void onCompose(MenuItem menu){



        Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);

        startActivityForResult(intent,EDIT_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // check request code and result code first

        // Use data parameter
        Tweet tweet = (Tweet) Parcels.unwrap(data.getParcelableExtra("tweet"));
//        Tweet tweet = (Tweet) data.getSerializableExtra("tweet");



        tweets.add(0, tweet);
        tweetAdapter.notifyItemInserted(0);
        rvTweets.scrollToPosition(0);
    }



    private void populateTimeline(){
        client.getHomeTimeline(new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                Log.d("TwitterClient", response.toString());


                for(int i= 0; i<response.length();i++){

                    // iterate the JSON array
                    //add that Tweet modeal to our data source
                    // deserialize the JSON object
                    // convert each object to a tweet model


                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));

                        tweets.add(tweet);
                        tweetAdapter.notifyItemChanged(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("TwitterClient", response.toString());
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });



    }
}
