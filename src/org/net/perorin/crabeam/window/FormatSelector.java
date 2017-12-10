package org.net.perorin.crabeam.window;

import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.bind.JAXB;

import org.net.perorin.crabeam.config.Constant;
import org.net.perorin.crabeam.format.TestSuiteFormat;

public class FormatSelector {

	public JFrame frame;
	private String xlsx;
	private ArrayList<TestSuiteFormat> listFormat;
	private ArrayList<String> listString;
	private JComboBox<String> comboBox;

	public FormatSelector(String xlsx) {
		this.xlsx = xlsx;
		initialize();
		loadFormat();
	}

	private void initialize() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		listFormat = new ArrayList<>();
		listString = new ArrayList<>();

		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		DisplayMode displayMode = env.getDefaultScreenDevice().getDisplayMode();
		int width = displayMode.getWidth();
		int height = displayMode.getHeight();

		frame = new JFrame();
		frame.setBounds(width / 2 - 122, height / 2 - 50, 244, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setIconImage(new ImageIcon(Constant.ICON_PATH).getImage());
		frame.setTitle("フォーマット選択");
		frame.setResizable(false);

		comboBox = new JComboBox<String>();
		comboBox.setBounds(12, 10, 217, 19);
		frame.getContentPane().add(comboBox);

		JButton btnOk = new JButton("OK");
		btnOk.setBounds(138, 39, 91, 21);
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CrabLaserWindow window = new CrabLaserWindow(xlsx, listString.get(comboBox.getSelectedIndex()));
				window.frame.setVisible(true);
				frame.dispose();
			}
		});
		frame.getContentPane().add(btnOk);
	}

	private void loadFormat() {
		File formatPath = new File("./contents/format");
		for (File file : formatPath.listFiles()) {
			if (file.getPath().endsWith("xml")) {
				listFormat.add(JAXB.unmarshal(file, TestSuiteFormat.class));
				listString.add(file.getAbsolutePath());
			}
		}
		for (TestSuiteFormat tsf : listFormat) {
			comboBox.addItem(tsf.getFormat_name());
		}
	}

}
