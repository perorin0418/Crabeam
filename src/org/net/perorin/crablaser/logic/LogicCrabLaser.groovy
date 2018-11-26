package org.net.perorin.crablaser.logic

import java.awt.AWTException
import java.awt.Component
import java.awt.HeadlessException
import java.awt.MouseInfo
import java.awt.Point
import java.awt.Rectangle
import java.awt.Robot
import java.awt.image.BufferedImage
import java.text.SimpleDateFormat

import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JDialog
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.xml.bind.JAXB

import org.eclipse.swt.internal.win32.OS
import org.eclipse.swt.internal.win32.RECT
import org.eclipse.swt.internal.win32.TCHAR
import org.net.perorin.crablaser.config.Config
import org.net.perorin.crablaser.config.ConfigCrabLaser
import org.net.perorin.crablaser.config.Constant
import org.net.perorin.crablaser.cv.CV
import org.net.perorin.crablaser.cv.CVImage
import org.net.perorin.crablaser.format.EvidenceInformation
import org.net.perorin.crablaser.format.FolderList
import org.net.perorin.crablaser.poi.FormatLoader
import org.net.perorin.crablaser.window.CrabLaserWindow

public class LogicCrabLaser {

	public static File getExcelPath(String current, JFrame frame) {

		File file = new File("");

		JFileChooser fileChooser = new JFileChooser(current) {
					@Override
					protected JDialog createDialog(Component parent) throws HeadlessException {
						JDialog dialog = super.createDialog(parent);
						dialog.setIconImage(new ImageIcon(Constant.ICON_PATH).getImage());
						dialog.setBounds(new Rectangle(frame.getX() + 10, frame.getY() + 10, (int) dialog.getBounds()
								.getWidth(), (int) dialog.getBounds().getHeight()));
						return dialog;
					}
				};

		fileChooser.setDialogTitle("テスト仕様書の選択");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		JFrame f = new JFrame();
		int selected = fileChooser.showOpenDialog(f);
		if (selected == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
		}
		f.dispose();

		return file;
	}

	public static File getSavePath(String current, JFrame frame) {

		File file = new File("")

		JFileChooser fileChooser = new JFileChooser(current) {
					protected JDialog createDialog(Component parent) throws HeadlessException {
						JDialog dialog = super.createDialog(parent)
						dialog.setIconImage(new ImageIcon(Constant.ICON_PATH).getImage())
						dialog.setBounds(new Rectangle(frame.getX() + 10, frame.getY() + 10, (int) dialog.getBounds().getWidth(), (int) dialog.getBounds().getHeight()))
						return dialog
					}
				}

		fileChooser.setDialogTitle("保存先の設定")
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)

		JFrame f = new JFrame()
		int selected = fileChooser.showOpenDialog(f)
		if (selected == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile()
		}
		f.dispose()

