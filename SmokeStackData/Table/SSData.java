package Smokestack.SmokestackData;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity

@Getter @Setter
public class SSData {
    @Id @GeneratedValue
    private int Number;

    private String Fact_manage_nm;

    private String Stack_code;

    private double Tsp; // 먼지

    private double Nox; // 질소산화물

    private double Sox; //황산화물

    private double Hcl; // 염화수소

    private double Hf; // 불화수소

    private double Nh3; //암모니아

    private double Co; //일산화탄소

    private double Sum;


}


