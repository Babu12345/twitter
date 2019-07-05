package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    //pass in tweet array into the adapter to be able to ise

    Context context;
    List<Tweet> mTweets;
    public TweetAdapter(List<Tweet> tweets){
        mTweets = tweets;

    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet,parent,false);

        ViewHolder viewHolder = new ViewHolder(tweetView);

        return  viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // get the data according to the data
        Tweet tweet = mTweets.get(position);
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvTimestamp.setText(getRelativeTimeAgo(tweet.createAt));


        holder.ivreply.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View v) {
//            int position = getAdapterPosition();
//            Toast.makeText(context, "Cannot Tweet",Toast.LENGTH_SHORT).show();

                 //Make sure that the movie position isn't invalid


                // create an intent for the new activity
                Intent intent = new Intent(context, EditActivity.class);

                //serialize the movie

                intent.putExtra("screen_name", mTweets.get(position).user.screenName);
                intent.putExtra("uid", mTweets.get(position).uid);

                //show the activity
                context.startActivity(intent);


            }
        });



        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }


    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }




    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

    //for each row, inflate the layout and cache references into ViewHolder

    // create the viewholder class

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivProfileImage;
        public TextView  tvUsername;
        public TextView  tvBody;
        public TextView  tvTimestamp;
        public ImageButton ivreply;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUsername     = itemView.findViewById(R.id.tvUserName);
            tvBody         = itemView.findViewById(R.id.tvBody);
            tvTimestamp    = itemView.findViewById(R.id.tvTimestamp);
            ivreply        = itemView.findViewById(R.id.ivreply);






        }

        @Override
        public void onClick(View v) {
//            int position = getAdapterPosition();
//            Toast.makeText(context, "Cannot Tweet",Toast.LENGTH_SHORT).show();

//            if (position != RecyclerView.NO_POSITION){
                // Make sure that the movie position isn't invalid

//                Toast.makeText(context, "Cannot Tweet",Toast.LENGTH_SHORT).show();
//                // get the movie at the position
//                Tweet tweet = mTweets.get(position);
//
//                // create an intent for the new activity
//                Intent intent = new Intent(context, EditActivity.class);
//
//                //serialize the movie
//                intent.putExtra("screen_name", tweet.user.screenName);
//
//                //show the activity
//                context.startActivity(intent);


//            }
        }
    }
}
