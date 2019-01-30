## Bee版本说明
 * 1、beeJava
    * <依赖修改>如下所示：
    ```
    <dependency>
		<groupId>com.qdcz</groupId>
		<artifactId>common-service</artifactId>
		<version>1.0-SNAPSHOT</version>
	</dependency>
    ```
        * 这种实现，还特意加上了日志依赖，和日志配置文件，system文件夹中的日志有日志数据，
        * 但是另外一个日志文件没数据，导致页面上面没数据，这个需要继续跟进一下。