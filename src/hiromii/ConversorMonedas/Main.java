package hiromii.ConversorMonedas;

import hiromii.ConversorMonedas.models.TipoCambio;
import hiromii.ConversorMonedas.utils.ParserJson;
import hiromii.ConversorMonedas.services.ServicioTipoCambio;
import hiromii.ConversorMonedas.utils.HistorialArchivo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        bienvenida();
        List<String> historialConversiones = new ArrayList<>();

        while (true) {
            String fromCurrency = seleccionarMoneda("La moneda BASE");
            String toCurrency = seleccionarMoneda("La moneda COTIZADA");
            double montoUsuario = montoAConvertir();

            String buildRequest = ServicioTipoCambio.construirURL(fromCurrency);
            String respuestaExchange = ServicioTipoCambio.consultarURL(ServicioTipoCambio.construirURL(fromCurrency));
            TipoCambio tipoCambio = ParserJson.parseRate(respuestaExchange, toCurrency);
            infoParseada(tipoCambio);
            mostrarResultadoConversion(tipoCambio, montoUsuario);

            String resumen = String.format("%.2f %s => %.2f %s (Tasa: %.4f)",
                    montoUsuario,
                    tipoCambio.baseCode(),
                    montoUsuario * tipoCambio.conversionRate(),
                    tipoCambio.targetCode(),
                    tipoCambio.conversionRate());
            historialConversiones.add(resumen);

            if (preguntaBucle().equals("no")) {
                break;
            }
        }

        preguntarYGuardarArchivo(historialConversiones);
        despedida();
    }

    private static void bienvenida() {
        String mensaje = """
        Bienvenido al Conversor de Monedas!
        Convierte divisas de manera rápida y sencilla.
        Empecemos seleccionando las monedas que deseas usar
        """;

        String[] lineas = mensaje.split("\n");
        int maxAncho = 0;
        for (String linea : lineas) {
            if (linea.length() > maxAncho) {
                maxAncho = linea.length();
            }
        }

        System.out.println("+" + "-".repeat(maxAncho + 2) + "+");
        for (String linea : lineas) {
            System.out.printf("| %-"+ maxAncho +"s |\n", linea);
        }
        System.out.println("+" + "-".repeat(maxAncho + 2) + "+");
    }

    private static String seleccionarMoneda(String tipo) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Elige" + tipo + ":");

        String[][] opciones = {
                {"1) USD - Dólar estadounidense", "2) EUR - Euro", "3) GBP - Libra esterlina"},
                {"4) JPY - Yen japonés", "5) MXN - Peso mexicano", "6) NZD - Dólar neozelandés"},
                {"7) AUD - Dólar australiano", "8) CAD - Dólar canadiense", "9) CNY - Yuan chino"},
                {"10) CHF - Franco suizo", "11) BRL - Real brasileño", "12) ARS - Peso argentino"}
        };

        int maxLen = 0;
        for (String[] fila : opciones) {
            for (String op : fila) {
                if (op.length() > maxLen) maxLen = op.length();
            }
        }

        String borde = "+";
        for (int i = 0; i < 3; i++) borde += "-".repeat(maxLen + 2) + "+";
        for (String[] fila : opciones) {
            System.out.println(borde);
            System.out.print("|");
            for (String op : fila) {
                System.out.printf(" %-"+maxLen+"s |", op);
            }
            System.out.println();
        }
        System.out.println(borde);

        int opcion = scanner.nextInt();
        return switch (opcion) {
            case 1 -> "USD";
            case 2 -> "EUR";
            case 3 -> "GBP";
            case 4 -> "JPY";
            case 5 -> "MXN";
            case 6 -> "NZD";
            case 7 -> "AUD";
            case 8 -> "CAD";
            case 9 -> "CNY";
            case 10 -> "CHF";
            case 11 -> "BRL";
            case 12 -> "ARS";
            default -> {
                System.out.println("No valido, intenta nuevamente.");
                yield seleccionarMoneda(tipo);
            }
        };
    }

    private static double montoAConvertir() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digita el monto que quieres convertir:");
        return scanner.nextDouble();
    }

    private static void infoParseada(TipoCambio tipoCambio) {
        System.out.printf("Actualmente, el tipo de cambio de %s a %s es: %.2f%n",
                tipoCambio.baseCode(),
                tipoCambio.targetCode(),
                tipoCambio.conversionRate());
    }

    private static void mostrarResultadoConversion(TipoCambio tipoCambio, double monto) {
        double resultado = monto * tipoCambio.conversionRate();
        System.out.printf("%.2f %s equivale a %.2f %s%n",
                monto,
                tipoCambio.baseCode(),
                resultado,
                tipoCambio.targetCode());
    }

    private static String preguntaBucle() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Deseas continuar? Si/No");
        return scanner.nextLine().trim().toLowerCase(Locale.ROOT);
    }

    private static void preguntarYGuardarArchivo(List<String> historial) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Desea guardar el historial? Si/No");
        String respuesta = scanner.nextLine().trim().toLowerCase();

        if (respuesta.equals("si")) {
            HistorialArchivo.guardarHistorial(historial);
        } else {
            System.out.println("No se guardó historial.");
        }
    }

    private static void despedida() {
        String mensaje1 = "Gracias por usar ConversorMonedas!";
        String mensaje2 = "¡Hasta pronto!";

        int maxLen = Math.max(mensaje1.length(), mensaje2.length());
        String borde = "+-" + "-".repeat(maxLen) + "-+";

        System.out.println(borde);
        System.out.printf("| %-"+maxLen+"s |\n", mensaje1);
        System.out.printf("| %-"+maxLen+"s |\n", mensaje2);
        System.out.println(borde);
    }
}