package com.ruoyi.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.business.dao.MyTestDao;
import com.ruoyi.business.entity.MyTest;
import com.ruoyi.business.service.MyTestService;
import org.springframework.stereotype.Service;

/**
 * 测试表(MyTest)表服务实现类
 *
 * @author chenqiang
 * @since 2024-03-08 14:08:06
 */
@Service("myTestService")
public class MyTestServiceImpl extends ServiceImpl<MyTestDao, MyTest> implements MyTestService {

}

