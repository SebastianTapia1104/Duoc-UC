package com.sebastian.bank_europe.modelos.cuentas;

import com.sebastian.bank_europe.modelos.cuentas.CuentaBancaria;

// HERENCIA
public class CuentaDigital extends CuentaBancaria {

    // ATRIBUTOS
    private double limiteCreditoAsociado; 
    private double deudaLineaCredito; 
    private double tasaInteresAhorro; 
    private double tasaInteresDeuda; 
    private static final double tasaInteresMensualDeuda = 0.015; // Uso de 2 modos de establecer atributos fijos

    // CONSTRUCTOR
    public CuentaDigital(int numeroCuenta, double saldoInicial, double limiteCreditoAsociado) { // Uso de 2 modos de establecer atributos fijos
        super(numeroCuenta, saldoInicial);
        this.tasaInteresAhorro = 0.035;
        this.limiteCreditoAsociado = limiteCreditoAsociado;
        this.tasaInteresDeuda = 0.015;
        this.deudaLineaCredito = 0.0;
        System.out.println("Cuenta Digital #" + numeroCuenta + " creada con saldo inicial de $" + String.format("%.2f", saldoInicial) + " CLP.");
        System.out.println("Tasa de interés de ahorro: " + String.format("%.2f", (tasaInteresAhorro * 100)) + "%. Límite de crédito asociado: $" + String.format("%.2f", limiteCreditoAsociado) + " CLP (interés: " + String.format("%.2f", (tasaInteresDeuda * 100)) + "%).");
    }

    // GETTERS Y SETTERS
    public double getLimiteCreditoAsociado() {
        return limiteCreditoAsociado;
    }

    public void setLimiteCreditoAsociado(double limiteCreditoAsociado) {
        this.limiteCreditoAsociado = limiteCreditoAsociado;
    }

    public double getDeudaLineaCredito() {
        return deudaLineaCredito;
    }

    public void setDeudaLineaCredito(double deudaLineaCredito) {
        this.deudaLineaCredito = deudaLineaCredito;
    }

    public double getTasaInteresAhorro() {
        return tasaInteresAhorro;
    }

    public void setTasaInteresAhorro(double tasaInteresAhorro) {
        this.tasaInteresAhorro = tasaInteresAhorro;
    }

    public double getTasaInteresDeuda() {
        return tasaInteresDeuda;
    }

    public void setTasaInteresDeuda(double tasaInteresDeuda) {
        this.tasaInteresDeuda = tasaInteresDeuda;
    }

    // MÉTODOS
    @Override
    public void depositar(double monto) {
        if (monto <= 0) {
            System.out.println("No se permite el ingreso de montos menores o iguales a cero.");
            return;
        }
        double montoRestante = monto;
        if (this.deudaLineaCredito > 0) {
            double pagoDeuda = Math.min(montoRestante, this.deudaLineaCredito);
            this.deudaLineaCredito -= pagoDeuda;
            montoRestante -= pagoDeuda;
            System.out.println("Se aplicaron $" + String.format("%.2f", pagoDeuda) + " CLP al pago de la línea de crédito asociada. Deuda restante: $" + String.format("%.2f", this.deudaLineaCredito) + " CLP.");
            if (this.deudaLineaCredito <= 0.001) {
                this.deudaLineaCredito = 0.0;
                System.out.println("¡Deuda de línea de crédito asociada saldada completamente!");
            }
        }
        if (montoRestante > 0) {
            this.saldo += montoRestante;
            System.out.println("Se depositaron $" + String.format("%.2f", montoRestante) + " CLP al saldo principal de la cuenta.");
        }
        System.out.println("Depósito procesado. Saldo actual de la cuenta: $" + String.format("%.2f", this.saldo) + " CLP. Deuda de línea de crédito asociada: $" + String.format("%.2f", this.deudaLineaCredito) + " CLP.");
    }
    
