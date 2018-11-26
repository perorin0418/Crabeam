package org.net.perorin.crablaser.panel

import java.awt.Font
import java.awt.FontFormatException

import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JLayeredPane
import javax.swing.JTextArea

import org.net.perorin.crablaser.config.Constant
import org.net.perorin.crablaser.cv.CV
import org.net.perorin.crablaser.cv.CVImage
import org.net.perorin.crablaser.logic.CacheManeger

public class BddByImage extends JLayeredPane {

	private String bdd
	private CVImage fusenImg
	private JLabel fusenLbl
	private CVImage selectImg
	private JLabel selectLbl
	private JTextArea textArea
	private boolean isSelect = false
	private int height

	private final int IMAGE_LAYER = 0
	private final int TEXTAREA_LAYER = 1
	private final int SELECT_LAYER = 2

	public BddByImage(String bdd) {
		this.bdd = bdd
		switch (bdd) {
		case "Given":
			this.fusenImg = new CVImage(Constant.TESTSUITE_GIVEN_PATH)
			break

		case "When":
			this.fusenImg = new CVImage(Constant.TESTSUITE_WHEN_PATH)
			break

		case "Then":
			this.fusenImg = new CVImage(Constant.TESTSUITE_THEN_PATH)
			break

		default:
			break
		}

		fusenLbl = new JLabel()
		this.add(fusenLbl)
		this.setLayer(fusenLbl, IMAGE_LAYER)

		textArea = new JTextArea()
		textArea.setOpaque(false)
		textArea.setLineWrap(true)
		textArea.setEditable(false)
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, new File(Constant.TANUKI_FONT_PATH))
			textArea.setFont(font.deriveFont(16f))
		} catch (FontFormatException | IOException e) {
			textArea.setFont(new Font("MS UI Gothic", Font.PLAIN, 16))
		}
		this.add(textArea)
		this.setLayer(textArea, TEXTAREA_LAYER)

		this.selectImg = new CVImage(Constant.TESTSUITE_SELECTION_PATH)
		selectLbl = new JLabel()
		selectLbl.setVisible(isSelect)
		this.add(selectLbl)
		this.setLayer(selectLbl, SELECT_LAYER)
	}

	/**
	 * バウンズ設定
	 * <p>
	 * ※高さは自動設定
	 *
	 * @param x
	 * @param y
	 * @param width
	 */
	public void setBounds(int x, int y, int width) {
		super.setBounds(x, y, width, 0)

		textArea.setBounds(20, 20, width - 40, 100)
		height = (int) textArea.getPreferredSize().getHeight() + 40
		super.setSize(width, height)
		textArea.setBounds(20, 20, width - 40, height)

		fusenLbl.setBounds(0, 0, width, height)
		CVImage cache_fusen = CacheManeger.uncacheImage(bdd + width + height)
		if (cache_fusen != null) {
			fusenImg = cache_fusen
		} else {
			fusenImg = CV.resize(fusenImg, width, height)
			CacheManeger.cacheImage(bdd + width + height, fusenImg)
		}
		fusenLbl.setIcon(new ImageIcon(fusenImg.getImageBuffer()))

		selectLbl.setBounds(30, 5, width - 60, height - 10)
		CVImage cache_select = CacheManeger.uncacheImage("select" + (width - 60) + (height - 10))
		if (cache_select != null) {
			selectImg = cache_select
		} else {
			selectImg = CV.resize(selectImg, width - 60, height - 10)
			CacheManeger.cacheImage("select" + (width - 60) + (height - 10), selectImg)
		}
		selectLbl.setIcon(new ImageIcon(selectImg.getImageBuffer()))
	}

	public int getPreferredHeight() {
		return height
	}

	public boolean isSelect() {
		return isSelect
	}

	public void select() {
		this.isSelect = true
		selectLbl.setVisible(isSelect)
	}

	public void deselect() {
		this.isSelect = false
		selectLbl.setVisible(isSelect)
	}

	public void setText(String text) {
		textArea.setText(text)
	}

	public String getText(){
		return textArea.getText()
	}

}
