package anton_ruban.fitz.network.req;

/**
 * Created by antonruban on 08.05.2018.
 */

public class ClubReq {

    public int id;
    public String nameClub;
    public String description;
    public String emailClub;
    public String imagePath;
    public String phone;
    public String addres;
    public float lat;
    public float lng;
    public int idOwner;

    public ClubReq(String nameClub, String description, String emailClub, String phone, String addres) {
        this.nameClub = nameClub;
        this.description = description;
        this.emailClub = emailClub;
        this.phone = phone;
        this.addres = addres;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
