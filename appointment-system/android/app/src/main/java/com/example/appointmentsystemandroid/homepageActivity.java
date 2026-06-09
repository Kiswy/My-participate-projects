package com.example.appointmentsystemandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class homepageActivity extends AppCompatActivity {

    private View navBookingHall;
    private View navMyReservation;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        // 获取导航按钮
        navBookingHall = findViewById(R.id.nav_booking_hall);
        navMyReservation = findViewById(R.id.nav_my_reservation);

        // 设置按钮文字
        TextView tv1 = navBookingHall.findViewById(R.id.nav_text);
        tv1.setText("预约大厅");
        TextView tv2 = navMyReservation.findViewById(R.id.nav_text);
        tv2.setText("我的预约");

        // 预约大厅点击事件
        navBookingHall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavSelection(navBookingHall);
                switchToFragment(new BookingHallFragment());
            }
        });

        // 我的预约点击事件
        navMyReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNavSelection(navMyReservation);
                switchToFragment(new MyReservationFragment());
            }
        });

        // 默认选中预约大厅
        setNavSelection(navBookingHall);
        switchToFragment(new BookingHallFragment());
    }

    // 切换 Fragment
    private void switchToFragment(Fragment fragment) {
        getSupportFragmentManager()                        // 1. 获取Fragment管理器
                .beginTransaction()                        // 2. 开始一个事务
                .replace(R.id.content_container, fragment) // 3. 把容器里的内容替换成新的Fragment
                .commit();                                 // 4. 提交事务，执行替换
    }

    // 设置导航按钮选中状态
    private void setNavSelection(View selectedNav) {
        navBookingHall.setSelected(false);
        navMyReservation.setSelected(false);
        selectedNav.setSelected(true);
    }
}