<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- 项目管理页面 -->
<div>
    <div class="view-header project-header">
        <h2>项目管理</h2>
        <button
            type="button"
            class="btn-primary"
            @click="showAddDialog = true">
            新增项目
        </button>
    </div>
    <div>
        <div class="project-item" v-for="pm in projectManagement" :key="pm.id">
            <div class="project-info">
                <strong>{{ pm.title }}</strong>
                <br>
                <small>{{ pm.time }} | 容量：{{ pm.num1 }} 剩余：{{ pm.num2 }}</small>
                <div>{{ pm.desc }}</div>
            </div>
            <button
                class="btn-delete-project"
                @click="deleteProject(pm.id)">
                删除
            </button>
        </div>
    </div>
    <!-- 新增项目弹窗 -->
    <div
        v-if="showAddDialog"
        class="dialog-mask"
        @click.self="closeAddDialog">

        <div
            class="project-dialog-card"
            role="dialog"
            aria-modal="true">

            <div class="dialog-header">
                <h3>新增项目</h3>

                <button
                    type="button"
                    class="dialog-close"
                    @click="closeAddDialog">
                    &times;
                </button>
            </div>

            <hr>

            <form
                class="project-form"
                @submit.prevent="submitNewProject">

                <div class="project-form-group">
                    <label for="categoryId">项目分类</label>
                    <select
                        id="categoryId"
                        v-model.number="newProject.categoryId"
                        required>
                        <option disabled value="">请选择分类</option>
                        <option
                            v-for="category in serviceCategories"
                            :key="category.id"
                            :value="category.id">
                            {{ category.name }}
                        </option>
                    </select>
                </div>

                <div class="project-form-group">
                    <label for="projectName">项目名称</label>
                    <input
                        id="projectName"
                        v-model.trim="newProject.projectName"
                        type="text"
                        placeholder="请输入项目名称"
                        required>
                </div>

                <div class="project-form-group">
                    <label for="description">项目描述</label>
                    <textarea
                        id="description"
                        v-model.trim="newProject.description"
                        placeholder="请输入项目描述"
                        rows="3"
                        required>
                    </textarea>
                </div>

                <div class="project-form-group">
                    <label for="location">地点</label>
                    <input
                        id="location"
                        v-model.trim="newProject.location"
                        type="text"
                        placeholder="请输入项目地点"
                        required>
                </div>

                <div class="project-form-group">
                    <label for="appointmentTime">预约时间</label>
                    <input
                        id="appointmentTime"
                        v-model.trim="newProject.appointmentTime"
                        type="text"
                        placeholder="例如：08:00-22:00"
                        required>
                </div>

                <div class="project-form-group">
                    <label for="capacity">容量</label>
                    <input
                        id="capacity"
                        v-model.number="newProject.capacity"
                        type="number"
                        min="1"
                        placeholder="请输入项目容量"
                        required>
                </div>

                <div class="project-form-actions">
                    <button
                        type="button"
                        class="dialog-cancel-btn"
                        @click="closeAddDialog">
                        取消
                    </button>

                    <button
                        type="submit"
                        class="dialog-save-btn"
                        :disabled="savingProject">
                        {{ savingProject ? '保存中...' : '保存' }}
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>