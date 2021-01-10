package sap.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sap.webapp.repository.CategoryRepository;
import sap.webapp.repository.ProductRepository;
import sap.webapp.repository.RoleRepository;
import sap.webapp.repository.UserRepository;

@Service
public class ShopSuperService {
    @Autowired
    protected RoleRepository roleRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected CategoryRepository categoryRepository;
    @Autowired
    protected ProductRepository productRepository;
}
