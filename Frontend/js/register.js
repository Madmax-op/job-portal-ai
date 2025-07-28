// register.js
import { registerUser } from './auth.js';
import { showSpinner, showToast } from './utils.js';

document.getElementById('registerForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  showSpinner(true);
  const name = document.getElementById('name').value.trim();
  const email = document.getElementById('email').value.trim();
  const password = document.getElementById('password').value;
  const role = document.getElementById('role').value;
  
  if (!role) {
    showToast('Please select a role', 'error');
    showSpinner(false);
    return;
  }
  
  await registerUser(name, email, password, role);
  showSpinner(false);
}); 