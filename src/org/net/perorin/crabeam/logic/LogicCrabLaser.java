package org.net.perorin.crabeam.logic;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXB;

import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.RECT;
import org.net.perorin.crabeam.config.Config;
import org.net.perorin.crabeam.config.ConfigCrabLaser;
import org.net.perorin.crabeam.config.Constant;
import org.net.perorin.crabeam.cv.CV;
import org.net.perorin.crabeam.cv.CVImage;
import org.net.perorin.crabeam.format.EvidenceInformation;
import org.net.perorin.crabeam.format.FolderList;
import org.net.perorin.crabeam.poi.FormatLoader;
import org.net.perorin.crabeam.window.CrabLaserWindow;

public class LogicCrabLaser {

	public static String screenShot(CrabLaserWindow crablaser) {
		try {
			ConfigCrabLaser config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class).getCrablaser();
			RECT rect = new RECT();
			if (config.isSave_full()) {
				OS.GetWindowRect(OS.GetDesktopWindow(), rect);
			} else {
				OS.GetWindowRect(OS.GetForegroundWindow(), rect);
			}
			Robot robot = new Robot();
			BufferedImage img = robot.createScreenCapture(new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top));
			img = drawCursor(rect, img);

			StringBuffer imgFileName = new StringBuffer();
			imgFileName.append(config.getSave_path());
			imgFileName.append("\\");

			if (crablaser.rdbtnAfter.isSelected()) {
				imgFileName.append("after");
				imgFileName.append("\\");
				new File(imgFileName.toString()).mkdir();
				createFolderList(new File(imgFileName.toString() + "FolderList.xml"), crablaser.testData);
			} else {
				imgFileName.append("before");
				imgFileName.append("\\");
				new File(imgFileName.toString()).mkdir();
				createFolderList(new File(imgFileName.toString() + "FolderList.xml"), crablaser.testData);
			}

			imgFileName.append(crablaser.currentTestSuite.getHeadText());
			File outputFolder = new File(imgFileName.toString());
			outputFolder.mkdir();
			//			int fileCount = outputFolder.listFiles().length == 0 ? 0 : outputFolder.listFiles().length / 2;

