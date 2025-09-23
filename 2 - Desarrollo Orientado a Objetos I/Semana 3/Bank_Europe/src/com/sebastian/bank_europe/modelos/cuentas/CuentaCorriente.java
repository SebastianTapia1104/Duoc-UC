package com.sebastian.bank_europe.modelos.cuentas;

import com.sebastian.bank_europe.modelos.cuentas.CuentaBancaria;

// HERENCIA
public class CuentaCorriente extends CuentaBancaria {
    
    // ATRIBUTOS
    private double lineaCredito;
    private double deudaLineaCredito;
    private static final double tasaInteresMensual = 0.015;

    // CONSTRUCTOR
    public CuentaCorriente(int numeroCuenta, double saldoInicial, double lineaCredito) {
        super(numeroCuenta, saldoInicial);
        this.lineaCredito = lineaCredito;
        this.deudaLineaCredito = 0.0;
        System.out.println("Cuenta Corriente #" + numeroCuenta + " creada con saldo inicial de $" + String.format("%.2f", saldoInicial) + " CLP y línea de crédito de $" + String.format("%.2f", lineaCredito) + " CLP.");
    }

    // GETTERS Y SETTERS
    public double getLineaCredito() {
        return lineaCredito;
    }

    public void setLineaCredito(double lineaCredito) {
        this.lineaCredito = lineaCredito;
    }

    public double getDeudaLineaCredito() {
        return deudaLineaCredito;
    }

    public void setDeudaLineaCredito(double deudaLineaCredito) {
        this.deudaLineaCredito = deudaLineaCredito;
    }
    
    // MÉTODOS
    @Override
    public void depositar(double monto) {
        if (monto <= 0) {
            System.out.println("No se permite el ingreso de montos menores o iguales a cero.");
            return;
        }
        double montoRestante = monto;
        if (this.deudaLineaCredito > 0) { // Pago de deuda
            double pagoDeuda = Math.min(montoRestante, this.deudaLineaCredito);
            this.deudaLineaCredito -= pagoDeuda;
            montoRestante -= pagoDeuda;
            System.out.println("Se aplicaron $" + String.format("%.2f", pagoDeuda) + " CLP al pago de la línea de crédito. Deuda restante: $" + String.format("%.2f", this.deudaLineaCredito) + " CLP.");
            if (this.deudaLineaCredito <= 0.001) { // Pequeña tolerancia para flotantes
                this.deudaLineaCredito = 0.0;
                System.out.println("¡Deuda de línea de crédito saldada completamente!");
            }
        } if (montoRestante > 0) {
            this.saldo += montoRestante; 
            System.out.println("Se depositaron $" + String.format("%.2f", montoRestante) + " CLP al saldo de la cuenta.");
        }
        System.out.println("Depósito procesado. Saldo actual de la cuenta: $" + String.format("%.2f", this.saldo) + " CLP.");
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
            double creditoDisponibleDeLinea = this.lineaCredito - this.deudaLineaCredito;
            if (montoFaltante <= creditoDisponibleDeLinea) { // Uso de crédito
                this.saldo = 0;
                this.deudaLineaCredito += montoFaltante; 
                System.out.println("Giro realizado: Se utilizaron $" + String.format("%.2f", monto - (montoFaltante)) + " CLP del saldo principal (si había) y $" + String.format("%.2f", montoFaltante) + " CLP de la línea de crédito.");
            } else {
                System.out.println("Fondos insuficientes. No se puede girar $" + String.format("%.2f", monto) + " CLP.");
                System.out.println("Saldo disponible: $" + String.format("%.2f", this.saldo) + " CLP.");
                System.out.println("Crédito disponible de línea: $" + String.format("%.2f", creditoDisponibleDeLinea) + " CLP.");
                return;
            }
        }
        System.out.println("Saldo actual de la cuenta: $" + String.format("%.2f", this.saldo) + " CLP. Deuda de línea de crédito: $" + String.format("%.2f", this.deudaLineaCredito) + " CLP.");
    }
    
    @Override
    public void consultarSaldo() {
        System.out.println("Saldo actual en Cuenta Corriente #" + this.numeroCuenta + ": $" + String.format("%.2f", this.getSaldo()) + " CLP.");
        System.out.println("Deuda de Línea de Crédito: $" + String.format("%.2f", this.deudaLineaCredito) + " CLP.");
        this.calcularInteres();
    }
    
    @Override
    public void visualizarDatosCuenta() {
        System.out.println("--- Datos de la Cuenta Corriente ---");
        System.out.println("Número de Cuenta: " + this.getNumeroCuenta());
        System.out.println("Saldo Actual: $" + String.format("%.2f", this.getSaldo()) + " CLP");
        System.out.println("Línea de Crédito Aprobada: $" + String.format("%.2f", this.lineaCredito) + " CLP");
        System.out.println("Deuda de Línea de Crédito: $" + String.format("%.2f", this.deudaLineaCredito) + " CLP");
        System.out.println("Crédito Disponible de Línea: $" + String.format("%.2f", (this.lineaCredito - this.deudaLineaCredito)) + " CLP");
        this.calcularInteres();
    }
    
    @Override
    public void calcularInteres() {
        if (this.deudaLineaCredito > 0) {
            System.out.println("\n--- Proyección de Deuda de Línea de Crédito (si no se cancela) ---");
            System.out.println("Tasa de Interés Mensual: " + String.format("%.2f", (tasaInteresMensual * 100)) + "%");
            double deuda1Mes = proyectarDeuda(1); // Proyección a 1 mes
            System.out.println("Deuda en 1 mes: $" + String.format("%.2f", deuda1Mes) + " CLP");
            double deuda6Meses = proyectarDeuda(6);// Proyección a 6 meses
            System.out.println("Deuda en 6 meses: $" + String.format("%.2f", deuda6Meses) + " CLP");
            double deuda1Año = proyectarDeuda(12);// Proyección a 1 año (12 meses)
            System.out.println("Deuda en 1 año: $" + String.format("%.2f", deuda1Año) + " CLP");

            System.out.println("(Nota: Estas proyecciones asumen que no se realizan pagos ni se usan más fondos de la línea de crédito.)");
        } else {
            System.out.println("No hay deuda de línea de crédito para calcular intereses.");
        }
    }
    
    private double proyectarDeuda(int meses) {
        return this.deudaLineaCredito * Math.pow((1 + tasaInteresMensual), meses);
    }

}