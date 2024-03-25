package Smokestack.SmokestackData.Repository;


import Smokestack.SmokestackData.Table.SSData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SSDRepository extends JpaRepository<SSData, Integer> {
    // SSData 테이블의 모든 열을 가져오는 sql 함수
    List<SSData> findAll();
    // SSDate 테이블에서 factmanagenm 컬럼에서 keyword 문자열과 같은 열을 찾는 sql 함수
    // 참고로 factmanagenm은 회사명이다.
    List<SSData> findByfactmanagenm(String keyword);




}
