package vista.componentes;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class TablaModerna {

    public static JScrollPane crear(JTable tabla) {
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(57, 169, 0));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 42));

        tabla.setBackground(new Color(30, 41, 59));
        tabla.setForeground(new Color(248, 250, 252));
        tabla.setSelectionBackground(new Color(57, 169, 0, 60));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setGridColor(new Color(255, 255, 255, 15));
        tabla.setRowHeight(40);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        tabla.setIntercellSpacing(new Dimension(0, 1));

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(30, 41, 59) : new Color(25, 35, 52));
                    c.setForeground(new Color(248, 250, 252));
                }
                setBorder(new javax.swing.border.EmptyBorder(0, 12, 0, 12));
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(new Color(30, 41, 59));
        return scroll;
    }
}
