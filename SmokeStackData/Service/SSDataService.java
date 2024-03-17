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
        List<SSData> ssdlist = ssdRepository.findByfactmanagenm(keyword);
        return ssdlist;
    }
}
