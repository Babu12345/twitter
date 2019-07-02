package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {


    // list all of the attributes

    // deserialize the JSON

    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;

    // deserialize the JSON
    public static User fromJSON(JSONObject jsonObject) throws JSONException {

        User user = new User();

        // extract and fill values

        user.name = jsonObject.getString("name");
        user.uid  = jsonObject.getLong("id");
        user.screenName = jsonObject.getString("profile_image_url");





        return user;
    }
}
