<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>首页-多功能预约系统</title>
    <link rel="stylesheet" href="css/homepage_Max.css">
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
</head>
<body>
    <!-- 当前登录用户信息 -->
    <h3 class="title">
        <span>
            欢迎您：${loginUser.username}
        </span>

        <a href="/logout" class="title-a">
          退出登录
        </a>
    </h3>

    <!-- Vue 应用挂载区域 -->
    <div id="app">
        <div class="app-container">
            <!-- 系统顶部导航和角色切换 -->
            <header class="main-header">
                <div class="logo-area">
                    <h1>多功能预约系统</h1>
                </div>
                <div class="role-switch">
                    <button class="role-btn" :class="{ active: currentRole === 'user' }" @click="currentRole = 'user'">学生/教职工端</button>
                    <button class="role-btn" :class="{ active: currentRole === 'admin' }" @click="currentRole = 'admin'">后台管理端</button>
                </div>
            </header>

            <!-- 普通用户功能区域 -->
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

            <!-- 管理员功能区域 -->
            <div id="adminModeContent" class="dashboard" :style="{ display: currentRole === 'admin' ? 'block' : 'none' }">
                <div class="admin-tab-nav">
                    <button class="tab-btn" :class="{ active: adminTab === 'ao' }" @click="adminTab = 'ao'">预约概览</button>
                    <button class="tab-btn" :class="{ active: adminTab === 'pm' }" @click="adminTab = 'pm'">项目管理</button>
                </div>

                <!-- 管理员预约总览 -->
                <div id="aoPanel" :style="{ display: adminTab === 'ao' ? 'block' : 'none' }">
                    <jsp:include page="homepage/AO.jsp"/>
                </div>

                <!-- 管理员项目管理 -->
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
                // ====================
                // 页面切换状态
                // ====================

                // 当前显示的角色页面
                const currentRole = ref('user');

                // 普通用户端当前页签
                const userTab = ref('hall');

                // 管理员端当前页签
                const adminTab = ref('ao');

                // ====================
                // 页面业务数据
                // ====================

                const serviceCategories = ref([]); // 预约大厅数据
                const reservationCards = ref([]);  // 我的预约数据
                const appointmentForm = ref([]);   // 预约概览数据
                const projectManagement = ref([]); // 项目管理数据

                // ====================
                // 页面初始化
                // ====================

                // 页面加载完成后，从后端读取项目和预约数据
                onMounted(async () => {
                    try {
                        // 加载预约大厅
                        await loadServiceCategories();

                        // 加载当前用户的预约记录
                        await loadReservations();

                        // 加载管理员预约总览
                        await loadAppointmentOverview();

                        // 加载管理员项目列表
                        await loadProjectManagement();

                    } catch (e) {
                        console.error('加载预约大厅失败', e);
                    }
                });

                // ====================
                // 加载预约大厅
                // ====================

                async function loadServiceCategories() {
                    const categoryResponse = await fetch('/categories');
                    const categories = await categoryResponse.json();
                    const result = [];

                    for (const category of categories) {
                        const projectResponse = await fetch('/projects?categoryId=' + category.id);
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
                }

                // ====================
                // 预约项目
                // ====================

                // 向预约接口提交项目ID
                async function reserveProject(item) {
                    try {
                        // 提交预约请求
                        const response = await fetch('/reservation', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            },
                            body: 'projectId=' + item.id
                        });

                        const message = await response.text();

                        alert(message);

                        // 预约操作完成后刷新“我的预约”
                        await loadReservations();

                        // 加载管理员预约总览
                        await loadAppointmentOverview();

                    } catch (e) {
                        console.error('预约失败', e);
                        alert('预约失败，请稍后重试');
                    }
                }

                // ====================
                // 取消预约
                // ====================

                // 根据预约ID提交取消请求
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
                                    // action=cancel 用于区分预约和取消操作
                                    body:
                                        'action=cancel'
                                        + '&reservationId='
                                        + reservationId
                                }
                            );

                        const message = await response.text();

                        alert(message);

                        // 取消操作完成后刷新预约列表
                        await loadReservations();

                        // 加载管理员预约总览
                        await loadAppointmentOverview();

                    } catch (e) {
                        console.error( '取消预约失败', e );
                        alert( '取消预约失败' );
                    }
                }

                // ====================
                // 加载我的预约
                // ====================

                // 查询当前登录用户尚未取消的预约
                async function loadReservations() {
                    const reservationResponse = await fetch( '/reservation' );
                    const reservations = await reservationResponse.json();

                    // 将后端预约对象转换为预约卡片数据
                    reservationCards.value = reservations.map(r => ({
                        id: r.id,
                        title: r.projectName,
                        code: r.reservationCode,
                        name: r.status
                    }));
                }

                // ====================
                // 加载项目管理
                // ====================

                // 查询后台项目列表并转换为项目卡片数据
                async function loadProjectManagement() {
                    try {
                        const response = await fetch('/admin/projects');
                        const projects = await response.json();

                        // 将后端项目对象转换为项目管理页面需要的格式
                        projectManagement.value = projects.map(project => ({
                            id: project.id,
                            title: project.projectName,
                            time: project.appointmentTime,
                            num1: project.capacity,
                            num2: project.remainingCount,
                            desc: project.description,
                            categoryName: project.categoryName
                        }));
                    } catch (e) {
                        console.error('加载项目管理失败', e);
                    }
                }

                // ====================
                // 删除项目
                // ====================

                async function deleteProject(projectId) {
                    const confirmed =
                        confirm(
                             '确定删除该项目吗？'
                        );

                    if (!confirmed) {
                        return;
                    }

                    try {
                        const response = await fetch(
                            '/admin/projects?projectId='
                            + projectId,
                            {
                                method: 'DELETE'
                            }
                        );

                        const message = await response.text();

                        if (!response.ok) {
                            throw new Error(
                                message || '删除项目失败'
                            );
                        }

                        // 加载管理员项目列表
                        await loadProjectManagement();

                        // 加载预约大厅
                        await loadServiceCategories();

                    } catch (e) {
                        console.error('删除项目失败', e);
                        alert('删除项目失败');
                    }
                }

                // ====================
                // 加载预约总览
                // ====================

                async function loadAppointmentOverview() {
                    try {
                        const response = await fetch('/admin/reservations');
                        const reservations = await response.json();

                        appointmentForm.value = reservations.map(r => ({
                            id: r.id,
                            code: r.reservationCode,
                            title: r.projectName,
                            name: r.username,
                            email: r.phone,
                            time: r.reserveTime,
                            status: r.status
                        }));

                    } catch (e) {
                        console.error('加载预约总览失败', e);
                    }
                }

                // ====================
                // 新增项目弹窗
                // ====================

                 // 控制新增项目弹窗是否显示
                 const showAddDialog = ref(false);

                 // 防止保存过程中重复提交
                 const savingProject = ref(false);

                // 创建一份空的项目表单数据
                function createEmptyProject() {
                    return {
                        categoryId: '',
                        projectName: '',
                        description: '',
                        location: '',
                        appointmentTime: '',
                        capacity: ''
                    };
                }

                // 新增项目表单数据
                const newProject = ref(createEmptyProject());

                // 打开弹窗并清空上一次填写的内容
                function openAddDialog() {
                    newProject.value = createEmptyProject();
                    showAddDialog.value = true;
                }

                // 关闭弹窗并重置表单
                function closeAddDialog() {
                    showAddDialog.value = false;
                    newProject.value = createEmptyProject();
                }

                // ====================
                // 提交新增项目
                // ====================

                // 校验提交状态并将项目数据发送到后端
                async function submitNewProject() {
                    // 保存过程中不再接受新的提交
                    if (savingProject.value) {
                        return;
                    }

                    savingProject.value = true;

                    try {
                        // 将项目对象转换为表单格式的请求参数
                        const body = new URLSearchParams({
                            categoryId: String(newProject.value.categoryId),
                            projectName: newProject.value.projectName,
                            description: newProject.value.description,
                            location: newProject.value.location,
                            appointmentTime: newProject.value.appointmentTime,
                            capacity: String(newProject.value.capacity)
                        });

                        // 调用项目新增接口
                        const response = await fetch('/projects', {
                            method: 'POST',
                            headers: {
                                'Content-Type':
                                    'application/x-www-form-urlencoded;charset=UTF-8'
                            },
                            body: body.toString()
                        });

                        const message = await response.text();

                        // HTTP状态码不是成功状态时，按新增失败处理
                        if (!response.ok) {
                            throw new Error(message || '新增项目失败');
                        }

                        // 加载管理员项目列表
                        await loadProjectManagement();

                        // 加载预约大厅
                        await loadServiceCategories();

                        alert(message);
                        closeAddDialog();

                    } catch (error) {
                        alert(error.message || '新增项目失败');
                    // 无论成功或失败，都解除保存状态
                    } finally {
                        savingProject.value = false;
                    }
                }

                // 将页面状态、数据和操作方法暴露给JSP模板
                return {
                    currentRole,
                    userTab,
                    adminTab,
                    serviceCategories,
                    reserveProject,
                    cancelReservation,
                    reservationCards,
                    appointmentForm,
                    loadAppointmentOverview,
                    projectManagement,
                    loadProjectManagement,
                    deleteProject,
                    showAddDialog,
                    newProject,
                    openAddDialog,
                    closeAddDialog,
                    submitNewProject,
                    savingProject
                };
            }
        }).mount('#app');
    </script>
</body>
</html>
