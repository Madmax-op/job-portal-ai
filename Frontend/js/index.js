// index.js
import { fetchAllJobs } from './job.js';
import { showToast, showLoading, hideLoading } from './utils.js';
import { getUserRole, getJWT, getUserInfo, logout } from './auth.js';
import { deleteJob } from './job.js';
import { API_BASE } from './config.js';

window.addEventListener('DOMContentLoaded', async () => {
  // Display user info and setup logout
  const userInfo = getUserInfo();
  const userInfoElement = document.getElementById('userInfo');
  const logoutBtn = document.getElementById('logoutBtn');
  const aiFeatures = document.getElementById('aiFeatures');
  
  if (userInfoElement && userInfo) {
    userInfoElement.textContent = `Welcome, ${userInfo.username}`;
  }
  
  if (logoutBtn && userInfo) {
    logoutBtn.classList.remove('hidden');
    logoutBtn.addEventListener('click', logout);
  }

  // Show AI features based on user role
  if (aiFeatures && userInfo) {
    const role = getUserRole();
    console.log('Current user role:', role); // Debug log
    
    // Show the aiFeatures container for all authenticated users
    aiFeatures.classList.remove('hidden');
    
    // Show resume upload for all authenticated users (button is already visible in HTML)
    // No need to remove hidden class since we removed it from HTML
    
    // Show AI Matching for ADMIN and RECRUITER users
    const matchingLink = document.querySelector('a[href="candidate-matching.html"]');
    console.log('AI Matching link found:', !!matchingLink); // Debug log
    if (matchingLink && (role === 'ADMIN' || role === 'RECRUITER')) {
      matchingLink.classList.remove('hidden');
      console.log('AI Matching link shown for', role); // Debug log
    } else if (matchingLink) {
      matchingLink.classList.add('hidden');
      console.log('AI Matching link hidden for role:', role); // Debug log
    }
  }

  // Setup resume upload functionality after DOM is ready
  setTimeout(() => {
    setupResumeUpload();
  }, 100);

  // Load user's resume summary if they have uploaded one
  console.log('About to call loadResumeSummary...');
  // Add a small delay to ensure DOM is fully loaded
  setTimeout(() => {
    loadResumeSummary();
  }, 500);
  
  // Add refresh button functionality
  const refreshBtn = document.getElementById('refreshResumeSummary');
  if (refreshBtn) {
    refreshBtn.addEventListener('click', () => {
      console.log('Refresh button clicked');
      loadResumeSummary();
    });
  }
  
  // Show Add Job button for RECRUITER or ADMIN
  const addJobBtn = document.getElementById('addJobBtn');
  const role = getUserRole();
  if (addJobBtn && (role === 'RECRUITER' || role === 'ADMIN')) {
    addJobBtn.classList.remove('hidden');
    addJobBtn.onclick = () => window.location.href = 'post-job.html';
  }
  
  // Show delete button for ADMIN only (RECRUITER can post but not delete)
  if (role === 'ADMIN') {
    // Delete functionality is already handled in the job cards loop
  }

  const jobsContainer = document.getElementById('jobsContainer');
  const jobs = await fetchAllJobs();
  jobsContainer.innerHTML = '';
  if (jobs.length === 0) {
    jobsContainer.innerHTML = '<p class="text-gray-500">No jobs found.</p>';
    return;
  }
  jobs.forEach(job => {
    const card = document.createElement('div');
    card.className = 'bg-white rounded-lg shadow-md p-6 hover:shadow-xl transition flex flex-col';
    card.innerHTML = `
      <h3 class="text-lg font-bold text-blue-700 mb-2">${job.postProfile}</h3>
      <p class="text-gray-700 mb-2">${job.postDesc}</p>
      <div class="mb-2"><span class="font-semibold">Experience:</span> ${job.reqExperience} years</div>
      <div class="mb-2"><span class="font-semibold">Tech Stack:</span> ${Array.isArray(job.postTechStack) ? job.postTechStack.join(', ') : ''}</div>
    `;
    // Show delete button for ADMIN
    if (role === 'ADMIN') {
      const deleteBtn = document.createElement('button');
      deleteBtn.textContent = 'Delete';
      deleteBtn.className = 'mt-2 bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 transition self-end';
      deleteBtn.onclick = async () => {
        if (confirm('Are you sure you want to delete this job?')) {
          await deleteJob(job.postId);
          window.location.reload();
        }
      };
      card.appendChild(deleteBtn);
    }
    jobsContainer.appendChild(card);
  });
});

