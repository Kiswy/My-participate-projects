package com.example.appointmentsystemandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class BookingHallFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookingHallAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 加载布局文件
        View view = inflater.inflate(R.layout.fragment_booking_hall, container, false);

        recyclerView = view.findViewById(R.id.rvBookingHall);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new BookingHallAdapter();
        recyclerView.setAdapter(adapter);

        loadMockData();

        return view;
    }

    private void loadMockData() {
        List<BookingItem> mockList = new ArrayList<>();
        mockList.add(new BookingItem(1, "图书馆·自习室", "图书馆二层", "8:00-22:00", 4, 10));
        mockList.add(new BookingItem(2, "图书馆·研讨室", "图书馆三层", "10:00-22:00", 3, 8));
        mockList.add(new BookingItem(3, "AI前沿讲座", "图书馆报告厅", "14:00-16:00", 45, 100));
        mockList.add(new BookingItem(4, "心理健康公开课", "学生活动中心", "15:30-17:00", 60, 80));

        adapter.setDataList(mockList);
    }
}