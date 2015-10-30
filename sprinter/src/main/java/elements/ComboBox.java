package elements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import manage.InitProp;
import manage.Request;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ComboBox {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addSelectOption(final Request req, final InitProp prop, JPanel contentPane) {

		File file = new File(prop.getData_path());

		final JComboBox comboBox = new JComboBox();

		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
			POIFSFileSystem fs = new POIFSFileSystem(in);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			req.setWorkbook(wb);
			for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
				HSSFSheet st = wb.getSheetAt(sheetIndex);
				comboBox.addItem((sheetIndex + 1) + " " + st.getSheetName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		comboBox.setBounds(102, 10, 120, 28);
		comboBox.setToolTipText("测试数据");
		contentPane.add(comboBox);
		req.setSheetName(comboBox.getItemAt(0).toString());
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				req.setSheetName(comboBox.getSelectedItem().toString());
			}
		});

	}
}
