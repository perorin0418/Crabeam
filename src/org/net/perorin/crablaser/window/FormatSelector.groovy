package org.net.perorin.crablaser.window

import java.awt.DisplayMode
import java.awt.GraphicsEnvironment
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JProgressBar
import javax.swing.UIManager
import javax.swing.UnsupportedLookAndFeelException
import javax.xml.bind.JAXB

import org.net.perorin.crablaser.config.Constant
import org.net.perorin.crablaser.format.TestSuiteFormat
import org.net.perorin.crablaser.logic.LogicCrabLaser

public class FormatSelector {

	public static JProgressBar progress = null

	public JFrame frame
	private String xlsx
	private ArrayList<TestSuiteFormat> listFormat
	private ArrayList<String> listString
	private JComboBox<String> comboBox

	public FormatSelector(String xlsx) throws IOException {
		initialize()
		this.xlsx = xlsx == "" ? LogicCrabLaser.getExcelPath("", frame).toString() : xlsx
		if(xlsx == ""){
			JOptionPane.showMessageDialog(
				frame,
				"テスト仕様書が選択されていないので、\n終了します。（#^ω^）",
				"警告",
				JOptionPane.WARNING_MESSAGE)
			System.exit(0)
		}
		loadFormat()
	}

	private void initialize() throws IOException {

		File out_log = new File("./contents/log/crabeam.log")
		if(out_log.exists()){
			out_log.delete()
		}
		out_log.createNewFile()
		System.setOut(new PrintStream(out_log))
		System.setErr(new PrintStream(out_log))

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
		} catch (ClassNotFoundException | InstantiationException
		| IllegalAccessException | UnsupportedLookAndFeelException ex) {
			ex.printStackTrace()
		}

		listFormat = new ArrayList<>()
		listString = new ArrayList<>()

		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment()
		DisplayMode displayMode = env.getDefaultScreenDevice().getDisplayMode()
		int width = displayMode.getWidth()
		int height = displayMode.getHeight()

		frame = new JFrame()
		frame.setBounds(
				(int)(width / 2 - 122),
				(int)(height / 2 - 50),
				(int)244,
				(int)127)
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
		frame.getContentPane().setLayout(null)
		frame.setIconImage(new ImageIcon(Constant.ICON_PATH).getImage())
		frame.setTitle("フォーマット選択")
		frame.setResizable(false)

		comboBox = new JComboBox<String>()
		comboBox.setBounds(12, 10, 217, 19)
		frame.getContentPane().add(comboBox)

		JButton btnOk = new JButton("OK")
		btnOk.setBounds(138, 39, 91, 21)
		btnOk.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							CrabLaserWindow window = new CrabLaserWindow(xlsx, listString.get(comboBox.getSelectedIndex()))
							window.frame.setVisible(true)
							frame.dispose()
						} catch (Exception e2) {
							e2.printStackTrace()
							JOptionPane.showMessageDialog(
									frame,
									"ログを参照してください。\n『./contents/log/crabeam.log』",
									"例外発生",
									JOptionPane.WARNING_MESSAGE)
						}
					}
				})
		frame.getContentPane().add(btnOk)

		progress = new JProgressBar()
		progress.setBounds(12, 70, 217, 19)
		progress.setStringPainted(true)
		frame.getContentPane().add(progress)
	}

	private void loadFormat() {
		File formatPath = new File("./contents/format")
		for (File file : formatPath.listFiles()) {
			if (file.getPath().endsWith("xml")) {
				listFormat.add(JAXB.unmarshal(file, TestSuiteFormat.class))
				listString.add(file.getAbsolutePath())
			}
		}
		for (TestSuiteFormat tsf : listFormat) {
			comboBox.addItem(tsf.getFormat_name())
		}
	}

	public static void setProgress(int i) {
		if (progress != null) {
			progress.setValue(i)
			progress.update(progress.getGraphics())
		}
	}
}
