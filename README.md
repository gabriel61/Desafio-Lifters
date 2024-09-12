# Sistema de Eleição - Desafio para a empresa Lifters

## Descrição

O Sistema de Eleição é uma aplicação desenvolvida para gerenciar o processo eleitoral. Ele permite a criação e gerenciamento de eleitores, candidatos e sessões de votação. Os principais recursos incluem:

- CRUD para Eleitores, Candidatos e Cargos
- Abertura e Fechamento de Sessões de Votação
- Registro e Validação de Votos
- Geração de Boletim de Urna

## Tecnologias Utilizadas

- **Spring Boot 3.3.3**: Framework para desenvolvimento de aplicações Java.
- **Spring Data JPA**: Para persistência de dados com JPA e Hibernate.
- **MySQL 8**: Banco de dados relacional utilizado para armazenar os dados da aplicação.
- **Lombok**: Biblioteca para reduzir o boilerplate code.
- **JUnit**: Framework para testes unitários.

- **IntelliJ IDEA**: IDE utilizada para escrever, compilar e executar o código Java da aplicação.
- **Insomnia**: Utilizado para testar e consumir as APIs REST, permitindo verificar o comportamento dos endpoints e validar as funcionalidades implementadas.


## Requisitos para Executar o Projeto

1. **Java Development Kit (JDK) 17**: Certifique-se de ter o JDK 17 instalado.
2. **MySQL**: Instale o MySQL e crie um banco de dados chamado `eleicao`.
3. **Maven**: Utilize o Maven para gerenciar as dependências do projeto e para construção do projeto.

### Configuração do Banco de Dados

No arquivo `src/main/resources/application.properties`, configure as credenciais do banco de dados:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/eleicao
spring.datasource.username=root
spring.datasource.password=admin
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### Estrutura do Projeto 

* **Modelos**: Contêm as entidades da aplicação, como `Eleitor`, `Candidato`, `Sessao`, `Cargo` e `Voto`.
* **Repositórios**: Interfaces para acesso aos dados das entidades.
* **Serviços**: Contêm a lógica de negócios e regras de validação.
* **Controladores**: Endpoints REST para interagir com a aplicação.
* **Services**: `CandidatoService`, `EleitorService`, `SessaoService`, `CargoService`, `VotoService` e `BoletimService` para a lógica de negócios.

## Endpoints da API

- **CRUD Eleitores**:
  - `GET /eleitores` - Lista todos os eleitores.
  - `GET /eleitores/{id}` - Busca eleitor por ID.
  - `POST /eleitores` - Cria um novo eleitor.
  - `PUT /eleitores/{id}` - Atualiza um eleitor existente.
  - `DELETE /eleitores/{id}` - Deleta um eleitor.

- **CRUD Candidatos**:
  - `GET /candidatos` - Lista todos os candidatos.
  - `GET /candidatos/{id}` - Busca candidato por ID.
  - `POST /candidatos` - Cria um novo candidato.
  - `PUT /candidatos/{id}` - Atualiza um candidato existente.
  - `DELETE /candidatos/{id}` - Deleta um candidato.

- **CRUD Cargos**:
  - `GET /cargos` - Lista todos os cargos.
  - `GET /cargos/{id}` - Busca cargo por ID.
  - `POST /cargos` - Cria um novo cargo.
  - `PUT /cargos/{id}` - Atualiza um cargo existente.
  - `DELETE /cargos/{id}` - Deleta um cargo.

- **Sessões**:
  - `POST /sessao/abrir` - Abre uma nova sessão de votação.
  - `PATCH /sessao/fechar/{id}` - Fecha uma sessão de votação.

- **Votação**:
  - `POST /eleitores/{id}/votar` - Registra um voto de um eleitor.

- **Boletim de Urna**:
  - `GET /boletim-urna/{idSessao}` - Gera o boletim de urna para uma sessão encerrada.

## DatabaseInitializer

O `DatabaseInitializer` é uma classe de inicialização usada para popular automaticamente o banco de dados da aplicação com dados iniciais, como eleitores, candidatos e sessões de votação. Ao iniciar a aplicação, essa classe é executada automaticamente, criando registros fictícios para simular um cenário de eleição.

1. **Criação de Eleitores e Candidatos**: Gera uma lista de eleitores e candidatos com dados fictícios, facilitando o desenvolvimento e testes sem a necessidade de inserir dados manualmente.

2. **Abertura de Sessão e Registro de Votos**: Abre uma sessão de votação associada a um cargo específico e, durante o período em que a sessão está aberta, registra votos de maneira aleatória entre os candidatos existentes. Cada voto é distribuído aleatoriamente, simulando o comportamento de uma eleição real onde os eleitores escolhem seus candidatos preferidos.

3. **Fechamento de Sessão e Geração do Boletim de Votação**: Após fechar a sessão, o `DatabaseInitializer` utiliza o `BoletimService` para gerar um boletim de votação detalhado, que inclui o número de votos para cada candidato, o total de votos e o vencedor da eleição, garantindo uma visão clara dos resultados.

## Como Executar o Projeto

1. Clone o repositório:
    
    ```bash
    git clone https://github.com/gabriel61/Desafio-Lifters.git
    ```
    
2. Navegue até o diretório do projeto:
    
    ```bash
    cd Desafio-Lifters
    ```
    
3. Compile e execute o projeto usando Maven:
    
    ```bash
    mvn spring-boot:run
    ```
    
4. A aplicação estará rodando em `http://localhost:8080`.

## Testes
Para rodar os testes, utilize o Maven:

```bash
    mvn test
```

## Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

</br>

## ✒️ Autor

</br>

<a href="https://github.com/gabriel61">
 <img style="border-radius: 50%;" src="https://avatars.githubusercontent.com/gabriel61" width="100px;" alt=""/>
 <br />
 
 [![Linkedin Badge](https://img.shields.io/badge/-gabrielsampaio-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/gabriel-oliveira-852759190/)](https://www.linkedin.com/in/gabriel-oliveira-852759190/)
<br>
sogabris@gmail.com
<br>
