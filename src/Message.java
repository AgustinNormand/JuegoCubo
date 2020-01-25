import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;

public class Message extends JFrame {
	
	private boolean response = null;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public Message(String mensajeLabel,String mensajeBotonAfirmativo,String mensajeBotonNegativo) {
		createFrame(mesajeLabel);
		JButton btnAfirmativo = new JButton(mensajeBotonAfirmatvo);
		panel.add(btnAfirmativo, "cell 1 2,alignx center,aligny center");
		btnAfirmativo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				response = true;
			}
		});
		JButton btnNegativo = new JButton(mensajeBotonNegativo);
		panel.add(btnNegativo, "cell 1 2,alignx center,aligny center");
		btnNegativo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					response = false;
			}
		});
	}
	public Message(String mensajeLabel,String mensajeBoton) {
		createFrame(mesajeLabel);
		JButton btnSalir = new JButton(mensajeBoton);
		panel.add(btnSalir, "cell 1 2,alignx center,aligny center");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					dispose();
			}
		});
	}
		
}
	private void createFrame(String mesajeLabel) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 183, 147);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[]", "[][]"));
		
		JLabel lblNewLabel = new JLabel(mensajeLabel);
		panel.add(lblNewLabel, "cell 1 1,alignx center,aligny center");
		
	}
	
	public boolean getResponse() {
		boolean aux = response;
		response = null;
		return aux;
	}

}