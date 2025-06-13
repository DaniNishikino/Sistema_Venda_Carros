# Sistema de Venda de Carros

## Visão Geral do Projeto

Este projeto é uma API robusta e completa para um sistema de venda de carros, desenvolvida em Java com o framework Spring Boot. A aplicação oferece funcionalidades RESTful para gerenciamento de usuários, veículos e vendas, além de renderizar páginas HTML dinâmicas utilizando Thymeleaf para uma experiência de usuário rica e interativa. O sistema foi projetado para ser escalável e seguro, incorporando autenticação e autorização baseadas em papéis (GERENTE, VENDEDOR, CLIENTE) através do Spring Security. A persistência de dados é realizada com JPA e PostgreSQL, e o ambiente de desenvolvimento pode ser facilmente configurado utilizando Docker e Docker Compose.

O objetivo principal deste projeto é fornecer uma solução abrangente para concessionárias ou vendedores de carros, permitindo o cadastro e a gestão de veículos, o registro de vendas e o controle de acesso de diferentes tipos de usuários. A arquitetura do projeto segue as melhores práticas de desenvolvimento, com camadas bem definidas para Model, Repository, Service e Controller, garantindo modularidade e facilidade de manutenção.

## Funcionalidades

O Sistema de Venda de Carros oferece as seguintes funcionalidades principais:

### Gestão de Usuários

- **Cadastro de Usuários:** Permite o registro de novos usuários com diferentes papéis (GERENTE, VENDEDOR, CLIENTE).

- **Autenticação e Autorização:** Controle de acesso baseado em Spring Security, garantindo que apenas usuários autorizados possam acessar determinadas funcionalidades e endpoints.

- **Perfis de Usuário:** Cada usuário possui um perfil com informações como nome, login e senha. Gerentes podem cadastrar outros usuários, enquanto vendedores e clientes têm acesso restrito às suas próprias informações ou funcionalidades específicas.

- **Atualização de Dados:** Usuários podem atualizar suas informações de perfil.

- **Exclusão de Usuários:** Gerentes podem excluir usuários, com validação para evitar a exclusão de usuários que possuem vendas associadas.

### Gestão de Veículos

- **Cadastro de Veículos:** Permite o registro de novos veículos com detalhes como marca, modelo, ano de fabricação, ano do modelo, cor, placa, preço, descrição e status de disponibilidade.

- **Listagem de Veículos:** Visualização de todos os veículos cadastrados, com opções de busca e filtragem.

- **Busca por Placa:** Capacidade de buscar um veículo específico pela sua placa.

- **Atualização de Veículos:** Gerentes podem atualizar as informações de veículos existentes.

- **Exclusão de Veículos:** Gerentes podem remover veículos do sistema.

### Gestão de Vendas

- **Realização de Vendas:** Permite registrar a venda de um veículo para um cliente, associando o veículo, o usuário (vendedor) e o valor da venda.

- **Registro de Data da Venda:** A data da venda é automaticamente registrada.

- **Associação de Veículo e Usuário:** Cada venda é vinculada a um veículo e a um usuário específico.

### Interface do Usuário (Thymeleaf)

- **Página Inicial (Home):** Exibe uma lista de veículos disponíveis para venda.

- **Página de Login:** Interface para autenticação de usuários.

- **Página de Registro:** Permite que novos usuários se cadastrem como clientes.

- **Páginas de Administração de Usuários:** Interfaces para gerentes cadastrarem, listarem e gerenciarem usuários.

- **Página de Perfil do Usuário:** Exibe as informações do usuário logado e permite a edição.

- **Página de Acesso Negado:** Exibida quando um usuário tenta acessar uma funcionalidade sem permissão.

## Tecnologias Utilizadas

O projeto foi desenvolvido utilizando as seguintes tecnologias:

- **Java 21:** Linguagem de programação principal.

- **Spring Boot 3.5.0:** Framework para construção de aplicações Java robustas e escaláveis.

- **Spring Data JPA:** Para persistência de dados e interação com o banco de dados.

- **Spring Security:** Para autenticação e autorização, garantindo a segurança da aplicação.

- **Thymeleaf:** Motor de template para renderização de páginas HTML dinâmicas no lado do servidor.

- **PostgreSQL:** Banco de dados relacional utilizado para armazenar as informações do sistema.

- **Maven:** Ferramenta de automação de build e gerenciamento de dependências.

- **Lombok:** Biblioteca para reduzir o código boilerplate em classes Java.

- **MapStruct:** Gerador de código para mapeamento de objetos, facilitando a conversão entre entidades e DTOs.

- **Docker e Docker Compose:** Para orquestração de contêineres, facilitando a configuração do ambiente de desenvolvimento e implantação.

- **HTML, CSS, JavaScript:** Para o desenvolvimento das interfaces de usuário.

- **Bootstrap 5.3.2:** Framework CSS para design responsivo e componentes de UI.

## Como Configurar e Rodar o Projeto

