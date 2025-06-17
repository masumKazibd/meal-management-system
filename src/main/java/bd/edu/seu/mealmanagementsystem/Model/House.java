package bd.edu.seu.mealmanagementsystem.Model;

public class House {
    private int houseId;
    private String houseName;
    private String genderType;

    public House(int houseId, String houseName, String genderType) {
        this.houseId = houseId;
        this.houseName = houseName;
        this.genderType = genderType;
    }

    public int getHouseId() {
        return houseId;
    }

    public String getHouseName() {
        return houseName;
    }

    public String getGenderType() {
        return genderType;
    }

    @Override
    public String toString() {
        return houseName;
    }

    }

