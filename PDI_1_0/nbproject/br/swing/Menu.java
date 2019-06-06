package swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameAdapter;
import swing.JImageWindow.Tipo;
import swing.janelas.TelaComparaLote;
import swing.janelas.PDI_Lote;
import swing.janelas.MorfologiaB;

@SuppressWarnings("serial")
public class Menu extends JMenuBar {

    private JMainFrame mainFrame;
    static public String textoImg = "";

    //********************
    // Cria o Menu
    //********************
    /**
     * Cria o Menu
     * @param mainFrame 
     */
    Menu(JMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.add(newMenu(1, "Opções do Arquivo"));
        //this.add(newMenu(2, "K-Means Imagem Cinza"));
        //this.add(newMenu(2, "Método de Greenness Index"));
        //this.add(newMenu(4, "APAGAR - Greenness Index em Lote"));
        this.add(newMenu(5, "Processamento em Lote"));
        this.add(newMenu(6, "Comparação em Lote"));
    }

    //********************
    // CRIA SUB-MENU
    //********************
    /**
     * Cria o Sub-Menu
     * 
     * @param pos
     * @param titulo
     * @return 
     */
    public JMenu newMenu(int pos, String titulo) {

        JMenu menu = new JMenu(titulo);
        

        switch (pos) {
            case 1:
                menu.add(itemAbrir());
                menu.add(itemSalvar());
                menu.addSeparator();
                menu.add(itemFechar());
                break;            
            case 5:
                menu.add(PDIEmLote());                
                menu.setEnabled(true);
                break;
            case 6:
                menu.add(itemLoteComparaGT());
                menu.setEnabled(true);
                break;
        }
        return menu;
    }

    //********************
    // Itens Menu Arquivo
    //********************
    public JImageWindow geraJanela(BufferedImage imagem, Tipo t, String titulo) {
        JImageWindow imgWindow = new JImageWindow(imagem, t);
        imgWindow.setTitle(titulo);
        imgWindow.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent e) {
                mainFrame.setSelected((JImageWindow) e.getSource());
                alterarMenu(true);
            }

            ;
						
            @Override
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) {
                JImageWindow closed = (JImageWindow) e.getSource();
                mainFrame.getDesktopPane().remove(closed);
                if (mainFrame.getDesktopPane().getComponentCount() == 0) {
                    alterarMenu(false);
                }
            }
        ;
        });
                            imgWindow.setVisible(true);
        if (mainFrame.getSelected() != null) {
            imgWindow.setLocation(mainFrame.getSelected().getLocation().x + 50,
                    mainFrame.getSelected().getLocation().y + 50);
        }
        mainFrame.getDesktopPane().add(imgWindow);
        try {
            imgWindow.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return imgWindow;
    }

    public JMenuItem itemLoteComparaGT() {
        JMenuItem item = new JMenuItem("Lote de Comparação");

        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TelaComparaLote tc = new TelaComparaLote(mainFrame);
                tc.setVisible(true);

            }
        });
        return item;
    }

    public JMenuItem itemMorfoBinaria() {
        JMenuItem item = new JMenuItem("Morfologia Binaria");
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                MorfologiaB m = new MorfologiaB(mainFrame);
                m.setVisible(true);
            }
        });
        return item;
    }        
    
    public JMenuItem PDIEmLote() {
        JMenuItem item = new JMenuItem("Processamento em Lote...");

        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PDI_Lote Wt = new PDI_Lote(mainFrame);
                Wt.setVisible(true);

            }
        });
        return item;
    }    

    public JMenuItem itemAbrir() {

        JMenuItem item = new JMenuItem("Abrir");

        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser("D:\\TrabDoc\\");
                if (chooser.showOpenDialog(mainFrame) != JFileChooser.APPROVE_OPTION) {
                    return;
                }

                try {
                    BufferedImage imagem = ImageIO.read(chooser.getSelectedFile());
                    JImageWindow imgWindow = geraJanela(imagem, Tipo.NORMAL, chooser.getSelectedFile().getName());
                    imgWindow.setSelected(true);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(mainFrame, "Falha ao carregar a imagem");
                    e1.printStackTrace();
                }
            }
        });

        return item;
    }

    public JMenuItem itemSalvar() {

        JMenuItem item = new JMenuItem("Salvar");

        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                File file = null;
                JFileChooser chooser = new JFileChooser();
                JavaFilter filter = new JavaFilter();
                chooser.setFileFilter(filter);
                if (chooser.showSaveDialog(mainFrame) != JFileChooser.APPROVE_OPTION) {
                    return;
                }

                try {
                    file = new File(chooser.getSelectedFile().getAbsolutePath() + ".png");
                    ImageIO.write(mainFrame.getSelected().getImage(), "PNG", file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        item.setEnabled(false);

        return item;
    }

    public JMenuItem itemFechar() {

        JMenuItem item = new JMenuItem("Fechar");

        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        return item;
    }    

    public void alterarMenu(boolean completo) {
        if (completo) {
            for (int i = 1; i < Menu.this.getMenuCount(); i++) {
                Menu.this.getMenu(i).setEnabled(true);
            }
            Menu.this.getMenu(0).getItem(1).setEnabled(true);
        } else {
            for (int i = 1; i < Menu.this.getMenuCount(); i++) {
                Menu.this.getMenu(i).setEnabled(true);
            }
            Menu.this.getMenu(0).getItem(1).setEnabled(false);

        }
    }

    public class JavaFilter extends javax.swing.filechooser.FileFilter {

        @Override
        public boolean accept(File f) {
            return f.getName().toLowerCase().endsWith(".png")
                    || f.isDirectory();
        }

        @Override
        public String getDescription() {
            return "PNG Files (*.png)";
        }
    }
}
