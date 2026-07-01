package vista;

import vista.componentes.*;
import javax.swing.*;
import java.awt.*;

public class VistaConfiguracion extends JPanel {

    public VistaConfiguracion() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel header = new JPanel(new BorderLayout(15, 0));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel tituloPanel = new JPanel();
        tituloPanel.setLayout(new BoxLayout(tituloPanel, BoxLayout.Y_AXIS));
        tituloPanel.setOpaque(false);

        JLabel titulo = new JLabel("Configuración");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel subtitulo = new JLabel("Ajustes generales del sistema y preferencias de usuario");
        subtitulo.setForeground(SenaColores.TEXTO_SECUNDARIO);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        tituloPanel.add(titulo);
        tituloPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        tituloPanel.add(subtitulo);

        header.add(tituloPanel, BorderLayout.WEST);

        PanelRedondeado body = new PanelRedondeado(new Color(15, 23, 42, 200), 18);
        body.setLayout(new BorderLayout());
        
        JLabel lblEnConstruccion = new JLabel("El módulo de configuración estará disponible en futuras actualizaciones.", SwingConstants.CENTER);
        lblEnConstruccion.setForeground(new Color(148, 163, 184));
        lblEnConstruccion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        body.add(lblEnConstruccion, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);
    }
}
