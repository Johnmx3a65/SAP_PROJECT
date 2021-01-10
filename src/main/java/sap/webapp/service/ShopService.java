package sap.webapp.service;

import com.sun.xml.bind.v2.util.QNameMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sap.webapp.binding.model.ProductBindingModel;
import sap.webapp.binding.model.UserBindingModel;
import sap.webapp.entity.Category;
import sap.webapp.entity.Product;
import sap.webapp.entity.Role;
import sap.webapp.entity.User;
import sap.webapp.repository.CategoryRepository;
import sap.webapp.repository.ProductRepository;
import sap.webapp.repository.RoleRepository;
import sap.webapp.repository.UserRepository;

import java.io.IOException;

@Service
public class ShopService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    public boolean userIsAdmin(User user){
        return user.getRoles().contains(roleRepository.findByName("ROLE_ADMIN")) || user.getRoles().contains(roleRepository.findByName("ROLE_SUPER"));
    }

    public void createUser(UserBindingModel userBindingModel){

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user = new User(userBindingModel.getEmail(),
                userBindingModel.getFullName(),
                userBindingModel.getCompanyName(),
                bCryptPasswordEncoder.encode(userBindingModel.getPassword()));

        Role userRole = this.roleRepository.findByName("ROLE_USER");

        user.addRole(userRole);

        this.userRepository.saveAndFlush(user);
    }

    public void createProduct(ProductBindingModel productBindingModel) throws IOException {

        User company = this.userRepository.getOne(productBindingModel.getUserId());

        Category category = this.categoryRepository.getOne(productBindingModel.getCategoryId());

        Product productEntity = new Product(productBindingModel.getPrice(), productBindingModel.getCurrentCount(), productBindingModel.getWarnCount(), productBindingModel.getTitle(), productBindingModel.getDescription(), productBindingModel.getPhoto(), company, category);

        this.productRepository.saveAndFlush(productEntity);
    }
}
