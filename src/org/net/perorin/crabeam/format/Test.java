package org.net.perorin.crabeam.format;

import java.io.File;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.net.perorin.crabeam.poi.FormatLoader;

public class Test {

	public static void main(String[] args) {
		FormatLoader f = new FormatLoader("./contents/format/sample.xlsx", "./contents/format/test.xml");
	}

	private static void test1() {
		try {
			Workbook workbook = WorkbookFactory.create(new File("./contents/format/sample.xlsx"));
			Sheet sheet = workbook.getSheet("テストケース_結合");

			Iterator<Row> rows = sheet.rowIterator();
			while (rows.hasNext()) {
				Row row = rows.next();
				Iterator<Cell> cells = row.cellIterator();
				while (cells.hasNext()) {
					Cell cell = cells.next();
					try {
						String stringValue = cell.getStringCellValue();
						if ("".equals(stringValue)) {
							continue;
						}
						System.out.print(stringValue + " , ");
					} catch (IllegalStateException e) {
					}
					try {
						Double numericValue = cell.getNumericCellValue();
						System.out.print(numericValue + " , ");
					} catch (IllegalStateException e) {
					}
				}
				System.out.println("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void test2() {
		try {
			Workbook workbook = WorkbookFactory.create(new File("./contents/format/sample.xlsx"));
			Sheet sheet = workbook.getSheet("テストケース_結合");

			for (int rowCnt = 0; rowCnt < sheet.getLastRowNum(); rowCnt++) {
				Row row = sheet.getRow(rowCnt);
				if (row == null) {
					continue;
				}
				for (int cellCnt = 0; cellCnt < row.getLastCellNum(); cellCnt++) {
					Cell cell = row.getCell(cellCnt);
					if (cell == null) {
						continue;
					}
					try {
						String stringValue = cell.getStringCellValue();
						if ("".equals(stringValue)) {
							continue;
						}
						System.out.print(stringValue + " , ");
					} catch (IllegalStateException e) {
					}
					try {
						Double numericValue = cell.getNumericCellValue();
						System.out.print(numericValue + " , ");
					} catch (IllegalStateException e) {
					}
				}
				System.out.println("");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}