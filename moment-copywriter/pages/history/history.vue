<template>
	<view class="app-page history-page">
		<view class="history-shell">
			<view class="history-header">
				<text class="page-title">历史记录</text>
				<view class="favorite-filter">
					<text>只看收藏</text>
					<switch
						class="filter-switch"
						:checked="onlyFavorites"
						color="#0878F7"
						@change="changeFavoriteFilter"
					/>
				</view>
			</view>

			<view class="empty-state" v-if="!loading && displayRecords.length === 0">
				{{ onlyFavorites ? '暂无收藏文案' : '暂无历史记录' }}
			</view>

			<view
				v-for="record in displayRecords"
				:key="record.uniqueKey"
				class="history-card"
				@tap="copyRecord(record)"
			>
				<view class="card-top">
					<button
						class="card-icon"
						:class="{ muted: !record.favorite }"
						@tap.stop="toggleRecordFavorite(record)"
					>
						<text>{{ record.favorite ? '♥' : '♡' }}</text>
					</button>
					<view class="card-main">
						<view class="card-title-row">
							<text class="card-title">{{ record.style || '朋友圈文案' }}</text>
							<text class="arrow">›</text>
						</view>
						<text class="card-preview">{{ record.generatedContent }}</text>
					</view>
				</view>

				<view class="card-bottom">
					<text class="record-time">{{ formatTime(record.createTime || record.favoriteTime) }}</text>
					<button class="delete-button" @tap.stop="deleteRecord(record)">删除</button>
				</view>
			</view>
		</view>

		<app-tabbar active="history"></app-tabbar>
	</view>
</template>

<script>
	import AppTabbar from '../../components/app-tabbar/app-tabbar.vue'
	import { get, post } from '../../common/request.js'
	import { ensureLogin } from '../../common/auth.js'
	import { getFavorites, isFavorite, toggleFavorite } from '../../common/favorites.js'

	export default {
		components: {
			AppTabbar
		},
		data() {
			return {
				records: [],
				favorites: [],
				onlyFavorites: false,
				loading: false
			}
		},
		computed: {
			displayRecords() {
				const source = this.onlyFavorites ? this.favorites : this.records
				return source.map((record, index) => {
					const favorite = this.onlyFavorites ? true : isFavorite(record)
					const uniqueKey = (record.id ? 'id:' + record.id : 'local:' + index)
					return Object.assign({}, record, {
						favorite,
						uniqueKey
					})
				})
			}
		},
		onLoad(options) {
			this.onlyFavorites = options && options.favorite === '1'
		},
		onShow() {
			if (!ensureLogin()) {
				return
			}
			this.loadRecords(false)
		},
		onPullDownRefresh() {
			if (!ensureLogin()) {
				uni.stopPullDownRefresh()
				return
			}
			this.loadRecords(true)
		},
		methods: {
			refreshFavorites() {
				return getFavorites().then(data => {
					this.favorites = Array.isArray(data) ? data : []
				})
			},
			changeFavoriteFilter(event) {
				this.onlyFavorites = event.detail.value
				this.loadRecords(false)
			},
			loadRecords(stopRefresh) {
				if (this.onlyFavorites) {
					this.loadFavorites(stopRefresh)
					return
				}

				this.loadHistory(stopRefresh)
			},
			loadFavorites(stopRefresh) {
				this.loading = true
				this.refreshFavorites().then(() => {
					this.loading = false
					if (stopRefresh) {
						uni.stopPullDownRefresh()
					}
				}).catch(message => {
					this.loading = false
					if (stopRefresh) {
						uni.stopPullDownRefresh()
					}
					uni.showToast({
						title: String(message),
						icon: 'none'
					})
				})
			},
			loadHistory(stopRefresh) {
				this.loading = true
				get('/api/copywriting/history').then(data => {
					this.records = Array.isArray(data) ? data : []
					this.loading = false
					if (stopRefresh) {
						uni.stopPullDownRefresh()
					}
				}).catch(message => {
					this.loading = false
					if (stopRefresh) {
						uni.stopPullDownRefresh()
					}
					uni.showToast({
						title: String(message),
						icon: 'none'
					})
				})
			},
			copyRecord(record) {
				if (!record || !record.generatedContent) {
					return
				}

				uni.setClipboardData({
					data: record.generatedContent
				})
			},
			toggleRecordFavorite(record) {
				toggleFavorite(record).then(favorite => {
					this.applyFavoriteState(record, favorite)
					uni.showToast({
						title: favorite ? '已收藏' : '已取消',
						icon: 'none'
					})
				}).catch(message => {
					uni.showToast({
						title: String(message),
						icon: 'none'
					})
				})
			},
			applyFavoriteState(record, favorite) {
				const id = record && record.id
				if (!id) {
					return
				}

				this.records.forEach(item => {
					if (item.id === id) {
						item.favorite = favorite
					}
				})

				if (favorite) {
					return
				}

				this.favorites = this.favorites.filter(item => item.id !== id)
			},
			deleteRecord(record) {
				uni.showModal({
					title: '删除记录',
					content: '确定删除这条文案吗？',
					success: res => {
						if (!res.confirm) {
							return
						}

						if (!record.id) {
							uni.showToast({
								title: '缺少文案记录ID，无法删除',
								icon: 'none'
							})
							return
						}

						post('/api/copywriting/delete', {
							id: record.id
						}).then(() => {
							this.loadRecords(false)
						}).catch(message => {
							uni.showToast({
								title: String(message),
								icon: 'none'
							})
						})
					}
				})
			},
			formatTime(value) {
				if (!value) {
					return ''
				}

				return String(value).replace('T', ' ').slice(0, 16)
			}
		}
	}
