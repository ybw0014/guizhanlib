# GuizhanLib

[![Maven Central](https://img.shields.io/maven-central/v/net.guizhanss/GuizhanLib.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22net.guizhanss%22%20AND%20a:%22GuizhanLib%22)
[![Jitpack 状态](https://jitpack.io/v/net.guizhanss/GuizhanLib.svg)](https://jitpack.io/#net.guizhanss/GuizhanLib)

一个可用于汉化插件的库。

## 内容

* 内置Minecraft简体中文语言文件，可实现获取任意语言文件中包含的内容
    * 该文件打包至jar中，打包后 70.4 KB
* Slimefun金属中文名
* 部分附属插件的金属中文名

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
        <groupId>net.guizhanss</groupId>
        <artifactId>GuizhanLib</artifactId>
        <version>将此处替换为版本号</version>
        <scope>compile</scope>
    </dependency>
```

在`build`中，你需要将GuizhanLib迁移到你的包中，避免与其他插件中使用的GuizhanLib冲突（如果已存在`maven-shade-plugin`的配置，只需要添加relocation即可:

```
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.2.4</version>

                <configuration>
                    <!-- 你可以添加下面这一行，去除所有库中未使用的类，来减少生成jar的大小，非必须，但建议开启 -->
                    <minimizeJar>true</minimizeJar>
                    <relocations>
                        <!-- 重要: 你需要将以下relocation(迁移)部分添加到你的pom.xml中 -->
                        <relocation>
                            <pattern>net.guizhanss.guizhanlib</pattern>
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

[点我查看](/CHANGELOG.md)
