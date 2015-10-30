package elements;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import manage.InitProp;
import manage.Request;

public class ShutDown {
	public void add(final Request req, final InitProp prop, final JPanel contentPane) {
		JButton ShutDownButton = new JButton();
		ShutDownButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = JOptionPane.showConfirmDialog(null, "确认关闭", "温馨提示", JOptionPane.YES_NO_OPTION);
				if (a == JOptionPane.YES_OPTION) {
					String command = "cmd /c taskkill -f -im chromedriver.exe";
					try {
						Process p = Runtime.getRuntime().exec(command);
						p.waitFor();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					System.exit(0);
					contentPane.setVisible(false);
				}
			}
		});
		ShutDownButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		ShutDownButton.setContentAreaFilled(false);
		ShutDownButton.setBorder(null);
		ShutDownButton.setToolTipText("关闭");
		URL file = this.getClass().getResource("/img/ShutDown.png");
		ImageIcon icon = new ImageIcon(file);
		ShutDownButton.setIcon(icon);
		ShutDownButton.setBounds(317, 5, icon.getIconWidth() + 2, icon.getIconHeight() + 2);
		ShutDownButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(ShutDownButton);
	}
}
