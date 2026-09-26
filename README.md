# GuizhanLib

**English** | [简体中文](/README.zh_cn.md)

[![Maven Central](https://img.shields.io/maven-central/v/net.guizhanss/guizhanlib-all.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22net.guizhanss%22%20AND%20a:%22GuizhanLib%22)
![Java 21+](https://img.shields.io/badge/Java-21%2B-blue)
[![Javadoc](https://javadoc.io/badge2/net.guizhanss/guizhanlib-all/javadoc.svg)](https://javadoc.io/doc/net.guizhanss/guizhanlib-all)

A Java library that helps developing Slimefun/Pylon addons.

## Usage

### Gradle

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("net.guizhanss:guizhanlib-all:REPLACE WITH VERSION")
}
```

If you use the Shadow plugin, relocate the library to your own package to avoid conflicts with GuizhanLib bundled in other plugins:

```kotlin
tasks.shadowJar {
    relocate("net.guizhanss.guizhanlib", "YOUR PACKAGE NAME HERE.guizhanlib")
}
```

### Maven

Add `guizhanlib-all` (which includes all sub modules) or **the modules you need** as dependency:

```
    <dependency>
        <groupId>net.guizhanss</groupId>
        <artifactId>guizhanlib-all</artifactId>
        <version>REPLACE WITH VERSION</version>
        <scope>compile</scope>
    </dependency>
```

You will need to relocate the library classes if you use it for addon development.

```
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.3.0</version>

                <configuration>
                    <!-- Add the following field to remove all unused classes and reduce the size of generated jar file. Not required, but recommended  -->
                    <minimizeJar>true</minimizeJar>
                    <relocations>
                        <!-- IMPORTANT: add the following relocation -->
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

## Changelog

[Changelog](/CHANGELOG.md)

## Library loading

If your addon needs runtime libraries, use `BukkitLibraryManager` from the `guizhanlib-libraries` module. Repositories are tried in insertion order, so add a mirror before Maven Central for faster downloads:

```java
var manager = new BukkitLibraryManager(this);
// Optional: mirror, tried before Maven Central
manager.addRepository("https://maven.aliyun.com/repository/public/");
manager.addMavenCentral();

manager.loadLibrary(Library.builder()
        .groupId("com.google.code.gson")
        .artifactId("gson")
        .version("2.10.1")
        .build());
```
