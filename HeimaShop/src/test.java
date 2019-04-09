import java.util.List;

import cn.itcast.store.domain.Category;
import cn.itcast.store.service.ProductService;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProductService service = new ProductService();
		List<Category> list = service.findAllCategory();
		for(Category category :list){
			System.out.println(category);
		}

	}

}
