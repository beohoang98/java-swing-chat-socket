# Xây dựng Ứng dụng chat bằng Java Socket và Java Swing

## 0. Yêu cầu môi trường

- JRE và JDK >= 1.8

## 1. Chạy ứng dụng

Ứng dụng được đóng gói trong thư mục **jar**:

```
jar/
----client-x.y.z-SNAPSHOT-all.jar
----server-x.y.z-SNAPSHOT-all.jar
```

1.1 Chạy Server

```bash
java -jar server-x.y.z-SNAPSHOT-all.jar
```

Server socket sẽ lắng nghe ở port `8000`

> File Database và log sẽ được lưu cùng thư mục 

1.2 Chạy Giao diện client

```bash
java -jar client-x.y.z-SNAPSHOT-all.jar
```

## 2. Cài đặt và chạy từ mã nguồn

Project được phát triển và build bằng Netbeans IDE.

Tuy nhiên ta vẫn có thể build và run được bằng Gradle.

**2.1 Yêu cầu:**

- JDK 1.8+
- Gradle

**2.2 Run:**

```bash
# server
./gradlew :server:run

# client
./gradlew :client:run
```

**2.3 Build fat-jar:**

```bash
# server
./gradlew :server:shadowJar

# client
./gradlew :client:shadowJar
```

## 3 Demo

[Youtube](https://youtu.be/EtJRlz9ulB8)
