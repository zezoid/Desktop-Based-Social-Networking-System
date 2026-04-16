# Desktop-Based-Social-Networking-System
Desktop-Based Social Networking System developed using Java Swing, JDBC, and MySQL with CRUD operations and theme customization.

Social Media Platform (Java + MySQL)


🚀 Features

👤 User System

- Registration & Login
- User Profiles
- Follow / Unfollow Users

📝 Posts

- Create posts (Text / Image / Video)
- Edit & Delete posts
- View global feed

❤️ Interactions

- Like / Unlike posts
- Comment on posts
- View like & comment counts
- Delete own comments

💬 Messaging

- One-to-one chat system
- Auto-refresh chat (real-time feel)

🎨 UI Features

- Light / Dark mode
- Mobile / Desktop layout toggle
- Responsive feed design

---

🛠️ Tech Stack

- Frontend: Java Swing
- Backend: Java (JDBC)
- Database: MySQL
- IDE: VS Code / IntelliJ / Eclipse

---

📁 Project Structure

src/
 ├── dao/        # Database operations (DAO classes)
 ├── model/      # Data models
 ├── ui/         # Swing UI (Frames)
 └── util/       # DB connection & schema initializer

---

⚙️ How to Run

1️⃣ Install Requirements

- Java JDK 8+
- MySQL Server
- JDBC Driver (MySQL Connector)

---

2️⃣ Setup Database

Open MySQL and run:

CREATE DATABASE socialapp;
USE socialapp;

---

3️⃣ Configure Database

Update credentials in:

util/DBConnection.java

Example:

String url = "jdbc:mysql://localhost:3306/socialapp";
String user = "root";
String password = "your_password";

---

4️⃣ Create Tables

Run:

SchemaInitializer.createTables();

This will create:

- user
- post
- like
- comment
- follow
- message

---

5️⃣ Run Application

Run:

LoginFrame.java

---

🧪 Test Data (Optional)

You can insert sample users:

INSERT INTO user (username, email, password)
VALUES ('user1','user1@mail.com','123');

---

🗄️ Database Tables

- "user" → stores user info
- "post" → user posts (with media support)
- "like" → post likes
- "comment" → post comments
- "follow" → user relationships
- "message" → chat system

---

⚠️ Known Issues

- Media files use local file paths (not cloud-based)

---

🔮 Future Improvements

- Cloud image storage
- Notifications system
- Group chat
- REST API backend
- Mobile app version

---

👨‍💻 Author

Salman Saifi

---

📜 License

This project is for educational purposes only.
