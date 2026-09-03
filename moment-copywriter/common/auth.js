let currentUser = null

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
