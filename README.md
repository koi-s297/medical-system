# 医疗系统 Hello World

Java Web 开发课程 · 实验一：开发环境搭建与医疗系统 Hello World。

本项目完成 JDK / Maven / MySQL / IDEA 全套开发环境配置、Git 仓库与分支规范、
Spring Boot 项目创建、标准目录分层初始化，以及一个可本地访问的测试接口。

## 一、开发环境

| 组件 | 版本 / 说明 |
| --- | --- |
| JDK | Azul Zulu 17.0.14（Java 17） |
| Maven | 3.9.11，`conf/settings.xml` 配置阿里云 public 镜像 |
| MySQL | 8.0.13（MySQL80 服务运行中） |
| IDE | IntelliJ IDEA Ultimate 2026.2 |
| Spring Boot | 4.1.2，基础依赖 `spring-boot-starter-webmvc` |
| 项目编码 | UTF-8（Global / Project / properties 三处统一） |

环境排障要点：JDK 8 无法运行 Spring Boot 3/4，改用 JDK 17 并在 IDEA 中切换 SDK；
Maven 中央仓库访问异常，改用阿里云 public 镜像；IDEA 中文乱码，通过三处 UTF-8
与 native2ascii 转换解决；MySQL 端口冲突用 `netstat -ano | findstr 3306` 定位占用进程，
再释放端口或修改 `my.ini` 中的 `port`；首次提交报 `Author identity unknown`，
用 `git config --global user.name/user.email` 配置提交者身份。

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

## 三、启动与验证

1. 运行 `MedicalSystemApplication.main()`；
2. 控制台出现 `Tomcat started on port 8080` 即启动成功；
3. 浏览器访问 http://localhost:8080/api/hello 。

返回示例：

```json
{"code": 200, "msg": "医疗系统 Hello World", "system": "medical-system"}
```

中文正常显示，可同时验证 UTF-8 编码配置已生效。

## 四、Git 规范

- `main`：主干分支，保持可运行状态；
- `dev`：开发分支，日常提交在此进行，完成后合并回主干；
- 提交注释采用约定式前缀：`init:` 初始化、`feat:` 新功能、`docs:` 文档、
  `fix:` 修复、`chore:` 构建与配置。

实验一仅完成工程初始化，患者管理、数据库接入等业务功能留待后续实验扩展。
