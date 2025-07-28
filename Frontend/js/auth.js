// auth.js
import { showToast, showSpinner, validateEmail, validatePassword } from './utils.js';
import { API_BASE } from './config.js';

export function saveJWT(token) {
  localStorage.setItem('jwt', token);
}

export function getJWT() {
  return localStorage.getItem('jwt');
}

export function logout() {
  localStorage.removeItem('jwt');
  window.location.href = 'login.html';
}

// Decode JWT and get user role
export function getUserRole() {
  const token = getJWT();
  if (!token) return null;
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    // Role may be in 'authorities', 'role', or 'roles' depending on backend
    if (payload.role) return payload.role;
    if (payload.authorities && payload.authorities.length > 0) return payload.authorities[0];
    if (payload.roles && payload.roles.length > 0) return payload.roles[0];
    return null;
  } catch (e) {
    return null;
  }
}

// Get user info from JWT token
export function getUserInfo() {
  const token = getJWT();
  if (!token) return null;
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return {
      username: payload.sub || payload.username || 'User',
      role: payload.role || (payload.authorities && payload.authorities[0]) || (payload.roles && payload.roles[0]) || 'USER',
      email: payload.email || ''
    };
  } catch (e) {
    return null;
  }
}

// Check if user is authenticated
export function checkAuth() {
  const token = getJWT();
  if (!token) return false;
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    // Check if token is expired
    if (payload.exp && payload.exp * 1000 < Date.now()) {
      logout();
      return false;
    }
    return true;
  } catch (e) {
    logout();
    return false;
  }
}

// Attach logout button if present
window.addEventListener('DOMContentLoaded', () => {
  const btn = document.getElementById('logoutBtn');
  if (btn) {
    if (getJWT()) {
      btn.classList.remove('hidden');
      btn.onclick = logout;
    } else {
      btn.classList.add('hidden');
    }
  }
});

// Register user
export async function registerUser(name, email, password, role = 'USER') {
  if (!validateEmail(email)) {
    showToast('Invalid email', 'error');
    return;
  }
  if (!validatePassword(password)) {
    showToast('Password must be at least 6 characters', 'error');
    return;
  }
  showSpinner(true);
  try {
    const res = await fetch(`${API_BASE}/api/users/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username: name, email, password, role })
    });
    showSpinner(false);
    if (res.ok) {
      showToast('Registration successful! Please login.');
      setTimeout(() => window.location.href = 'login.html', 1200);
    } else {
      showToast('Registration failed', 'error');
    }
  } catch (e) {
    showSpinner(false);
    showToast('Network error', 'error');
  }
}

// Login user
export async function loginUser(username, password) {
  // No email validation for login, username can be any string
  showSpinner(true);
  try {
    console.log('Attempting login for user:', username);
    const res = await fetch(`${API_BASE}/api/users/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    });
    console.log('Login response status:', res.status);
    const token = await res.text();
    console.log('Login response body:', token);
    showSpinner(false);
    if (res.ok && token && !token.includes('Login Failed')) {
      saveJWT(token);
      showToast('Login successful!');
      setTimeout(() => window.location.href = 'index.html', 1200);
    } else {
      showToast('Login failed', 'error');
    }
  } catch (e) {
    console.error('Login error:', e);
    showSpinner(false);
    showToast('Network error', 'error');
  }
} 