package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {


    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;

    private TwitterClient client;
    MenuItem miActionProgressItem;


    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    public final static int EDIT_REQUEST_CODE = 20;
    public final static String ITEM_TEXT = "itemText";


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
        setContentView(R.layout.activity_timeline);


        // find the RecyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        // init the arraylist (data source)

        tweets = new ArrayList<>();
        // construct the adapter from the data source

        tweetAdapter = new TweetAdapter(tweets);
        // RecyclerView setup (layour manager, use adapter)

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rvTweets.setLayoutManager(linearLayoutManager);



        //set the adapter
        rvTweets.setAdapter(tweetAdapter);

        client = TwitterApp.getRestClient(getApplicationContext());


//        showProgressBar();
        populateTimeline();
//        hideProgressBar();

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                showProgressBar();
                populateTimeline();

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);




        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                showProgressBar();
                loadNextDataFromApi(page);
            }
        };


        rvTweets.addOnScrollListener(scrollListener);


    }


    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(final int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
        long min_id = tweets.get(0).uid;

        for (int j=1; j<tweets.size();j++){

            if (tweets.get(j).uid < min_id){
                min_id = tweets.get(j).uid;
            }


        }
        client.getHomeTimelinePrev(min_id,new JsonHttpResponseHandler(){

               @Override
               public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                   super.onSuccess(statusCode, headers, response);

                   Log.d("TwitterClient", response.toString());
//                   tweetAdapter.clear();
//                   tweets.clear();

                   hideProgressBar();



                   for(int i= 0; i<response.length();i++){

                       // iterate the JSON array
                       //add that Tweet modeal to our data source
                       // deserialize the JSON object
                       // convert each object to a tweet model


                       try {
                           Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));

                           tweets.add(tweet);
                           tweetAdapter.notifyItemInserted(tweets.size()-1);

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
           }


        );

    }



    public void showProgressBar() {
        // Show progress item

        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
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
                tweetAdapter.clear();
                tweets.clear();

                hideProgressBar();

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

                swipeContainer.setRefreshing(false);


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
        }


        );



    }
}
