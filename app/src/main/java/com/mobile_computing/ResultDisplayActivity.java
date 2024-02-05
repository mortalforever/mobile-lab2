package com.mobile_computing;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.mobile_computing.impl.FavoriteServiceImpl;
import com.squareup.picasso.Picasso;

import java.io.Serializable;


/**
 * This activity displays the result of a book
 * @author dxh
 * @date 1/27/24
 */
public class ResultDisplayActivity extends AppCompatActivity {

    private static ActionBar actionBar;

    private static FavoriteService favoriteService;

    public static final String favoriteTitle = ": your_favorite";
    public static final String unFavoriteTitle = ": un_favorite";
    // The favorite status of the book
    private static boolean isFavorite = false;
    private ImageButton starButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        favoriteService = new FavoriteServiceImpl();
        Intent intent = getIntent();
        Serializable serializeResult = intent.getSerializableExtra("datumApiSearch");
        if (!(serializeResult instanceof Datum)) {
            Log.println(Log.ERROR, "ResultDisplayActivity", "not a datum");
        } else {
            Datum datumResult = (Datum) serializeResult;
            Log.println(Log.DEBUG, "datumResult:", datumResult.title()+" "+datumResult.date()+" "+datumResult.text()+" "+datumResult.imageUrl());
            int id = datumResult.id();
            String date = datumResult.date();
            String title = datumResult.title();
            String text = datumResult.text();
            String imageUrl = datumResult.imageUrl();

            // Setting up the action bar
            actionBar = getSupportActionBar();
            if(actionBar != null) {
                // 1. Enabling the back button
                actionBar.setDisplayHomeAsUpEnabled(true);
                // 2. Enabling the title and setting it.
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setTitle(title);
            }

            TextView titleView = (TextView) findViewById(R.id.res_title);
            TextView dataView = (TextView) findViewById(R.id.res_date);
            //TextView idView = (TextView) findViewById(R.id.res_id);
            TextView textView = (TextView) findViewById(R.id.res_description);
            ImageView imageView = (ImageView) findViewById(R.id.res_image);
            starButton = (ImageButton) findViewById(R.id.startButton);
            titleView.setText(title);
            dataView.setText(date);
            textView.setText(text);
            //idView.setText(String.valueOf(id));
            setupStarButton(id,title);
            loadImage(imageView, imageUrl);
        }
    }


    /**
     * This method sets up the star button and its click listener.
     * @param id the id of the book
     * @param title the title of the book
     */
    private void setupStarButton(int id, String title) {
        actionBar.setDisplayShowTitleEnabled(true);

        // Check if the book is in the favorite list
        isFavorite = favoriteService.queryFavoriteList().contains(String.valueOf(id));
        updateStarButtonAppearance(title);
        // Set click listener for the star button
        starButton.setOnClickListener(v -> {
            // Toggle favorite status
            isFavorite = !isFavorite;
            // Update favorite list accordingly
            if (isFavorite) {
                boolean isSuccess = favoriteService.addBook2FavoriteList(id);
                if (isSuccess) {
                    Log.d("STARBUTTON", "Book added to favorite list");
                } else {
                    Log.e("STARBUTTON", "Failed to add to favorite list");
                    // Revert the favorite status
                    isFavorite = !isFavorite;
                }
            } else {
                boolean isSuccess = favoriteService.removeBookFromFavoriteList(id);
                if (isSuccess) {
                    Log.d("STARBUTTON", "Book removed from favorite list");
                } else {
                    Log.e("STARBUTTON", "Failed to remove from favorite list");
                    // Revert the favorite status
                    isFavorite = !isFavorite;
                }
            }
            // Update UI
            updateStarButtonAppearance(title);
        });
    }

    /**
     * This method updates the appearance of the star button based on the favorite status and the action bar title with favorite status.
     * @param title the title of the book
     */
    private void updateStarButtonAppearance(String title) {
        TextView idView = (TextView) findViewById(R.id.res_id);
        String tmpstr;
        if (isFavorite) {
            actionBar.setTitle(title + favoriteTitle);
            starButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_filled));
            tmpstr = "your_favourite";
        } else {
            actionBar.setTitle(title + unFavoriteTitle);
            starButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_outline));
            tmpstr = "not_your_favourite";
        }
        idView.setText(tmpstr);
    }

    /**
     * This method loads an image from a URL and sets it to an ImageView.
     * @param imageView the ImageView can show the image.
     * @param imageUrl the URL of the image
     */
    private void loadImage(ImageView imageView, String imageUrl) {
        Picasso.get().load(imageUrl).into(imageView);
    }

    /**
     * This method is called when the back button is pressed.
     * @param item the menu item that was selected.
     * @return true if the back button is pressed, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }
}
