package com.example.calendarapp.event;

import com.example.calendarapp.event.update.AbstractAuditableEvent;
import com.example.calendarapp.exception.InvalidEventException;

import java.time.Duration;
import java.time.ZonedDateTime;

public abstract class AbstractEvent implements Event {
//  ----- Event 내의 구성 요소 -----
    private final int id;   // 고유 번호
    private String title;   // 타이틀

    private ZonedDateTime startAt;  // 시작 시간
    private ZonedDateTime endAt;    // 종료 시간
    private Duration duration;      // 기간

    private final ZonedDateTime createdAt;  // 생성일자
    private ZonedDateTime modifiedAt;       // 수정일자

    private boolean deletedYn;  // 삭제 유무 - soft-delete를 위한 속성

    // ----- AbstractEvent 생성자 -----
    protected AbstractEvent(int id, String title,
                            ZonedDateTime startAt, ZonedDateTime endAt) {

        if (startAt.isAfter(endAt)) {
            throw new InvalidEventException(
                    String.format("시작일은 종료일보다 이전이어야 합니다. 시작일=%s, 종료일=%s", startAt, endAt)
            );
        }

        this.id = id;
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.duration = Duration.between(startAt, endAt);

        ZonedDateTime now = ZonedDateTime.now();
        this.createdAt = now;
        this.modifiedAt = now;

        this.deletedYn = false;     // 삭제 유무 초기 데이터 - false
    }

    public void validateAndUpdate(AbstractAuditableEvent update) {
        // ----- validate -----
        // 업데이트 시 삭제 상태이면 수정을 못하도록 조건
        //------------------------------------
        if (deletedYn == true) {
            throw new RuntimeException("이미 삭제된 이벤트는 수정할 수 없음.");
        }

        // ----- update -----
        defaultUpdate(update);      // 공동적인 요소들을 업데이트 - title, startAt, endAt, duration, modified
        update(update);     // 그 외의 요소들을 업데이트
    }

    private void defaultUpdate(AbstractAuditableEvent update) {
        this.title = update.getTitle();
        this.startAt = update.getStartAt();
        this.endAt = update.getEndAt();
        this.duration = Duration.between(this.startAt, this.endAt);
        this.modifiedAt = ZonedDateTime.now();
    }

    protected abstract void update(AbstractAuditableEvent update);

    // 삭제
    public void delete(boolean deletedYn) {
        this.deletedYn = deletedYn;
    }

    public String getTitle() {
        return this.title;
    }

    public ZonedDateTime getStartAt() {
        return startAt;
    }

    public ZonedDateTime getEndAt() {
        return endAt;
    }
}
