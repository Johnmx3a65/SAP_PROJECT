package sap.webapp.service;

import org.springframework.stereotype.Service;
import sap.webapp.binding.model.CategoryBindingModel;
import sap.webapp.entity.Category;
import sap.webapp.entity.Product;

@Service
public class CategoryService extends ShopSuperService{

    public void createCategory(CategoryBindingModel categoryBindingModel){

        Category category = new Category(categoryBindingModel.getName());

        this.categoryRepository.saveAndFlush(category);
    }

    public void editCategory(CategoryBindingModel categoryBindingModel, Integer id){

        Category category = this.categoryRepository.getOne(id);

        category.setName(categoryBindingModel.getName());

        this.categoryRepository.saveAndFlush(category);

    }

    public void deleteCategory(Integer id){

        Category category = this.categoryRepository.getOne(id);

        for(Product product : category.getProducts()){
            this.productRepository.delete(product);
        }

        this.categoryRepository.delete(category);
    }
}
