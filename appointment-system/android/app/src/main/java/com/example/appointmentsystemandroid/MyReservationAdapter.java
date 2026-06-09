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

// 把 MyReservationItem 数据填充到 item_myreservation_card.xml 卡片中，并处理预约按钮的点击事件。
public class MyReservationAdapter extends RecyclerView.Adapter<MyReservationAdapter.ViewHolder> {

    private List<MyReservationItem> dataList;

    public void setDataList(List<MyReservationItem> list) {
        this.dataList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_myreservation_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyReservationItem item = dataList.get(position);

        holder.tvTitle.setText(item.getTitle());
        holder.tvSubtitle.setText("预约码：" + item.getCode() + " | " + item.getUserName());
        holder.tvDate.setText("预约日期：" + item.getDate());

        holder.btnDelete.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "删除预约：" + item.getTitle(), Toast.LENGTH_SHORT).show();
            // TODO: 等后端写好，这里发送删除请求
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSubtitle, tvDate;
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