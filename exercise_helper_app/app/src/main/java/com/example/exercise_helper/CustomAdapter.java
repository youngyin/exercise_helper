package com.example.exercise_helper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Dictionary> mList;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView category;
        private TextView delay;
        private TextView content;
        private TextView time;

        public CustomViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.title_textview);
            this.time = (TextView) view.findViewById(R.id.time_textview);
        }
    }

    public CustomAdapter(ArrayList<Dictionary> list) {
        this.mList = list;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        //viewholder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        //viewholder.time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        //viewholder.title.setGravity(Gravity.CENTER);
        //viewholder.time.setGravity(Gravity.CENTER);

        viewholder.title.setText(mList.get(position).getTitle());
        viewholder.time.setText(mList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}
