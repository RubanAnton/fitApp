package anton_ruban.fitz.network.req;

import com.google.gson.annotations.SerializedName;

/**
 @author  antonruban on 25.04.2018.
 */

public class UserRegistrationReq {

    @SerializedName("Email")
    public String Email;
    @SerializedName("Password")
    public String Password;
    @SerializedName("ConfirmPassword")
    public String ConfirmPassword;

    public UserRegistrationReq() {

    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }
}
