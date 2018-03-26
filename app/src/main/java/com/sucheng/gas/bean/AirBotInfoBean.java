package com.sucheng.gas.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/26.
 */

public class AirBotInfoBean {


    private DataBean data;
    private Object msg;
    private int code;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class DataBean {
        /**
         * production_unit_id : 2121
         * factory_number : 1234
         * air_bottle_type_name : 15KG
         * detection_unit_name : 常态
         * produce_time : 1514736000000
         * air_bottle_id : 4
         * expiration_time : 1640966400000
         * next_check_time_tmp : 1640966400000
         * check_time : 1514736000000
         * check_time_tmp : 1514736000000
         * photos : null
         * air_bottle_seal_code : 1234
         * air_bottle_type_id : 323
         * air_bottle_code : 5mV2tDVfvO/GoQ2CZ5gEzA==
         * use_cycle : 33
         * volume : fds
         * produce_time_tmp : 1514736000000
         * next_check_time : 1640966400000
         * production_unit_name : 百福
         * detection_unit_id : null
         * mobileAirBottleTrackingRecords : [{"mobileClinentInfo":"21d","warehouse_name":"道滘仓库","operator_name":"管理员","state_description":"空瓶初始化在仓库","create_time":1516937797000,"second_category_name":"fdsfds","delivery_man_name":"fdsad","client_name":"dfsafd"}]
         * safe_time : 1640966400000
         */

        private String production_unit_id;
        private String factory_number;
        private String air_bottle_type_name;
        private String detection_unit_name;
        private long produce_time;
        private String air_bottle_belong_name;
        private int air_bottle_id;
        private long expiration_time;
        private long next_check_time_tmp;
        private long check_time;
        private long check_time_tmp;
        private Object photos;
        private String air_bottle_seal_code;
        private String air_bottle_type_id;
        private String air_bottle_code;
        private int use_cycle;
        private String volume;
        private long produce_time_tmp;
        private long next_check_time;
        private String production_unit_name;
        private Object detection_unit_id;
        private long safe_time;
        private List<BotInfoBean> mobileAirBottleTrackingRecords;

        public String getProduction_unit_id() {
            return production_unit_id;
        }

        public void setProduction_unit_id(String production_unit_id) {
            this.production_unit_id = production_unit_id;
        }

        public String getFactory_number() {
            return factory_number;
        }

        public void setFactory_number(String factory_number) {
            this.factory_number = factory_number;
        }

        public String getAir_bottle_type_name() {
            return air_bottle_type_name;
        }

        public String getAir_bottle_belong_name() {
            return air_bottle_belong_name;
        }

        public void setAir_bottle_belong_name(String air_bottle_belong_name) {
            this.air_bottle_belong_name = air_bottle_belong_name;
        }

        public void setAir_bottle_type_name(String air_bottle_type_name) {
            this.air_bottle_type_name = air_bottle_type_name;
        }

        public String getDetection_unit_name() {
            return detection_unit_name;
        }

        public void setDetection_unit_name(String detection_unit_name) {
            this.detection_unit_name = detection_unit_name;
        }

        public long getProduce_time() {
            return produce_time;
        }

        public void setProduce_time(long produce_time) {
            this.produce_time = produce_time;
        }

        public int getAir_bottle_id() {
            return air_bottle_id;
        }

        public void setAir_bottle_id(int air_bottle_id) {
            this.air_bottle_id = air_bottle_id;
        }

        public long getExpiration_time() {
            return expiration_time;
        }

        public void setExpiration_time(long expiration_time) {
            this.expiration_time = expiration_time;
        }

        public long getNext_check_time_tmp() {
            return next_check_time_tmp;
        }

        public void setNext_check_time_tmp(long next_check_time_tmp) {
            this.next_check_time_tmp = next_check_time_tmp;
        }

        public long getCheck_time() {
            return check_time;
        }

        public void setCheck_time(long check_time) {
            this.check_time = check_time;
        }

        public long getCheck_time_tmp() {
            return check_time_tmp;
        }

        public void setCheck_time_tmp(long check_time_tmp) {
            this.check_time_tmp = check_time_tmp;
        }

        public Object getPhotos() {
            return photos;
        }

        public void setPhotos(Object photos) {
            this.photos = photos;
        }

        public String getAir_bottle_seal_code() {
            return air_bottle_seal_code;
        }

        public void setAir_bottle_seal_code(String air_bottle_seal_code) {
            this.air_bottle_seal_code = air_bottle_seal_code;
        }

        public String getAir_bottle_type_id() {
            return air_bottle_type_id;
        }

        public void setAir_bottle_type_id(String air_bottle_type_id) {
            this.air_bottle_type_id = air_bottle_type_id;
        }

        public String getAir_bottle_code() {
            return air_bottle_code;
        }

        public void setAir_bottle_code(String air_bottle_code) {
            this.air_bottle_code = air_bottle_code;
        }

        public int getUse_cycle() {
            return use_cycle;
        }

        public void setUse_cycle(int use_cycle) {
            this.use_cycle = use_cycle;
        }

        public String getVolume() {
            return volume;
        }

        public void setVolume(String volume) {
            this.volume = volume;
        }

        public long getProduce_time_tmp() {
            return produce_time_tmp;
        }

        public void setProduce_time_tmp(long produce_time_tmp) {
            this.produce_time_tmp = produce_time_tmp;
        }

        public long getNext_check_time() {
            return next_check_time;
        }

        public void setNext_check_time(long next_check_time) {
            this.next_check_time = next_check_time;
        }

        public String getProduction_unit_name() {
            return production_unit_name;
        }

        public void setProduction_unit_name(String production_unit_name) {
            this.production_unit_name = production_unit_name;
        }

        public Object getDetection_unit_id() {
            return detection_unit_id;
        }

        public void setDetection_unit_id(Object detection_unit_id) {
            this.detection_unit_id = detection_unit_id;
        }

        public long getSafe_time() {
            return safe_time;
        }

        public void setSafe_time(long safe_time) {
            this.safe_time = safe_time;
        }

        public List<BotInfoBean> getMobileAirBottleTrackingRecords() {
            return mobileAirBottleTrackingRecords;
        }

        public void setMobileAirBottleTrackingRecords(List<BotInfoBean> mobileAirBottleTrackingRecords) {
            this.mobileAirBottleTrackingRecords = mobileAirBottleTrackingRecords;
        }

        public static class MobileAirBottleTrackingRecordsBean {
            /**
             * mobileClinentInfo : 21d
             * warehouse_name : 道滘仓库
             * operator_name : 管理员
             * state_description : 空瓶初始化在仓库
             * create_time : 1516937797000
             * second_category_name : fdsfds
             * delivery_man_name : fdsad
             * client_name : dfsafd
             */

            private String mobileClinentInfo;
            private String warehouse_name;
            private String operator_name;
            private String state_description;
            private long create_time;
            private String second_category_name;
            private String delivery_man_name;
            private String client_name;

            public String getMobileClinentInfo() {
                return mobileClinentInfo;
            }

            public void setMobileClinentInfo(String mobileClinentInfo) {
                this.mobileClinentInfo = mobileClinentInfo;
            }

            public String getWarehouse_name() {
                return warehouse_name;
            }

            public void setWarehouse_name(String warehouse_name) {
                this.warehouse_name = warehouse_name;
            }

            public String getOperator_name() {
                return operator_name;
            }

            public void setOperator_name(String operator_name) {
                this.operator_name = operator_name;
            }

            public String getState_description() {
                return state_description;
            }

            public void setState_description(String state_description) {
                this.state_description = state_description;
            }

            public long getCreate_time() {
                return create_time;
            }

            public void setCreate_time(long create_time) {
                this.create_time = create_time;
            }

            public String getSecond_category_name() {
                return second_category_name;
            }

            public void setSecond_category_name(String second_category_name) {
                this.second_category_name = second_category_name;
            }

            public String getDelivery_man_name() {
                return delivery_man_name;
            }

            public void setDelivery_man_name(String delivery_man_name) {
                this.delivery_man_name = delivery_man_name;
            }

            public String getClient_name() {
                return client_name;
            }

            public void setClient_name(String client_name) {
                this.client_name = client_name;
            }
        }
    }
}
