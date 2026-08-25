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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class Adapter2 extends RecyclerView.Adapter<Adapter2.ViewHolder> {
    private Context mContext;
    private List<ReadExcelModel> mlist;

    public Adapter2(Context context, List<ReadExcelModel> list) {
        //super(context,list,resource);
        this.mContext = context;
        this.mlist = list;

        for (ReadExcelModel item : mlist) {
            item.setExpanded(false); // Initially, all items are collapsed
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        holder.descp.setText(mlist.get(position).getDescp());
        holder.info.setText(mlist.get(position).getInfo());
        holder.view.setText(mlist.get(position).getViews());
        holder.size.setText(mlist.get(position).getPrice());
        holder.weight.setText(mlist.get(position).getWeight());
        holder.purity.setText(mlist.get(position).getPricePerGram());
       // Bitmap bitmap = BitmapFactory.decodeByteArray(mlist.get(position).getData(), 0, mlist.get(position).getData().length);
       // holder.imageView.setImageBitmap(bitmap);
        Picasso.get().load(mlist.get(position).getImageUrl()).into(holder.imageView);


        // Handle "Claim Discount" button click

        boolean isExpanded = mlist.get(position).isExpanded();

        // Set the visibility of the expandedLayout based on the expansion state
        holder.layout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ExpandedImageActivity.class);
                intent.putExtra("image_url", mlist.get(position).getImageUrl());
                mContext.startActivity(intent);
              //  notifyDataSetChanged();
            }
        });
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
        //return super.getItemId(position);
        return position;
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
    private void startCountdown(Adapter2.ViewHolder holder) {
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
        public  TextView purity;
        public TextView view;
        public CardView cardView;
        public ImageView imageView;
        public LinearLayout layout;
        public Button claimDiscountButton;
        public TextView countdownTimer;
        public ViewHolder(View itemView) {
            super(itemView);
            descp = itemView.findViewById(R.id.descp);
            info = itemView.findViewById(R.id.info);
            size = itemView.findViewById(R.id.size);
            weight=itemView.findViewById(R.id.weight);
            purity=itemView.findViewById(R.id.purity);
            view=itemView.findViewById(R.id.view);
            imageView=itemView.findViewById(R.id.image);

            cardView=itemView.findViewById(R.id.cardView);
            layout=itemView.findViewById(R.id.expandedLayout);
            claimDiscountButton = itemView.findViewById(R.id.claimDiscountButton);
            claimDiscountButton.setBackgroundColor(Color.rgb(229,14,75));
            countdownTimer = itemView.findViewById(R.id.countdownTimer);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ReadExcelModel clickedItem = mlist.get(position);
                        clickedItem.setExpanded(!clickedItem.isExpanded()); // Toggle expansion state

                        // Notify adapter that data has changed to reflect the new expanded state
                        notifyItemChanged(position);
                       // notifyDataSetChanged();
                    }
                }
            });
        }
    }
}