package cn.freeprogramming.config;


import cn.freeprogramming.enums.CommonErrorEnums;
import cn.freeprogramming.error.BusinessError;
import cn.freeprogramming.vo.result.R;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        log.error(e.getMessage());
        log.error("异常拦截器打印");
        log.error("类名："+e.getClass().getName());
        log.error("错误信息："+e.getMessage());
        log.error("打印整个错误："+e);
        return R.error(CommonErrorEnums.BUSINESS_ERROR);
    }

    /**
     * 缺少请求体或者请求体不可读
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public R handleRequestBodyException(HttpMessageNotReadableException e){
        log.error(e.getMessage());
        return R.error(CommonErrorEnums.ILLEGAL_REQUEST_BODY);
    }

    /**
     * 参数校验不通过
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public R handleNotValidArgumentException(MethodArgumentNotValidException e){
        log.error(e.getMessage());
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        Set<String> tips = new HashSet<>(allErrors.size());
        allErrors.forEach(p->tips.add(p.getDefaultMessage()));
        return new R(CommonErrorEnums.ILLEGAL_ARGUMENT.name(),"参数审核未通过",tips);
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
        log.error(e.getErrorMessage());
        return R.error(e);
    }
}
