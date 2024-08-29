package com.example.calendarapp.event;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
//  Abstract Class인 AbstractEvent List 할당
    private List<AbstractEvent> events = new ArrayList<>();

    public void add(AbstractEvent event) {
        // 날짜 입력이 정상적이면 그대로 진행
        if (hasScheduleConflictWith(event)) {
            return;
        }
        this.events.add(event);     // event 추가
    }

    // 모든 항목 출력
    public void printAll() {
        events.forEach(Event::print);       // 모든 요소들을 돌며 print 함수 실행
    }

    // 타입에 따라 출력
    public void printBy(EventType type) {
        events.stream()
                .filter(event -> event.support(type))   // 타입 filter 후
                .forEach(Event::print);     // filter한 타입의 Event를 출력
    }

    private boolean hasScheduleConflictWith(AbstractEvent event) {
        return events.stream()
                .anyMatch(each ->
                        (event.getStartAt().isBefore(each.getEndAt()) && event.getStartAt().isAfter(each.getStartAt()))
                                || (event.getEndAt().isAfter(each.getStartAt())) && event.getEndAt().isBefore(each.getEndAt()));
    }
}
