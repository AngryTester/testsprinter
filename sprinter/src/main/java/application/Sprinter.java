package application;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import manage.InitProp;
import manage.Request;
import elements.ShutDown;
import elements.ComboBox;
import elements.Browser;
import elements.Sprint;
import elements.Selector;
import elements.Update;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Sprinter extends JFrame {

	private static final long serialVersionUID = 1L;
	Request req = new Request();
	InitProp prop = new InitProp();
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				try {
					Sprinter frame = new Sprinter();// 窗体实例化
					frame.setDragable();// 可拖拽
					frame.setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE);// 不关闭
					frame.dispose();
					frame.setUndecorated(true); // 与上一句一起实现隐藏标题栏
					frame.setVisible(true);// 显示
					frame.setAlwaysOnTop(true);// 永远最前
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*
	 * 以下代码实现可拖拽
	 */
	Point loc = null;
	Point tmp = null;
	boolean isDragged = false;

	private void setDragable() {
		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseReleased(java.awt.event.MouseEvent e) {
				isDragged = false;
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			public void mousePressed(java.awt.event.MouseEvent e) {
				tmp = new Point(e.getX(), e.getY());
				isDragged = true;
				setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}
		});
		this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			public void mouseDragged(java.awt.event.MouseEvent e) {
				if (isDragged) {
					loc = new Point(getLocation().x + e.getX() - tmp.x, getLocation().y + e.getY() - tmp.y);
					setLocation(loc);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("serial")
	public Sprinter() {

		Browser initButton = new Browser();// 初始化IE浏览器按钮
		Update updateButton = new Update();// 更新数据按钮
		ComboBox excelComboBox = new ComboBox();// 选择excel sheet下拉框
		Sprint injectButton = new Sprint();// 注入按钮
		ShutDown closeButton = new ShutDown();// 关闭按钮
		Selector selectorButton = new Selector();

		// 重写paintComponent方法实现背景图
		contentPane = new JPanel() {
			protected void paintComponent(Graphics g) {
				URL file = this.getClass().getResource("/img/back.png");
				ImageIcon icon = new ImageIcon(file);// 背景图
				Image img = icon.getImage();
				g.drawImage(img, 0, 0, icon.getIconWidth(), icon.getIconHeight(), icon.getImageObserver());
			}
		};

		/*
		 * 以下读取配置文件
		 */
		InputStream in = null;

		try {
			in = new BufferedInputStream(new FileInputStream("src/main/resources/test.properties"));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Properties p = new Properties();
		try {
			p.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		prop.setBrowserType(p.getProperty("browserType"));// 设置浏览器类型
		prop.setDriver_path(p.getProperty("driver_path"));// 设置driver路径
		prop.setData_path(p.getProperty("data_path"));// 设置data路径

		try {
			setTitle("Sprinter");// 标题栏
			setFont(new Font("微软雅黑", Font.PLAIN, 12));
			this.setIconImage(
					Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/logo.png")));// 用于生成标题栏图标
			add(contentPane);// 添加窗体
			setBounds(1100, 115, 358, 44);// 位置大小
			contentPane.setBorder(new LineBorder(Color.white, 2, true));// 白色边框
			contentPane.setLayout(null);// 布局

			// 初始化浏览器按钮
			initButton.addBrowserButton(req, prop, contentPane);
			// 更新数据按钮
			updateButton.addUpdateButton(req, prop, contentPane);
			// 下拉框选择data
			excelComboBox.addSelectOption(req, prop, contentPane);
			// 注入数据按钮
			injectButton.addSprintButton(req, prop, contentPane);
			// 关闭按钮
			closeButton.add(req, prop, contentPane);
			// 定位按钮
			selectorButton.addSelectorButton(req, prop, contentPane);

		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * 以下为关闭的监听
		 */
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int a = JOptionPane.showConfirmDialog(null, "确认关闭","温馨提示", JOptionPane.YES_NO_OPTION);
				if (a == JOptionPane.YES_OPTION) {
					String command = "cmd /c taskkill -f -im IEDriverServer.exe";
					try {
						Process p = Runtime.getRuntime().exec(command);
						p.waitFor();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					System.exit(0);
					setVisible(false);// 关闭
				}
			}
		});

	}

}
