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
                    <button class="role-btn" :class="{ active: currentRole === 'user' }" @click="currentRole = 'user'">学生/教职工端</button>
                    <button class="role-btn" :class="{ active: currentRole === 'admin' }" @click="currentRole = 'admin'">后台管理端</button>
                </div>
            </header>

            <!-- 学生/教职工端内容 -->
            <div id="userModeContent" class="dashboard" :style="{ display: currentRole === 'user' ? 'block' : 'none' }">
                <div class="user-tab-nav">
                    <button class="tab-btn" :class="{ active: userTab === 'hall' }" @click="userTab = 'hall'">预约大厅</button>
                    <button class="tab-btn" :class="{ active: userTab === 'my' }" @click="userTab = 'my'">我的预约</button>
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

            <!-- 后台管理端内容 -->
            <div id="adminModeContent" class="dashboard" :style="{ display: currentRole === 'admin' ? 'block' : 'none' }">
                <div class="admin-tab-nav">
                    <button class="tab-btn" :class="{ active: adminTab === 'ao' }" @click="adminTab = 'ao'">预约概览</button>
                    <button class="tab-btn" :class="{ active: adminTab === 'pm' }" @click="adminTab = 'pm'">项目管理</button>
                </div>

                <!-- 预约概览页面 -->
                <div id="aoPanel" :style="{ display: adminTab === 'ao' ? 'block' : 'none' }">
                    <jsp:include page="homepage/AO.jsp"/>
                </div>

                <!-- 项目管理页面 -->
                <div id="pmPanel" :style="{ display: adminTab === 'pm' ? 'block' : 'none' }">
                    <jsp:include page="homepage/PM.jsp"/>
                </div>
            </div>
        </div>
    </div>

    <script>
        // 创建 Vue 应用
        const { createApp, ref, onMounted } = Vue;

        createApp({
            setup() {
                // 仅保留界面切换状态，无任何业务数据
                const currentRole = ref('user');
                const userTab = ref('hall');
                const adminTab = ref('ao');

                // 预约大厅数据
                const serviceCategories = ref([]);

                // 我的预约数据
                const reservationCards = ref([]);

                onMounted(async () => {
                    try {
                        // ====================
                        // 加载预约大厅
                        // ====================
                        const categoryResponse = await fetch('/categories');
                        const categories = await categoryResponse.json();
                        const result = [];

                        for (const category of categories) {
                            const projectResponse =
                                await fetch(
                                    '/projects?categoryId='
                                    + category.id
                                );
                            const projects = await projectResponse.json();

                            result.push({
                                id: category.id,
                                name: category.categoryName,
                                items: projects.map(project => ({
                                    id: project.id,
                                    title: project.projectName,
                                    desc: project.description,
                                    time: project.appointmentTime,
                                    location: project.location,
                                    remaining: project.remainingCount,
                                    capacity: project.capacity
                                }))
                            });
                        }

                        serviceCategories.value = result;

                        // ====================
                        // 加载我的预约
                        // ====================
                        const reservationResponse = await fetch('/reservation');
                        const reservations = await reservationResponse.json();

                        reservationCards.value =
                            reservations.map(r => ({
                                id: r.id,
                                title: r.projectName,
                                code: r.reservationCode,
                                name: r.status
                            })
                        );

                    } catch (e) {
                        console.error('加载预约大厅失败', e);
                    }
                });

                async function reserveProject(item) {
                    try {
                        const response = await fetch('/reservation', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            },
                            body: 'projectId=' + item.id
                        });

                        const message = await response.text();

                        alert(message);

                    } catch (e) {
                        console.error('预约失败', e);
                        alert('预约失败，请稍后重试');
                    }
                }

                // 取消预约
                async function cancelReservation( reservationId ) {
                    try {
                        const response =
                            await fetch(
                                '/reservation',
                                {
                                    method: 'POST',
                                    headers: {
                                        'Content-Type':
                                            'application/x-www-form-urlencoded'
                                    },
                                    body:
                                        'action=cancel'
                                        + '&reservationId='
                                        + reservationId
                                }
                            );

                        const message = await response.text();

                        alert(message);

                        // 重新加载我的预约
                        const reservationResponse = await fetch( '/reservation' );
                        const reservations = await reservationResponse.json();

                        reservationCards.value =
                            reservations.map(r => ({
                                id: r.id,
                                title: r.projectName,
                                code: r.reservationCode,
                                name: r.status
                            }));

                    } catch (e) {
                        console.error( '取消预约失败', e );
                        alert( '取消预约失败' );
                    }
                }

                // 预约概览数据
                const appointmentForm = ref([
                    { id: 1, code: 'ZXS12LH', title: '图书馆·自习室', name: '李华', email:'lihua@stu.edu', time:'2024-04-08 10:30'},
                    { id: 2, code: 'JZ123LH', title: 'AI前沿讲座：大模型时代', name: '李华', email:'lihua@stu.edu', time:'2024-04-08 14:20' }
                ]);

                // 项目管理数据
                const projectManagement = ref([
                    { id: 1, title: '图书馆·自习室', time: '4/10 08:00-22:00', num1: '30', num2: '12', desc: '安静学习空间' },
                    { id: 2, title: '图书馆·研讨室', time: '4/10 10:00-22:00', num1: '8', num2: '3', desc: '小组讨论专用' },
                    { id: 3, title: 'AI前沿讲座：大模型时代', time: '4/11 14:00-16:00', num1: '100', num2: '45', desc: '张三教授主讲' },
                    { id: 4, title: '心理健康公开课', time: '4/12 15:30-17:00', num1: '80', num2: '60', desc: '减压与情绪管理' },
                ]);

                return {
                    currentRole,
                    userTab,
                    adminTab,
                    serviceCategories,
                    reserveProject,
                    cancelReservation,
                    reservationCards,
                    appointmentForm,
                    projectManagement
                };
            }
        }).mount('#app');
    </script>
</body>
</html>