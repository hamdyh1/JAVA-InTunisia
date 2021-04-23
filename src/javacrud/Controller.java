package javacrud;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    @FXML
    private AnchorPane mainPane;

    @FXML
    private Button listing;

    @FXML
    private Button reservation;

    @FXML
    private Button review;

    @FXML
    private Button stats;

    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {
        if(event.getSource() == reservation) {
            Parent reservation = FXMLLoader.load(getClass().getResource("reservations.fxml"));
            Scene reservationScene = new Scene(reservation);

            // Get the stage information and change it
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(reservationScene);
            window.show();
        }

        if(event.getSource() == review) {
            Parent review = FXMLLoader.load(getClass().getResource("reviews.fxml"));
            Scene reviewScene = new Scene(review);

            // Get the stage information and change it
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(reviewScene);
            window.show();
        }

        if(event.getSource() == stats) {
            Parent stats = FXMLLoader.load(getClass().getResource("statistics.fxml"));
            Scene statsScene = new Scene(stats);

            // Get the stage information
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(statsScene);
            window.show();
        }

        if(event.getSource() == listing) {
            Parent stats = FXMLLoader.load(getClass().getResource("listings.fxml"));
            Scene statsScene = new Scene(stats);

            // Get the stage information
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(statsScene);
            window.show();
        }
    }

}
