// ── Modelos de datos ─────────────────────────────────────────────────────────
// Clases simples (Java 8 compatible). No usan records ni lambdas.

import java.util.List;

class Chaza {
    int id;
    String nombre, descripcion, ubicacion;
    int disponibles;

    Chaza(int id, String n, String d, String u, int disp) {
        this.id = id; nombre = n; descripcion = d; ubicacion = u; disponibles = disp;
    }
}

class Producto {
    int id;
    String nombre, descripcion;
    double precio;
    boolean disponible;

    Producto(int id, String n, String d, double p, boolean disp) {
        this.id = id; nombre = n; descripcion = d; precio = p; disponible = disp;
    }
}

class Categoria {
    String nombre;
    List<Producto> productos;

    Categoria(String n, List<Producto> p) { nombre = n; productos = p; }
}

class ItemCarrito {
    Producto p;
    String chaza;
    int cantidad;

    ItemCarrito(Producto p, String c, int cant) { this.p = p; chaza = c; cantidad = cant; }
}
