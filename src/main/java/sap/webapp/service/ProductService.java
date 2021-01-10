package sap.webapp.service;

import org.springframework.stereotype.Service;
import sap.webapp.binding.model.ProductBindingModel;
import sap.webapp.entity.Category;
import sap.webapp.entity.Product;
import sap.webapp.entity.User;

import java.io.IOException;
import java.util.Base64;

@Service
public class ProductService extends ShopSuperService{

    public boolean userIsAdmin(User user){
        return user.getRoles().contains(roleRepository.findByName("ROLE_ADMIN")) || user.getRoles().contains(roleRepository.findByName("ROLE_SUPER"));
    }

    public void createProduct(ProductBindingModel productBindingModel) throws IOException {

        User company = this.userRepository.getOne(productBindingModel.getUserId());

        Category category = this.categoryRepository.getOne(productBindingModel.getCategoryId());

        Product productEntity = new Product(productBindingModel.getPrice(), productBindingModel.getCurrentCount(), productBindingModel.getWarnCount(), productBindingModel.getTitle(), productBindingModel.getDescription(), productBindingModel.getPhoto(), company, category);

        this.productRepository.saveAndFlush(productEntity);
    }
    public void editProduct(ProductBindingModel productBindingModel, Integer id) throws IOException {
        Product product = this.productRepository.getOne(id);
        Category category = this.categoryRepository.getOne(productBindingModel.getCategoryId());
        User user = this.userRepository.getOne(productBindingModel.getUserId());

        product.setTitle(productBindingModel.getTitle());
        product.setPrice(productBindingModel.getPrice());

        if (!productBindingModel.getPhoto().isEmpty()){
            product.setPhotoBase64(Base64.getEncoder().encodeToString(productBindingModel.getPhoto().getBytes()));
        }

        product.setAuthor(user);
        product.setDescription(productBindingModel.getDescription());
        product.setCategory(category);
        product.setCurrentCount(productBindingModel.getCurrentCount());
        product.setWarnCount(productBindingModel.getWarnCount());

        this.productRepository.saveAndFlush(product);
    }

    public void deleteProduct(Integer id){

        Product product = this.productRepository.getOne(id);

        this.productRepository.delete(product);
    }
}
