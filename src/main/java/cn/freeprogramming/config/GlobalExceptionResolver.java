package cn.freeprogramming.config;


import cn.freeprogramming.enums.CommonErrorEnums;
import cn.freeprogramming.error.BusinessError;
import cn.freeprogramming.vo.result.R;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局Controller层异常处理类
 * @author humorchen
 * @date 2021/12/5 15:28
 */

@ControllerAdvice
@Slf4j
public class GlobalExceptionResolver {

//    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionResolver.class);

    /**
     * 处理所有不可知异常
     *
     * @param e 异常
     * @return json结果
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R handleException(Exception e) {
        // 打印异常堆栈信息
        //dubbo异常处理
        if (e instanceof RuntimeException )
        log.error("异常拦截器打印");
        log.error("类名："+e.getClass().getName());
        log.error("错误信息："+e.getMessage());
        log.error("打印整个错误："+e);
        try {
            BusinessError businessError = (BusinessError)e;
            if (businessError != null){
                return R.error(businessError);
            }
        } catch (Exception exception) {
            System.err.println("转化失败");
            exception.printStackTrace();
        }
        return R.error(CommonErrorEnums.BUSINESS_ERROR);
    }

    /**
     * 处理所有业务异常
     *
     * @param e 业务异常
     * @return json结果
     */
    @ExceptionHandler(BusinessError.class)
    @ResponseBody
    public R handleOpdRuntimeException(BusinessError e) {
        // 不打印异常堆栈信息
        log.error(e.getMessage());
        return R.error(e);
    }
}
