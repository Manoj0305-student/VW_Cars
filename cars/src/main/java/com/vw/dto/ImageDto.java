package com.vw.dto;

import java.util.Arrays;

public class ImageDto {
	private String imgName;
	private String contentType;
	private byte [] data;
	
	public ImageDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ImageDto(String imgName, String contentType, byte[] data) {
		super();
		this.imgName = imgName;
		this.contentType = contentType;
		this.data = data;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ImageDto [imgName=" + imgName + ", contentType=" + contentType + ", data=" + Arrays.toString(data)
				+ "]";
	}
	
	
	
	
}
