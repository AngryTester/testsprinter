package elements;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import manage.InitProp;
import manage.Request;

public class Selector{
	public void addSelectorButton(final Request req,final InitProp prop,JPanel contentPane){
		JButton selectorButton = new JButton();
		selectorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					InputStreamReader inputReader = null;
			        BufferedReader bufferReader = null;
			        StringBuffer strBuffer = new StringBuffer();
			        try{
			            InputStream inputStream = new FileInputStream(new File("src/main/resources/selector.js"));
			            inputReader = new InputStreamReader(inputStream);
			            bufferReader = new BufferedReader(inputReader);
			            String line = null;
			            while ((line = bufferReader.readLine()) != null)
			            {
			                strBuffer.append(line);
			            } 
			        } catch (IOException e1){
			        	e1.printStackTrace();
			        }finally{
			        	try {
							bufferReader.close();
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
			        }
			        String script = strBuffer.toString();
				  	WebDriver driver = req.getDriver();
				  	((JavascriptExecutor)driver).executeScript(script);
				}catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		selectorButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		selectorButton.setContentAreaFilled(false);
		selectorButton.setBorder(null);
		selectorButton.setToolTipText("ShowId&Xpath"); 
		URL file = this.getClass().getResource("/img/selector.png");
		ImageIcon icon = new ImageIcon(file);
		selectorButton.setIcon(icon);
		selectorButton.setBounds(277, 5, icon.getIconWidth()+2, icon.getIconHeight()+2);
		selectorButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(selectorButton);
	}
	
	
	
}