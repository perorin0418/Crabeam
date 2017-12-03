package org.net.perorin.crabeam.window;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.xml.bind.JAXB;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.net.perorin.crabeam.config.Config;
import org.net.perorin.crabeam.config.ConfigCrabeam;
import org.net.perorin.crabeam.config.Constant;
import org.net.perorin.crabeam.config.Meta;
import org.net.perorin.crabeam.config.MetaCrabeam;
import org.net.perorin.crabeam.font.NicoFont;
import org.net.perorin.crabeam.logic.Logic;
import org.net.perorin.crabeam.logic.Shortcut;
import org.net.perorin.crabeam.table.ImageModel;
import org.net.perorin.crabeam.table.ImageTable;
import org.net.perorin.crabeam.table.ShortcutModel;
import org.net.perorin.crabeam.table.ShortcutTable;

public class CrabeamWindow implements NativeKeyListener {

	private List<Shortcut> scList;

	private MetaCrabeam meta;
	private ConfigCrabeam config;
	private JFrame frame;
	private ImageModel imgModel;
	private ImageTable imgTable;
	private JTextField currentNoTxt;
	private JPanel schSlctPnl;
	private JPanel schPnl;
	private JPanel imgTblPnl;
	private JPanel sctPnl;
	private JPanel castPnl;
	private JPanel txtPnl;
	private JPanel btnPnl;
	private JPanel optPnl;
	private JPanel upDownPnl;
	private JTextField formatTxt;
	private JPanel optTab2;
	private JTabbedPane tabbedPane;
	private JCheckBox chkOnTop;
	private JLabel extensionLbl;
	private JLabel givenLbl;
	private JTextField givenTxt;
	private JLabel whenLbl;
	private JTextField whenTxt;
	private JLabel thenLbl;
	private JTextField thenTxt;
	private JPanel optTab4;
	private ShortcutTable shortcutTable;
	private ShortcutModel shortcutModel;
	private JButton btnNewButton;
	private JComboBox<String> extensionCmb;
	private JPanel optTab3;
	private JLabel savePathLbl;
	private JTextField savePathTxt;
	private JButton savePathBtn;
	private JCheckBox chkSaveFull;
	private String section = "given";
	private RoundButton givenBtn;
	private RoundButton whenBtn;
	private RoundButton thenBtn;
	private JTextField keyCodeViewer;
	private JCheckBox chkAutoCountUp;
	private JTextField countUpTxt;

	private JComboBox<String> cmbMouseCursor;

	public CrabeamWindow() {
		initialize();
		initFrame();
		initCastPnl();
		initTxtPnl();
		initBtnPnl();
		initUpDownButton();
		initSchBtnPnl();
		initSchPnl();
		initSctPnl();
		initSchSlctPnl();
		initImgTblPnl();
		initOptPnl();
		initOptTab1();
		initOptTab2();
		initOptTab3();
		initOptTab4();
	}

	private void initialize() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		System.setProperty("file.encoding", "UTF-8");

