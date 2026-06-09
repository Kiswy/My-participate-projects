<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- 预约大厅页面 - 纯静态界面 -->
<div>
    <div class="view-header">
        <h2>预约大厅</h2>
    </div>
    <div v-for="category in serviceCategories" :key="category.id">
        <div class="section-title">{{ category.name }}</div>
        <div class="cards-grid">
            <div class="service-card" v-for="item in category.items" :key="item.id">
                <div class="card-info">
                    <h3>{{ item.title }}</h3>
                    <p>{{ item.desc }}</p>
                    <div class="meta">{{ item.time }}</div>
                    <div class="meta">{{ item.location }}</div>
                    <div class="badge">剩余 {{ item.remaining }}/{{ item.capacity }}</div>
                </div>
                <button class="cards-btn">立即预约</button>
            </div>
        </div>
    </div>
</div>