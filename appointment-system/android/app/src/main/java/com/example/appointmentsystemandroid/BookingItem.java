package com.example.appointmentsystemandroid;

// 定义预约大厅卡片的数据结构
public class BookingItem {
    private int id;
    private String title;       // 标题：图书馆·自习室
    private String location;    // 地点：图书馆二层
    private String time;        // 时间：8:00-22:00
    private int remaining;      // 剩余数量：4
    private int total;          // 总数量：10

    // 构造方法（创建对象时使用）
    public BookingItem(int id, String title, String location, String time, int remaining, int total) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.time = time;
        this.remaining = remaining;
        this.total = total;
    }

    // Getter 方法（让其他类能获取这些数据）
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public String getTime() { return time; }
    public int getRemaining() { return remaining; }
    public int getTotal() { return total; }
}