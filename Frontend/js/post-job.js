// post-job.js
import { postJob } from './job.js';
import { showSpinner } from './utils.js';

document.getElementById('postJobForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  showSpinner(true);
  const title = document.getElementById('title').value.trim();
  const description = document.getElementById('description').value.trim();
  const experience = document.getElementById('experience').value;
  const stack = document.getElementById('stack').value.trim();
  await postJob(title, description, experience, stack);
  showSpinner(false);
}); 