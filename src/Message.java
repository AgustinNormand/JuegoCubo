import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Controlador.IVista;
import net.miginfocom.swing.MigLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;

public class Message extends JFrame {
	static final int YES_OPTION = 1; 
	private boolean updatedResponse = false;
	private boolean response;
	private JPanel contentPane;
	JPanel panel = new JPanel();
	VentanaPrincipal vista;

	/**
	 * Create the frame.
	 */
	public Message(VentanaPrincipal vista,String mensajeLabel,String mensajeBotonAfirmativo) {
		createFrame(mensajeLabel);
		JButton btnAfirmativo = new JButton(mensajeBotonAfirmativo);
		panel.add(btnAfirmativo, "cell 1 2,alignx center,aligny center");
		btnAfirmativo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				vista.cartasVistas();
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
		
	}
}