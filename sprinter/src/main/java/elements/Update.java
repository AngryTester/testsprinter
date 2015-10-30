package elements;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import manage.InitProp;
import manage.Request;

public class Update {
	public void addUpdateButton(final Request req, final InitProp prop, JPanel contentPane) {
		JButton updateButton = new JButton();
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					File file = new File(prop.getData_path());
					BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
					POIFSFileSystem fs = new POIFSFileSystem(in);
					HSSFWorkbook wb = new HSSFWorkbook(fs);
					req.setWorkbook(wb);

					InputStream ins = null;
					try {
						ins = new BufferedInputStream(new FileInputStream("src/main/resources/test.properties"));
					} catch (FileNotFoundException e2) {
						e2.printStackTrace();
					}
					Properties p = new Properties();
					try {
						p.load(ins);
					} catch (IOException e3) {
						e3.printStackTrace();
					}
					prop.setBrowserType(p.getProperty("browserType"));
					prop.setDriver_path(p.getProperty("driver_path"));
					prop.setData_path(p.getProperty("data_path"));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		updateButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		updateButton.setContentAreaFilled(false);
		updateButton.setBorder(null);
		updateButton.setToolTipText("更新数据");
		URL file = this.getClass().getResource("/img/refresh.png");
		ImageIcon icon = new ImageIcon(file);
		updateButton.setIcon(icon);
		updateButton.setBounds(55, 5, icon.getIconWidth() + 2, icon.getIconHeight() + 2);
		updateButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(updateButton);
	}
}
