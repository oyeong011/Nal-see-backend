package everycoding.nalseebackend.user.dto;

import lombok.Data;

@Data
public class UserLocationDto{
    private double latitude;
    private double longitude;

    public UserLocationDto(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