Para configurar e rodar este projeto em sua máquina local, siga os passos abaixo:

### Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas:

- **Java Development Kit (JDK) 21** ou superior.

- **Maven** (gerenciador de dependências do projeto).

- **Docker** e **Docker Compose** (para o banco de dados PostgreSQL).

### 1. Clonar o Repositório

Primeiro, clone o repositório do GitHub para o seu ambiente local:

```bash
git clone https://github.com/DaniNishikino/Sistema_Venda_Carros.git
cd Sistema_Venda_Carros
```

### 2. Configurar o Banco de Dados com Docker Compose

O projeto utiliza PostgreSQL como banco de dados. Você pode facilmente configurar uma instância do PostgreSQL usando Docker Compose. O arquivo `docker-compose.yml` já está configurado para isso.

No diretório raiz do projeto, execute o seguinte comando para iniciar o contêiner do banco de dados:

```bash
docker-compose up -d
```

Este comando irá baixar a imagem do PostgreSQL (se ainda não tiver) e iniciar um contêiner chamado `db_vehicles` na porta `5432`. As credenciais do banco de dados são:

- **Nome do Banco de Dados:** `carros`

- **Usuário:** `postgres`

- **Senha:** `postgres`

O Spring Boot configurará automaticamente a conexão com este banco de dados. As tabelas serão criadas automaticamente pelo Hibernate, mas você pode consultar o arquivo `comandos_sql.txt` para ver o esquema do banco de dados.

### 3. Rodar a Aplicação Spring Boot

Após o banco de dados estar em execução, você pode iniciar a aplicação Spring Boot. Navegue até o diretório raiz do projeto (onde está o arquivo `pom.xml`) e execute o seguinte comando Maven:

```bash
mvn spring-boot:run
```

Alternativamente, você pode construir o projeto e depois executar o arquivo JAR:

```bash
mvn clean install
java -jar target/Venda_Carros-0.0.1-SNAPSHOT.jar
```

A aplicação será iniciada e estará acessível em `http://localhost:8080` (ou na porta configurada no `application.yml`).

### 4. Acessar a Aplicação

Após a aplicação estar em execução, você pode acessar as interfaces web via Thymeleaf ou interagir com os endpoints REST:

- **Página Inicial:** `http://localhost:8080/home`

- **Página de Login:** `http://localhost:8080/login`

- **Página de Registro:** `http://localhost:8080/usuarios/registo`

## Endpoints da API REST

O projeto expõe os seguintes endpoints RESTful para interação programática com o sistema:

### Usuários (`/usuarios`)

| Método HTTP | Endpoint | Descrição | Papéis de Acesso |
| --- | --- | --- | --- |
| `GET` | `/usuarios/{login}` | Busca um usuário pelo login. | `GERENTE`, `VENDEDOR` |
| `POST` | `/usuarios` | Cria um novo usuário. | Qualquer um (para registro) |
| `PUT` | `/usuarios/{login}` | Atualiza as informações de um usuário. | Usuário logado (para perfil), `GERENTE` (para outros usuários) |
| `DELETE` | `/usuarios/{login}` | Deleta um usuário. | `GERENTE` |

### Veículos (`/veiculos`)

| Método HTTP | Endpoint | Descrição | Papéis de Acesso |
| --- | --- | --- | --- |
| `GET` | `/veiculos` | Lista todos os veículos disponíveis. | Qualquer um |
| `GET` | `/veiculos/{placa}` | Busca um veículo pela placa. | `GERENTE`, `VENDEDOR` |
| `POST` | `/veiculos` | Cadastra um novo veículo. | `GERENTE` |
| `PUT` | `/veiculos/{placa}` | Atualiza as informações de um veículo. | `GERENTE` |
| `DELETE` | `/veiculos/{placa}` | Deleta um veículo. | `GERENTE` |

### Vendas (`/vendas`)

| Método HTTP | Endpoint | Descrição | Papéis de Acesso |
| --- | --- | --- | --- |
| `POST` | `/vendas` | Realiza uma nova venda de veículo. | `VENDEDOR`, `GERENTE` |

## Telas/Views (Thymeleaf)

O projeto utiliza Thymeleaf para renderizar as seguintes páginas HTML, proporcionando uma interface de usuário completa para as operações do sistema:

- **`/`**** (Redireciona para ****`/home`****):** Redirecionamento para a página inicial.

- **`/home`****:** Página inicial que exibe uma lista de veículos disponíveis para venda. É a primeira tela que o usuário vê após o login ou ao acessar a raiz da aplicação.

- **`/login`****:** Página de formulário para autenticação de usuários. Usuários existentes podem inserir suas credenciais aqui para acessar o sistema.

- **`/usuarios/registo`****:** Formulário de registro para novos usuários, permitindo que se cadastrem com o papel de `CLIENTE`.

- **`/usuarios/admin/cadastrar`****:** Formulário de cadastro de usuários acessível apenas por `GERENTE`, permitindo a criação de novos usuários com diferentes papéis.

