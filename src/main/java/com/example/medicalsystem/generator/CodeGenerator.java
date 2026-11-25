package com.example.medicalsystem.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * MyBatis-Plus 代码生成器（实验二）。
 *
 * <p>运行前提：已执行 sql/schema.sql 建表。密码不写入代码与仓库，按以下优先级读取：</p>
 * <ol>
 *   <li>JVM 参数 -Dmysql.password=xxx（IDEA 的 VM options）</li>
 *   <li>环境变量 MYSQL_PASSWORD（IDEA Run Configuration 的 Environment variables）</li>
 *   <li>本地文件 sql/.db-password（第一行写密码，已被 .gitignore 忽略）</li>
 *   <li>以上都没有时，在控制台交互式输入一次，仅用于本次运行</li>
 * </ol>
 *
 * <p>一次运行为 5 张表生成 entity / mapper / mapper.xml / service / service.impl
 * 全套持久层代码；Controller 由本人编写，故禁用 controller 模板。</p>
 */
public class CodeGenerator {

    private static final String JDBC_URL =
            "jdbc:mysql://localhost:3306/medical_system?useUnicode=true&characterEncoding=utf8"
                    + "&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        String password = resolvePassword(projectPath);

        System.out.println("[CodeGenerator] project  = " + projectPath);
        System.out.println("[CodeGenerator] database = medical_system @ localhost:3306");

        try {
            FastAutoGenerator.create(JDBC_URL, USERNAME, password)
                    .globalConfig(builder -> builder
                            .author("koi")
                            .outputDir(projectPath + "/src/main/java")
                            .disableOpenDir()
                            .commentDate("yyyy-MM-dd"))
                    .packageConfig(builder -> builder
                            .parent("com.example.medicalsystem")
                            .entity("entity")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("service.impl")
                            .pathInfo(outputPaths(projectPath)))
                    .strategyConfig(builder -> {
                        builder.addInclude("department", "doctor", "patient",
                                "registration", "prescription");
                        builder.entityBuilder()
                                .enableLombok()
                                .enableFileOverride()
                                .idType(IdType.AUTO)
                                .naming(NamingStrategy.underline_to_camel)
                                .columnNaming(NamingStrategy.underline_to_camel)
                                .enableTableFieldAnnotation();
                        builder.mapperBuilder()
                                .enableFileOverride()
                                .enableBaseResultMap()
                                .enableBaseColumnList()
                                .formatMapperFileName("%sMapper")
                                .formatXmlFileName("%sMapper");
                        builder.serviceBuilder()
                                .enableFileOverride()
                                .formatServiceFileName("%sService")
                                .formatServiceImplFileName("%sServiceImpl");
                    })
                    // Controller 自行编写，避免生成器覆盖业务接口
                    .templateConfig(builder -> builder.disable(TemplateType.CONTROLLER))
                    .templateEngine(new FreemarkerTemplateEngine())
                    .execute();

            System.out.println("[CodeGenerator] DONE -> entity / mapper / mapper.xml / service / service.impl");
        } catch (Exception ex) {
            printDiagnosis(ex);
            System.exit(1);
        }
    }

    /**
     * mapper.xml 输出到 resources/mapper；controller 即使被模板意外生成，
     * 也只落到 target 目录，绝不覆盖手写的业务接口。
     */
    private static Map<OutputFile, String> outputPaths(String projectPath) {
        Map<OutputFile, String> paths = new HashMap<>();
        paths.put(OutputFile.xml, projectPath + "/src/main/resources/mapper");
        paths.put(OutputFile.controller, projectPath + "/target/generator-unused");
        return paths;
    }

    /** 依次尝试四种密码来源，日志只打印来源、绝不打印密码本身。 */
    private static String resolvePassword(String projectPath) {
        String pwd = System.getProperty("mysql.password");
        if (hasText(pwd)) {
            System.out.println("[CodeGenerator] password source = VM option -Dmysql.password");
            return pwd;
        }

        pwd = System.getenv("MYSQL_PASSWORD");
        if (hasText(pwd)) {
            System.out.println("[CodeGenerator] password source = env MYSQL_PASSWORD");
            return pwd;
        }

        Path local = Paths.get(projectPath, "sql", ".db-password");
        try {
            if (Files.isRegularFile(local)) {
                String text = Files.readString(local).trim();
                if (hasText(text)) {
                    System.out.println("[CodeGenerator] password source = sql/.db-password (local, gitignored)");
                    return text;
                }
            }
        } catch (Exception ignored) {
            // 读不到就继续走交互式
        }

        System.out.println("[CodeGenerator] 未检测到数据库密码，请在下方控制台输入 MySQL root 密码：");
        System.out.println("               （只用于本次运行，不写入代码、配置或 Git 仓库）");
        System.out.print("password> ");
        try {
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextLine()) {
                String text = scanner.nextLine().trim();
                if (hasText(text)) {
                    System.out.println("[CodeGenerator] password source = console input");
                    return text;
                }
            }
        } catch (Exception ignored) {
            // 非交互环境直接落到下面的中文提示
        }

        throw new IllegalStateException(
                "未读取到 MySQL 密码，生成器无法连接数据库。请任选一种方式后重新运行：\n"
                        + "  1) IDEA 右键 CodeGenerator → More Run/Debug → Modify Run Configuration\n"
                        + "     → Environment variables 填 MYSQL_PASSWORD=你的密码\n"
                        + "     （注意：必须加在 CodeGenerator 这个运行配置上，\n"
                        + "       MedicalSystemApplication 的配置不会共享）\n"
                        + "  2) 同一配置里 VM options 填 -Dmysql.password=你的密码\n"
                        + "  3) 在项目 sql/ 目录建 .db-password 文件，第一行写密码（已被 .gitignore 忽略）\n"
                        + "  4) 重新运行，在控制台出现 password> 时直接输入密码并回车");
    }

    private static boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }

    /** 把常见的连接异常翻成中文定位提示，避免一屏英文堆栈。 */
    private static void printDiagnosis(Exception ex) {
        Throwable root = ex;
        while (root.getCause() != null) {
            root = root.getCause();
        }
        String msg = root.getMessage() == null ? "" : root.getMessage();
        System.err.println("[CodeGenerator] FAILED: " + msg);
        if (msg.contains("Access denied")) {
            if (msg.contains("using password: NO")) {
                System.err.println("  → 数据库没有收到密码（当前密码为空）。请按上面的方式重新提供密码。");
            } else {
                System.err.println("  → 密码不正确（using password: YES）。检查 root 密码，或改用 Navicat 里能连上的那个账号。");
            }
            System.err.println("  → 若环境变量已填仍显示 NO，说明它加在了别的 Run Configuration 上。");
        } else if (msg.contains("Unknown database")) {
            System.err.println("  → 库 medical_system 不存在。先在 Navicat / IDEA Database 执行 sql/schema.sql。");
        } else if (msg.contains("No suitable driver")) {
            System.err.println("  → classpath 里没有 MySQL 驱动。pom.xml 的 mysql-connector-j 不能设为 runtime scope，");
            System.err.println("     或在运行配置里改用 runtime classpath。");
        } else if (msg.contains("Communications link failure") || msg.contains("Connection refused")) {
            System.err.println("  → 连不上 3306。检查 MySQL80 服务是否 Running。");
        } else if (msg.contains("No tables") || (msg.contains("Table") && msg.contains("doesn't exist"))) {
            System.err.println("  → 表还没建好。先执行 sql/schema.sql 再跑生成器。");
        }
    }
}
