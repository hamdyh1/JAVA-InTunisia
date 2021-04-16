package crud;

import java.util.Date;

public class Reservations {

    private Integer id;
    private String phoneNumber;
    private Integer adultCount;
    private Integer childrenCount;
    private Date date;
    private String time;
    private String status;

    public Reservations(Integer id, String phoneNumber, Integer adultCount, Integer childrenCount, Date date, String time, String status) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.adultCount = adultCount;
        this.childrenCount = childrenCount;
        this.date = date;
        this.time = time;
        this.status = status;
    }



    public Integer getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Integer getAdultCount() {
        return adultCount;
    }

    public Integer getChildrenCount() {
        return childrenCount;
    }

    public Date getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
