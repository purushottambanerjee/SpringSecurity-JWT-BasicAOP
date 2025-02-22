**Project Overview:**
Task Management System – A simple task management app where users can create, update, delete, and view tasks. Users can log in and have roles like Admin or User. Admins can manage all tasks, while users can only manage their tasks.
**Core Features:**
User Authentication and Authorization (Spring Security + JWT)
Task CRUD Operations (REST API with Spring Data JPA)
User Roles (Admin/User Role-based access)
Advanced JPA (One-to-Many Relationship)


**Dependency**
Spring Web
Spring Data JPA
Spring Security
Spring Boot Actuator
H2 Database (for simplicity) or PostgreSQL/MySQL
Lombok (optional for reducing boilerplate)
Springfox Swagger (for API documentation)


**User Entity:**
Fields: id, username, password, role (User or Admin)
Use Spring Security for hashing passwords.
One-to-many relationship with Task.
**Task Entity:**
Fields: id, title, description, status, created_at, updated_at
Many tasks belong to one user (One-to-Many relation).
Use JPA annotations to map relationships.


**Implement JWT Authentication and Authorization:**
Create a UserDetailsService for loading user data from the database.
Implement JWT generation and validation.
Set up the SecurityConfig to protect endpoints and use JWT for stateless authentication.
Example:
/login: User login (returns JWT token).
/tasks: Secure endpoint to create, read, update, or delete tasks (accessible only after authentication).
Role-based Access Control:
Admins can manage all tasks; users can only manage their own tasks.
Use @PreAuthorize or @Secured annotations to secure endpoints.
Step 4: Implement Task CRUD Operations
Create a TaskRepository interface extending JpaRepository.

GET/task : View Tasks
POST /tasks: Create a new task.
PUT /tasks/{id}: Update an existing task.
DELETE /tasks/{id}: Delete a task.
