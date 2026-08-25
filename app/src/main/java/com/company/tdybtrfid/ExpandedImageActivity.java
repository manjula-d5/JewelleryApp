package com.company.tdybtrfid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Picasso;

public class ExpandedImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_image);
        ImageView expandedImageView = findViewById(R.id.expandedImageView);
        ViewGroup backgroundContainer = findViewById(R.id.backgroundContainer); // The container for background blur

        Intent intent = getIntent();
        if (intent != null) {
            String imageUrl = intent.getStringExtra("image_url");

            // Load the image into the expanded ImageView
            Picasso.get().load(imageUrl).into(expandedImageView);

            // Apply blur to the background container
            Glide.with(this)
                    .load(imageUrl)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                // Set the blurred image as the background
                                backgroundContainer.setBackground(resource);

                                // Use Palette to generate a dominant color
                                Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        // Get the dominant color and use it to set the background color
                                        int dominantColor = palette.getDominantColor(/* default color */ Color.BLACK);
                                        backgroundContainer.setBackgroundColor(dominantColor);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            // Do nothing here
                        }
                    });
        }
    }
}