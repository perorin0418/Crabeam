package org.net.perorin.crablaser.poi

import javax.xml.bind.JAXB

import org.apache.poi.EncryptedDocumentException
import org.apache.poi.hssf.util.CellReference
import org.apache.poi.openxml4j.exceptions.InvalidFormatException
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.ss.util.CellRangeAddress
import org.net.perorin.crablaser.format.BddItem
import org.net.perorin.crablaser.format.HeadItem
import org.net.perorin.crablaser.format.TestSuiteFormat
import org.net.perorin.crablaser.window.FormatSelector

public class FormatLoader {

	private TestSuiteFormat format
	private List<String> headers
	private List<String> givens
	private List<String> whens
	private List<String> thens
	private List<String> evi_nos
	private int size

	public FormatLoader(String excel, String format) {
		try {
			this.format = JAXB.unmarshal(new File(format), TestSuiteFormat.class)
			Workbook workbook = WorkbookFactory.create(new File(excel))
			Sheet sheet = workbook.getSheet(this.format.getSheet_name())

			FormatSelector.setProgress(40)

			List<List<String>> rawData = getRawData(sheet)

			FormatSelector.setProgress(50)

			size = rawData.size()
			headers = createHeaders(rawData)
			givens = createGivens(rawData)
			whens = createWhens(rawData)
			thens = createThens(rawData)
			evi_nos = createEviNos(rawData)
			workbook.close()
		} catch (EncryptedDocumentException e) {
			e.printStackTrace()
		} catch (InvalidFormatException e) {
			e.printStackTrace()
		} catch (IOException e) {
			e.printStackTrace()
		}
	}

	public String getHeader(int index) {
		return headers.get(index)
	}

	public String getGiven(int index) {
		return givens.get(index)
	}

	public String getWhen(int index) {
		return whens.get(index)
	}

	public String getThen(int index) {
		return thens.get(index)
	}

	public String getEviNo(int index) {
		return evi_nos.get(index)
	}

	public int size() {
		return size
	}

	private List<String> createHeaders(List<List<String>> rawData) {
		List<String> ret = new LinkedList<String>()
		List<HeadItem> hi = format.getHead_item_list()

		for (List<String> row : rawData) {
			StringBuffer headData = new StringBuffer()
			for (int i = 0; i < hi.size(); i++) {
				String value = ""
				String type = format.getHeadItemByOrder(i).getType()
				if ("Integer".equals(type)) {
					String buf_value = row.get(hi.get(i).getAddress())
					if ("-".equals(buf_value) || "－".equals(buf_value) || "".equals(buf_value)) {
						value = buf_value
					} else {
						value = String.valueOf((int) Double.parseDouble(buf_value))
					}
				} else if ("Double".equals(type)) {
					value = String.valueOf(Double.parseDouble(row.get(hi.get(i).getAddress())))
				} else if ("String".equals(type)) {
					value = row.get(hi.get(i).getAddress())
				} else {
					System.exit(-1)
				}

				if (format.getHeadItemByOrder(i).isShow_name()) {
					value = format.getHeadItemByOrder(i).getName() + "[" + value + "]"
				}

				if (i == 0) {
					headData.append(value)
				} else {
					headData.append("-" + value)
				}
			}
			ret.add(headData.toString())
		}

		return ret
	}

	private List<String> createGivens(List<List<String>> rawData) {
		List<String> ret = new LinkedList<String>()
		BddItem bi = format.getGiven()
		for (List<String> row : rawData) {
			ret.add(row.get(bi.getAddress()))
		}
		return ret
	}

	private List<String> createWhens(List<List<String>> rawData) {
		List<String> ret = new LinkedList<String>()
		BddItem bi = format.getWhen()
		for (List<String> row : rawData) {
			ret.add(row.get(bi.getAddress()))
		}
		return ret
	}

	private List<String> createThens(List<List<String>> rawData) {
		List<String> ret = new LinkedList<String>()
		BddItem bi = format.getThen()
		for (List<String> row : rawData) {
			ret.add(row.get(bi.getAddress()))
		}
		return ret
	}

