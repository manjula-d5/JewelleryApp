package com.company.tdybtrfid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class Adapter3 extends RecyclerView.Adapter<Adapter3.ViewHolder> {
private Context mContext;
 private List<ReadExcelModel> mlist;

public Adapter3(Context context, List<ReadExcelModel> list) {
this.mContext = context;
 this.mlist = list;
 }

@NonNull
 @Override
 public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
 View itemView = LayoutInflater.from(parent.getContext())
.inflate(R.layout.singletagread, parent, false);
 return new ViewHolder(itemView);
}

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
 ReadExcelModel item = mlist.get(position);

 // Set data to TextViews and ImageView using Picasso
holder.descp.setText(item.getDescp());
 holder.info.setText(item.getInfo());
holder.view.setText(item.getViews());
 holder.size.setText(item.getPrice());
 holder.weight.setText(item.getWeight());
 holder.purity.setText(item.getPricePerGram());

 Picasso.get().load(item.getImageUrl()).into(holder.imageView);

 // Handle image click to open a new activity with the expanded image
 holder.imageView.setOnClickListener(new View.OnClickListener() {
 @Override
 public void onClick(View view) {
 Intent intent = new Intent(mContext, ExpandedImageActivity.class);
 intent.putExtra("image_url", item.getImageUrl());
 mContext.startActivity(intent);
 }
 });

 // Handle "Claim Discount" button click
 holder.claimDiscountButton.setOnClickListener(new View.OnClickListener() {
 @Override
 public void onClick(View view) {
 // Handle the button click action (e.g., disable the button and start countdown)
  holder.countdownTimer.setVisibility(View.VISIBLE);
 startCountdown(holder);
 }
 });

 // Set the initial countdown timer value
 holder.countdownTimer.setText("2:00"); // Replace with your initial countdown value
}

@Override
 public long getItemId(int position) {
 return position;
 }

 @Override
public int getItemCount() {
return mlist.size();
 }

 // Method to start the countdown timer
      private void startCountdown(ViewHolder holder) {
        new CountDownTimer(120000, 1000) { // 120000 ms (2 minutes) with 1000 ms (1 second) interval
        public void onTick(long millisUntilFinished) {
 // Update the countdown timer text with the remaining time
      long minutes = millisUntilFinished / 60000;
     long seconds = (millisUntilFinished % 60000) / 1000;
         String timeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    holder.countdownTimer.setText(timeLeft);
 }

public void onFinish() {
// Countdown timer finished, you can perform any desired action here
 holder.countdownTimer.setText("00:00"); // Display 00:00 when the timer is done
 holder.claimDiscountButton.setVisibility(View.GONE);
 holder.countdownTimer.setVisibility(View.GONE);
 }
 }.start();
 }
    public class ViewHolder extends RecyclerView.ViewHolder {
 public TextView descp;
 public TextView info;
 public TextView size;
 public TextView weight;
public TextView purity;
 public TextView view;
 public ImageView imageView;
 public Button claimDiscountButton;
 public TextView countdownTimer;

 public ViewHolder(View itemView) {
 super(itemView);
descp = itemView.findViewById(R.id.descp);
 info = itemView.findViewById(R.id.info);
 size = itemView.findViewById(R.id.size);
weight = itemView.findViewById(R.id.weight);
 purity = itemView.findViewById(R.id.purity);
 view = itemView.findViewById(R.id.view);
 imageView = itemView.findViewById(R.id.image);
 claimDiscountButton = itemView.findViewById(R.id.claimDiscountButton);
  claimDiscountButton.setBackgroundColor(Color.rgb(229,14,75));
     countdownTimer = itemView.findViewById(R.id.countdownTimer);
 }
}
}
