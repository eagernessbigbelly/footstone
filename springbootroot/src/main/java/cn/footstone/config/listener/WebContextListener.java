package cn.footstone.config.listener;

import cn.footstone.license.support.LicenseVerifyUtil;
import cn.footstone.utils.HardwareInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


/**
 * @description: License Listener
 * @author: liuyan
 * @create: 2022−05-18 2:14 PM
 */
@ConditionalOnProperty(prefix = "company.license", name = "need", havingValue = "true")
@Slf4j
@WebListener
public class WebContextListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            LicenseVerifyUtil.clientLicenseInstall();
        } catch (Exception e) {
            log.error("+++++++++++++++++++License 证书安装失败,程序终止+++++++++++++++++++");
            log.error("License authcode:{}", HardwareInfoUtil.getMachineCode());
            System.exit(0);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
