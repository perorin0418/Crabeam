package org.net.perorin.crablaser.window;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.bind.JAXB;

import org.net.perorin.crablaser.config.Config;
import org.net.perorin.crablaser.config.ConfigCrabLaser;
import org.net.perorin.crablaser.config.Constant;
import org.net.perorin.crablaser.format.EvidenceInformation;
import org.net.perorin.crablaser.logic.LogicCrabLaser;
import org.net.perorin.crablaser.panel.PictureCanvas;
import org.net.perorin.crablaser.poi.FormatLoader;
import org.net.perorin.crablaser.table.EvidenceEditModel;
import org.net.perorin.crablaser.table.EvidenceEditTable;

public class CrabLaserEdit {

	public CrabLaserWindow parent;
	public boolean isParentOnTop;
	public FormatLoader testData;
	public HashMap<String, LinkedList<String>> pictureAfterMap;
	public HashMap<String, LinkedList<String>> pictureBeforeMap;

	public JFrame frame;
	private EvidenceEditTable tableAfter;
	private EvidenceEditModel modelAfter;
	private JPanel mainPanel;
	private JPanel selectPanel;
	private JPanel tableAfterPanel;
	private JPanel picturePanel;
	private JPanel footerPanel;
	private JComboBox<String> comboBox;
	private PictureCanvas pictureCanvas;

	private ArrayList<String> delListAfter;
	private ArrayList<String> delListBefore;
	private JPanel tableBeforePanel;
	private EvidenceEditModel modelBefore;
	private EvidenceEditTable tableBefore;

	public CrabLaserEdit(CrabLaserWindow parent, FormatLoader testData,
			HashMap<String, LinkedList<String>> pictureAfterMap, HashMap<String, LinkedList<String>> pictureBeforeMap) {
		this.parent = parent;
		this.testData = testData;
		this.pictureAfterMap = pictureAfterMap;
		this.pictureBeforeMap = pictureBeforeMap;
		initialize();
		initFramePanel();
		initMainPanel();
		initSelectPanel();
		initTableAfterPanel();
		initTableBeforePanel();
		initPicturePanel();
		initFooterPanel();
		loadAfterEvidenceData();
		loadBeforeEvidenceData();
		initFinal();
	}

