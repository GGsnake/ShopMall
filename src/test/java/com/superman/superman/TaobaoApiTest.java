package com.superman.superman;

import com.superman.superman.service.TaoBaoApiService;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@Log
@RunWith(SpringRunner.class)
@SpringBootTest
public class TaobaoApiTest {
    @Autowired
    TaoBaoApiService taoBaoApiService;
    //通过
    @Test
    public void convertTkl(){
        taoBaoApiService.convertTaobaoTkl("￥vxfnbIAP8uA￥");
    }
    @Test
    public void search(){
        TbkDgMaterialOptionalRequest tk=new TbkDgMaterialOptionalRequest();
        tk.setHasCoupon(true);
        tk.setQ("模拟");
        taoBaoApiService.serachGoodsAll(tk,28l);
    }

}

