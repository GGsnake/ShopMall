package com.superman.superman.model;

import java.util.List;

/**
 * Created by liujupeng on 2018/11/12.
 */
public class JdBillBean {
    /**
     * data : [{"comments":341,"commissionShare":80,"endTime":32472115200000,"goodCommentsShare":99,"imageUrl":"jfs/t1/9810/35/3978/247062/5bd97f96E78e81058/106f5a1dcf5cff84.jpg","inOrderCount30Days":2644,"pingouPrice":1,"pingouTmCount":2,"pingouUrl":"https://wq.jd.com/pingou_api/GetCpsGroup?sku_id=35317402288&from=cps","promotionUrl":"","skuId":35317402288,"skuName":"【10片装疯抢特惠】鲜润补水保湿面膜（清爽水润 控油平衡 细致毛孔）玻尿酸蚕丝面膜贴 10片装","startTime":1541779200000,"wlCommissionShare":80,"wlPrice":49.9},{"comments":11546,"commissionShare":80,"endTime":32472115200000,"goodCommentsShare":98,"imageUrl":"jfs/t2980/37/1577663409/253740/9b74447a/57871649N789a8f27.jpg","inOrderCount30Days":349,"pingouPrice":11.11,"pingouTmCount":2,"pingouUrl":"https://wq.jd.com/pingou_api/GetCpsGroup?sku_id=10481718811&from=cps","promotionUrl":"","skuId":10481718811,"skuName":"LAAZEE蓝哲汽车钥匙扣经典款创意教师节礼物生日礼物男士女士皮带腰挂钥匙圈金属钥匙链刻字小礼品","startTime":1541952000000,"wlCommissionShare":80,"wlPrice":12},{"comments":5858,"commissionShare":80,"endTime":32472115200000,"goodCommentsShare":98,"imageUrl":"jfs/t2620/28/3329292690/339843/ac312192/5788fa13Ne0841b9f.jpg","inOrderCount30Days":95,"pingouPrice":11.11,"pingouTmCount":2,"pingouUrl":"https://wq.jd.com/pingou_api/GetCpsGroup?sku_id=10486353079&from=cps","promotionUrl":"","skuId":10486353079,"skuName":"LAAZEE汽车钥匙扣 男士钥匙扣金属皮带腰挂扣 商务实用礼品教师节礼物送父亲男友创意DIY生日礼物 010全金属银（光面无字）","startTime":1541952000000,"wlCommissionShare":80,"wlPrice":15},{"comments":177,"commissionShare":80,"endTime":32472115200000,"goodCommentsShare":97,"imageUrl":"jfs/t2626/33/3827295476/177252/f832e275/579e19e4N999fcd91.jpg","inOrderCount30Days":8,"pingouPrice":11.11,"pingouTmCount":2,"pingouUrl":"https://wq.jd.com/pingou_api/GetCpsGroup?sku_id=10528199595&from=cps","promotionUrl":"","skuId":10528199595,"skuName":"LAAZEE足球迷纪念品世界杯钥匙扣环国际足联冠军奖杯足协会圣杯模型英超锦标赛梅西C罗法国","startTime":1541952000000,"wlCommissionShare":80,"wlPrice":19},{"comments":996,"commissionShare":80,"endTime":32472115200000,"goodCommentsShare":98,"imageUrl":"jfs/t2623/192/3020157670/46613/1cc1ddc9/577fbe8dNda187b8b.jpg","inOrderCount30Days":6,"pingouPrice":11.11,"pingouTmCount":2,"pingouUrl":"https://wq.jd.com/pingou_api/GetCpsGroup?sku_id=10470201835&from=cps","promotionUrl":"","skuId":10470201835,"skuName":"LAAZEE不锈钢钥匙扣 结实耐用汽车钥匙扣男士腰挂扣 父亲节生日礼物送父亲爸爸男友个性礼品 221方形","startTime":1541952000000,"wlCommissionShare":80,"wlPrice":19.9},{"comments":5,"commissionShare":80,"endTime":32472115200000,"goodCommentsShare":100,"imageUrl":"jfs/t24958/275/1161660369/189011/9cccfa8/5b8a5c12N8afb36be.jpg","inOrderCount30Days":6,"pingouPrice":115,"pingouTmCount":2,"pingouUrl":"https://wq.jd.com/pingou_api/GetCpsGroup?sku_id=32293714819&from=cps","promotionUrl":"","skuId":32293714819,"skuName":"2018秋冬打底裤女外穿高腰小脚裤弹力铅笔裤百搭韩版长裤 黑色 均码","startTime":1535817600000,"wlCommissionShare":80,"wlPrice":128},{"comments":0,"commissionShare":80,"endTime":32472115200000,"goodCommentsShare":100,"imageUrl":"jfs/t24037/111/2453020544/337770/8c30bde1/5b7f9e62N32ac7f10.jpg","inOrderCount30Days":3,"pingouPrice":62,"pingouTmCount":2,"pingouUrl":"https://wq.jd.com/pingou_api/GetCpsGroup?sku_id=31939044229&from=cps","promotionUrl":"","skuId":31939044229,"skuName":"丝巾女加大纯色棉麻围巾沙滩巾夏季防晒披肩两用空调披肩旅游遮阳纱巾 大红 均码","startTime":1535126400000,"wlCommissionShare":80,"wlPrice":69},{"comments":0,"commissionShare":80,"endTime":32472115200000,"goodCommentsShare":100,"imageUrl":"jfs/t1/5770/9/2076/109722/5b95f35dE048e5d0e/f5205434326b0cd8.jpg","inOrderCount30Days":1,"pingouPrice":115,"pingouTmCount":2,"pingouUrl":"https://wq.jd.com/pingou_api/GetCpsGroup?sku_id=32776761154&from=cps","promotionUrl":"","skuId":32776761154,"skuName":"【包邮】帽子男棒球帽女韩版潮四季户外休闲时尚运动防晒鸭舌帽遮阳帽 黑色 均码","startTime":1536595200000,"wlCommissionShare":80,"wlPrice":129}]
     * resultCode : 1
     * resultMessage : success
     * total : 164488889
     */

    private Integer resultCode;
    private String resultMessage;
    private Integer total;
    private List<JdBillBean> data;

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<JdBillBean> getData() {
        return data;
    }

    public void setData(List<JdBillBean> data) {
        this.data = data;
    }


}