	private void initialize() {
		isParentOnTop = parent.frame.isAlwaysOnTop();
		parent.frame.setAlwaysOnTop(false);
		parent.frame.setEnabled(false);
		delListAfter = new ArrayList<>();
		delListBefore = new ArrayList<>();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}
	}

	private void initFramePanel() {

		// 画面の解像度取得
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		DisplayMode displayMode = env.getDefaultScreenDevice().getDisplayMode();
		int width = displayMode.getWidth();
		int height = displayMode.getHeight();

		// 画面のはみ出し量を取得
		int diffWidth = 0;
		if (parent.frame.getX() + 635 > width) {
			diffWidth = parent.frame.getX() + 635 - width;
		}

		int diffHeight = 0;
		if (parent.frame.getY() + 800 > height) {
			diffHeight = parent.frame.getY() + 800 - height;
		}

		frame = new JFrame();
		frame.setBounds(parent.frame.getX() - diffWidth, parent.frame.getY() - diffHeight, 635, 800);
		frame.setAlwaysOnTop(true);
		frame.setResizable(false);
		frame.setIconImage(new ImageIcon(Constant.ICON_PATH).getImage());
		frame.setTitle("編集");
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				parent.frame.setAlwaysOnTop(isParentOnTop);
				parent.frame.setEnabled(true);
				parent.loadPictureMap();
				parent.reloadScreenShotThumb();
			}
		});
	}

	private void initMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setBackground(SystemColor.inactiveCaptionBorder);
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));
	}

	private void initSelectPanel() {
		selectPanel = new JPanel();
		selectPanel.setBackground(SystemColor.inactiveCaptionBorder);
		mainPanel.add(selectPanel, BorderLayout.NORTH);
		selectPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		comboBox = new JComboBox<String>();
		comboBox.setFont(new Font("メイリオ", Font.PLAIN, 12));
		comboBox.setPreferredSize(new Dimension(500, 30));
		selectPanel.add(comboBox);

		for (int i = 0; i < testData.size(); i++) {
			comboBox.addItem(testData.getHeader(i));
		}

	}

	private void initTableAfterPanel() {
		tableAfterPanel = new JPanel();
		tableAfterPanel.setBackground(SystemColor.inactiveCaptionBorder);
		tableAfterPanel.setLayout(new BorderLayout(0, 0));
		tableAfterPanel.setPreferredSize(new Dimension(310, 0));
		mainPanel.add(tableAfterPanel, BorderLayout.WEST);

		JLabel lblTableAfterTitle = new JLabel("修正後");
		lblTableAfterTitle.setFont(new Font("メイリオ", Font.PLAIN, 15));
		lblTableAfterTitle.setHorizontalAlignment(SwingConstants.CENTER);
		tableAfterPanel.add(lblTableAfterTitle, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setBackground(SystemColor.inactiveCaptionBorder);
		tableAfterPanel.add(scrollPane);
		modelAfter = new EvidenceEditModel();
		tableAfter = new EvidenceEditTable(modelAfter) {

			@Override
			public void beforeRowRemove() {
				delListAfter.add(modelAfter.getValueAt(tableAfter.getSelectedRow(), 5).toString());
				delListAfter.add(modelAfter.getValueAt(tableAfter.getSelectedRow(), 6).toString());
			}
		};
		tableAfter.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = tableAfter.getSelectedRow();
				if (tableAfter.getSelectedColumn() == 3) {
					return;
				}
				pictureCanvas.setPicture(modelAfter.getValueAt(row, 6).toString());
				pictureCanvas.refresh();
			}
		});
		scrollPane.setViewportView(tableAfter);
	}

	private void initTableBeforePanel() {
		tableBeforePanel = new JPanel();
		tableBeforePanel.setBackground(SystemColor.inactiveCaptionBorder);
		tableBeforePanel.setLayout(new BorderLayout(0, 0));
		tableBeforePanel.setPreferredSize(new Dimension(310, 0));
		mainPanel.add(tableBeforePanel, BorderLayout.EAST);

		JLabel lblTableBeforeTitle = new JLabel("修正前");
		lblTableBeforeTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTableBeforeTitle.setFont(new Font("メイリオ", Font.PLAIN, 15));
		tableBeforePanel.add(lblTableBeforeTitle, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setBackground(SystemColor.inactiveCaptionBorder);
		tableBeforePanel.add(scrollPane);
		modelBefore = new EvidenceEditModel();
		tableBefore = new EvidenceEditTable(modelBefore) {

			@Override
			public void beforeRowRemove() {
				delListBefore.add(modelBefore.getValueAt(tableBefore.getSelectedRow(), 5).toString());
				delListBefore.add(modelBefore.getValueAt(tableBefore.getSelectedRow(), 6).toString());
			}
		};
		tableBefore.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = tableBefore.getSelectedRow();
				if (tableBefore.getSelectedColumn() == 3) {
					return;
				}
				pictureCanvas.setPicture(modelBefore.getValueAt(row, 6).toString());
				pictureCanvas.refresh();
			}
		});
		scrollPane.setViewportView(tableBefore);
	}

	private void initPicturePanel() {
		pictureCanvas = new PictureCanvas();
		pictureCanvas.setBackground(SystemColor.inactiveCaptionBorder);
		pictureCanvas.setBounds(10, 10, frame.getWidth() - 30);

		picturePanel = new JPanel();
		picturePanel.setBackground(SystemColor.inactiveCaptionBorder);
		picturePanel.setLayout(null);
		picturePanel.setPreferredSize(new Dimension(frame.getWidth(), pictureCanvas.getHeight() + 10));
		mainPanel.add(picturePanel, BorderLayout.SOUTH);

		picturePanel.add(pictureCanvas);
	}

	private void initFooterPanel() {
		footerPanel = new JPanel();
		footerPanel.setBackground(SystemColor.inactiveCaptionBorder);
		frame.getContentPane().add(footerPanel, BorderLayout.SOUTH);
		footerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				apply();
			}
		});
		footerPanel.add(btnApply);

		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				apply();
				parent.frame.setAlwaysOnTop(isParentOnTop);
				parent.frame.setEnabled(true);
				frame.dispose();
				parent.loadPictureMap();
				parent.reloadTestSuite();
			}
		});
		footerPanel.add(btnOK);

		JButton btnCancel = new JButton("Cansel");
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.frame.setAlwaysOnTop(isParentOnTop);
				parent.frame.setEnabled(true);
				frame.dispose();
				parent.loadPictureMap();
				parent.reloadTestSuite();
			}
		});
		footerPanel.add(btnCancel);
	}

	private void initFinal() {
		comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				loadAfterEvidenceData();
				loadBeforeEvidenceData();
			}
		});
	}

	private void loadAfterEvidenceData() {
		modelAfter.setRowCount(0);
		ConfigCrabLaser config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class).getCrablaser();
		ArrayList<EvidenceInformation> list = new ArrayList<EvidenceInformation>();
		File path = new File(config.getSave_path() + "\\" + "after" + "\\" + comboBox.getSelectedItem());
		if (path.exists()) {
			for (File file : path.listFiles()) {
				if (file.getPath().endsWith("info")) {
					EvidenceInformation ei = JAXB.unmarshal(file, EvidenceInformation.class);
					list.add(ei);
				}
			}
		}
		for (int i = 0; i < list.size(); i++) {
			EvidenceInformation ei = LogicCrabLaser.getByOrder(list, i);
			String order = "Ξ";
			String bdd = ei.getBdd();
			String evidenceName = ei.getEvidence_name();
			String infoPath = ei.getImage_name().substring(0,
					ei.getImage_name().length() - config.getSave_type().length())
					+ "info";
			String imagePath = ei.getImage_name();

			Object[] obj = [
					order,
					bdd,
					evidenceName,
					"",
					ei,
					config.getSave_path() + "\\" + "after" + "\\" + comboBox.getSelectedItem() + "\\" + infoPath,
					config.getSave_path() + "\\" + "after" + "\\" + comboBox.getSelectedItem() + "\\" + imagePath
			];
			modelAfter.addRow(obj);
		}
	}

	private void loadBeforeEvidenceData() {
		modelBefore.setRowCount(0);
		ConfigCrabLaser config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class).getCrablaser();
		ArrayList<EvidenceInformation> list = new ArrayList<EvidenceInformation>();
		File path = new File(config.getSave_path() + "\\" + "before" + "\\" + comboBox.getSelectedItem());
		if (path.exists()) {
			for (File file : path.listFiles()) {
				if (file.getPath().endsWith("info")) {
					EvidenceInformation ei = JAXB.unmarshal(file, EvidenceInformation.class);
					list.add(ei);
				}
			}
		}
		for (int i = 0; i < list.size(); i++) {
			EvidenceInformation ei = LogicCrabLaser.getByOrder(list, i);
			String order = "Ξ";
			String bdd = ei.getBdd();
			String evidenceName = ei.getEvidence_name();
			String infoPath = ei.getImage_name().substring(0,
					ei.getImage_name().length() - config.getSave_type().length())
					+ "info";
			String imagePath = ei.getImage_name();

			Object[] obj = [
					order,
					bdd,
					evidenceName,
					"",
					ei,
					config.getSave_path() + "\\" + "before" + "\\" + comboBox.getSelectedItem() + "\\" + infoPath,
					config.getSave_path() + "\\" + "before" + "\\" + comboBox.getSelectedItem() + "\\" + imagePath
			];
			modelBefore.addRow(obj);
		}
	}

	public void setSelect(String select) {
		comboBox.setSelectedItem(select);
	}

	private void apply() {

		// カレントテストを選択中のテストに変更する。
		parent.currentNo = comboBox.getSelectedIndex();
		parent.currentTestSuite.setHeadText(parent.testData.getHeader(parent.currentNo));
		parent.currentTestSuite.setGivenText(parent.testData.getGiven(parent.currentNo));
		parent.currentTestSuite.setWhenText(parent.testData.getWhen(parent.currentNo));
		parent.currentTestSuite.setThenText(parent.testData.getThen(parent.currentNo));

		ConfigCrabLaser config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class).getCrablaser();
		for (String delStr : delListAfter) {
			File delFile = new File(delStr);
			if (delFile.exists()) {
				delFile.delete();
				if (delFile.getPath().endsWith(config.getSave_type())) {
					LinkedList<String> list = pictureAfterMap.get(comboBox.getSelectedItem());
					list.remove(delFile.getPath());
				}
			}
		}
		for (String delStr : delListBefore) {
			File delFile = new File(delStr);
			if (delFile.exists()) {
				delFile.delete();
				if (delFile.getPath().endsWith(config.getSave_type())) {
					LinkedList<String> list = pictureBeforeMap.get(comboBox.getSelectedItem());
					list.remove(delFile.getPath());
				}
			}
		}

		// 変更をinfoファイルに反映する
		Vector rows = modelAfter.getDataVector();
		for (int i = 0; i < rows.size(); i++) {
			try {
				Vector row = (Vector) rows.get(i);
				EvidenceInformation ei = (EvidenceInformation) row.get(4);
				String evidenceName = row.get(2).toString();

				// 順序の更新
				ei.setOrder(i);

				// BDDの更新
				ei.setBdd(row.get(1).toString());

				// エビデンス名称の更新
				ei.setEvidence_name(evidenceName);

				// BDDに対応するテスト内容に更新
				ei.setContents(parent.currentTestSuite.getTestSuiteText(true, row.get(1).toString()));

				// 画像格納場所の更新
				ei.setImage_name(new File(row.get(6).toString()).getName());

				// 出力
				FileOutputStream fos = new FileOutputStream(row.get(5).toString());
				JAXB.marshal(ei, fos);
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		loadAfterEvidenceData();

		// 変更をinfoファイルに反映する
		rows = modelBefore.getDataVector();
		for (int i = 0; i < rows.size(); i++) {
			try {
				Vector row = (Vector) rows.get(i);
				EvidenceInformation ei = (EvidenceInformation) row.get(4);
				String evidenceName = row.get(2).toString();

				// 順序の更新
				ei.setOrder(i);

				// BDDの更新
				ei.setBdd(row.get(1).toString());

				// エビデンス名称の更新
				ei.setEvidence_name(evidenceName);

				// BDDに対応するテスト内容に更新
				ei.setContents(parent.currentTestSuite.getTestSuiteText(true, row.get(1).toString()));

				// 画像格納場所の更新
				ei.setImage_name(new File(row.get(6).toString()).getName());

				// 出力
				FileOutputStream fos = new FileOutputStream(row.get(5).toString());
				JAXB.marshal(ei, fos);
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		loadBeforeEvidenceData();
	}

}
