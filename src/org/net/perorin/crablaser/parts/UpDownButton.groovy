package org.net.perorin.crablaser.parts

import java.awt.Dimension
import java.awt.SystemColor
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import javax.swing.ImageIcon
import javax.swing.JPanel

import org.net.perorin.crablaser.config.Constant

public class UpDownButton extends JPanel {

	private int number = 0
	private int nod = 1
	private RoundButton upBtn
	private RoundButton downBtn

	public UpDownButton() {
		this.setLayout(null)
		this.setPreferredSize(new Dimension(22, 60))
		this.setBackground(SystemColor.inactiveCaptionBorder)

		upBtn = new RoundButton(new ImageIcon(Constant.BUTTON_PLUS))
		upBtn.setBounds(3, 6, 19, 19)
		this.add(upBtn)
		upBtn.setPressedIcon(new ImageIcon(Constant.BUTTON_PLUS_P))
		upBtn.setBackground(SystemColor.inactiveCaption)
		upBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				upBtnAction(e)
			}
		})

		downBtn = new RoundButton(new ImageIcon(Constant.BUTTON_MINUS))
		downBtn.setBounds(3, 28, 19, 19)
		this.add(downBtn)
		downBtn.setPressedIcon(new ImageIcon(Constant.BUTTON_MINUS_P))
		downBtn.setBackground(SystemColor.inactiveCaption)
		downBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				downBtnAction(e)
			}
		})
	}

	public void setNOD(int nod) {
		this.nod = nod
	}

	public String getNumber() {
		return String.format("%0" + nod + "d", number)
	}

	public void setNumber(int number) {
		this.number = number
	}

	public void clickUp() {
		upBtn.doClick()
	}

	public void clickDown() {
		downBtn.doClick()
	}

	public void upBtnAction(ActionEvent e) {
		if ((e.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK) {
			number = number + 100
		} else if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == ActionEvent.SHIFT_MASK) {
			number = number + 10
		} else {
			number++
		}
	}

	public void downBtnAction(ActionEvent e) {
		if ((e.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK) {
			number = number - 100
		} else if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == ActionEvent.SHIFT_MASK) {
			number = number - 10
		} else {
			number--
		}
		if (number < 0) {
			number = 0
		}
	}

}
