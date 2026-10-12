DCM(Declarative Configuration Management) Client
# 概述
本工具为开河平台下申明式配置初始化的支持工具之一。
计划提供以下功能：
1. 抽取配置申明
    1. 从发布包/lib目录中抽取各个模块的配置申明， 导出到指定目录
    2. 从在线模块抽取当前的实际运行时配置, 导出到指定目录
    3. 从在线模块获取当前发布版本的内置配置申明，导出到指定目录
    4. ~~枚举nacos下注册的各个模块提供的配置申明, 导出到指定目录~~ (目前暂未支持)
2. 获取配置申明的差异
    1. 基于指定目录下所有配置文件, 或抽取nacos下注册的各个服务提供的配置申明, 与当前模块的实际运行时配置比较, 生成差异文件
3. 应用配置申明
    1. 基于指定目录下所有配置文件, 或抽取nacos下注册的各个服务提供的配置申明, 与当前模块的实际运行时配置比较, 触发配置差异应用.


# 用法

dcm-cli [options] cmd  [command args]
## 参数选项
### 公共参数
* -k/ --use_auth 访问服务端点时是否使用ouaa认证信息, 默认False
* -f/ --file  指定一个单一的资源文件，用于作为diff/apply的本地资源
* -fp/--fpath 本地文件模式 从springboot fat jar 或展开的目录中中抽取配置
  * -fm/--fmode 指定jar包/目录样式, 目前有sbtfat/libpath/basedir三种选项， 默认basedir
* -s/--services   service1,service2 逗号分割的service(provider)列表, 内部会统一处理成为大写 (由于目前代码中的问题， 指定-s 参数时请一定不要将其作为命令前的最后一个参数， 否则命令及其参数将会被错误处理。)
* -a/--all_builtin_services   使用所有预知的服务（一个内嵌的provider list），但看上去不太建议使用
* -t/--target 指定一个目标服务的基地址，在没有其他设定的情况下，本工具会以该基地址拼接provider的全小写作为对应provider的 api访问端点基地址。
* -o/--out_path 输出目录
* -m/--map ServiceName 映射表文件路径; provider与service name映射表, 以# 开头的是注释行, 每一行均为 provider= serviceName 样式或provider="service url"样式。 当指定的是url时，将直接使用这个url作为provider的访问端点基地址。
* -c/--config 配置文件, 以key=value 形式指定前面的各个带参数选项, 遵循hocon 格式标准; 对于无参数选项, 通过指定key=True 标志开启选项, key使用命令行中长参数的首字母小写驼峰形式. 如out_path需要用outPath指定。

### 命令
* local_extract patterns 从spring boot fat jar 或展开的目录中抽取配置
* extract 从模块抽取配置申明
* rt_extract 从模块抽取运行时配置
* diff  抽取配置差异
* apply 应用配置差异

## 使用示例 
### 抽取配置申明

1. 抽取指定jar包中的dcm 静态资源<br>
```java -jar dcm-cli.jar -o output -fp app.jar -fm sbtfat  local_extract```
2. 抽取目标服务上的ouaa/dict/iot 静态配置<br>
```java -jar dcm-cli.jar  -s ouaa,iot -o output -t http://127.0.0.1:3200 -m service_map.map ex tract```

### 抽取运行时配置
1. 抽取目标服务上的ouaa/dict/iot 的系统配置和租户123的配置 <br>
```java -jar dcm-cli.jar  -s ouaa,iot -o output -t http://127.0.0.1:3200 -m service_map.map rt_extract system 123```

### 应用配置申明
1. 将本地的指定配置文件内容应用到目标系统中
```java -jar dcm-cli.jar  -f dcm-declaration-abc.yaml -t http://127.0.0.1:3200 -m service_map.map apply system```

2. 将本地指定配置文件内容中适用于租户123的部分应用到租户123
```java -jar dcm-cli.jar  -f dcm-declaration-abc.yaml -t http://127.0.0.1:3200 -m service_map.map apply 123```

