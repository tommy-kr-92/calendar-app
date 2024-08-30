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

    private final RawCsvReader rawCsvReader;

    public EventCsvReader(RawCsvReader rawCsvReader) {
        this.rawCsvReader = rawCsvReader;
    }


    public List<Meeting> readMeetings(String path) throws IOException {
        List<Meeting> result = new ArrayList<>();

        // 데이터 읽기
        List<String[]> read = rawCsvReader.readAll(path);

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

        // Meeting 반환
        return result;
    }

    private static boolean skipHeader(int i) {
        return i == 0;
    }


}
