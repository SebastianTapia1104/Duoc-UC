package com.sebastiantapia.patronesarquitectonicos.modelos.decorator;

public class TwentyPercDecorator extends BaseProductDecorator {

    public TwentyPercDecorator(BaseProduct componente) {
        super(componente);
    }
    
    @Override
    public double getPrecio() {
        return super.getPrecio() * 0.80;
    }
}