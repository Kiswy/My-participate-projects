import dao.UserDao;

public class Main {

    public static void main(String[] args) {

        UserDao userDao =
                new UserDao();

        boolean success =
                userDao.register(
                        "wangwu",
                        "123456",
                        "13800000003"
                );

        System.out.println(success);

    }

}