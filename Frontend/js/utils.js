// utils.js

// Show toast message
export function showToast(message, type = 'success') {
  const toast = document.getElementById('toast');
  toast.textContent = message;
  toast.className = `fixed top-4 right-4 z-50 px-4 py-2 rounded shadow-lg text-white ${type === 'success' ? 'bg-green-500' : 'bg-red-500'}`;
  toast.classList.remove('hidden');
  setTimeout(() => {
    toast.classList.add('hidden');
  }, 2500);
}

// Show/hide spinner
export function showSpinner(show = true) {
  const spinner = document.getElementById('spinner');
  if (spinner) spinner.classList.toggle('hidden', !show);
}

// Show/hide loading state
export function showLoading() {
  const spinner = document.getElementById('spinner');
  if (spinner) spinner.classList.remove('hidden');
}

export function hideLoading() {
  const spinner = document.getElementById('spinner');
  if (spinner) spinner.classList.add('hidden');
}

// Simple form validation
export function validateEmail(email) {
  return /^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(email);
}

export function validatePassword(password) {
  return password.length >= 6;
} 