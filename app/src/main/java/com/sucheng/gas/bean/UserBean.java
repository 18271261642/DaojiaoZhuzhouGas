package com.sucheng.gas.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/24.
 */

public class UserBean {


    private int code;
    private Object msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * user_id : 1
         * username : aa
         * full_name : 管理员
         * mobileModules : [{"module_id":10001,"module_name":"二维码初始化","module_description":"二维码初始化"},{"module_id":10002,"module_name":"获取气瓶信息","module_description":"获取气瓶信息"},{"module_id":10003,"module_name":"APP版本手动更新","module_description":"APP版本手动更新"},{"module_id":10004,"module_name":"强制空瓶入库-仓库","module_description":"强制空瓶入库-仓库"},{"module_id":10005,"module_name":"重瓶出库给配送工-仓库","module_description":"重瓶出库给配送工-仓库"}]
         */

        private int user_id;
        private String username;
        private String full_name;
        private List<MobileModulesBean> mobileModules;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public List<MobileModulesBean> getMobileModules() {
            return mobileModules;
        }

        public void setMobileModules(List<MobileModulesBean> mobileModules) {
            this.mobileModules = mobileModules;
        }

        public static class MobileModulesBean {
            /**
             * module_id : 10001
             * module_name : 二维码初始化
             * module_description : 二维码初始化
             */

            private int module_id;
            private String module_name;
            private String module_description;

            public int getModule_id() {
                return module_id;
            }

            public void setModule_id(int module_id) {
                this.module_id = module_id;
            }

            public String getModule_name() {
                return module_name;
            }

            public void setModule_name(String module_name) {
                this.module_name = module_name;
            }

            public String getModule_description() {
                return module_description;
            }

            public void setModule_description(String module_description) {
                this.module_description = module_description;
            }
        }
    }
}
