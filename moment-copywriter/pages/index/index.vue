<template>
	<view class="app-page home-page">
		<view class="top-space"></view>

		<view class="title-wrap">
			<text class="main-title">AI文案生成器</text>
		</view>

		<scroll-view class="category-scroll" scroll-x>
			<view class="category-row">
				<button
					v-for="item in categories"
					:key="item.name"
					class="category-chip"
					:class="{ active: category === item.name }"
					@tap="chooseCategory(item)"
				>
					{{ item.name }}
				</button>
			</view>
		</scroll-view>

		<view class="surface-card input-card">
			<textarea
				class="textarea prompt-input"
				v-model="scene"
				:placeholder="placeholder"
				maxlength="220"
			></textarea>

			<button
				class="primary-button generate-button"
				:loading="loading"
				:disabled="loading"
				@tap="generateCopywriting"
			>
				{{ loading ? '生成中' : '生成文案' }}
			</button>
		</view>

		<view class="surface-card result-card" v-if="result">
			<view class="result-title-row">
				<view class="spark-icon"></view>
				<text class="result-title">生成结果</text>
			</view>

			<text class="result-content">{{ result }}</text>

			<view class="result-actions">
				<button class="plain-action" @tap="copyResult">
					<text class="circle-mark">✓</text>
					<text>复制</text>
				</button>
				<button class="favorite-action" :class="{ active: favorite }" @tap="toggleCurrentFavorite">
					<text class="circle-mark">{{ favorite ? '♥' : '♡' }}</text>
					<text>{{ favorite ? '已收藏' : '收藏' }}</text>
				</button>
			</view>
		</view>

		<view class="surface-card hint-card" v-else>
			<text class="hint-title">试试这些内容</text>
			<view class="example-list">
				<button
					v-for="item in examples"
					:key="item"
					class="example-item"
					@tap="useExample(item)"
				>
					{{ item }}
				</button>
			</view>
		</view>

		<app-tabbar active="home"></app-tabbar>
	</view>
</template>

<script>
	import AppTabbar from '../../components/app-tabbar/app-tabbar.vue'
	import { post } from '../../common/request.js'
	import { ensureLogin, isLoggedIn, loadCurrentUser } from '../../common/auth.js'
	import { isFavorite, toggleFavorite } from '../../common/favorites.js'

	export default {
		components: {
			AppTabbar
		},
		data() {
			return {
				category: '节日祝福',
				scene: '',
				result: '',
				recordId: 0,
				loading: false,
				favorite: false,
				categories: [
					{
						name: '朋友圈文案',
						placeholder: '输入想发布的场景，例如：傍晚散步，晚风轻轻吹过，心情很好'
					},
					{
						name: '节日祝福',
						placeholder: '输入祝福对象、想要的风格，例如：给妈妈的中秋祝福，温柔简短'
					},
					{
						name: '自我介绍',
						placeholder: '输入你的身份、特点和用途，例如：大学生社团面试，真诚自然'
					},
					{
						name: '演讲稿',
						placeholder: '输入主题、场合和时长，例如：班会分享，主题是坚持'
					},
					{
						name: '短视频配文',
						placeholder: '输入视频内容和情绪，例如：旅行 vlog，轻松治愈'
					},
					{
						name: '治愈短句',
						placeholder: '输入情绪或关键词，例如：最近很累，想要一点鼓励'
					}
				],
				examples: [
					'给妈妈的中秋祝福，温柔简短',
					'傍晚散步，晚风轻轻吹过，今天的心情很好',
					'旅行短视频配文，轻松治愈，适合朋友圈'
				]
			}
		},
		computed: {
			placeholder() {
				const current = this.categories.find(item => item.name === this.category)
				return current ? current.placeholder : '请输入你的需求'
			},
			currentRecord() {
				return {
					id: this.recordId,
					recordId: this.recordId,
					style: this.category,
					scene: this.scene,
					generatedContent: this.result,
					content: this.result
				}
			}
		},
		methods: {
			chooseCategory(item) {
				this.category = item.name
			},
			generateCopywriting() {
				this.requireLogin().then(loggedIn => {
					if (!loggedIn) {
						return
					}

					this.submitGenerateCopywriting()
				})
			},
			requireLogin() {
				if (isLoggedIn()) {
					return Promise.resolve(true)
				}

				return loadCurrentUser().then(user => {
					if (user) {
						return true
					}

					ensureLogin()
					return false
				}).catch(() => {
					ensureLogin()
					return false
				})
			},
			submitGenerateCopywriting() {
				if (!this.scene.trim()) {
					uni.showToast({
						title: '请输入文案需求',
						icon: 'none'
					})
					return
				}

				this.loading = true
				this.recordId = 0
				this.favorite = false

				post('/api/copywriting/generate', {
					scene: this.scene,
					mood: this.category,
					style: this.category,
					keywords: this.scene
				}).then(data => {
					this.result = data && data.content ? data.content : ''
					this.recordId = data && data.recordId ? data.recordId : 0
					this.favorite = isFavorite(data)
					this.loading = false
				}).catch(message => {
					this.loading = false
					uni.showToast({
						title: String(message),
						icon: 'none'
					})
				})
			},
			copyResult() {
				if (!this.result) {
					return
				}

				uni.setClipboardData({
					data: this.result
				})
			},
			toggleCurrentFavorite() {
				if (!this.result) {
					return
				}

				toggleFavorite(Object.assign({}, this.currentRecord, {
					favorite: this.favorite
				})).then(favorite => {
					this.favorite = favorite
					uni.showToast({
						title: this.favorite ? '已收藏' : '已取消',
						icon: 'none'
					})
				}).catch(message => {
					uni.showToast({
						title: String(message),
						icon: 'none'
					})
				})
			},
			useExample(item) {
				this.scene = item
			}
		}
	}
