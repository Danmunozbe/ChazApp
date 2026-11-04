// ── Base de datos ─────────────────────────────────────────────────────────────
// Conexión SQLite y consultas. Usa el driver xerial sqlite-jdbc.

import java.sql.*;
import java.util.*;

class BaseDatos {

    static Connection connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }
        return DriverManager.getConnection("jdbc:sqlite:" + ChazApp.DB);
    }

    // Devuelve todas las chazas activas con su conteo de productos disponibles
    static List<Chaza> queryChazas() {
        List<Chaza> list = new ArrayList<>();
        try {
            Connection c = connect();
            ResultSet rs = c.createStatement().executeQuery(
                "SELECT id, nombre, descripcion, ubicacion FROM chazas WHERE activa=1 ORDER BY id");
            while (rs.next()) {
                int id = rs.getInt("id");
                ResultSet rd = c.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM productos WHERE chaza_id=" + id + " AND disponible=1");
                int disp = rd.getInt(1);
                list.add(new Chaza(id,
                    rs.getString("nombre"), rs.getString("descripcion"),
                    rs.getString("ubicacion"), disp));
            }
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Devuelve el menú de una chaza organizado por categorías
    static List<Categoria> queryMenu(int chazaId) {
        List<Categoria> cats = new ArrayList<>();
        try {
            Connection c = connect();
            ResultSet rsCat = c.createStatement().executeQuery(
                "SELECT id, nombre FROM categorias WHERE chaza_id=" + chazaId + " ORDER BY orden");
            while (rsCat.next()) {
                int catId = rsCat.getInt("id");
                String catNombre = rsCat.getString("nombre");
                List<Producto> prods = new ArrayList<>();
                ResultSet rsProd = c.createStatement().executeQuery(
                    "SELECT id, nombre, descripcion, precio, disponible " +
                    "FROM productos WHERE categoria_id=" + catId + " ORDER BY id");
                while (rsProd.next()) {
                    prods.add(new Producto(
                        rsProd.getInt("id"),
                        rsProd.getString("nombre"),
                        rsProd.getString("descripcion"),
                        rsProd.getDouble("precio"),
                        rsProd.getInt("disponible") == 1
                    ));
                }
                cats.add(new Categoria(catNombre, prods));
            }
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cats;
    }
}
