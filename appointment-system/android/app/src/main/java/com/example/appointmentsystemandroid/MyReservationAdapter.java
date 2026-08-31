package com.example.appointmentsystemandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MyReservationAdapter extends RecyclerView.Adapter<MyReservationAdapter.ViewHolder> {
    private List<MyReservationItem> dataList;
    public interface OnDeleteClickListener {
        void onDeleteClick(
                MyReservationItem item
        );
    }

    private OnDeleteClickListener listener;

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.listener = listener;
    }

    public void setDataList(
            List<MyReservationItem> list
    ) {

        this.dataList = list;

        notifyDataSetChanged();
    }

    // 加载卡片样式
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(
                                parent.getContext()
                        )
                        .inflate(
                                R.layout.item_myreservation_card,
                                parent,
                                false
                        );

        return new ViewHolder(view);
    }

    // 填充卡片数据
    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {
        MyReservationItem item = dataList.get(position);

        holder.tvTitle.setText(item.getTitle());

        holder.tvSubtitle.setText(
                "预约码："
                        + item.getCode()
                        + " | "
                        + item.getUserName()
        );

        holder.tvDate.setText("预约日期：" + item.getDate());
        holder.btnDelete.setOnClickListener(
                v -> {
                    if(listener != null){
                        listener.onDeleteClick(item);
                    }
                }
        );
    }

    // 获取数据条数
    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvSubtitle;
        TextView tvDate;

        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.card_title);
            tvSubtitle = itemView.findViewById(R.id.card_subtitle);
            tvDate = itemView.findViewById(R.id.card_date);
            btnDelete = itemView.findViewById(R.id.card_btn);
        }
    }
}