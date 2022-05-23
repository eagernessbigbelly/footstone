package cn.footstone.config.filter;

import cn.footstone.license.core.verify.LicenseMessage;
import cn.footstone.license.support.LicenseVerifyUtil;
import cn.footstone.model.R;
import cn.hutool.json.JSONUtil;
import de.schlichtherle.license.LicenseContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @description: License filter
 * @author: liuyan
 * @create: 2022−05-18 2:14 PM
 */
@ConditionalOnProperty(prefix = "company.license", name = "filter", havingValue = "true")
@WebFilter(urlPatterns = "/*", filterName = "SimpleFilter")
@Order(1)
@Slf4j
public class LicenseFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        LicenseContent licenseContent;
        try {
            //校验license
            licenseContent = LicenseVerifyUtil.clientLicenseVerify();
        } catch (Exception e) {
            log.error(LicenseMessage.EXC_LICENSE_VERIFY_ERROR, e);
            httpResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            httpResponse.getWriter().write(JSONUtil.toJsonStr(new R<>(R.FAIL, LicenseMessage.EXC_LICENSE_VERIFY_ERROR, null)));
            return;
        }
        if (null == licenseContent) {
            httpResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            httpResponse.getWriter().write(JSONUtil.toJsonStr(new R<>(R.FAIL, LicenseMessage.EXC_LICENSE_NOINFO, null)));
            return;
        }
        //校验license 时间
        Date now = new Date();
        if (now.before(licenseContent.getNotBefore())
                || now.after(licenseContent.getNotAfter())) {
            httpResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            httpResponse.getWriter().write(JSONUtil.toJsonStr(new R<>(R.FAIL, LicenseMessage.EXC_LICENSE_TIME_INVALID, null)));
            return;
        }
        filterChain.doFilter(httpRequest, servletResponse);
//            if (licenseContent == null) {
//                String machineCode = HardwareInfoUtil.getMachineCode();
//                httpRequest.setAttribute("machineCode", machineCode);
////                httpRequest.getRequestDispatcher("/web/core/license/license.jsp").forward(httpRequest, httpResponse);
//                return;
//            } else {
//                filterChain.doFilter(servletRequest, servletResponse);
//            }
    }

    @Override
    public void destroy() {

    }
}
