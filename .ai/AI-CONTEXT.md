# NexusShop AI Rules & Security Standards

Você é um especialista em Java 21, Spring Boot 3.4+ e Spring Security.

## 1. Segurança Robusta (Mandatório)
- **OAuth2 & JWT**: O sistema deve operar como Resource Server. Use `Nimbus JWT` para decodificar tokens.
- **RBAC (Role Based Access Control)**: Sempre verifique permissões. ADMIN para catálogo/estoque, CLIENT para pedidos/carrinho.
- **Passwords**: Nunca manipule senhas em texto plano. Use BCrypt via Spring Security.
- **CORS/CSRF**: Configurar explicitamente no SecurityConfig para evitar ataques.

## 2. Arquitetura Monolítica Modular
- Respeite rigorosamente a separação:
  - `api/[domain]/controller`: Apenas validação e roteamento.
  - `api/[domain]/service`: Toda a regra de negócio e @Transactional.
  - `model/[domain]/entity`: Mapeamento JPA puro.
  - `model/[domain]/request|response`: DTOs imutáveis (use Java Records quando possível).
  - `persistence/specification`: Use para qualquer busca que tenha mais de 2 filtros.

## 3. Padrões Java 21+
- Use **Records** para DTOs.
- Use **Pattern Matching** para instâncias e switch.
- Use **Sequenced Collections** se necessário.
- Use **keywords** padrão como **final** para variaveis imutáveis, **this** para referencias a própria classe
- Use o padrão **Magic numbers** para constantes.

## 4. Performance & Qualidade
- Proibido consultas sem paginação (Pageable).
- Use `Bean Validation` (@Valid) em todos os endpoints de entrada.
- Mantenha os arquivos `package-info.java` atualizados ao criar novos pacotes.

## 5. Mapa do projeto

```tree
@Root
├── src/main/java/com/nexus/shop
│   ├── api/                 # Camada de Negócio e Exposição
│   │   ├── [domain]/        # Ex: product, auth, order, ai
│   │   │   ├── controller/  # RestControllers
│   │   │   └── service/     # Services (Interfaces e Impl)
│   ├── infra/               # Base Técnica
│   │   ├── security/        # Configuração OAuth2, JWT Converter, CORS
│   │   └── exception/       # GlobalExceptionHandler e Problem Details (RFC 7807)
│   ├── model/               # Estruturas de Dados
│   │   └── [domain]/
│   │       ├── entity/      # JPA Entities
│   │       ├── request/     # Records para entrada de dados
│   │       └── response/    # Records para saída de dados
│   │       └── dto/         # Objetos internos/comuns
│   ├── persistence/         # Acesso a Dados
│   │   ├── repository/      # Spring Data JPA Repositories
│   │   └── specification/   # JPA Specifications para filtros dinâmicos
│   ├── utils/               # Helpers e constantes
│   └── ShopApplication.java # Spring Boot Entry Point
```