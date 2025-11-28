# Project Info: Enterprise CMS API

REST API xây dựng với Spring Boot 3.4, PostgreSQL, và JWT authentication. (Internal internal news management system (CMS))

## Tech Stack

- **Backend**: Spring Boot 3.4.12, Java 17
- **Database**: PostgreSQL 15
- **Security**: Spring Security + JWT (OAuth2 Resource Server)
- **Documentation**: Swagger/OpenAPI 3
- **ORM**: Spring Data JPA (Hibernate)
- **Build**: Maven

## Main Function

- Xác thực người dùng (Login/Register) với JWT
- New (CRUD)
- Category (CRUD)
- Phân quyền dựa trên Role (ADMIN, USER)
- API documentation với Swagger UI

## Setup and Run

### Yêu cầu
- Docker Desktop
- Port 8080 và 5432 trống

### Khởi động
```bash
docker compose up -d
```

### Truy cập
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html

### Dừng ứng dụng
```bash
docker compose down
```

## Các lệnh hữu ích

```bash
# Xem logs
docker compose logs -f app

# Build lại khi code thay đổi
docker compose up -d --build

# Xem trạng thái
docker compose ps

# Reset database
docker compose down -v
```

## Database

- **Host**: localhost:5432
- **Database**: webapi
- **User**: webapi_user
- **Password**: 123456

## Tài khoản mặc định

Khi khởi động lần đầu, hệ thống tự động tạo:

**Admin** 
- Username: `admin`
- Password: `admin123`

**User** 
- Username: `user`
- Password: `user123`

## Cấu trúc API

Xem chi tiết tại Swagger UI sau khi chạy ứng dụng.
