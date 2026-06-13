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
        const { createApp, ref, onMounted } = Vue;

        createApp({
            setup() {
                // 普通用户端仅需要预约大厅和我的预约两个页签
                const userTab = ref('hall');

                // 预约大厅数据
                const serviceCategories = ref([]);
                onMounted(async () => {
                    try {
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

                // 我的预约数据
                const reservationCards = ref([
                    { id: 1, title: '图书馆·自习室', code: 'ZXS12LH', name: '李华' },
                    { id: 2, title: 'AI前沿讲座：大模型时代', code: 'JZ123LH', name: '李华' }
                ]);

                return {
                    userTab,
                    serviceCategories,
                    reserveProject,
                    reservationCards
                };
            }
        }).mount('#app');
    </script>
</body>
</html>