3. 将本地jar包中包含的dcm配置资源中适用到租户123的部分应用到租户123
```java -jar dcm-cli.jar  -fp app.jar  -t http://127.0.0.1:3200 -m service_map.map apply 123```

### 使用贴示
1. 部分需要定义的参数，特别是在访问被授权保护的端点时需要使用的授权token获取相关的参数，需要通过修改spring boot应用的profile文件，或是通过命令行指定相关参数进行处理。这些内容示例如下：
```
ouaa:
  auth:
    url: http://localhost:4009/oauth/token
    clientId: xxx
    clientSecret: xxx
    tenant: xxx
    terminal: xxx
    refreshInterval: xxx
```
  * 目前代码中植入了当前默认会初始化到数据库中的一个信息，仅需要显式定义ouaa.auth.url. 在项目现场或实际使用中， 可以将目前代码中的 application-local.yaml复制一份到当前目录下，按项目现场实际状况进行修改后，运行时指定-Dspring.profiles.active=local即可。
2. 如果有较多的参数需要设置， 可将这些参数设置到一个配置文件中， 通过-c参数指定配置文件，减少重复输入。
3. 在使用了配置文件之后，命令行上相同目的的参数仍然可以覆盖配置文件的定义。
## 附录
### Hocoon格式
hocoon格式在处理简单的key/value 格式时，有以下主要要点：
1.  以“\#”字符开始到行末均为注释内容
2. key 与 value间可使用"=", 也可使用":"分隔。
3. 如果key/value 中包含了":"字符， value需要以半角英文双引号包括。

Hocoon支持其他更多高级的配置能力， 详见 [hocon 中文手册](https://github.com/ustc-zzzz/HOCON-CN-Translation/blob/master/HOCON.md)

### 主配置文件
主配置文件使用hocoon格式， 由命令行参数 -c 指定。但需要注意，如果同一个选项在主配置文件和命令行上都有指定，那么最终取值以命令行参数为准 。

### Service Map 格式说明
service map文件使用hocoon 格式， 本文件存放从provider 到对应dcm controller 端点基地址间的映射。
如果使用service map文件，需要通过命令行 -m 参数指定文件位置。

目前有两种逻辑：
一种是指定全http url.  所有http/https开始的字符串被视为全http url。
另一种是指定相对于命令行或主配置文件中指定的targetServerUrl的相对路径。不需要以‘/’开头。
当未指定provider作为key的选项时， 默认使用provider的小写形态作为其值。

示例：(已设置targetServerUrl = http://127.0.0.1:3200)

| **规则**                            | **对应dcm controller 端点基地址**      |
|-----------------------------------|---------------------------------|
| ouaa="http://127.0.0.1:4009/ouaa" | http://127.0.0.1:4009/ouaa      |
| ouaa=ouaa/ouaa                    | http://127.0.0.1:3200/ouaa/ouaa |
| ouaa （不显式配置）                      | http://127.0.0.1:3200/ouaa      |

### Auth Token相关配置
1. 命令行上-k/--use_auth， 或在-c指定的配置文件中添加useAuth=true启用
2. 通过定义java 系统属性ouaa.auth.url 指定到ouaa oauth/token 端点的url <br>例如： ```-Douaa.auth.url=http://127.0.0.1:3200/ouaa/oauth/token```
3. 与ouaa auth相关的属性集整体如下，除像前述ouaa.auth.url的指定一样通过命令行定义外， 还可以自行定义一个spring boot profile配置文件, 在启动时加以指定。（需要将该app profile添加到java -cp 参数指定的路径中, 或添加到当前工作目录下， 并通过-Dspring.profiles.active={profile_name} 予以指定）
``` yaml
ouaa:
  auth:
    url: http://localhost:4009/oauth/token
    clientId: client_2   # 以下信息请与运维或研发获取.目前办公室各个环境均初始化了以下可用信息.
    clientSecret: cli@pwd#ghy!789
    tenant: 0
    terminal: terminal
    refreshInterval: 600  # token更新间隔, 当前暂时没有太大意义
```
