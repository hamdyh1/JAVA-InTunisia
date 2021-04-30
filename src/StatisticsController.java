import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable {

    @FXML
    private TextField review_count;

    @FXML
    private TextField reservation_count;

    @FXML
    private TextField listing_count;

    @FXML
    private TextField satisfaction_rating;

    @FXML
    private Button back;

    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {
        if(event.getSource() == back) {
            Parent reservation = FXMLLoader.load(getClass().getResource("main.fxml"));
            Scene reservationScene = new Scene(reservation);

            // Get the stage information and change it
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(reservationScene);
            window.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            showStatistics();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

    public void showStatistics() throws SQLException {
        int countReview = 0;
        int countReservation = 0;
        int countListing = 0;
        int ratings = 0;

        String reviewQuery = "SELECT COUNT(*) AS revcount FROM reviews";
        String reservationQuery = "SELECT COUNT(*) AS rescount FROM reservations";
        String listingQuery = "SELECT COUNT(*) AS listcount FROM listing";
        String ratingQuery = "SELECT * FROM reviews";

        // Set SQL variables
        Connection connection = getConnection();
        Statement st;
        ResultSet rs;

        // Fetch the values
        try {
            st = connection.createStatement();
            rs = st.executeQuery(reviewQuery);
            rs.next();
            countReview = rs.getInt("revcount");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            st = connection.createStatement();
            rs = st.executeQuery(reservationQuery);
            rs.next();
            countReservation = rs.getInt("rescount");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            st = connection.createStatement();
            rs = st.executeQuery(ratingQuery);
            while(rs.next()) {
                ratings += rs.getInt("rate");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        try {
            st = connection.createStatement();
            rs = st.executeQuery(ratingQuery);
            while(rs.next()) {
                countListing = rs.getInt("rate");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        // Update values
        float satisfaction_rate = (float) ratings / (float) countReview;
        review_count.setText(String.valueOf(countReview));
        reservation_count.setText(String.valueOf(countReservation));
        satisfaction_rating.setText(String.valueOf(satisfaction_rate));
        listing_count.setText(String.valueOf(countListing));
    }
}
