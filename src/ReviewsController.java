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

public class ReviewsController implements Initializable {
    @FXML
    private TextField tf_review_id;

    @FXML
    private TextField tf_review_user;

    @FXML
    private TextField tf_review_rate;

    @FXML
    private TextField tf_review_desc;

    @FXML
    private DatePicker tf_review_date;

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
    private Button btn_review_ins;

    @FXML
    private Button btn_review_del;

    @FXML
    private Button btn_review_upd;

    @FXML
    private Button back;

    @FXML
    private Button search;

    @FXML
    private TextField filterField;

    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {


        if(event.getSource() == btn_review_ins){
            insertReviewRecord();
        }
        if(event.getSource() == btn_review_del) {
            deleteReviewRecord();
        }
        if(event.getSource() == btn_review_upd) {
            updateReviewRecord();
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
            ObservableList<Review> list = getReviewList();
            FilteredList<Review> filteredData = new FilteredList<Review>(list);

            filteredData.setPredicate(Review -> {
                if (filterField.getText() == null || filterField.getText().isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = filterField.getText().toLowerCase();

                if (String.valueOf(Review.getId()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                    // Filter matches id.

                } else if (String.valueOf(Review.getDescription()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches description.
                } else if (String.valueOf(Review.getDate()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches date
                }
                return false; // Does not match.
            });

            SortedList<Review> sortedData = new SortedList<>(filteredData);

            sortedData.comparatorProperty().bind(tv_review.comparatorProperty());
            tv_review.setItems(sortedData);
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
        showReviews();
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
                review = new Review(rs.getInt("id"),rs.getString("user_name"), rs.getInt("rate"),
                        rs.getDate("date"), rs.getString("description"), rs.getByte("feature"));
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
        String query = "INSERT INTO reviews VALUES (" + tf_review_id.getText() + ",'" + tf_review_desc.getText() +
                "','" + tf_review_user.getText() + "'," + tf_review_rate.getText() + ",'" + tf_review_date.getValue() +
                "', 1);";
        executeQuery(query);
        showReviews();
    }

    private void updateReviewRecord() {
        String query = "UPDATE reviews SET description='" + tf_review_desc.getText() + "', user_name='" +
                tf_review_user.getText() + "', rate=" + tf_review_rate.getText() + ", date='" + tf_review_date.getValue()
                + "', feature=1 WHERE id =" + tf_review_id.getText() + ";";
        executeQuery(query);
        showReviews();
    }

    private void deleteReviewRecord() {
        String query = "DELETE FROM reviews WHERE id = " + tf_review_id.getText() + ";";
        executeQuery(query);
        showReviews();
    }
}
