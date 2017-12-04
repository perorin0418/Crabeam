package org.net.perorin.crabeam.window;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import org.net.perorin.crabeam.config.Constant;

public class TestSuitePanel extends JPanel {

	JScrollPane scroll;
	JPanel labelSuite;
	HeaderByImage header;
	BddByImage given;
	BddByImage when;
	BddByImage then;
	PictureCanvas picture;

	public TestSuitePanel() {
		super();
		this.setLayout(null);
		this.setOpaque(false);

		scroll = new JScrollPane();
		scroll.setOpaque(false);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(new EmptyBorder(0, 0, 0, 0));
		scroll.getVerticalScrollBar().setUnitIncrement(5);
		this.add(scroll, BorderLayout.CENTER);

		labelSuite = new JPanel();
		labelSuite.setLayout(null);
		labelSuite.setOpaque(false);
		scroll.getViewport().add(labelSuite);
		scroll.getViewport().setOpaque(false);

		header = new HeaderByImage();
		given = new BddByImage("Given");
		when = new BddByImage("When");
		then = new BddByImage("Then");

		labelSuite.add(header);
		labelSuite.add(given);
		labelSuite.add(when);
		labelSuite.add(then);

		JLabel castFrame = new JLabel(new ImageIcon(Constant.CAST_WAPPEN_PATH));
		castFrame.setBounds(20, 10, 50, 50);
		labelSuite.add(castFrame);

		JLabel castImg = new JLabel(new ImageIcon(ImageRandomizer.getCastImagePath()));
		castImg.setBounds(20, 10, 50, 50);
		labelSuite.add(castImg);

		picture = new PictureCanvas();
		labelSuite.add(picture);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		scroll.setBounds(x, y, width, height);

		header.setBounds(80, 10, width - 120, 50);

		given.setBounds(10, 70, width - 40);
		int height_given = given.getPreferredHeight();

		when.setBounds(10, 80 + height_given, width - 40);
		int height_when = when.getPreferredHeight();

		then.setBounds(10, 90 + height_given + height_when, width - 40);
		int height_then = then.getPreferredHeight();

		picture.setBounds(10, 100 + height_given + height_when + height_then, width - 40);
		int height_picture = picture.getPreferredHeight();

		labelSuite.setBounds(0, 0, width, 110 + height_given + height_when + height_then + height_picture);
		labelSuite.setPreferredSize(new Dimension(width, 110 + height_given + height_when + height_then + height_picture));
	}

	public void setHeadText(String text) {
		header.setText(text);
	}

	public void setGivenText(String text) {
		given.setText(text);
	}

	public void setWhenText(String text) {
		when.setText(text);
	}

	public void setThenText(String text) {
		then.setText(text);
	}

	public void setPicture(String imgPath) {
		picture.setPicture(imgPath);
	}

	public void setScrollEnable(boolean b) {
		if (b) {
			scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		} else {
			scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		}
	}

}