		return file
	}

	public static String screenShot(CrabLaserWindow crablaser) {
		try {
			ConfigCrabLaser config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class).getCrablaser()

			// フルスクリーンショットを撮るか判定
			RECT rect = new RECT()
			if (config.isSave_full()) {
				// デスクトップの解像度を取得
				OS.GetWindowRect(OS.GetDesktopWindow(), rect)
			} else {
				// アクティブウィンドウのハンドルを取得
				int hwnd = OS.GetForegroundWindow()

				// アクティブウィンドウのサイズを取得
				OS.GetWindowRect(hwnd, rect)

				// アクティブウィンドウの名前を取得
				TCHAR buf = new TCHAR(OS.CP_INSTALLED, 1000)
				OS.GetWindowText(hwnd, buf, 1000)

				// アクティブウィンドウの名前の長さを取得
				int length = OS.GetWindowTextLength(hwnd)

				// 蟹レーザーのスクショを撮っているか判定
				if ("蟹レーザー".equals(buf.toString(0, length))) {
					int option = JOptionPane.showConfirmDialog(crablaser.frame,
							"蟹レーザーのスクリーンショットを取得しようとしています。\nよろしいですか？",
							"諫山さんへ", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE)

					if (option == JOptionPane.YES_OPTION) {
						// nop
					} else if (option == JOptionPane.NO_OPTION) {
						return ""
					}
				}
			}
			Robot robot = new Robot()
			BufferedImage img = robot.createScreenCapture(new Rectangle(rect.left, rect.top, rect.right - rect.left,
					rect.bottom - rect.top))
			img = drawCursor(rect, img)

			StringBuffer imgFileName = new StringBuffer()
			imgFileName.append(config.getSave_path())
			imgFileName.append("\\")

			if (crablaser.rdbtnAfter.isSelected()) {
				imgFileName.append("after")
				imgFileName.append("\\")
				new File(imgFileName.toString()).mkdir()
				createFolderList(new File(imgFileName.toString() + "FolderList.xml"), crablaser.testData)
			} else {
				imgFileName.append("before")
				imgFileName.append("\\")
				new File(imgFileName.toString()).mkdir()
				createFolderList(new File(imgFileName.toString() + "FolderList.xml"), crablaser.testData)
			}

			imgFileName.append(crablaser.currentTestSuite.getHeadText())
			File outputFolder = new File(imgFileName.toString())
			outputFolder.mkdir()
			// int fileCount = outputFolder.listFiles().length == 0 ? 0 :
			// outputFolder.listFiles().length / 2

			imgFileName.append("\\")
			imgFileName.append(crablaser.currentTestSuite.getHeadText())
			imgFileName.append("_")
			// imgFileName.append(String.format("%08d", fileCount))
			imgFileName.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Calendar.getInstance().getTime()))

			EvidenceInformation ei = createEvidenceInformation(crablaser, imgFileName.toString(), img)

			if (config.isComp_image()) {
				img = CV.resize(new CVImage(img), ei.getImage_width(), ei.getImage_height()).getImageBuffer()
			}

			imgFileName.append(".")
			imgFileName.append(config.getSave_type())
			ImageIO.write(img, config.getSave_type(), new File(imgFileName.toString()))

			return imgFileName.toString()

		} catch (AWTException e) {
			e.printStackTrace()
		} catch (IOException e) {
			e.printStackTrace()
		}
		return ""
	}

	private static EvidenceInformation createEvidenceInformation(CrabLaserWindow crablaser, String imgFileName,
			BufferedImage img) {
		try {
			ConfigCrabLaser config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class).getCrablaser()
			String fileName = imgFileName.split("\\\\")[imgFileName.split("\\\\").length - 1]

			new File(imgFileName + ".info").createNewFile()
			EvidenceInformation ei = new EvidenceInformation()

			File outputFolder = null
			if (crablaser.rdbtnAfter.isSelected()) {
				outputFolder = new File(config.getSave_path() + "\\" + "after" + "\\"
						+ crablaser.currentTestSuite.getHeadText())
			} else {
				outputFolder = new File(config.getSave_path() + "\\" + "before" + "\\"
						+ crablaser.currentTestSuite.getHeadText())
			}
			int fileCount = outputFolder.listFiles().length == 0 ? 0 : outputFolder.listFiles().length / 2
			ei.setOrder(fileCount)

			ei.setEvidence_name(crablaser.currentTestSuite.getHeadText())
			ei.setImage_name(fileName + "." + config.getSave_type())

			int width_limit = config.getImage_width_limit()
			int height_limit = config.getImage_height_limit()

			double mag_pic = (double) height_limit / (double) img.getHeight()

			if (width_limit < img.getWidth() * mag_pic) {
				mag_pic = (double) width_limit / (double) img.getWidth()
			}

			ei.setImage_width((int) (img.getWidth() * mag_pic))
			ei.setImage_height((int) (img.getHeight() * mag_pic))

			ei.setEvi_no_address(crablaser.testData.getEviNo(crablaser.currentNo))

			ei.setBdd(crablaser.currentTestSuite.getSelect())
			ei.setContents(crablaser.currentTestSuite.getTestSuiteText(true))

			FileOutputStream fos = new FileOutputStream(imgFileName + ".info")
			JAXB.marshal(ei, fos)
			fos.close()

			return ei
		} catch (IOException e) {
			e.printStackTrace()
		}

		return null
	}

	private static void createFolderList(File folderList, FormatLoader testData) {
		try {
			ArrayList<String> list = new ArrayList<>()
			for (int i = 0; i < testData.size(); i++) {
				list.add(testData.getHeader(i))
			}
			FolderList fl = new FolderList()
			fl.setFolder(list)
			FileOutputStream fos = new FileOutputStream(folderList)
			JAXB.marshal(fl, fos)
			fos.close()

		} catch (FileNotFoundException e) {
			e.printStackTrace()
		} catch (IOException e) {
			e.printStackTrace()
		}
	}

	private static BufferedImage drawCursor(RECT rect, BufferedImage img) {
		ConfigCrabLaser config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class).getCrablaser()
		CVImage img1 = new CVImage(img)
		CVImage img2 = new CVImage(Constant.CURSOR_DEFAULT)
		Point point = MouseInfo.getPointerInfo().getLocation()
		point.x = point.x - rect.left
		point.y = point.y - rect.top
		switch (config.getCursor_type()) {
			case "DEFAULT":
				img2 = new CVImage(Constant.CURSOR_DEFAULT)
				break

			case "HAND":
				img2 = new CVImage(Constant.CURSOR_HAND)
				point.x = point.x - 6
				point.y = point.y - 1
				break

			case "BUSY":
				img2 = new CVImage(Constant.CURSOR_BUSY)
				point.x = point.x - 10
				point.y = point.y - 10
				break

			case "MOVE":
				img2 = new CVImage(Constant.CURSOR_MOVE)
				point.x = point.x - 11
				point.y = point.y - 11
				break

			case "EXPAND_VERTICAL":
				img2 = new CVImage(Constant.CURSOR_EXPAND_VERTICAL)
				point.x = point.x - 4
				point.y = point.y - 10
				break

			case "EXPAND_HORIZONTAL":
				img2 = new CVImage(Constant.CURSOR_EXPAND_HORIZONTAL)
				point.x = point.x - 11
				point.y = point.y - 4
				break

			case "EXPAND_VER_HOR":
				img2 = new CVImage(Constant.CURSOR_EXPAND_VER_HOR)
				point.x = point.x - 8
				point.y = point.y - 7
				break

			case "EXPAND_HOR_VER":
				img2 = new CVImage(Constant.CURSOR_EXPAND_HOR_VER)
				point.x = point.x - 8
				point.y = point.y - 8
				break

			case "BEAM":
				img2 = new CVImage(Constant.CURSOR_BEAM)
				point.x = point.x - 4
				point.y = point.y - 9
				break

			case "CROSS":
				img2 = new CVImage(Constant.CURSOR_CROSS)
				point.x = point.x - 8
				point.y = point.y - 8
				break

			default:
				return img1.getImageBuffer()
		}
		img1 = CV.merge(img1, img2, point)
		return img1.getImageBuffer()
	}

	public static HashMap<String, LinkedList<String>> loadPictureMap(FormatLoader testData, String boa) {
		ConfigCrabLaser config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class).getCrablaser()
		HashMap<String, LinkedList<String>> ret = new HashMap<String, LinkedList<String>>()

		for (int i = 0; i < testData.size(); i++) {
			File path = new File(config.getSave_path() + "\\" + boa + "\\" + testData.getHeader(i))
			if (path.exists()) {
				ArrayList<EvidenceInformation> eiList = new ArrayList<>()
				for (File file : path.listFiles()) {
					if (file.getPath().endsWith("info")) {
						eiList.add(JAXB.unmarshal(file, EvidenceInformation.class))
					}
				}
				LinkedList<String> list = new LinkedList<>()
				for (int j = 0; j < eiList.size(); j++) {
					EvidenceInformation ei = getByOrder(eiList, j)
					list.add(path.getPath() + "\\" + ei.getImage_name())
				}
				ret.put(testData.getHeader(i), list)
			}
		}

		return ret
	}

	public static EvidenceInformation getByOrder(ArrayList<EvidenceInformation> list, int index) {
		for (EvidenceInformation ei : list) {
			if (ei.getOrder() == index) {
				return ei
			}
		}
		return null
	}

}
