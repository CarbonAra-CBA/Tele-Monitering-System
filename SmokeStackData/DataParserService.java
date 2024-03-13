package Smokestack.SmokestackData;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataParserService {
    @Autowired
    private SSDRepository ssdRepository;
    public boolean checkInt(String s){
        return (48 <= s.charAt(0) && s.charAt(0) <= 57);
    }
    public void parseData() {
        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/cleansys/rltmMesureResult"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=서비스키"); /*Service Key*/
            //urlBuilder.append("&" + URLEncoder.encode("areaNm", "UTF-8") + "=" + URLEncoder.encode("서울", "UTF-8")); /*지역 명 LIKE 검색*/
            //urlBuilder.append("&" + URLEncoder.encode("factManageNm","UTF-8") + "=" + URLEncoder.encode("노원자원회수시설", "UTF-8")); /*사업장의 이름 LIKE 검색*/
            //urlBuilder.append("&" + URLEncoder.encode("stackCode","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*배출구 번호*/
            urlBuilder.append("&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*json, xml 중 택1*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            //System.out.println(sb.toString());
            String filePath = "data.json";

            // JSON 파일에 데이터 쓰기
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(sb.toString());
                System.out.println("Data saved to JSON file: " + filePath);
            } catch (IOException e) {
                System.out.println("Error writing to JSON file: " + e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void parseAndSaveData() {
        try {
            String json = new String(Files.readAllBytes(Paths.get("data.json")), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            List<SSData> dataList = new ArrayList<>();

            try {
                JsonNode rootNode = mapper.readTree(json);
                JsonNode items = rootNode.path("response").path("body").path("items");
                int i = 1;
                double s;
                for (JsonNode node : items) {
                    double sum = 0;
                    SSData data = new SSData();
                    boolean check1;
                    data.setNumber(i);
                    data.setFact_manage_nm(node.path("fact_manage_nm").asText());

                    data.setStack_code(node.path("stack_code").asText());
                    check1 = checkInt(node.path("tsp_mesure_value").asText());
                    if(check1){
                        s = Double.parseDouble(node.path("tsp_mesure_value").asText());
                        data.setTsp(s);
                        sum += s;

                    }
                    else data.setTsp(0);

                    check1 = checkInt(node.path("sox_mesure_value").asText());
                    if(check1){
                        s = Double.parseDouble(node.path("sox_mesure_value").asText());
                        data.setSox(s);
                        sum += s;

                    }
                    else data.setSox(0);

                    check1 = checkInt(node.path("nox_mesure_value").asText());
                    if(check1){
                        s = Double.parseDouble(node.path("nox_mesure_value").asText());
                        data.setNox(s);
                        sum += s;

                    }
                    else data.setNox(0);

                    check1 = checkInt(node.path("hcl_mesure_value").asText());
                    if(check1){
                        s = Double.parseDouble(node.path("hcl_mesure_value").asText());
                        data.setHcl(s);
                        sum += s;

                    }
                    else data.setHcl(0);

                    check1 = checkInt(node.path("hf_mesure_value").asText());
                    if(check1){
                        s = Double.parseDouble(node.path("hf_mesure_value").asText());
                        data.setHf(s);
                        sum += s;

                    }
                    else data.setHf(0);

                    check1 = checkInt(node.path("nh3_mesure_value").asText());
                    if(check1){
                        s = Double.parseDouble(node.path("nh3_mesure_value").asText());
                        data.setNh3(s);
                        sum += s;

                    }
                    else data.setNh3(0);

                    check1 = checkInt(node.path("co_mesure_value").asText());
                    if(check1){
                        s = Double.parseDouble(node.path("co_mesure_value").asText());
                        data.setCo(s);
                        sum += s;

                    }
                    else data.setCo(0);
                    data.setSum(Math.round(sum * 1000.0) / 1000.0);
                    i++;
                    dataList.add(data);
                }

                ssdRepository.saveAll(dataList); // 데이터베이스에 저장
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
