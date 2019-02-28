package com.yesia.crudsqlite.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yesia.crudsqlite.R;
import com.yesia.crudsqlite.detail.DetailActivity;
import com.yesia.crudsqlite.model.DataDiri;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataDiriAdapter extends RecyclerView.Adapter<DataDiriAdapter.ViewHolder> {

    LinkedList<DataDiri> listDataDiri;

    Activity activity;

    public DataDiriAdapter(Activity activity) {
        this.activity = activity;
    }

    public LinkedList<DataDiri> getListDataDiri() {
        return listDataDiri;
    }

    public void setListDataDiri(LinkedList<DataDiri> listDataDiri) {
        this.listDataDiri = listDataDiri;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_data_diri, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        // set dan get sesuai position pada item_biodata.xml
        viewHolder.tvItemNama.setText(getListDataDiri().get(i).getName());
        viewHolder.tvItemUmur.setText(getListDataDiri().get(i).getAge());
        viewHolder.tvItemGender.setText(getListDataDiri().get(i).getGender());
        viewHolder.tvItemWeight.setText(getListDataDiri().get(i).getWeight());
        viewHolder.tvItemDate.setText(getListDataDiri().get(i).getDate());

        // lalu disini event click pada cardview
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // yang akan putExtra ke BiodataActivity sesuai position nya
                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_POSITION, i);
                intent.putExtra(DetailActivity.EXTRA_NOTE, getListDataDiri().get(i));
                activity.startActivityForResult(intent, DetailActivity.REQUEST_UPDATE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return getListDataDiri().size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_date)
        TextView tvItemDate;
        @BindView(R.id.tv_item_nama)
        TextView tvItemNama;
        @BindView(R.id.tv_item_umur)
        TextView tvItemUmur;
        @BindView(R.id.tv_item_gender)
        TextView tvItemGender;
        @BindView(R.id.tv_item_weight)
        TextView tvItemWeight;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
