package everycoding.nalseebackend.user.dto;

import everycoding.nalseebackend.BaseEntity;
import lombok.Data;

@Data
public class UserLocationDto extends BaseEntity {
    private double latitude;
    private double longitude;

    public UserLocationDto(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
