package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dao.SysJhTaobaoHotDao;
import com.superman.superman.dto.SysDayGoodDto;
import com.superman.superman.model.SysJhTaobaoAll;
import com.superman.superman.utils.PageParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.superman.superman.dto.SysFriendDto;
import com.superman.superman.dao.SysFriendDtoMapper;
import com.superman.superman.service.FriendDtoService;

@Service
public class FriendDtoServiceImpl implements FriendDtoService {
    @Autowired
    private SysFriendDtoMapper sysFriendDtoMapper;
    @Override
    public JSONObject queryList(PageParam pageParam) {
        PageParam temp = new PageParam(pageParam.getPageNo(), pageParam.getPageSize());
        List<SysFriendDto> sysFriendDtos = sysFriendDtoMapper.queryListGod(temp.getStartRow(), temp.getPageSize());
        Integer count = sysFriendDtoMapper.count();
        JSONObject var = new JSONObject();
        JSONArray data = new JSONArray();
        if (count == null || count == 0) {
            var.put("pageCount", 0);
            var.put("pageData", data);
            return var;
        }
        for (SysFriendDto var1 : sysFriendDtos) {
            SysDayGoodDto dto = new SysDayGoodDto();
            BeanUtils.copyProperties(var1, dto);
            List<String> images = sysFriendDtoMapper.getImages(var1.getId());
            dto.setContent_Images(images);
            data.add(dto);
        }
        var.put("pageCount", count);
        var.put("pageData", data);
        return var;
    }

    @Autowired
    private SysJhTaobaoHotDao sysJhTaobaoHotDao;

    @Override
    public JSONArray queryListFriend(PageParam pageParam) {
        PageParam temp = new PageParam(pageParam.getPageNo(), pageParam.getPageSize());

        JSONObject tempObject = null;
        JSONArray dataArray = new JSONArray();

        List<SysFriendDto> sysFriendDtos = sysFriendDtoMapper.queryListFriend(temp.getStartRow(), temp.getPageSize());
        for (SysFriendDto var : sysFriendDtos) {
            tempObject = new JSONObject();
            String goodIds = var.getGoodIds();
            JSONObject text = JSONObject.parseObject(goodIds);
            tempObject.put("titile",var.getTitile());
            tempObject.put("content",var.getContent());
            tempObject.put("image",var.getImage());
            tempObject.put("createtime",var.getCreatetime());
            JSONArray data = text.getJSONArray("data");
            JSONArray jsonArray = new JSONArray();
            data.forEach(child -> {
                Long goodId = (Long)child;
                JSONObject tempMap=new JSONObject();
                SysJhTaobaoAll sysJhTaobaoAll = sysJhTaobaoHotDao.queryLocalSimple(goodId);
                tempMap.put("goodId",sysJhTaobaoAll.getNumiid());
                tempMap.put("imagUrl",sysJhTaobaoAll.getPicturl());
                tempMap.put("isTmall",sysJhTaobaoAll.getIstamll());
                tempMap.put("goodName",sysJhTaobaoAll.getTitle());
                tempMap.put("coupon",sysJhTaobaoAll.getCoupon());
                tempMap.put("volume",sysJhTaobaoAll.getVolume());
                tempMap.put("zk_price",sysJhTaobaoAll.getCouponprice());
                tempMap.put("price",sysJhTaobaoAll.getZkfinalprice());
                jsonArray.add(tempMap);
            });
            tempObject.put("dataArray",jsonArray);
            dataArray.add(tempObject);
        }
        return dataArray;

    }

}
