// ── Pantalla: Modo Vendedor ────────────────────────────────────────────────
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

class PantallaVendedor {

    static void abrir() {
        List<Chaza> chazas = BaseDatos.queryChazas();
        if (chazas.isEmpty()) {
            JOptionPane.showMessageDialog(ChazApp.ventana,
                "No hay chazas registradas o no se pudo conectar a la base de datos.",
                "ChazApp — Modo Vendedor", JOptionPane.ERROR_MESSAGE);
            return;
        }

        final Chaza[] chazaSeleccionada = { chazas.get(0) };

        final JDialog dlg = new JDialog(ChazApp.ventana, "Modo Vendedor — Gestión de Productos", true);
        dlg.setSize(520, 700);
        dlg.setLocationRelativeTo(ChazApp.ventana);

        JPanel main = UIHelper.darkPanel(new BorderLayout());

        // Top bar
        JPanel header = UIHelper.topBar("MODO VENDEDOR");
        main.add(header, BorderLayout.NORTH);

        // Selector de Chaza
        JPanel topPanel = UIHelper.darkPanel(new BorderLayout(8, 8));
        topPanel.setBorder(new EmptyBorder(12, 16, 8, 16));
        
        JLabel lblChaza = UIHelper.label("Selecciona tu Chaza:", 13, Font.BOLD, ChazApp.TEXT);
        
        String[] nombresChazas = chazas.stream().map(c -> c.nombre).toArray(String[]::new);
        JComboBox<String> comboChazas = new JComboBox<>(nombresChazas);
        UIHelper.styleCombo(comboChazas);

        topPanel.add(lblChaza, BorderLayout.NORTH);
        topPanel.add(comboChazas, BorderLayout.CENTER);

        // Contenedor dinámico de lista de productos
        JPanel contentPanel = UIHelper.darkPanel(new BorderLayout());
        main.add(topPanel, BorderLayout.NORTH);
        main.add(contentPanel, BorderLayout.CENTER);

        // Carga dinámica de productos según la chaza seleccionada
        Runnable cargarProductos = new Runnable() {
            public void run() {
                contentPanel.removeAll();
                List<Producto> productos = BaseDatos.queryTodosLosProductos(chazaSeleccionada[0].id);

                JPanel lista = UIHelper.darkPanel(new GridLayout(0, 1, 0, 10));
                lista.setBorder(new EmptyBorder(12, 16, 12, 16));

                if (productos.isEmpty()) {
                    lista.add(UIHelper.label("No hay productos registrados en esta chaza.", 13, Font.PLAIN, ChazApp.TEXT_DIM));
                } else {
                    for (Producto prod : productos) {
                        lista.add(tarjetaProductoVendedor(prod, dlg, this));
                    }
                }

                JScrollPane scroll = new JScrollPane(lista);
                scroll.setBorder(null);
                scroll.getViewport().setBackground(ChazApp.BG);
                contentPanel.add(scroll, BorderLayout.CENTER);
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        };

        comboChazas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idx = comboChazas.getSelectedIndex();
                if (idx >= 0) {
                    chazaSeleccionada[0] = chazas.get(idx);
                    cargarProductos.run();
                }
            }
        });

        cargarProductos.run();

        // Footer con botón para volver
        JPanel footer = UIHelper.darkPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        JButton btnVolver = new JButton("← Volver al inicio");
        UIHelper.styleSecBtn(btnVolver);
        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { dlg.dispose(); }
        });
        footer.add(btnVolver);
        main.add(footer, BorderLayout.SOUTH);

        dlg.setContentPane(main);
        dlg.setVisible(true);
    }

    private static JPanel tarjetaProductoVendedor(final Producto prod, final JDialog parentDlg, final Runnable recargar) {
        JPanel card = new JPanel(new BorderLayout(8, 4));
        card.setBackground(ChazApp.SURFACE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(55, 55, 70), 1, true),
            new EmptyBorder(10, 12, 10, 12)
        ));

        // Detalles del producto
        JPanel info = new JPanel(new GridLayout(3, 1, 0, 2));
        info.setBackground(ChazApp.SURFACE);
        info.add(UIHelper.label(prod.nombre, 14, Font.BOLD, ChazApp.TEXT));
        info.add(UIHelper.label("<html><body style='width:220px;color:#8c8c9b'>" + prod.descripcion + "</body></html>", 11, Font.PLAIN, ChazApp.TEXT_DIM));
        info.add(UIHelper.label(UIHelper.formatPrecio(prod.precio), 13, Font.BOLD, ChazApp.ACCENT));
        card.add(info, BorderLayout.CENTER);

        // Botón de toggle (Disponible / Agotado)
        JPanel right = new JPanel(new GridLayout(2, 1, 0, 6));
        right.setBackground(ChazApp.SURFACE);

        JLabel badge = prod.disponible
            ? UIHelper.label("✓ Disponible", 11, Font.BOLD, ChazApp.GREEN)
            : UIHelper.label("✗ Agotado", 11, Font.BOLD, ChazApp.RED_BADGE);
        badge.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnToggle = new JButton(prod.disponible ? "Marcar Agotado" : "Marcar Disponible");
        btnToggle.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnToggle.setBackground(prod.disponible ? ChazApp.RED_BADGE : ChazApp.GREEN);
        btnToggle.setForeground(Color.WHITE);
        btnToggle.setBorder(new EmptyBorder(6, 10, 6, 10));
        btnToggle.setFocusPainted(false);
        btnToggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnToggle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean nuevoEstado = !prod.disponible;
                boolean exito = BaseDatos.actualizarDisponibilidad(prod.id, nuevoEstado);
                if (exito) {
                    recargar.run();
                } else {
                    JOptionPane.showMessageDialog(parentDlg,
                        "No se pudo actualizar la disponibilidad en la base de datos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        right.add(badge);
        right.add(btnToggle);
        card.add(right, BorderLayout.EAST);

        return card;
    }
}