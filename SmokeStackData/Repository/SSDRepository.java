package Smokestack.SmokestackData.Repository;


import Smokestack.SmokestackData.Table.SSData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //SSDataRepository
public interface SSDRepository extends JpaRepository<SSData, Integer> {
    List<SSData> findAll(); // 지도에 회사 표시
    List<SSData> findByfactmanagenm(String keyword); //특정 회사 검색




}
