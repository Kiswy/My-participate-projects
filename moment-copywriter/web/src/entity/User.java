package entity;

public class User {
    private int id;
    private String username;
    private String phone;
    private String role;
    private String createTime;

    public User() {
    }

    public User(int id, String username, String phone, String role, String createTime) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.role = role;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
