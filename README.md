# SkillWorker Backend - Setup Instructions

## 🔧 Configuration Setup

### 1. Environment Configuration
Before running the application, you need to set up your environment variables or create a local configuration file.

#### Option A: Environment Variables (Recommended)
Set these environment variables in your system or IDE:
```
JWT_SECRET=your_actual_jwt_secret_here
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
CLOUDINARY_CLOUD_NAME=your_cloudinary_cloud_name
CLOUDINARY_API_KEY=your_cloudinary_api_key
CLOUDINARY_API_SECRET=your_cloudinary_api_secret
PAYHERE_MERCHANT_ID=your_payhere_merchant_id
PAYHERE_MERCHANT_SECRET=your_payhere_merchant_secret
```

#### Option B: Local Properties File
1. Copy `application-template.properties` to `application-local.properties`
2. Replace all placeholder values with your actual credentials
3. Run with profile: `--spring.profiles.active=local`

### 2. Recent Fixes Applied

✅ **Bean Conflict Resolved**: Fixed duplicate `passwordEncoder` bean definition between `SecurityConfig` and `AppConfig`

✅ **Git Security**: Removed sensitive OAuth credentials from version control

### 3. Running the Application

#### Using IDE:
- Set environment variables in your IDE run configuration
- Run `SkillWorkerApplication.java`

#### Using Command Line:
```bash
# Set environment variables first, then:
./mvnw spring-boot:run
```

### 4. Database Setup
Ensure MySQL is running with:
- Database: `skill_worker_db` (auto-created)
- Username: `root`
- Password: `mysql`

## 🔒 Security Notes
- Never commit real OAuth credentials to Git
- Use environment variables or encrypted configuration in production
- The `application-template.properties` shows the required configuration structure
