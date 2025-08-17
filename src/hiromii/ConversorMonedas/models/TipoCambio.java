package hiromii.ConversorMonedas.models;

public record TipoCambio(
        String result,
        String baseCode,
        String targetCode,
        double conversionRate
) {
}