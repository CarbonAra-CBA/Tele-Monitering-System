package Smokestack.SmokestackData.Service;

import Smokestack.SmokestackData.Repository.SSDCYRepository;
import Smokestack.SmokestackData.Table.SSDAY;
import Smokestack.SmokestackData.Repository.SSDAYRepository;
import Smokestack.SmokestackData.Repository.SSDRepository;
import Smokestack.SmokestackData.Table.SSDCY;

import Smokestack.SmokestackData.Table.SSData;
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
    @Autowired
    private SSDAYRepository ssdayRepository;
    @Autowired
    private SSDCYRepository ssdcyRepository;
    public boolean checkInt(String s){
        return (48 <= s.charAt(0) && s.charAt(0) <= 57);
    }

    /*api를 이용해서 지역 정보를 json 형태로 수집한 다음 data.json, data2.json, data3.json 파일에 덮어쓰기
    * data.json, data3.json 에는 SSData 데이터베이스에 업데이트 할 공장 정보(배출한 화학물질 양 등)이 저장되어 있음
    * data2.json에는 각 지역별 화학물질 배출량 통계 정보들이 저장되어 있음*/
    public void parseData() {
        try {
            /*api를 이용하여 공장 정보를 json으로 받아서 data.json으로 저장*/
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/cleansys/rltmMesureResult"); /*URL, 서비스 키 등 여러 문자열을 붙이기 위해 StringBuilder 객체로 선언*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=zyPKasfClmXWQUP0jZLMGiyMTO0Ykuzu1p5AXxRMTWZLpzeDtt58hJaPT4VvUMpwsswMa2pMScIgEKCGwb3ECw=="); /*Service Key*/
            //urlBuilder.append("&" + URLEncoder.encode("areaNm", "UTF-8") + "=" + URLEncoder.encode("서울", "UTF-8")); /*지역 명 LIKE 검색*/
            //urlBuilder.append("&" + URLEncoder.encode("factManageNm","UTF-8") + "=" + URLEncoder.encode("노원자원회수시설", "UTF-8")); /*사업장의 이름 LIKE 검색*/
            //urlBuilder.append("&" + URLEncoder.encode("stackCode","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*배출구 번호*/
            urlBuilder.append("&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*json, xml 중 택1, 여기서는 json을 선택*/
            URL url = new URL(urlBuilder.toString()); /*완성된 Url 문자열을 URL 객체에 넣기*/
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); /*서버와 연결, 앞으로 통신을 담당할 객체 생성*/
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json"); /*Content-Type 헤더로 서버에게 요청 본문이 json 형식임을 알리기*/
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            /*getResponseCode의 반환값에 따라 HTTP 상태를 알 수 있음.
            * 2xx 상태 코드는 요청이 성공적으로 처리되었음을 나타내므로 이 경우에만 네트워크 I/O를 수행*/
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
        try {
            /*api를 이용하여 지역별 탄소 배출량 통계 정보를 json으로 받아서 data2.json으로 저장*/
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/cleansys/areaFyerBsnesStatsInfo"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=zyPKasfClmXWQUP0jZLMGiyMTO0Ykuzu1p5AXxRMTWZLpzeDtt58hJaPT4VvUMpwsswMa2pMScIgEKCGwb3ECw=="); /*Service Key*/
            //urlBuilder.append("&" + URLEncoder.encode("areaNm","UTF-8") + "=" + URLEncoder.encode("서울", "UTF-8")); /*지역 명 LIKE 검색 ( 서울특별시 충청북도 충청남도 전라북도 전라남도 경상북도 경상남도 제주특별자치도 세종특별자치시 부산광역시 대구광역시 인천광역시 광주광역시 대전광역시 울산광역시 경기도 강원도 )*/
            urlBuilder.append("&" + URLEncoder.encode("SearchBeginYear","UTF-8") + "=" + URLEncoder.encode("2018", "UTF-8")); /*통계 검색의 시작년도*/
            urlBuilder.append("&" + URLEncoder.encode("SearchEndYear","UTF-8") + "=" + URLEncoder.encode("2020", "UTF-8")); /*통계 검색의 종료 년도*/
            urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*json, xml 중 택1 Default 는 json*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
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
            String filePath = "data2.json";

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
        try {
            /*api를 이용하여 공장 정보를 json으로 받아서 data3.json으로 저장*/
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/cleansys/recentThYearCmprFyerBsnesStatsInfo"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=zyPKasfClmXWQUP0jZLMGiyMTO0Ykuzu1p5AXxRMTWZLpzeDtt58hJaPT4VvUMpwsswMa2pMScIgEKCGwb3ECw=="); /*Service Key*/
            //urlBuilder.append("&" + URLEncoder.encode("areaNm","UTF-8") + "=" + URLEncoder.encode("서울", "UTF-8")); /*지역 명 LIKE 검색 ( 서울특별시 충청북도 충청남도 전라북도 전라남도 경상북도 경상남도 제주특별자치도 세종특별자치시 부산광역시 대구광역시 인천광역시 광주광역시 대전광역시 울산광역시 경기도 강원도 )*/
            //urlBuilder.append("&" + URLEncoder.encode("factManageNm","UTF-8") + "=" + URLEncoder.encode("노원자원회수시설", "UTF-8")); /*사업장의 이름 LIKE 검색*/
            urlBuilder.append("&" + URLEncoder.encode("searchYear","UTF-8") + "=" + URLEncoder.encode("2018", "UTF-8")); /*통계 검색의 시작년도*/
            urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*json, xml 중 택1 Default 는 json*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
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
            String filePath = "data3.json";

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
            /*공장 정보(배출하는 화학 물질의 양 등)를 SSData 데이터베이스로 옮기기*/
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
                    data.setFactmanagenm(node.path("fact_manage_nm").asText());

                    data.setStackcode(node.path("stack_code").asText());
                    check1 = checkInt(node.path("tsp_mesure_value").asText());
                    if(check1){
                        String a = (node.path("tsp_mesure_value").asText());
                        s = Double.parseDouble(a.replaceAll(",", ""));
                        data.setTsp(s);
                        sum += s;

                    }
                    else data.setTsp(0);

                    check1 = checkInt(node.path("sox_mesure_value").asText());
                    if(check1){
                        String a = (node.path("sox_mesure_value").asText());
                        s = Double.parseDouble(a.replaceAll(",", ""));
                        data.setSox(s);
                        sum += s;

                    }
                    else data.setSox(0);

                    check1 = checkInt(node.path("nox_mesure_value").asText());
                    if(check1){
                        String a = (node.path("nox_mesure_value").asText());
                        s = Double.parseDouble(a.replaceAll(",", ""));
                        data.setNox(s);
                        sum += s;

                    }
                    else data.setNox(0);

                    check1 = checkInt(node.path("hcl_mesure_value").asText());
                    if(check1){
                        String a = (node.path("hcl_mesure_value").asText());
                        s = Double.parseDouble(a.replaceAll(",", ""));
                        data.setHcl(s);
                        sum += s;

                    }
                    else data.setHcl(0);

                    check1 = checkInt(node.path("hf_mesure_value").asText());
                    if(check1){
                        String a = (node.path("hf_mesure_value").asText());
                        s = Double.parseDouble(a.replaceAll(",", ""));
                        data.setHf(s);
                        sum += s;

                    }
                    else data.setHf(0);

                    check1 = checkInt(node.path("nh3_mesure_value").asText());
                    if(check1){
                        String a = (node.path("nh3_mesure_value").asText());
                        s = Double.parseDouble(a.replaceAll(",", ""));
                        data.setNh3(s);
                        sum += s;

                    }
                    else data.setNh3(0);

                    check1 = checkInt(node.path("co_mesure_value").asText());
                    if(check1){
                        String a = (node.path("co_mesure_value").asText());
                        s = Double.parseDouble(a.replaceAll(",", ""));
                        //s = Double.parseDouble(node.path("co_mesure_value").asText());

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
        /*지역별로 배출되는 화학 물질의 양을 저장하고 있는 data2.json의 값을 SSDAY 데이터베이스로 옮기기*/
        try {
            String json = new String(Files.readAllBytes(Paths.get("data2.json")), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            List<SSDAY> dataList = new ArrayList<>();

            try {
                JsonNode rootNode = mapper.readTree(json);
                JsonNode items = rootNode.path("response").path("body").path("items");
                int i = 1;
                double s;
                for (JsonNode node : items) {

                    SSDAY data = new SSDAY();
                    data.setArea_nm(node.path("area_nm").asText());
                    data.setTSP(node.path("tsp_dscamt").asDouble());
                    data.setYear_1(node.path("examin_year").asText());
                    data.setSox(node.path("sox_dscamt").asDouble());
                    data.setNox(node.path("nox_dscamt").asDouble());
                    data.setHcl(node.path("hcl_dscamt").asDouble());
                    data.setHf(node.path("hf_dscamt").asDouble());
                    data.setNh3(node.path("nh3_dscamt").asDouble());
                    data.setCo(node.path("co_dscamt").asDouble());
                    data.setNumber(i);
                    i++;
                    dataList.add(data);
                }

                ssdayRepository.saveAll(dataList); // 데이터베이스에 저장
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String json = new String(Files.readAllBytes(Paths.get("data3.json")), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            List<SSDCY> dataList = new ArrayList<>();
            /*각 공장의 위치 정보를 SSDCY 데이터베이스에 저장*/
            try {
                JsonNode rootNode = mapper.readTree(json);
                JsonNode items = rootNode.path("response").path("body").path("items");
                int i = 1;
                double s;
                for (JsonNode node : items) {

                    SSDCY data = new SSDCY();
                    data.setName(node.path("fact_manage_nm").asText());
                    data.setAddress(node.path("fact_adres").asText());
                    dataList.add(data);
                }

                ssdcyRepository.saveAll(dataList); // 데이터베이스에 저장
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}