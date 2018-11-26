package org.net.perorin.crablaser.format

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement

@XmlAccessorType( XmlAccessType.NONE )
public class FolderList {

	@XmlElement(name="folder")
	private ArrayList<String> folder

	public ArrayList<String> getFolder() {
		return folder
	}

	public void setFolder(ArrayList<String> folder) {
		this.folder = folder
	}
}
