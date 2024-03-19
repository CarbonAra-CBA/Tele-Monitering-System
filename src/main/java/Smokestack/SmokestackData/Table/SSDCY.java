package Smokestack.SmokestackData.Table;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class SSDCY {
    @Id
    private String name;
    private String address;
    private double latitude;
    private double longitude;

}
