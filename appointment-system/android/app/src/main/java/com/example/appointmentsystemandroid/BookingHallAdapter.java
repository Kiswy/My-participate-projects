package com.example.appointmentsystemandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// // 把 BookingItem 数据填充到 item_reservation_card.xml 卡片中，并处理预约按钮的点击事件。
public class BookingHallAdapter extends RecyclerView.Adapter<BookingHallAdapter.ViewHolder> {

    private List<BookingItem> dataList;

    // 设置数据（外部调用这个方法传入数据）
    public void setDataList(List<BookingItem> list) {
        this.dataList = list;
        notifyDataSetChanged();  // 通知 RecyclerView 刷新界面
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 把 item_reservation_card.xml 布局转换成 View 对象
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reservation_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingItem item = dataList.get(position);

        // 把数据设置到控件上
        holder.tvTitle.setText(item.getTitle());
        holder.tvLocation.setText(item.getLocation());
        holder.tvTime.setText(item.getTime());
        holder.tvStatus.setText("剩余 " + item.getRemaining() + "/" + item.getTotal());

        // 设置按钮点击事件
        holder.btnBook.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "预约：" + item.getTitle(), Toast.LENGTH_SHORT).show();
            // TODO: 等后端写好，这里发送网络请求
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    // ViewHolder：缓存卡片中的控件，避免重复 findViewById
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvLocation, tvTime, tvStatus;
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