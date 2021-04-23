package javacrud;

public class Listing {
    private Integer id;
    private String name;
    private Integer user_id;
    private String status;
    private Integer visit_number;
    private String email;
    private String phone;

    public Listing(Integer id, String name, Integer user_id, String status, Integer visit_number, String email, String phone) {
        this.id = id;
        this.name = name;
        this.user_id = user_id;
        this.status = status;
        this.visit_number = visit_number;
        this.email = email;
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getStatus() {
        return status;
    }

    public Integer getVisit_number() {
        return visit_number;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
