package com.sugang.coursepage.exception;

//같은 강사의 같은 날짜·시간에 이미 수업이 잡혀 있을 때
public class DuplicateBookingException extends RuntimeException {
    public DuplicateBookingException(String message, Throwable cause) {
        super(message, cause);
    }
}
