package fxml;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

public class CatViewController implements Initializable {
    public Button buttonNext;
    public Button buttonPrevious;
    public Label labelName;
    public Label labelCatNumber;
    public Label labelPageNumber;
    public Label labelPoints;
    public ImageView imageViewCat;
    private int page = 1;
    private int catNumber = 0;
    private String token;
    private JSONArray cats;
    private JSONObject cat;

    public CatViewController(String token) {
        this.token = token;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonNext.setOnAction(e-> {

            catNumber++;
            if (catNumber != 25) {
                cat = cats.getJSONObject(catNumber);
                showNewCat();
                buttonPrevious.setDisable(false);
            } else {
                buttonNext.setDisable(true);
                try {
                    page++;
                    catNumber = 0;
                    cats = new JSONArray(getCatsFromServer());
                    cat = cats.getJSONObject(catNumber);
                    showNewCat();
                    buttonNext.setDisable(false);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        });

        buttonPrevious.setOnAction(e-> {

            catNumber--;
            if (catNumber == 0 && page==1){
                buttonPrevious.setDisable(true);
            }
            if (catNumber != -1) {
                cat = cats.getJSONObject(catNumber);
                showNewCat();
            } else {
                buttonNext.setDisable(true);
                try {
                    page--;
                    catNumber = 24;
                    cats = new JSONArray(getCatsFromServer());
                    cat = cats.getJSONObject(catNumber);
                    showNewCat();
                    buttonNext.setDisable(false);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        });

        try {
            cats = new JSONArray(getCatsFromServer());
            cat = cats.getJSONObject(catNumber);
            showNewCat();
        } catch (IOException e) {
            e.printStackTrace();
        }
        buttonPrevious.setDisable(true);

    }

    private void showNewCat() {
        labelName.setText(cat.getString("name"));
        labelPoints.setText(String.valueOf(cat.getInt("vote_count")));
        labelCatNumber.setText(String.valueOf(catNumber+((page-1)*25+1)));
        labelPageNumber.setText(String.valueOf(page));
        String imageURL = cat.getString("url");
        imageViewCat.setImage(new Image(imageURL));
    }


    private String getCatsFromServer() throws IOException {
        URL url = new URL("http://smieszne-koty.herokuapp.com/api/kittens?access_token=" + token + "&page=" + page);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(30000);

        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = input.readLine()) != null) {
            response.append(inputLine);
        }
        input.close();
        connection.disconnect();

        return response.toString();
    }


}