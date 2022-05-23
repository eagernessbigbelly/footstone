package cn.footstone.license.core;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @description: LicenseExtra
 * @author: liuyan
 * @create: 2022−05-20 4:24 PM
 */
@AllArgsConstructor
@Data
public class LicenseExtra {

    /**
     * 服务器硬件信息
     */
    private String machineCode;
    /**
     * 版本
     */
    private Double version;
}
