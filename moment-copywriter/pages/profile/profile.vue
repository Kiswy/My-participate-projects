<template>
	<view class="app-page profile-page">
		<text class="page-title profile-title">我的</text>

		<view class="user-card">
			<view class="avatar-wrap">
				<view class="avatar-head"></view>
				<view class="avatar-body"></view>
			</view>
			<view class="user-info" v-if="user">
				<text class="username">{{ displayUsername }}</text>
				<text class="login-state">已登录</text>
			</view>
			<view class="user-info" v-else>
				<text class="username">未登录</text>
				<text class="login-state">登录后保存文案历史</text>
			</view>
		</view>

		<view class="menu-card">
			<button class="menu-row" @tap="goFavorites">
				<view class="menu-icon bubble-icon">
					<text>...</text>
				</view>
				<text class="menu-text">收藏的文案</text>
				<text class="menu-arrow">›</text>
			</button>

			<view class="divider"></view>

			<button class="menu-row" @tap="goHistory">
				<view class="menu-icon note-icon">
					<text>▤</text>
				</view>
				<text class="menu-text">历史记录</text>
				<text class="menu-arrow">›</text>
			</button>

			<view class="divider"></view>

			<button class="menu-row" @tap="clearHistory">
				<view class="menu-icon note-icon">
					<text>⌫</text>
				</view>
				<text class="menu-text">清空全部历史记录</text>
				<text class="menu-arrow">›</text>
			</button>

			<view class="divider"></view>

			<button class="menu-row" @tap="showAbout">
				<view class="menu-icon check-icon">
					<text>✓</text>
				</view>
				<text class="menu-text">关于小程序</text>
				<text class="menu-arrow">›</text>
			</button>

			<view class="divider" v-if="user"></view>

			<button class="menu-row" v-if="user" @tap="logout">
				<view class="menu-icon logout-icon">
					<text>×</text>
				</view>
				<text class="menu-text">退出登录</text>
				<text class="menu-arrow">›</text>
			</button>
		</view>

		<view class="login-actions" v-if="!user">
			<button class="primary-button" @tap="goLogin">登录</button>
			<button class="outline-button register-button" @tap="goRegister">注册</button>
		</view>

		<app-tabbar active="profile"></app-tabbar>
	</view>
</template>

<script>
	import AppTabbar from '../../components/app-tabbar/app-tabbar.vue'
	import { post } from '../../common/request.js'
	import { clearUser, getUser, ensureLogin, loadCurrentUser } from '../../common/auth.js'

	export default {
		components: {
			AppTabbar
		},
		data() {
			return {
				user: null
			}
		},
		computed: {
			displayUsername() {
				return this.user ? this.displayText(this.user.username) : ''
			}
		},
		onShow() {
			this.user = getUser()
			loadCurrentUser().then(user => {
				this.user = user
			}).catch(() => {
				this.user = null
			})
		},
		methods: {
			goFavorites() {
				if (!ensureLogin()) {
					return
				}

				uni.reLaunch({
					url: '/pages/history/history?favorite=1'
				})
			},
			goHistory() {
				if (!ensureLogin()) {
					return
				}

				uni.reLaunch({
					url: '/pages/history/history'
				})
			},
			clearHistory() {
				if (!ensureLogin()) {
					return
				}

				uni.showModal({
					title: '清空历史',
					content: '确定清空全部历史记录吗？',
					success: res => {
						if (!res.confirm) {
							return
						}

						post('/api/copywriting/clear-history').then(data => {
							const deletedCount = data && data.deletedCount ? data.deletedCount : 0
							uni.showToast({
								title: '已清空' + deletedCount + '条记录',
								icon: 'none'
							})
						}).catch(message => {
							uni.showToast({
								title: String(message),
								icon: 'none'
							})
						})
					}
				})
			},
			goLogin() {
				uni.navigateTo({
					url: '/pages/login/login'
				})
			},
			goRegister() {
				uni.navigateTo({
					url: '/pages/register/register'
				})
			},
			showAbout() {
				uni.showModal({
					title: '关于小程序',
					content: '朋友圈文案生成器，用于生成、复制和收藏日常文案。',
					showCancel: false
				})
			},
			logout() {
				post('/api/logout', {}, {
					auth: false
				}).then(() => {
					this.clearAndGoHome()
				}).catch(() => {
					this.clearAndGoHome()
				})
			},
			clearAndGoHome() {
				clearUser()
				this.user = null
				uni.reLaunch({
					url: '/pages/index/index'
				})
			},
			displayText(value) {
				if (value === null || value === undefined) {
					return ''
				}

				const text = String(value)
				if (!this.looksGarbled(text)) {
					return text
				}

				try {
					const decoded = this.decodeMojibake(text)
					return decoded && !this.looksGarbled(decoded) ? decoded : text
				} catch (e) {
					return text
				}
			},
			decodeMojibake(text) {
				let encoded = ''

				for (let i = 0; i < text.length; i++) {
					const code = text.charCodeAt(i)
					if (code > 255) {
						encoded += encodeURIComponent(text.charAt(i))
						continue
					}

					const hex = code.toString(16)
					encoded += '%' + (hex.length === 1 ? '0' + hex : hex)
				}

				return decodeURIComponent(encoded)
			},
			looksGarbled(text) {
				return /[ÃÂäåæçèéïâ]/.test(text)
			}
		}
	}
