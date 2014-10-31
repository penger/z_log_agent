<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
<log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">

<appender name="outfile" class="org.apache.log4j.DailyRollingFileAppender">
   	<param name="File" value="f:/opt/applog/msi_info.log" />
   	<param name="DatePattern" value="'.'yyyy-MM-dd" />
   	<layout class="org.apache.log4j.PatternLayout">
       	<param name="ConversionPattern" value="%d{ABSOLUTE}[%5p] %c{1}:%L - %m%n" />
   	</layout>
   	<filter class="org.apache.log4j.varia.LevelRangeFilter">
     	<param name="LevelMin" value="INFO" />
     	<param name="LevelMax" value="WARN" />    
  	</filter>
</appender>
<appender name="errfile" class="org.apache.log4j.DailyRollingFileAppender">
   	<param name="File" value="f:/opt/applog/msi_err.log" />
   	<param name="DatePattern" value="'.'yyyy-MM-dd" />
   	<layout class="org.apache.log4j.PatternLayout">
       	<param name="ConversionPattern" value="%d{ABSOLUTE}[%5p] %c{1}:%L - %m%n" />
   	</layout>
   	<filter class="org.apache.log4j.varia.LevelRangeFilter">
     	<param name="LevelMin" value="ERROR" />
     	<param name="LevelMax" value="FATAL" />    
  	</filter>
</appender>

<root> <!-- 设置接收所有输出的通道 -->
	<level value="debug" /><!-- 设置级别 -->
	<!-- 
	<appender-ref ref="stdout" />
	<appender-ref ref="stderr" />
	-->
	<appender-ref ref="outfile" />
	<appender-ref ref="errfile" />
	 
</root>
</log4j:configuration>
