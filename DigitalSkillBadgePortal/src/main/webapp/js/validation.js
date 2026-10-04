/**
 * Digital Skill Badge Portal - Client-Side Script & Validation
 */

document.addEventListener('DOMContentLoaded', () => {

  // Auto-dismiss alerts after 5 seconds
  const alerts = document.querySelectorAll('.alert-auto-dismiss');
  alerts.forEach(alert => {
    setTimeout(() => {
      alert.style.opacity = '0';
      alert.style.transition = 'opacity 0.5s ease';
      setTimeout(() => alert.remove(), 500);
    }, 5000);
  });

  // Client-side Registration Validation
  const registerForm = document.getElementById('registerForm');
  if (registerForm) {
    registerForm.addEventListener('submit', (e) => {
      const studentId = document.getElementById('studentId')?.value.trim();
      const name = document.getElementById('name')?.value.trim();
      const email = document.getElementById('email')?.value.trim();
      const password = document.getElementById('password')?.value;
      const confirmPassword = document.getElementById('confirmPassword')?.value;
      const course = document.getElementById('course')?.value.trim();

      if (!studentId || !name || !email || !password || !confirmPassword || !course) {
        showClientError(registerForm, 'All fields are required.');
        e.preventDefault();
        return;
      }

      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(email)) {
        showClientError(registerForm, 'Please enter a valid email address.');
        e.preventDefault();
        return;
      }

      if (password.length < 6) {
        showClientError(registerForm, 'Password must be at least 6 characters long.');
        e.preventDefault();
        return;
      }

      if (password !== confirmPassword) {
        showClientError(registerForm, 'Passwords do not match.');
        e.preventDefault();
        return;
      }
    });
  }

  // Client-side Issue Badge Validation
  const issueBadgeForm = document.getElementById('issueBadgeForm');
  if (issueBadgeForm) {
    issueBadgeForm.addEventListener('submit', (e) => {
      const studentId = document.getElementById('studentId')?.value;
      const moduleId = document.getElementById('moduleId')?.value;
      const badgeLevel = document.getElementById('badgeLevel')?.value;
      const issueDate = document.getElementById('issueDate')?.value;
      const expiryDate = document.getElementById('expiryDate')?.value;

      if (!studentId || !moduleId || !badgeLevel || !issueDate) {
        showClientError(issueBadgeForm, 'Please select Student, Module, Level and Issue Date.');
        e.preventDefault();
        return;
      }

      if (expiryDate && new Date(expiryDate) < new Date(issueDate)) {
        showClientError(issueBadgeForm, 'Expiry date cannot be earlier than issue date.');
        e.preventDefault();
        return;
      }
    });
  }

  // Live filter for table rows (badges or students)
  const searchInput = document.getElementById('tableSearchInput');
  if (searchInput) {
    searchInput.addEventListener('input', () => {
      const filter = searchInput.value.toLowerCase();
      const rows = document.querySelectorAll('.data-table tbody tr');
      rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(filter) ? '' : 'none';
      });
    });
  }
});

/**
 * Copies text (e.g. badge code) to clipboard with temporary feedback
 */
function copyToClipboard(text, element) {
  if (!text) return;
  navigator.clipboard.writeText(text).then(() => {
    const originalText = element.innerHTML;
    element.innerHTML = 'Copied!';
    element.classList.add('copied');
    setTimeout(() => {
      element.innerHTML = originalText;
      element.classList.remove('copied');
    }, 2000);
  }).catch(err => {
    console.error('Failed to copy text: ', err);
  });
}

/**
 * Copies full verification URL
 */
function copyVerificationLink(badgeCode, btn) {
  const url = window.location.origin + window.location.pathname.replace(/\/student\/.*|\/admin\/.*/, '') + '/verify?code=' + encodeURIComponent(badgeCode);
  copyToClipboard(url, btn);
}

/**
 * Pre-fills the public verification form with a sample code for rapid testing
 */
function setSampleVerifyCode(code) {
  const input = document.getElementById('verifyCodeInput');
  if (input) {
    input.value = code;
    input.focus();
  }
}

/**
 * Shows an inline error message inside a form
 */
function showClientError(formElement, message) {
  let existing = formElement.querySelector('.client-error-alert');
  if (!existing) {
    existing = document.createElement('div');
    existing.className = 'alert alert-error client-error-alert';
    formElement.insertBefore(existing, formElement.firstChild);
  }
  existing.innerHTML = '<span>' + message + '</span>';
  existing.scrollIntoView({ behavior: 'smooth', block: 'center' });
}

/**
 * Triggers browser print dialog for certificates
 */
function printCertificate() {
  window.print();
}
