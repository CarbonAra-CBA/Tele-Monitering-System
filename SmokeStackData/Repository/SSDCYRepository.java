package Smokestack.SmokestackData.Repository;

import Smokestack.SmokestackData.Table.SSDAY;
import Smokestack.SmokestackData.Table.SSDCY;
import Smokestack.SmokestackData.Table.SSData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SSDCYRepository extends JpaRepository<SSDCY, String> {
    List<SSDCY> findAll();
}
