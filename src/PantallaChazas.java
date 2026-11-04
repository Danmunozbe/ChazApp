// ── Pantalla 2: Lista de chazas ───────────────────────────────────────────────
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

class PantallaChazas {

    static void llenar(final JPanel wrapper) {
        wrapper.setLayout(new BorderLayout());
        wrapper.add(UIHelper.topBar("MODO COMPRADOR"), BorderLayout.NORTH);

        JPanel center = UIHelper.darkPanel(new BorderLayout());
        JLabel tit = UIHelper.label("\u00bfQu\u00e9 se te antoja? \uD83C\uDF7D\uFE0F", 20, Font.BOLD, ChazApp.TEXT);
        tit.setBorder(new EmptyBorder(16, 16, 10, 16));
        center.add(tit, BorderLayout.NORTH);

        JPanel lista = UIHelper.darkPanel(new GridLayout(0, 1, 0, 12));
        lista.setBorder(new EmptyBorder(0, 14, 14, 14));

        List<Chaza> chazas = BaseDatos.queryChazas();
        String[] imgNames = {"burritos_banner.jpg", "bowls_banner.png", "salchipapas_banner.png"};

        if (chazas.isEmpty()) {
            JLabel err = UIHelper.label("No se pudo conectar a la base de datos.", 13, Font.PLAIN, ChazApp.RED_BADGE);
            err.setBorder(new EmptyBorder(20, 20, 20, 20));
            lista.add(err);
            lista.add(UIHelper.label("Ruta buscada: " + ChazApp.DB, 11, Font.PLAIN, ChazApp.TEXT_DIM));
        } else {
            for (int i = 0; i < chazas.size(); i++) {
                String img = i < imgNames.length ? imgNames[i] : null;
                lista.add(tarjeta(chazas.get(i), img));
            }
        }

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(ChazApp.BG);
        center.add(scroll, BorderLayout.CENTER);
        wrapper.add(center, BorderLayout.CENTER);

        // Botón flotante del carrito
        final JButton fab = new JButton("\uD83D\uDED2  Ver carrito");
        fab.setFont(new Font("SansSerif", Font.BOLD, 13));
        fab.setBackground(ChazApp.ACCENT);
        fab.setForeground(Color.WHITE);
        fab.setBorder(new EmptyBorder(11, 22, 11, 22));
        fab.setFocusPainted(false);
        fab.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        fab.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { Carrito.abrir(wrapper, fab); }
        });
        JPanel fabPanel = UIHelper.darkPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        fabPanel.add(fab);
        wrapper.add(fabPanel, BorderLayout.SOUTH);

        wrapper.revalidate();
        wrapper.repaint();
    }

    static JPanel tarjeta(final Chaza c, String imgName) {
        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(ChazApp.SURFACE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(55, 55, 70), 1, true),
            new EmptyBorder(0, 0, 0, 0)
        ));

        // Banner de imagen
        if (imgName != null) {
            ImageIcon banner = UIHelper.loadImg(imgName, 440, 130);
            if (banner != null) {
                JLabel imgLbl = new JLabel(banner);
                imgLbl.setPreferredSize(new Dimension(440, 130));
                card.add(imgLbl, BorderLayout.NORTH);
            }
        }

        // Contenido
        JPanel body = new JPanel(new BorderLayout(0, 6));
        body.setBackground(ChazApp.SURFACE);
        body.setBorder(new EmptyBorder(10, 14, 10, 14));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        top.setBackground(ChazApp.SURFACE);
        top.add(UIHelper.label(c.nombre, 16, Font.BOLD, ChazApp.TEXT));
        JLabel badge = UIHelper.label(" Abierta ", 11, Font.BOLD, Color.WHITE);
        badge.setOpaque(true);
        badge.setBackground(ChazApp.GREEN);
        badge.setBorder(new EmptyBorder(2, 6, 2, 6));
        top.add(badge);
        body.add(top, BorderLayout.NORTH);

        JPanel mid = new JPanel(new GridLayout(0, 1, 0, 3));
        mid.setBackground(ChazApp.SURFACE);
        mid.add(UIHelper.label("<html><body style='width:280px'>" + c.descripcion + "</body></html>", 12, Font.PLAIN, ChazApp.TEXT_DIM));
        mid.add(UIHelper.label("\uD83D\uDCCD " + c.ubicacion, 11, Font.PLAIN, ChazApp.TEXT_DIM));
        mid.add(UIHelper.label("\uD83D\uDED2 " + c.disponibles + " productos disponibles", 11, Font.PLAIN, ChazApp.GREEN));
        body.add(mid, BorderLayout.CENTER);

        JButton btn = UIHelper.accentButton("Ver men\u00fa  \u2192");
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        south.setBackground(ChazApp.SURFACE);
        south.add(btn);
        body.add(south, BorderLayout.SOUTH);

        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { MenuChaza.abrir(c); }
        });

        card.add(body, BorderLayout.CENTER);
        return card;
    }
}
