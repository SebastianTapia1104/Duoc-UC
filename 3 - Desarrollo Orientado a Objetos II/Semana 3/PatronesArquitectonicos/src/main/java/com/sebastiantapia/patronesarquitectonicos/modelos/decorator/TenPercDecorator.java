package com.sebastiantapia.patronesarquitectonicos.modelos.decorator;

public class TenPercDecorator extends BaseProductDecorator {

    public TenPercDecorator(BaseProduct componente) {
        super(componente);
    }
    
    @Override
    public double getPrecio() {
        return super.getPrecio() * 0.90;
    }
}