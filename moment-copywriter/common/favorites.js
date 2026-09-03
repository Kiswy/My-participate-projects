import { get, post } from './request.js'

const FAVORITE_API_READY = false
const FAVORITE_API_MESSAGE = '收藏功能待后端数据库接口实现'

function recordIdOf(record) {
	if (!record) {
		return 0
	}

	return Number(record.id || record.recordId || 0)
}

export function getFavorites() {
	if (!FAVORITE_API_READY) {
		return Promise.reject(FAVORITE_API_MESSAGE)
	}

	return get('/api/copywriting/favorites')
}

export function isFavorite(record) {
	return !!(record && record.favorite)
}

export function addFavorite(record) {
	const id = recordIdOf(record)
	if (!id) {
		return Promise.reject('缺少文案记录ID，无法收藏')
	}

	if (!FAVORITE_API_READY) {
		return Promise.reject(FAVORITE_API_MESSAGE)
	}

	return post('/api/copywriting/favorite/add', {
		id
	})
}

export function removeFavorite(record) {
	const id = recordIdOf(record)
	if (!id) {
		return Promise.reject('缺少文案记录ID，无法取消收藏')
	}

	if (!FAVORITE_API_READY) {
		return Promise.reject(FAVORITE_API_MESSAGE)
	}

	return post('/api/copywriting/favorite/delete', {
		id
	})
}

export function toggleFavorite(record) {
	if (isFavorite(record)) {
		return removeFavorite(record).then(() => false)
	}

	return addFavorite(record).then(() => true)
}
