# GuizhanLib

![Jitpack 状态](https://jitpack.io/v/ybw0014/GuizhanLib.svg)

一个可用于汉化插件的库。

## 内容

* 所有实体类型的中文
* 部分实体属性(例如，热带鱼样式、村民职业等)的中文
* 

## 如何使用

你需要在`pom.xml`中添加Jitpack的仓库:

```
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
```

并将GuizhanLib添加为附属:

```
    <dependency>
        <groupId>com.github.ybw0014</groupId>
        <artifactId>GuizhanLib</artifactId>
        <version>将此处替换为版本号</version>
        <scope>compile</scope>
    </dependency>
```

在`build`中，你需要将GuizhanLib迁移到你的包中，避免与其他插件冲突:

```
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.2.4</version>

                <configuration>
                    <!-- 你可以添加下面这一行，去除所有库中未使用的类，来减少生成jar的大小，非必须 -->
                    <minimizeJar>true</minimizeJar>
                    <relocations>
                        <!-- 重要: 你需要将以下relocation(迁移)部分添加到你的pom.xml中 -->
                        <relocation>
                            <pattern>net.guizhanss.minecraft.guizhanlib</pattern>
                            <shadedPattern>将此处替换为你的软件包.guizhanlib</shadedPattern>
                        </relocation>
                    </relocations>

                    <filters>
                        <filter>
                            <artifact>*:*</artifact>
                            <excludes>
                                <exclude>META-INF/*</exclude>
                            </excludes>
                        </filter>
                    </filters>
                </configuration>

                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
```

## 更新日志

### 0.3.0

* 加入Minecraft的语言文件
* 更换更新日志的格式

### 0.2.4

* `ChatColors`增加带颜色字符串获取

### 0.2.3

* 捕捉低版本下`EntityTypes`中可能出现的`NoSuchFieldError`

### 0.2.2

* 不再直接使用`EntityType`以适配低版本

### 0.2.1

* 新增 Slimefun 的基础金属 `BasicMetals`

### 0.2.0 

* 新增 Minecraft 实体类型 `EntityTypes`

### 0.1.0

* 首个版本
