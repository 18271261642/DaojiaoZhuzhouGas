package com.sucheng.gas.bean;

/**
 * Created by Administrator on 2018/1/27.
 */

public class DeliverOrderBean {


    /**
     * id : 3
     * order_code : 3008441
     * client_id : 1
     * client_name : 某某
     * air_bottle_specifications : 15KG
     * business_type_name : 212
     * delivery_type_name : 订气配送
     * pay_type_name : 现金
     * order_number : 1
     * order_address : 没有地址
     * order_tel_number : 110
     * remark :
     * state_description : 派工未派送
     * floor_subsidies_money : 0
     * delivery_fee : 0
     * check_fee : 0
     * price : 88
     * discount_amount : 0
     * total_amount : 88
     * order_time : 1516888913000
     * feeType :
     * fee_total_amount : 0
     * order_appointment_time1 : 1516888800000
     * order_appointment_time2 : 1516888800000
     * pay_state_description : 未付款
     */

    private int id;
    private String order_code;
    private int client_id;
    private String client_name;
    private String air_bottle_specifications;
    private String business_type_name;
    private String delivery_type_name;
    private String pay_type_name;
    private int order_number;
    private String order_address;
    private String order_tel_number;
    private String remark;
    private String state_description;
    private int floor_subsidies_money;
    private int delivery_fee;
    private int check_fee;
    private int price;
    private int discount_amount;
    private int total_amount;
    private long order_time;
    private String feeType;
    private int fee_total_amount;
    private long order_appointment_time1;
    private long order_appointment_time2;
    private String pay_state_description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getAir_bottle_specifications() {
        return air_bottle_specifications;
    }

    public void setAir_bottle_specifications(String air_bottle_specifications) {
        this.air_bottle_specifications = air_bottle_specifications;
    }

    public String getBusiness_type_name() {
        return business_type_name;
    }

    public void setBusiness_type_name(String business_type_name) {
        this.business_type_name = business_type_name;
    }

    public String getDelivery_type_name() {
        return delivery_type_name;
    }

    public void setDelivery_type_name(String delivery_type_name) {
        this.delivery_type_name = delivery_type_name;
    }

    public String getPay_type_name() {
        return pay_type_name;
    }

    public void setPay_type_name(String pay_type_name) {
        this.pay_type_name = pay_type_name;
    }

    public int getOrder_number() {
        return order_number;
    }

    public void setOrder_number(int order_number) {
        this.order_number = order_number;
    }

    public String getOrder_address() {
        return order_address;
    }

    public void setOrder_address(String order_address) {
        this.order_address = order_address;
    }

    public String getOrder_tel_number() {
        return order_tel_number;
    }

    public void setOrder_tel_number(String order_tel_number) {
        this.order_tel_number = order_tel_number;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getState_description() {
        return state_description;
    }

    public void setState_description(String state_description) {
        this.state_description = state_description;
    }

    public int getFloor_subsidies_money() {
        return floor_subsidies_money;
    }

    public void setFloor_subsidies_money(int floor_subsidies_money) {
        this.floor_subsidies_money = floor_subsidies_money;
    }

    public int getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(int delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public int getCheck_fee() {
        return check_fee;
    }

    public void setCheck_fee(int check_fee) {
        this.check_fee = check_fee;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(int discount_amount) {
        this.discount_amount = discount_amount;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public long getOrder_time() {
        return order_time;
    }

    public void setOrder_time(long order_time) {
        this.order_time = order_time;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public int getFee_total_amount() {
        return fee_total_amount;
    }

    public void setFee_total_amount(int fee_total_amount) {
        this.fee_total_amount = fee_total_amount;
    }

    public long getOrder_appointment_time1() {
        return order_appointment_time1;
    }

    public void setOrder_appointment_time1(long order_appointment_time1) {
        this.order_appointment_time1 = order_appointment_time1;
    }

    public long getOrder_appointment_time2() {
        return order_appointment_time2;
    }

    public void setOrder_appointment_time2(long order_appointment_time2) {
        this.order_appointment_time2 = order_appointment_time2;
    }

    public String getPay_state_description() {
        return pay_state_description;
    }

    public void setPay_state_description(String pay_state_description) {
        this.pay_state_description = pay_state_description;
    }
}
