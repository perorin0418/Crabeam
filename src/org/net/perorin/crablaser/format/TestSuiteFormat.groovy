package org.net.perorin.crablaser.format

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement

@XmlAccessorType( XmlAccessType.NONE )
public class TestSuiteFormat {

	@XmlElement(name="head_item_list")
	private ArrayList<HeadItem> head_item_list

	@XmlElement(name="given")
	private BddItem given

	@XmlElement(name="when")
	private BddItem when

	@XmlElement(name="then")
	private BddItem then

	@XmlElement(name="evi_no")
	private BddItem evi_no

	@XmlElement(name="sheet_name")
	private String sheet_name

	@XmlElement(name="format_name")
	private String format_name

	@XmlElement(name="top")
	private int top

	@XmlElement(name="bottom")
	private int bottom

	@XmlElement(name="left")
	private int left

	@XmlElement(name="right")
	private int right

	public ArrayList<HeadItem> getHead_item_list() {
		return head_item_list
	}

	public void setHead_item_list(ArrayList<HeadItem> head_item_list) {
		this.head_item_list = head_item_list
	}

	public BddItem getGiven() {
		return given
	}

	public void setGiven(BddItem given) {
		this.given = given
	}

	public BddItem getWhen() {
		return when
	}

	public void setWhen(BddItem when) {
		this.when = when
	}

	public BddItem getThen() {
		return then
	}

	public void setThen(BddItem then) {
		this.then = then
	}

	public String getSheet_name() {
		return sheet_name
	}

	public void setSheet_name(String sheet_name) {
		this.sheet_name = sheet_name
	}

	public int getTop() {
		return top
	}

	public void setTop(int top) {
		this.top = top
	}

	public int getBottom() {
		return bottom
	}

	public void setBottom(int bottom) {
		this.bottom = bottom
	}

	public int getLeft() {
		return left
	}

	public void setLeft(int left) {
		this.left = left
	}

	public int getRight() {
		return right
	}

	public void setRight(int right) {
		this.right = right
	}

	public String getFormat_name() {
		return format_name
	}

	public void setFormat_name(String format_name) {
		this.format_name = format_name
	}

	public HeadItem getHeadItemByOrder(int order) {
		for (HeadItem hi : head_item_list) {
			if (hi.getOrder() == order) {
				return hi
			}
		}
		return null
	}

	public BddItem getEvi_no() {
		return evi_no
	}

	public void setEvi_no(BddItem evi_no) {
		this.evi_no = evi_no
	}

}
