
# Gestão de Ponto

Este projeto é um sistema para gestão de jornada de trabalho desenvolvido com Java Spring. Ele inclui funcionalidades de autenticação, gerenciamento de usuários, registro de pontos e resumos de jornada.

---

## **Tecnologias Utilizadas**

- **Java 17**
- **Spring Boot 3.4.0**
- **Spring Security**
- **JPA/Hibernate**
- **Flyway** (migração de banco de dados)
- **PostgreSQL**
- **JWT** (JSON Web Tokens)

---

## **Instalação e Configuração**

### **Pré-requisitos**

- Java 17 instalado
- Maven 3.8+ instalado
- PostgreSQL instalado e configurado

---

### **Passo a Passo**

1. **Clone o repositório**

   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd gestaodeponto
   ```

2. **Criação do Banco de Dados**

   - Acesse seu PostgreSQL e crie o banco de dados:

     ```sql
     CREATE DATABASE gdp;
     ```

3. **Configuração do `application.properties`**

   No diretório `src/main/resources`, configure o arquivo `application.properties` com as seguintes informações:

   ```properties
   spring.application.name=gestaodeponto

   spring.datasource.url=jdbc:postgresql://localhost:5432/gdp
   spring.datasource.username=postgres
   spring.datasource.password=root
   spring.datasource.driver-class-name=org.postgresql.Driver

   api.security.token.secret=${JWT_SECRET:my-secret-key}

   logging.level.org.springframework.security=DEBUG
   logging.level.org.springframework.web=DEBUG
   ```

   **Notas:**
   - Certifique-se de ajustar `spring.datasource.username` e `spring.datasource.password` com as credenciais do seu banco de dados PostgreSQL.
   - Você pode substituir o valor padrão de `JWT_SECRET` configurando uma variável de ambiente `JWT_SECRET` no sistema.

4. **Migração do Banco de Dados**

   O Flyway será executado automaticamente ao iniciar a aplicação e aplicará as migrações necessárias para preparar o banco de dados.


5. **Instale as dependências**

   Utilize o Maven para instalar as dependências do projeto:

   ```bash
   mvn clean install
   ```

6. **Execute o projeto**

   Para iniciar a aplicação, execute:

   ```bash
   mvn spring-boot:run
   ```

---

### **Usuário Administrador Padrão (Seed)**

Após a execução inicial, um usuário administrador padrão será criado automaticamente no sistema com as seguintes credenciais:

- **Email:** `admin@gmail.com`
- **Password:** `admin`

**Nota:** Por segurança, altere a senha do administrador assim que possível.

---

## **Endpoints Principais**

### **Autenticação**
#### **Login** - `POST /auth/login`
- **Body (JSON):**
  ```json
  {
    "email": "usuario@example.com",
    "password": "senha123"
  }
  ```

#### **Registrar Usuário** - `POST /auth/register`

- **Descrição:**
  Cria um novo usuário no sistema. **Apenas usuários com o papel ADMIN estão autorizados a executar esta ação.**

- **Body (JSON):**
  ```json
  {
    "name": "Nome do Usuário",
    "email": "usuario@example.com",
    "password": "senha123",
    "role": "ADMIN",
    "work_schedule": "EIGHT_HOURS"
  }
  ```

### **Usuários**
#### **Listar Usuários** - `GET /users`

#### **Buscar Usuário por ID** - `GET /users/{id}`

#### **Atualizar Usuário** - `PUT /users/{id}`
- **Body (JSON):**
  ```json
  {
    "name": "Nome Atualizado",
    "email": "usuario_atualizado@example.com",
    "role": "USER",
    "work_schedule": "SIX_HOURS"
  }
  ```

#### **Deletar Usuário** - `DELETE /users/{id}`

### **Pontos de Trabalho**
#### **Registrar Ponto** - `POST /worklogs/register`

- **logType**: Tipo de ponto a ser registrado. Os valores possíveis são:
  - `"CHECK_IN"`: Entrada no trabalho
  - `"CHECK_OUT"`: Saída do trabalho
  - `"LUNCH_START"`: Início do intervalo para almoço
  - `"LUNCH_END"`: Fim do intervalo para almoço
  - `"BREAK_START"`: Início de uma pausa
  - `"BREAK_END"`: Fim de uma pausa
- **timestamp**: Data e hora do registro no formato ISO-8601 (`yyyy-MM-dd'T'HH:mm:ss`).

- **Body (JSON):**
  ```json
  {
    "logType": "CHECK_IN",
    "timestamp": "2024-12-04T08:00:00"
  }
  ```

#### **Consultar Pontos** - `GET /worklogs`

#### **Resumo de Jornada** - `GET /worklogs/summary`

---

## **Segurança**

- O projeto utiliza **JWT** para autenticação e controle de acesso.
- As permissões de endpoint são configuradas via Spring Security.

---
