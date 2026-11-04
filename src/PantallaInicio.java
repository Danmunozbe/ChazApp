// ── Pantalla 1: Inicio ───────────────────────────────────────────────────────
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

class PantallaInicio {

    static JPanel construir(final JPanel panelChazas) {
        JPanel p = UIHelper.darkPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 20, 10, 20);
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.gridx  = 0;

        // Logo UNAL
        ImageIcon logoIcon = UIHelper.loadImg("unal_logo.png", 110, 110);
        g.gridy = 0; g.insets = new Insets(30, 20, 10, 20);
        if (logoIcon != null) {
            JLabel logoLbl = new JLabel(logoIcon);
            logoLbl.setHorizontalAlignment(SwingConstants.CENTER);
            p.add(logoLbl, g);
        }

        // Título
        JLabel t1 = UIHelper.label("Chaz", 42, Font.BOLD, ChazApp.TEXT);
        JLabel t2 = UIHelper.label("App",  42, Font.BOLD, ChazApp.ACCENT);
        JPanel tRow = UIHelper.darkPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tRow.add(t1); tRow.add(t2);
        g.gridy = 1; g.insets = new Insets(6, 20, 2, 20);
        p.add(tRow, g);

        JLabel sub = UIHelper.label("Puestos de comida \u00b7 UNAL", 13, Font.PLAIN, ChazApp.TEXT_DIM);
        g.gridy = 2; g.insets = new Insets(0, 20, 30, 20);
        p.add(sub, g);

        // Selector de modo
        JLabel lbl = UIHelper.label("Selecciona el modo:", 13, Font.PLAIN, ChazApp.TEXT_DIM);
        g.gridy = 3; g.insets = new Insets(0, 20, 6, 20);
        p.add(lbl, g);

        final String[] modos = {
            "\u2014 elige un modo \u2014",
            "\uD83D\uDED2  Modo Comprador",
            "\uD83C\uDFEA  Modo Vendedor"
        };
        final JComboBox<String> combo = new JComboBox<>(modos);
        UIHelper.styleCombo(combo);
        g.gridy = 4; g.insets = new Insets(0, 20, 20, 20);
        p.add(combo, g);

        // Botón Ingresar
        final JButton btn = UIHelper.accentButton("Ingresar  \u2192");
        btn.setEnabled(false);
        g.gridy = 5; g.insets = new Insets(0, 20, 10, 20);
        p.add(btn, g);

        combo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btn.setEnabled(combo.getSelectedIndex() > 0);
            }
        });

        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (combo.getSelectedIndex() == 1) {
                    if (panelChazas.getComponentCount() == 0) {
                        PantallaChazas.llenar(panelChazas);
                    }
                    ChazApp.cards.show(ChazApp.root, "chazas");
                } else if (combo.getSelectedIndex() == 2) {
                     // Conexión con el modo vendedor
                     PantallaVendedor.abrir();
                }
            }
        });

        return p;
    }
}
