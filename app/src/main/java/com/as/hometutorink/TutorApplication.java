package com.as.hometutorink;

class TutorApplication {

    public String tutorKey;
    public String appStatus;

    public TutorApplication(){

    }

    public TutorApplication(String tutorKey, String appStatus) {
        this.tutorKey = tutorKey;
        this.appStatus = appStatus;
    }

    public String getTutorKey() {
        return tutorKey;
    }

    public void setTutorKey(String tutorKey) {
        this.tutorKey = tutorKey;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }
}
