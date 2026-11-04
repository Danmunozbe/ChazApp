// ── ChazApp — Punto de entrada ───────────────────────────────────────────────
// App de escritorio para pedir en las chazas de la UNAL.
// Contiene: constantes de color, rutas, estado global y main().
//
// Módulos:
//   Modelos.java       → clases de datos (Chaza, Producto, Categoria, ItemCarrito)
//   BaseDatos.java     → consultas SQLite
//   UIHelper.java      → helpers de Swing (colores, botones, labels, imágenes)
//   PantallaInicio.java → pantalla 1: logo UNAL + selector de modo
//   PantallaChazas.java → pantalla 2: lista de chazas con banners
//   MenuChaza.java     → pantalla 3: menú por tabs + tarjeta de producto
//   Carrito.java       → lógica del carrito y diálogo de pedido

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChazApp {

    // ── Paleta de colores ────────────────────────────────────────────────────
    static final Color BG        = new Color(18, 18, 24);
    static final Color SURFACE   = new Color(30, 30, 40);
    static final Color ACCENT    = new Color(220, 50, 50);   // rojo UNAL
    static final Color TEXT      = new Color(230, 230, 235);
    static final Color TEXT_DIM  = new Color(140, 140, 155);
    static final Color GREEN     = new Color(50, 200, 100);
    static final Color RED_BADGE = new Color(200, 50, 50);

    // ── Rutas (relativas al directorio donde se ejecuta run.bat) ─────────────
    static final String DB      = System.getProperty("user.dir") + "/database/chazapp.db";
    static final String IMG_DIR = System.getProperty("user.dir") + "/img/";

    // ── Estado global ─────────────────────────────────────────────────────────
    static java.util.List<ItemCarrito> carrito = new java.util.ArrayList<>();
    static JFrame     ventana;
    static CardLayout cards;
    static JPanel     root;

    // ── Punto de entrada ──────────────────────────────────────────────────────
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignored) {}

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ventana = new JFrame("ChazApp \u2014 UNAL");
                ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ventana.setSize(480, 820);
                ventana.setMinimumSize(new Dimension(400, 600));
                ventana.setLocationRelativeTo(null);

                cards = new CardLayout();
                root  = new JPanel(cards);

                // Panel de chazas vacío: se llena al presionar "Ingresar"
                final JPanel panelChazas = UIHelper.darkPanel(new BorderLayout());

                root.add(PantallaInicio.construir(panelChazas), "inicio");
                root.add(panelChazas, "chazas");

                ventana.setContentPane(root);
                ventana.setVisible(true);
            }
        });
    }
}