		meta = JAXB.unmarshal(new File(Meta.META_PATH), Meta.class).getCrabeam();
		config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class).getCrabeam();

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
	}

	private void initFrame() {
		frame = new JFrame();
		frame.setTitle("蟹光線");
		if (meta != null) {
			frame.setBounds(meta.getFrame_x(), meta.getFrame_y(), meta.getFrame_width(), meta.getFrame_height());
		} else {
			frame.setBounds(100, 100, 360, 280);
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(new ImageIcon(Constant.ICON_PATH).getImage());
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				frameClosing();
			}

			private void frameClosing() {
				meta.setFrame_x(frame.getX());
				meta.setFrame_y(frame.getY());
				meta.setFrame_width(frame.getWidth());
				meta.setFrame_height(frame.getHeight());
				meta.setNo_width(imgTable.getNoWidth());
				meta.setSs_width(imgTable.getSsWidth());
				try {
					Meta buf_meta = JAXB.unmarshal(new File(Meta.META_PATH), Meta.class);
					buf_meta.setCrabeam(meta);
					JAXB.marshal(buf_meta, new FileOutputStream(Meta.META_PATH));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				config.setFormat(formatTxt.getText());
				config.setOn_top(chkOnTop.isSelected());
				config.setSave_full(chkSaveFull.isSelected());
				config.setAuto_count_up(chkAutoCountUp.isSelected());
				config.setSave_type((String) extensionCmb.getSelectedItem());
				config.setGiven_ext(givenTxt.getText());
				config.setWhen_ext(whenTxt.getText());
				config.setThen_ext(thenTxt.getText());
				config.setSave_path(savePathTxt.getText());
				config.setAuto_count_up_no(Integer.parseInt(countUpTxt.getText()));
				try {
					Config buf_config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class);
					buf_config.setCrabeam(config);
					JAXB.marshal(buf_config, new FileOutputStream(Config.CONFIG_PATH));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				Logic.saveShortcutPreset(shortcutModel);
			}
		});
	}

	private void initCastPnl() {
		castPnl = new JPanel();
		castPnl.setBackground(SystemColor.inactiveCaptionBorder);
		castPnl.setLayout(null);
		castPnl.setPreferredSize(new Dimension(60, 60));
		castPnl.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					optPnl.setVisible(!optPnl.isVisible());
				}
			}

		});

		JLabel crabFrame = new JLabel(new ImageIcon(Constant.CAST_FRAME_PATH));
		crabFrame.setBounds(5, 0, 50, 50);
		castPnl.add(crabFrame);
		crabFrame.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel crabImg = new JLabel(new ImageIcon(ImageRandomizer.getCastImagePath()));
		crabImg.setBounds(5, 0, 50, 50);
		castPnl.add(crabImg);
		crabImg.setHorizontalAlignment(SwingConstants.CENTER);
	}

	private void initTxtPnl() {
		txtPnl = new JPanel();
		txtPnl.setBackground(SystemColor.inactiveCaptionBorder);
		txtPnl.setLayout(new BorderLayout(0, 0));
		Border b = txtPnl.getBorder();
		EmptyBorder e = new EmptyBorder(12, 0, 12, 0);
		txtPnl.setBorder(new CompoundBorder(b, e));

		currentNoTxt = new JTextField();
		currentNoTxt.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPnl.add(currentNoTxt);
		currentNoTxt.setFont(NicoFont.getFont());
		currentNoTxt.setEditable(false);
	}

	private void initUpDownButton() {
		upDownPnl = new JPanel();
		upDownPnl.setBackground(SystemColor.inactiveCaptionBorder);
		upDownPnl.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		btnPnl.add(upDownPnl, BorderLayout.WEST);
	}

	private void initSchBtnPnl() {
	}

	private void initBtnPnl() {
		btnPnl = new JPanel();
		btnPnl.setBackground(SystemColor.inactiveCaptionBorder);
		btnPnl.setLayout(new BorderLayout(0, 0));
	}

	private void initSchPnl() {
		schPnl = new JPanel();
		schPnl.setBackground(SystemColor.inactiveCaptionBorder);
		schPnl.setPreferredSize(new Dimension(300, 55));
		schPnl.setLayout(new BorderLayout(0, 0));
		schPnl.add(castPnl, BorderLayout.WEST);
		schPnl.add(txtPnl, BorderLayout.CENTER);
		schPnl.add(btnPnl, BorderLayout.EAST);
	}

	private void initSctPnl() {
		sctPnl = new JPanel();
		Border b = sctPnl.getBorder();
		EmptyBorder e = new EmptyBorder(0, 10, 0, 10);
		sctPnl.setBorder(new CompoundBorder(b, e));
		sctPnl.setBackground(SystemColor.inactiveCaptionBorder);
		sctPnl.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

		givenBtn = new RoundButton(new ImageIcon(Constant.BUTTON_GIVEN_P));
		givenBtn.setBackground(SystemColor.inactiveCaption);
		givenBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!"given".equals(section)) {
					section = "given";
					givenBtn.setIcon(new ImageIcon(Constant.BUTTON_GIVEN_P));
					whenBtn.setIcon(new ImageIcon(Constant.BUTTON_WHEN));
					thenBtn.setIcon(new ImageIcon(Constant.BUTTON_THEN));
				}
			}
		});
		;
		sctPnl.add(givenBtn);

		whenBtn = new RoundButton(new ImageIcon(Constant.BUTTON_WHEN));
		whenBtn.setBackground(SystemColor.inactiveCaption);
		whenBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!"when".equals(section)) {
					section = "when";
					givenBtn.setIcon(new ImageIcon(Constant.BUTTON_GIVEN));
					whenBtn.setIcon(new ImageIcon(Constant.BUTTON_WHEN_P));
					thenBtn.setIcon(new ImageIcon(Constant.BUTTON_THEN));
				}
			}
		});
		sctPnl.add(whenBtn);

		thenBtn = new RoundButton(new ImageIcon(Constant.BUTTON_THEN));
		thenBtn.setBackground(SystemColor.inactiveCaption);
		thenBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!"then".equals(section)) {
					section = "then";
					givenBtn.setIcon(new ImageIcon(Constant.BUTTON_GIVEN));
					whenBtn.setIcon(new ImageIcon(Constant.BUTTON_WHEN));
					thenBtn.setIcon(new ImageIcon(Constant.BUTTON_THEN_P));
				}
			}
		});
		sctPnl.add(thenBtn);
	}

	private void initSchSlctPnl() {
		schSlctPnl = new JPanel();
		schSlctPnl.setLayout(new BorderLayout(0, 0));
		schSlctPnl.add(schPnl, BorderLayout.NORTH);
		schSlctPnl.add(sctPnl, BorderLayout.SOUTH);
		frame.getContentPane().add(schSlctPnl, BorderLayout.NORTH);
	}

	private void initImgTblPnl() {
		imgTblPnl = new JPanel();
		imgTblPnl.setBackground(SystemColor.inactiveCaptionBorder);
		Border b = imgTblPnl.getBorder();
		EmptyBorder e = new EmptyBorder(5, 10, 10, 10);
		imgTblPnl.setBorder(new CompoundBorder(b, e));
		frame.getContentPane().add(imgTblPnl, BorderLayout.CENTER);
		imgTblPnl.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(SystemColor.inactiveCaptionBorder);
		imgTblPnl.add(scrollPane, BorderLayout.CENTER);

		imgModel = new ImageModel();
		imgTable = new ImageTable(imgModel);
		scrollPane.setViewportView(imgTable);
		scrollPane.getViewport().setOpaque(true);
		scrollPane.getViewport().setBackground(SystemColor.inactiveCaptionBorder);
	}

	private void initOptPnl() {
		optPnl = new JPanel();
		optPnl.setVisible(false);
		optPnl.setBackground(SystemColor.inactiveCaptionBorder);
		optPnl.setPreferredSize(new Dimension(200, 200));
		frame.getContentPane().add(optPnl, BorderLayout.SOUTH);
		optPnl.setLayout(new BorderLayout(0, 0));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		optPnl.add(tabbedPane, BorderLayout.CENTER);
	}

	private void initOptTab1() {
		JPanel optTab1 = new JPanel();
		optTab1.setBackground(SystemColor.inactiveCaptionBorder);
		tabbedPane.addTab("フォーマット設定", null, optTab1, null);
		optTab1.setLayout(null);

		JLabel formatLbl = new JLabel("フォーマット");
		formatLbl.setBounds(12, 10, 78, 19);
		optTab1.add(formatLbl);
		formatLbl.setFont(new Font("メイリオ", Font.PLAIN, 12));

		formatTxt = new JTextField();
		formatTxt.setBounds(111, 9, 117, 20);
		formatTxt.setText(config.getFormat());
		Logic.loadFormat(formatTxt, upDownPnl, currentNoTxt);
		Logic.updateCurrentNo(formatTxt, upDownPnl, currentNoTxt);
		optTab1.add(formatTxt);

		JButton button = new JButton("適用");
		button.setFont(new Font("メイリオ", Font.PLAIN, 12));
		button.setBounds(229, 9, 57, 21);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Logic.loadFormat(formatTxt, upDownPnl, currentNoTxt);
				Logic.updateCurrentNo(formatTxt, upDownPnl, currentNoTxt);
				Logic.updateShortcut(upDownPnl, shortcutModel);
			}
		});
		optTab1.add(button);

		extensionLbl = new JLabel("保存形式");
		extensionLbl.setFont(new Font("メイリオ", Font.PLAIN, 12));
		extensionLbl.setBounds(12, 39, 78, 13);
		optTab1.add(extensionLbl);

		extensionCmb = new JComboBox<String>();
		extensionCmb.setBounds(111, 35, 117, 19);
		extensionCmb.setFont(new Font("メイリオ", Font.PLAIN, 12));
		extensionCmb.addItem("png");
		extensionCmb.addItem("jpg");
		extensionCmb.addItem("bmp");
		extensionCmb.setSelectedItem(config.getSave_type());
		optTab1.add(extensionCmb);

		givenLbl = new JLabel("Given付加文字");
		givenLbl.setFont(new Font("メイリオ", Font.PLAIN, 12));
		givenLbl.setBounds(12, 62, 100, 13);
		optTab1.add(givenLbl);

		givenTxt = new JTextField();
		givenTxt.setBounds(111, 58, 117, 19);
		givenTxt.setText(config.getGiven_ext());
		optTab1.add(givenTxt);

		whenLbl = new JLabel("When付加文字");
		whenLbl.setFont(new Font("メイリオ", Font.PLAIN, 12));
		whenLbl.setBounds(12, 85, 100, 13);
		optTab1.add(whenLbl);

		whenTxt = new JTextField();
		whenTxt.setBounds(111, 81, 117, 19);
		whenTxt.setText(config.getWhen_ext());
		optTab1.add(whenTxt);

		thenLbl = new JLabel("Then付加文字");
		thenLbl.setFont(new Font("メイリオ", Font.PLAIN, 12));
		thenLbl.setBounds(12, 108, 100, 13);
		optTab1.add(thenLbl);

		thenTxt = new JTextField();
		thenTxt.setBounds(111, 104, 117, 19);
		thenTxt.setText(config.getThen_ext());
		optTab1.add(thenTxt);

	}

	private void initOptTab2() {
		optTab2 = new JPanel();
		optTab2.setBackground(SystemColor.inactiveCaptionBorder);
		tabbedPane.addTab("表示設定", null, optTab2, null);
		optTab2.setLayout(null);

		chkOnTop = new JCheckBox("常に最前面表示");
		chkOnTop.setBackground(SystemColor.inactiveCaptionBorder);
		chkOnTop.setFont(new Font("メイリオ", Font.PLAIN, 12));
		chkOnTop.setBounds(8, 6, 180, 20);
		chkOnTop.setSelected(config.isOn_top());
		frame.setAlwaysOnTop(chkOnTop.isSelected());
		chkOnTop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setAlwaysOnTop(chkOnTop.isSelected());
			}
		});
		optTab2.add(chkOnTop);

	}

	private void initOptTab3() {
		optTab3 = new JPanel();
		optTab3.setBackground(SystemColor.inactiveCaptionBorder);
		tabbedPane.addTab("保存設定", null, optTab3, null);
		optTab3.setLayout(null);

		savePathLbl = new JLabel("保存先");
		savePathLbl.setFont(new Font("メイリオ", Font.PLAIN, 12));
		savePathLbl.setBounds(12, 10, 45, 19);
		optTab3.add(savePathLbl);

		savePathTxt = new JTextField();
		savePathTxt.setBounds(59, 9, 164, 20);
		savePathTxt.setText(config.getSave_path());
		optTab3.add(savePathTxt);

		savePathBtn = new JButton("...");
		savePathBtn.setFont(new Font("メイリオ", Font.PLAIN, 12));
		savePathBtn.setBounds(235, 9, 39, 21);
		savePathBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String path = Logic.getSavePath(savePathTxt.getText(), frame).getPath();
				if (!"".equals(path)) {
					savePathTxt.setText(path);
				}

			}
		});
		optTab3.add(savePathBtn);

		chkSaveFull = new JCheckBox("全画面保存");
		chkSaveFull.setBackground(SystemColor.inactiveCaptionBorder);
		chkSaveFull.setFont(new Font("メイリオ", Font.PLAIN, 12));
		chkSaveFull.setBounds(8, 35, 103, 21);
		chkSaveFull.setSelected(config.isSave_full());
		optTab3.add(chkSaveFull);

		chkAutoCountUp = new JCheckBox("オートカウントアップ");
		chkAutoCountUp.setBackground(SystemColor.inactiveCaptionBorder);
		chkAutoCountUp.setFont(new Font("メイリオ", Font.PLAIN, 12));
		chkAutoCountUp.setBounds(8, 58, 145, 21);
		chkAutoCountUp.setSelected(config.isAuto_count_up());
		chkAutoCountUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				countUpTxt.setEditable(chkAutoCountUp.isSelected());
			}
		});
		optTab3.add(chkAutoCountUp);

		countUpTxt = new JTextField();
		countUpTxt.setBounds(161, 58, 45, 19);
		countUpTxt.setDocument(new PlainDocument() {
			int currentValue = 0;

			public int getValue() {
				return currentValue;
			}

			@Override
			public void insertString(int offset, String str, AttributeSet attributes)
					throws BadLocationException {
				if (str == null) {
					return;
				} else {
					String newValue;
					int length = getLength();
					if (length == 0) {
						newValue = str;
					} else {
						String currentContent = getText(0, length);
						StringBuffer currentBuffer = new StringBuffer(currentContent);
						currentBuffer.insert(offset, str);
						newValue = currentBuffer.toString();
					}
					currentValue = checkInput(newValue, offset);
					super.insertString(offset, str, attributes);
				}
			}

			@Override
			public void remove(int offset, int length) throws BadLocationException {
				int currentLength = getLength();
				String currentContent = getText(0, currentLength);
				String before = currentContent.substring(0, offset);
				String after = currentContent.substring(length + offset, currentLength);
				String newValue = before + after;
				currentValue = checkInput(newValue, offset);
				super.remove(offset, length);
			}

			private int checkInput(String proposedValue, int offset) throws BadLocationException {
				if (proposedValue.length() > 0) {
					try {
						int newValue = Integer.parseInt(proposedValue);
						return newValue;
					} catch (NumberFormatException e) {
						throw new BadLocationException(proposedValue, offset);
					}
				} else {
					return 0;
				}
			}
		});
		countUpTxt.setText(String.valueOf(config.getAuto_count_up_no()));
		countUpTxt.setEditable(chkAutoCountUp.isSelected());
		optTab3.add(countUpTxt);

		JLabel countUpLbl = new JLabel("番目のエビ№");
		countUpLbl.setFont(new Font("メイリオ", Font.PLAIN, 12));
		countUpLbl.setBounds(210, 62, 72, 13);
		optTab3.add(countUpLbl);

		JLabel lblMouseCursor = new JLabel("マウスカーソル");
		lblMouseCursor.setFont(new Font("メイリオ", Font.PLAIN, 12));
		lblMouseCursor.setBounds(12, 85, 84, 13);
		optTab3.add(lblMouseCursor);

		cmbMouseCursor = new JComboBox<String>();
		cmbMouseCursor.setBounds(108, 81, 115, 19);
		cmbMouseCursor.addItem("無し");
		cmbMouseCursor.addItem("デフォルト");
		cmbMouseCursor.addItem("指");
		cmbMouseCursor.addItem("砂時計");
		cmbMouseCursor.addItem("移動");
		cmbMouseCursor.addItem("上下拡縮");
		cmbMouseCursor.addItem("左右拡縮");
		cmbMouseCursor.addItem("右斜拡縮");
		cmbMouseCursor.addItem("左斜拡縮");
		cmbMouseCursor.addItem("キャレット");
		cmbMouseCursor.addItem("十字");
		optTab3.add(cmbMouseCursor);
	}

	private void initOptTab4() {
		optTab4 = new JPanel();
		optTab4.setBackground(SystemColor.inactiveCaptionBorder);
		tabbedPane.addTab("ショートカットキー", null, optTab4, null);
		Border b = optTab4.getBorder();
		EmptyBorder e = new EmptyBorder(10, 10, 10, 10);
		optTab4.setBorder(new CompoundBorder(b, e));
		optTab4.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(SystemColor.inactiveCaptionBorder);
		optTab4.add(scrollPane, BorderLayout.CENTER);

		shortcutModel = Logic.loadShortcutPreset();
		shortcutTable = new ShortcutTable(shortcutModel);
		scrollPane.setViewportView(shortcutTable);
		scrollPane.getViewport().setOpaque(true);
		scrollPane.getViewport().setBackground(SystemColor.inactiveCaptionBorder);

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panel.setBackground(SystemColor.inactiveCaptionBorder);

		keyCodeViewer = new JTextField();
		keyCodeViewer.setEditable(false);
		keyCodeViewer.setPreferredSize(new Dimension(200, 25));
		panel.add(keyCodeViewer);

		btnNewButton = new JButton("適用");
		btnNewButton.setPreferredSize(new Dimension(60, 25));
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				scList = Logic.loadShortcut(shortcutModel);
			}
		});
		panel.add(btnNewButton);

		scList = Logic.loadShortcut(shortcutModel);

		optTab4.add(panel, BorderLayout.SOUTH);
	}

	private void nextSection() {
		if ("given".equals(section)) {
			section = "when";
			givenBtn.setIcon(new ImageIcon(Constant.BUTTON_GIVEN));
			whenBtn.setIcon(new ImageIcon(Constant.BUTTON_WHEN_P));
			thenBtn.setIcon(new ImageIcon(Constant.BUTTON_THEN));
		} else if ("when".equals(section)) {
			section = "then";
			givenBtn.setIcon(new ImageIcon(Constant.BUTTON_GIVEN));
			whenBtn.setIcon(new ImageIcon(Constant.BUTTON_WHEN));
			thenBtn.setIcon(new ImageIcon(Constant.BUTTON_THEN_P));
		} else if ("then".equals(section)) {
			section = "given";
			givenBtn.setIcon(new ImageIcon(Constant.BUTTON_GIVEN_P));
			whenBtn.setIcon(new ImageIcon(Constant.BUTTON_WHEN));
			thenBtn.setIcon(new ImageIcon(Constant.BUTTON_THEN));
		}
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

	public JFrame getFrame() {
		return frame;
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		System.out.println("KeyCode => " + e.getKeyCode() + " , Modifiers => " + e.getModifiers());
		keyCodeViewer.setText("KeyCode => " + e.getKeyCode() + " , Modifiers => " + e.getModifiers());
		for (Shortcut sc : scList) {
			if (e.getKeyCode() == sc.getKeyCode() && e.getModifiers() == sc.getModifiers()) {
				if (sc.getShortcut().contains("screenshot")) {
					if (chkSaveFull.isSelected()) {
						String ret = Logic.saveScreenshot(
								section,
								savePathTxt,
								currentNoTxt,
								givenTxt,
								whenTxt,
								thenTxt,
								(String) extensionCmb.getSelectedItem(),
								(String) cmbMouseCursor.getSelectedItem());
						Object[] obj = { imgModel.getRowCount() + 1, new File(ret).getName() };
						imgModel.insertRow(0, obj);
						if (chkAutoCountUp.isSelected()) {
							Component[] comps = upDownPnl.getComponents();
							((UpDownButton) comps[Integer.parseInt(countUpTxt.getText())]).clickUp();
						}
						break;
					} else {
						String ret = Logic.saveScreenshotActive(
								section,
								savePathTxt,
								currentNoTxt,
								givenTxt,
								whenTxt,
								thenTxt,
								(String) extensionCmb.getSelectedItem(),
								(String) cmbMouseCursor.getSelectedItem());
						Object[] obj = { imgModel.getRowCount() + 1, new File(ret).getName() };
						imgModel.insertRow(0, obj);
						if (chkAutoCountUp.isSelected()) {
							Component[] comps = upDownPnl.getComponents();
							((UpDownButton) comps[Integer.parseInt(countUpTxt.getText())]).clickUp();
						}
						break;
					}
				} else if (sc.getShortcut().contains("countUp")) {
					Component[] comps = upDownPnl.getComponents();
					int upDownIndex = Integer.parseInt(String.valueOf(sc.getShortcut().charAt(0)));
					((UpDownButton) comps[upDownIndex - 1]).clickUp();
				} else if (sc.getShortcut().contains("countDown")) {
					Component[] comps = upDownPnl.getComponents();
					int upDownIndex = Integer.parseInt(String.valueOf(sc.getShortcut().charAt(0)));
					((UpDownButton) comps[upDownIndex - 1]).clickDown();
				} else if (sc.getShortcut().contains("nextSection")) {
					nextSection();
				}
			}
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
	}
}
