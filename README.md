# Telefriends
一款手表跨品牌加好友软件

> [!CAUTION]
> 该软件仅供学习和研究使用。其旨在为学术和研究人员提供参考和资料，任何其他目的均不适用。严禁将此软件用于任何商业或非法用途。对于因违反此规定而产生的任何法律后果，用户需自行承担全部责任。该软件的所有资源信息均来源于网络。如有关于版权的争议或问题，请联系原作者或权利人。本声明者与版权问题无关且不承担任何相关责任。

# 使用场景
当你买了个杂牌手表想加好友时

# 最低支持Android版本
`Android 5.0`

# 开发进度
| 手表名称 | 跨手表加好友 | 跨手表聊天 |
| --- | --- | --- |
| 小天才 | 30% | ❌ |
| 米兔 | ❌ | ❌ |
| 奇虎360 | ❌ | ❌ |
| 华为手表 | ❌ | ❌ |
| 子腾 | ❌ | ❌ |

# 下载
自行编译或前往[Github Releases](https://github.com/csjdyr001/Telefriends/releases)下载已编译的版本

# 使用方法
在一台能自由安装apk的手表上安装`Telefriends`并登录账号即可使用

# 手动编译前准备
## 服务端准备
待补充

## 客户端准备
***此项目使用`AndroidIDE`开发,其他编译器不一定能正常使用***

创建`app/src/main/java/com/cfks/telefriends/ApiConfig.java`文件并写入
```java
package com.cfks.telefriends;

public class ApiConfig {
    public static String getLoginApi(){
        return "yourLoginApi";
    }

    public static String getUserInfoApi(){
        return "yourUserInfoApi";
    }
}
```

在项目根目录创建`local.properties`文件并写入(换成你自己的签名)
```
keystore.path=
keystore.password=
keystore.alias=
keystore.alias_password=
```

# 参与贡献
您可以前往[watch-apks分支]()提交提取到的各品牌手表apk文件,或推送PR提交您贡献的代码