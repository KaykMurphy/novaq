# Novaq

Novaq e uma API REST em Java com Spring Boot para e-commerce, oferecendo autenticacao JWT, catalogo de produtos, carrinho de compras, gerenciamento de pedidos e integracao com pagamento PIX via Mercado Pago.

## Visao geral

O Novaq fornece uma base segura e organizada para uma plataforma de e-commerce completa. A aplicacao possui fluxo de autenticacao, catalogo com paginacao, carrinho de compras, pedidos com controle de estoque, integracao com Mercado Pago para pagamentos PIX e documentacao interativa com Swagger.

## Funcionalidades implementadas

- Cadastro de usuarios com validacao de dados.
- Validacao de email, senha e confirmacao de senha.
- Criptografia de senha com BCrypt.
- Login com Spring Security.
- Emissao de token JWT para usuarios autenticados.
- Autenticacao de rotas privadas via filtro JWT e header `Authorization: Bearer <token>`.
- Persistencia de usuarios com Spring Data JPA.
- Banco H2 em memoria para desenvolvimento.
- Dependencia do driver PostgreSQL para evolucao em ambiente persistente.
- Tratamento global de erros da API.
- Catalogo de produtos com paginacao e busca.
- Gerenciamento de categorias.
- Carrinho de compras com adicao, consulta, remocao de itens e limpeza do carrinho.
- Controle de estoque com baixa automatica ao finalizar pedido.
- Pedidos com historico de compras e multiplos status (pendente, pago, cancelado).
- Integracao com Mercado Pago para pagamentos PIX.
- Documentacao interativa da API com Swagger UI e OpenAPI 3.
- CORS configurado para integracao com frontend.
- Imagens de produto com suporte a galeria e ordem de exibicao.
- Informacoes de frete por produto (frete gratis ou valor fixo).
- Soft delete com reutilizacao de SKU para variantes inativos.
- Inicializacao automatica de usuario admin via variaveis de ambiente.
- Endpoint GET para listagem de categorias.

## Stack utilizada

- Java 17
- Spring Boot 4
- Spring Web MVC
- Spring Security
- Spring Data JPA
- Bean Validation
- H2 Database
- PostgreSQL Driver
- Lombok
- Java JWT
- SpringDoc OpenAPI
- Mercado Pago SDK
- MapStruct
- Maven Wrapper

## Estrutura do projeto

```text
src/main/java/com/novaq
├── config        # Configuracoes de seguranca e CORS
├── controller    # Endpoints REST
├── dtos          # Objetos de entrada e saida da API
├── enums         # Enumeracoes do dominio
├── exceptions    # Tratamento global de excecoes
├── mapper        # Mapeamento entre entidades e DTOs (MapStruct)
├── model         # Entidades JPA
├── repository    # Repositorios de acesso a dados
├── service       # Regras de negocio
└── validation    # Validacoes customizadas
```

## Requisitos

- JDK 17 ou superior.
- Git.
- Terminal com permissao de execucao para o Maven Wrapper.

## Como executar localmente

Clone o repositorio:

```bash
git clone https://github.com/KaykMurphy/novaq.git
cd novaq
```

Configure as variaveis de ambiente em um arquivo `.env` na raiz do projeto:

```env
JWT_SECRET=troque-por-um-segredo-forte
JWT_EXPIRATION_HOURS=2
MP_ACCESS_TOKEN=seu-token-mercado-pago
URL=https://seu-endereco-ngrok
ADMIN_PASSWORD=sua-senha-admin
EMAIL_ADMIN=seu-email-admin
```

Execute os testes:

```bash
./mvnw test
```

Inicie a aplicacao:

```bash
./mvnw spring-boot:run
```

Por padrao, a API ficara disponivel em:

```text
http://localhost:8080
```

## Banco de dados em desenvolvimento

O projeto utiliza H2 em memoria para facilitar o desenvolvimento local.

Console H2:

```text
http://localhost:8080/h2-console
```

Configuracao padrao:

```text
JDBC URL: jdbc:h2:mem:novaqdb
User: sa
Password: password
```

## Documentacao da API (Swagger)

Apos iniciar a aplicacao, a documentacao interativa esta disponivel em:

```text
http://localhost:8080/swagger-ui/index.html
```

O schema OpenAPI pode ser acessado em:

```text
http://localhost:8080/v3/api-docs
```

## Endpoints disponiveis

### Cadastro de usuario

```http
POST /api/auth/register
```

Exemplo de requisicao:

```json
{
  "name": "Maria Silva",
  "email": "maria@email.com",
  "password": "123456",
  "confirmPassword": "123456"
}
```

### Login

