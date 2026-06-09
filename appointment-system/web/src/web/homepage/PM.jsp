<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- 项目管理页面 - 纯静态界面 -->
<div>
    <div class="view-header">
        <h2>项目管理
            <button class="btn-primary">新增项目</button>
        </h2>
    </div>
    <div>
        <div class="project-item" v-for="pm in projectManagement" :key="pm.id">
            <div class="project-info">
                <strong>{{ pm.title }}</strong>
                <br>
                <small>{{ pm.time }} | 容量：{{ pm.num1 }} 剩余：{{ pm.num2 }}</small>
                <div>{{ pm.desc }}</div>
            </div>
            <button class="btn-delete-project">删除</button>
        </div>
    </div>
</div>