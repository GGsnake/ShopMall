package com.superman.superman.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by liujupeng on 2018/11/14.
 */
@ToString
@Setter
@Getter
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


}
