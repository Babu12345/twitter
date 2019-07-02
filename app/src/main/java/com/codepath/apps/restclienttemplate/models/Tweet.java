package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;


@Parcel
public class Tweet {

    // list out all of the atriibutes that you want to store away
    public String body;
    public long uid;// database ID for the tweet
    public String createAt;
    public User user;


    // deserialize the JSON data
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        tweet.body = jsonObject.getString("text");

        tweet.uid = jsonObject.getLong("id");

        tweet.createAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));


        return tweet;



    }
}
