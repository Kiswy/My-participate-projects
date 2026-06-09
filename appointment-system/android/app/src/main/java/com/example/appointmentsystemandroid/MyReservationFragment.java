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

public class MyReservationFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyReservationAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 加载布局文件
        View view = inflater.inflate(R.layout.fragment_my_reservation, container, false);

        recyclerView = view.findViewById(R.id.rvMyReservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyReservationAdapter();
        recyclerView.setAdapter(adapter);

        loadMockData();

        return view;
    }

    private void loadMockData() {
        List<MyReservationItem> mockList = new ArrayList<>();
        mockList.add(new MyReservationItem(1, "图书馆·自习室", "ZXS12LH", "李华", "2024-05-20", "已预约"));
        mockList.add(new MyReservationItem(2, "AI前沿讲座", "AI888", "李华", "2024-05-25", "已预约"));
        mockList.add(new MyReservationItem(3, "心理健康公开课", "XL999", "李华", "2024-06-01", "已预约"));

        adapter.setDataList(mockList);
    }
}