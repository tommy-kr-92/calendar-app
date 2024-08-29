package com.example.calendarapp.reader;

import com.example.calendarapp.event.Meeting;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class EventCsvReader {
    public List<Meeting> readMeetings(String path) throws IOException {
        List<Meeting> result = new ArrayList<>();

        // 데이터 읽기
        List<String[]> read = readAll(path);

        for (int i = 0; i < read.size(); i++) {
            if (skipHeader(i)) {    // Head 부분을 스킵하고 반복문이 돌아야 함
                continue;
            }

            // 데이터 배열에 담기 - 1 줄
            String[] each = read.get(i);

            // Meeting 변환
            result.add(
                    // Meeting 객체를 보면서 해당되는 Header의 데이터들을 가져오기
                    new Meeting(
                            Integer.parseInt(each[0]),  // id
                            each[2],    // title
                            ZonedDateTime.of(       // startAt
                                    LocalDateTime.parse(
                                            each[6],
                                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                    ),
                                    ZoneId.of("Asia/Seoul")
                            ),
                            ZonedDateTime.of(       // endAt
                                    LocalDateTime.parse(
                                            each[7],
                                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                    ),
                                    ZoneId.of("Asia/Seoul")
                            ),
                            new HashSet<>(Arrays.asList(each[3].split(","))),       // participants
                            each[4],    // meetingRoom
                            each[5]     // agenda
                    )
            );
        }

        return result;
    }

    private static boolean skipHeader(int i) {
        return i == 0;
    }

    public List<String[]> readAll(String path) throws IOException {
        // 파일 경로 입력 - resource 내의 파일 경로
        InputStream in = getClass().getResourceAsStream(path);

        // 파일 읽기 위한 InputStreamReader 준비, charset -> UTF_8
        InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);

        // open-csv 를 통한 csv 읽기
        CSVReader csvReader = new CSVReader(reader);

        // csv 파일을 모두 읽은 결과를 return
        return csvReader.readAll();
    }
}
