package com.example.calendarapp;

import com.example.calendarapp.event.*;
import com.example.calendarapp.event.update.UpdateMeeting;
import com.example.calendarapp.reader.EventCsvReader;
import com.example.calendarapp.reader.RawCsvReader;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

@SpringBootApplication
public class CalendarAppApplication {

    public static void main(String[] args) throws IOException {
        // 일정 생성
        Schedule schedule = new Schedule();

        // csv의 데이터를 통해 대량의 데이터들을 불러오는 소스
        EventCsvReader csvReader = new EventCsvReader(new RawCsvReader());
        String meetingCsvPath = "/data/meeting.csv";

        // csv 파일을 읽고 schedule 객체에 추가
        List<Meeting> meetings = csvReader.readMeetings(meetingCsvPath);    // csv 파일 읽기
        meetings.forEach(schedule::add);    // schedule 객체에 추가

        Meeting meeting = meetings.get(0);  // 제일 처음 데이터를 가져오기
        meeting.print();    // 출력

        System.out.println("수정 후 ... ");
        // 삭제된 데이터인지 확인 후 수정
        meetings.get(0).validateAndUpdate(
                new UpdateMeeting(
                        "new title",
                        ZonedDateTime.now(),
                        ZonedDateTime.now().plusHours(1),
                        null,
                        "A",
                        "new agenda"
                )
        );
        meeting.print();

        // 삭제
        meeting.delete(true);
        System.out.println("삭제 후 수정 시도 ... ");
        // 삭제 유무 확인 후 수정
        meetings.get(0).validateAndUpdate(
                new UpdateMeeting(
                        "new title 2",
                        ZonedDateTime.now(),
                        ZonedDateTime.now().plusHours(1),
                        null,
                        "B",
                        "new agenda 2"
                )
        );
        // 삭제 상태라서 Error 풀력
        meeting.print();
    }

}
