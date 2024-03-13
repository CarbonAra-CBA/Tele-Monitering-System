package Smokestack.SmokestackData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SSDAYRepository extends JpaRepository<SSDAY, Integer> {

}
