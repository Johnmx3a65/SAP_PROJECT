package sap.webapp.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import sap.webapp.db.entity.User;

import java.util.ArrayList;
import java.util.Collection;

public class ShopUserDetails extends User implements UserDetails {
    private ArrayList<String> roles;
    private User user;

    public ShopUserDetails(User user, ArrayList<String> roles){
        super(user.getEmail(), user.getFullName(), user.getCompanyName(), user.getPassword());

        this.roles = roles;
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        String userRoles = StringUtils.collectionToCommaDelimitedString(this.roles);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(userRoles);
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }

    public User getUser() {
        return user;
    }
}
