# 医疗系统 Hello World

Java Web 开发课程 · 实验一：开发环境搭建与医疗系统 Hello World。

本项目完成 JDK / Maven / MySQL / IDEA 全套开发环境配置、Git 仓库与分支规范、
Spring Boot 项目创建、标准目录分层初始化，以及一个可本地访问的测试接口；
并补充 Node / Vue 3 + Vite 前端静态页面 Demo、MyBatis 与 MySQL 数据源依赖配置。

## 一、开发环境

| 组件 | 版本 / 说明 |
| --- | --- |
| JDK | Azul Zulu 17.0.14（Java 17） |
| Maven | 3.9.11，`conf/settings.xml` 配置阿里云 public 镜像 |
| MySQL | 8.0.13（MySQL80 服务运行中） |
| IDE | IntelliJ IDEA Ultimate 2026.2 |
| Spring Boot | 4.1.2，基础依赖 `spring-boot-starter-webmvc` |
| 项目编码 | UTF-8（Global / Project / properties 三处统一） |
| Node.js | 22.22.2（npm 10.9.7，registry 使用 npmmirror 镜像） |
| 前端框架 | Vue 3.5.44 + Vite 8.3.6，工程位于 `frontend/` |
| 持久层 | `mybatis-spring-boot-starter` 4.1.0（Spring Boot 4 需用 4.x） |
| MySQL 驱动 | `mysql-connector-j` 9.7.0（版本由 Boot 4.1.2 BOM 管理） |
| 版本控制 | Git，远程仓库 GitHub `koi-s297/medical-system` |

环境排障要点：JDK 8 无法运行 Spring Boot 3/4，改用 JDK 17 并在 IDEA 中切换 SDK；
Maven 中央仓库访问异常，改用阿里云 public 镜像；IDEA 中文乱码，通过三处 UTF-8
与 native2ascii 转换解决；MySQL 端口冲突用 `netstat -ano | findstr 3306` 定位占用进程，
再释放端口或修改 `my.ini` 中的 `port`；首次提交报 `Author identity unknown`，
用 `git config --global user.name/user.email` 配置提交者身份。

补充两点：Spring Boot 4.x 必须使用 `mybatis-spring-boot-starter` 4.x，
网上常见的 3.0.x 在 Boot 4 上自动配置不会加载；PowerShell 中执行
`mvn dependency:tree -Dincludes=...` 时逗号会被拆成多个参数，需给参数整体加引号。

## 二、标准目录分层

```text
src/main/java/com/example/medicalsystem/
├── MedicalSystemApplication.java   启动类（保持在根包，负责组件扫描）
├── controller/                     控制层：接收 HTTP 请求、返回 JSON
│   └── HelloController.java        GET /api/hello 测试接口
├── service/                        业务逻辑层接口
│   └── impl/                       业务逻辑层实现
├── dao/                            数据访问层接口（预留数据库接入）
├── entity/                         实体层（Patient 患者对象）
└── config/                         配置层（预留 Web 相关配置）
```

启动类位于根包，`@SpringBootApplication` 默认扫描该包及其子包，
因此所有分层包必须放在 `com.example.medicalsystem` 之下，
不能建到 `src/main/java` 顶层，否则 Controller 无法被扫描、接口返回 404。

前端工程与后端同仓库管理，位于 `frontend/`：

```text
frontend/
├── package.json          Vue 3 + Vite 依赖与 dev / build 脚本
├── index.html            页面入口（中文标题）
├── vite.config.js        Vite 配置
└── src/
    ├── main.js           创建 Vue 应用
    └── App.vue           医疗系统静态首页 Demo
```

## 三、启动与验证

1. 运行 `MedicalSystemApplication.main()`；
2. 控制台出现 `Tomcat started on port 8080` 即启动成功；
3. 浏览器访问 http://localhost:8080/api/hello 。

返回示例：

```json
{"code": 200, "msg": "医疗系统 Hello World", "system": "medical-system"}
```

中文正常显示，可同时验证 UTF-8 编码配置已生效。

### 前端页面 Demo

```bash
cd frontend
npm install
npm run dev
```

浏览器访问 http://localhost:5173/ ，可看到「医疗管理系统」首页：
蓝色顶栏、侧边导航、4 张统计卡片、系统环境表与公告。页面数据为静态示例值，
**不连接数据库、不调用后端接口、不渲染动态数据**（符合本实验要求）。

### MyBatis 与数据源配置

`pom.xml` 引入 `mybatis-spring-boot-starter:4.1.0`（已自带 JDBC，
无需另加 `spring-boot-starter-jdbc`）与 `mysql-connector-j`（版本由 Boot BOM 管理）。
`application.properties` 配置数据源与 MyBatis 基础项：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/medical_system?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=${MYSQL_PASSWORD:}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
mybatis.type-aliases-package=com.example.medicalsystem.entity
mybatis.configuration.map-underscore-to-camel-case=true
```

密码使用环境变量占位，不写入仓库；需要实际连接数据库时，启动前设置
`$env:MYSQL_PASSWORD="..."`（PowerShell）或在 IDEA Run Configuration 中配置环境变量。
本实验**只做依赖与配置，不建表、不写 Mapper、不执行查询**，后端接口行为保持不变。

## 四、Git 规范

- `main`：主干分支，保持可运行状态；
- `dev`：开发分支，日常提交在此进行，完成后合并回主干；
- 提交注释采用约定式前缀：`init:` 初始化、`feat:` 新功能、`docs:` 文档、
  `fix:` 修复、`chore:` 构建与配置。

实验一完成前后端环境搭建、Vue 静态首页 Demo 与 MyBatis 依赖配置；
患者管理等业务功能、建表与 Mapper 开发留待后续实验。`main` 与 `dev` 均已同步至最新提交。
