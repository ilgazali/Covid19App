package com.example.covid19app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    int m = 1;
    Context mContext;
    List<com.example.covid19app.ModelClass> countryList;

    public CardAdapter(Context mContext, List<com.example.covid19app.ModelClass> countryList) {
        this.mContext = mContext;
        this.countryList = countryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item,null,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        com.example.covid19app.ModelClass modelClass = countryList.get(position);

        if (m == 1){
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(modelClass.getCases())));
        }else if(m == 2){
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(modelClass.getRecovered())));
        }else if(m == 3){
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(modelClass.getDeaths())));
        }else{
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(modelClass.getActive())));
        }

        holder.country.setText(modelClass.getCountry());

    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cases, country;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cases = itemView.findViewById(R.id.countryCase);
            country = itemView.findViewById(R.id.countryName);
        }
    }

    public void filter(String charText){

        if (charText.equals("Cases")){
            m = 1;
        }else if(charText.equals("Recovered")){
            m = 2;
        }else if(charText.equals("Deaths")){
            m = 3;
        }else{
            m = 4;
        }

        notifyDataSetChanged();
    }
}
