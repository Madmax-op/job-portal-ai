// login.js
import { loginUser } from './auth.js';
import { showSpinner } from './utils.js';

document.getElementById('loginForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  showSpinner(true);
  const username = document.getElementById('email').value.trim();
  const password = document.getElementById('password').value;
  await loginUser(username, password);
  showSpinner(false);
}); 