package fxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginController {
    public TextField textFieldEmail;
    public PasswordField passwordFieldPassword;
    public Button buttonLogin;
    public Label error;


    public void login(ActionEvent actionEvent) throws Exception {
        String email = textFieldEmail.getText();
        String password = passwordFieldPassword.getText();
        URL url = new URL("http://smieszne-koty.herokuapp.com/oauth/token?grant_type=password&email="+email+"&password="+password);
       try {
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("POST");
           connection.setReadTimeout(3000);
           connection.setDoInput(true);
           connection.setDoOutput(true);

           BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
           StringBuilder response = new StringBuilder();
           String inputLine;

           while ((inputLine = input.readLine()) != null)
               response.append(inputLine);
           input.close();

           JSONObject autoryzacja = new JSONObject(response.toString());
           String token = autoryzacja.getString("access_token");
           Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
           CatViewController catViewController = new CatViewController(token);
           FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CatView.fxml"));
           fxmlLoader.setController(catViewController);
           stage.setScene(new Scene(fxmlLoader.load()));

       }
       catch (Exception e) {
           error.setText("Error in logging in");
           e.printStackTrace();
       }
    }
}
