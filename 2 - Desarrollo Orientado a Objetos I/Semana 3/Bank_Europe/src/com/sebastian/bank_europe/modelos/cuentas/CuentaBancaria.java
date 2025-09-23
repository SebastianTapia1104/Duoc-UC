package com.sebastian.bank_europe.modelos.cuentas;

// CLASE PADRE ABSTRACTA
public abstract class CuentaBancaria {

    // ATRIBUTOS
    protected int numeroCuenta;
    protected double saldo;
    
    // CONSTRUCTORES
    public CuentaBancaria(int numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = 0.0;
    }
    
    public CuentaBancaria(int numeroCuenta, double saldoInicial) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldoInicial;
    }

    // GETTERS Y SETTERS
    public int getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(int numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    // MÉTODOS ABSTRACTOS
    public abstract void calcularInteres(); // Cálculo con valores fijos en distintos periódos dependiendo si son positivos o negativos.
    
    public abstract void depositar(double monto); // Montos positivos y calculo de deuda pendiente.
    
    public abstract void girar(double monto); // Montos positivos y calculo de deuda pendiente.
    
    public abstract void consultarSaldo(); // Muestra de saldo e interéses
    
    public abstract void visualizarDatosCuenta(); // Muestra de saldo e interéses
    
}