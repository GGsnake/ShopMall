package com.superman.superman.model;

import java.util.List;

/**
 * Created by liujupeng on 2018/11/14.
 */
public class PddGoodDetailBean  {

    /**
     * goods_id : 545922677
     * goods_name : 【百草味-夏威夷果150g/300g/多规格可选】 送开口器牛皮纸袋内!
     * goods_desc : 【百草味-夏威夷果100g/300g多规格可选】坚果零食干果炒货 奶油味送开口器牛皮纸袋内!  我们的夏威夷果奶香四溢，酥脆爽滑，坚果中的皇后。每袋都附有开果器的!!!附在牛皮纸袋内部!!您收货后仔细找找就能发现了呢。（使用小窍门：您可以把开果器插在果果的裂缝处，然后插深一些，再转一下，这样比较好开哦）
     * goods_thumbnail_url : http://omsproductionimg.yangkeduo.com/images/2017-11-28/34eba02f3c71f5a269e64e9042d9950d.jpeg
     * goods_image_url : http://omsproductionimg.yangkeduo.com/images/2017-10-17/0b7bca0ff9378f573337f6fb8f0448a8.jpeg
     * goods_gallery_urls : ["http://omsproductionimg.yangkeduo.com/images/2017-11-28/b8ff55a74b615d06b91bd6a8785f739a.jpeg","http://t04img.yangkeduo.com/images/2018-04-08/04c90c1db3e618eeb7d3b681a53fdfcf.jpeg","http://omsproductionimg.yangkeduo.com/images/2017-10-17/3986d8258d16b4c74e56c2629f41a299.jpeg","http://t08img.yangkeduo.com/images/2018-04-08/8699ad96bfe31e7e39dbf7d233106f91.jpeg","http://t03img.yangkeduo.com/images/2018-04-08/fe4aa08cbc1f6c9a88bb00f66ab86636.jpeg"]
     * sold_quantity : 92
     * min_group_price : 1990
     * min_normal_price : 2990
     * mall_name : 百草味旗舰店
     * category_id : 1
     * category_name : 美食
     * opt_id : 1
     * opt_name : 美食
     * cat_ids : [2,65,4,0]
     * has_coupon : true
     * coupon_min_order_amount : 300
     * coupon_discount : 300
     * coupon_total_quantity : 50000
     * coupon_remain_quantity : 49940
     * coupon_start_time : 1525536000
     * coupon_end_time : 1525967999
     * promotion_rate : 150
     * goods_eval_score : 0
     * goods_eval_count : 0
     * cat_id : null
     * avg_desc : 494
     * avg_lgst : 494
     * avg_serv : 494
     */

    private Integer goods_id;
    private String goods_name;
    private String goods_desc;
    private String goods_thumbnail_url;
    private String goods_image_url;
    private Integer sold_quantity;
    private Integer min_group_price;
    private Integer min_normal_price;
    private String mall_name;
    private Integer category_id;
    private String category_name;
    private Integer opt_id;
    private String opt_name;
    private boolean has_coupon;
    private Integer coupon_min_order_amount;
    private Integer coupon_discount;
    private Integer coupon_total_quantity;
    private Integer coupon_remain_quantity;
    private Integer coupon_start_time;
    private Integer coupon_end_time;
    private Integer promotion_rate;
    private Integer goods_eval_score;
    private Integer goods_eval_count;
    private Object cat_id;
    private Integer avg_desc;
    private Integer avg_lgst;
    private Integer avg_serv;
    private List<String> goods_gallery_urls;
    private List<Integer> cat_ids;

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_desc() {
        return goods_desc;
    }

    public void setGoods_desc(String goods_desc) {
        this.goods_desc = goods_desc;
    }

    public String getGoods_thumbnail_url() {
        return goods_thumbnail_url;
    }

    public void setGoods_thumbnail_url(String goods_thumbnail_url) {
        this.goods_thumbnail_url = goods_thumbnail_url;
    }

    public String getGoods_image_url() {
        return goods_image_url;
    }

