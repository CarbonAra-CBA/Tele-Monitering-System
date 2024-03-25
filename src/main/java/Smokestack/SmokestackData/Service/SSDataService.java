package Smokestack.SmokestackData.Service;

import Smokestack.SmokestackData.Repository.SSDRepository;
import Smokestack.SmokestackData.Table.SSData;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;




@Service
public class SSDataService {
    @Autowired
    SSDRepository ssdRepository;
    @Transactional
    public List<SSData> serach(String keyword){
        /*공장을 관리하는 회사의 이름을 기준으로 그 회사의 관리 하에 있는 공장들을 가져오기*/
        List<SSData> ssdlist = ssdRepository.findByfactmanagenm(keyword);
        return ssdlist;
    }
}
