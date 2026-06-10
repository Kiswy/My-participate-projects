<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>首页-多功能预约系统</title>
    <link rel="stylesheet" href="css/homepage_Max.css">
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
</head>
<body>
    <h3 class="title">
        <span>
            欢迎您：${loginUser.username}
        </span>

        <a href="/logout" class="title-a">
          退出登录
        </a>
    </h3>

    <div id="app">
        <div class="app-container">
            <!-- 头部导航 -->
            <header class="main-header">
                <div class="logo-area">
                    <h1>多功能预约系统</h1>
                </div>
                <div class="role-switch">
                    <button class="role-btn active" type="button">学生/教职工端</button>
                </div>
            </header>

            <!-- 学生/教职工端内容 -->
            <div id="userModeContent" class="dashboard">
                <div class="user-tab-nav">
                    <button class="tab-btn" type="button" :class="{ active: userTab === 'hall' }" @click="userTab = 'hall'">预约大厅</button>
                    <button class="tab-btn" type="button" :class="{ active: userTab === 'my' }" @click="userTab = 'my'">我的预约</button>
                </div>

                <!-- 预约大厅页面 -->
                <div id="hallPanel" :style="{ display: userTab === 'hall' ? 'block' : 'none' }">
                    <jsp:include page="homepage/bookingHall.jsp"/>
                </div>

                <!-- 我的预约页面 -->
                <div id="myPanel" :style="{ display: userTab === 'my' ? 'block' : 'none' }">
                    <jsp:include page="homepage/myAppointment.jsp"/>
                </div>
            </div>
        </div>
    </div>

    <script>
        // 创建 Vue 应用
        const { createApp, ref } = Vue;

        createApp({
            setup() {
                // 普通用户端仅需要预约大厅和我的预约两个页签
                const userTab = ref('hall');

                // 预约大厅数据
                const serviceCategories = ref([
                    {
                        id: 1,
                        name: '图书馆',
                        items: [
                            { id: 1, title: '图书馆·自习室', desc: '安静学习空间', time: '4/10 08:00-22:00', location: '图书馆二层', remaining: 12, capacity: 30 },
                            { id: 2, title: '图书馆·研讨室', desc: '小组讨论专用', time: '4/10 10:00-22:00', location: '图书馆三层', remaining: 3, capacity: 8 }
                            ]
                        },
                    {
                        id: 2,
                        name: '讲座',
                        items: [
                            { id: 3, title: 'AI前沿讲座：大模型时代', desc: '张三教授主讲', time: '4/11 14:00-16:00', location: '图书馆三层报告厅', remaining: 45, capacity: 100 },
                            { id: 4, title: '心理健康公开课', desc: '减压与情绪管理', time: '4/12 15:30-17:00', location: '学生活动中心小礼堂', remaining: 60, capacity: 80 }
                        ]
                    }
                ]);

                // 我的预约数据
                const reservationCards = ref([
                    { id: 1, title: '图书馆·自习室', code: 'ZXS12LH', name: '李华' },
                    { id: 2, title: 'AI前沿讲座：大模型时代', code: 'JZ123LH', name: '李华' }
                ]);

                return {
                    userTab,
                    serviceCategories,
                    reservationCards
                };
            }
        }).mount('#app');
    </script>
</body>
</html>