    public void setGoods_image_url(String goods_image_url) {
        this.goods_image_url = goods_image_url;
    }

    public Integer getSold_quantity() {
        return sold_quantity;
    }

    public void setSold_quantity(Integer sold_quantity) {
        this.sold_quantity = sold_quantity;
    }

    public Integer getMin_group_price() {
        return min_group_price;
    }

    public void setMin_group_price(Integer min_group_price) {
        this.min_group_price = min_group_price;
    }

    public Integer getMin_normal_price() {
        return min_normal_price;
    }

    public void setMin_normal_price(Integer min_normal_price) {
        this.min_normal_price = min_normal_price;
    }

    public String getMall_name() {
        return mall_name;
    }

    public void setMall_name(String mall_name) {
        this.mall_name = mall_name;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Integer getOpt_id() {
        return opt_id;
    }

    public void setOpt_id(Integer opt_id) {
        this.opt_id = opt_id;
    }

    public String getOpt_name() {
        return opt_name;
    }

    public void setOpt_name(String opt_name) {
        this.opt_name = opt_name;
    }

    public boolean isHas_coupon() {
        return has_coupon;
    }

    public void setHas_coupon(boolean has_coupon) {
        this.has_coupon = has_coupon;
    }

    public Integer getCoupon_min_order_amount() {
        return coupon_min_order_amount;
    }

    public void setCoupon_min_order_amount(Integer coupon_min_order_amount) {
        this.coupon_min_order_amount = coupon_min_order_amount;
    }

    public Integer getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(Integer coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public Integer getCoupon_total_quantity() {
        return coupon_total_quantity;
    }

    public void setCoupon_total_quantity(Integer coupon_total_quantity) {
        this.coupon_total_quantity = coupon_total_quantity;
    }

    public Integer getCoupon_remain_quantity() {
        return coupon_remain_quantity;
    }

    public void setCoupon_remain_quantity(Integer coupon_remain_quantity) {
        this.coupon_remain_quantity = coupon_remain_quantity;
    }

    public Integer getCoupon_start_time() {
        return coupon_start_time;
    }

    public void setCoupon_start_time(Integer coupon_start_time) {
        this.coupon_start_time = coupon_start_time;
    }

    public Integer getCoupon_end_time() {
        return coupon_end_time;
    }

    public void setCoupon_end_time(Integer coupon_end_time) {
        this.coupon_end_time = coupon_end_time;
    }

    public Integer getPromotion_rate() {
        return promotion_rate;
    }

    public void setPromotion_rate(Integer promotion_rate) {
        this.promotion_rate = promotion_rate;
    }

    public Integer getGoods_eval_score() {
        return goods_eval_score;
    }

    public void setGoods_eval_score(Integer goods_eval_score) {
        this.goods_eval_score = goods_eval_score;
    }

    public Integer getGoods_eval_count() {
        return goods_eval_count;
    }

    public void setGoods_eval_count(Integer goods_eval_count) {
        this.goods_eval_count = goods_eval_count;
    }

    public Object getCat_id() {
        return cat_id;
    }

    public void setCat_id(Object cat_id) {
        this.cat_id = cat_id;
    }

    public Integer getAvg_desc() {
        return avg_desc;
    }

    public void setAvg_desc(Integer avg_desc) {
        this.avg_desc = avg_desc;
    }

    public Integer getAvg_lgst() {
        return avg_lgst;
    }

    public void setAvg_lgst(Integer avg_lgst) {
        this.avg_lgst = avg_lgst;
    }

    public Integer getAvg_serv() {
        return avg_serv;
    }

    public void setAvg_serv(Integer avg_serv) {
        this.avg_serv = avg_serv;
    }

    public List<String> getGoods_gallery_urls() {
        return goods_gallery_urls;
    }

    public void setGoods_gallery_urls(List<String> goods_gallery_urls) {
        this.goods_gallery_urls = goods_gallery_urls;
    }

    public List<Integer> getCat_ids() {
        return cat_ids;
    }

    public void setCat_ids(List<Integer> cat_ids) {
        this.cat_ids = cat_ids;
    }
}
