package org.net.perorin.crabeam.window;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.bind.JAXB;

import org.net.perorin.crabeam.config.Config;
import org.net.perorin.crabeam.config.ConfigCrabLaser;
import org.net.perorin.crabeam.config.Constant;
import org.net.perorin.crabeam.format.EvidenceInformation;
import org.net.perorin.crabeam.logic.LogicCrabLaser;
import org.net.perorin.crabeam.poi.FormatLoader;
import org.net.perorin.crabeam.table.EvidenceEditModel;
import org.net.perorin.crabeam.table.EvidenceEditTable;

public class CrabLaserEdit {

	public CrabLaserWindow parent;
	public boolean isParentOnTop;
	public FormatLoader testData;
	public HashMap<String, LinkedList<String>> pictureMap;

	public JFrame frame;
	private EvidenceEditTable table;
	private EvidenceEditModel model;
	private JPanel mainPanel;
	private JPanel selectPanel;
	private JPanel tablePanel;
	private JPanel picturePanel;
	private JPanel footerPanel;
	private JComboBox<String> comboBox;
	private PictureCanvas pictureCanvas;

	private ArrayList<String> delList;

	public CrabLaserEdit(CrabLaserWindow parent, FormatLoader testData, HashMap<String, LinkedList<String>> pictureMap) {
		this.parent = parent;
		this.testData = testData;
		this.pictureMap = pictureMap;
		initialize();
		initFramePanel();
		initMainPanel();
		initSelectPanel();
		initTablePanel();
		initPicturePanel();
		initFooterPanel();
		loadEvidenceData();
		initFinal();
	}

	private void initialize() {
		isParentOnTop = parent.frame.isAlwaysOnTop();
		parent.frame.setAlwaysOnTop(false);
		parent.frame.setEnabled(false);
		delList = new ArrayList<>();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}
	}

	private void initFramePanel() {
		frame = new JFrame();
		frame.setBounds(parent.frame.getX(), parent.frame.getY(), 320, 500);
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
		FlowLayout flowLayout = (FlowLayout) selectPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		selectPanel.setBackground(SystemColor.inactiveCaptionBorder);
		mainPanel.add(selectPanel, BorderLayout.NORTH);

		comboBox = new JComboBox<String>();
		selectPanel.add(comboBox);

		for (int i = 0; i < testData.size(); i++) {
			comboBox.addItem(testData.getHeader(i));
		}

	}

	private void initTablePanel() {
		tablePanel = new JPanel();
		tablePanel.setBackground(SystemColor.inactiveCaptionBorder);
		mainPanel.add(tablePanel, BorderLayout.CENTER);
		tablePanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setBackground(SystemColor.inactiveCaptionBorder);
		tablePanel.add(scrollPane);
		model = new EvidenceEditModel();
		table = new EvidenceEditTable(model) {

			@Override
			public void beforeRowRemove() {
				delList.add(model.getValueAt(table.getSelectedRow(), 5).toString());
				delList.add(model.getValueAt(table.getSelectedRow(), 6).toString());
			}
		};
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				if (table.getSelectedColumn() == 3) {
					return;
				}
				pictureCanvas.setPicture(model.getValueAt(row, 6).toString());
				pictureCanvas.refresh();
			}
		});
		scrollPane.setViewportView(table);
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
				parent.reloadScreenShotThumb();
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
				parent.reloadScreenShotThumb();
			}
		});
		footerPanel.add(btnCancel);
	}

	private void initFinal() {
		comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				loadEvidenceData();
			}
		});
	}

	private void loadEvidenceData() {
		model.setRowCount(0);
		ConfigCrabLaser config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class).getCrablaser();
		ArrayList<EvidenceInformation> list = new ArrayList<EvidenceInformation>();
		File path = new File(config.getSave_path() + "\\" + comboBox.getSelectedItem());
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

			Object[] obj = {
					order,
					bdd,
					evidenceName,
					"",
					ei,
					config.getSave_path() + "\\" + comboBox.getSelectedItem() + "\\" + evidenceName + ".info",
					config.getSave_path() + "\\" + comboBox.getSelectedItem() + "\\" + evidenceName + "." + config.getSave_type()
			};
			model.addRow(obj);
		}
	}

	public void setSelect(String select) {
		comboBox.setSelectedItem(select);
	}

	private void apply() {
		ConfigCrabLaser config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class).getCrablaser();
		for (String delStr : delList) {
			File delFile = new File(delStr);
			if (delFile.exists()) {
				delFile.delete();
				if (delFile.getPath().endsWith(config.getSave_type())) {
					LinkedList<String> list = pictureMap.get(comboBox.getSelectedItem());
					list.remove(delFile.getPath());
				}
			}
		}

		Vector rows = model.getDataVector();
		for (int i = 0; i < rows.size(); i++) {
			try {
				Vector row = (Vector) rows.get(i);
				EvidenceInformation ei = (EvidenceInformation) row.get(4);
				String evidenceName = row.get(2).toString();
				ei.setOrder(i);
				ei.setBdd(row.get(1).toString());
				ei.setEvidence_name(evidenceName);
				ei.setImage_name(evidenceName + "." + config.getSave_type());
				FileOutputStream fos = new FileOutputStream(row.get(5).toString());
				JAXB.marshal(ei, fos);
				fos.close();
				File infoFile = new File(row.get(5).toString());
				File imgFile = new File(row.get(6).toString());
				infoFile.renameTo(new File(infoFile.getParent() + "\\" + evidenceName + ".info"));
				imgFile.renameTo(new File(imgFile.getParent() + "\\" + evidenceName + "." + config.getSave_type()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		loadEvidenceData();
	}

}
