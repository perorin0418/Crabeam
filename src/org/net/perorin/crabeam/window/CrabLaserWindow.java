package org.net.perorin.crabeam.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
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
import org.net.perorin.crabeam.logic.CacheManeger;
import org.net.perorin.crabeam.logic.LogicCrabLaser;
import org.net.perorin.crabeam.poi.FormatLoader;

public class CrabLaserWindow implements NativeKeyListener {

	public ConfigCrabLaser config;
	public MetaCrabLaser meta;
	public FormatLoader testData;
	public String format;

	public JFrame frame;
	public JSplitPane mainSplitPane;
	public RepeatImagePanel testSuitePanel;
	public int currentNo = 0;
	public JPanel underPanel;
	public TestSuitePanel currentTestSuite;
	public JPanel pictureBeforePanel;
	public HashMap<String, LinkedList<String>> pictureAfterMap;
	public HashMap<String, LinkedList<String>> pictureBeforeMap;
	private JScrollPane pictureBeforeScrollPane;
	private RoundButton prev;
	private RoundButton next;
	private JScrollPane pictureAfterScrollPane;
	private JPanel pictureAfterPanel;
	public JRadioButton rdbtnAfter;
	public JRadioButton rdbtnBefore;

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

		this.format = format;
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
		initButtonPanel();
		loadPictureMap();
		reloadTestSuite();
	}

	private void initFrame() {
		frame = new JFrame();
		if (meta != null) {
			frame.setBounds(meta.getFrame_x(), meta.getFrame_y(), meta.getFrame_width(), meta.getFrame_height());
		} else {
			frame.setBounds(100, 100, 400, 500);
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(new ImageIcon(Constant.ICON_PATH).getImage());
		frame.setTitle("蟹レーザー");
		frame.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				reloadTestSuite();
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
					FileOutputStream fos = new FileOutputStream(Meta.META_PATH);
					JAXB.marshal(buf_meta, fos);
					fos.close();
				} catch (FileNotFoundException fne) {
					fne.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				CacheManeger.clear();
			}
		});
		frame.setAlwaysOnTop(config.isOn_top());
	}

	private void initSplitPanel() {
		mainSplitPane = new JSplitPane();
		mainSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mainSplitPane.setDividerLocation(frame.getHeight() - config.getSplit_location());
		mainSplitPane.setEnabled(false);
		frame.getContentPane().add(mainSplitPane, BorderLayout.CENTER);

		underPanel = new JPanel();
		mainSplitPane.setRightComponent(underPanel);
		underPanel.setLayout(new BorderLayout(0, 0));
	}

	private void initTestSuitePanel() {
		testSuitePanel = new RepeatImagePanel(Constant.TESTSUITE_BACKGROUND_PATH);
		mainSplitPane.setLeftComponent(testSuitePanel);
	}

	private void initPicturePanel() {
		pictureAfterScrollPane = new JScrollPane();
		pictureAfterScrollPane.getHorizontalScrollBar().setUnitIncrement(5);
		pictureAfterScrollPane.setPreferredSize(new Dimension(0, 100));
		underPanel.add(pictureAfterScrollPane, BorderLayout.NORTH);

		JPanel pictureAfterMainPanel = new JPanel();
		pictureAfterMainPanel.setLayout(new BorderLayout(0, 0));
		pictureAfterScrollPane.setViewportView(pictureAfterMainPanel);

		JLabel pictureAfterTitleLabel = new JLabel("<html>修<br>正<br>後");
		pictureAfterTitleLabel.setFont(new Font("メイリオ", Font.PLAIN, 14));
		pictureAfterTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		pictureAfterTitleLabel.setBackground(SystemColor.inactiveCaption);
		pictureAfterTitleLabel.setOpaque(true);
		pictureAfterTitleLabel.setPreferredSize(new Dimension(30, 0));
		pictureAfterMainPanel.add(pictureAfterTitleLabel, BorderLayout.WEST);

		pictureAfterPanel = new JPanel();
		pictureAfterPanel.setBackground(SystemColor.inactiveCaptionBorder);
		pictureAfterMainPanel.add(pictureAfterPanel, BorderLayout.CENTER);

		pictureBeforeScrollPane = new JScrollPane();
		pictureBeforeScrollPane.getHorizontalScrollBar().setUnitIncrement(5);
		pictureBeforeScrollPane.setPreferredSize(new Dimension(0, 100));
		underPanel.add(pictureBeforeScrollPane, BorderLayout.CENTER);

		JPanel pictureBeforeMainPanel = new JPanel();
		pictureBeforeMainPanel.setLayout(new BorderLayout(0, 0));
		pictureBeforeScrollPane.setViewportView(pictureBeforeMainPanel);

		JLabel pictureBeforeTitleLabel = new JLabel("<html>修<br>正<br>前");
		pictureBeforeTitleLabel.setFont(new Font("メイリオ", Font.PLAIN, 14));
		pictureBeforeTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		pictureBeforeTitleLabel.setBackground(SystemColor.inactiveCaption);
		pictureBeforeTitleLabel.setOpaque(true);
		pictureBeforeTitleLabel.setPreferredSize(new Dimension(30, 0));
		pictureBeforeMainPanel.add(pictureBeforeTitleLabel, BorderLayout.WEST);

		pictureBeforePanel = new JPanel();
		pictureBeforePanel.setBackground(SystemColor.inactiveCaptionBorder);
		pictureBeforeMainPanel.add(pictureBeforePanel, BorderLayout.CENTER);
	}

	private void initButtonPanel() {
		JPanel buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setHgap(1);
		buttonPanel.setBackground(Color.WHITE);
		underPanel.add(buttonPanel, BorderLayout.SOUTH);

		prev = new RoundButton(new ImageIcon(Constant.BUTTON_PREV));
		prev.setBackground(new Color(255, 255, 255, 255));
		prev.setPressedIcon(new ImageIcon(Constant.BUTTON_PREV_P));
		prev.setToolTipText("ひとつ前のテストへ");
		prev.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (0 < currentNo) {
					currentNo--;
					reloadTestSuite();
					if (rdbtnAfter.isSelected()) {
						if (pictureAfterMap.containsKey(currentTestSuite.getHeadText())) {
							LinkedList<String> list = pictureAfterMap.get(currentTestSuite.getHeadText());
							currentTestSuite.setPicture(list.getLast());
							currentTestSuite.pictureRefresh();
						}
					} else {
						if (pictureBeforeMap.containsKey(currentTestSuite.getHeadText())) {
							LinkedList<String> list = pictureBeforeMap.get(currentTestSuite.getHeadText());
							currentTestSuite.setPicture(list.getLast());
							currentTestSuite.pictureRefresh();
						}
					}
				}
			}
		});
		buttonPanel.add(prev);

		RoundButton star = new RoundButton(new ImageIcon(Constant.BUTTON_STAR));
		star.setBackground(new Color(255, 255, 255, 255));
		star.setPressedIcon(new ImageIcon(Constant.BUTTON_STAR_P));
		star.setToolTipText("BDDを切り替え");
		star.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				currentTestSuite.toggleSelect();
			}
		});
		buttonPanel.add(star);

		next = new RoundButton(new ImageIcon(Constant.BUTTON_NEXT));
		next.setBackground(new Color(255, 255, 255, 255));
		next.setPressedIcon(new ImageIcon(Constant.BUTTON_NEXT_P));
		next.setToolTipText("一つ次のテストへ");
		next.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentNo < testData.size() - 1) {
					currentNo++;
					reloadTestSuite();
					if (rdbtnAfter.isSelected()) {
						if (pictureAfterMap.containsKey(currentTestSuite.getHeadText())) {
							LinkedList<String> list = pictureAfterMap.get(currentTestSuite.getHeadText());
							currentTestSuite.setPicture(list.getLast());
							currentTestSuite.pictureRefresh();
						}
					} else {
						if (pictureBeforeMap.containsKey(currentTestSuite.getHeadText())) {
							LinkedList<String> list = pictureBeforeMap.get(currentTestSuite.getHeadText());
							currentTestSuite.setPicture(list.getLast());
							currentTestSuite.pictureRefresh();
						}
					}
				}
			}
		});
		buttonPanel.add(next);

		buttonPanel.add(Box.createHorizontalStrut(10));

		RoundButton config = new RoundButton(new ImageIcon(Constant.BUTTON_CONFIG));
		config.setBackground(new Color(255, 255, 255, 255));
		config.setPressedIcon(new ImageIcon(Constant.BUTTON_CONFIG_P));
		config.setToolTipText("コンフィグ");
		config.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CrabLaserConfig clc = new CrabLaserConfig(CrabLaserWindow.this);
				clc.frame.setVisible(true);
			}
		});
		buttonPanel.add(config);

		RoundButton menu = new RoundButton(new ImageIcon(Constant.BUTTON_MENU));
		menu.setBackground(new Color(255, 255, 255, 255));
		menu.setPressedIcon(new ImageIcon(Constant.BUTTON_MENU_P));
		menu.setToolTipText("エビデンスの編集");
		menu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CrabLaserEdit cle = new CrabLaserEdit(CrabLaserWindow.this, testData, pictureAfterMap, pictureBeforeMap);
				cle.setSelect(currentTestSuite.getHeadText());
				cle.frame.setVisible(true);
			}
		});
		buttonPanel.add(menu);

		JPanel panel = new JPanel();
		buttonPanel.add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		rdbtnAfter = new JRadioButton("修正後");
		rdbtnAfter.setBackground(Color.WHITE);
		rdbtnAfter.setFont(new Font("メイリオ", Font.PLAIN, 12));
		rdbtnAfter.setSelected(true);
		panel.add(rdbtnAfter, BorderLayout.NORTH);

		rdbtnBefore = new JRadioButton("修正前");
		rdbtnBefore.setBackground(Color.WHITE);
		rdbtnBefore.setFont(new Font("メイリオ", Font.PLAIN, 12));
		panel.add(rdbtnBefore, BorderLayout.SOUTH);

		ButtonGroup bg = new ButtonGroup();
		bg.add(rdbtnAfter);
		bg.add(rdbtnBefore);

	}

	private void reloadTestSuite() {

		// SplitPaneの更新
		mainSplitPane.setDividerLocation(frame.getHeight() - config.getSplit_location());

		// テストスウィートの更新
		testSuitePanel.removeAll();
		currentTestSuite = new TestSuitePanel(format);
		currentTestSuite.setHeadText(testData.getHeader(currentNo));
		currentTestSuite.setGivenText(testData.getGiven(currentNo));
		currentTestSuite.setWhenText(testData.getWhen(currentNo));
		currentTestSuite.setThenText(testData.getThen(currentNo));
		testSuitePanel.add(currentTestSuite);
		testSuitePanel.setLayer(currentTestSuite, 0);
		currentTestSuite.setBounds(
				0,
				0,
				frame.getWidth(),
				mainSplitPane.getDividerLocation());
		currentTestSuite.setScrollEnable(true);
		currentTestSuite.setVisible(true);
		currentTestSuite.toggleSelect();

		reloadScreenShotThumb();

		System.gc();
	}

	public void reloadScreenShotThumb() {
		boolean isAfter = rdbtnAfter.isSelected();
		rdbtnAfter.setSelected(true);
		pictureAfterPanel.removeAll();
		if (pictureAfterMap.containsKey(currentTestSuite.getHeadText())) {
			LinkedList<String> list = pictureAfterMap.get(currentTestSuite.getHeadText());
			for (String imgFile : list) {
				addScreenShotThumb(imgFile);
			}
		}
		pictureAfterPanel.updateUI();

		rdbtnBefore.setSelected(true);
		pictureBeforePanel.removeAll();
		if (pictureBeforeMap.containsKey(currentTestSuite.getHeadText())) {
			LinkedList<String> list = pictureBeforeMap.get(currentTestSuite.getHeadText());
			for (String imgFile : list) {
				addScreenShotThumb(imgFile);
			}
		}
		pictureBeforePanel.updateUI();
		if (isAfter) {
			rdbtnAfter.setSelected(true);
		} else {
			rdbtnBefore.setSelected(true);
		}
	}

	private void addScreenShotThumb(String imgFile) {
		ScreenShotThumb sst = new ScreenShotThumb(imgFile);
		sst.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				currentTestSuite.setPicture(imgFile);
				currentTestSuite.pictureRefresh();
				for (Component buf : pictureAfterPanel.getComponents()) {
					((ScreenShotThumb) buf).diselect();
				}
				for (Component buf : pictureBeforePanel.getComponents()) {
					((ScreenShotThumb) buf).diselect();
				}
				sst.select();
			}
		});
		if (rdbtnAfter.isSelected()) {
			pictureAfterPanel.add(sst);
			for (Component buf : pictureAfterPanel.getComponents()) {
				((ScreenShotThumb) buf).diselect();
			}
		} else {
			pictureBeforePanel.add(sst);
			for (Component buf : pictureBeforePanel.getComponents()) {
				((ScreenShotThumb) buf).diselect();
			}
		}
	}

	public void loadPictureMap() {
		pictureAfterMap = LogicCrabLaser.loadPictureMap(testData, "after");
		pictureBeforeMap = LogicCrabLaser.loadPictureMap(testData, "before");
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

	private void wait(int mill) {
		try {
			Thread.sleep(mill);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		// NOP
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		System.out.println("Key release : KeyCode => " + e.getKeyCode() + " Modifiers => " + e.getModifiers());
		if (e.getKeyCode() == 3639 && (e.getModifiers() == 0 || e.getModifiers() == 256)) {
			currentTestSuite.setWait(true);
			String imgFile = LogicCrabLaser.screenShot(this);
			if (rdbtnAfter.isSelected()) {
				if (pictureAfterMap.containsKey(currentTestSuite.getHeadText())) {
					LinkedList<String> list = pictureAfterMap.get(currentTestSuite.getHeadText());
					list.add(imgFile);
				} else {
					LinkedList<String> list = new LinkedList<String>();
					list.add(imgFile);
					pictureAfterMap.put(currentTestSuite.getHeadText(), list);
				}
			} else {
				if (pictureBeforeMap.containsKey(currentTestSuite.getHeadText())) {
					LinkedList<String> list = pictureBeforeMap.get(currentTestSuite.getHeadText());
					list.add(imgFile);
				} else {
					LinkedList<String> list = new LinkedList<String>();
					list.add(imgFile);
					pictureBeforeMap.put(currentTestSuite.getHeadText(), list);
				}
			}
			currentTestSuite.setPicture(imgFile);
			currentTestSuite.pictureRefresh();
			addScreenShotThumb(imgFile);
			if (rdbtnAfter.isSelected()) {
				pictureAfterPanel.updateUI();
				wait(100);
				JScrollBar hBar = pictureAfterScrollPane.getHorizontalScrollBar();
				int hBarMax = hBar.getMaximum();
				hBar.setValue(hBarMax);
			} else {
				pictureBeforePanel.updateUI();
				wait(100);
				JScrollBar hBar = pictureBeforeScrollPane.getHorizontalScrollBar();
				int hBarMax = hBar.getMaximum();
				hBar.setValue(hBarMax);
			}
			currentTestSuite.setWait(false);
		} else if (e.getKeyCode() == 57421 && e.getModifiers() == 8) {
			next.doClick();
		} else if (e.getKeyCode() == 57419 && e.getModifiers() == 8) {
			prev.doClick();
		} else if (e.getKeyCode() == 57424 && e.getModifiers() == 8) {
			currentTestSuite.toggleSelect();
		} else if (e.getKeyCode() == 57416 && e.getModifiers() == 8) {
			currentTestSuite.unToggleSelect();
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent ee) {
		// NOP
	}
}