</script>

<style>
	.top-space {
		height: 54rpx;
	}

	.title-wrap {
		margin-bottom: 48rpx;
		text-align: center;
	}

	.main-title {
		color: #030303;
		font-size: 60rpx;
		font-weight: 900;
		line-height: 1.2;
		letter-spacing: 0;
	}

	.category-scroll {
		width: 100%;
		white-space: nowrap;
		margin-bottom: 36rpx;
	}

	.category-row {
		display: flex;
		width: max-content;
		padding-right: 40rpx;
	}

	.category-chip {
		height: 58rpx;
		margin-right: 16rpx;
		padding: 0 24rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border: 1rpx solid #DADDE4;
		border-radius: 29rpx;
		background: #FFFFFF;
		color: #272727;
		font-size: 28rpx;
		white-space: nowrap;
	}

	.category-chip.active {
		border-color: #006DF0;
		background: #0878F7;
		color: #FFFFFF;
		font-weight: 800;
	}

	.input-card {
		padding: 44rpx 40rpx 42rpx;
		margin-bottom: 40rpx;
	}

	.prompt-input {
		margin-bottom: 44rpx;
		padding: 0;
		background: #FFFFFF;
	}

	.generate-button {
		height: 88rpx;
	}

	.result-card {
		padding: 40rpx;
		margin-bottom: 40rpx;
	}

	.result-title-row {
		display: flex;
		align-items: center;
		margin-bottom: 34rpx;
	}

	.spark-icon {
		width: 24rpx;
		height: 24rpx;
		margin-right: 18rpx;
		position: relative;
		transform: rotate(45deg);
		background: #FFC928;
	}

	.spark-icon::before,
	.spark-icon::after {
		content: '';
		position: absolute;
		left: 50%;
		top: 50%;
		transform: translate(-50%, -50%);
		background: #FFC928;
	}

	.spark-icon::before {
		width: 44rpx;
		height: 12rpx;
	}

	.spark-icon::after {
		width: 12rpx;
		height: 44rpx;
	}

	.result-title {
		color: #050505;
		font-size: 38rpx;
		font-weight: 800;
	}

	.result-content {
		display: block;
		color: #090909;
		font-size: 34rpx;
		line-height: 1.6;
		white-space: pre-wrap;
	}

	.result-actions {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top: 42rpx;
	}

	.plain-action,
	.favorite-action {
		height: 72rpx;
		display: flex;
		align-items: center;
		color: #666666;
		font-size: 30rpx;
	}

	.favorite-action {
		padding: 0 28rpx;
		border: 1rpx solid #E1E4EA;
		border-radius: 36rpx;
		color: #005EDB;
		background: #FFFFFF;
	}

	.favorite-action.active {
		background: #EAF3FF;
		border-color: #B7D7FF;
	}

	.circle-mark {
		width: 34rpx;
		height: 34rpx;
		margin-right: 12rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border: 2rpx solid currentColor;
		border-radius: 50%;
		font-size: 22rpx;
		line-height: 1;
	}

	.hint-card {
		padding: 34rpx 34rpx 24rpx;
	}

	.hint-title {
		display: block;
		margin-bottom: 22rpx;
		color: #111111;
		font-size: 32rpx;
		font-weight: 800;
	}

	.example-item {
		width: 100%;
		min-height: 76rpx;
		padding: 18rpx 24rpx;
		display: block;
		box-sizing: border-box;
		border-bottom: 1rpx solid #F0F1F4;
		color: #555555;
		font-size: 28rpx;
		line-height: 1.45;
		text-align: left;
	}

	.example-item:last-child {
		border-bottom: none;
	}
</style>
