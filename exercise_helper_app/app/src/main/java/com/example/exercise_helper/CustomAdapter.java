package com.example.exercise_helper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/*
참고자료
https://mine-it-record.tistory.com/254 리사이클러뷰 클릭이벤트 처리
https://webnautes.tistory.com/1214 리사이클러뷰 처리
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Dictionary> mList;
    private OnItemClickListener mListener = null ;

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    public void setOnClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView id;
        private TextView title;
        private TextView category;
        private TextView delay;
        private TextView content;
        private TextView time;
        private TextView average;

        public CustomViewHolder(View view) {
            super(view);
            this.id = (TextView) view.findViewById(R.id.id_textview);
            this.title = (TextView) view.findViewById(R.id.title_textview);
            this.category = (TextView) view.findViewById(R.id.category_textview);
            this.delay = (TextView) view.findViewById(R.id.delay_textview);
            this.content = (TextView) view.findViewById(R.id.content_textview);
            this.time = (TextView) view.findViewById(R.id.time_textview);
            this.average = (TextView) view.findViewById(R.id.average_textview);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        if (mList != null){
                            Dictionary item = new Dictionary(
                                    id.getText().toString(),
                                    title.getText().toString(),
                                    category.getText().toString(),
                                    delay.getText().toString(),
                                    content.getText().toString(),
                                    time.getText().toString(),
                                    average.getText().toString()
                            );
                            MyPointer.setDictionary(item);
                            MyPointer.setMode(MyPointer.getUPDATE_MODE());
                            mListener.onItemClick(v, position);
                        }
                    }
                }
            });

            /*view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        if (mList != null){
                            mListener.onItemClick(v, position);
                        }
                    }
                }
            });*/
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

        viewholder.id.setText(mList.get(position).getId());
        viewholder.time.setText(mList.get(position).getTime());
        viewholder.title.setText(mList.get(position).getTitle());
        viewholder.category.setText(mList.get(position).getCategory());
        viewholder.content.setText(mList.get(position).getContent());
        viewholder.delay.setText(mList.get(position).getDelay());
        viewholder.average.setText(mList.get(position).getAverage());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}
