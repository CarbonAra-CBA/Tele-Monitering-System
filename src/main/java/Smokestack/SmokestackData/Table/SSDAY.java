package Smokestack.SmokestackData.Table;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class SSDAY {
    @Id
    private int Number;
    private String Area_nm;
    private Double Hcl;
    private Double TSP;
    private String Year_1;
    private Double Nox;
    private Double Nh3;
    private Double Co;
    private Double Sox;
    private Double Hf;

}
