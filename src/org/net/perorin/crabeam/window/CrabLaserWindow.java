package org.net.perorin.crabeam.window;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.bind.JAXB;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.net.perorin.crabeam.config.Config;
import org.net.perorin.crabeam.config.ConfigCrabLaser;
import org.net.perorin.crabeam.config.Constant;
import org.net.perorin.crabeam.config.Meta;
import org.net.perorin.crabeam.config.MetaCrabLaser;
import org.net.perorin.crabeam.poi.FormatLoader;

public class CrabLaserWindow implements NativeKeyListener {

	private ConfigCrabLaser config;
	private MetaCrabLaser meta;
	private FormatLoader testData;

	private JFrame frame;
	private JSplitPane splitPane;
	private RepeatImagePanel testSuitePanel;
	private int currentNo = 0;

	public CrabLaserWindow(String excel, String format) {
		initialize(excel, format);
	}

	private void initialize(String excel, String format) {

		Toolkit.getDefaultToolkit().setDynamicLayout(false);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		testData = new FormatLoader(excel, format);
		meta = JAXB.unmarshal(new File(Meta.META_PATH), Meta.class).getCrablaser();
		config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class).getCrablaser();

		if (!GlobalScreen.isNativeHookRegistered()) {
			try {
				GlobalScreen.registerNativeHook();
			} catch (NativeHookException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

		suppressLogger();
		GlobalScreen.addNativeKeyListener(this);

		initFrame();
		initSplitPanel();
		initTestSuitePanel();
		initPicturePanel();
	}

	private void initFrame() {
		frame = new JFrame();
		if (meta != null) {
			frame.setBounds(meta.getFrame_x(), meta.getFrame_y(), meta.getFrame_width(), meta.getFrame_height());
		} else {
			frame.setBounds(100, 100, 400, 500);
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				reloadBouds();
			}
		});
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				meta.setFrame_x(frame.getX());
				meta.setFrame_y(frame.getY());
				meta.setFrame_width(frame.getWidth());
				meta.setFrame_height(frame.getHeight());
				try {
					Meta buf_meta = JAXB.unmarshal(new File(Meta.META_PATH), Meta.class);
					buf_meta.setCrablaser(meta);
					JAXB.marshal(buf_meta, new FileOutputStream(Meta.META_PATH));
				} catch (FileNotFoundException fne) {
					fne.printStackTrace();
				}
			}
		});
	}

	private void initSplitPanel() {
		splitPane = new JSplitPane();
		splitPane.setEnabled(false);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(frame.getHeight() - config.getSplit_location());
		splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent pce) {
				reloadBouds();
			}
		});
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);
	}

	private void initTestSuitePanel() {
				testSuitePanel = new RepeatImagePanel(Constant.TESTSUITE_BACKGROUND_PATH);
				splitPane.setLeftComponent(testSuitePanel);

				reloadBouds();
	}

	private void initPicturePanel() {
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);

		JPanel picturePanel = new JPanel();
		picturePanel.setBackground(SystemColor.inactiveCaptionBorder);
		picturePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		scrollPane.setViewportView(picturePanel);

		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentNo < testData.size() - 1) {
					currentNo++;
					reloadBouds();
				}
			}
		});
		picturePanel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (0 < currentNo) {
					currentNo--;
					reloadBouds();
				}
			}
		});
		picturePanel.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("New button");
		picturePanel.add(btnNewButton_2);
	}

	public JFrame getFrame() {
		return frame;
	}

	private void reloadBouds() {

		// SplitPaneの更新
		splitPane.setDividerLocation(frame.getHeight() - config.getSplit_location());

		// テストスウィートの更新
		testSuitePanel.removeAll();
		TestSuitePanel tsp = new TestSuitePanel();
		tsp.setHeadText(testData.getHeader(currentNo));
		tsp.setGivenText(testData.getGiven(currentNo));
		tsp.setWhenText(testData.getWhen(currentNo));
		tsp.setThenText(testData.getThen(currentNo));
		tsp.setPicture("./contents/screenshot.png");
		testSuitePanel.add(tsp);
		testSuitePanel.setLayer(tsp, 0);
		tsp.setBounds(
				0,
				0,
				frame.getWidth(),
				splitPane.getDividerLocation());
		tsp.setScrollEnable(true);
		tsp.setVisible(true);
	}

	private void suppressLogger() {
		ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, r -> {
			Thread thread = new Thread(r);
			thread.setDaemon(true);
			return thread;
		});

		executorService.schedule(() -> {
			final Logger jNativeHookLogger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			if (jNativeHookLogger.getLevel() != Level.OFF) {
				synchronized (jNativeHookLogger) {
					jNativeHookLogger.setLevel(Level.OFF);
				}
			}
			System.gc();
		}, 2, TimeUnit.SECONDS);
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		// NOP
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		System.out.println("release");
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// NOP
	}
}
