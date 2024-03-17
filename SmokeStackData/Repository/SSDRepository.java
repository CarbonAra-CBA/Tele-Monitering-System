package Smokestack.SmokestackData.Repository;


import Smokestack.SmokestackData.Table.SSData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SSDRepository extends JpaRepository<SSData, Integer> {
    List<SSData> findAll();
    List<SSData> findByfactmanagenm(String keyword);




}
