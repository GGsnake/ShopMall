package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.CollectDao;
import com.superman.superman.dao.UserinfoMapper;
import com.superman.superman.dto.CollectBeanDto;
import com.superman.superman.model.CollectBean;
import com.superman.superman.model.Userinfo;
import com.superman.superman.service.CollectService;
import com.superman.superman.utils.GoodUtils;
import com.superman.superman.utils.PageParam;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;


/**
 * Created by snake on 2018/11/20.
 */
@Log
@Service("collectService")
public class CollectServiceImpl implements CollectService {
    @Autowired
    private CollectDao collectDao;
    @Autowired
    private UserinfoMapper userinfoMapper;
    @Value("${juanhuang.range}")
    Double rangeaa;
    /**
     * 查询收藏
     *
     * @param uid
     * @param pageParam
     * @return
     */
    @Override
    public JSONArray queryCollect(@NonNull Long uid, @NonNull PageParam pageParam) {
        Userinfo userinfo = userinfoMapper.selectByPrimaryKey(uid);
        if (userinfo == null) {
            return null;
        }
        Integer roleId = userinfo.getRoleId();
        Integer score = userinfo.getScore();
        JSONArray data = new JSONArray();
        List<CollectBean> query = collectDao.query(uid, pageParam.getStartRow(), pageParam.getPageSize());
        if (query == null || query.size() == 0) {
            return null;
        }
        if (roleId == 1) {
            for (CollectBean collectBean : query) {
                CollectBeanDto temp = new CollectBeanDto();
                BeanUtils.copyProperties(collectBean, temp);
                Long promotion_rate = collectBean.getPromotion_rate();
                BigDecimal agent = GoodUtils.commissonAritTaobao(collectBean.getCoupon_price().toString(), promotion_rate.toString());
                temp.setAgent(agent.doubleValue());
                data.add(temp);
            }
        }
        if (roleId == 2) {
            Integer sc = 100 - score;
            for (CollectBean collectBean : query) {
                CollectBeanDto temp = new CollectBeanDto();
                Long promotion_rate = collectBean.getPromotion_rate();
                BeanUtils.copyProperties(collectBean, temp);
                BigDecimal agent = GoodUtils.commissonAritTaobao(collectBean.getCoupon_price().toString(), promotion_rate.toString());
                Double v = agent.doubleValue() * sc / 100;
                temp.setAgent(new BigDecimal(v).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
                data.add(temp);
            }
        }
        if (roleId == 3) {
            for (CollectBean collectBean : query) {
                CollectBeanDto temp = new CollectBeanDto();
                BeanUtils.copyProperties(collectBean, temp);
                temp.setAgent(0d);
                data.add(temp);
            }
        }
        return data;
    }

    /**
     * 删除收藏
     *
     * @param colId
     * @param uid
     * @return
     */
    @Override
    public Boolean deleteCollect(@NonNull Integer colId, @NonNull Long uid) {
        CollectBean collectBean = collectDao.querySimple(colId);
        if (collectBean == null || collectBean.getUserId() != uid)
            return false;
        Integer flag = 0;
        try {
            flag = collectDao.delete(Long.valueOf(colId));
        } catch (Exception e) {
            log.warning(e.getMessage());
            return false;
        }
        return flag == 0 ? false : true;
    }

    /**
     * 统计收藏数量
     *
     * @param uid
     * @return
     */
    @Override
    public Integer countCollect(@NonNull Long uid) {
        Integer count = collectDao.count(uid);
        return count == null ? 0 : count;
    }

    /**
     * 增加收藏
     *
     * @param collectBean
     * @return
     */
    @Override
    public Boolean addCollect(@NonNull CollectBean collectBean) {
        Integer flag = 0;
        try {
            flag = collectDao.addCollect(collectBean);
        } catch (Exception e) {
            log.warning(e.getMessage());
            return false;
        }
        return flag == 0 ? false : true;
    }
}
