package com.jornada.jobapi.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorDTO {

    private Date timestamp;
    private int statuscode;
    private String message;

    public ErrorDTO(Date timestamp, int statuscode, String message) {
        this.timestamp = timestamp;
        this.statuscode = statuscode;
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

