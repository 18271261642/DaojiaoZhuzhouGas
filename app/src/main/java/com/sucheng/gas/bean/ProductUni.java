package com.sucheng.gas.bean;
/**
 * 
 * @Describe 生产单位
 * @author sunjianhua
 * QiAnEnergyMob
 * 2016-6-17
 *
 */
public class ProductUni {
	
	private int id;
	
	private String production_unit_name;

	@Override
	public String toString() {
		return "ProductUni [id=" + id + ", production_unit_name="
				+ production_unit_name + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProduction_unit_name() {
		return production_unit_name;
	}

	public void setProduction_unit_name(String production_unit_name) {
		this.production_unit_name = production_unit_name;
	}
	
	

}
