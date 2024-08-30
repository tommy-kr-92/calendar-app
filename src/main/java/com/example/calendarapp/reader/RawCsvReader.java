package com.example.calendarapp.reader;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

//moking을 만들어주기 위한 Class
public class RawCsvReader {
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
