import { API_BASE_URL, API_TIMEOUT } from './config.js'

let currentUser = null
let sessionCookie = ''

export function saveUser(user) {
	currentUser = user || null
}

export function getUser() {
	return currentUser
}

export function getUserId() {
	const user = getUser()
	return user && user.id ? Number(user.id) : 0
}

export function isLoggedIn() {
	return getUserId() > 0
}

export function clearUser() {
	currentUser = null
	sessionCookie = ''
}

export function ensureLogin() {
	if (isLoggedIn()) {
		return true
	}

	uni.navigateTo({
		url: '/pages/login/login'
	})
	return false
}

export function getSessionCookie() {
	return sessionCookie
}

export function saveSessionCookie(header) {
	const rawCookie = getHeader(header, 'Set-Cookie')
	if (!rawCookie) {
		return
	}

	const cookieText = Array.isArray(rawCookie) ? rawCookie.join(',') : String(rawCookie)
	const matched = cookieText.match(/JSESSIONID=[^;,]+/i)
	if (matched) {
		sessionCookie = matched[0]
	}
}

export function loadCurrentUser() {
	return new Promise((resolve, reject) => {
		uni.request({
			url: normalizeUrl('/api/current-user'),
			method: 'GET',
			timeout: API_TIMEOUT,
			withCredentials: true,
			header: buildAuthHeader(),
			success(res) {
				saveSessionCookie(res.header)

				const body = res.data || {}
				if (res.statusCode >= 200 && res.statusCode < 300 && body.success !== false) {
					const user = body.data && body.data.user ? body.data.user : null
					saveUser(user)
					resolve(user)
					return
				}

				clearUser()
				reject(body.message || '请先登录')
			},
			fail(err) {
				reject(err.errMsg || '网络连接失败')
			}
		})
	})
}

function normalizeUrl(path) {
	const baseUrl = API_BASE_URL.replace(/\/$/, '')
	const apiPath = path.indexOf('/') === 0 ? path : '/' + path
	return baseUrl + apiPath
}

function buildAuthHeader() {
	if (!sessionCookie) {
		return {}
	}

	return {
		Cookie: sessionCookie
	}
}

function getHeader(header, name) {
	if (!header) {
		return ''
	}

	if (header[name]) {
		return header[name]
	}

	const lowerName = name.toLowerCase()
	const key = Object.keys(header).find(item => item.toLowerCase() === lowerName)
	return key ? header[key] : ''
}
