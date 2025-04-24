# 🚀 GitHub API Testing – REST-assured + TestNG + Allure

This project automates GitHub REST API testing using:

- ✅ REST-assured for HTTP request testing
- ✅ TestNG for test execution and data providers
- ✅ Allure for beautiful, detailed test reports
- ✅ Spring for dependency injection
- ✅ AssertJ, JSON Schema Validator

---

## 🧪 API Endpoint Coverage

| Endpoint                    | Method | Description                   |
|-----------------------------|--------|-------------------------------|
| `/user`                     | GET    | Fetch authenticated user info |
| `/user/repos`               | POST   | Create a new repository       |
| `/repos/{login}/{repoName}` | GET    | Fetch specific repository     |
| `/repos/{login}/{repoName}` | DELETE | Delete a specific repository  |

---

## ✅ Prerequisites

- Java 17+
- Maven 3.6+
- GitHub token with necessary scopes (`repo`, `user`, etc.)
- Allure CLI installed → [Installation Guide](https://docs.qameta.io/allure/#_installing_a_commandline)

---

## 🛠️ Running Tests

### 1. Set up GitHub token

You can provide your GitHub token via `config/env.properties` file.
- github.test.user.login={login}
- github.test.user.token={token}

#### 👉 Via command line:

```bash
mvn clean verify
allure serve allure-results


