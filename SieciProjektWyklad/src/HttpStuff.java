import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpStuff {

    public static void main(String[] args) throws Exception {
        URL url = new URL("http://smieszne-koty.herokuapp.com//oauth/token?grant_type=password&email=229715@student.pwr.edu.pl&password=229715");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setReadTimeout(3000);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine=input.readLine()) != null)
            response.append(inputLine);
        input.close();
        System.out.println(response);

        JSONObject autoryzacja = new JSONObject(response.toString());
        System.out.println("Token = " + autoryzacja.getString("access_token"));

        String token = autoryzacja.getString("access_token");
        url = new URL("http://smieszne-koty.herokuapp.com/api/kittens?access_token="+token);

        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(3000);

        input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        response = new StringBuilder();

        while ((inputLine = input.readLine()) != null)
            response.append(inputLine);
        input.close();
        System.out.println(response);

        var kotki = new JSONArray(response.toString());

        for (var i =0; i< kotki.length(); i++){
            var kotek = kotki.getJSONObject(i);
            System.out.println(kotek.getString("name"));
        }

    }


}
