package com.superman.superman.serviceImpl;

import com.superman.superman.model.Emp;
import com.superman.superman.mapper.EmpMapper;
import com.superman.superman.service.EmpService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author GGsnake
 * @since 2018-11-10
 */
@Service
public class EmpServiceImpl extends ServiceImpl<EmpMapper, Emp> implements EmpService {

}
