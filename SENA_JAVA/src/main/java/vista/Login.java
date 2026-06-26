package vista;

import vista.componentes.BotonPlano;
import vista.componentes.CampoTextoModerno;
import vista.componentes.SenaColores;
import vista.componentes.PanelParticulasAnimadas;
import vista.componentes.PanelCristal;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Login extends JFrame {

    private PanelParticulasAnimadas fondoAnimado;

    private CampoTextoModerno txtLogDoc;
    private vista.componentes.CampoPasswordModerno txtLogPass;
    private BotonPlano btnLogin;

    private CampoTextoModerno txtRegName;
    private JComboBox<String> cmbRegTipo;
    private CampoTextoModerno txtRegNum;
    private CampoTextoModerno txtRegEmail;
    private CampoTextoModerno txtRegPhone;
    private vista.componentes.CampoPasswordModerno txtRegPass;
    private BotonPlano btnReg;

    public Login() {
        setTitle("BIBLIOTECA & ALMACÉN SENA - Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600));

        fondoAnimado = new PanelParticulasAnimadas();
        fondoAnimado.setLayout(null);

        CardLayout cardLayout = new CardLayout();
        JPanel cardsContainer = new JPanel(cardLayout);
        cardsContainer.setOpaque(false);

        PanelCristal tarjetaLogin = crearTarjetaLogin(cardLayout, cardsContainer);
        JPanel pnlLoginWrapper = new JPanel(new GridBagLayout());
        pnlLoginWrapper.setOpaque(false);
        pnlLoginWrapper.add(tarjetaLogin);

        PanelCristal tarjetaRegistro = crearTarjetaRegistro(cardLayout, cardsContainer);
        JPanel pnlRegistroWrapper = new JPanel(new GridBagLayout());
        pnlRegistroWrapper.setOpaque(false);
        pnlRegistroWrapper.add(tarjetaRegistro);

        PanelInicio pnlInicio = new PanelInicio(cardLayout, cardsContainer);

        cardsContainer.add(pnlLoginWrapper, "Login");
        cardsContainer.add(pnlRegistroWrapper, "Registro");
        cardsContainer.add(pnlInicio, "Inicio");

        fondoAnimado.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                cardsContainer.setBounds(0, 0, fondoAnimado.getWidth(), fondoAnimado.getHeight());
            }
        });

        fondoAnimado.add(cardsContainer);
        setContentPane(fondoAnimado);
        cardLayout.show(cardsContainer, "Inicio");
    }
    
    // GETTERS PARA EL CONTROLADOR
    public BotonPlano getBtnLogin() { return btnLogin; }
    public BotonPlano getBtnReg() { return btnReg; }
    public CampoTextoModerno getTxtLogDoc() { return txtLogDoc; }
    public vista.componentes.CampoPasswordModerno getTxtLogPass() { return txtLogPass; }
    
    public CampoTextoModerno getTxtRegName() { return txtRegName; }
    public JComboBox<String> getCmbRegTipo() { return cmbRegTipo; }
    public CampoTextoModerno getTxtRegNum() { return txtRegNum; }
    public CampoTextoModerno getTxtRegEmail() { return txtRegEmail; }
    public CampoTextoModerno getTxtRegPhone() { return txtRegPhone; }
    public vista.componentes.CampoPasswordModerno getTxtRegPass() { return txtRegPass; }

    public void mostrarLogin() {
        CardLayout cl = (CardLayout) ((JPanel)fondoAnimado.getComponent(0)).getLayout();
        cl.show((JPanel)fondoAnimado.getComponent(0), "Login");
    }

    private PanelCristal crearTarjetaLogin(CardLayout cardLayout, JPanel cardsContainer) {
        PanelCristal tarjeta = new PanelCristal();
        tarjeta.setLayout(new GridBagLayout());
        tarjeta.setPreferredSize(new Dimension(420, 580));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 40, 5, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        int gridy = 0;
        gbc.gridy = gridy++;
        tarjeta.add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        try {
            URL urlLogo = getClass().getResource("/imagenes/logo_sena_trans.png");
            if (urlLogo != null) {
                BufferedImage img = ImageIO.read(urlLogo);
                Image scaled = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                JLabel lblLogo = new JLabel(new ImageIcon(scaled));
                lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
                gbc.gridy = gridy++;
                tarjeta.add(lblLogo, gbc);
            }
        } catch (Exception e) {}

        JLabel lblTitulo = new JLabel("INICIAR SESIÓN", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(SenaColores.VERDE_SENA);
        gbc.gridy = gridy++;
        tarjeta.add(lblTitulo, gbc);

        JLabel lblSub = new JLabel("Ingresa tus credenciales de acceso", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(SenaColores.TEXTO_SECUNDARIO);
        gbc.gridy = gridy++;
        tarjeta.add(lblSub, gbc);

        gbc.gridy = gridy++;
        tarjeta.add(Box.createRigidArea(new Dimension(0, 25)), gbc);

        JLabel lblDoc = new JLabel("Número de Documento");
        lblDoc.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDoc.setForeground(Color.WHITE);
        gbc.gridy = gridy++;
        gbc.insets = new Insets(5, 40, 0, 40);
        tarjeta.add(lblDoc, gbc);

        txtLogDoc = new CampoTextoModerno("Ingresa tu documento", 1);
        txtLogDoc.setPreferredSize(new Dimension(300, 45));
        gbc.gridy = gridy++;
        gbc.insets = new Insets(5, 40, 15, 40);
        tarjeta.add(txtLogDoc, gbc);

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(Color.WHITE);
        gbc.gridy = gridy++;
        gbc.insets = new Insets(5, 40, 0, 40);
        tarjeta.add(lblPass, gbc);

        txtLogPass = new vista.componentes.CampoPasswordModerno("Tu contraseña");
        txtLogPass.setPreferredSize(new Dimension(300, 45));
        gbc.gridy = gridy++;
        gbc.insets = new Insets(5, 40, 5, 40);
        tarjeta.add(txtLogPass, gbc);

        JLabel lblForgot = new JLabel("¿Olvidaste tu contraseña?");
        lblForgot.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblForgot.setForeground(SenaColores.VERDE_SENA);
        lblForgot.setHorizontalAlignment(SwingConstants.RIGHT);
        lblForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 40, 20, 40);
        tarjeta.add(lblForgot, gbc);

        btnLogin = new BotonPlano("Ingresar a la Plataforma");
        btnLogin.setPreferredSize(new Dimension(300, 45));
        btnLogin.setActionCommand("Ingresar");
        gbc.gridy = gridy++;
        gbc.insets = new Insets(5, 40, 20, 40);
        tarjeta.add(btnLogin, gbc);

        JLabel lblReg = new JLabel("¿No tienes cuenta? Regístrate aquí", SwingConstants.CENTER);
        lblReg.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblReg.setForeground(SenaColores.TEXTO_SECUNDARIO);
        lblReg.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblReg.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardsContainer, "Registro");
            }
        });
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 40, 8, 40);
        tarjeta.add(lblReg, gbc);

        JLabel lblHome1 = new JLabel("Volver al Inicio", SwingConstants.CENTER);
        lblHome1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblHome1.setForeground(Color.WHITE);
        lblHome1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblHome1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardsContainer, "Inicio");
            }
        });
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 40, 0, 40);
        tarjeta.add(lblHome1, gbc);

        gbc.gridy = gridy++;
        gbc.weighty = 1.0;
        tarjeta.add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        return tarjeta;
    }

    private PanelCristal crearTarjetaRegistro(CardLayout cardLayout, JPanel cardsContainer) {
        PanelCristal tarjeta = new PanelCristal();
        tarjeta.setLayout(new GridBagLayout());
        tarjeta.setPreferredSize(new Dimension(450, 680));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 40, 5, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;

        int gridy = 0;
        gbc.gridy = gridy++;
        tarjeta.add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        try {
            URL urlLogo = getClass().getResource("/imagenes/logo_sena_trans.png");
            if (urlLogo != null) {
                BufferedImage img = ImageIO.read(urlLogo);
                Image scaled = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                JLabel lblLogo = new JLabel(new ImageIcon(scaled));
                lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
                gbc.gridy = gridy++;
                tarjeta.add(lblLogo, gbc);
            }
        } catch (Exception e) {}

        JLabel lblTitulo = new JLabel("CREAR CUENTA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(SenaColores.VERDE_SENA);
        gbc.gridy = gridy++;
        tarjeta.add(lblTitulo, gbc);

        JLabel lblSub = new JLabel("Completa tus datos de registro", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(SenaColores.TEXTO_SECUNDARIO);
        gbc.gridy = gridy++;
        tarjeta.add(lblSub, gbc);

        gbc.gridy = gridy++;
        tarjeta.add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        JLabel lblName = new JLabel("Nombre Completo");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblName.setForeground(Color.WHITE);
        gbc.gridy = gridy++;
        gbc.insets = new Insets(2, 40, 0, 40);
        tarjeta.add(lblName, gbc);

        txtRegName = new CampoTextoModerno("Tu nombre completo", 1);
        txtRegName.setPreferredSize(new Dimension(300, 45));
        gbc.gridy = gridy++;
        gbc.insets = new Insets(2, 40, 10, 40);
        tarjeta.add(txtRegName, gbc);

        JPanel pnlDoc = new JPanel(new BorderLayout(10, 0));
        pnlDoc.setOpaque(false);

        JPanel pnlTipo = new JPanel(new BorderLayout());
        pnlTipo.setOpaque(false);
        JLabel lblTipo = new JLabel("Tipo");
        lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTipo.setForeground(Color.WHITE);
        cmbRegTipo = new JComboBox<>(new String[]{"CC", "TI", "CE"});
        cmbRegTipo.setPreferredSize(new Dimension(80, 45));
        cmbRegTipo.setBackground(new Color(25, 30, 40));
        cmbRegTipo.setForeground(Color.WHITE);
        pnlTipo.add(lblTipo, BorderLayout.NORTH);
        pnlTipo.add(cmbRegTipo, BorderLayout.CENTER);

        JPanel pnlNum = new JPanel(new BorderLayout());
        pnlNum.setOpaque(false);
        JLabel lblNum = new JLabel("Número de Documento");
        lblNum.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNum.setForeground(Color.WHITE);
        txtRegNum = new CampoTextoModerno("Ej. 1098...", 2);
        txtRegNum.setPreferredSize(new Dimension(200, 45));
        pnlNum.add(lblNum, BorderLayout.NORTH);
        pnlNum.add(txtRegNum, BorderLayout.CENTER);

        pnlDoc.add(pnlTipo, BorderLayout.WEST);
        pnlDoc.add(pnlNum, BorderLayout.CENTER);

        gbc.gridy = gridy++;
        gbc.insets = new Insets(2, 40, 10, 40);
        tarjeta.add(pnlDoc, gbc);

        JLabel lblEmail = new JLabel("Correo Institucional");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEmail.setForeground(Color.WHITE);
        gbc.gridy = gridy++;
        gbc.insets = new Insets(2, 40, 0, 40);
        tarjeta.add(lblEmail, gbc);

        txtRegEmail = new CampoTextoModerno("usuario@mi.sena.edu.co", 3);
        txtRegEmail.setPreferredSize(new Dimension(300, 45));
        gbc.gridy = gridy++;
        gbc.insets = new Insets(2, 40, 10, 40);
        tarjeta.add(txtRegEmail, gbc);

        JLabel lblPhone = new JLabel("Número de Teléfono");
        lblPhone.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPhone.setForeground(Color.WHITE);
        gbc.gridy = gridy++;
        gbc.insets = new Insets(2, 40, 0, 40);
        tarjeta.add(lblPhone, gbc);

        txtRegPhone = new CampoTextoModerno("Ej. 3001234567", 4);
        txtRegPhone.setPreferredSize(new Dimension(300, 45));
        gbc.gridy = gridy++;
        gbc.insets = new Insets(2, 40, 10, 40);
        tarjeta.add(txtRegPhone, gbc);

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(Color.WHITE);
        gbc.gridy = gridy++;
        gbc.insets = new Insets(2, 40, 0, 40);
        tarjeta.add(lblPass, gbc);

        txtRegPass = new vista.componentes.CampoPasswordModerno("Crea una contraseña");
        txtRegPass.setPreferredSize(new Dimension(300, 45));
        gbc.gridy = gridy++;
        gbc.insets = new Insets(2, 40, 20, 40);
        tarjeta.add(txtRegPass, gbc);

        btnReg = new BotonPlano("Registrarse ahora");
        btnReg.setPreferredSize(new Dimension(300, 45));
        btnReg.setActionCommand("Registrar");
        gbc.gridy = gridy++;
        gbc.insets = new Insets(5, 40, 20, 40);
        tarjeta.add(btnReg, gbc);

        JLabel lblLogin = new JLabel("<html>¿Ya tienes cuenta? <font color='#39A900'>Iniciar sesión</font></html>", SwingConstants.CENTER);
        lblLogin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLogin.setForeground(SenaColores.TEXTO_SECUNDARIO);
        lblLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLogin.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardsContainer, "Login");
            }
        });
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 40, 8, 40);
        tarjeta.add(lblLogin, gbc);

        JLabel lblHome2 = new JLabel("Volver al inicio", SwingConstants.CENTER);
        lblHome2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblHome2.setForeground(Color.WHITE);
        lblHome2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblHome2.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardsContainer, "Inicio");
            }
        });
        gbc.gridy = gridy++;
        gbc.insets = new Insets(0, 40, 0, 40);
        tarjeta.add(lblHome2, gbc);

        gbc.gridy = gridy++;
        gbc.weighty = 1.0;
        tarjeta.add(Box.createRigidArea(new Dimension(0, 10)), gbc);

        return tarjeta;
    }

    private Image makeColorTransparent(BufferedImage im, final Color color) {
        ImageFilter filter = new RGBImageFilter() {
            public int markerRGB = color.getRGB() | 0xFF000000;
            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    return 0x00FFFFFF & rgb;
                }
                return rgb;
            }
        };
        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    public static void main(String[] args) {
        try {
            com.formdev.flatlaf.FlatDarkLaf.setup();
            UIManager.put("Component.arc", 10);
            UIManager.put("Button.arc", 10);
            UIManager.put("TextComponent.arc", 10);
        } catch (Exception e) {}

        SwingUtilities.invokeLater(() -> {
            Login vistaLogin = new Login();
            controlador.ControladorLogin controlador = new controlador.ControladorLogin(vistaLogin);
            controlador.iniciar();
        });
    }
}
