package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.PJSalt;
import data.Data;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JCheckBox staminaUsageBox;
	private JCheckBox worldHopBox;
	
	public boolean running;

	public GUI() {
		setTitle("Cx");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 120, 120);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		JButton btnStartScript = new JButton("Start Script");
		btnStartScript.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PJSalt.data = new Data(
						staminaUsageBox.isSelected(), 
						worldHopBox.isSelected());
				running = false;
				dispose();
			}
		});
		btnStartScript.setBounds(17, 60, 80, 25);
		contentPane.add(btnStartScript);
		
		staminaUsageBox = new JCheckBox();
		staminaUsageBox.setText("Stamina Pots");
		staminaUsageBox.setBounds(10, 10, 100, 20);
		staminaUsageBox.setSelected(true);
		contentPane.add(staminaUsageBox);
		
		worldHopBox = new JCheckBox();
		worldHopBox.setText("World Hopping");
		worldHopBox.setBounds(10, 30, 100, 20);
		worldHopBox.setSelected(true);
		contentPane.add(worldHopBox);

		running = true;
		setVisible(true);
	}
}