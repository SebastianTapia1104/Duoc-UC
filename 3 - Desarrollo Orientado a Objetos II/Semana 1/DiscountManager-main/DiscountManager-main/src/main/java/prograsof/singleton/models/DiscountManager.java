package prograsof.singleton.models;

// Clase Singleton
// Garantiza que solo exista un único objeto de una clase
// En este caso, el objeto creado es el discount manager
public class DiscountManager {

    // Variable estática para guardar la única instancia
    private static DiscountManager instancia;

    // Variable para almacenar el "descuento global"
    private double descuentoGlobal;

    // Constructor privado → impide crear instancias con "new" desde fuera
    private DiscountManager() {
        descuentoGlobal = 0.0; // descuento inicial
    }

    // Método estático público para obtener la instancia única
    // Esta es la "puerta oficial" 
    public static DiscountManager getInstance() {
        if (instancia == null) {
            instancia = new DiscountManager(); // se crea solo la primera vez
        }
        return instancia; // si ya está creada, solo la devuelve
    }

    //️ Métodos para trabajar con el descuento
    public void setDescuentoGlobal(double descuento) {
        this.descuentoGlobal = descuento;
    }

    public double getDescuentoGlobal() {
        return this.descuentoGlobal;
    }
}

