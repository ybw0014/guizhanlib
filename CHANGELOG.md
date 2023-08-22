# Changelog

## 1.3.3

- 修复自动更新通过alias查找仓库失败的问题

## 1.3.2

- 修复自动更新构建工件的路径错误

## 1.3.1

- 自动更新添加R2路径

## 1.3.0

- 自动更新对鬼斩构建站v2的兼容

## 1.2.2

- `AddonConfig`添加`getOrCreateSection`方法

## 1.2.1

- 自动更新添加新的构造函数

## 1.2.0

- 小型重构自动更新模块，允许添加更多配置项
- 增加了2个新的Slimefun机器方法

## 1.1.1

- 自动更新模块捕捉更多错误，应不再输出更多报错。

## 1.1.0

- 更新模块语言内置，不再从外部读取。
- 重写优化了大部分代码。
- 移除美西螈错误的旧方法。

## 1.0.4

- 修改美西螈相关方法。
- 语言助手添加属性翻译获取。

## 1.0.3

- Minecraft包新增美西螈。
- 自动更新捕捉更多错误。

## 1.0.2

- 使用旧版JsonParser，兼容旧版本MC。

## 1.0.1

- 修改了Minecraft模块的entity包，并新增青蛙。

## 1.0.0

- 多模块化重写

## 0.9.9

- 更新语言文件为MC1.19版本。

## 0.9.8

- 为`AbstractAddon`添加了可传递`Throwable`的`log`方法
- 不再调用`AbstractAddon#setupMetrics()`方法

## 0.9.7

- 修复`WikiSetup#setupItem(SlimefunItem, String)`错误调用静态方法的问题

## 0.9.6

- 添加`WikiSetup`类，用于设置物品Wiki页面（兼容旧版本粘液）

## 0.9.5

- `AbstractAddon`添加了`getSlimefunTickCount()`方法，获取已经过的粘液刻数

## 0.9.4

- 向`RandomUtil`添加了`randomDouble`获取随机小数

## 0.9.3

- 更新了`MinecraftTag`，现在包含更多的分类了

## 0.9.2

- 调整`AbstractAddon#setupMetrics()`调用顺序，现在将在启用插件后调用该方法

## 0.9.1

- 小重构更新模块`GuizhanBuildsUpdater`，支持修改显示语言
- 小重构`AbstractAddon`，支持修改自动更新显示语言（从配置文件读取）
- 将`Localization`的输出信息全部改为英文

## 0.9.0

- 重构了语言助手，正常方法使用不受影响。
    - 获取中文或键名的方法由`LanguageHelper#getLang`改名为`LanguageHelper#getLangOrKey`
    - 添加了获取键名或null的方法`LanguageHelper#getLangOrNull`
- 为`AbstractAddon`添加了获取插件指令的方法`getPluginCommand`
- 添加用于获取玩家头颅物品的工具包`PlayerHeadUtil`
- 添加了整数`Integer`助手 `IntegerHelper`
- 修正了部分javadoc

## 0.8.5

* `AbstractAddon`的`sendConsole`方法现在会带插件名称了

## 0.8.4

* `AbstractAddon`添加使用`ConsoleCommandSender`发送消息

## 0.8.3

* `AbstractAddon`类添加了logger助手
* 添加了背包工具包`InventoryUtil`
* 添加了随机工具包`RandomUtil`

## 0.8.2

* 为一些语言助手添加了字符串获取的方法

## 0.8.1

* 添加`MinecraftTag`，功能与`org.bukkit.Tag`类似

## 0.8.0

* 添加附属主类超类`AbstractAddon`
* 对自动更新模块进行一些小更改
* 添加`MenuBlock`
* 添加`Scheduler`
* 添加一些Javadoc

## 0.7.0

* 添加多语言支持(`Localization`)
* 自动更新现在无法找到仓库信息时的报错更改

## 0.6.1

* 修复自动更新无法读取自定义构建目录的问题
* 对自动更新做了一点小优化

## 0.6.0

* 自动更新模块适配构建站更新

## 0.5.4

* 为`DyeColorHelper`添加字符串获取中文的方法

## 0.5.3

* `JsonUtil`增加`parse(BufferedReader)`方法

## 0.5.2

* 修复`EntityTypeHelper`无法获取部分生物名称的问题

## 0.5.1

* 为`MaterialHelper`以及`EntityTypeHelper`添加字符串获取中文的方法

## 0.5.0

* 加入自动更新模块

## 0.4.0

**重大更新**

* 更改`groupId`为`net.guizhanss`
* 软件包由`net.guizhanss.minecraft.guizhanlib`改为`net.guizhanss.guizhanlib`
* 补充部分缺失的javadoc

## 0.3.3

* 增加药水类型助手`PotionEffectTypeHelper`
* 将生物群系助手`BiomeHelper`迁移

## 0.3.2

* 增加频率限制`RateLimit`
* 增加冷却时间`Cooldown`
* 将实体包迁移至助手包

## 0.3.1

* 增加Slimefun合金`AdvancedMetals`

## 0.3.0

* 加入Minecraft的语言文件
* 大量重构Minecraft原版相关方法
* 更换更新日志的格式

## 0.2.4

* `ChatColors`增加带颜色字符串获取

## 0.2.3

* 捕捉低版本下`EntityTypes`中可能出现的`NoSuchFieldError`

## 0.2.2

* 不再直接使用`EntityType`以适配低版本

## 0.2.1

* 新增 Slimefun 的基础金属 `BasicMetals`

## 0.2.0

* 新增 Minecraft 实体类型 `EntityTypes`

## 0.1.0

* 首个版本
