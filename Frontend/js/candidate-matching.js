import { API_BASE } from './config.js';
import { checkAuth, getUserInfo, logout, getJWT } from './auth.js';
import { showToast, showLoading, hideLoading } from './utils.js';

// Check authentication and admin role on page load
document.addEventListener('DOMContentLoaded', async () => {
    const isAuthenticated = await checkAuth();
    if (!isAuthenticated) {
        window.location.href = 'login.html';
        return;
    }

    // Check if user is admin or recruiter
    const userInfo = getUserInfo();
    if (userInfo.role !== 'ADMIN' && userInfo.role !== 'RECRUITER') {
        showToast('Access denied. Admin or recruiter privileges required.', 'error');
        window.location.href = 'index.html';
        return;
    }

    // Display user info
    document.getElementById('userInfo').textContent = `${userInfo.role === 'ADMIN' ? 'Admin' : 'Recruiter'}: ${userInfo.username}`;

    // Setup logout
    document.getElementById('logoutBtn').addEventListener('click', logout);

    // Load jobs and setup matching
    await loadJobs();
    setupMatching();
});

async function loadJobs() {
    try {
        const response = await fetch(`${API_BASE}/api/jobs/all`);
        const jobs = await response.json();

        const jobSelect = document.getElementById('jobSelect');
        jobSelect.innerHTML = '<option value="">Choose a job position...</option>';

        jobs.forEach(job => {
            const option = document.createElement('option');
            option.value = job.postId;
            option.textContent = `${job.postProfile} (ID: ${job.postId})`;
            jobSelect.appendChild(option);
        });
    } catch (error) {
        console.error('Error loading jobs:', error);
        showToast('Failed to load job positions', 'error');
    }
}

function setupMatching() {
    const matchBtn = document.getElementById('matchBtn');
    const jobSelect = document.getElementById('jobSelect');

    matchBtn.addEventListener('click', async () => {
        const selectedJobId = jobSelect.value;
        
        if (!selectedJobId) {
            showToast('Please select a job position', 'error');
            return;
        }

        await findMatchingCandidates(selectedJobId);
    });
}

async function findMatchingCandidates(jobId) {
    const matchBtn = document.getElementById('matchBtn');
    const loadingState = document.getElementById('loadingState');
    const results = document.getElementById('results');
    const noResults = document.getElementById('noResults');

    // Show loading state
    matchBtn.disabled = true;
    loadingState.classList.remove('hidden');
    results.classList.add('hidden');
    noResults.classList.add('hidden');

    try {
        const token = getJWT();
        const response = await fetch(`${API_BASE}/api/resumes/jobs/${jobId}/candidates`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const candidates = await response.json();
            
            if (candidates.length > 0) {
                displayCandidates(candidates);
            } else {
                noResults.classList.remove('hidden');
            }
        } else {
            showToast('Failed to fetch candidates', 'error');
        }
    } catch (error) {
        console.error('Error fetching candidates:', error);
        showToast('An error occurred while fetching candidates', 'error');
    } finally {
        matchBtn.disabled = false;
        loadingState.classList.add('hidden');
    }
}

function displayCandidates(candidates) {
    const results = document.getElementById('results');
    const candidatesList = document.getElementById('candidatesList');
    const template = document.getElementById('candidateCardTemplate');

    // Clear previous results
    candidatesList.innerHTML = '';

    candidates.forEach(candidate => {
        const card = template.content.cloneNode(true);
        
        // Fill candidate information
        card.querySelector('.candidate-name').textContent = candidate.candidateName;
        card.querySelector('.candidate-email').textContent = candidate.candidateEmail;
        card.querySelector('.candidate-file').textContent = `Resume: ${candidate.resumeFileName}`;
        card.querySelector('.match-score').textContent = `${Math.round(candidate.matchScore * 100)}%`;
        card.querySelector('.candidate-summary').textContent = candidate.aiSummary || 'No summary available';
        card.querySelector('.candidate-reason').textContent = candidate.matchReason || 'No analysis available';

        // Display skills
        const skillsContainer = card.querySelector('.candidate-skills');
        if (candidate.extractedSkills) {
            const skills = candidate.extractedSkills.split(',').map(skill => skill.trim());
            skills.forEach(skill => {
                const skillTag = document.createElement('span');
                skillTag.className = 'bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded-full';
                skillTag.textContent = skill;
                skillsContainer.appendChild(skillTag);
            });
        }

        candidatesList.appendChild(card);
    });

    results.classList.remove('hidden');
} 