import { API_BASE_URL, API_TIMEOUT } from './config.js'
import { getUserId } from './auth.js'

function normalizeUrl(path) {
	const baseUrl = API_BASE_URL.replace(/\/$/, '')
	const apiPath = path.indexOf('/') === 0 ? path : '/' + path
	return baseUrl + apiPath
}

function buildData(data, auth) {
	const payload = Object.assign({}, data || {})
	const userId = getUserId()

	if (auth !== false && userId > 0 && !payload.userId) {
		payload.userId = userId
	}

	return payload
}

function buildHeader(method, header) {
	const contentType = method === 'GET'
		? 'application/json'
		: 'application/x-www-form-urlencoded'

	return Object.assign({
		'content-type': contentType
	}, header || {})
}

export function request(path, options) {
	const requestOptions = options || {}
	const method = (requestOptions.method || 'GET').toUpperCase()

	return new Promise((resolve, reject) => {
		uni.request({
			url: normalizeUrl(path),
			method,
			timeout: requestOptions.timeout || API_TIMEOUT,
			data: buildData(requestOptions.data, requestOptions.auth),
			header: buildHeader(method, requestOptions.header),
			success(res) {
				const body = res.data || {}
				const ok = res.statusCode >= 200 && res.statusCode < 300

				if (ok && body.success !== false) {
					resolve(Object.prototype.hasOwnProperty.call(body, 'data') ? body.data : body)
					return
				}

				reject(body.message || '请求失败')
			},
			fail(err) {
				reject(err.errMsg || '网络连接失败')
			}
		})
	})
}

export function get(path, data, options) {
	return request(path, Object.assign({}, options || {}, {
		method: 'GET',
		data
	}))
}

export function post(path, data, options) {
	return request(path, Object.assign({}, options || {}, {
		method: 'POST',
		data
	}))
}
