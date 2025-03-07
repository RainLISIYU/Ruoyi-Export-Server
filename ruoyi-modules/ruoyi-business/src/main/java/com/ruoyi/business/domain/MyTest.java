package com.ruoyi.business.domain;

import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.io.Serial;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试对象 my_test
 * 
 * @author ruoyi
 * @date 2024-03-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MyTest extends BaseEntity
{

    ThreadLocal<MyTest> threadLocal = ThreadLocal.withInitial(MyTest::new);
    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    @Excel(name = "删除标识")
    private Integer deletedAt;

    @Version
    private Integer version;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("address", getAddress())
            .toString();
    }

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(CgClass.class);
        enhancer.setClassLoader(CgClass.class.getClassLoader());
        enhancer.setCallback(new DebugMethodInterceptor());
        CgClass cg = (CgClass) enhancer.create();
        cg.send();
    }


}

class CgClass {
    public void send() {
        System.out.println("1111");
    }
}

class DebugMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return proxy.invoke(obj, args);
    }
}
