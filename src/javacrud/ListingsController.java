package javacrud;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.ResourceBundle;

public class ListingsController implements Initializable {

    @FXML
    private Label listing_id;

    @FXML
    private Label listing_name;

    @FXML
    private Label listing_user;

    @FXML
    private Label listing_status;

    @FXML
    private Label listing_visit;

    @FXML
    private Label listing_email;

    @FXML
    private Label listing_phone;

    @FXML
    private TextField tf_listing_id;

    @FXML
    private TextField tf_listing_name;

    @FXML
    private TextField tf_listing_user;

    @FXML
    private TextField tf_listing_status;

    @FXML
    private TextField tf_listing_visit;

    @FXML
    private TextField tf_listing_email;

    @FXML
    private TextField tf_listing_phone;

    @FXML
    private TableView<Listing> tv_listing;

    @FXML
    private TableColumn<Listing, Integer> col_listing_id;

    @FXML
    private TableColumn<Listing, String> col_listing_name;

    @FXML
    private TableColumn<Listing, Integer> col_listing_user;

    @FXML
    private TableColumn<Listing, String> col_listing_status;

    @FXML
    private TableColumn<Listing, Integer> col_listing_visits;

    @FXML
    private TableColumn<Listing, String> col_listing_email;

    @FXML
    private TableColumn<Listing, String> col_listing_phone;

    @FXML
    private Button btn_listing_ins;

    @FXML
    private Button btn_listing_del;

    @FXML
    private Button btn_listing_upd;

    @FXML
    private Button back;

    @FXML
    private Button search;

    @FXML
    private TextField filterField;

    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {
        if(event.getSource() == btn_listing_ins){
            insertListingRecord();
        }
        if(event.getSource() == btn_listing_del) {
            deleteListingRecord();
        }
        if(event.getSource() == btn_listing_upd) {
            updateListingRecord();
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
            ObservableList<Listing> list = getListingList();
            FilteredList<Listing> filteredData = new FilteredList<Listing>(list);

            filteredData.setPredicate(Listing -> {
                if (filterField.getText() == null || filterField.getText().isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = filterField.getText().toLowerCase();

                if (String.valueOf(Listing.getId()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                    // Filter matches id.

                } else if (String.valueOf(Listing.getEmail()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches description.
                } else if (String.valueOf(Listing.getPhone()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter phone
                } else if (String.valueOf(Listing.getStatus()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter status
                } else if (String.valueOf(Listing.getUser_id()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter user id
                } else if (String.valueOf(Listing.getName()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter name
                }
                return false; // Does not match.
            });

            SortedList<Listing> sortedData = new SortedList<>(filteredData);

            sortedData.comparatorProperty().bind(tv_listing.comparatorProperty());
            tv_listing.setItems(sortedData);
        }
    }

    public Connection getConnection() {
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo?useLegacyDatetimeCode=false&serverTimezone=UTC","admin","password");
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
        showListings();
    }

    public ObservableList<Listing> getListingList(){
        ObservableList<Listing> listingsList = FXCollections.observableArrayList();
        Connection connection = getConnection();
        String query = "SELECT * FROM listing ";
        Statement st;
        ResultSet rs;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            Listing listings;
            while(rs.next()) {
                listings = new Listing(rs.getInt("id"), rs.getString("name"),
                                    rs.getInt("user_id"), rs.getString("status"),
                        rs.getInt("visit_number"), rs.getString("email"), rs.getString("phone"));
                listingsList.add(listings);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listingsList;
    }

    public void showListings() {
        ObservableList<Listing> list = getListingList();

        col_listing_id.setCellValueFactory(new PropertyValueFactory<Listing,Integer>("id"));
        col_listing_name.setCellValueFactory(new PropertyValueFactory<Listing,String>("name"));
        col_listing_user.setCellValueFactory(new PropertyValueFactory<Listing,Integer>("user_id"));
        col_listing_status.setCellValueFactory(new PropertyValueFactory<Listing, String>("status"));
        col_listing_visits.setCellValueFactory(new PropertyValueFactory<Listing,Integer>("visit_number"));
        col_listing_email.setCellValueFactory(new PropertyValueFactory<Listing,String>("email"));
        col_listing_phone.setCellValueFactory(new PropertyValueFactory<Listing,String>("phone"));

        tv_listing.setItems(list);
    }

    private void insertListingRecord() {
        String query = "INSERT INTO listing VALUES (" + tf_listing_id.getText() + ",'" + tf_listing_name.getText() +
                "'," + tf_listing_user.getText() + ",'" + tf_listing_status.getText() + "'," + tf_listing_visit.getText() +
                ", '" + tf_listing_email.getText() + "', '" + tf_listing_phone.getText() + "');";
        executeQuery(query);
        showListings();
    }

    private void updateListingRecord() {
        String query = "UPDATE listing SET name='" + tf_listing_name.getText() + "', user_id=" +
                tf_listing_user.getText() + ", status='" + tf_listing_status.getText() + "', visit_number=" + tf_listing_visit.getText()
                + ", email='" + tf_listing_email.getText() + "', phone='" + tf_listing_phone.getText()  + "' WHERE id =" + tf_listing_id.getText() + ";";
        executeQuery(query);
        showListings();
    }

    private void deleteListingRecord() {
        String query = "DELETE FROM listing WHERE id = " + tf_listing_id.getText() + ";";
        executeQuery(query);
        showListings();
    }

}
