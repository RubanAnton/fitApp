package anton_ruban.fitz.network.req;

/**
 @author  antonruban on 25.04.2018.
 */

public class UserRegistrationReq {
    public String Email;
    public String Password;
    public String ConfirmPassword;

    public UserRegistrationReq(String email, String password, String confirmPassword) {
        Email = email;
        Password = password;
        ConfirmPassword = confirmPassword;
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
