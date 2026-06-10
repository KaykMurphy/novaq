# Novaq

Novaq e uma API REST em Java com Spring Boot criada para ser a base de uma loja de vendas online. O projeto esta em fase inicial e atualmente concentra a estrutura principal de autenticacao, cadastro de usuarios e emissao de tokens JWT, preparando o backend para evoluir para funcionalidades como catalogo de produtos, carrinho, pedidos, pagamentos e administracao da loja.

## Visao geral

O objetivo do Novaq e fornecer uma base segura e organizada para uma plataforma de e-commerce. Nesta etapa, a aplicacao ja possui fluxo de criacao de usuarios, login, criptografia de senha, validacoes de entrada, persistencia com JPA e tratamento centralizado de erros.

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
- Maven Wrapper

## Estrutura do projeto

```text
src/main/java/com/novaq
├── config        # Configuracoes de seguranca
├── controller    # Endpoints REST
├── dtos          # Objetos de entrada e saida da API
├── enums         # Enumeracoes do dominio
├── exceptions    # Tratamento global de excecoes
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

## Endpoints disponiveis

### Cadastro de usuario

```http
POST /api/auth/register
```

Exemplo de requisicao:

```json
{
  "nome": "Maria Silva",
  "email": "maria@email.com",
  "senha": "123456",
  "confirmSenha": "123456"
}
```

Exemplo de resposta:

```json
{
  "id": "00000000-0000-0000-0000-000000000000",
  "nome": "Maria Silva",
  "email": "maria@email.com"
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
  "senha": "123456"
}
```

Exemplo de resposta:

```json
{
  "token": "jwt-gerado-pela-api"
}
```

## Seguranca

A configuracao atual usa sessoes stateless, desabilita CSRF para uso como API REST e libera os endpoints de autenticacao em `/api/auth/**`. As senhas sao armazenadas com hash BCrypt e o login emite tokens JWT assinados.

Rotas fora de `/api/auth/**` exigem um token JWT valido no header HTTP:

```http
Authorization: Bearer jwt-gerado-pela-api
```

O filtro JWT recupera o token desse header, valida assinatura, emissor e expiracao, carrega o usuario pelo email do token e registra a autenticacao no contexto do Spring Security.

## Roadmap

- Catalogo de produtos.
- Categorias e busca de produtos.
- Carrinho de compras.
- Pedidos e historico de compras.
- Controle de estoque.
- Pagamentos.
- Painel administrativo.
- Autorizacao por perfis de usuario.
- Configuracao de banco PostgreSQL para producao.

## Status do projeto

Projeto em desenvolvimento ativo. A base atual cobre autenticacao, usuarios e protecao JWT de rotas privadas, servindo como fundacao para a construcao dos modulos comerciais da loja online.
