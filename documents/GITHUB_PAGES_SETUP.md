# GitHub Pages Setup Guide

## 🚀 Quick Deployment (5 Minutes)

### Step 1: Push Documentation to GitHub

```bash
# Navigate to your project
cd SandsAutoFramework

# Add all documentation files
git add docs/

# Commit
git commit -m "Add comprehensive documentation site"

# Push to GitHub
git push origin main
```

### Step 2: Enable GitHub Pages

1. Go to your repository on GitHub: `https://github.com/YOUR_USERNAME/SandsAutoFramework`

2. Click on **Settings** tab

3. Scroll down to **Pages** section (left sidebar)

4. Under **Source**, select:
   - Branch: `main`
   - Folder: `/docs`

5. Click **Save**

6. Wait 2-3 minutes for deployment

7. Your documentation will be live at:
   ```
   https://YOUR_USERNAME.github.io/SandsAutoFramework/
   ```

### Step 3: Verify Deployment

1. Visit your GitHub Pages URL
2. Check all pages load correctly
3. Test theme toggle
4. Test mobile responsiveness
5. Verify code copy buttons work

## 🎨 Customization Before Deployment

### Update GitHub Username

Replace `YOUR_USERNAME` in all HTML files:

```bash
# macOS/Linux
find docs/ -name "*.html" -exec sed -i '' 's/YOUR_USERNAME/your-actual-username/g' {} +

# Linux
find docs/ -name "*.html" -exec sed -i 's/YOUR_USERNAME/your-actual-username/g' {} +
```

### Update Author Name

Replace "Afsar Ali" with your name:

```bash
# macOS
find docs/ -name "*.html" -exec sed -i '' 's/Afsar Ali/Your Name/g' {} +

# Linux
find docs/ -name "*.html" -exec sed -i 's/Afsar Ali/Your Name/g' {} +
```

### Update Repository URL

Edit `pom.xml` and update:
```xml
<url>https://github.com/YOUR_USERNAME/SandsAutoFramework</url>
```

## 🌐 Custom Domain Setup (Optional)

### Step 1: Add CNAME File

```bash
echo "docs.yourframework.com" > docs/CNAME
git add docs/CNAME
git commit -m "Add custom domain"
git push
```

### Step 2: Configure DNS

Add these DNS records at your domain provider:

**For Apex Domain (yourframework.com):**
```
Type: A
Name: @
Value: 185.199.108.153
Value: 185.199.109.153
Value: 185.199.110.153
Value: 185.199.111.153
```

**For Subdomain (docs.yourframework.com):**
```
Type: CNAME
Name: docs
Value: YOUR_USERNAME.github.io
```

### Step 3: Enable HTTPS

1. Go to repository Settings → Pages
2. Check "Enforce HTTPS"
3. Wait for SSL certificate provisioning (can take up to 24 hours)

## 📱 Testing Locally

Before deploying, test locally:

### Using Python
```bash
cd docs
python3 -m http.server 8000
```

### Using Node.js
```bash
npx http-server docs -p 8000
```

### Using PHP
```bash
cd docs
php -S localhost:8000
```

Then open: http://localhost:8000

## ✅ Pre-Deployment Checklist

- [ ] All HTML files created
- [ ] CSS and JS files in place
- [ ] GitHub username updated in all files
- [ ] Author name updated
- [ ] Repository URLs updated
- [ ] Tested locally
- [ ] All links working
- [ ] Mobile responsive
- [ ] Theme toggle working
- [ ] Code copy buttons working
- [ ] Search functionality working

## 🔧 Troubleshooting

### Issue: 404 Page Not Found

**Solution:**
- Ensure `/docs` folder is selected in GitHub Pages settings
- Check that `index.html` exists in docs folder
- Wait 2-3 minutes after enabling Pages

### Issue: CSS/JS Not Loading

**Solution:**
- Check file paths are relative (not absolute)
- Verify `css/style.css` and `js/main.js` exist
- Clear browser cache

### Issue: Custom Domain Not Working

**Solution:**
- Verify DNS records are correct
- Wait up to 24 hours for DNS propagation
- Check CNAME file contains correct domain
- Ensure "Enforce HTTPS" is enabled

### Issue: Theme Not Persisting

**Solution:**
- Check browser allows localStorage
- Test in different browser
- Check JavaScript console for errors

## 📊 Analytics Setup (Optional)

### Google Analytics

Add before `</head>` in all HTML files:

```html
<!-- Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=GA_MEASUREMENT_ID"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());
  gtag('config', 'GA_MEASUREMENT_ID');
</script>
```

### GitHub Pages Built-in Analytics

- Go to repository Insights → Traffic
- View page views and unique visitors

## 🔄 Updating Documentation

### Make Changes

1. Edit HTML files in `docs/` folder
2. Test locally
3. Commit and push:

```bash
git add docs/
git commit -m "Update documentation"
git push origin main
```

4. Changes will be live in 2-3 minutes

### Versioning

Create version tags:

```bash
git tag -a v1.0 -m "Documentation v1.0"
git push origin v1.0
```

## 📢 Sharing Your Documentation

### Add Badge to README

```markdown
[![Documentation](https://img.shields.io/badge/docs-live-brightgreen)](https://YOUR_USERNAME.github.io/SandsAutoFramework/)
```

### Social Media

Share your documentation URL:
```
https://YOUR_USERNAME.github.io/SandsAutoFramework/
```

### Team Access

Share the URL with your team. No authentication required - it's public!

## 🎯 Next Steps

1. ✅ Deploy to GitHub Pages
2. ✅ Test all functionality
3. ✅ Share with team
4. ✅ Add remaining pages (API, Mobile, Desktop, etc.)
5. ✅ Set up custom domain (optional)
6. ✅ Add analytics (optional)
7. ✅ Collect feedback
8. ✅ Iterate and improve

## 📞 Support

If you encounter issues:

1. Check GitHub Pages status: https://www.githubstatus.com/
2. Review GitHub Pages documentation: https://docs.github.com/en/pages
3. Open an issue in your repository
4. Check browser console for JavaScript errors

## 🎉 Success!

Once deployed, your documentation will be:
- ✅ Publicly accessible
- ✅ Mobile-friendly
- ✅ Searchable
- ✅ Professional
- ✅ Easy to maintain

---

**Happy Documenting! 📚**
