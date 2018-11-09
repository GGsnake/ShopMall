package com.superman.superman.model;

import java.util.List;

/**
 * Created by liujupeng on 2018/11/6.
 */
public class PddBillBean {

    /**
     * app_new_bill_list_response : {"list":[{"pid":"60013_2039","order_sn":"180515-392972355200461","goods_thumb_url":"http://pddtestimg.yangkeduo.com/test/2017-09-19/ef972ed0e60279164d8fbea31515e238.jpeg","goods_id":10073718,"goods_name":"商品锁定19","money":1000,"pay_time":null,"updated_at":1526969107,"verify_time":null,"status":1,"custom_parameters":"abcd"}],"total_count":1}
     */

    private AppNewBillListResponseBean app_new_bill_list_response;

    public AppNewBillListResponseBean getApp_new_bill_list_response() {
        return app_new_bill_list_response;
    }

    public void setApp_new_bill_list_response(AppNewBillListResponseBean app_new_bill_list_response) {
        this.app_new_bill_list_response = app_new_bill_list_response;
    }

    public static class AppNewBillListResponseBean {
        /**
         * list : [{"pid":"60013_2039","order_sn":"180515-392972355200461","goods_thumb_url":"http://pddtestimg.yangkeduo.com/test/2017-09-19/ef972ed0e60279164d8fbea31515e238.jpeg","goods_id":10073718,"goods_name":"商品锁定19","money":1000,"pay_time":null,"updated_at":1526969107,"verify_time":null,"status":1,"custom_parameters":"abcd"}]
         * total_count : 1
         */

        private int total_count;
        private List<ListBean> list;

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * pid : 60013_2039
             * order_sn : 180515-392972355200461
             * goods_thumb_url : http://pddtestimg.yangkeduo.com/test/2017-09-19/ef972ed0e60279164d8fbea31515e238.jpeg
             * goods_id : 10073718
             * goods_name : 商品锁定19
             * money : 1000
             * pay_time : null
             * updated_at : 1526969107
             * verify_time : null
             * status : 1
             * custom_parameters : abcd
             */

            private String pid;
            private String order_sn;
            private String goods_thumb_url;
            private int goods_id;
            private String goods_name;
            private int money;
            private Object pay_time;
            private int updated_at;
            private Object verify_time;
            private int status;
            private String custom_parameters;

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getOrder_sn() {
                return order_sn;
            }

            public void setOrder_sn(String order_sn) {
                this.order_sn = order_sn;
            }

            public String getGoods_thumb_url() {
                return goods_thumb_url;
            }

            public void setGoods_thumb_url(String goods_thumb_url) {
                this.goods_thumb_url = goods_thumb_url;
            }

            public int getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(int goods_id) {
                this.goods_id = goods_id;
            }

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public int getMoney() {
                return money;
            }

            public void setMoney(int money) {
                this.money = money;
            }

            public Object getPay_time() {
                return pay_time;
            }

            public void setPay_time(Object pay_time) {
                this.pay_time = pay_time;
            }

            public int getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(int updated_at) {
                this.updated_at = updated_at;
            }

            public Object getVerify_time() {
                return verify_time;
            }

            public void setVerify_time(Object verify_time) {
                this.verify_time = verify_time;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getCustom_parameters() {
                return custom_parameters;
            }

            public void setCustom_parameters(String custom_parameters) {
                this.custom_parameters = custom_parameters;
            }
        }
    }
}
