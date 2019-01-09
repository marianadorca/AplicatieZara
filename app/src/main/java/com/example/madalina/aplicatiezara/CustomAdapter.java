package com.example.madalina.aplicatiezara;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


// clasa care face legatura intre datele din memorie de tip DataModel si componentele listei din UI
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<DataModel> dataSet;

    // aici se defineste ce campuri contine un rand al listei din UI
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewPrice;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    public CustomAdapter(ArrayList<DataModel> data) {
        this.dataSet = data;
    }

    // aici se construieste un rand din UI
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // contruieste un rand pe baza lui card_layout.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        // pune ca atunci cand dai tap pe un rand sa se apeleze myOnClickListener din MainActivity
        view.setOnClickListener(MainActivity.myOnClickListener);

        // creaza un obiect holder care sa tina datele din randul creat
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    // aici se updateaza un rand din UI
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewPrice = holder.textViewPrice;
        ImageView imageView = holder.imageViewIcon;

        // ia valorile din dataSet (datele din memorie) si le pune pe randul din UI
        textViewName.setText(dataSet.get(listPosition).getName());
        textViewPrice.setText(dataSet.get(listPosition).getPrice());
        imageView.setImageBitmap(dataSet.get(listPosition).getImage());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
