package org.net.perorin.crabeam.logic;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.net.perorin.crabeam.config.Constant;
import org.net.perorin.crabeam.table.ShortcutModel;
import org.net.perorin.crabeam.window.UpDownButton;

import jp.sf.orangesignal.csv.Csv;
import jp.sf.orangesignal.csv.CsvConfig;
import jp.sf.orangesignal.csv.handlers.StringArrayListHandler;

public class Logic {

	public static void loadFormat(JTextField formatTxt, JPanel upDownPnl, JTextField currentNoTxt) {
		List<String> matchList = match(formatTxt);
		upDownPnl.setVisible(false);
		upDownPnl.removeAll();
		for (String match : matchList) {
			int nod = Integer.parseInt(match.substring(1, match.length()));
			UpDownButton udb = new UpDownButton() {

				@Override
				public void upBtnAction() {
					super.upBtnAction();
					Logic.updateCurrentNo(formatTxt, upDownPnl, currentNoTxt);
				}

				@Override
				public void downBtnAction() {
					super.downBtnAction();
					Logic.updateCurrentNo(formatTxt, upDownPnl, currentNoTxt);
				}
			};
			udb.setNOD(nod);
			upDownPnl.add(udb);
		}
		upDownPnl.setVisible(true);
	}

	public static void updateCurrentNo(JTextField formatTxt, JPanel upDownPnl, JTextField currentNoTxt) {
		String formatStr = convertFormat(formatTxt.getText());
		List<String> noList = new ArrayList<String>();
		Component[] comps = upDownPnl.getComponents();
		for (Component comp : comps) {
			if (comp instanceof UpDownButton) {
				noList.add(((UpDownButton) comp).getNumber());
			}
		}
		String[] noArray = (String[]) noList.toArray(new String[0]);
		currentNoTxt.setText(String.format(formatStr, noArray));
	}

