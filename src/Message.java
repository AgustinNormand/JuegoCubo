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
	static final int YES_OPTION = 1; 
	private boolean updatedResponse = false;
	private boolean response;
	private JPanel contentPane;
	JPanel panel = new JPanel();

	/**
	 * Create the frame.
	 */
	public Message(String mensajeLabel,String mensajeBotonAfirmativo,String mensajeBotonNegativo) {
		createFrame(mensajeLabel);
		JButton btnAfirmativo = new JButton(mensajeBotonAfirmativo);
		panel.add(btnAfirmativo, "cell 1 2,alignx center,aligny center");
		btnAfirmativo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				response = true;
				setVisible(false);
				updatedResponse = true;
			}
		});
		JButton btnNegativo = new JButton(mensajeBotonNegativo);
		panel.add(btnNegativo, "cell 1 2,alignx center,aligny center");
		btnNegativo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					response = false;
					setVisible(false);
					updatedResponse = true;
			}
		});
	}
	public Message(String mensajeLabel,String mensajeBoton) {
		createFrame(mensajeLabel);
		JButton btnSalir = new JButton(mensajeBoton);
		panel.add(btnSalir, "cell 1 2,alignx center,aligny center");
		btnSalir.addActionListener(new ActionListener() {
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
		
	}
	
	public int getResponse() {
		int integerResponse;
		if (response)
			integerResponse = 1;
		else
			integerResponse = 0;
		
		if (updatedResponse)
			dispose();
		else
			integerResponse = -1;
		
		return integerResponse;
		
	}

}