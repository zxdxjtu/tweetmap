package es;

import io.searchbox.annotations.JestId;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {
    private static final long serialVersionUID = -3971912226293959387L;

    @JestId
    private String keyword;
    private String username;
    private String profile;
    private double longitude;
    private double latitude;
    private long id;
    private String content;

    private String note;

    private Date createdOn;


    public Note(final String keyword, final String username, final String profile, final double longitude, final double latitude, final long id, final String content) {
        this.keyword = keyword;
    	this.username = username;
        this.profile = profile;
        this.longitude = longitude;
        this.latitude = latitude;
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getprofile() {
        return username;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    

//    @Override
//    public String toString() {
//        return "Note [userName= " + username + ",\n profileLocation=" + profile + ",\n geoLocation= " + geo
//                + ",\n id= " + id + ",\n Content: " + content +"]";
//    }
}