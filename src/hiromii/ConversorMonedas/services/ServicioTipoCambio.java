package hiromii.ConversorMonedas.services;
import hiromii.ConversorMonedas.models.TipoCambio;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServicioTipoCambio {

    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";
    private static final String API_KEY = "4bb4ab1d16cfb4d19a9e2fdd";

    public static String construirURL(String base) {
        return BASE_URL + API_KEY + "/latest/" + base;
    }

    public static String consultarURL(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            String inputLine;
            StringBuilder contenido = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                contenido.append(inputLine);
            }
            in.close();
            conexion.disconnect();

            return contenido.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
}