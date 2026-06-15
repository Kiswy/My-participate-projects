package com.example.appointmentsystemandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// 把 BookingItem 数据填充到 item_reservation_card.xml 卡片中
public class BookingHallAdapter extends RecyclerView.Adapter<BookingHallAdapter.ViewHolder> {
    private List<BookingItem> dataList;

    // 预约按钮点击回调接口
    public interface OnBookClickListener {
        void onBookClick(BookingItem item);
    }

    private OnBookClickListener listener;
    public void setOnBookClickListener(OnBookClickListener listener) {this.listener = listener;}

    // 设置数据
    public void setDataList(List<BookingItem> list) {
        this.dataList = list;
        notifyDataSetChanged();
    }

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
                                R.layout.item_reservation_card,
                                parent,
                                false
                        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        BookingItem item = dataList.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvLocation.setText(item.getLocation());
        holder.tvTime.setText(item.getTime());
        holder.tvStatus.setText("剩余 " + item.getRemaining() + "/" + item.getTotal());
        holder.btnBook.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvLocation;
        TextView tvTime;
        TextView tvStatus;

        Button btnBook;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.card_title);
            tvLocation = itemView.findViewById(R.id.card_location);
            tvTime = itemView.findViewById(R.id.card_time);
            tvStatus = itemView.findViewById(R.id.card_status);
            btnBook = itemView.findViewById(R.id.card_btn);
        }
    }
}