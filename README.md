**Telegram rss bot written in java**

### 使用

1. clone项目   
`git clone https://github.com/anicmv/rss_bot.git`
2. 修改根目录下的`./src/main/resources/application.yml`文件配置
3. 构建并运行 Docker 容器:   
`docker-compose up --build -d`

**注意:**
需要本地调试程序时:
application.yml中kafka配置改为:
```
kafka:
    bootstrap-servers: localhost:9092
```

需要一键部署时候:
application.yml中kafka配置改为:
```
kafka:
    bootstrap-servers: kafka:29092
```
