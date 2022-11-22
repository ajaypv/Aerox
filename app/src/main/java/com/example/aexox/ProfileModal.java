package com.example.aexox;

public class ProfileModal {
    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getProfilename() {
        return profilename;
    }

    public void setProfilename(String profilename) {
        this.profilename = profilename;
    }

    String profilepic;

    public ProfileModal(String profilepic, String profilename) {
        this.profilepic = profilepic;
        this.profilename = profilename;
    }

    String profilename;
}
