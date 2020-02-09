package Vista;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FrameMessage extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6609453882836520612L;
	
	static final int YES_OPTION = 1; 
	private JPanel contentPane;
	JPanel panel = new JPanel();
	VentanaPrincipal vista;

	/**
	 * Create the frame.
	 */
	public FrameMessage(VentanaPrincipal vista,String mensajeLabel,String mensajeBotonAfirmativo) {
		createFrame(mensajeLabel);
		JButton btnAfirmativo = new JButton(mensajeBotonAfirmativo);
		panel.add(btnAfirmativo, "cell 1 2,alignx center,aligny center");
		btnAfirmativo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
	}
		
	private void createFrame(String mensajeLabel) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 183, 147);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[]", "[][]"));
		JLabel lblNewLabel = new JLabel(mensajeLabel);
		panel.add(lblNewLabel, "cell 1 1,alignx center,aligny center");
		setVisible(true);
		setMinimumSize(new Dimension(lblNewLabel.getSize().width+40,140));	}
}