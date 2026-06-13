<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- 我的预约页面 - 纯静态界面 -->
<div>
    <div class="view-header">
        <h2>我的预约</h2>
    </div>
    <div class="reservations-list">
        <div class="reservation-item" v-for="rc in reservationCards" :key="rc.id">
            <div class="card-info">
                <strong>{{ rc.title }}</strong>
                <br>
                <small>预约码：{{ rc.code }} | {{ rc.name }}</small>
            </div>
            <button
                class="btn-cancel"
                @click="cancelReservation(rc.id)">
                取消预约
            </button>
        </div>
    </div>
</div>