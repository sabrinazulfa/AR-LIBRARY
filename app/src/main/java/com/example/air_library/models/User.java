package com.airlibrary.models;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private String id;
    private String email;
    private String displayName;
    private String profileImageUrl;
    private Date registrationDate;
    private int uploadedDocumentsCount;
    private boolean isPremiumUser;

    // Konstruktor
    public User() {}

    public User(String email, String displayName) {
        this.email = email;
        this.displayName = displayName;
        this.registrationDate = new Date();
        this.uploadedDocumentsCount = 0;
        this.isPremiumUser = false;
    }

    // Getter dan Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public int getUploadedDocumentsCount() {
        return uploadedDocumentsCount;
    }

    public void setUploadedDocumentsCount(int uploadedDocumentsCount) {
        this.uploadedDocumentsCount = uploadedDocumentsCount;
    }

    public boolean isPremiumUser() {
        return isPremiumUser;
    }

    public void setPremiumUser(boolean premiumUser) {
        isPremiumUser = premiumUser;
    }
}