</script>

<style>
	.history-page {
		padding-top: 92rpx;
	}

	.history-shell {
		min-height: calc(100vh - 248rpx);
		padding: 52rpx 40rpx;
		border-radius: 16rpx;
		background: rgba(255, 255, 255, 0.78);
	}

	.history-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-bottom: 52rpx;
	}

	.favorite-filter {
		display: flex;
		align-items: center;
		color: #111111;
		font-size: 28rpx;
	}

	.filter-switch {
		margin-left: 12rpx;
		transform: scale(0.74);
		transform-origin: center right;
	}

	.history-card {
		margin-bottom: 28rpx;
		padding: 32rpx 32rpx 30rpx;
		border-radius: 16rpx;
		background: #FFFFFF;
		box-shadow: 0 16rpx 34rpx rgba(24, 58, 101, 0.08);
	}

	.card-top {
		display: flex;
		align-items: flex-start;
	}

	.card-icon {
		width: 48rpx;
		height: 48rpx;
		margin-right: 24rpx;
		border-radius: 8rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		background: #0878F7;
		color: #FFFFFF;
		font-size: 26rpx;
		line-height: 1;
	}

	.card-icon.muted {
		background: #C9C9C9;
	}

	.card-main {
		flex: 1;
		min-width: 0;
	}

	.card-title-row {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-bottom: 18rpx;
	}

	.card-title {
		color: #0A0A0A;
		font-size: 34rpx;
		font-weight: 800;
	}

	.arrow {
		color: #9A9A9A;
		font-size: 46rpx;
		line-height: 1;
	}

	.card-preview {
		display: -webkit-box;
		overflow: hidden;
		color: #242424;
		font-size: 28rpx;
		line-height: 1.45;
		text-overflow: ellipsis;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
	}

	.card-bottom {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top: 30rpx;
	}

	.record-time {
		color: #555555;
		font-size: 26rpx;
	}

	.delete-button {
		height: 58rpx;
		padding: 0 12rpx;
		color: #222222;
		font-size: 26rpx;
	}
</style>
