package Smokestack.SmokestackData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SSDRepository extends JpaRepository<SSData, Integer> {
    
}
