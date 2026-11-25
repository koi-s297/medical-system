# 实验二 · 接口自测与截图清单

> 配套文件：`api-test.http`（IDEA 打开后点绿色三角逐条执行，无需安装 Postman）。
> 应用地址 `http://localhost:8080`，统一返回 `code / msg / data`。

## 一、跑起来的前置条件

1. 在 Navicat 或 IDEA Database 执行 `sql/schema.sql`（会重建 5 张表并插入测试数据，**会覆盖已有数据**）。
2. 设置 MySQL root 密码环境变量（**不要写进 `application.properties`**）：
   - PowerShell：`$env:MYSQL_PASSWORD="你的密码"`
   - Git Bash：`export MYSQL_PASSWORD='你的密码'`
   - IDEA：`Run → Edit Configurations → Environment variables` 填 `MYSQL_PASSWORD=你的密码`
3. 运行 `generator.CodeGenerator.main()` 生成持久层代码（同样读取该环境变量）。
4. `mvn clean compile` → 启动 `MedicalSystemApplication`。

## 二、三套接口

| 路径 | 分页查询可用条件 | 增 / 改 / 删 |
| --- | --- | --- |
| `/api/departments` | `deptName`(模糊)、`deptCode`、`current`、`size` | POST / PUT /{id} / DELETE /{id} |
| `/api/doctors` | `name`、`phone`、`idCard`、`deptId`、`title`、`current`、`size` | 同上 |
| `/api/patients` | `name`、`patientNo`、`phone`、`idCard`、`gender`、`current`、`size` | 同上 |

## 三、必须验证的 4 类结果

| 类别 | 用例 | 预期 |
| --- | --- | --- |
| 多条件组合分页 | `?current=1&size=5&name=丽&gender=2` | `total=2`，`records` 只含黄丽、孙丽；`pages`、`total` 字段齐全 |
| 增 / 改 / 删 | api-test.http 的 05 / 06 / 07 | 依次返回 `code:200` 与新 id、`true`、`true` |
| 参数校验 | 手机号 `abc`、身份证 `123` | `code:400`，`msg` 为中文，含字段名与具体原因 |
| 防 SQL 注入 | `?name=' OR 1=1 --` | `total=0`，控制台 SQL 为 `WHERE (name LIKE ?) LIMIT ?`，参数以 `?` 绑定 |

## 四、Git Bash 下的等价 curl 命令

```bash
# 分页
curl "http://localhost:8080/api/patients?current=1&size=5&name=%E4%B8%BD&gender=2"
# 注入测试
curl "http://localhost:8080/api/patients?name=%27%20OR%201%3D1%20--"
# 新增
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{"patientNo":"P0013","name":"测试患者","idCard":"430101199001010013","phone":"13900000013","gender":1,"address":"吉首市乾州街道 13 号"}'
```

PowerShell 里 `curl` 是 `Invoke-WebRequest` 的别名，请改用 `curl.exe` 或直接用 `api-test.http`。

## 五、截图点（按实验二报告顺序编号）

| 编号 | 截什么 | 画面里必须有 |
| --- | --- | --- |
| ★ 01 | `schema.sql` 执行结果 | 5 张表创建成功、无报错；或 Navicat 表列表 5 张表 |
| ★ 02 | 表结构 / ER 关系 | `SHOW CREATE TABLE patient` 或 Navicat 设计表，能看到 PK / UK / FK |
| ★ 03 | 代码生成器 | `CodeGenerator.java` 配置 + 运行日志 `DONE` + 生成的 5 组文件树 |
| ★ 04 | pom 依赖树 | `mybatis-plus-spring-boot4-starter:3.5.18`、`mybatis-plus-jsqlparser:3.5.18`、`generator:3.5.18` |
| ★ 05 | 启动日志 | `Tomcat started on 8080` + 带 `?` 占位符的 SQL 日志 |
| ★ 06 | 多条件组合分页 | 请求 URL 与响应同屏：`records`、`total=2`、`pages`、`size` |
| ★ 07 | 增 / 改 / 删 | 三条请求与各自的 `code:200` 响应（可拼一屏） |
| ★ 08 | 参数校验失败 | 非法手机号 / 身份证 → `code:400` + 中文提示 |
| ★ 09 | 防注入测试 | 注入串请求 + `total:0` 响应；最好带上控制台 `LIKE ?` 日志 |
| 10 | 报告用设计图 | `E:\workplace\实验二图表\ER图.png`、`时序图-多条件分页查询.png`（已生成，无需截屏） |

> 截图统一存到 `E:\workplace\_code_crops\`，按 `实验二-01-建表.png` 这种命名，避免和实验一的 01~12 混在一起。
