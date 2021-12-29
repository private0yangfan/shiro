package com.baizhi;

import com.baizhi.realm.CustomerMd5Realm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;

import java.util.Arrays;

public class TestCustomerMd5RealmAuthenicator {

    public static void main(String[] args) {

        //创建安全管理器
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        //注入realm
        CustomerMd5Realm realm = new CustomerMd5Realm();
        //设置realm使用hash凭证匹配器

        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //使用算法
        credentialsMatcher.setHashAlgorithmName("md5");
        //散列次数
        credentialsMatcher.setHashIterations(1024);
        realm.setCredentialsMatcher(credentialsMatcher);

        defaultSecurityManager.setRealm(realm);
        //将安全管理器注入安全工具
        SecurityUtils.setSecurityManager(defaultSecurityManager);

        //通过安全工具类获取subject
        Subject subject = SecurityUtils.getSubject();

        //认证
        UsernamePasswordToken token = new UsernamePasswordToken("xiaochen", "123");

        try {
            subject.login(token);
            System.out.println("登录成功");
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("用户名错误");
        }catch (IncorrectCredentialsException e){
            e.printStackTrace();
            System.out.println("密码错误");
        }


        //授权
        if(subject.isAuthenticated()){

            //基于角色权限控制
            System.out.println(subject.hasRole("super"));

            //基于多角色权限控制
            System.out.println(subject.hasAllRoles(Arrays.asList("admin", "super")));

            //是否具有其中一个角色
            boolean[] booleans = subject.hasRoles(Arrays.asList("admin", "super", "user"));
            for (boolean aBoolean : booleans) {
                System.out.println(aBoolean);
            }
            System.out.println("==============================================");

            //基于权限字符串的访问控制  资源标识符:操作:资源类型
            System.out.println("权限:"+subject.isPermitted("user:update:01"));
            System.out.println("权限:"+subject.isPermitted("product:create:02"));

            //分别具有那些权限
            boolean[] permitted = subject.isPermitted("user:*:01", "order:*:10");
            for (boolean b : permitted) {
                System.out.println(b);
            }

            //同时具有哪些权限
            boolean permittedAll = subject.isPermittedAll("user:*:01", "product:create:01");
            System.out.println(permittedAll);

//            基于角色的访问控制
//            RBAC基于角色的访问控制（Role-Based Access Control）是以
//                    角色为中心进行访问控制

            if(subject.hasRole("admin")){
                //操作什么资源
            }
//
//            基于资源的访问控制
//            RBAC基于资源的访问控制（Resource-Based Access Control）
//            是以资源为中心进行访问控制
            //资源实例//对01用户进行修改
            if(subject.isPermitted("user:update:01")){

            }
            if(subject.isPermitted("user:update:*")){  //资源类型
            }
//            权限字符串的规则是：资源标识符：操作：资源实例标识符，
//            意思是对哪个资源的哪个实例具有什么操作，“:”是资源/操作/实
//            例的分割符，权限字符串也可以使用*通配符。

//            编程式
            Subject subject1 = SecurityUtils.getSubject();
            if(subject1.hasRole("admin")) {
                //有权限
            } else {
                //无权限
            }


        }


    }

    //注解式
    @RequiresRoles("admin")
    public void hello() {
        //有权限
    }

    // 标签式
//    JSP/GSP 标签：在JSP/GSP 页面通过相应的标签完成：
//<shiro:hasRole name="admin">
//<!— 有权限—>
//</shiro:hasRole>
//    注意: Thymeleaf 中使用shiro需要额外集成!

}