- **`/usuarios/perfil`****:** Exibe os detalhes do perfil do usuário logado, como nome, login e papel. Permite que o usuário visualize suas próprias informações.

- **`/usuarios/editar`****:** Formulário para o usuário logado editar suas informações de perfil.

- **`/usuarios/admin/listar`****:** Lista todos os usuários cadastrados no sistema, acessível apenas por `GERENTE`. Permite a visualização e gerenciamento de todos os usuários.

- **`/acesso-negado`****:** Página exibida quando um usuário tenta acessar uma URL ou funcionalidade para a qual não possui permissão.

Essas telas são construídas dinamicamente com o Thymeleaf, integrando dados do backend e oferecendo uma experiência de usuário fluida e responsiva.

## Estrutura do Banco de Dados

O banco de dados do sistema de venda de carros é composto por três tabelas principais: `Veiculo`, `Usuario` e `Venda`. Abaixo, detalhamos a estrutura de cada tabela e seus relacionamentos:

### Tabela `Veiculo`

Armazena informações sobre os veículos disponíveis para venda.

| Coluna | Tipo de Dados | Restrições | Descrição |
| --- | --- | --- | --- |
| `id` | `UUID` | `PRIMARY KEY`, `NOT NULL` | Identificador único do veículo. |
| `marca` | `VARCHAR(100)` | `NOT NULL` | Marca do veículo (ex: Ford, Chevrolet). |
| `modelo` | `VARCHAR(100)` | `NOT NULL` | Modelo do veículo (ex: Fusion, Onix). |
| `ano_fabricacao` | `SMALLINT` | `NOT NULL` | Ano de fabricação do veículo. |
| `ano_modelo` | `SMALLINT` | `NOT NULL` | Ano do modelo do veículo. |
| `cor` | `VARCHAR(50)` | `NOT NULL` | Cor do veículo. |
| `placa` | `VARCHAR(10)` | `NOT NULL`, `UNIQUE` | Placa do veículo, deve ser única. |
| `preco` | `NUMERIC(12,2)` | `NOT NULL`, `CHECK (preco >= 0)` | Preço de venda do veículo. |
| `descricao` | `TEXT` |  | Descrição detalhada do veículo. |
| `disponivel` | `BOOLEAN` | `DEFAULT TRUE` | Indica se o veículo está disponível para venda. |
| `data_cadastro` | `TIMESTAMPTZ` | `DEFAULT CURRENT_TIMESTAMP` | Data e hora do cadastro do veículo. |

**Índices:**

- `IDX_Veiculo_Marca_Modelo` (`marca`, `modelo`): Para otimizar buscas por marca e modelo.

### Tabela `Usuario`

Armazena informações sobre os usuários do sistema, incluindo clientes, vendedores e gerentes.

| Coluna | Tipo de Dados | Restrições | Descrição |
| --- | --- | --- | --- |
| `id` | `UUID` | `PRIMARY KEY`, `NOT NULL` | Identificador único do usuário. |
| `nome` | `VARCHAR(150)` | `NOT NULL` | Nome completo do usuário. |
| `login` | `VARCHAR(150)` | `NOT NULL`, `UNIQUE` | Nome de usuário para login, deve ser único. |
| `senha` | `VARCHAR(100)` | `NOT NULL` | Senha do usuário (geralmente armazenada de forma criptografada). |
| `papel` | `VARCHAR(20)` | `NOT NULL`, `CHECK (papel IN ('GERENTE', 'VENDEDOR', 'CLIENTE'))` | Papel do usuário no sistema, definindo suas permissões. |

### Tabela `Venda`

Registra as transações de venda de veículos.

| Coluna | Tipo de Dados | Restrições | Descrição |
| --- | --- | --- | --- |
| `id` | `UUID` | `PRIMARY KEY`, `NOT NULL` | Identificador único da venda. |
| `id_veiculo` | `UUID` | `NOT NULL`, `REFERENCES Veiculo(id)` | Chave estrangeira para o veículo vendido. |
| `id_usuario` | `UUID` | `NOT NULL`, `REFERENCES Usuario(id)` | Chave estrangeira para o usuário (vendedor) que realizou a venda. |
| `data_venda` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP` | Data e hora em que a venda foi realizada. |
| `valor_venda` | `NUMERIC(12,2)` | `NOT NULL`, `CHECK (valor_venda >= 0)` | Valor total da venda. |

**Relacionamentos:**

- `Venda` para `Veiculo`: Uma venda está associada a um único veículo (`id_veiculo`).

- `Venda` para `Usuario`: Uma venda está associada a um único usuário (`id_usuario`).

**Índices:**

- `IDX_Venda_IdVeiculo` (`id_veiculo`): Para otimizar buscas por veículo em vendas.

- `IDX_Venda_IdUsuario` (`id_usuario`): Para otimizar buscas por usuário em vendas.


