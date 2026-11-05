// ── Menú de una chaza ────────────────────────────────────────────────────────
// Diálogo modal con tabs por categoría y tarjeta por producto.

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

class MenuChaza {

    // Abre el diálogo del menú de la chaza seleccionada
    static void abrir(Chaza chaza) {
        List<Categoria> menu = BaseDatos.queryMenu(chaza.id);

        final JDialog dlg = new JDialog(ChazApp.ventana, chaza.nombre, true);
        dlg.setSize(460, 720);
        dlg.setLocationRelativeTo(ChazApp.ventana);

        JPanel main = UIHelper.darkPanel(new BorderLayout());

        // ── Header ───────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout(10, 6));
        header.setBackground(new Color(25, 25, 35));
        header.setBorder(new EmptyBorder(14, 16, 10, 16));

        JButton back = new JButton("\u2190  Volver");
        UIHelper.styleSecBtn(back);
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { dlg.dispose(); }
        });

        JPanel info = new JPanel(new GridLayout(2, 1, 0, 2));
        info.setBackground(new Color(25, 25, 35));
        info.add(UIHelper.label(chaza.nombre,       18, Font.BOLD,  ChazApp.TEXT));
        info.add(UIHelper.label("\uD83D\uDCCD " + chaza.ubicacion, 12, Font.PLAIN, ChazApp.TEXT_DIM));

        header.add(back, BorderLayout.WEST);
        header.add(info, BorderLayout.CENTER);
        main.add(header, BorderLayout.NORTH);

        // ── Tabs de categorías ────────────────────────────────────────────────
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(ChazApp.BG);
        tabs.setForeground(ChazApp.TEXT);
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13));

        String[] emojis = {"\uD83C\uDF2F", "\uD83E\uDD57", "\uD83C\uDF5F", "\uD83E\uDD64"};
        int ei = 0;
        for (Categoria cat : menu) {
            String em = ei < emojis.length ? emojis[ei++] : "\uD83C\uDF74";
            if (cat.nombre.equalsIgnoreCase("Bebidas")) em = "\uD83E\uDD64";
            tabs.addTab(em + " " + cat.nombre, panelProductos(cat.productos, chaza.nombre, em, dlg));
        }
        main.add(tabs, BorderLayout.CENTER);

        // ── Footer: botón carrito ─────────────────────────────────────────────
        final JButton carritoBtn = new JButton("\uD83D\uDED2  Ver carrito");
        carritoBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        carritoBtn.setBackground(ChazApp.ACCENT);
        carritoBtn.setForeground(Color.WHITE);
        carritoBtn.setBorder(new EmptyBorder(12, 20, 12, 20));
        carritoBtn.setFocusPainted(false);
        carritoBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        carritoBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { Carrito.abrir(dlg, carritoBtn); }
        });

        JPanel footer = UIHelper.darkPanel(new BorderLayout());
        footer.setBorder(new EmptyBorder(8, 16, 12, 16));
        footer.add(carritoBtn, BorderLayout.CENTER);
        main.add(footer, BorderLayout.SOUTH);

        dlg.setContentPane(main);
        dlg.setVisible(true);
    }

    // Scroll con todas las tarjetas de producto de una categoría
    private static JScrollPane panelProductos(List<Producto> productos,
                                               String chazaNombre, String emoji, JDialog dlg) {
        JPanel panel = UIHelper.darkPanel(new GridLayout(0, 1, 0, 10));
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        for (Producto prod : productos) {
            panel.add(tarjetaProducto(prod, chazaNombre, emoji));
        }
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(ChazApp.BG);
        return scroll;
    }

    // Tarjeta visual de un producto: emoji, nombre, descripción, precio, botón +
    private static JPanel tarjetaProducto(final Producto prod, final String chazaNombre, String emoji) {
        JPanel card = new JPanel(new BorderLayout(8, 4));
        card.setBackground(ChazApp.SURFACE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(55, 55, 70), 1, true),
            new EmptyBorder(10, 12, 10, 12)
        ));

        JLabel ico = UIHelper.label(emoji, 30, Font.PLAIN, ChazApp.TEXT);
        ico.setBorder(new EmptyBorder(0, 0, 0, 8));
        card.add(ico, BorderLayout.WEST);

        JPanel info = new JPanel(new GridLayout(0, 1, 0, 3));
        info.setBackground(ChazApp.SURFACE);
        info.add(UIHelper.label(prod.nombre, 14, Font.BOLD, ChazApp.TEXT));
        info.add(UIHelper.label("<html><body style='width:220px;color:#8c8c9b'>" + prod.descripcion + "</body></html>",
                                11, Font.PLAIN, ChazApp.TEXT_DIM));
        info.add(UIHelper.label(UIHelper.formatPrecio(prod.precio), 13, Font.BOLD, ChazApp.ACCENT));
        card.add(info, BorderLayout.CENTER);

        // Badge disponible / agotado + botón +
        JPanel right = new JPanel(new GridLayout(2, 1, 0, 6));
        right.setBackground(ChazApp.SURFACE);
        right.setBorder(new EmptyBorder(0, 8, 0, 0));

        JLabel badge = prod.disponible
            ? UIHelper.label("\u2713 Disponible", 10, Font.BOLD, ChazApp.GREEN)
            : UIHelper.label("\u2717 Agotado",    10, Font.BOLD, ChazApp.RED_BADGE);
        badge.setHorizontalAlignment(SwingConstants.CENTER);

        final JButton addBtn = new JButton("+");
        addBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        addBtn.setBackground(prod.disponible ? ChazApp.GREEN : new Color(60, 60, 70));
        addBtn.setForeground(Color.WHITE);
        addBtn.setBorder(new EmptyBorder(4, 12, 4, 12));
        addBtn.setFocusPainted(false);
        addBtn.setEnabled(prod.disponible);
        addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Carrito.agregar(prod, chazaNombre);
                addBtn.setText("\u2713");
                javax.swing.Timer t = new javax.swing.Timer(600, new ActionListener() {
                    public void actionPerformed(ActionEvent ev) { addBtn.setText("+"); }
                });
                t.setRepeats(false);
                t.start();
            }
        });

        right.add(badge);
        right.add(addBtn);
        card.add(right, BorderLayout.EAST);
        return card;
    }
}
