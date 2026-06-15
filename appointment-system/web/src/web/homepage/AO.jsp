<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- 预约概览页面 - 纯静态界面 -->
<div>
    <div class="view-header">
        <h2>
            预约总览
            <button
                class="refresh-btn"
                @click="loadAppointmentOverview">
                刷新
            </button>
        </h2>
    </div>

    <table class="data-table">
        <thead>
            <tr>
                <th>预约码</th>
                <th>项目名称</th>
                <th>预约人</th>
                <th>联系方式</th>
                <th>预约时间</th>
                <th>预约状态</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="ap in appointmentForm" :key="ap.id">
                <td>{{ ap.code }}</td>
                <td>{{ ap.title }}</td>
                <td>{{ ap.name }}</td>
                <td>{{ ap.email }}</td>
                <td>{{ ap.time }}</td>
                <td>{{ ap.status }}</td>
            </tr>
        </tbody>
    </table>
</div>