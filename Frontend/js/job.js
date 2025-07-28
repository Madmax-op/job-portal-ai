// job.js
import { showToast, showSpinner } from './utils.js';
import { getJWT } from './auth.js';
import { API_BASE } from './config.js';

const JOB_API = `${API_BASE}/api/jobs`;

export async function fetchAllJobs() {
  showSpinner(true);
  try {
    const res = await fetch(`${JOB_API}/all`);
    showSpinner(false);
    if (res.ok) {
      return await res.json();
    } else {
      showToast('Failed to fetch jobs', 'error');
      return [];
    }
  } catch (e) {
    showSpinner(false);
    showToast('Network error', 'error');
    return [];
  }
}

export async function postJob(title, description, experience, stack) {
  showSpinner(true);
  try {
    const jwt = getJWT();
    if (!jwt) {
      showToast('You must be logged in to post a job', 'error');
      showSpinner(false);
      setTimeout(() => window.location.href = 'login.html', 1200);
      return;
    }
    const res = await fetch(`${JOB_API}/post`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${jwt}`
      },
      body: JSON.stringify({
        postProfile: title,
        postDesc: description,
        reqExperience: parseInt(experience),
        postTechStack: stack.split(',').map(s => s.trim())
      })
    });
    showSpinner(false);
    if (res.ok) {
      showToast('Job posted successfully!');
      setTimeout(() => window.location.href = 'index.html', 1200);
    } else {
      showToast('Failed to post job', 'error');
    }
  } catch (e) {
    showSpinner(false);
    showToast('Network error', 'error');
  }
}

export async function deleteJob(postId) {
  const jwt = getJWT();
  if (!jwt) return;
  await fetch(`${JOB_API}/post/${postId}`, {
    method: 'DELETE',
    headers: { 'Authorization': `Bearer ${jwt}` }
  });
} 