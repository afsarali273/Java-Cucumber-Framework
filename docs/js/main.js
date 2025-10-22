// Theme Toggle
const themeToggle = document.getElementById('theme-toggle');
const html = document.documentElement;

// Load saved theme
const savedTheme = localStorage.getItem('theme') || 'light';
html.setAttribute('data-theme', savedTheme);

themeToggle?.addEventListener('click', () => {
  const currentTheme = html.getAttribute('data-theme');
  const newTheme = currentTheme === 'light' ? 'dark' : 'light';
  html.setAttribute('data-theme', newTheme);
  localStorage.setItem('theme', newTheme);
  themeToggle.innerHTML = newTheme === 'light' ? '🌙' : '☀️';
});

// Set initial icon
if (themeToggle) {
  themeToggle.innerHTML = savedTheme === 'light' ? '🌙' : '☀️';
}

// Mobile Menu Toggle
const mobileMenuBtn = document.getElementById('mobile-menu-btn');
const sidebar = document.querySelector('.sidebar');

mobileMenuBtn?.addEventListener('click', () => {
  sidebar?.classList.toggle('active');
});

// Close sidebar on link click (mobile)
document.querySelectorAll('.nav-link').forEach(link => {
  link.addEventListener('click', () => {
    if (window.innerWidth <= 768) {
      sidebar?.classList.remove('active');
    }
  });
});

// Copy Code to Clipboard
document.querySelectorAll('.copy-btn').forEach(btn => {
  btn.addEventListener('click', () => {
    const codeBlock = btn.closest('.code-wrapper')?.querySelector('code');
    if (codeBlock) {
      navigator.clipboard.writeText(codeBlock.textContent).then(() => {
        btn.textContent = 'Copied!';
        setTimeout(() => {
          btn.textContent = 'Copy';
        }, 2000);
      });
    }
  });
});

// Search Functionality
const searchBox = document.getElementById('search-box');
searchBox?.addEventListener('input', (e) => {
  const searchTerm = e.target.value.toLowerCase();
  const sections = document.querySelectorAll('.section');
  
  sections.forEach(section => {
    const text = section.textContent.toLowerCase();
    if (text.includes(searchTerm)) {
      section.style.display = 'block';
    } else {
      section.style.display = 'none';
    }
  });
  
  // Show all if search is empty
  if (searchTerm === '') {
    sections.forEach(section => {
      section.style.display = 'block';
    });
  }
});

// Active Navigation
const currentPage = window.location.pathname.split('/').pop() || 'index.html';
document.querySelectorAll('.nav-link').forEach(link => {
  if (link.getAttribute('href') === currentPage) {
    link.classList.add('active');
  }
});

// Smooth Scroll for Anchor Links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
  anchor.addEventListener('click', function (e) {
    e.preventDefault();
    const target = document.querySelector(this.getAttribute('href'));
    if (target) {
      target.scrollIntoView({
        behavior: 'smooth',
        block: 'start'
      });
    }
  });
});

// Add Copy Button to Code Blocks
document.querySelectorAll('pre').forEach(pre => {
  if (!pre.closest('.code-wrapper')) {
    const wrapper = document.createElement('div');
    wrapper.className = 'code-wrapper';
    pre.parentNode.insertBefore(wrapper, pre);
    wrapper.appendChild(pre);
    
    const header = document.createElement('div');
    header.className = 'code-header';
    header.innerHTML = `
      <span class="code-lang">Code</span>
      <button class="copy-btn">Copy</button>
    `;
    wrapper.insertBefore(header, pre);
    
    // Add copy functionality
    const copyBtn = header.querySelector('.copy-btn');
    copyBtn.addEventListener('click', () => {
      const code = pre.querySelector('code');
      if (code) {
        navigator.clipboard.writeText(code.textContent).then(() => {
          copyBtn.textContent = 'Copied!';
          setTimeout(() => {
            copyBtn.textContent = 'Copy';
          }, 2000);
        });
      }
    });
  }
});

// Back to Top Button
const backToTop = document.createElement('button');
backToTop.innerHTML = '↑';
backToTop.className = 'back-to-top';
backToTop.style.cssText = `
  position: fixed;
  bottom: 2rem;
  right: 2rem;
  background: var(--primary);
  color: white;
  border: none;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  font-size: 1.5rem;
  cursor: pointer;
  display: none;
  z-index: 1000;
  box-shadow: 0 4px 6px var(--shadow);
  transition: all 0.3s;
`;

document.body.appendChild(backToTop);

window.addEventListener('scroll', () => {
  if (window.scrollY > 300) {
    backToTop.style.display = 'block';
  } else {
    backToTop.style.display = 'none';
  }
});

backToTop.addEventListener('click', () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  });
});

// Print current page info
console.log('Sands Automation Framework Documentation');
console.log('Version: 1.0');
console.log('Theme:', savedTheme);
