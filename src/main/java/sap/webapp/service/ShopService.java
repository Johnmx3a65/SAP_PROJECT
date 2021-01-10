package sap.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sap.webapp.entity.User;
import sap.webapp.repository.RoleRepository;

@Service
public class ShopService {
    @Autowired
    private RoleRepository roleRepository;

    public boolean userIsAdmin(User user){
        return user.getRoles().contains(roleRepository.findByName("ROLE_ADMIN")) || user.getRoles().contains(roleRepository.findByName("ROLE_SUPER"));
    }
}
