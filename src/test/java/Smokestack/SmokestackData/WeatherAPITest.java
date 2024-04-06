package Smokestack.SmokestackData;

import java.net.*;
import java.io.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WeatherAPITest {

    public static void main(String[] args) {
        try {
            // 요청 문자열을 만들기
            String tm = "202404061900"; // 날짜
            StringBuilder strUrl = new StringBuilder("https://apihub.kma.go.kr/api/typ01/url/kma_sfctm2.php?");
            strUrl.append("tm=");
            strUrl.append(tm);
            strUrl.append("&stn=0");    // 위치
            strUrl.append("&help=0");   // 도움말 제거
            strUrl.append("&authKey=7MBK14sBR8iASteLAafIPg");
            // API URL을 만듭니다.
            URL url = new URL(strUrl.toString());
            // HttpURLConnection 객체를 만들어 API를 호출합니다.
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // 요청 방식을 GET으로 설정합니다.
            con.setRequestMethod("GET");
            // 요청 헤더를 설정합니다. 여기서는 Content-Type을 application/json으로 설정합니다.
            con.setRequestProperty("Content-Type", "application/json");

            // API의 응답을 읽기 위한 BufferedReader를 생성합니다.
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            // 응답을 한 줄씩 읽어들이면서 StringBuffer에 추가합니다.
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            // BufferedReader를 닫습니다.
            in.close();
            // 문자열에서 칼럼별 데이터를 추출하고 json으로 변환합니다.
            // 0~796 번째 바이트까지 지우기
            response.delete(0,796);
            // 공백으로 모든 요소를 구분합니다.
            String[] res = response.toString().split("\\s+");
            // 한국표준시 : 0번째, 위치 : 1번째, 풍향 : 2번째, 풍속 : 3번째, 기온 : 11번째
            int count = 0;
            int lastIdx = 0;
            int[] statArr = {0,1,2,3,11,46};
            String[] statStr = {"KST","STN", "WD", "WS", "TA"};
            // Json 변수
            JSONObject obj = new JSONObject();
            List<JSONObject> objList = new ArrayList<>();
            int statIdx = 0;
            for(var col : res) {
                if(count-lastIdx == 44) {
                    // 한 열을 다 순회했으므로 순회자 초기화 및 json 객체를 리스트에 넣기.
                    objList.add(obj);
                    lastIdx=count;
                    statIdx=0;
                }
                else if(count - lastIdx == statArr[statIdx]) {
                    // 0번째인 경우 한국 표준시 데이터이므로 날짜 데이터 변환이 필요하다.
                    if(statIdx == 0) {
                        StringBuilder date = new StringBuilder(col);
                        if(date.length() > 12) {
                            date.delete(0,date.length()-12);
                        }
                        // 문자열을 날짜 데이터로 변환하기
                        date.append("00");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
                        Timestamp timestamp = Timestamp.valueOf(dateTime);
                        obj.put(statStr[statIdx], timestamp);
                        //System.out.print(statArr[statIdx] + " : " + timestamp + "   ");
                    }
                    // 정수 변환
                    else if(statIdx == 1 || statIdx == 2) {
                        obj.put(statStr[statIdx], Integer.parseInt(col));
                    }
                    // 실수 변환
                    else if(statIdx == 3 || statIdx == 4) {
                        obj.put(statStr[statIdx], Double.parseDouble(col));
                    }
                    statIdx++;
                }
                count++;
            }
            // List<JSONObject>를 JSONArray로 변환
            JSONArray objArr = new JSONArray(objList);
            String filePath = "weatherData.json";
            // JSON 파일에 데이터 쓰기
            try (FileWriter fileWriter = new FileWriter(filePath)) {
                fileWriter.write(objList.toString());
                System.out.println("Data saved to JSON file: " + filePath);
            } catch (IOException e) {
                System.out.println("Error writing to JSON file: " + e.getMessage());
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }
}