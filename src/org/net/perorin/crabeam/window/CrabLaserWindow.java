package org.net.perorin.crabeam.window;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class CrabLaserWindow {

	private JFrame frame;

	private TestSuitePanel tsp;

	private JSplitPane splitPane;

	private TestSuitePanel tsp1;
	private TestSuitePanel tsp2;

	public CrabLaserWindow(String excelFile) {
		initialize();
	}

	private void initialize() {

		Toolkit.getDefaultToolkit().setDynamicLayout(false);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		initFrame();
	}

	private void initFrame() {
		frame = new JFrame();
		frame.setBounds(100, 100, 400, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				reloadBouds();
			}
		});

		splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(400);
		splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				reloadBouds();
			}
		});
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);

		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton btnNewButton = new JButton("New button");
		panel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("New button");
		panel.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("New button");
		panel.add(btnNewButton_2);

		RepeatImagePanel panel_1 = new RepeatImagePanel(new File("./contents/image/background/cork.png"));
		panel_1.setPreferredSize(new Dimension(1000, 1000));
		splitPane.setLeftComponent(panel_1);

		tsp = new TestSuitePanel();
		panel_1.add(tsp);
		panel_1.setLayer(tsp, 0);
		tsp.setHeadText("1-1-1");
		tsp.setGivenText("ああああああああああああああああああああああああああああああああああああああああああああああああああああああ");
		tsp.setWhenText("ああああああああああああああああああああああああああああああああああああああああああああああああああああああ");
		tsp.setThenText("ああああああああああああああああああああああああああああああああああああああああああああああああああああああ");
		tsp.setPicture("./contents/screenshot.png");
		tsp.setBounds(0, 0, frame.getWidth(), splitPane.getDividerLocation());

		tsp1 = new TestSuitePanel();
		panel_1.add(tsp1);
		panel_1.setLayer(tsp1, 1);
		tsp1.setHeadText("1-1-1");
		tsp1.setGivenText("ああああああああああああああああああああああああああああああああああああああああああああああああああああああ");
		tsp1.setWhenText("ああああああああああああああああああああああああああああああああああああああああああああああああああああああ");
		tsp1.setThenText("ああああああああああああああああああああああああああああああああああああああああああああああああああああああ");
		tsp1.setBounds(frame.getWidth() / 2 - 29, 0, frame.getWidth(), splitPane.getDividerLocation());
		tsp1.setScrollEnable(false);

		tsp2 = new TestSuitePanel();
		panel_1.add(tsp2);
		panel_1.setLayer(tsp2, 1);
		tsp2.setHeadText("1-1-1");
		tsp2.setGivenText("ああああああああああああああああああああああああああああああああああああああああああああああああああああああ");
		tsp2.setWhenText("ああああああああああああああああああああああああああああああああああああああああああああああああああああああ");
		tsp2.setThenText("ああああああああああああああああああああああああああああああああああああああああああああああああああああああ");
		tsp2.setBounds(-frame.getWidth() / 2 + 29, 0, frame.getWidth(), splitPane.getDividerLocation());
		tsp2.setScrollEnable(false);

	}

	public JFrame getFrame() {
		return frame;
	}

	private void reloadBouds() {
		tsp.setBounds(0, 0, frame.getWidth(), splitPane.getDividerLocation());
		tsp1.setBounds(frame.getWidth() / 2 - 29, 0, frame.getWidth(), splitPane.getDividerLocation());
		tsp2.setBounds(-frame.getWidth() / 2 + 29, 0, frame.getWidth(), splitPane.getDividerLocation());
	}
}
