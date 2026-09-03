import { get, post } from './request.js'

function recordIdOf(record) {
	if (!record) {
		return 0
	}

	return Number(record.id || record.recordId || 0)
}

export function getFavorites() {
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

	return post('/api/copywriting/favorite/add', {
		recordId: id
	})
}

export function removeFavorite(record) {
	const id = recordIdOf(record)
	if (!id) {
		return Promise.reject('缺少文案记录ID，无法取消收藏')
	}

	return post('/api/copywriting/favorite/delete', {
		recordId: id
	})
}

export function toggleFavorite(record) {
	if (isFavorite(record)) {
		return removeFavorite(record).then(() => false)
	}

	return addFavorite(record).then(() => true)
}
