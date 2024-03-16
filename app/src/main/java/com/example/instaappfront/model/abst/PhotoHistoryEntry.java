package com.example.instaappfront.model.abst;

public class PhotoHistoryEntry {
    private String status;
    private Long timestamp;
    private String url;

    public PhotoHistoryEntry() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PhotoHistoryEntry{" +
                "status='" + status + '\'' +
                ", timestamp=" + timestamp +
                ", url='" + url + '\'' +
                '}';
    }
}
