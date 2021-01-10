package sap.webapp.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import sap.webapp.binding.model.UserBindingModel;
import sap.webapp.binding.model.UserEditBindingModel;
import sap.webapp.entity.Product;
import sap.webapp.entity.Role;
import sap.webapp.entity.User;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService extends ShopSuperService{

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

    public void editUser(UserEditBindingModel userBindingModel, Integer id){

        User user = this.userRepository.getOne(id);

        if(!StringUtils.isEmpty(userBindingModel.getPassword()) && !StringUtils.isEmpty(userBindingModel.getConfirmPassword())){
            if(userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())){
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

                user.setPassword(bCryptPasswordEncoder.encode(userBindingModel.getPassword()));
            }
        }
        user.setFullName(userBindingModel.getFullName());
        user.setCompanyName(userBindingModel.getCompanyName());
        user.setEmail(userBindingModel.getEmail());

        Set<Role> roles = new HashSet<>();

        for (Integer roleId : userBindingModel.getRoles()){
            roles.add(this.roleRepository.getOne(roleId));
        }

        user.setRoles(roles);

        this.userRepository.saveAndFlush(user);
    }

    public void deleteUser(Integer id){

        User user = this.userRepository.getOne(id);

        for(Product product : user.getProducts()){
            this.productRepository.delete(product);
        }

        this.userRepository.delete(user);
    }

    public boolean isSuper(User user){
        return user.getRoles().contains(roleRepository.findByName("ROLE_SUPER"));
    }
}
