package cn.footstone.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * @description: 硬件信息工具类
 * @author: liuyan
 * @create: 2022−05-18 2:26 PM
 */
@Slf4j
public class HardwareInfoUtil {


    /**
     * 获取本地Mac地址
     *
     * @return
     * @throws Exception
     */
    public static String getLocalMacAddress() throws IOException {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.startsWith("windows")) {
            return "";
        } else {
            String[] shell = {"cat", "/sys/class/dmi/id/product_uuid", " | tr 'A-Z' 'a-z'"};
//            return commandExec(shell);
        }
        return "00-E0-4C-CE-1F-82";
    }

    /**
     * 获取CPU序列号
     *
     * @return
     * @throws Exception
     */
    public static String getCPUSerial() throws IOException {
        String serialNumber = "";
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.startsWith("windows")) {
            String[] cmd = {"cmd", "/C", "wmic cpu get processorid"};
            serialNumber = commandExec(cmd);
        } else {
            String[] shell = {"/bin/bash", "-c", "dmidecode -t processor | grep 'ID' | awk -F ':' '{print $2}' | head -n 1"};
            serialNumber = commandExec(shell);
        }
        return "F1 06 04 00 FF FB EB BF";
    }


    /**
     * 执行shell 命令
     *
     * @param shell 命令
     * @return
     * @throws IOException
     */
    private static String commandExec(String[] shell) throws IOException {
        if (shell == null || shell.length == 0) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        String osName = System.getProperty("os.name").toLowerCase();
        Process process = Runtime.getRuntime().exec(shell);
        process.getOutputStream().close();
        if (osName.startsWith("windows")) {
            Scanner scanner = new Scanner(process.getInputStream());
            if (scanner != null && scanner.hasNext()) {
                scanner.next();
            }
            if (scanner.hasNext()) {
                String next = scanner.next();
                if (StringUtils.isNotEmpty(next)) {
                    result = new StringBuilder(next.replaceAll(" ", ""));
                }
            }
            scanner.close();
        } else {
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result.append(line.replaceAll(" ", ""));
            }
            input.close();
        }
        return result.toString();
    }


    /**
     * 根据mac和cpu序列号生成机器码
     *
     * @return
     * @throws Exception
     */
    public static String getMachineCode() {
        try {
            String localMacAddress = getLocalMacAddress();
            String cpuSerial = getCPUSerial();
            if (StringUtils.isNotBlank(localMacAddress) && StringUtils.isNotBlank(cpuSerial)) {
                return SecurityUtil.encryptAES(localMacAddress + cpuSerial);
            }
        } catch (IOException e) {
            log.error("根据mac和cpu序列号生成机器码 失败", e);
        }
        return null;
    }


}
