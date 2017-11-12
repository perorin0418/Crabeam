package org.net.perorin.crabeam.window;

import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.net.perorin.crabeam.config.Constant;

public class UpDownButton extends JPanel {

	private int number = 0;
	private int nod = 1;
	private RoundButton upBtn;
	private RoundButton downBtn;

	public UpDownButton() {
		this.setLayout(null);
		this.setPreferredSize(new Dimension(22, 60));
		this.setBackground(SystemColor.inactiveCaptionBorder);

		upBtn = new RoundButton(new ImageIcon(Constant.BUTTON_PLUS));
		upBtn.setBounds(3, 6, 19, 19);
		this.add(upBtn);
		upBtn.setPressedIcon(new ImageIcon(Constant.BUTTON_PLUS_P));
		upBtn.setBackground(SystemColor.inactiveCaption);
		upBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				upBtnAction();
			}
		});

		downBtn = new RoundButton(new ImageIcon(Constant.BUTTON_MINUS));
		downBtn.setBounds(3, 28, 19, 19);
		this.add(downBtn);
		downBtn.setPressedIcon(new ImageIcon(Constant.BUTTON_MINUS_P));
		downBtn.setBackground(SystemColor.inactiveCaption);
		downBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				downBtnAction();
			}
		});
	}

	public void setNOD(int nod) {
		this.nod = nod;
	}

	public String getNumber() {
		return String.format("%0" + nod + "d", number);
	}

	public void clickUp() {
		upBtn.doClick();
	}

	public void clickDown() {
		downBtn.doClick();
	}

	public void upBtnAction() {
		number++;
	}

	public void downBtnAction() {
		number--;
		if (number < 0) {
			number = 0;
		}
	}

}
