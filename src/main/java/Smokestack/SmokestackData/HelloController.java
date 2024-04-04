package Smokestack.SmokestackData;

import Smokestack.SmokestackData.Repository.SSDCYRepository;
import Smokestack.SmokestackData.Repository.SSDRepository;
import Smokestack.SmokestackData.Table.SSDCY;

import Smokestack.SmokestackData.Table.SSData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HelloController {
    @Autowired
    SSDCYRepository ssdcyRepository;
    @Autowired
    SSDRepository ssdRepository;
    @GetMapping("/")
    public String hello(Model model,String ch) {

        if (ch == null || ch.isEmpty()) {
            ch = "Sox"; // 기본값 설정
        }
        List<SSDCY> ssdcyList = ssdcyRepository.findAll();
        List<SSData> ssdList = ssdRepository.findAll();
        //List<SSData> ssdList2 = ssdRepository.findByfactmanagenm(keyword);
        //List<SSDCY> ssdcyList2 = ssdcyRepository.findAll(Sort.by(Sort.Direction.DESC, ch));
        List<SSDCY> ssdcyList2 = ssdcyRepository.findAll(Sort.by(Sort.Direction.DESC, ch));
        model.addAttribute("ssdcyListJson", ssdcyList);
        model.addAttribute("ssdList", ssdList);
        //model.addAttribute("ssdList2", ssdList2);
        model.addAttribute("ssdcyList2", ssdcyList2);
        model.addAttribute("ch", ch);

        return "hello";
    }
}
