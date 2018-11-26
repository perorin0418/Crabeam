package org.net.perorin.crablaser.panel

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension

import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JLayeredPane
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.ScrollPaneConstants
import javax.swing.border.EmptyBorder
import javax.xml.bind.JAXB

import org.net.perorin.crablaser.config.Constant
import org.net.perorin.crablaser.format.TestSuiteFormat

public class TestSuitePanel extends JLayeredPane {

	private JScrollPane scroll
	private JPanel labelSuite
	private HeaderByImage header
	private BddByImage given
	private BddByImage when
	private BddByImage then
	private PictureCanvas picture
	private TestSuiteFormat format
	private JPanel waitPanel
	private JLabel waitLbl

	public TestSuitePanel(String format) {
		super()
		this.setLayout(null)
		this.setOpaque(false)

		this.format = JAXB.unmarshal(new File(format), TestSuiteFormat.class)

		scroll = new JScrollPane()
		scroll.setOpaque(false)
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED)
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)
		scroll.setBorder(new EmptyBorder(0, 0, 0, 0))
		scroll.getVerticalScrollBar().setUnitIncrement(5)
		this.add(scroll, BorderLayout.CENTER)
		this.setLayer(scroll, 0)

		labelSuite = new JPanel()
		labelSuite.setLayout(null)
		labelSuite.setOpaque(false)
		scroll.getViewport().add(labelSuite)
		scroll.getViewport().setOpaque(false)

		header = new HeaderByImage()
		given = new BddByImage("Given")
		when = new BddByImage("When")
		then = new BddByImage("Then")

		labelSuite.add(header)
		labelSuite.add(given)
		labelSuite.add(when)
		labelSuite.add(then)

		JLabel castFrame = new JLabel(new ImageIcon(Constant.CAST_WAPPEN_PATH))
		castFrame.setBounds(20, 10, 50, 50)
		castFrame.setToolTipText("スクリーンショットを撮るときは、プリントスクリーンキーですよ。")
		labelSuite.add(castFrame)

		JLabel castImg = new JLabel(new ImageIcon(ImageRandomizer.getCastImagePath()))
		castImg.setBounds(20, 10, 50, 50)
		labelSuite.add(castImg)

		picture = new PictureCanvas()
		labelSuite.add(picture)

		waitPanel = new JPanel()
		waitPanel.setBackground(new Color(252, 254, 252))
		waitPanel.setVisible(false)
		waitLbl = new JLabel(new ImageIcon(Constant.LOADING_GIF))
		waitPanel.add(waitLbl, BorderLayout.CENTER)
		this.add(waitPanel)
		this.setLayer(waitPanel, 1)
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height)
		scroll.setBounds(x, y, width, height)

		header.setBounds(80, 10, width - 120, 50)

		given.setBounds(10, 70, width - 40)
		int height_given = given.getPreferredHeight()

		when.setBounds(10, 80 + height_given, width - 40)
		int height_when = when.getPreferredHeight()

		then.setBounds(10, 90 + height_given + height_when, width - 40)
		int height_then = then.getPreferredHeight()

		picture.setBounds(10, 100 + height_given + height_when + height_then, width - 40)
		int height_picture = picture.getPreferredHeight()

		labelSuite.setBounds(0, 0, width, 110 + height_given + height_when + height_then + height_picture)
		labelSuite.setPreferredSize(new Dimension(width, 110 + height_given + height_when + height_then + height_picture))

		waitPanel.setBounds(0, (int)(height / 2 - 220), width, 440)
	}

	public void setHeadText(String text) {
		header.setText(text)
	}

	public void setGivenText(String text) {
		given.setText(text)
	}

	public void setWhenText(String text) {
		when.setText(text)
	}

	public void setThenText(String text) {
		then.setText(text)
	}

	public void setPicture(String imgPath) {
		picture.setPicture(imgPath)
	}

	public void setWait(boolean wait) {
		waitPanel.setVisible(wait)
	}

	public void pictureRefresh() {
		picture.refresh()
	}

	public String getHeadText() {
		return header.getText()
	}

	public void toggleSelect() {
		if (given.isSelect()) {
			given.deselect()
			when.select()
		} else if (when.isSelect()) {
			when.deselect()
			then.select()
		} else if (then.isSelect()) {
			then.deselect()
			given.select()
		} else {
			given.select()
		}
	}

	public void unToggleSelect() {
		if (given.isSelect()) {
			given.deselect()
			then.select()
		} else if (when.isSelect()) {
			when.deselect()
			given.select()
		} else if (then.isSelect()) {
			then.deselect()
			when.select()
		} else {
			given.select()
		}
	}

	public String getSelect() {
		StringBuffer ret = new StringBuffer()
		if (given.isSelect()) {
			ret.append("Given")
		}
		if (when.isSelect()) {
			if (!"".equals(ret.toString())) {
				ret.append("-")
			}
			ret.append("When")
		}
		if (then.isSelect()) {
			if (!"".equals(ret.toString())) {
				ret.append("-")
			}
			ret.append("Then")
		}
		return ret.toString()
	}

	public String getTestSuiteText(boolean escape) {
		StringBuffer ret = new StringBuffer()
		if (given.isSelect()) {
			ret.append(format.getGiven().getName())
			ret.append("\n")
			ret.append(given.getText())
		}
		if (when.isSelect()) {
			if (!"".equals(ret.toString())) {
				ret.append("\n")
			}
			ret.append(format.getWhen().getName())
			ret.append("\n")
			ret.append(when.getText())
		}
		if (then.isSelect()) {
			if (!"".equals(ret.toString())) {
				ret.append("\n")
			}
			ret.append(format.getThen().getName())
			ret.append("\n")
			ret.append(then.getText())
		}

		if (escape) {
			return ret.toString().replace("\n", "\\n")
		} else {
			return ret.toString()
		}
	}

	public String getTestSuiteText(boolean escape, String bdd){
		StringBuffer ret = new StringBuffer()
		if (bdd.contains("Given")) {
			ret.append(format.getGiven().getName())
			ret.append("\n")
			ret.append(given.getText())
		}
		if (bdd.contains("When")) {
			if (!"".equals(ret.toString())) {
				ret.append("\n")
			}
			ret.append(format.getWhen().getName())
			ret.append("\n")
			ret.append(when.getText())
		}
		if (bdd.contains("Then")) {
			if (!"".equals(ret.toString())) {
				ret.append("\n")
			}
			ret.append(format.getThen().getName())
			ret.append("\n")
			ret.append(then.getText())
		}

		if (escape) {
			return ret.toString().replace("\n", "\\n")
		} else {
			return ret.toString()
		}
	}

	public void setScrollEnable(boolean b) {
		if (b) {
			scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED)
		} else {
			scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER)
		}
	}

}
