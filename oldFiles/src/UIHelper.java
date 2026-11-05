// ── Helpers de UI ─────────────────────────────────────────────────────────────
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

class UIHelper {

    static JLabel label(String txt, int size, int style, Color color) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("SansSerif", style, size));
        l.setForeground(color);
        return l;
    }

    static JPanel darkPanel(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setBackground(ChazApp.BG);
        return p;
    }

    static JButton accentButton(String txt) {
        JButton b = new JButton(txt);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setBackground(ChazApp.ACCENT);
        b.setForeground(Color.WHITE);
        b.setBorder(new EmptyBorder(10, 20, 10, 20));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    static void styleSecBtn(JButton b) {
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setBackground(ChazApp.SURFACE);
        b.setForeground(ChazApp.TEXT);
        b.setBorder(new EmptyBorder(6, 12, 6, 12));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    static JButton smallBtn(String txt) {
        JButton b = new JButton(txt);
        b.setFont(new Font("SansSerif", Font.BOLD, 15));
        b.setBackground(new Color(55, 55, 70));
        b.setForeground(ChazApp.TEXT);
        b.setBorder(new EmptyBorder(2, 10, 2, 10));
        b.setFocusPainted(false);
        return b;
    }

    static void styleCombo(JComboBox<String> cb) {
        cb.setBackground(ChazApp.SURFACE);
        cb.setForeground(ChazApp.TEXT);
        cb.setFont(new Font("SansSerif", Font.PLAIN, 14));
    }

    static JPanel topBar(String modo) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        bar.setBackground(new Color(25, 25, 35));
        bar.add(label("ChazApp", 15, Font.BOLD,  ChazApp.TEXT));
        bar.add(label("\u00b7",  15, Font.PLAIN, ChazApp.TEXT_DIM));
        bar.add(label(modo,      13, Font.BOLD,  ChazApp.ACCENT));
        bar.add(label("\u00b7",  15, Font.PLAIN, ChazApp.TEXT_DIM));
        bar.add(label("UNAL",    13, Font.PLAIN, ChazApp.TEXT_DIM));
        return bar;
    }

    static String formatPrecio(double precio) {
        return String.format("$ %,.0f", precio).replace(",", ".");
    }

    // Carga una imagen de img/ y la escala. Devuelve null si no existe.
    static ImageIcon loadImg(String nombre, int w, int h) {
        try {
            java.io.File f = new java.io.File(ChazApp.IMG_DIR + nombre);
            if (!f.exists()) return null;
            ImageIcon raw = new ImageIcon(f.getAbsolutePath());
            Image scaled = raw.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) { return null; }
    }
}
