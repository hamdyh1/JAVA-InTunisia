package crud;

import java.util.Date;

public class Review {
    private Integer id;
    private String username;
    private Integer rate;
    private Date date;
    private String description;
    private Byte feature;

    public Review(Integer id, String username, Integer rate, Date date, String description, Byte feature) {
        this.id = id;
        this.username = username;
        this.rate = rate;
        this.date = date;
        this.description = description;
        this.feature = feature;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
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
}
