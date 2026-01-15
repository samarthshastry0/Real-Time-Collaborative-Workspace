# Real-Time Collaborative Workspace

A Notion-inspired collaborative workspace application built with **Spring Boot** and **Angular**. Features workspace management, hierarchical page structures, and rich content editing with multiple block types.

## 🛠️ Tech Stack

**Backend:**
- Spring Boot 4.0.1 (Java 17)
- Spring Security + JWT Authentication
- Spring Data JPA + PostgreSQL
- WebSocket (planned for real-time collaboration)
- Maven

**Frontend:**
- Angular 18+
- Standalone Components
- RxJS for reactive programming
- SCSS styling

## 🏗️ Architecture

**Backend Structure:**
- RESTful API design
- JWT-based authentication
- Repository-Service-Controller pattern
- JPA entities with relationship mapping

**Frontend Structure:**
- Feature-based module organization
- Core services (auth, workspace, page, block)
- Route guards and HTTP interceptors
- Shared models and components

## 📊 Data Model

- **User** → owns/joins multiple workspaces
- **Workspace** → contains pages and members
- **Page** → hierarchical (parent/child), contains blocks
- **Block** → ordered content units (paragraph, headings, lists, code, quotes)

## 🚀 Setup & Run

### Prerequisites
- Java 17+
- Node.js 18+
- PostgreSQL
- Maven

### Backend
```bash
cd backend

# Set environment variables (or use .env)
export JWT_SECRET="your-secret-key"
export DB_URL="jdbc:postgresql://localhost:5433/workspace_db"
export DB_USERNAME="workspace_user"
export DB_PASSWORD="workspace_password"

./mvnw spring-boot:run
```
Runs on `http://localhost:8080`

### Frontend
```bash
cd frontend
npm install
ng serve
```
Runs on `http://localhost:4200`

### Database
```bash
# Using Docker Compose (from backend/)
docker-compose up -d
```

## 🔐 Security

- JWT token-based authentication
- Password encryption with BCrypt
- Environment variable configuration for secrets
- CORS configured for frontend origin

## 🎯 Key Features Implemented

✅ User authentication (register/login)  
✅ Workspace CRUD operations  
✅ Multi-member workspace support  
✅ Hierarchical page structure  
✅ Rich block-based content editing  
✅ RESTful API endpoints  
✅ JWT authentication flow  
✅ PostgreSQL persistence  

## 🚧 In Progress / Planned

- WebSocket for real-time collaboration
- User presence indicators
- Rich text editor integration
- Drag-and-drop functionality
- Advanced block types (images, tables, embeds)
- Search functionality
- Role-based permissions
- Comments and mentions

## 📝 API Endpoints

**Auth:** `/api/auth/register`, `/api/auth/login`  
**Workspaces:** `/api/workspaces/*`  
**Pages:** `/api/pages/*`  
**Blocks:** `/api/blocks/*`  
**Users:** `/api/users/*`  

## 🧪 Development Notes

- Spring Boot auto-configures JPA with PostgreSQL
- Frontend uses proxy configuration for API calls
- JWT tokens expire after 24 hours
- Database schema auto-updates via `spring.jpa.hibernate.ddl-auto=update`
