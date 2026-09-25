# GuizhanLib

[English](/README.md) | **简体中文**

[![Maven Central](https://img.shields.io/maven-central/v/net.guizhanss/guizhanlib-all.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22net.guizhanss%22%20AND%20a:%22GuizhanLib%22)
![Java 21+](https://img.shields.io/badge/Java-21%2B-blue)
[![Javadoc](https://javadoc.io/badge2/net.guizhanss/guizhanlib-all/javadoc.svg)](https://javadoc.io/doc/net.guizhanss/guizhanlib-all)

一个帮助粘液科技/Pylon附属开发的 Java 库。

## 如何使用

### Gradle

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("net.guizhanss:guizhanlib-all:REPLACE WITH VERSION")
}
```

如果你使用 Shadow 插件，需要将 GuizhanLib 迁移到你的包中，避免与其他插件中使用的 GuizhanLib 冲突：

```kotlin
tasks.shadowJar {
    relocate("net.guizhanss.guizhanlib", "YOUR PACKAGE NAME HERE.guizhanlib")
}
```

### Maven

将`guizhanlib-all`（包含所有包）或者你需要使用的包添加为依赖项：

```
    <dependency>
        <groupId>net.guizhanss</groupId>
        <artifactId>guizhanlib-all</artifactId>
        <version>REPLACE WITH VERSION</version>
        <scope>compile</scope>
    </dependency>
```

在`build`中，你需要将 GuizhanLib 迁移到你的包中，避免与其他插件中使用的 GuizhanLib 冲突
（如果已存在 `maven-shade-plugin` 的配置，只需要添加 relocation 即可:

```
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.3.0</version>

                <configuration>
                    <!-- 你可以添加下面这一行，去除所有库中未使用的类，来减少生成jar的大小，非必须，但建议开启 -->
                    <minimizeJar>true</minimizeJar>
                    <relocations>
                        <!-- 重要: 你需要将以下relocation(迁移)部分添加到你的pom.xml中 -->
                        <relocation>
                            <pattern>net.guizhanss.guizhanlib</pattern>
                            <shadedPattern>(YOUR PACKAGE NAME HERE).guizhanlib</shadedPattern>
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

[Changelog](/CHANGELOG.md)
