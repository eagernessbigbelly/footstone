# license服务

> 启动需判断是否启用license，如果启用则需要获取license证书

## license证书构建步骤

### 使用java keytool 生成私钥

```
keytool -genkeypair -keysize 1024 -validity 3650 -alias SNMS_ESCROW_LICENSE -keystore privateKeys.keystore -storepass 12345678A -keypass 12345678A -dname "CN=teleinfo, OU=teleinfo, O=teleinfo, L=BJ, ST=BJ, C=CN"
```

##### 参数说明：
+ keysize 密钥长度
+ validity 私钥的有效期（单位：天）
+ alias 私钥别称
+ keystore 指定私钥库文件的名称 (生成在当前目录)
+ storepass 指定私钥库的密码 (keystore 文件存储密码)
+ keypass 指定别名条目的密码 (私钥加解密密码)
+ dname 证书个人信息
+ CN 为你的姓名
    + OU 为你的组织单位名称
    + O 为你的组织名称
    + L 为你所在的城市名称
    + ST 为你所在的省份名称
    + C 为你的国家名称

### 导出证书

```
keytool -exportcert -alias SNMS_ESCROW_LICENSE -keystore privateKeys.keystore -storepass 12345678A -file certfile.cer
```
##### 参数说明：
+ alias 私钥别称
+ keystore 指定私钥库文件的名称 (如果没有带路径，在当前目录查找)
+ storepass 指定私钥库的密码
+ file 导出证书文件名称
### 导入证书文件

```
keytool -import -alias SNMS_ESCROW_LICENSE -file certfile.cer -keystore publicCerts.keystore  -storepass 12345678A
```
##### 注意：
+ 生成完成后,将publicCerts.keystore放到resources中certs文件夹中

### 调用代码生成license.lic文件

```
LicenseCreateUtil中的main方法
```
##### 注意：
+ 生成lic证书时需要设置获取当前机器的cpu与mac地址，本地可能出现获取不到的情况，可以写死
+ 修改resources中config文件夹中license配置文件，将licensepath修改为lic的具体路径
##### 此时生成了生成了4个文件
+ certfile.cer 认证证书，已经没用了，可以删掉
+ privateKeys.keystore 私钥，授权者保留，不能泄露
+ publicCerts.keystore 公钥，给客人使用（一般放在验证的代码中使用）
+ license.lic 认证文件，给客人使用

### 修改yml
+ 服务启动校验开关通过yml中teleinfo.license.need控制
+ 服务请求校验开关通过yml中teleinfo.license.filter控制

此时就可以启动了
