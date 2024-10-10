package Prototype2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class weather {
  public static float getWeather(){
    try{
      String city;
        // Retrieve user input
        city = "Davao city";
        // Get location data
        JSONObject cityLocationData = getLocationData(city);
        assert cityLocationData != null;
        double latitude = (double) cityLocationData.get("latitude");
        double longitude = (double) cityLocationData.get("longitude");
        return (float) displayWeatherData(latitude, longitude);
    }catch(Exception e){
      e.printStackTrace();
      return -1;
      //set the outside weather to no internet
    }
  }

  private static JSONObject getLocationData(String city){
    city = city.replaceAll(" ", "+");

    String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
    city + "&count=1&language=en&format=json";

    try{
      // 1. Fetch the API response based on API Link
      HttpURLConnection apiConnection = fetchApiResponse(urlString);

      // check for response status
      // 200 - means that the connection was a success
      assert apiConnection != null;
      if(apiConnection.getResponseCode() != 200){
        System.out.println("Error: Could not connect to API");
        return null;
      }

      // 2. Read the response and convert store String type
      String jsonResponse = readApiResponse(apiConnection);

      // 3. Parse the string into a JSON Object
      JSONParser parser = new JSONParser();
      JSONObject resultsJsonObj = (JSONObject) parser.parse(jsonResponse);

      // 4. Retrieve Location Data
      JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
      return (JSONObject) locationData.getFirst();

    }catch(Exception e){
      e.printStackTrace();
    }
    return null;
  }

  private static double displayWeatherData(double latitude, double longitude){
    try{
      // 1. Fetch the API response based on API Link
      String url = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
      "&longitude=" + longitude + "&current=temperature_2m,relative_humidity_2m,wind_speed_10m";
      HttpURLConnection apiConnection = fetchApiResponse(url);

      assert apiConnection != null;
      if(apiConnection.getResponseCode() != 200){
        System.out.println("Error: Could not connect to API");

      }

      // 2. Read the response and convert store String type
      String jsonResponse = readApiResponse(apiConnection);

      // 3. Parse the string into a JSON Object
      JSONParser parser = new JSONParser();
      JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
      JSONObject currentWeatherJson = (JSONObject) jsonObject.get("current");
//            System.out.println(currentWeatherJson.toJSONString());

      // 4. Store the data into their corresponding data type
      String time = (String) currentWeatherJson.get("time");
      System.out.println("Current Time: " + time);

      return (double) currentWeatherJson.get("temperature_2m");
    }catch(Exception e){
      e.printStackTrace();
      return 0;
    }
  }

  private static String readApiResponse(HttpURLConnection apiConnection) {
    try {
      // Create a StringBuilder to store the resulting JSON data
      StringBuilder resultJson = new StringBuilder();

      // Create a Scanner to read from the InputStream of the HttpURLConnection
      Scanner scanner = new Scanner(apiConnection.getInputStream());

      // Loop through each line in the response and append it to the StringBuilder
      while (scanner.hasNext()) {
        // Read and append the current line to the StringBuilder
        resultJson.append(scanner.nextLine());
      }
      // Return the JSON data as a String
      return resultJson.toString();

    } catch (IOException e) {
      // Print the exception details in case of an IOException
      e.printStackTrace();
    }

    // Return null if there was an issue reading the response
    return null;
  }

  private static HttpURLConnection fetchApiResponse(String urlString){
    try{
      // attempt to create connection
      URL url = new URL(urlString);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();

      // set request method to get
      conn.setRequestMethod("GET");

      return conn;
    }catch(IOException e){
      e.printStackTrace();
    }

    // could not make connection
    return null;
  }
}