package anton_ruban.fitz.network.res;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by antonruban on 12.05.2018.
 */

public class UserResponse {
    @SerializedName("userId")
    private int userID;
    @SerializedName("email")
    private String email;
    @SerializedName("username")
    private String username;
    @SerializedName("gender")
    private int gender;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("imagePath")
    private String imagePath;
    @SerializedName("phone")
    private String phone;
    @SerializedName("bithDate")
    private String bithDate;
    @SerializedName("isOwner")
    private int isOwner;
    @SerializedName("idClub")
    private int idClub;
    @SerializedName("isCoach")
    private int isCoach;

    public UserResponse() {

    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBithDate() {
        return bithDate;
    }

    public void setBithDate(String bithDate) {
        this.bithDate = bithDate;
    }

    public int getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }

    public int getIdClub() {
        return idClub;
    }

    public void setIdClub(int idClub) {
        this.idClub = idClub;
    }

    public int getIsCoach() {
        return isCoach;
    }

    public void setIsCoach(int isCoach) {
        this.isCoach = isCoach;
    }
}
