package com.as.hometutorink;

class Children {

    public String childname;
    public String edulevel;
    public String level;
    public String childID;



    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Children() {
    }

    public Children(String childname, String edulevel, String level, String childID) {
        this.childname = childname;
        this.edulevel = edulevel;
        this.level = level;
        this.childID = childID;
    }

    public String getChildname(){
        return childname;
    }

    public void setChildname(String childname) {
        this.childname = childname;
    }

    public String getEdulevel() {
        return edulevel;
    }

    public void setEdulevel(String edulevel) {
        this.edulevel = edulevel;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getChildID() {
        return childID;
    }

    public void setChildID(String childID) {
        this.childID = childID;
    }



}
