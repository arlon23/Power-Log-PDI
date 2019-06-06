package swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;

import swing.JImageWindow.Tipo;

@SuppressWarnings("serial")
public class JMainFrame extends JFrame {
	private JDesktopPane desktopPane;
	private JPanel pnlButtons;
	private JButton btnAbrir;
	private JImageWindow selected = null;
	private Menu menu;
        
	
	public JMainFrame() {
		setSize(800, 600);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		add(getDesktopPane(), BorderLayout.CENTER);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setTitle("Processamento Digital de Imagens");
		//add(getPnlButtons(), BorderLayout.SOUTH);
		menu = new Menu(this);
		setJMenuBar(menu);
		
		Conf.kmeans_classes = 2;
		Conf.color_spaces = 0;
		Conf.feature_color = true;
		Conf.feature_media = false;
		Conf.feature_variancia = false;
		Conf.feature_skewness = false;
		Conf.feature_curtose = false;
	}
	
	public void setSelected(JImageWindow imageWindow) {
		selected = imageWindow;
	}
	
	public JImageWindow getSelected() {
		return selected;
	}

	public JDesktopPane getDesktopPane() {
		if (desktopPane != null)
			return desktopPane;
		
		desktopPane = new JDesktopPane();
		
		desktopPane.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				menu.alterarMenu(false);
			}
		});
		
		return desktopPane;
	}
	
	public JPanel getPnlButtons() {
		if (pnlButtons != null)
			return pnlButtons;
		
		pnlButtons = new JPanel();
		pnlButtons.setLayout(new FlowLayout());
		pnlButtons.add(getBtnAbrir());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return pnlButtons;
	}
	
	public JButton getBtnAbrir() {
		if (btnAbrir != null)
			return btnAbrir;
		
		btnAbrir = new JButton("Abrir");
		btnAbrir.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				if (chooser.showOpenDialog(JMainFrame.this) != JFileChooser.APPROVE_OPTION)
					return;
				
				try {
					BufferedImage imagem = ImageIO.read(chooser.getSelectedFile());
					JImageWindow imgWindow = new JImageWindow(imagem, Tipo.NORMAL);
					imgWindow.addInternalFrameListener(new InternalFrameAdapter() {
						public void internalFrameActivated(javax.swing.event.InternalFrameEvent e) {
							selected = (JImageWindow) e.getSource();
						};
						
						public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) {
							JImageWindow closed = (JImageWindow) e.getSource();
							getDesktopPane().remove(closed);
						};
					});
					
					imgWindow.setVisible(true);
					getDesktopPane().add(imgWindow);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(JMainFrame.this, "Falha ao carregar a imagem");
					e.printStackTrace();
				}
			}
		});
		return btnAbrir;
	}
	
	public static void main(String[] args) {
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new JMainFrame().setVisible(true);
			}
		});
	}
}
