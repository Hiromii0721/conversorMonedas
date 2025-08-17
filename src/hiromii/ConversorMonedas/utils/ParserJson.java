package hiromii.ConversorMonedas.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import hiromii.ConversorMonedas.models.TipoCambio;

public class ParserJson {

    public static TipoCambio parseRate(String jsonString, String targetCurrency) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

        String result = jsonObject.get("result").getAsString();
        String baseCode = jsonObject.get("base_code").getAsString();
        double conversionRate = jsonObject.getAsJsonObject("conversion_rates").get(targetCurrency).getAsDouble();

        return new TipoCambio(result, baseCode, targetCurrency, conversionRate);
    }
}