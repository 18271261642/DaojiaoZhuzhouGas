package com.sucheng.gas.bean;
/**
 *气瓶初始化，检测单位
 * @author sunjianhua
 * QiAnEnergyMob
 * 2016-6-17
 *
 */
public class DetectionUnit {
	
	private int id;
	
	private String detection_unit_name;

	@Override
	public String toString() {
		return "ProductUnit [id=" + id + ", detection_unit_name="
				+ detection_unit_name + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDetection_unit_name() {
		return detection_unit_name;
	}

	public void setDetection_unit_name(String detection_unit_name) {
		this.detection_unit_name = detection_unit_name;
	}
	
	

}