```http
POST /api/auth/login
```

Exemplo de requisicao:

```json
{
  "email": "maria@email.com",
  "password": "123456"
}
```

Exemplo de resposta:

```json
{
  "token": "jwt-gerado-pela-api"
}
```

### Categorias

```http
GET /api/categories
```

Lista todas as categorias disponiveis. Endpoint publico.

```http
POST /api/categories
Authorization: Bearer <token-admin>
```

Cria uma nova categoria. Exige papel ADMIN.

### Produtos

```http
GET /api/products?page=0&size=12
```

Lista produtos com paginacao. Endpoint publico.

```http
GET /api/products/{id}
```

Retorna detalhes de um produto especifico. Endpoint publico.

```http
GET /api/products/category/{categoryId}?page=0&size=12
```

Lista produtos por categoria. Endpoint publico.

```http
POST /api/products
Authorization: Bearer <token-admin>
```

Cria um novo produto. Exige papel ADMIN. Suporta imagens e informacoes de frete.

Exemplo de requisicao:

```json
{
  "name": "Camiseta Basica",
  "description": "Camiseta 100% algodao",
  "brand": "MarcaX",
  "categoryId": "uuid-da-categoria",
  "imageUrl": "https://exemplo.com/imagem.jpg",
  "images": [
    { "url": "https://exemplo.com/img1.jpg", "position": 0 },
    { "url": "https://exemplo.com/img2.jpg", "position": 1 }
  ],
  "freeShipping": true,
  "shippingCost": 0,
  "variations": [
    { "sku": "CAM-PRE-M", "color": "Preto", "stockQuantity": 50, "price": 79.90 }
  ]
}
```

```http
PUT /api/products/{id}
Authorization: Bearer <token-admin>
```

Atualiza um produto existente. Exige papel ADMIN.

```http
DELETE /api/products/{id}
Authorization: Bearer <token-admin>
```

Desativa um produto (soft delete). Exige papel ADMIN.

### Carrinho — Adicionar item

```http
POST /api/cart
Content-Type: application/json
Authorization: Bearer <token>
```

Exemplo de requisicao:

```json
{
  "variantId": "uuid-da-variante",
  "quantity": 2
}
```

### Carrinho — Consultar

```http
GET /api/cart
Authorization: Bearer <token>
```

### Carrinho — Remover item

```http
DELETE /api/cart/{itemId}
Authorization: Bearer <token>
```

### Carrinho — Limpar

```http
DELETE /api/cart/clear
Authorization: Bearer <token>
```

### Pedido — Checkout

```http
POST /api/orders/checkout
Authorization: Bearer <token>
```

Exemplo de requisicao:

```json
{
  "cep": "01001-000",
  "numero": 1000,
  "complemento": "Sala 101",
  "cpf": "12345678901"
}
```

### Pedidos — Listar (Admin)

```http
GET /api/orders?page=0&size=10
Authorization: Bearer <token-admin>
```

Lista todos os pedidos com paginacao. Exige papel ADMIN.

### Webhook MercadoPago

```http
POST /api/checkout/webhook
```

Endpoint chamado pelo MercadoPago para notificar status de pagamento. Publico.

## Seguranca

A configuracao atual usa sessoes stateless, desabilita CSRF para uso como API REST e configura CORS para permitir requisicoes do frontend (`localhost:3000`). As senhas sao armazenadas com hash BCrypt e o login emite tokens JWT assinados.

Rotas publicas (sem autenticacao):

- `POST /api/auth/**` (registro e login)
- `GET /api/products/**` (catalogo)
- `GET /api/categories/**` (listagem de categorias)
- `POST /api/checkout/webhook` (notificacao MercadoPago)

Rotas que exigem papel ADMIN:

- `POST /api/products` (criar produto)
- `PUT /api/products/**` (atualizar produto)
- `DELETE /api/products/**` (desativar produto)
- `POST /api/categories` (criar categoria)
- `GET /api/orders/**` (listar pedidos)

Outras rotas exigem token JWT valido no header HTTP:

```http
Authorization: Bearer <token>
```

## Frontend

O frontend esta em desenvolvimento separado usando Next.js + TypeScript + Tailwind CSS. Consulte o repositorio do frontend para mais detalhes.

## Roadmap

- Pagamentos (Pix, cartao).
- Painel administrativo completo.
- Autorizacao por perfis de usuario.
- Configuracao de banco PostgreSQL para producao.

## Status do projeto

Projeto em desenvolvimento ativo. Conta com autenticacao JWT, catalogo de produtos com imagens e frete, categorias, carrinho de compras, pedidos com integracao PIX, documentacao Swagger e suporte a frontend via CORS.
