# 医疗系统 Hello World

Java Web 开发课程 · 实验一：开发环境搭建与医疗系统 Hello World；
实验二：医疗系统数据库设计与基础 CRUD（在实验一工程上继续开发）。

本项目完成 JDK / Maven / MySQL / IDEA 全套开发环境配置、Git 仓库与分支规范、
Spring Boot 项目创建、标准目录分层初始化，以及一个可本地访问的测试接口；
并补充 Node / Vue 3 + Vite 前端静态页面 Demo、MyBatis 与 MySQL 数据源依赖配置。
实验二进一步完成 5 张第三范式业务表、MyBatis-Plus 代码生成器与科室/医生/患者三套 CRUD 接口。

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
| 持久层（实验一） | `mybatis-spring-boot-starter` 4.1.0（Spring Boot 4 需用 4.x） |
| 持久层（实验二） | `mybatis-plus-spring-boot4-starter` 3.5.18 + `mybatis-plus-jsqlparser` 3.5.18 + `mybatis-plus-generator` 3.5.18 |
| 参数校验 | `spring-boot-starter-validation`（Jakarta Bean Validation） |
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

实验一引入 `mybatis-spring-boot-starter:4.1.0`（已自带 JDBC）与 `mysql-connector-j`；
实验二将其替换为 MyBatis-Plus，前缀也由 `mybatis.*` 改为 `mybatis-plus.*`：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/medical_system?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=${MYSQL_PASSWORD:}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
mybatis-plus.type-aliases-package=com.example.medicalsystem.entity
mybatis-plus.configuration.map-underscore-to-camel-case=true
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
mybatis-plus.global-config.db-config.id-type=auto
```

密码使用环境变量占位，不写入仓库；需要实际连接数据库时，启动前设置
`$env:MYSQL_PASSWORD="..."`（PowerShell）或在 IDEA Run Configuration 中配置环境变量。
实验一只做依赖与配置、不建表不写 Mapper；实验二已建表并接入 MyBatis-Plus，
控制台会打印带 `?` 占位符的 SQL，可直接作为「参数绑定防注入」的证据。

## 四、Git 规范

- `main`：主干分支，保持可运行状态；
- `dev`：开发分支，日常提交在此进行，完成后合并回主干；
- 提交注释采用约定式前缀：`init:` 初始化、`feat:` 新功能、`docs:` 文档、
  `fix:` 修复、`chore:` 构建与配置。

实验一完成前后端环境搭建、Vue 静态首页 Demo 与 MyBatis 依赖配置；
实验二完成 5 张业务表、MyBatis-Plus 代码生成器与三套基础 CRUD 接口；
药品明细拆表、前端联调等业务功能留待后续实验。

## 五、实验二：数据库设计与基础 CRUD

### 5.1 表结构（第三范式）

`sql/schema.sql` 可直接重复执行，含 5 张表与测试数据：

| 表 | 说明 | 关键约束 |
| --- | --- | --- |
| `department` | 科室 | 主键；`dept_code`、`dept_name` 唯一 |
| `doctor` | 医生 | 主键；工号 / 身份证 / 手机号唯一；`dept_id` 外键 → 科室 |
| `patient` | 患者 | 主键；就诊卡号 / 身份证 / 手机号唯一 |
| `registration` | 挂号记录 | 主键；`patient_id`、`doctor_id` 外键；挂号单号唯一 |
| `prescription` | 电子处方 | 主键；`registration_id` 外键；处方编号唯一 |

三范式要点：所有字段均为原子值；非主属性完全依赖主键；
挂号表只存 `patient_id` / `doctor_id`，不冗余患者姓名、医生姓名与科室名称
（科室由 `doctor.dept_id` 推导），消除传递依赖与更新异常。
药品明细属多值属性，严格建模应独立成第 6 张表，本实验按大纲要求保持 5 个实体。

### 5.2 目录增补

```text
src/main/java/com/example/medicalsystem/
├── common/         R 统一响应、GlobalExceptionHandler 中文异常提示
├── config/         MybatisPlusConfig 分页插件
├── controller/     Department/Doctor/PatientController（手写）
├── dto/            三个请求对象，@Pattern 校验手机号与身份证
├── entity/         5 张表实体（由生成器产出）
├── mapper/         BaseMapper 接口 + resources/mapper/*.xml
├── service/        IService 接口与实现（由生成器产出）
└── generator/      CodeGenerator（一次性工具，不参与运行时）
```

### 5.3 运行顺序

1. 在 Navicat / IDEA Database 执行 `sql/schema.sql`；
2. 设置环境变量 `MYSQL_PASSWORD`（本机 MySQL root 密码），不写入配置文件；
3. 运行 `generator.CodeGenerator.main()`，一键生成 5 张表的 entity / mapper / service；
4. `mvn clean compile`，启动 `MedicalSystemApplication`；
5. 按 `docs/api-test.md` 逐个验证接口。

> 版本坑：Spring Boot 4 必须用 `mybatis-plus-spring-boot4-starter`，
> 老教程的 `mybatis-plus-boot-starter` / `boot3-starter` 自动配置不加载；
> 3.5.18 起 `PaginationInnerInterceptor` 拆到 `mybatis-plus-jsqlparser`，
> `IService` / `ServiceImpl` 拆到 `mybatis-plus-spring` 包
> （`com.baomidou.mybatisplus.spring.service.*`），缺依赖或沿用旧包名会编译失败。

### 5.4 接口清单

三套接口路径为 `/api/departments`、`/api/doctors`、`/api/patients`：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `?current=1&size=5&name=...` | 多条件组合分页查询，返回 `records` + `total` |
| POST | `/` | 新增，`@Validated` 校验请求体 |
| PUT | `/{id}` | 修改 |
| DELETE | `/{id}` | 删除（外键引用时返回中文提示） |

统一返回 `{"code":200,"msg":"success","data":...}`；校验失败返回 400 与中文字段提示，
唯一键冲突返回 409。

### 5.5 参数校验与防注入

手机号 `^1[3-9]\d{9}$`、身份证 `^\d{17}[\dXx]$` 由 `jakarta.validation` 的
`@Pattern` 在 DTO 上声明，异常由全局处理器转成中文响应，
避免脏数据进入医疗档案、也避免英文堆栈外泄。

查询条件全部通过 `LambdaQueryWrapper` 动态拼装，底层使用 `#{}` 参数绑定，
等价于 `PreparedStatement` 的 `?` 占位符；项目未使用任何 `${}` 字符串拼接。
传入 `' OR 1=1 --` 只会被当作普通姓名值，返回空结果而非全表数据。

---

## 六、实验三：业务逻辑与事务处理

在实验二 5 张表基础上扩展，实现医生排班管理、在线挂号、开处方三类核心业务，
用声明式事务保证号源与库存扣减的原子性，并完善业务异常提示。

### 6.1 增量建表

执行 `sql/schema-exp3.sql`（在实验二 `schema.sql` 之后）：

| 新表 | 作用 |
| --- | --- |
| `doctor_schedule` | 医生排班，含总号源 / 剩余号源，是号源扣减的载体 |
| `medicine` | 药品，含库存数量，是库存扣减的载体 |
| `prescription_item` | 处方明细，拆出"处方—药品"多对多关系 |

`registration` 追加 `schedule_id` 外键与 `(patient_id, schedule_id)` 唯一键，
后者用于数据库层面兜底防重复挂号。

### 6.2 分层结构

```
controller/  只收参数、封装 R 响应
  └─ service/biz/   业务编排 + @Transactional 事务边界
       └─ service/   基础单表服务（IService）
            └─ mapper/    单表读写 + 带条件 UPDATE
```

业务逻辑集中在 `service/biz`，Controller 保持很薄，体现高内聚低耦合。

### 6.3 事务与防超卖

- `RegistrationBizService.register()`：查排班 → 防重复 → 扣号源 → 写挂号单，
  四步同处一个 `@Transactional`；
- `PrescriptionBizService.issue()`：写处方 → 逐项扣库存 → 写明细，任一步失败整体回滚；
- 扣号源 / 扣库存用**带条件 UPDATE**（`WHERE remaining > 0` / `stock >= 数量`），
  受影响行数为 0 即判定"号源已满 / 库存不足"，同时避免并发超卖。

### 6.4 三类业务异常

`BusinessException` 携带状态码，由 `GlobalExceptionHandler` 转成中文响应：
- 重复挂号 → 409 `重复挂号：该患者已预约过此排班`
- 号源已满 → 409 `号源已满，请选择其他排班`
- 库存不足 → 409 `库存不足：<药品名>，当前仅剩 N 件`

### 6.5 事务回滚演示

`RegisterReq` / `PrescriptionReq` 可带 `"simulateFail": true`：
业务层在扣减后故意抛异常，用来现场验证事务回滚——
号源 / 库存与已写数据都应恢复原状。测试脚本见 `docs/api-test-exp3.http`。

### 6.6 轻量前端联调

`frontend/src/App.vue` 在实验一静态页上加了"在线挂号"演示区，
用原生 `fetch` 调用 `/api/registration/book`，把后端 `code` / `msg` 友好展示出来；
`vite.config.js` 配 `/api` 代理到 8080 解决本地跨域（完整 CORS 配置留待实验五）。

启动顺序：先起后端 8080，再在 `frontend/` 执行 `npm run dev`，访问 Vite 提示的地址。
