package elements;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import manage.InitProp;
import manage.Keys;
import manage.Request;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class Sprint {
	final static Logger logger = Logger.getLogger("Sprint");
	public void addSprintButton(final Request req, final InitProp prop, JPanel contentPane) {
		JButton sprintButton = new JButton();
		FileHandler fh = null;
		try {
			fh = new FileHandler("log.txt");
		} catch (SecurityException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		fh.setFormatter(new SimpleFormatter());
		logger.addHandler(fh);
		sprintButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int sheetNum = Integer.parseInt(req.getSheetName().split(" ")[0]);// 指定sheet页编号
					HSSFSheet st = req.getWorkbook().getSheetAt(sheetNum - 1);// 获取指定sheet页
					HSSFCell cell = null;
					logger.log(Level.INFO, "当前sheet页共有" + st.getLastRowNum() + "行数据");
					for (int rowIndex = 1; rowIndex <= st.getLastRowNum(); rowIndex++) {
						HSSFRow row = st.getRow(rowIndex);
						if (row.getCell(0).getStringCellValue().equals("N") || row == null) {
							continue;
						} // 如果设置为不执行或为空，跳过
						String using = "";
						cell = row.getCell(1);
						if (cell != null) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							if (cell.getStringCellValue() != "") {
								using = row.getCell(1).getStringCellValue();// 元素属性
							}
						}
						cell = row.getCell(2);
						String value = "";
						if (cell != null) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							if (cell.getStringCellValue() != "") {
								value = row.getCell(2).getStringCellValue();// 属性值
							}
						}
						int index = 1;
						cell = row.getCell(3);// index
						if (cell != null) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							if (cell.getStringCellValue() != "") {
								index = Integer.parseInt(cell.getStringCellValue());
							}
						}
						String action = row.getCell(4).getStringCellValue();// 动作
						cell = row.getCell(5);// 输入值
						String cellValue = "";
						if (cell != null) {
							cellValue = getCellValue2String(cell);
						}

						logger.log(Level.INFO, "元素属性=" + using);
						logger.log(Level.INFO, "属性值=" + value);
						logger.log(Level.INFO, "index=" + index);
						logger.log(Level.INFO, "动作=" + action);
						logger.log(Level.INFO, "值=" + cellValue);

						WebElement element = null;

						if (using != "") {
							Method byMethod = By.class.getMethod(using, new Class[] { String.class });
							List<WebElement> elements = req.getDriver()
									.findElements((By) byMethod.invoke(By.class, new Object[] { value }));
							if (elements.isEmpty()) {
								logger.log(Level.SEVERE, "找不到" + using + "=" + "\"" + value + "\"的元素");
								;
							} else {
								element = elements.get(index - 1);
							}
						}

						/*
						 * 转window
						 */
						if (action.equals("switchWindow")) {
							Set<String> handles = req.getDriver().getWindowHandles();
							for (String handle : handles) {
								req.getDriver().switchTo().window(handle);
								String title = req.getDriver().getTitle();
								if (!title.contains(cellValue)) {
									continue;
								}
							}
						}
						/*
						 * 转DefaultContent
						 */
						if (action.equals("switchDefaultContent")) {
							req.getDriver().switchTo().defaultContent();
							logger.log(Level.INFO, "转DefaultContent成功");
						}
						/*
						 * 清空输入框
						 */
						if (action.equals("clear")) {
							element.clear();
							logger.log(Level.INFO, "clear成功");
						}

						/*
						 * 输入值
						 */
						if (action.equals("sendKeys")) {
							element.clear();
							logger.log(Level.INFO, "clear成功");
							element.sendKeys(cellValue);
							logger.log(Level.INFO, "sendKeys成功");
						}
						/*
						 * 点击元素,不能用原始click，转为执行JS
						 */
						if (action.equals("click")) {
							screenshot(req.getDriver(), using, value, action);// 点击前截图
							((JavascriptExecutor) req.getDriver()).executeScript("arguments[0].click();", element);
							logger.log(Level.INFO, "click成功");
						}
						/*
						 * 提交表单
						 */
						if (action.equals("submit")) {
							screenshot(req.getDriver(), using, value, action);// 点击前截图
							element.submit();
							logger.log(Level.INFO, "submit成功");
						}
						/*
						 * 转frame
						 */
						if (action.equals("switchFrame")) {
							req.getDriver().switchTo().frame(element);
							logger.log(Level.INFO, "转frame成功");

						}
						/*
						 * selectByValue
						 */
						if (action.equals("selectByValue")) {
							Select select = new Select(element);
							select.selectByValue(cellValue);
							logger.log(Level.INFO, "selectByValue成功");
						}
						/*
						 * selectByIndex
						 */
						if (action.equals("selectByIndex")) {
							Select select = new Select(element);
							select.selectByIndex(Integer.parseInt(cellValue));
							logger.log(Level.INFO, "selectByIndex成功");
						}
						/*
						 * 模拟键盘的回车
						 */
						if (action.equals("Enter")) {
							screenshot(req.getDriver(), using, value, action);// 点击前截图
							element.sendKeys(Keys.ENTER);
							logger.log(Level.INFO, "Enter成功");
						}
						/*
						 * checkBox
						 */
						if (action.equals("setTrue")) {
							if (!element.isSelected()) {
								((JavascriptExecutor) req.getDriver()).executeScript("arguments[0].click();", element);
							}
							logger.log(Level.INFO, "setTrue成功");
						}
						/*
						 * readOnly属性
						 */
						if (action.equals("readOnly")) {
							((JavascriptExecutor) req.getDriver())
									.executeScript("arguments[0].readOnly=" + cellValue.toLowerCase(), element);
							logger.log(Level.INFO, "readOnly设置成功");
						}
						// sleep
						if (action.equals("sleep")) {
							Thread.sleep(Integer.parseInt(cellValue) * 1000);
						}
					}
				} catch (Exception e1) {
					logger.log(Level.SEVERE, e1.toString());
					e1.printStackTrace();
				}
			}
		});
		sprintButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		sprintButton.setContentAreaFilled(false);
		sprintButton.setBorder(null);
		sprintButton.setToolTipText("加速");
		URL file = this.getClass().getResource("/img/go.png");
		ImageIcon icon = new ImageIcon(file);// 背景图
		sprintButton.setIcon(icon);
		sprintButton.setBounds(233, 5, icon.getIconWidth() + 2, icon.getIconHeight() + 2);
		sprintButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(sprintButton);
	}

	public static String getCellValue2String(HSSFCell cell) {
		if (cell == null) {
			return null;
		}
		// 时间对象 特殊处理
		int dataFormat = cell.getCellStyle().getDataFormat();

		if (dataFormat == 14 || dataFormat == 178 || dataFormat == 180 || dataFormat == 181 || dataFormat == 182) {
			return getDateValue(cell);
		}

		String value = null;
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC:
			value = new DecimalFormat("0.##########").format(cell.getNumericCellValue());
			break;
		case HSSFCell.CELL_TYPE_STRING:
			// value = cell.getStringCellValue();
			value = cell.getRichStringCellValue().toString();
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			value = String.valueOf(cell.getCellFormula());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			// value = String.valueOf(cell.getStringCellValue());
			value = String.valueOf(cell.getRichStringCellValue().toString());
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			value = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			value = String.valueOf(cell.getErrorCellValue());
			break;
		}
		return value;
	}

	// 对date数据的特殊处理
	private static String getDateValue(HSSFCell cell) {
		final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
		return DEFAULT_DATE_FORMAT.format(cell.getDateCellValue());
	}

	private static void screenshot(WebDriver driver, String using, String value, String action) {
		Date now = new Date();
		SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss");
		File file = new File("screenshots//" + time.format(now) + "_" + using + "_" + value + "_" + action + ".jpg");
		File source_file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(source_file, file);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.toString());
			e.printStackTrace();

		}
	}

}
