// ── Carrito de compras ───────────────────────────────────────────────────────
// Lógica del carrito (agregar, actualizar cantidad) y diálogo de pedido.

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

class Carrito {

    // Agrega un producto al carrito global. Si ya existe, suma la cantidad.
    static void agregar(Producto prod, String chaza) {
        for (int i = 0; i < ChazApp.carrito.size(); i++) {
            ItemCarrito item = ChazApp.carrito.get(i);
            if (item.p.id == prod.id) {
                ChazApp.carrito.set(i, new ItemCarrito(item.p, item.chaza, item.cantidad + 1));
                return;
            }
        }
        ChazApp.carrito.add(new ItemCarrito(prod, chaza, 1));
    }

    // Abre el diálogo del carrito mostrando items, cantidades y total
    static void abrir(final Component parent, final JButton originBtn) {
        final JDialog dlg = new JDialog(ChazApp.ventana, "\uD83D\uDED2 Carrito", true);
        dlg.setSize(420, 580);
        dlg.setLocationRelativeTo(parent);

        JPanel main = UIHelper.darkPanel(new BorderLayout());

        JLabel titulo = UIHelper.label("Tu pedido", 18, Font.BOLD, ChazApp.TEXT);
        titulo.setBorder(new EmptyBorder(16, 16, 10, 16));
        main.add(titulo, BorderLayout.NORTH);

        // ── Lista de items ────────────────────────────────────────────────────
        JPanel lista = UIHelper.darkPanel(new GridLayout(0, 1, 0, 8));
        lista.setBorder(new EmptyBorder(8, 12, 8, 12));

        if (ChazApp.carrito.isEmpty()) {
            lista.add(UIHelper.label("El carrito est\u00e1 vac\u00edo \uD83D\uDE36", 14, Font.PLAIN, ChazApp.TEXT_DIM));
        } else {
            for (int i = 0; i < ChazApp.carrito.size(); i++) {
                final int idx = i;
                final ItemCarrito item = ChazApp.carrito.get(i);

                JPanel row = new JPanel(new BorderLayout(8, 0));
                row.setBackground(ChazApp.SURFACE);
                row.setBorder(new CompoundBorder(
                    new LineBorder(new Color(55, 55, 70), 1, true),
                    new EmptyBorder(8, 10, 8, 10)
                ));

                // Info del producto
                JPanel info = new JPanel(new GridLayout(3, 1, 0, 2));
                info.setBackground(ChazApp.SURFACE);
                info.add(UIHelper.label(item.p.nombre, 13, Font.BOLD, ChazApp.TEXT));
                info.add(UIHelper.label(item.chaza, 11, Font.PLAIN, ChazApp.TEXT_DIM));
                info.add(UIHelper.label(UIHelper.formatPrecio(item.p.precio * item.cantidad), 13, Font.BOLD, ChazApp.ACCENT));
                row.add(info, BorderLayout.CENTER);

                // Controles − cantidad +
                JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
                ctrl.setBackground(ChazApp.SURFACE);
                JButton menos = UIHelper.smallBtn("\u2212");
                JLabel cant   = UIHelper.label(String.valueOf(item.cantidad), 13, Font.BOLD, ChazApp.TEXT);
                JButton mas   = UIHelper.smallBtn("+");

                menos.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (item.cantidad == 1) ChazApp.carrito.remove(idx);
                        else ChazApp.carrito.set(idx, new ItemCarrito(item.p, item.chaza, item.cantidad - 1));
                        dlg.dispose();
                        abrir(parent, originBtn);
                    }
                });
                mas.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ChazApp.carrito.set(idx, new ItemCarrito(item.p, item.chaza, item.cantidad + 1));
                        dlg.dispose();
                        abrir(parent, originBtn);
                    }
                });

                ctrl.add(menos); ctrl.add(cant); ctrl.add(mas);
                row.add(ctrl, BorderLayout.EAST);
                lista.add(row);
            }
        }

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(ChazApp.BG);
        main.add(scroll, BorderLayout.CENTER);

        // ── Footer: total + botón pedido ──────────────────────────────────────
        JPanel footer = UIHelper.darkPanel(new BorderLayout(0, 8));
        footer.setBorder(new EmptyBorder(10, 16, 16, 16));

        double total = 0;
        for (ItemCarrito i : ChazApp.carrito) total += i.p.precio * i.cantidad;
        footer.add(UIHelper.label("Total:  " + UIHelper.formatPrecio(total), 16, Font.BOLD, ChazApp.TEXT),
                   BorderLayout.NORTH);

        JButton pedidoBtn = UIHelper.accentButton("Realizar pedido \u2713");
        pedidoBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ChazApp.carrito.isEmpty()) {
                    JOptionPane.showMessageDialog(dlg,
                        "El carrito est\u00e1 vac\u00edo.", "ChazApp", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dlg,
                        "\u00a1Pedido recibido! \uD83C\uDF89\nFuncionalidad de pago pr\u00f3ximamente.",
                        "ChazApp", JOptionPane.INFORMATION_MESSAGE);
                    ChazApp.carrito.clear();
                    dlg.dispose();
                }
            }
        });
        footer.add(pedidoBtn, BorderLayout.SOUTH);
        main.add(footer, BorderLayout.SOUTH);

        dlg.setContentPane(main);
        dlg.setVisible(true);
    }
}
