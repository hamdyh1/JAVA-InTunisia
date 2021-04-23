package javacrud;

import java.util.Date;

public class Reservations {

    private Integer id;
    private String phone_number;
    private Integer adult_number;
    private Integer children_number;
    private Date date;
    private String time;
    private String status;

    public Reservations(Integer id, String phoneNumber, Integer adultCount, Integer childrenCount, Date date, String time, String status) {
        this.id = id;
        this.phone_number = phoneNumber;
        this.adult_number = adultCount;
        this.children_number = childrenCount;
        this.date = date;
        this.time = time;
        if(status.length() > 0) {
            this.status = status;
        }
        else {
            this.status = "AC";
        }
    }

    public Integer getId() {
        return id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public Integer getAdult_number() {
        return adult_number;
    }

    public Integer getChildren_number() {
        return children_number;
    }

    public Date getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}
