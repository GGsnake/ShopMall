package com.superman.superman.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.superman.superman.dto.SysDayGoodDto;
import com.superman.superman.model.SysDaygoods;
import com.superman.superman.utils.PageParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.superman.superman.dto.SysFriendDto;
import com.superman.superman.dao.SysFriendDtoMapper;
import com.superman.superman.service.SysFriendDtoService;

@Service
public class SysFriendDtoServiceImpl implements SysFriendDtoService{
    @Autowired
    private SysFriendDtoMapper sysFriendDtoMapper;
//    @Resource
//    private SysFriendDtoMapper sysFriendDtoMapper;

    @Override
    public JSONObject queryList(PageParam pageParam){
        PageParam temp =new PageParam(pageParam.getPageNo(),pageParam.getPageSize());
        List<SysFriendDto> sysFriendDtos = sysFriendDtoMapper.queryListGod(temp.getStartRow(), temp.getPageSize());
        Integer count = sysFriendDtoMapper.count();
        JSONObject var=new JSONObject();
        JSONArray data=new JSONArray();
        if (count==null||count==0){
            var.put("pageCount",0);
            var.put("pageData",data);
            return var;
        }
        for (SysFriendDto var1:sysFriendDtos){
            SysDayGoodDto dto=new SysDayGoodDto();
            BeanUtils.copyProperties(var1,dto);
            List<String> images = sysFriendDtoMapper.getImages(var1.getId());
            dto.setContent_Images(images);
            data.add(dto);
        }
        var.put("pageCount",count);
        var.put("pageData",data);
        return var;
    }

}
