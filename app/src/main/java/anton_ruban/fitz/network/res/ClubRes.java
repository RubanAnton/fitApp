package anton_ruban.fitz.network.res;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antonruban on 08.05.2018.
 */

public class ClubRes {

    @SerializedName("idClub")
    public int idClub;
    @SerializedName("nameClub")
    public String nameClub;
    @SerializedName("descriptionClub")
    public String description;
    @SerializedName("email")
    public String emailClub;
    @SerializedName("phone")
    public String phone;
    @SerializedName("imagePath")
    public String imagePath;
    @SerializedName("address")
    public String addres;
    @SerializedName("lat")
    public float lat;
    @SerializedName("lng")
    public float lng;
    @SerializedName("idOwner")
    public int idOwner;
    @SerializedName("idsCoachs")
    public String idsCoachs;

    public int getIdClub() {
        return idClub;
    }

    public void setIdClub(int idClub) {
        this.idClub = idClub;
    }

    public String getNameClub() {
        return nameClub;
    }

    public void setNameClub(String nameClub) {
        this.nameClub = nameClub;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmailClub() {
        return emailClub;
    }

    public void setEmailClub(String emailClub) {
        this.emailClub = emailClub;
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

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        this.addres = addres;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public int getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(int idOwner) {
        this.idOwner = idOwner;
    }

    public String getIdsCoachs() {
        return idsCoachs;
    }

    public void setIdsCoachs(String idsCoachs) {
        this.idsCoachs = idsCoachs;
    }
}