	public static String saveScreenshot(String scCode, JTextField savePathTxt, JTextField currentNoTxt, JTextField givenTxt, JTextField whenTxt, JTextField thenTxt, String saveType) {
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_PRINTSCREEN);
			robot.delay(40);
			robot.keyRelease(KeyEvent.VK_PRINTSCREEN);
			robot.delay(40);
			String ret = "";
			for (int retry = 0; retry < 100; retry++) {
				ret = saveSS(scCode, savePathTxt, currentNoTxt, givenTxt, whenTxt, thenTxt, saveType);
				if (!"".equals(ret)) {
					break;
				} else {
					Thread.sleep(100);
				}
			}
			return ret;
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String saveScreenshotActive(String scCode, JTextField savePathTxt, JTextField currentNoTxt, JTextField givenTxt, JTextField whenTxt, JTextField thenTxt, String saveType) {
		try {
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ALT);
			robot.delay(40);
			robot.keyPress(KeyEvent.VK_PRINTSCREEN);
			robot.delay(40);
			robot.keyRelease(KeyEvent.VK_PRINTSCREEN);
			robot.delay(40);
			robot.keyRelease(KeyEvent.VK_ALT);
			robot.delay(40);
			String ret = "";
			for (int retry = 0; retry < 100; retry++) {
				ret = saveSS(scCode, savePathTxt, currentNoTxt, givenTxt, whenTxt, thenTxt, saveType);
				if (!"".equals(ret)) {
					break;
				} else {
					Thread.sleep(100);
				}
			}
			return ret;
		} catch (AWTException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void updateShortcut(JPanel upDownPnl, ShortcutModel shortcutModel) {
		List<Shortcut> list = new ArrayList<>();
		int upDownCount = 0;
		Vector rows = shortcutModel.getDataVector();
		for (Object row : rows) {
			if (row instanceof Vector) {
				String shortcut = (String) (((Vector) row).elementAt(0));
				String scCode = "";
				if (shortcut.contains("前提条件エビ取得")) {
					scCode = "前提条件エビ取得";
				} else if (shortcut.contains("イベントエビ取得")) {
					scCode = "イベントエビ取得";
				} else if (shortcut.contains("処理結果エビ取得")) {
					scCode = "処理結果エビ取得";
				} else if (shortcut.contains("カウントアップ")) {
					if (Integer.parseInt(String.valueOf(shortcut.charAt(1))) <= upDownPnl.getComponentCount()) {
						scCode = shortcut;
						upDownCount++;
					}
				} else if (shortcut.contains("カウントダウン")) {
					if (Integer.parseInt(String.valueOf(shortcut.charAt(1))) <= upDownPnl.getComponentCount()) {
						scCode = shortcut;
					}
				}

				if (!"".equals(scCode)) {
					int keyCode = Integer.parseInt(String.valueOf(((Vector) row).elementAt(1)));
					int modifies = Integer.parseInt(String.valueOf(((Vector) row).elementAt(2)));
					list.add(new Shortcut(scCode, keyCode, modifies));
				}
			}
		}
		for (int i = upDownCount; i < upDownPnl.getComponentCount(); i++) {
			list.add(new Shortcut("第" + (i + 1) + "番 カウントアップ", 0, 0));
			list.add(new Shortcut("第" + (i + 1) + "番 カウントダウン", 0, 0));
		}
		shortcutModel.setRowCount(0);
		shortcutModel.setRowCount(list.size());
		rows = shortcutModel.getDataVector();
		for (int i = 0; i < list.size(); i++) {
			if (rows.get(i) instanceof Vector) {
				((Vector) rows.get(i)).setElementAt(list.get(i).getShortcut(), 0);
				((Vector) rows.get(i)).setElementAt(list.get(i).getKeyCode(), 1);
				((Vector) rows.get(i)).setElementAt(list.get(i).getModifiers(), 2);
			}
		}
	}

	public static List<Shortcut> loadShortcut(ShortcutModel shortcutModel) {
		List<Shortcut> ret = new ArrayList<>();
		Vector rows = shortcutModel.getDataVector();
		for (Object row : rows) {
			if (row instanceof Vector) {
				String shortcut = (String) (((Vector) row).elementAt(0));
				String scCode = "";
				if (shortcut.contains("前提条件エビ取得")) {
					scCode = "given";
				} else if (shortcut.contains("イベントエビ取得")) {
					scCode = "when";
				} else if (shortcut.contains("処理結果エビ取得")) {
					scCode = "then";
				} else if (shortcut.contains("カウントアップ")) {
					scCode = shortcut.charAt(1) + "_countUp";
				} else if (shortcut.contains("カウントダウン")) {
					scCode = shortcut.charAt(1) + "_countDown";
				}

				if (!"".equals(scCode)) {
					int keyCode = (int) (((Vector) row).elementAt(1));
					int modifier = (int) (((Vector) row).elementAt(2));
					ret.add(new Shortcut(scCode, keyCode, modifier));
				}
			}
		}
		return ret;
	}

	public static ShortcutModel loadShortcutPreset() {
		ShortcutModel model = new ShortcutModel();
		try {
			List<String[]> presets = Csv.load(new File(Constant.PRESET_PATH), new CsvConfig(), new StringArrayListHandler());
			for (int i = 0; i < presets.size() - 1; i++) {
				String[] preset = presets.get(i);
				Object[] obj = { preset[0], Integer.parseInt(preset[1]), Integer.parseInt(preset[2]) };
				model.addRow(obj);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return model;
	}

	public static void saveShortcutPreset(ShortcutModel model) {
		Vector rows = model.getDataVector();
		List<String[]> list = new ArrayList<>();
		for (Object row : rows) {
			if (row instanceof Vector) {
				String shortcut = String.valueOf(((Vector) row).get(0));
				String keyCode = String.valueOf(((Vector) row).get(1));
				String modifier = String.valueOf(((Vector) row).get(2));
				String[] rowStr = { shortcut, keyCode, modifier };
				list.add(rowStr);
			}
		}
		try {
			Csv.save(list, new File(Constant.PRESET_PATH), new CsvConfig(), new StringArrayListHandler());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File getSavePath(String current, JFrame frame) {

		File file = new File("");

		JFileChooser fileChooser = new JFileChooser(current) {
			@Override
			protected JDialog createDialog(Component parent) throws HeadlessException {
				JDialog dialog = super.createDialog(parent);
				dialog.setIconImage(new ImageIcon(Constant.ICON_PATH).getImage());
				dialog.setBounds(new Rectangle(frame.getX() + 10, frame.getY() + 10, (int) dialog.getBounds().getWidth(), (int) dialog.getBounds().getHeight()));
				return dialog;
			}
		};
		;
		fileChooser.setDialogTitle("保存先の設定");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		JFrame f = new JFrame();
		int selected = fileChooser.showOpenDialog(f);
		if (selected == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
		}
		f.dispose();

		return file;
	}

	private static String saveSS(String scCode, JTextField savePathTxt, JTextField currentNoTxt, JTextField givenTxt, JTextField whenTxt, JTextField thenTxt, String saveType) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		BufferedImage img = null;
		try {
			img = (BufferedImage) clipboard.getData(DataFlavor.imageFlavor);
		} catch (Exception e) {
			System.out.println("Clipboard connect waiting");
			return "";
		}
		if (img != null) {
			try {
				StringBuilder fileName = new StringBuilder();
				fileName.append(savePathTxt.getText() + "\\");
				fileName.append(currentNoTxt.getText());
				if ("given".equals(scCode)) {
					fileName.append(givenTxt.getText());
				} else if ("when".equals(scCode)) {
					fileName.append(whenTxt.getText());
				} else if ("then".equals(scCode)) {
					fileName.append(thenTxt.getText());
				}
				if ("png".equals(saveType)) {
					fileName.append(".png");
				} else if ("jpg".equals(saveType)) {
					fileName.append(".jpg");
				} else if ("bmp".equals(saveType)) {
					fileName.append(".bmp");
				}
				File f = new File(fileName.toString());
				ImageIO.write(img, saveType, f);
				System.out.println("save:" + f.getPath());
				return f.getPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	private static List<String> match(JTextField formatTxt) {
		String formatStr = formatTxt.getText();
		String regex = "\\%\\d";
		Pattern p = Pattern.compile(regex);
		List<String> matchList = new ArrayList<String>();
		Matcher m = p.matcher(formatStr);
		while (m.find()) {
			matchList.add(m.group());
		}
		return matchList;
	}

	private static String convertFormat(String str) {
		String ret = str;
		String regex = "\\%\\d";
		Pattern p = Pattern.compile(regex);
		while (true) {
			Matcher m = p.matcher(ret);
			if (m.find()) {
				StringBuilder sb = new StringBuilder();
				sb.append(ret.substring(0, m.start()));
				sb.append("%s");
				sb.append(ret.substring(m.end(), ret.length()));
				ret = sb.toString();
			} else {
				break;
			}
		}
		return ret;
	}
}
