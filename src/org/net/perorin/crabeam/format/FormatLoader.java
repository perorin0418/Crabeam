package org.net.perorin.crabeam.format;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXB;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class FormatLoader {

	TestSuiteFormat format;

	public FormatLoader(String excel, String format) {
		try {
			this.format = JAXB.unmarshal(new File(format), TestSuiteFormat.class);
			Workbook workbook = WorkbookFactory.create(new File(excel));
			Sheet sheet = workbook.getSheet(this.format.getSheet_name());
			List<List<String>> rawData = getRawData(sheet);
			for (List<String> a : rawData) {
				for (String b : a) {
					System.out.print(b + " , ");
				}
				System.out.println();
			}

		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<List<String>> getRawData(Sheet sheet) {
		List<List<String>> list = new LinkedList<List<String>>();
		int top = format.getTop();
		int bottom = format.getBottom() == 0 ? getLastRowNum(sheet) : format.getBottom();
		int left = format.getLeft();
		int right = format.getRight() == 0 ? getLastCellNum(sheet) : format.getRight();
		for (int y = top; y <= bottom; y++) {
			List<String> rec = new LinkedList<String>();
			for (int x = left; x <= right; x++) {
				String value = "";
				Row row = sheet.getRow(y);
				if (row != null) {
					Cell cell = row.getCell(x);
					if (cell != null) {
						value = getCellValue(cell);
					}
				}
				rec.add(value);
			}
			if (rec.size() > 0) {
				list.add(rec);
			}
		}
		List<HeadItem> headList = format.getHead_item_list();
		for (HeadItem hi : headList) {
			if (hi.isRequire()) {
				Iterator<List<String>> it = list.iterator();
				while (it.hasNext()) {
					String value = it.next().get(hi.getAddress());
					if("".equals(value)){
						it.remove();
					}
				}
			}
		}
		return list;
	}

	private int getLastRowNum(Sheet sheet) {
		return sheet.getLastRowNum();
	}

	private int getLastCellNum(Sheet sheet) {
		int ret = 0;
		Iterator<Row> it = sheet.rowIterator();
		while (it.hasNext()) {
			Row r = it.next();
			if (r.getLastCellNum() > ret) {
				ret = r.getLastCellNum();
			}
		}
		return ret;
	}

	private String getCellValue(Cell cell) {
		String ret = "";
		switch (cell.getCellTypeEnum()) {
		case NUMERIC:
			double i = cell.getNumericCellValue();
			ret = String.valueOf(i);
			break;

		case STRING:
			String str = cell.getStringCellValue();
			ret = str;
			break;

		case FORMULA:
			String f = cell.getCellFormula();
			ret = f;
			break;

		case BLANK:
			ret = "";
			break;

		case BOOLEAN:
			boolean b = cell.getBooleanCellValue();
			ret = String.valueOf(b);
			break;

		case ERROR:
			ret = "";
			break;

		default:
			break;
		}

		return ret;
	}

}
