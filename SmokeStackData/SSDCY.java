package Smokestack.SmokestackData;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
// 각 기업별 3년치 상세내역을 하려고 했으나 너무 많을거 같아서 Top10만 할 생각
@Entity
@Getter @Setter
public class SSDCY {
    @Id
    private Long id;

}
