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

// ---- 实验三：在线挂号 + 友好提示（原生 fetch，不引入额外库） ----
import { ref } from 'vue'
const form = ref({ patientId: '', scheduleId: '', symptom: '' })
const result = ref(null)   // { ok, msg }

async function submitRegister() {
  result.value = null
  try {
    const resp = await fetch('/api/registration/book', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        patientId: Number(form.value.patientId),
        scheduleId: Number(form.value.scheduleId),
        symptom: form.value.symptom,
      }),
    })
    const data = await resp.json()
    // 后端统一返回 code/msg：200 成功，其余把中文 msg 显示出来
    result.value = { ok: data.code === 200, msg: data.code === 200
      ? '挂号成功，单号已生成（编号 ' + data.data + '）'
      : data.msg }
  } catch (e) {
    result.value = { ok: false, msg: '无法连接后端服务，请确认 8080 已启动' }
  }
}
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

        <!-- 实验三：在线挂号演示，重点展示后端的友好中文提示 -->
        <h2>在线挂号（实验三 · 事务与异常演示）</h2>
        <div class="reg-box">
          <div class="reg-row">
            <label>患者编号</label>
            <input v-model="form.patientId" placeholder="如 1" />
            <label>排班编号</label>
            <input v-model="form.scheduleId" placeholder="如 1" />
          </div>
          <div class="reg-row">
            <label>主诉</label>
            <input v-model="form.symptom" placeholder="如 咳嗽两天" />
            <button @click="submitRegister">提交挂号</button>
          </div>
          <p v-if="result" class="reg-msg" :class="result.ok ? 'ok' : 'err'">{{ result.msg }}</p>
          <p class="reg-tip">提示：重复挂号、号源已满时，这里会显示后端返回的中文原因，而不是报错堆栈。</p>
        </div>

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
.reg-box { background: #fff; border-radius: 10px; padding: 20px; box-shadow: 0 2px 10px rgba(20,50,100,.06); }
.reg-row { display: flex; align-items: center; gap: 10px; margin-bottom: 12px; }
.reg-row label { width: 72px; font-size: 14px; color: #6b7280; }
.reg-row input { flex: 1; padding: 8px 10px; border: 1px solid #d7dfeb; border-radius: 6px; font-size: 14px; }
.reg-row input:first-of-type { max-width: 130px; }
.reg-row button { background: #1565d8; color: #fff; border: none; padding: 9px 18px; border-radius: 6px; font-size: 14px; cursor: pointer; }
.reg-msg { padding: 10px 14px; border-radius: 6px; font-size: 14px; margin: 8px 0; }
.reg-msg.ok { background: #e7f7ee; color: #15803d; }
.reg-msg.err { background: #fdeaea; color: #c0392b; }
.reg-tip { font-size: 12px; color: #9ca3af; margin: 4px 0 0; }
footer { text-align: center; padding: 16px; color: #9ca3af; font-size: 12px; }
</style>