	private List<String> createEviNos(List<List<String>> rawData) {
		List<String> ret = new LinkedList<String>()
		BddItem bi = format.getEvi_no()
		for (int i = 0; i < rawData.size(); i++) {
			if (bi.getAddress() != -1) {
				ret.add(format.getSheet_name() + "!"
						+ CellReference.convertNumToColString(bi.getAddress() + format.getLeft())
						+ (format.getTop() + i + 1))
			} else {
				ret.add("")
			}
		}
		return ret
	}

	private List<List<String>> getRawData(Sheet sheet) {
		List<List<String>> list = new LinkedList<List<String>>()
		int top = format.getTop()
		int bottom = format.getBottom() == 0 ? getLastRowNum(sheet) : format.getBottom()
		int left = format.getLeft()
		int right = format.getRight() == 0 ? getLastCellNum(sheet) : format.getRight()
		for (int y = top; y <= bottom; y++) {
			List<String> rec = new LinkedList<String>()
			for (int x = left; x <= right; x++) {
				String value = ""
				Row row = sheet.getRow(y)
				if (row != null) {
					Cell cell = row.getCell(x)
					if (cell != null) {
						value = getStringRangeValue(cell)
					}
				}
				rec.add(value)
			}
			if (rec.size() > 0) {
				list.add(rec)
			}
		}

		// 必須項目が記載なしの行は削除
		List<HeadItem> headList = format.getHead_item_list()
		for (HeadItem hi : headList) {
			if (hi.isRequire()) {
				Iterator<List<String>> it = list.iterator()
				while (it.hasNext()) {
					String value = it.next().get(hi.getAddress())
					if ("".equals(value)) {
						it.remove()
					}
				}
			}
		}

		// 『〃』と『上記テストの続き』の対応
		for (int row = 1; row < list.size(); row++) {
			for (int cell = 0; cell < list.get(row).size(); cell++) {
				String value = list.get(row).get(cell)
				if ("〃".equals(value)) {
					String relace = list.get(row - 1).get(cell)
					list.get(row).set(cell, relace)
				}
			}
		}

		return list
	}

	private int getLastRowNum(Sheet sheet) {
		return sheet.getLastRowNum()
	}

	private int getLastCellNum(Sheet sheet) {
		int ret = 0
		Iterator<Row> it = sheet.rowIterator()
		while (it.hasNext()) {
			Row r = it.next()
			if (r.getLastCellNum() > ret) {
				ret = r.getLastCellNum()
			}
		}
		return ret
	}

	private Cell getCell(Sheet sheet, int rowIndex, int columnIndex) {
		Row row = sheet.getRow(rowIndex)
		if (row != null) {
			Cell cell = row.getCell(columnIndex)
			return cell
		}
		return null
	}

	private String getCellValue(Cell cell) {
		String ret = ""
		switch (cell.getCellTypeEnum()) {
		case CellType.NUMERIC:
			double i = cell.getNumericCellValue()
			ret = String.valueOf(i)
			break

		case CellType.STRING:
			String str = cell.getStringCellValue()
			ret = str
			break

		case CellType.FORMULA:
			String f = cell.getCellFormula()
			ret = f
			break

		case CellType.BLANK:
			ret = ""
			break

		case CellType.BOOLEAN:
			boolean b = cell.getBooleanCellValue()
			ret = String.valueOf(b)
			break

		case CellType.ERROR:
			ret = ""
			break

		default:
			break
		}

		return ret
	}

	private String getStringRangeValue(Cell cell) {
		int rowIndex = cell.getRowIndex()
		int columnIndex = cell.getColumnIndex()

		Sheet sheet = cell.getSheet()
		int size = sheet.getNumMergedRegions()
		for (int i = 0; i < size; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i)
			if (range.isInRange(rowIndex, columnIndex)) {
				Cell firstCell = getCell(sheet, range.getFirstRow(), range.getFirstColumn())
				return getCellValue(firstCell)
			}
		}
		return getCellValue(cell)
	}

}
