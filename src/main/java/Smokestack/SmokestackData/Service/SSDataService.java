package Smokestack.SmokestackData.Service;

import Smokestack.SmokestackData.Repository.SSDCYRepository;
import Smokestack.SmokestackData.Repository.SSDRepository;
import Smokestack.SmokestackData.Table.SSDCY;
import Smokestack.SmokestackData.Table.SSData;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;




@Service
public class SSDataService {
    @Autowired
    SSDRepository ssdRepository;
    @Autowired
    SSDCYRepository ssdcyRepository;
    @Transactional
    public List<SSData> serach(String keyword){ //지도에 표시하기 위해 findBy 사용
        List<SSData> ssdlist = ssdRepository.findByfactmanagenm(keyword);
        return ssdlist;
    }
    @Transactional
    public void UpdateData(){
        List<SSData> allData = ssdRepository.findAll();
        List<SSDCY> ssdcyList = ssdcyRepository.findAll();
        ssdcyList.forEach(ssdcy -> {
            ssdcy.setTsp(0);
            ssdcy.setNox(0);
            ssdcy.setSox(0);
            ssdcy.setHcl(0);
            ssdcy.setHf(0);
            ssdcy.setNh3(0);
            ssdcy.setCo(0);
            ssdcy.setSum(0);
            ssdcyRepository.save(ssdcy);
        });
        allData.forEach(ssData ->{
            SSDCY ssdcy = ssdcyRepository.findByName(ssData.getFactmanagenm()).orElse(null);
            if (ssdcy != null) {
                ssdcy.setTsp(Math.round((ssdcy.getTsp() + ssData.getTsp()) * 1000) / 1000.0);
                ssdcy.setNox(Math.round((ssdcy.getNox() + ssData.getNox()) * 1000) / 1000.0);
                ssdcy.setSox(Math.round((ssdcy.getSox() + ssData.getSox()) * 1000) / 1000.0);
                ssdcy.setHcl(Math.round((ssdcy.getHcl() + ssData.getHcl()) * 1000) / 1000.0);
                ssdcy.setHf(Math.round((ssdcy.getHf() + ssData.getHf()) * 1000) / 1000.0);
                ssdcy.setNh3(Math.round((ssdcy.getNh3() + ssData.getNh3()) * 1000) / 1000.0);
                ssdcy.setCo(Math.round((ssdcy.getCo() + ssData.getCo()) * 1000) / 1000.0);
                ssdcy.setSum(Math.round((ssdcy.getSum() + ssData.getSum()) * 1000) / 1000.0);

                ssdcyRepository.save(ssdcy);
            }
        });


    }


}
