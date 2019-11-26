package com.itheima.web;

import com.itheima.domain.User;
import com.itheima.service.UserService.UserService;
import com.itheima.utils.CommonsUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        //获得表单数据
        Map<String, String[]> parameterMap = request.getParameterMap();
        User user = new User();
        try {
            //指定一个类型转换器(将String转成Date)，回调，代码使我们写的，但是代码是由系统来调用的
            ConvertUtils.register(new Converter() {
                @Override
                public Object convert(Class arg0, Object arg1) {
                    //将string转成date
                    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                    Date parse = null;
                    try{
                        parse=format.parse(arg1.toString());
                    }catch (ParseException e){
                        e.printStackTrace();
                    }
                    return null;
                }
            }, Date.class);

            //映射封装
            BeanUtils.populate(user, parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //private String uid;
        user.setUid(CommonsUtils.getUUID());
        //private String telephone;
        user.setTelephone(null);
        //private int state;//是否激活
        user.setState(0);//表示未激活
        //private String code;//激活码
        user.setCode(CommonsUtils.getUUID());

        //将user传递给service层
        UserService service = new UserService();
        boolean isRegisterSuccess = service.register(user);
        //System.out.println(request.getContextPath().toString());
        //是否注册成功
        if(isRegisterSuccess){
            String emailMsg="恭喜注册成功，请点击下面链接"


            //跳转到成功页面
            response.sendRedirect(request.getContextPath()+"/registerSuccess.jsp");

        }else{
            response.sendRedirect(request.getContextPath()+"/registerFail.jsp");
        }
    }
}
