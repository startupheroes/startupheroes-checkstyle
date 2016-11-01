# StartupHeroes Checkstyle Configuration

This project provides a default configuration for checkstyle at StartupHeroes projects.

To use it, configure your maven-checkstyle-plugin like so:

```
   <plugin>
     <artifactId>maven-checkstyle-plugin</artifactId>
     <version>2.17</version>
     <dependencies>
       <dependency>
         <groupId>es.startuphero.checkstyle</groupId>
         <artifactId>startupheroes-checkstyle-config</artifactId>
         <version>THEVERSIONYOUWANT</version>
       </dependency>
       <dependency>
         <groupId>com.puppycrawl.tools</groupId>
         <artifactId>checkstyle</artifactId>
         <version>7.1.2</version>
       </dependency>
     </dependencies>
     <configuration>
       <configLocation>startupheroes_checks.xml</configLocation>
       <!-- The things above this line are required, the rest is 'bonus' -->
       <!------------------------------------------------------------------>
       <consoleOutput>true</consoleOutput>
       <!-- Remove or switch to false to keep building even with checkstyle errors -->
       <failOnViolation>true</failOnViolation>
       <logViolationsToConsole>true</logViolationsToConsole>
       <!-- change to 'warn' to be more strict about following checkstyle conventions -->
       <violationSeverity>error</violationSeverity>
     </configuration>
     <executions>
       <execution>
         <id>validate</id>
         <phase>validate</phase>
         <goals>
           <goal>check</goal>
         </goals>
       </execution>
     </executions>
   </plugin>
```

See the [maven-checkstyle-plugin docs](https://maven.apache.org/plugins/maven-checkstyle-plugin/)
for more information about what the configuration settings mean.

Internally, we have the above configuration in the `<pluginManagement/>` section of a
company-wide parent pom, meaning that projects only need to specify the below in their
`<build><plugins>` section:

```
   <plugin>
      <artifactId>maven-checkstyle-plugin</artifactId>
   </plugin>
```

To run checkstyle plugin:

```
   mvn checkstyle:check
```
