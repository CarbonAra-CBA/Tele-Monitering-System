package Smokestack.SmokestackData.Table;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SSData {
    @Id
    @GeneratedValue
    private int Number;

    private String factmanagenm;

    private String stackcode;

    private double tsp; // 먼지

    private double nox; // 질소산화물

    private double sox; //황산화물

    private double hcl; // 염화수소

    private double hf; // 불화수소

    private double nh3; //암모니아

    private double co; //일산화탄소

    private double sum;


}
