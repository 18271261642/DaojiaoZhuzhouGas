package com.sucheng.gas.bean;

/**
 * 气瓶初始化
 * @author sunjianhua
 *
 */
public class AddBottleTypeBean {
	
	private int id;

	private int airBottleId;
	private int airBottleTypeId;


	
	private String air_bottle_specifications;

	private Double default_weight; //默认重量

	public Double getDefault_weight() {
		return default_weight;
	}

	public void setDefault_weight(Double default_weight) {
		this.default_weight = default_weight;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAir_bottle_specifications() {
		return air_bottle_specifications;
	}

	public void setAir_bottle_specifications(String air_bottle_specifications) {
		this.air_bottle_specifications = air_bottle_specifications;
	}

	public int getAirBottleId() {
		return airBottleId;
	}

	public void setAirBottleId(int airBottleId) {
		this.airBottleId = airBottleId;
	}

	public int getAirBottleTypeId() {
		return airBottleTypeId;
	}

	public void setAirBottleTypeId(int airBottleTypeId) {
		this.airBottleTypeId = airBottleTypeId;
	}

	public AddBottleTypeBean(int id, String air_bottle_specifications) {
		super();
		this.id = id;
		this.air_bottle_specifications = air_bottle_specifications;
	}


	@Override
	public String toString() {
		return "AddBottleTypeBean{" +
				"id=" + id +
				", airBottleId=" + airBottleId +
				", airBottleTypeId=" + airBottleTypeId +
				", air_bottle_specifications='" + air_bottle_specifications + '\'' +
				", default_weight=" + default_weight +
				'}';
	}
}
