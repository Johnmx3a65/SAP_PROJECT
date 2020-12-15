package sap.webapp.binding.model;


import javax.validation.constraints.NotNull;

public class UserBindingModel extends UserLoginBindingModel{

    @NotNull
    private String fullName;

    @NotNull
    private String companyName;

    @NotNull
    private String confirmPassword;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
