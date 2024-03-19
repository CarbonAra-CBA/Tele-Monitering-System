package Smokestack.SmokestackData;

import Smokestack.SmokestackData.Service.DataParserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SmokestackDataApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SmokestackDataApplication.class, args);
		DataParserService dataParserService = context.getBean(DataParserService.class);
		dataParserService.parseData();
		dataParserService.parseAndSaveData();
	}
}
