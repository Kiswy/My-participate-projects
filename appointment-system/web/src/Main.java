import dao.CategoryDao;
import entity.Category;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        CategoryDao dao =
                new CategoryDao();

        List<Category> list =
                dao.getAllCategories();

        for(Category category : list){

            System.out.println(
                    category.getId()
                    + " - "
                    + category.getCategoryName()
            );

        }

    }
}