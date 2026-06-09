package com.example.appointmentsystemandroid;

// 定义我的预约卡片的数据结构
public class MyReservationItem {
    private int id;
    private String title;       // 图书馆·自习室
    private String code;        // 预约码：ZXS12LH
    private String userName;    // 预约人：李华
    private String date;        // 预约日期：2024-05-20
    private String status;      // 状态：已预约

    public MyReservationItem(int id, String title, String code, String userName, String date, String status) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.userName = userName;
        this.date = date;
        this.status = status;
    }

    // Getter 方法
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getCode() { return code; }
    public String getUserName() { return userName; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
}