# Sands Automation Framework Documentation

This directory contains the complete documentation for the Sands Automation Framework, designed to be hosted on GitHub Pages.

## 📚 Documentation Structure

- **index.html** - Home page with overview and quick start
- **getting-started.html** - Installation guide and first test creation
- **architecture.html** - Framework architecture and design patterns
- **web-automation.html** - Selenium and Playwright automation guide
- **api-automation.html** - REST and SOAP API testing guide
- **mobile-automation.html** - Android and iOS automation guide
- **desktop-automation.html** - Windows desktop automation guide
- **configuration.html** - Configuration management guide
- **reporting.html** - Reporting and logging guide
- **utilities.html** - Utility classes and helpers
- **examples.html** - Complete code examples and scenarios
- **api-reference.html** - API reference documentation

## 🚀 Hosting on GitHub Pages

### Option 1: Using /docs folder (Recommended)

1. Push the docs folder to your repository
2. Go to repository Settings → Pages
3. Select "Deploy from a branch"
4. Choose "main" branch and "/docs" folder
5. Click Save
6. Your site will be available at: `https://YOUR_USERNAME.github.io/SandsAutoFramework/`

### Option 2: Using gh-pages branch

```bash
# Create gh-pages branch
git checkout --orphan gh-pages

# Copy docs content to root
cp -r docs/* .

# Commit and push
git add .
git commit -m "Deploy documentation"
git push origin gh-pages

# Go to Settings → Pages and select gh-pages branch
```

## 🎨 Features

- ✅ Responsive design (mobile-friendly)
- ✅ Dark/Light theme toggle
- ✅ Search functionality
- ✅ Code syntax highlighting
- ✅ Copy-to-clipboard for code blocks
- ✅ Smooth scrolling navigation
- ✅ Mobile hamburger menu
- ✅ Back-to-top button
- ✅ Professional styling

## 🛠️ Local Development

To test locally:

```bash
# Using Python
cd docs
python3 -m http.server 8000

# Using Node.js
npx http-server docs -p 8000

# Using PHP
cd docs
php -S localhost:8000
```

Then open: http://localhost:8000

## 📝 Customization

### Update Branding

Edit `docs/index.html` and other HTML files:
- Replace "Afsar Ali" with your name
- Update GitHub username in URLs
- Modify color scheme in `docs/css/style.css`

### Add New Pages

1. Create new HTML file in `docs/` folder
2. Copy structure from existing page
3. Add navigation link in sidebar
4. Update `docs/js/main.js` if needed

### Modify Theme Colors

Edit `docs/css/style.css`:

```css
:root {
  --primary: #2563eb;        /* Primary color */
  --secondary: #10b981;      /* Secondary color */
  --bg: #ffffff;             /* Background */
  --text: #1f2937;           /* Text color */
}
```

## 📦 Files Structure

```
docs/
├── css/
│   └── style.css          # Main stylesheet
├── js/
│   └── main.js            # JavaScript functionality
├── images/                # Images (if any)
├── index.html             # Home page
├── getting-started.html   # Getting started guide
├── architecture.html      # Architecture documentation
├── web-automation.html    # Web automation guide
├── api-automation.html    # API automation guide
├── mobile-automation.html # Mobile automation guide
├── desktop-automation.html# Desktop automation guide
├── configuration.html     # Configuration guide
├── reporting.html         # Reporting guide
├── utilities.html         # Utilities documentation
├── examples.html          # Code examples
├── api-reference.html     # API reference
└── README.md              # This file
```

## 🔗 External Dependencies

- Font Awesome 6.4.0 (CDN) - Icons
- No other external dependencies

## 📄 License

Apache License 2.0

## 👤 Author

Afsar Ali

## 🤝 Contributing

To contribute to documentation:

1. Fork the repository
2. Create a feature branch
3. Make your changes in the `docs/` folder
4. Test locally
5. Submit a pull request

## 📞 Support

For issues or questions:
- Open an issue on GitHub
- Check existing documentation
- Review code examples

---

**Last Updated:** January 2025
