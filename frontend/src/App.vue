<script setup>
const nav = ['首页概览', '患者管理', '预约挂号', '药品库存', '费用结算', '系统设置']
const stats = [
  { label: '今日挂号', value: '128', unit: '人次' },
  { label: '在院患者', value: '46', unit: '人' },
  { label: '药品品种', value: '312', unit: '种' },
  { label: '待结算', value: '17', unit: '笔' },
]
const envs = [
  ['前端框架', 'Vue 3 + Vite'],
  ['后端框架', 'Spring Boot 4.1.2'],
  ['持久层', 'MyBatis'],
  ['数据库', 'MySQL 8.0.13'],
  ['构建工具', 'Maven 3.9.11'],
  ['JDK', 'Azul Zulu 17'],
]
</script>

<template>
  <div class="layout">
    <header class="top">
      <div class="brand"><span class="logo">✚</span>医疗管理系统 <small>Medical System</small></div>
      <div class="tag">实验一 · 环境搭建 Demo</div>
    </header>
    <div class="main">
      <aside class="side">
        <div v-for="(item, i) in nav" :key="item" class="nav-item" :class="{ active: i === 0 }">{{ item }}</div>
      </aside>
      <section class="content">
        <h1>首页概览</h1>
        <p class="hint">欢迎使用医疗管理系统。本页为前端静态演示，数据为示例值，不连接数据库、不调用后端接口。</p>
        <div class="cards">
          <div class="card" v-for="s in stats" :key="s.label">
            <div class="label">{{ s.label }}</div>
            <div class="value">{{ s.value }}<span>{{ s.unit }}</span></div>
          </div>
        </div>
        <h2>系统环境</h2>
        <table>
          <tr v-for="e in envs" :key="e[0]"><td>{{ e[0] }}</td><td>{{ e[1] }}</td></tr>
        </table>
        <h2>公告</h2>
        <ul class="news">
          <li>已完成 JDK、Maven、MySQL、IDEA 环境搭建与验证</li>
          <li>Spring Boot 后端 /api/hello 接口本地访问正常</li>
          <li>前后端工程均已纳入 Git 版本管理</li>
        </ul>
      </section>
    </div>
    <footer>吉首大学 · 24 计科一班 · Java Web 实验一 · 前端页面 Demo</footer>
  </div>
</template>

<style scoped>
* { box-sizing: border-box; }
:global(body) { margin: 0; }
.layout { font-family: "Microsoft YaHei", system-ui, sans-serif; color: #1f2937; background: #f3f6fb; min-height: 100vh; display: flex; flex-direction: column; }
.top { height: 64px; background: #1565d8; color: #fff; display: flex; align-items: center; justify-content: space-between; padding: 0 28px; }
.brand { font-size: 20px; font-weight: 700; letter-spacing: 1px; }
.brand small { font-size: 12px; opacity: .75; margin-left: 8px; font-weight: 400; }
.logo { background: rgba(255,255,255,.2); border-radius: 8px; padding: 2px 8px; margin-right: 6px; }
.tag { font-size: 13px; background: rgba(255,255,255,.16); padding: 6px 14px; border-radius: 20px; }
.main { flex: 1; display: flex; }
.side { width: 200px; background: #fff; padding: 18px 12px; border-right: 1px solid #e5eaf2; }
.nav-item { padding: 12px 16px; border-radius: 8px; font-size: 14px; color: #4b5563; margin-bottom: 4px; }
.nav-item.active { background: #e8f1ff; color: #1565d8; font-weight: 600; }
.content { flex: 1; padding: 28px 36px; }
h1 { font-size: 24px; margin: 0 0 8px; }
h2 { font-size: 17px; margin: 28px 0 12px; }
.hint { color: #6b7280; font-size: 14px; margin: 0; }
.cards { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-top: 22px; }
.card { background: #fff; border-radius: 12px; padding: 20px; box-shadow: 0 2px 10px rgba(20,50,100,.06); border-top: 3px solid #1565d8; }
.label { color: #6b7280; font-size: 13px; }
.value { font-size: 30px; font-weight: 700; color: #1565d8; margin-top: 8px; }
.value span { font-size: 13px; margin-left: 6px; font-weight: 400; color: #9ca3af; }
table { width: 100%; background: #fff; border-collapse: collapse; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 10px rgba(20,50,100,.06); }
td { padding: 11px 16px; border-bottom: 1px solid #eef2f7; font-size: 14px; }
td:first-child { width: 180px; color: #6b7280; }
.news { background: #fff; border-radius: 10px; padding: 16px 16px 16px 36px; margin: 0; box-shadow: 0 2px 10px rgba(20,50,100,.06); }
.news li { padding: 6px 0; font-size: 14px; color: #4b5563; }
footer { text-align: center; padding: 16px; color: #9ca3af; font-size: 12px; }
</style>
