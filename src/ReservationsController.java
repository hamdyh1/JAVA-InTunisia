import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.ResourceBundle;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class ReservationsController implements Initializable {
    @FXML
    private TextField tf_reservation_id;

    @FXML
    private TextField tf_reservation_phone;

    @FXML
    private DatePicker tf_reservation_date;

    @FXML
    private TextField tf_reservation_time;

    @FXML
    private TextField tf_reservation_ac;

    @FXML
    private TextField tf_reservation_cc;

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
    private Button btn_reservation_ins;

    @FXML
    private Button btn_reservation_del;

    @FXML
    private Button btn_reservation_upd;

    @FXML
    private Button back;

    @FXML
    private TextField filterField;

    @FXML
    private Button search;

    // Observable list to store data
    private final ObservableList<Reservations> dataList = FXCollections.observableArrayList();

    public void handleButtonAction(ActionEvent event) throws IOException {
        if(event.getSource() == btn_reservation_ins){
            insertReservationRecord();
        }
        if(event.getSource() == btn_reservation_del) {
            deleteReservationRecord();
        }
        if(event.getSource() == btn_reservation_upd) {
            updateReservationRecord();
        }
        if(event.getSource() == back) {
            Parent reservation = FXMLLoader.load(getClass().getResource("main.fxml"));
            Scene reservationScene = new Scene(reservation);

            // Get the stage information and change it
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(reservationScene);
            window.show();
        }
        if(event.getSource() == search) {
            ObservableList<Reservations> list = getReservationsList();
            FilteredList<Reservations> filteredData = new FilteredList<Reservations>(list);

            filteredData.setPredicate(Reservations -> {
                if (filterField.getText() == null || filterField.getText().isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = filterField.getText().toLowerCase();

                if (String.valueOf(Reservations.getId()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches id.

                } else if (String.valueOf(Reservations.getPhone_number()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches phone number.
                } else if (String.valueOf(Reservations.getDate()).toLowerCase().contains(lowerCaseFilter)){
                    return true; // Filter matches date
                }
                return false; // Does not match.
            });

            SortedList<Reservations> sortedData = new SortedList<>(filteredData);

            sortedData.comparatorProperty().bind(tv_reservation.comparatorProperty());
            tv_reservation.setItems(sortedData);
        }
    }

    public Connection getConnection() {
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo","root","");
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
                reservation = new Reservations(rs.getInt("id"),rs.getString("phone_number"),
                        rs.getInt("adult_number"), rs.getInt("children_number"),
                        rs.getDate("date"), rs.getString("time"), rs.getString("status"));
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

    private void insertReservationRecord() {
        String query = "INSERT INTO reservations VALUES (" + tf_reservation_id.getText() + ",'AC'," +
                tf_reservation_ac.getText() + ",'" + tf_reservation_time.getText() + "','" +
                tf_reservation_date.getValue() + "'," + tf_reservation_cc.getText() + ",'" +
                tf_reservation_phone.getText() + "');";
        if(tf_reservation_id.getText().length() <= 0) {
            return;
        }
        executeQuery(query);
        sendSms();
        showReservations();
    }

    private void updateReservationRecord() {
        String query = "UPDATE reservations SET adult_number= " + tf_reservation_ac.getText() + ", time='" +
                tf_reservation_time.getText() + "', date='" + tf_reservation_date.getValue() + "', children_number=" +
                tf_reservation_cc.getText() + ", phone_number='" + tf_reservation_phone.getText() + "', status='AC' WHERE id = " +
                tf_reservation_id.getText() + ";";
        executeQuery(query);
        showReservations();
    }

    private void deleteReservationRecord() {
        String query = "DELETE FROM reservations WHERE id = " + tf_reservation_id.getText() + ";";
        executeQuery(query);
        showReservations();
    }
    
    private void sendSms(){
        final String ACCOUNT_SID = System.getenv("AC8c40321dee16c7ccabe95615e72bc2f5");
        final String AUTH_TOKEN = System.getenv("2dae3c17bfb17b21e08eb2796d118cc0");
        
         Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+15558675310"),
                new com.twilio.type.PhoneNumber("+15017122661"),
                "Your reservation can be confirmed by the system!")
            .create();

        System.out.println(message.getSid());
    }
}
