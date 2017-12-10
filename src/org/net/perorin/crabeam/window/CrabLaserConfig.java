package org.net.perorin.crabeam.window;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.xml.bind.JAXB;

import org.net.perorin.crabeam.config.Config;
import org.net.perorin.crabeam.config.ConfigCrabLaser;
import org.net.perorin.crabeam.config.Constant;
import org.net.perorin.crabeam.logic.LogicCrabeam;

public class CrabLaserConfig {

	public CrabLaserWindow parent;

	public JFrame frame;
	private JLabel lblSavePath;
	private JTextField txtFldSavePath;
	private JButton btnSavePath;
	private JCheckBox chckbxOnTop;
	private JCheckBox chckbxSaveFull;
	private JLabel lblCusorType;
	private JComboBox<String> cmbCusorType;
	private JCheckBox chckbxCompImage;
	private JLabel lblImageWidth;
	private JTextField txtFldImageWidth;
	private JLabel lblImageHeight;
	private JTextField txtFldImageHeight;
	private JButton btnCancel;
	private JButton btnOk;
	private JButton btnApply;

	public CrabLaserConfig(CrabLaserWindow parent) {
		this.parent = parent;
		initialize();
		loadConfig();
	}

	private void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		parent.frame.setAlwaysOnTop(false);
		parent.frame.setEnabled(false);

		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.inactiveCaptionBorder);
		frame.setBounds(parent.frame.getX(), parent.frame.getY(), 280, 231);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setIconImage(new ImageIcon(Constant.ICON_PATH).getImage());
		frame.setTitle("設定");
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				parent.frame.setAlwaysOnTop(chckbxOnTop.isSelected());
				parent.frame.setEnabled(true);
				parent.loadPictureMap();
				parent.reloadScreenShotThumb();
			}
		});

		lblSavePath = new JLabel("保存先");
		lblSavePath.setFont(new Font("メイリオ", Font.PLAIN, 12));
		lblSavePath.setBounds(8, 10, 36, 13);
		frame.getContentPane().add(lblSavePath);

		txtFldSavePath = new JTextField();
		txtFldSavePath.setBounds(48, 6, 168, 19);
		frame.getContentPane().add(txtFldSavePath);
		txtFldSavePath.setColumns(10);

		btnSavePath = new JButton("...");
		btnSavePath.setFont(new Font("メイリオ", Font.PLAIN, 12));
		btnSavePath.setBounds(218, 6, 44, 21);
		btnSavePath.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String path = LogicCrabeam.getSavePath(txtFldSavePath.getText(), frame).getPath();
				if (!"".equals(path)) {
					txtFldSavePath.setText(path);
				}
			}
		});
		frame.getContentPane().add(btnSavePath);

		chckbxOnTop = new JCheckBox("常に最前面");
		chckbxOnTop.setBackground(SystemColor.inactiveCaptionBorder);
		chckbxOnTop.setFont(new Font("メイリオ", Font.PLAIN, 12));
		chckbxOnTop.setBounds(8, 29, 103, 21);
		frame.getContentPane().add(chckbxOnTop);

		chckbxSaveFull = new JCheckBox("全画面保存");
		chckbxSaveFull.setBackground(SystemColor.inactiveCaptionBorder);
		chckbxSaveFull.setFont(new Font("メイリオ", Font.PLAIN, 12));
		chckbxSaveFull.setBounds(8, 52, 103, 21);
		frame.getContentPane().add(chckbxSaveFull);

		lblCusorType = new JLabel("カーソルタイプ");
		lblCusorType.setFont(new Font("メイリオ", Font.PLAIN, 12));
		lblCusorType.setBounds(8, 79, 84, 13);
		frame.getContentPane().add(lblCusorType);

		cmbCusorType = new JComboBox<String>();
		cmbCusorType.setBounds(104, 75, 158, 19);
		cmbCusorType.addItem("NONE");
		cmbCusorType.addItem("DEFAULT");
		cmbCusorType.addItem("HAND");
		cmbCusorType.addItem("BUSY");
		cmbCusorType.addItem("MOVE");
		cmbCusorType.addItem("EXPAND_VERTICAL");
		cmbCusorType.addItem("EXPAND_HORIZONTAL");
		cmbCusorType.addItem("EXPAND_VER_HOR");
		cmbCusorType.addItem("EXPAND_HOR_VER");
		cmbCusorType.addItem("BEAM");
		cmbCusorType.addItem("CROSS");
		frame.getContentPane().add(cmbCusorType);

		chckbxCompImage = new JCheckBox("画像をリサイズして保存");
		chckbxCompImage.setBackground(SystemColor.inactiveCaptionBorder);
		chckbxCompImage.setFont(new Font("メイリオ", Font.PLAIN, 12));
		chckbxCompImage.setBounds(8, 98, 158, 21);
		frame.getContentPane().add(chckbxCompImage);

		lblImageWidth = new JLabel("画像幅最大値");
		lblImageWidth.setFont(new Font("メイリオ", Font.PLAIN, 12));
		lblImageWidth.setBackground(SystemColor.inactiveCaptionBorder);
		lblImageWidth.setBounds(8, 125, 72, 13);
		frame.getContentPane().add(lblImageWidth);

		txtFldImageWidth = new JTextField();
		txtFldImageWidth.setBounds(97, 121, 165, 19);
		txtFldImageWidth.setColumns(10);
		txtFldImageWidth.setDocument(new PlainDocument() {
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
		frame.getContentPane().add(txtFldImageWidth);

		lblImageHeight = new JLabel("画像高さ最大値");
		lblImageHeight.setFont(new Font("メイリオ", Font.PLAIN, 12));
		lblImageHeight.setBackground(SystemColor.inactiveCaptionBorder);
		lblImageHeight.setBounds(8, 148, 84, 13);
		frame.getContentPane().add(lblImageHeight);

		txtFldImageHeight = new JTextField();
		txtFldImageHeight.setBounds(97, 144, 165, 19);
		txtFldImageHeight.setColumns(10);
		txtFldImageHeight.setDocument(new PlainDocument() {
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
		frame.getContentPane().add(txtFldImageHeight);

		btnApply = new JButton("Apply");
		btnApply.setBounds(72, 171, 67, 21);
		btnApply.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveConfig();
			}
		});
		frame.getContentPane().add(btnApply);

		btnOk = new JButton("OK");
		btnOk.setBounds(140, 171, 49, 21);
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveConfig();
				parent.frame.setAlwaysOnTop(chckbxOnTop.isSelected());
				parent.frame.setEnabled(true);
				frame.dispose();
				parent.loadPictureMap();
				parent.reloadScreenShotThumb();
			}
		});
		frame.getContentPane().add(btnOk);

		btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("メイリオ", Font.PLAIN, 12));
		btnCancel.setBounds(190, 171, 72, 21);
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.frame.setAlwaysOnTop(chckbxOnTop.isSelected());
				parent.frame.setEnabled(true);
				frame.dispose();
				parent.loadPictureMap();
				parent.reloadScreenShotThumb();
			}
		});
		frame.getContentPane().add(btnCancel);
	}

	private void loadConfig() {
		ConfigCrabLaser config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class).getCrablaser();
		txtFldSavePath.setText(config.getSave_path());
		chckbxOnTop.setSelected(config.isOn_top());
		chckbxSaveFull.setSelected(config.isSave_full());
		cmbCusorType.setSelectedItem(config.getCursor_type());
		chckbxCompImage.setSelected(config.isComp_image());
		txtFldImageWidth.setText(config.getImage_width_limit() + "");
		txtFldImageHeight.setText(config.getImage_height_limit() + "");
	}

	private void saveConfig() {
		ConfigCrabLaser config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class).getCrablaser();
		config.setSave_path(txtFldSavePath.getText());
		config.setOn_top(chckbxOnTop.isSelected());
		config.setSave_full(chckbxSaveFull.isSelected());
		config.setCursor_type((String) cmbCusorType.getSelectedItem());
		config.setComp_image(chckbxCompImage.isSelected());
		config.setImage_width_limit(Integer.parseInt(txtFldImageWidth.getText()));
		config.setImage_height_limit(Integer.parseInt(txtFldImageHeight.getText()));
		try {
			Config buf_config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class);
			buf_config.setCrablaser(config);
			FileOutputStream fos = new FileOutputStream(Config.CONFIG_PATH);
			JAXB.marshal(buf_config, fos);
			fos.close();
			parent.config = config;
		} catch (FileNotFoundException fne) {
			fne.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
