package Smokestack.SmokestackData;

import Smokestack.SmokestackData.Repository.SSDCYRepository;
import Smokestack.SmokestackData.Repository.SSDRepository;
import Smokestack.SmokestackData.Table.SSDCY;
import Smokestack.SmokestackData.Table.SSData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;

@Controller
public class HelloController {
    @Autowired
    SSDCYRepository ssdcyRepository;
    @Autowired
    SSDRepository ssdRepository;
    @GetMapping("/")
    public String hello(Model model, String keyword) {


        List<SSDCY> ssdcyList = ssdcyRepository.findAll();
        List<SSData> ssdList = ssdRepository.findAll();


        model.addAttribute("ssdcyListJson", ssdcyList);
        model.addAttribute("ssdList", ssdList);

        return "hello";
    }
}
