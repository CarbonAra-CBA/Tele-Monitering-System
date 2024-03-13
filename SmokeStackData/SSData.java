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

    private String Tsp; // 먼지

    private String Nox; // 질소산화물

    private String Sox; //황산화물

    private String Hcl; // 염화수소

    private String Hf; // 불화수소

    private String Nh3; //암모니아

    private String Co; //일산화탄소


}


