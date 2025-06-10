package com.sebastian.bank_europe.modelos.cuentas;

import com.sebastian.bank_europe.modelos.cuentas.CuentaBancaria;

// HERENCIA
public class CuentaAhorro extends CuentaBancaria {
    
    // ATRIBUTOS
    private double tasaInteresAnual;

    // CONSTRUCTORES
    public CuentaAhorro(int numeroCuenta) {
        super(numeroCuenta);
        this.tasaInteresAnual = 0.035;
        System.out.println("Cuenta de Ahorro #" + numeroCuenta + " creada con tasa de interés de " + String.format("%.2f", (tasaInteresAnual * 100)) + "%.");
    }

    public CuentaAhorro(int numeroCuenta, double saldoInicial) {
        super(numeroCuenta, saldoInicial);
        this.tasaInteresAnual = 0.035;
        System.out.println("Cuenta de Ahorro #" + numeroCuenta + " creada con tasa de interés de " + String.format("%.2f", (tasaInteresAnual * 100)) + "%.");
    }

    // GETTERS Y SETTERS
    public double getTasaInteres() {
        return tasaInteresAnual;
    }

    public void setTasaInteres(double tasaInteres) {
        this.tasaInteresAnual = tasaInteres;
    }

    // MÉTODOS
    @Override
    public void depositar(double monto) {
        if (monto <= 0) {
            System.out.println("No se permite el ingreso de montos menores o iguales a cero.");
        } else {
            this.saldo += monto;
            System.out.println("¡Depósito realizado de manera exitosa en Cuenta de Ahorro!");
            consultarSaldo();
        }
    }
    
    @Override
    public void girar(double monto) {
        if (this.saldo <= 0) {
            System.out.println("Para realizar un giro, cada cliente debe tener un saldo mayor que cero.");
        } else if (monto <= 0) {
            System.out.println("No se permite el ingreso de montos menores o iguales a cero.");
        } else if (monto > this.saldo) {
            System.out.println("No se permite realizar giros que excedan o superen el saldo de la cuenta.");
        } else {
            this.saldo -= monto;
            System.out.println("Giro realizado con éxito en Cuenta de Ahorro. Saldo actual: $" + String.format("%.2f", this.saldo) + " CLP");
        }
    }
    
    @Override
    public void consultarSaldo() {
        System.out.println("Saldo actual en Cuenta de Ahorro #" + this.numeroCuenta + ": $" + String.format("%.2f", this.saldo) + " CLP");
        this.calcularInteres();
    }
    
    public void calcularInteres() {
        if (this.saldo > 0) {
            double interesesAnualesEstimados = this.saldo * this.tasaInteresAnual;
            System.out.println("Intereses anuales estimados: $" + String.format("%.2f", interesesAnualesEstimados) + " CLP.");
        } else {
            System.out.println("No hay saldo positivo para calcular intereses de ahorro.");
        }
    }
    
    public double consultarSaldo(int años) {
        if (años <= 0) {
            System.out.println("Los años para la proyección deben ser mayores que cero.");
            return this.saldo;
        }
        double saldoProyectado = this.saldo * Math.pow((1 + tasaInteresAnual), años);
        return saldoProyectado;
    }

    @Override
    public void visualizarDatosCuenta() {
        System.out.println("--- Datos de la Cuenta de Ahorro ---");
        System.out.println("Número de Cuenta: " + this.getNumeroCuenta());
        System.out.println("Saldo Actual: $" + String.format("%.2f", this.getSaldo()) + " CLP");
        System.out.println("Tasa de Interés Anual: " + String.format("%.2f", (this.tasaInteresAnual * 100)) + "%");
        System.out.println("\n--- Proyección de Saldo (interés compuesto) ---");
        double saldoProyectado3Años = consultarSaldo(3);
        System.out.println("Saldo en 3 años: $" + String.format("%.2f", saldoProyectado3Años) + " CLP");
        double saldoProyectado5Años = consultarSaldo(5);
        System.out.println("Saldo en 5 años: $" + String.format("%.2f", saldoProyectado5Años) + " CLP");
        this.calcularInteres();
    }
}