package swing;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

@SuppressWarnings("serial")
public class Dialog extends JDialog {
	private JRadioButton plainJRadioButton;
	private JRadioButton boldJRadioButton;
	private JRadioButton italicJRadioButton;
	private JRadioButton boldItalicJRadioButton;
	
	public Dialog() {
		JPanel panel=new JPanel();
        panel.add(new JLabel("Hello dialog"));
        this.getContentPane().add(panel);
	}
	
	public Dialog(JMainFrame mf,String title,boolean modal){
        super(mf,title,modal);
        this.setSize(500,500);
        JPanel panel=new JPanel();
        panel.setLayout(new FlowLayout());
        
        JButton btnFechar = new JButton("Fechar");
		btnFechar.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Dialog.this.dispose();
			}
		});
		panel.add(btnFechar);
		
		plainJRadioButton = new JRadioButton("Plain", true);
		boldJRadioButton = new JRadioButton("Bold", false);
		italicJRadioButton = new JRadioButton("Italic", false);
		boldItalicJRadioButton = new JRadioButton("Bold & Italic", false);
		panel.add(plainJRadioButton);
		panel.add(boldJRadioButton);
		panel.add(italicJRadioButton);
		panel.add(boldItalicJRadioButton);
        
        this.getContentPane().add(panel);
        this.setVisible(true);
    }
	
}
