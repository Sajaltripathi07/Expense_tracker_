# 🚀 Expense Tracker Deployment Guide

## Quick Deploy Options

### Option 1: Railway (Recommended - Easiest)

1. **Sign up** at [railway.app](https://railway.app)
2. **Connect your GitHub** repository
3. **Create new project** → Deploy from GitHub repo
4. **Add MySQL database**:
   - Go to your project
   - Click "New" → "Database" → "MySQL"
   - Copy the database URL
5. **Set environment variables**:
   - `DATABASE_URL` = Your MySQL connection string
   - `DB_USERNAME` = Your database username
   - `DB_PASSWORD` = Your database password
   - `SPRING_PROFILES_ACTIVE` = prod
6. **Deploy** - Railway will automatically build and deploy!

### Option 2: Render

1. **Sign up** at [render.com](https://render.com)
2. **Create new Web Service**
3. **Connect your GitHub** repository
4. **Configure**:
   - **Build Command**: `./mvnw clean package`
   - **Start Command**: `java -jar target/Expense-Tracker-end-0.0.1-SNAPSHOT.jar`
5. **Add environment variables**:
   - `DATABASE_URL` = Your MySQL connection string
   - `DB_USERNAME` = Your database username
   - `DB_PASSWORD` = Your database password
   - `SPRING_PROFILES_ACTIVE` = prod
6. **Deploy**!

### Option 3: Heroku

1. **Install Heroku CLI**
2. **Login**: `heroku login`
3. **Create app**: `heroku create your-app-name`
4. **Add MySQL addon**: `heroku addons:create jawsdb:kitefin`
5. **Set config**: `heroku config:set SPRING_PROFILES_ACTIVE=prod`
6. **Deploy**: `git push heroku main`

## Database Setup

### For Railway/Render:
- Use their built-in MySQL databases
- Copy the connection string provided

### For Heroku:
- Use JawsDB MySQL addon
- Connection string is automatically set

## Environment Variables

Set these in your deployment platform:

```bash
DATABASE_URL=jdbc:mysql://your-host:3306/your-database
DB_USERNAME=your-username
DB_PASSWORD=your-password
SPRING_PROFILES_ACTIVE=prod
PORT=8080
```

## Local Testing Before Deployment

1. **Build the project**:
   ```bash
   ./mvnw clean package
   ```

2. **Test locally**:
   ```bash
   java -jar target/Expense-Tracker-end-0.0.1-SNAPSHOT.jar
   ```

## Troubleshooting

### Common Issues:

1. **Database Connection Error**:
   - Check your `DATABASE_URL` format
   - Ensure database is accessible from deployment platform

2. **Build Failures**:
   - Check Java version (use Java 17+)
   - Ensure all dependencies are in `pom.xml`

3. **Port Issues**:
   - Most platforms set `PORT` environment variable
   - App will use `PORT` or default to 8080

### Logs:
- **Railway**: View logs in project dashboard
- **Render**: Check logs in service dashboard
- **Heroku**: `heroku logs --tail`

## Features After Deployment

✅ **User Authentication** - Login/Signup  
✅ **Expense Management** - Add/Edit/Delete expenses  
✅ **Dashboard** - Beautiful statistics and charts  
✅ **Role-based Access** - User/Admin roles  
✅ **Responsive Design** - Works on all devices  
✅ **Real-time Updates** - Instant data changes  

## Support

If you encounter issues:
1. Check the logs in your deployment platform
2. Verify environment variables are set correctly
3. Ensure database is properly configured

Happy deploying! 🎉 