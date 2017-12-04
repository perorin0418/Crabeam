package org.net.perorin.crabeam.format;

import java.util.ArrayList;

public class TestSuiteFormat {

	private ArrayList<HeadItem> head_item_list;
	private BddItem given;
	private BddItem when;
	private BddItem then;
	private String sheet_name;
	private String format_name;
	private int top;
	private int bottom;
	private int left;
	private int right;

	public ArrayList<HeadItem> getHead_item_list() {
		return head_item_list;
	}

	public void setHead_item_list(ArrayList<HeadItem> head_item_list) {
		this.head_item_list = head_item_list;
	}

	public BddItem getGiven() {
		return given;
	}

	public void setGiven(BddItem given) {
		this.given = given;
	}

	public BddItem getWhen() {
		return when;
	}

	public void setWhen(BddItem when) {
		this.when = when;
	}

	public BddItem getThen() {
		return then;
	}

	public void setThen(BddItem then) {
		this.then = then;
	}

	public String getSheet_name() {
		return sheet_name;
	}

	public void setSheet_name(String sheet_name) {
		this.sheet_name = sheet_name;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getBottom() {
		return bottom;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public String getFormat_name() {
		return format_name;
	}

	public void setFormat_name(String format_name) {
		this.format_name = format_name;
	}

	public HeadItem getHeadItemByOrder(int order) {
		for (HeadItem hi : head_item_list) {
			if (hi.getOrder() == order) {
				return hi;
			}
		}
		return null;
	}

}
