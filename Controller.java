package crud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField tf_reservation_id;

    @FXML
    private TextField tf_reservation_phone;

    @FXML
    private TextField tf_reservation_date;

    @FXML
    private TextField tf_reservation_time;

    @FXML
    private TextField tf_reservation_ac;

    @FXML
    private TextField tf_reservation_cc;

    @FXML
    private TextField tf_review_id;

    @FXML
    private TextField tf_review_user;

    @FXML
    private TextField tf_review_rate;

    @FXML
    private TextField tf_review_desc;

    @FXML
    private TextField tf_review_date;

    @FXML
    private TableView<Reservations> tv_reservation;

    @FXML
    private TableColumn<Reservations, Integer> col_reservation_id;

    @FXML
    private TableColumn<Reservations, String> col_reservation_phone;

    @FXML
    private TableColumn<Reservations, Date> col_reservation_date;

    @FXML
    private TableColumn<Reservations, String> col_reservation_time;

    @FXML
    private TableColumn<Reservations, Integer> col_reservation_ac;

    @FXML
    private TableColumn<Reservations, Integer> col_reservation_cc;

    @FXML
    private TableColumn<Reservations, String> col_reservation_status;

    @FXML
    private TableView<Review> tv_review;

    @FXML
    private TableColumn<Review, Integer> col_review_id;

    @FXML
    private TableColumn<Review, String> col_review_user;

    @FXML
    private TableColumn<Review, Integer> col_review_rate;

    @FXML
    private TableColumn<Review, String> col_review_desc;

    @FXML
    private TableColumn<Review, Date> col_review_date;

    @FXML
    private TableColumn<Review, Byte> col_review_feat;

    @FXML
    private Button btn_reservation_ins;

    @FXML
    private Button btn_reservation_del;

    @FXML
    private Button btn_reservation_upd;

    @FXML
    private Button btn_review_ins;

    @FXML
    private Button btn_review_del;

    @FXML
    private Button btn_review_upd;

    @FXML
    void handleButtonAction(ActionEvent event) {
        if(event.getSource() == btn_reservation_ins){
            insertReservationRecord();
        }
        if(event.getSource() == btn_reservation_del) {
            deleteReservationRecord();
        }
        if(event.getSource() == btn_reservation_upd) {
            updateReservationRecord();
        }

        if(event.getSource() == btn_review_ins){
            insertReviewRecord();
        }
        if(event.getSource() == btn_review_del) {
            deleteReviewRecord();
        }
        if(event.getSource() == btn_review_upd) {
            updateReviewRecord();
        }
    }

    public Connection getConnection() {
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo","phpmyadmin","demo");
            return conn;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showReservations();
        showReviews();
    }

    public ObservableList<Reservations> getReservationsList(){
        ObservableList<Reservations> reservationsList = FXCollections.observableArrayList();
        Connection connection = getConnection();
        String query = "SELECT * FROM reservations ";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            Reservations reservation;
            while(rs.next()) {
                reservation = new Reservations(rs.getInt("id"),rs.getString("phone_number"), rs.getInt("adult_number"), rs.getInt("children_number"), rs.getDate("date"), rs.getString("time"), rs.getString("status"));
                reservationsList.add(reservation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservationsList;
    }

    public void showReservations() {
        ObservableList<Reservations> list = getReservationsList();

        col_reservation_id.setCellValueFactory(new PropertyValueFactory<Reservations,Integer>("id"));
        col_reservation_phone.setCellValueFactory(new PropertyValueFactory<Reservations,String>("phone_number"));
        col_reservation_cc.setCellValueFactory(new PropertyValueFactory<Reservations,Integer>("children_number"));
        col_reservation_ac.setCellValueFactory(new PropertyValueFactory<Reservations,Integer>("adult_number"));
        col_reservation_status.setCellValueFactory(new PropertyValueFactory<Reservations,String>("status"));
        col_reservation_date.setCellValueFactory(new PropertyValueFactory<Reservations,Date>("date"));
        col_reservation_time.setCellValueFactory(new PropertyValueFactory<Reservations,String>("time"));

        tv_reservation.setItems(list);
    }

    public ObservableList<Review> getReviewList(){
        ObservableList<Review> reviewList = FXCollections.observableArrayList();
        Connection connection = getConnection();
        String query = "SELECT * FROM reviews ";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            Review review;
            while(rs.next()) {
                review = new Review(rs.getInt("id"),rs.getString("user_name"), rs.getInt("rate"), rs.getDate("date"), rs.getString("description"), rs.getByte("feature"));
                reviewList.add(review);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reviewList;
    }

    public void showReviews() {
        ObservableList<Review> list = getReviewList();

        col_review_id.setCellValueFactory(new PropertyValueFactory<Review,Integer>("id"));
        col_review_user.setCellValueFactory(new PropertyValueFactory<Review,String>("user_name"));
        col_review_rate.setCellValueFactory(new PropertyValueFactory<Review,Integer>("rate"));
        col_review_date.setCellValueFactory(new PropertyValueFactory<Review,Date>("date"));
        col_review_desc.setCellValueFactory(new PropertyValueFactory<Review,String>("description"));
        col_review_feat.setCellValueFactory(new PropertyValueFactory<Review,Byte>("feature"));

        tv_review.setItems(list);
    }

    private void insertReviewRecord() {
        String query = "INSERT INTO reservations VALUES (" + tf_review_id.getText() + ",'" + tf_review_desc.getText() + "','" + tf_review_user.getText() + "'," + tf_review_rate.getText() + ",'" + tf_review_date.getText() + "', 1);";
        if(tf_reservation_id.getText().length() <= 0) {
            return;
        }
        executeQuery(query);
        showReviews();
    }

    private void updateReviewRecord() {
        String query = "UPDATE reviews SET description= '" + tf_review_desc.getText() + "', user_name='" + tf_review_user.getText() + "', rate=" + tf_review_rate.getText() + ", date='" + tf_review_date.getText() + "' feature= 1 ' WHERE id = " + tf_reservation_id.getText() + ";";
        executeQuery(query);
        showReviews();
    }

    private void deleteReviewRecord() {
        String query = "DELETE FROM reviews WHERE ID = " + tf_reservation_id.getText() + ";";
        showReviews();
    }


    private void insertReservationRecord() {
        String query = "INSERT INTO reservations VALUES (" + tf_reservation_id.getText() + ",'YES'," + tf_reservation_ac.getText() + ",'" + tf_reservation_time.getText() + "','" + tf_reservation_date.getText() + "'," + tf_reservation_cc.getText() + ",'" + tf_reservation_phone.getText() + "');";
        if(tf_reservation_id.getText().length() <= 0) {
            return;
        }
        executeQuery(query);
        showReservations();
    }

    private void updateReservationRecord() {
        String query = "UPDATE reservations SET adult_number= " + tf_reservation_ac.getText() + ", time='" + tf_reservation_time.getText() + "', date='" + tf_reservation_date.getText() + "', children_number=" + tf_reservation_cc + ", phone_number='" + tf_reservation_phone + "' WHERE id = " + tf_reservation_id.getText() + ";";
        executeQuery(query);
        showReservations();
    }

    private void deleteReservationRecord() {
        String query = "DELETE FROM reservations WHERE ID = " + tf_reservation_id.getText() + ";";
        showReservations();
    }

}
