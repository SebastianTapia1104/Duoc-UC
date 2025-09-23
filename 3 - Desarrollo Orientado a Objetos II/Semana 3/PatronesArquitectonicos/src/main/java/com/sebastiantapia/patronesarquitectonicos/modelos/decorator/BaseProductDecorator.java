package com.sebastiantapia.patronesarquitectonicos.modelos.decorator;

public abstract class BaseProductDecorator extends BaseProduct {
    protected BaseProduct componente;

    public BaseProductDecorator(BaseProduct componente) {
        super(componente.getPrecio(), componente.getNombre(), componente.getCategoria(), componente.getColor(), componente.getTalla(), componente.getCantidad());
        this.componente = componente;
    }

    @Override
    public double getPrecio() {
        return this.componente.getPrecio();
    }
}