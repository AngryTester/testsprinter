package elements;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import manage.InitProp;
import manage.Request;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class Browser {
	public void addBrowserButton(final Request req,final InitProp prop,JPanel contentPane){
		JButton initButton = new JButton();
		Runtime exe = Runtime.getRuntime();
	    try {
			exe.exec("taskkill /f /im "+prop.getData_path());//启动driver之前先杀driver
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		initButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try{
				  	//打开IEDriver or ChromeDriver
				    String path = prop.getDriver_path();//获取driver路径
				    System.setProperty("webdriver.ie.driver", path);
				    WebDriver driver = new InternetExplorerDriver();
				    req.setDriver(driver);
				    driver.get("about:blank");
				    driver.manage().window().maximize();
				}catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		initButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		initButton.setContentAreaFilled(false);//去掉背景图
		initButton.setBorder(null);//不显示边框
		initButton.setToolTipText("初始化浏览器");//tip提示 
		URL file = this.getClass().getResource("/img/browser.png");
		ImageIcon icon = new ImageIcon(file);//背景图
		initButton.setIcon(icon);
		initButton.setBounds(10, 5, icon.getIconWidth()+2, icon.getIconHeight()+2);
		initButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//鼠标移上显示手型
		contentPane.add(initButton);
	}
}
