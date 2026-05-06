package com.p2.data;

import java.time.LocalDateTime;

public class LogEntry {

    private String ip;
    private String userId;
    private LocalDateTime dateTime;
    private String method;
    private String resource;
    private int statusCode;
    private long responseSize;
    private String referer;
    private String userAgent;

    public LogEntry(String ip, String userId, LocalDateTime dateTime,
                    String method, String resource, int statusCode,
                    long responseSize, String referer, String userAgent) {
        this.ip = ip;
        this.userId = userId;
        this.dateTime = dateTime;
        this.method = method;
        this.resource = resource;
        this.statusCode = statusCode;
        this.responseSize = responseSize;
        this.referer = referer;
        this.userAgent = userAgent;
    }

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode <= 299;
    }

    public boolean isNotFound() {
        return statusCode >= 400 && statusCode <= 499;
    }

    public String getIp() { return ip; }
    public String getUserId() { return userId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getMethod() { return method; }
    public String getResource() { return resource; }
    public int getStatusCode() { return statusCode; }
    public long getResponseSize() { return responseSize; }
    public String getReferer() { return referer; }
    public String getUserAgent() { return userAgent; }
}
