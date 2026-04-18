# NexusShop - Backend

Backend do sistema NexusShop, uma aplicação monolítica modular construída com Java e Spring Boot, com foco em performance, segurança e personalização da experiência do usuário.

---

# Tecnologias e Dependências

## Requisitos obrigatórios

Antes de executar o projeto, é necessário ter instalado:

* Java 21
* Maven 3.9 ou superior
* PostgreSQL
* Git

---

## Dependências principais (Spring Boot)

O projeto utiliza as seguintes dependências:

* spring-boot-starter-web
* spring-boot-starter-data-jpa
* spring-boot-starter-security
* spring-boot-starter-oauth2-client
* spring-boot-starter-oauth2-resource-server
* spring-boot-starter-validation
* spring-boot-devtools
* spring-boot-starter-actuator
* lombok

---

## Dependências adicionais

* Biblioteca JWT (ex: jjwt)
* Driver PostgreSQL
* Springdoc OpenAPI (Swagger)
* Cache (Caffeine ou Redis, opcional)

---

# Configuração do Ambiente

## Banco de Dados

Crie o banco de dados:

```sql id="sql001"
CREATE DATABASE nexusshop;
```

Configure no arquivo application.properties ou application.yml:

```properties id="prop001"
spring.datasource.url=jdbc:postgresql://localhost:5432/nexusshop
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## Variáveis de configuração (IGNORAR POR AGORA)

Defina as seguintes propriedades:

```properties id="prop002"
jwt.secret=sua_chave_secreta

spring.security.oauth2.client.registration.google.client-id=SEU_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=SEU_CLIENT_SECRET
```

---

# Como rodar o projeto

## Clonar repositório

```bash id="cmd001"
git clone https://github.com/seu-usuario/nexusshop.git
cd nexusshop
```

## Executar com Maven

```bash id="cmd002"
./mvnw spring-boot:run
```

ou

```bash id="cmd003"
mvn spring-boot:run
```

---

## Acesso à aplicação

A API estará disponível em:

```
http://localhost:8080
```

---

# Como testar a API

## Swagger (OpenAPI)

Após iniciar o projeto, acesse:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Testes manuais

Você pode utilizar ferramentas como Postman ou Insomnia para testar os endpoints.

### Autenticação

* Login com email e senha (retorna JWT)
* Login com Google OAuth2

### Catálogo

* GET /products
* GET /products/{id}
* POST /products

### Carrinho e pedidos

* POST /cart
* POST /orders

---

# Autenticação

O sistema utilizará:

* JWT (stateless)
* OAuth2 (Google)

Para acessar endpoints protegidos, envie o token no header:

```
Authorization: Bearer SEU_TOKEN
```

---

# Estrutura do Projeto

```text id="tree001"
src/main/java/com/nexusshop
├── config/
├── controllers/
├── dtos/
├── entities/
├── exceptions/
├── repositories/
├── services/
└── security/
```

---

# Funcionalidades principais

* Autenticação com JWT e OAuth2
* CRUD de produtos e categorias
* Sistema de pedidos
* Rastreamento de atividade do usuário
* Motor de recomendação baseado em comportamento
* Filtros dinâmicos com JPA Specifications
* Paginação

---

# Build do projeto

Para compilar o projeto:

```bash id="cmd004"
mvn clean install
```

O arquivo gerado estará em:

```
target/nexusshop.jar
```

---

# Executar o JAR

```bash id="cmd005"
java -jar target/nexusshop.jar
```

---

# Executar testes

```bash id="cmd006"
mvn test
```

---

# Observações

* Arquitetura monolítica modular c/ funções de multi-treading
* Preparado para evolução com cache e mensageria
* Adequado para aplicações de médio porte