			imgFileName.append("\\");
			imgFileName.append(crablaser.currentTestSuite.getHeadText());
			imgFileName.append("_");
			//			imgFileName.append(String.format("%08d", fileCount));
			imgFileName.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(Calendar.getInstance().getTime()));

			EvidenceInformation ei = createEvidenceInformation(crablaser, imgFileName.toString(), img);

			if (config.isComp_image()) {
				img = CV.resize(new CVImage(img), ei.getImage_width(), ei.getImage_height()).getImageBuffer();
			}

			imgFileName.append(".");
			imgFileName.append(config.getSave_type());
			ImageIO.write(img, config.getSave_type(), new File(imgFileName.toString()));

			return imgFileName.toString();

		} catch (AWTException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	private static EvidenceInformation createEvidenceInformation(CrabLaserWindow crablaser, String imgFileName, BufferedImage img) {
		try {
			ConfigCrabLaser config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class).getCrablaser();
			String fileName = imgFileName.split("\\\\")[imgFileName.split("\\\\").length - 1];

			new File(imgFileName + ".info").createNewFile();
			EvidenceInformation ei = new EvidenceInformation();

			File outputFolder = null;
			if (crablaser.rdbtnAfter.isSelected()) {
				outputFolder = new File(config.getSave_path() + "\\" + "after" + "\\" + crablaser.currentTestSuite.getHeadText());
			} else {
				outputFolder = new File(config.getSave_path() + "\\" + "before" + "\\" + crablaser.currentTestSuite.getHeadText());
			}
			int fileCount = outputFolder.listFiles().length == 0 ? 0 : outputFolder.listFiles().length / 2;
			ei.setOrder(fileCount);

			ei.setEvidence_name(fileName);
			ei.setImage_name(fileName + "." + config.getSave_type());

			int width_limit = config.getImage_width_limit();
			int height_limit = config.getImage_height_limit();

			double mag_pic = (double) height_limit / (double) img.getHeight();

			if (width_limit < img.getWidth() * mag_pic) {
				mag_pic = (double) width_limit / (double) img.getWidth();
			}

			ei.setImage_width((int) (img.getWidth() * mag_pic));
			ei.setImage_height((int) (img.getHeight() * mag_pic));

			ei.setBdd(crablaser.currentTestSuite.getSelect());
			ei.setContents(crablaser.currentTestSuite.getTestSuiteText(true));

			FileOutputStream fos = new FileOutputStream(imgFileName + ".info");
			JAXB.marshal(ei, fos);
			fos.close();

			return ei;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static void createFolderList(File folderList, FormatLoader testData) {
		try {
			if (!folderList.exists()) {
				ArrayList<String> list = new ArrayList<>();
				for (int i = 0; i < testData.size(); i++) {
					list.add(testData.getHeader(i));
				}
				FolderList fl = new FolderList();
				fl.setFolder(list);
				FileOutputStream fos = new FileOutputStream(folderList);
				JAXB.marshal(fl, fos);
				fos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static BufferedImage drawCursor(RECT rect, BufferedImage img) {
		ConfigCrabLaser config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class).getCrablaser();
		CVImage img1 = new CVImage(img);
		CVImage img2 = new CVImage(Constant.CURSOR_DEFAULT);
		Point point = MouseInfo.getPointerInfo().getLocation();
		point.x = point.x - rect.left;
		point.y = point.y - rect.top;
		switch (config.getCursor_type()) {
		case "DEFAULT":
			img2 = new CVImage(Constant.CURSOR_DEFAULT);
			break;

		case "HAND":
			img2 = new CVImage(Constant.CURSOR_HAND);
			point.x = point.x - 6;
			point.y = point.y - 1;
			break;

		case "BUSY":
			img2 = new CVImage(Constant.CURSOR_BUSY);
			point.x = point.x - 10;
			point.y = point.y - 10;
			break;

		case "MOVE":
			img2 = new CVImage(Constant.CURSOR_MOVE);
			point.x = point.x - 11;
			point.y = point.y - 11;
			break;

		case "EXPAND_VERTICAL":
			img2 = new CVImage(Constant.CURSOR_EXPAND_VERTICAL);
			point.x = point.x - 4;
			point.y = point.y - 10;
			break;

		case "EXPAND_HORIZONTAL":
			img2 = new CVImage(Constant.CURSOR_EXPAND_HORIZONTAL);
			point.x = point.x - 11;
			point.y = point.y - 4;
			break;

		case "EXPAND_VER_HOR":
			img2 = new CVImage(Constant.CURSOR_EXPAND_VER_HOR);
			point.x = point.x - 8;
			point.y = point.y - 7;
			break;

		case "EXPAND_HOR_VER":
			img2 = new CVImage(Constant.CURSOR_EXPAND_HOR_VER);
			point.x = point.x - 8;
			point.y = point.y - 8;
			break;

		case "BEAM":
			img2 = new CVImage(Constant.CURSOR_BEAM);
			point.x = point.x - 4;
			point.y = point.y - 9;
			break;

		case "CROSS":
			img2 = new CVImage(Constant.CURSOR_CROSS);
			point.x = point.x - 8;
			point.y = point.y - 8;
			break;

		default:
			return img1.getImageBuffer();
		}
		img1 = CV.merge(img1, img2, point);
		return img1.getImageBuffer();
	}

	public static HashMap<String, LinkedList<String>> loadPictureMap(FormatLoader testData, String boa) {
		ConfigCrabLaser config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class).getCrablaser();
		HashMap<String, LinkedList<String>> ret = new HashMap<String, LinkedList<String>>();

		for (int i = 0; i < testData.size(); i++) {
			File path = new File(config.getSave_path() + "\\" + boa + "\\" + testData.getHeader(i));
			if (path.exists()) {
				ArrayList<EvidenceInformation> eiList = new ArrayList<>();
				for (File file : path.listFiles()) {
					if (file.getPath().endsWith("info")) {
						eiList.add(JAXB.unmarshal(file, EvidenceInformation.class));
					}
				}
				LinkedList<String> list = new LinkedList<>();
				for (int j = 0; j < eiList.size(); j++) {
					EvidenceInformation ei = getByOrder(eiList, j);
					list.add(path.getPath() + "\\" + ei.getImage_name());
				}
				ret.put(testData.getHeader(i), list);
			}
		}

		return ret;
	}

	public static EvidenceInformation getByOrder(ArrayList<EvidenceInformation> list, int index) {
		for (EvidenceInformation ei : list) {
			if (ei.getOrder() == index) {
				return ei;
			}
		}
		return null;
	}

}
