# Telefriends
一款手表跨品牌加好友软件

## 使用场景
当你买了个杂牌手表却发现加不了好友时…

# 最低支持Android版本
`Android 5.0`

# 编译前准备
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