</script>

<style>
	.profile-page {
		padding-top: 92rpx;
	}

	.profile-title {
		margin-bottom: 50rpx;
	}

	.user-card {
		min-height: 250rpx;
		padding: 48rpx 48rpx;
		display: flex;
		align-items: center;
		border-radius: 16rpx 16rpx 0 0;
		background: linear-gradient(135deg, #CFE4FF 0%, #E8F3FF 100%);
	}

	.avatar-wrap {
		width: 132rpx;
		height: 132rpx;
		margin-right: 44rpx;
		position: relative;
		border-radius: 50%;
		background: #FFFFFF;
		overflow: hidden;
	}

	.avatar-head {
		position: absolute;
		left: 50%;
		top: 34rpx;
		width: 42rpx;
		height: 42rpx;
		transform: translateX(-50%);
		border-radius: 50%;
		background: #EEF4FF;
	}

	.avatar-body {
		position: absolute;
		left: 50%;
		bottom: 22rpx;
		width: 88rpx;
		height: 50rpx;
		transform: translateX(-50%);
		border-radius: 48rpx 48rpx 0 0;
		background: #EEF4FF;
	}

	.user-info {
		flex: 1;
		min-width: 0;
	}

	.username {
		display: block;
		overflow: hidden;
		color: #050505;
		font-size: 42rpx;
		font-weight: 800;
		line-height: 1.2;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.login-state {
		display: block;
		margin-top: 18rpx;
		color: #666666;
		font-size: 30rpx;
	}

	.menu-card {
		padding: 22rpx 32rpx;
		border-radius: 16rpx;
		background: #FFFFFF;
		box-shadow: 0 18rpx 42rpx rgba(24, 58, 101, 0.08);
		transform: translateY(-28rpx);
	}

	.menu-row {
		width: 100%;
		height: 120rpx;
		display: flex;
		align-items: center;
		text-align: left;
	}

	.menu-icon {
		width: 58rpx;
		height: 58rpx;
		margin-right: 28rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 8rpx;
		background: #F2F7FF;
		color: #0878F7;
		font-size: 28rpx;
		font-weight: 700;
	}

	.note-icon,
	.check-icon,
	.logout-icon {
		font-size: 34rpx;
	}

	.logout-icon {
		color: #D94B3D;
		background: #FFF3F2;
	}

	.menu-text {
		flex: 1;
		color: #111111;
		font-size: 34rpx;
		font-weight: 500;
	}

	.menu-arrow {
		color: #8B8B8B;
		font-size: 54rpx;
		line-height: 1;
	}

	.divider {
		height: 1rpx;
		margin-left: 86rpx;
		background: #EFEFEF;
	}

	.login-actions {
		margin-top: 12rpx;
	}

	.register-button {
		width: 100%;
		margin-top: 22rpx;
	}
</style>
