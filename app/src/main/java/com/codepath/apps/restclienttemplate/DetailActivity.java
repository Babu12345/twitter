package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class DetailActivity extends AppCompatActivity {


    Tweet tweet;
    TextView tvDetailBody;
    ImageView ivProfileImage;
    TextView tvHandler;
    TextView tvRetweets;
    TextView tvLikes;
    ImageButton ibRetweet;
    ImageButton ibReply;
    ImageButton ibStar;

    long uid;
    private TwitterClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvDetailBody = findViewById(R.id.tvDetailBody);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvHandler = findViewById(R.id.tvHandler);
        tvRetweets = findViewById(R.id.tvRetweets);
        tvLikes = findViewById(R.id.tvLikes);
        ibRetweet = findViewById(R.id.ibRetweet);
        ibReply = findViewById(R.id.ibReply);
        ibStar = findViewById(R.id.ibStar);


        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        tvHandler.setText(tweet.user.name);
        tvDetailBody.setText(tweet.body);

//        tvRetweets.setText("Hello");
//        tvLikes.setText("Hi");

        // load image using glide
        Glide.with(this)
                .load(tweet.user.profileImageUrl)
                .into(ivProfileImage)
        ;
        uid = tweet.uid;


        client = TwitterApp.getRestClient(getApplicationContext());

        client.Tweetdata(uid, new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(getApplicationContext(), "Counting", Toast.LENGTH_SHORT).show();
                try {
                    tvRetweets.setText(response.getJSONObject(0).getString("retweet_count") + " Retweets");
                    tvLikes.setText(response.getJSONObject(0).getString("favorite_count") + " Likes");
//
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });






    }






    public void onRetweet(View v){

        Toast.makeText(this,"Clicked Reweet", Toast.LENGTH_SHORT).show();

        client.reTweet(uid,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);



                //TODO -- maybe go back and display the retweet


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

                Toast.makeText(getApplicationContext(), errorResponse.toString(),Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void onLike(View v){

        Toast.makeText(this,"Clicked Like", Toast.LENGTH_SHORT).show();
        client.likeTweet(uid,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);




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

                Toast.makeText(getApplicationContext(), errorResponse.toString(),Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void onReply(View v){
        Toast.makeText(this,"Clicked Reply", Toast.LENGTH_SHORT).show();


        // create an intent for the new activity
        Intent intent = new Intent(this, EditActivity.class);

        //serialize the movie

        intent.putExtra("screen_name", tweet.user.screenName);
        intent.putExtra("uid", tweet.uid);

        //show the activity
        this.startActivity(intent);




    }

    public void onDone(View v){

        finish();
    }
}
