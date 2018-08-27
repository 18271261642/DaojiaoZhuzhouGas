package com.sucheng.gas.bean;

/**
 * Created by Administrator on 2018/7/17.
 */

/**
 * 门店入户安检
 */
public class FamilyCheckOrderBean {


    /**
     * client_code : 1
     * family_check_address : 大朗医院
     * remark :
     * family_check_tel_number : 110
     * create_time : 2018-07-17 16:59
     * family_check_code : 7
     * client_id : 1
     * appointment_check_time : 2018-07-17 00:00
     * client_name : 某某
     * family_check_id : 7
     */

    private String client_code;
    private String family_check_address;
    private String remark;
    private String family_check_tel_number;
    private String create_time;
    private String family_check_code;
    private int client_id;
    private String appointment_check_time;
    private String client_name;
    private int family_check_id;

    public String getClient_code() {
        return client_code;
    }

    public void setClient_code(String client_code) {
        this.client_code = client_code;
    }

    public String getFamily_check_address() {
        return family_check_address;
    }

    public void setFamily_check_address(String family_check_address) {
        this.family_check_address = family_check_address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFamily_check_tel_number() {
        return family_check_tel_number;
    }

    public void setFamily_check_tel_number(String family_check_tel_number) {
        this.family_check_tel_number = family_check_tel_number;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFamily_check_code() {
        return family_check_code;
    }

    public void setFamily_check_code(String family_check_code) {
        this.family_check_code = family_check_code;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getAppointment_check_time() {
        return appointment_check_time;
    }

    public void setAppointment_check_time(String appointment_check_time) {
        this.appointment_check_time = appointment_check_time;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public int getFamily_check_id() {
        return family_check_id;
    }

    public void setFamily_check_id(int family_check_id) {
        this.family_check_id = family_check_id;
    }
}
