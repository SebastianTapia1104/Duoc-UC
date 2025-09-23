package teatromoro;
import java.util.Scanner;
/**
 *
 * @author Administrator
 */
public class TeatroMoro {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Variables para ciclos
        boolean tipo = true;
        boolean tarifa = true;
        // Definir precios de entradas
        int[][] tarifas = {
            {20000, 30000}, // VIP
            {10000, 15000}, // Platea Baja
            {9000, 18000},  // Platea Alta
            {6500, 13000}   // Palcos
        };
        // Tipos de entrada y tarifa
        String[] salidaEntradas = {"VIP", "Platea Baja", "Platea Alta", "Palcos"};
        String[] salidaTarifa = {"Estudiante", "Público General"};
        // Variables de entrada de información
        int tipoEntrada = 0;
        int tipoTarifa  = 0;
        
        while (tipo) {
            // Solicitar tipo de entrada
            System.out.println("Seleccione el tipo de entrada:");
            System.out.println("1. VIP\n2. Platea Baja\n3. Platea Alta\n4. Palcos");
            tipoEntrada = scanner.nextInt();

            // Validar entrada
            if (tipoEntrada < 1 || tipoEntrada > 4) {
                System.out.println("Selección inválida. Intente nuevamente.");
            } else {
                tipo = false;
            }
        }
        
        while (tarifa) {
            // Solicitar tarifa
            System.out.println("Seleccione la tarifa:");
            System.out.println("1. Estudiante\n2. Público General");
            tipoTarifa = scanner.nextInt();

            // Validar entrada
            if (tipoTarifa < 1 || tipoTarifa > 2) {
                System.out.println("Selección inválida. Intente nuevamente.");
            } else {
                tarifa = false;
            }
        }
        // Calcular total a pagar
        int totalPagar = tarifas[tipoEntrada - 1][tipoTarifa - 1];
        System.out.println("Tipo de entrada: " + salidaEntradas[tipoEntrada-1]);
        System.out.println("Tarifa: " + salidaTarifa[tipoTarifa - 1]);
        System.out.println("Total a pagar: $" + totalPagar);
        System.out.println("Gracias por su compra, disfrute la función.");
        scanner.close();
    }
}