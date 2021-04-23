package javacrud;

import java.util.Date;

public class Review {
    private Integer id;
    private String user_name;
    private Integer rate;
    private Date date;
    private String description;
    private Byte feature;

    public Review(Integer id, String username, Integer rate, Date date, String description, Byte feature) {
        this.id = id;
        this.user_name = username;
        this.rate = rate;
        this.date = date;
        this.description = description;
        this.feature = feature;
    }

    public Integer getId() {
        return id;
    }

    public String getUser_name() {
        return user_name;
    }

    public Integer getRate() {
        return rate;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Byte getFeature() {
        return feature;
    }
}
