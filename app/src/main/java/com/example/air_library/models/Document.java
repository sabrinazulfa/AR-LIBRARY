package com.airlibrary.models;

import java.io.Serializable;
import java.util.Date;

public class Document implements Serializable {
    private String id;
    private String title;
    private String fileUrl;
    private String description;
    private Date uploadDate;
    private String uploadedBy;
    private long fileSize;
    private String fileType;

    // Konstruktor
    public Document() {}

    public Document(String title, String fileUrl, String description) {
        this.title = title;
        this.fileUrl = fileUrl;
        this.description = description;
        this.uploadDate = new Date();
    }

    // Getter dan Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}