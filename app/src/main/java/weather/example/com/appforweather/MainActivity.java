package weather.example.com.appforweather;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static weather.example.com.appforweather.R.drawable.pressure;

public class MainActivity extends AppCompatActivity {

    ImageView image;
    TextView weather_info,weather_Description,country_info,date_info,temperature_info,wind_info,humidity_info,sunrise_info,sunset_info;

    EditText cityName;

    // This method would be called when What's the weather button would be pressed
    public void findtheweather(View view) {


        if(cityName.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this,"enter a name ",Toast.LENGTH_LONG).show();
        }else {
            //Assigning the value which user entered to String s
            String s = cityName.getText().toString();

            // Calling download task function
            download task = new download();

            // Executing download task and change this " uk&appid=9f681051b003f104ae5e2a0cbef19a02" with your own API KEY
            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + s + "uk&appid=9f681051b003f104ae5e2a0cbef19a02");
        }
    }

    // onCreate method

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// Using id to tell where to make changes
        cityName = (EditText) findViewById(R.id.cityname);

    }

    // Creating download method with Async Task ( we r going to use this to get data from internet and parse it )
    private class download extends AsyncTask<String, Void, String> {

        // This method execute after doInBackground method and Parse the Json

        @Override
        protected void onPostExecute(String result) {

            Log.i("info",result);
            // Try and catch block to catch any errors
           try {
                   // linking result1 with textView with id result
                   weather_info = (TextView) findViewById(R.id.weather_info);
                   weather_Description = (TextView) findViewById(R.id.weather_Description);
                   country_info = (TextView) findViewById(R.id.city_country);
                   date_info = (TextView) findViewById(R.id.current_date);
                   temperature_info = (TextView) findViewById(R.id.weather_result);
                   wind_info= (TextView) findViewById(R.id.wind_result);
                   humidity_info = (TextView) findViewById(R.id.humidity_result);
                   sunrise_info = (TextView) findViewById(R.id.sunrise_result);
                   sunset_info = (TextView) findViewById(R.id.sunset_result);

               //calling  JSONObject as jsonObject
                   JSONObject jsonObject = new JSONObject(result);
               //using jsonObject to get String from Json which is tagged as weather
                   String weather = jsonObject.getString("weather");
                   //calling JSONArray as arr
                   JSONArray arr = new JSONArray(weather);
                   // using for loop to loop through arr array
                   for (int i = 0; i <= arr.length(); i++) {

                       JSONObject jsonPart = arr.getJSONObject(i);

                       // using result1 to set Text of that Text view with id Result  to this
                       // weather_info.setText(String.format("Weather : %s , Description : %s", jsonPart.getString("main") , jsonPart.getString("description")));

                       weather_info.setText(String.format("Weather : %s ", jsonPart.getString("main")));
                       weather_Description.setText(String.format("Description : %s ", jsonPart.getString("description")));

                       //JSONObject jsonObject = new JSONObject(result);
                       String temp = jsonObject.getString("coord");
                       //using jsonObject to get String from Json which is tagged as weather
                       //weather_info.setText(String.format("temp",temp.getString));
                       Log.i("info hfghf", temp);
                       JSONObject systemData = new JSONObject(jsonObject.getString("sys"));
                       String sunrise = systemData.getString("sunrise");
                       long sunriseTime = Long.parseLong(sunrise);
                       SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");
                       Date resultRiseTime = new Date(sunriseTime * 1000);
                       sunrise_info.setText(sdf1.format(resultRiseTime));

                       String sunset = systemData.getString("sunset");
                       long sunsetTime = Long.parseLong(sunset);
                       SimpleDateFormat sdf11 = new SimpleDateFormat("hh:mm a");
                       Date resultsunsetTime = new Date(sunsetTime * 1000);
                       sunset_info.setText(sdf11.format(resultsunsetTime));

                       String countrySet=systemData.getString("country");
                       Log.i("info",countrySet);
                       country_info.setText(String.format("%s ,%s", cityName.getText().toString() , countrySet));

                       JSONObject windData = new JSONObject(jsonObject.getString("wind"));
                       String windSpeed = windData.getString("speed");
                       double windLong=Double.parseDouble(windSpeed);
                       Log.i("dhgsd", String.valueOf(windLong));
                       wind_info.setText(String.format("%s km/h",String.valueOf(windLong)));

                       JSONObject main = new JSONObject(jsonObject.getString("main"));
                       String humidityData = main.getString("humidity");
                       double humidityLong=Double.parseDouble(humidityData);
                       Log.i("dhgsd", String.valueOf(humidityLong));
                       int humi=(int) humidityLong;
                       humidity_info.setText(String.format("%s ",String.valueOf(humi) +" %"));

                       String tempData = main.getString("temp");
                       double tempLong=Double.parseDouble(tempData);
                       long cel= (long) (tempLong-273);
                       int temp1=(int) cel;
                       temperature_info.setText(String.format("%s °",String.valueOf(temp1)));

                       Calendar calendar = Calendar.getInstance();
                       SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY, EEEE");
                       String formatted_date = sdf.format(calendar.getTime());
                       date_info.setText(formatted_date);

                   }
               //e.printStackTrace will just print a error report like a normal program do when it crashes u can change this with anything u like
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // do doInBackground method we r using this method to download Json from site.
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            //  calling url as url
            URL url;
            // calling HttpUrlConnection as urlConnection
            HttpURLConnection urlConnection;
            // Using try and catch block to find any errors
            try {
                // assigning url value of first object in array called urls which is declared in this start of this method
                url = new URL(urls[0]);
                // using urlConnection to open url which we assigning URL before
                urlConnection = (HttpURLConnection) url.openConnection();
                // Using InputStream to download the content
                InputStream inputStream = urlConnection.getInputStream();
                // Using InputStreamReader to read the inputstream or the data we r downloading
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                // using it to check if we reached the end of String / Data
                int Data = inputStreamReader.read();
                // using While loop to assign that data to string called result because InputStreamReader reads only one character at a time
                while (Data != -1) {
                    char current = (char) Data;
                    result += current;
                    Data = inputStreamReader.read();
                }
                // returning value of result
                return result;
                //Try and catch block to catch any errors
            } catch (Exception e) {
                 //  e.printStackTrace will just print a error report like a normal program do when it crashes u can change this with anything u like
                e.printStackTrace();
            }
            return null;
        }
    }
}