// Resume Upload Functions
function setupResumeUpload() {
  console.log('Setting up resume upload...');
  const toggleBtn = document.getElementById('toggleResumeUpload');
  const uploadSection = document.getElementById('resumeUploadSection');
  const fileInput = document.getElementById('resumeFile');
  const fileInfo = document.getElementById('fileInfo');
  const uploadBtn = document.getElementById('uploadResumeBtn');
  let selectedFile = null;

  console.log('Elements found:', {
    toggleBtn: !!toggleBtn,
    uploadSection: !!uploadSection,
    fileInput: !!fileInput,
    fileInfo: !!fileInfo,
    uploadBtn: !!uploadBtn
  });

  // Toggle resume upload section
  if (toggleBtn && uploadSection) {
    console.log('Adding click listener to toggle button');
    toggleBtn.addEventListener('click', () => {
      console.log('Toggle button clicked');
      const isHidden = uploadSection.classList.contains('hidden');
      uploadSection.classList.toggle('hidden');
      toggleBtn.textContent = isHidden ? 'Hide Upload' : 'Upload Resume';
      console.log('Upload section visibility:', !isHidden);
    });
  } else {
    console.error('Toggle button or upload section not found');
  }

  // Handle file selection
  if (fileInput) {
    fileInput.addEventListener('change', (e) => {
      selectedFile = e.target.files[0];
      if (selectedFile) {
        // Validate file type
        const validTypes = ['application/pdf', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'];
        if (!validTypes.includes(selectedFile.type)) {
          showToast('Please select a PDF or DOCX file', 'error');
          fileInput.value = '';
          selectedFile = null;
          fileInfo.classList.add('hidden');
          return;
        }

        // Validate file size (10MB)
        if (selectedFile.size > 10 * 1024 * 1024) {
          showToast('File size must be less than 10MB', 'error');
          fileInput.value = '';
          selectedFile = null;
          fileInfo.classList.add('hidden');
          return;
        }

        // Show file info
        fileInfo.classList.remove('hidden');
        fileInfo.innerHTML = `
          <div class="flex items-center space-x-2">
            <svg class="h-5 w-5 text-green-500" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
            </svg>
            <span>Selected: ${selectedFile.name} (${(selectedFile.size / 1024 / 1024).toFixed(2)} MB)</span>
          </div>
        `;
        uploadBtn.disabled = false;
      }
    });
  }

  // Handle file upload
  if (uploadBtn) {
    uploadBtn.addEventListener('click', async () => {
      if (!selectedFile) {
        showToast('Please select a file first', 'error');
        return;
      }

      const formData = new FormData();
      formData.append('file', selectedFile);

      showLoading();
      uploadBtn.disabled = true;

             try {
         const token = getJWT();
         console.log('Uploading file:', selectedFile.name);
         console.log('API Base:', API_BASE);
         console.log('Token available:', !!token);
         
         const response = await fetch(`${API_BASE}/api/resumes/upload`, {
           method: 'POST',
           headers: {
             'Authorization': `Bearer ${token}`
           },
           body: formData
         });
         
         console.log('Response status:', response.status);
         console.log('Response headers:', response.headers);

        if (response.ok) {
          const result = await response.json();
          showToast('Resume uploaded and parsed successfully!', 'success');
          
          // Show AI summary if available
          if (result.aiSummary) {
            showToast(`AI Summary: ${result.aiSummary.substring(0, 100)}...`, 'info');
          }
          
          // Reset form
          fileInput.value = '';
          selectedFile = null;
          fileInfo.classList.add('hidden');
          uploadBtn.disabled = true;
          
          // Hide upload section
          uploadSection.classList.add('hidden');
          toggleBtn.textContent = 'Upload Resume';
          
                     // Refresh resume summary display
           console.log('Calling loadResumeSummary after successful upload...');
           setTimeout(() => {
             loadResumeSummary();
           }, 1000); // Add a small delay to ensure backend has processed the data
                 } else {
           const errorText = await response.text();
           console.error('Upload failed with status:', response.status);
           console.error('Error text:', errorText);
           showToast(`Upload failed: ${errorText}`, 'error');
         }
       } catch (error) {
         console.error('Upload error:', error);
         console.error('Error details:', error.message);
         showToast(`Upload failed: ${error.message}`, 'error');
       } finally {
        hideLoading();
        uploadBtn.disabled = false;
      }
    });
  }

  // Drag and drop functionality
  const dropZone = document.querySelector('.border-dashed');
  if (dropZone) {
    dropZone.addEventListener('dragover', (e) => {
      e.preventDefault();
      dropZone.classList.add('border-blue-400', 'bg-blue-50');
    });

    dropZone.addEventListener('dragleave', (e) => {
      e.preventDefault();
      dropZone.classList.remove('border-blue-400', 'bg-blue-50');
    });

    dropZone.addEventListener('drop', (e) => {
      e.preventDefault();
      dropZone.classList.remove('border-blue-400', 'bg-blue-50');
      
      const files = e.dataTransfer.files;
      if (files.length > 0) {
        fileInput.files = files;
        fileInput.dispatchEvent(new Event('change'));
      }
    });
  }
}

// Load and display user's resume summary
async function loadResumeSummary() {
  try {
    const token = getJWT();
    if (!token) {
      console.log('No JWT token found, skipping resume summary load');
      return;
    }

    console.log('Loading resume summary...'); // Debug log
    console.log('API Base URL:', API_BASE); // Debug log
    console.log('Token available:', !!token); // Debug log
    
    const response = await fetch(`${API_BASE}/api/resumes/my-summary`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    console.log('Resume summary response status:', response.status); // Debug log
    console.log('Response headers:', response.headers); // Debug log

    if (response.ok) {
      const resumeData = await response.json();
      console.log('Resume summary data:', resumeData); // Debug log
      
      if (resumeData.success && resumeData.aiSummary) {
        console.log('Displaying resume summary with AI data'); // Debug log
        displayResumeSummary(resumeData);
      } else if (resumeData.success && resumeData.message === "No resume uploaded yet") {
        console.log('No resume uploaded yet'); // Debug log
        displayResumeSummary({ success: false });
      } else {
        console.log('No resume data or AI summary available'); // Debug log
        displayResumeSummary({ success: false });
      }
    } else if (response.status === 404) {
      // Endpoint doesn't exist yet, show "no resume" message
      console.log('Resume summary endpoint not available yet');
      displayResumeSummary({ success: false });
    } else if (response.status === 401) {
      console.log('Unauthorized - user not logged in');
      displayResumeSummary({ success: false });
    } else {
      console.log('Resume summary request failed with status:', response.status);
      const errorText = await response.text();
      console.log('Error response:', errorText);
      displayResumeSummary({ success: false });
    }
  } catch (error) {
    console.error('Error loading resume summary:', error);
    console.error('Error details:', error.message);
    // Show "no resume" message on error
    displayResumeSummary({ success: false });
  }
}

// Display resume summary in the UI
function displayResumeSummary(resumeData) {
  console.log('displayResumeSummary called with data:', resumeData);
  
  const noResumeMessage = document.getElementById('noResumeMessage');
  const resumeContent = document.getElementById('resumeContent');
  const aiSummaryText = document.getElementById('aiSummaryText');
  const skillsContainer = document.getElementById('skillsContainer');
  const experienceText = document.getElementById('experienceText');
  const educationText = document.getElementById('educationText');
  
  console.log('DOM elements found:', {
    noResumeMessage: !!noResumeMessage,
    resumeContent: !!resumeContent,
    aiSummaryText: !!aiSummaryText,
    skillsContainer: !!skillsContainer,
    experienceText: !!experienceText,
    educationText: !!educationText
  });

  if (resumeData.success && resumeData.aiSummary) {
    // Hide "no resume" message and show content
    if (noResumeMessage) noResumeMessage.classList.add('hidden');
    if (resumeContent) resumeContent.classList.remove('hidden');

    // Display AI summary
    if (aiSummaryText) {
      aiSummaryText.textContent = resumeData.aiSummary || 'No summary available';
    }

    // Display skills
    if (skillsContainer) {
      skillsContainer.innerHTML = '';
      if (resumeData.extractedSkills) {
        const skills = resumeData.extractedSkills.split(',').map(skill => skill.trim());
        skills.forEach(skill => {
          const skillTag = document.createElement('span');
          skillTag.className = 'bg-green-200 text-green-800 text-xs px-2 py-1 rounded-full mr-1 mb-1';
          skillTag.textContent = skill;
          skillsContainer.appendChild(skillTag);
        });
      }
    }

    // Display experience
    if (experienceText) {
      experienceText.textContent = resumeData.experience || 'No experience information available';
    }

    // Display education
    if (educationText) {
      educationText.textContent = resumeData.education || 'No education information available';
    }
  } else {
    // Show "no resume" message and hide content
    if (noResumeMessage) noResumeMessage.classList.remove('hidden');
    if (resumeContent) resumeContent.classList.add('hidden');
  }
}