    @Override
    public void girar(double monto) {
        if (monto <= 0) {
            System.out.println("Error: No se permite el ingreso de montos menores o iguales a cero.");
            return;
        }
        if (monto <= this.saldo) {
            this.saldo -= monto;
            System.out.println("Giro de $" + String.format("%.2f", monto) + " CLP realizado del saldo principal.");
        } else {
            double montoFaltante = monto - this.saldo;
            double creditoDisponibleDeLinea = this.limiteCreditoAsociado - this.deudaLineaCredito;
            if (montoFaltante <= creditoDisponibleDeLinea) {
                this.saldo = 0;
                this.deudaLineaCredito += montoFaltante; 
                System.out.println("Giro realizado: Se utilizaron $" + String.format("%.2f", monto - montoFaltante) + " CLP del saldo principal (si había) y $" + String.format("%.2f", montoFaltante) + " CLP de la línea de crédito asociada.");
            } else {
                System.out.println("Fondos insuficientes (saldo y línea de crédito asociada). No se puede girar $" + String.format("%.2f", monto) + " CLP.");
                System.out.println("Saldo disponible: $" + String.format("%.2f", this.saldo) + " CLP. Crédito disponible de línea asociada: $" + String.format("%.2f", creditoDisponibleDeLinea) + " CLP.");
                return;
            }
        }
        System.out.println("Saldo actual de la cuenta: $" + String.format("%.2f", this.saldo) + " CLP. Deuda de línea de crédito asociada: $" + String.format("%.2f", this.deudaLineaCredito) + " CLP.");
    }
    
    @Override
    public void consultarSaldo() {
        System.out.println("Saldo actual en Cuenta Digital #" + this.numeroCuenta + ": $" + String.format("%.2f", this.getSaldo()) + " CLP.");
        System.out.println("Deuda de Línea de Crédito Asociada: $" + String.format("%.2f", this.deudaLineaCredito) + " CLP.");
        System.out.println("Crédito Disponible de Línea Asociada: $" + String.format("%.2f", (this.limiteCreditoAsociado - this.deudaLineaCredito)) + " CLP");
        this.calcularInteres();
    }
    
    @Override
    public void calcularInteres() {
        if (this.saldo > 0) {
            double interesesAhorro = this.saldo * this.tasaInteresAhorro;
            System.out.println("Intereses anuales estimados: $" + String.format("%.2f", interesesAhorro) + " CLP.");
        } else {
            System.out.println("No hay saldo positivo para calcular intereses de ahorro.");
        }
        if (this.deudaLineaCredito > 0) {
            System.out.println("\n--- Proyección de Deuda de Línea de Crédito Asociada (si no se cancela) ---");
            System.out.println("Tasa de Interés Mensual: " + String.format("%.2f", (tasaInteresMensualDeuda * 100)) + "%");
            double deuda1Mes = proyectarDeudaCD(1); // Proyección a 1 mes
            System.out.println("Deuda en 1 mes: $" + String.format("%.2f", deuda1Mes) + " CLP");
            double deuda6Meses = proyectarDeudaCD(6); // Proyección a 6 meses
            System.out.println("Deuda en 6 meses: $" + String.format("%.2f", deuda6Meses) + " CLP");
            double deuda1Año = proyectarDeudaCD(12); // Proyección a 1 año (12 meses)
            System.out.println("Deuda en 1 año: $" + String.format("%.2f", deuda1Año) + " CLP");
            System.out.println("(Nota: Estas proyecciones asumen que no se realizan pagos ni se usan más fondos de la línea de crédito asociada.)");
        } else {
            System.out.println("No hay deuda de línea de crédito asociada para calcular intereses.");
        }
    }
    
    @Override
    public void visualizarDatosCuenta() {
        System.out.println("--- Datos de la Cuenta Digital ---");
        System.out.println("Número de Cuenta: " + this.getNumeroCuenta());
        System.out.println("Saldo Actual: $" + String.format("%.2f", this.getSaldo()) + " CLP");
        System.out.println("Tasa de Interés de Ahorro Anual: " + String.format("%.2f", (this.tasaInteresAhorro * 100)) + "%");
        System.out.println("Límite de Crédito Asociado: $" + String.format("%.2f", this.limiteCreditoAsociado) + " CLP");
        System.out.println("Deuda de Línea de Crédito Asociada: $" + String.format("%.2f", this.deudaLineaCredito) + " CLP");
        System.out.println("Crédito Disponible de Línea Asociada: $" + String.format("%.2f", (this.limiteCreditoAsociado - this.deudaLineaCredito)) + " CLP");
        System.out.println("Tasa de Interés de Deuda (Mensual): " + String.format("%.2f", (this.tasaInteresDeuda * 100)) + "%");
        this.calcularInteres();
    }
    
    private double proyectarDeudaCD(int meses) {
        return this.deudaLineaCredito * Math.pow((1 + tasaInteresMensualDeuda), meses);
    }
    
}