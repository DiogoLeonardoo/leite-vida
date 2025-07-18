package com.mycompany.myapp.web.rest.vm;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * View Model object for storing a user's credentials.
 */
public class PasswordChangeVM {

    @NotNull
    @Size(min = 4, max = 100)
    private String newPassword;

    public PasswordChangeVM() {
        // Empty constructor needed for Jackson.
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PasswordChangeVM{" +
            "newPassword='" + newPassword + '\'' +
            "}";
    }
}
