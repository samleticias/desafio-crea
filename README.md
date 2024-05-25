<h1 align="center">
  Desafio CREA-PI 
</h1>

Este repositório contém a API desenvolvida como parte do teste técnico para a vaga de estágio em Desenvolvimento Fullstack no CREA-PI, cujo objetivo era desenvolver uma aplicação para cadastro de profissionais e títulos.

# Tecnologias
 
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Postgresql](https://www.postgresql.org/download/)

# Práticas adotadas

- SOLID
- API REST
- Consultas com Spring Data JPA
- Injeção de Dependências
- Tratamento de exceções

# Como Executar
  ## Requistos
  - [Java Development Kit (JDK) 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
  - [Maven](https://maven.apache.org/download.cgi)
  - [Postgresql](https://www.postgresql.org/download/)
  ## Configurações
  - Clonar repositório git
  ```shell
  cd "diretório a livre escolha"
  git clone https://github.com/samleticias/desafio-crea
  ```
  - Inicie o PostgreSQL e crie uma database para a API(ex: create database desafio_crea_bd)
  - No `application.properties` (em desafio-crea/src/main/resources), atualize as configurações do banco de dados. Exemplo:
  ```
  spring.datasource.url=jdbc:postgresql://localhost:5432/desafio_crea_bd
  spring.datasource.username=seu_usuario
  spring.datasource.password=sua_senha

  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect
```

  ## Executando

``` shell
cd diretorio_do_projeto
./mvnw clean package
```
- Executar a aplicação:
```
$ java -jar target/desafio-crea-0.0.1-SNAPSHOT.jar
```

A API poderá ser acessada em [localhost:8080](http://localhost:8080).

# API Endpoints

Para fazer as requisições HTTP abaixo, foi utilizada a ferramenta [Postman](https://www.postman.com/downloads/):

## Profissionais

- Cadastrar profissional
```
POST http://localhost:8080/professional 
JSON Raw Body:

{
  "name": "string",
  "email": "string",
  "password": "string",
  "birthdate": "2024-05-25",
  "phone": "string",
  "professionalType": "REGISTERED",
  "registrationDate": "2024-05-25"
}

```

- Adicionar título a um profissional
```
POST http://localhost:8080/professional/{professionalId}/add-professional-title
JSON Raw Body:

{
    "titleId": 0
}

```

- Buscar profissional por id 
```
GET http://localhost:8080/professional/id/{id}
Response:

{
  "id": 0,
  "uniqueCode": "string",
  "name": "string",
  "email": "string",
  "password": "string",
  "birthdate": "2024-05-25",
  "phone": "string",
  "professionalType": "REGISTERED",
  "registrationStatus": "ACTIVE",
  "visaDate": "2024-05-25",
  "registrationDate": "2024-05-25",
  "titles": [
    {
      "id": 0,
      "description": "string"
    }
  ]
}
```

- Buscar profissional por código único
```
GET http://localhost:8080/professional/uniqueCode/{uniqueCode}
Response:

{
  "id": 0,
  "uniqueCode": "string",
  "name": "string",
  "email": "string",
  "password": "string",
  "birthdate": "2024-05-25",
  "phone": "string",
  "professionalType": "REGISTERED",
  "registrationStatus": "ACTIVE",
  "visaDate": "2024-05-25",
  "registrationDate": "2024-05-25",
  "titles": [
    {
      "id": 0,
      "description": "string"
    }
  ]
}
```

- Buscar profissional por email
```
GET http://localhost:8080/professional/email/{email}
Response:

{
  "id": 0,
  "uniqueCode": "string",
  "name": "string",
  "email": "string",
  "password": "string",
  "birthdate": "2024-05-25",
  "phone": "string",
  "professionalType": "REGISTERED",
  "registrationStatus": "ACTIVE",
  "visaDate": "2024-05-25",
  "registrationDate": "2024-05-25",
  "titles": [
    {
      "id": 0,
      "description": "string"
    }
  ]
}
```

- Listar profissionais
```
GET http://localhost:8080/professional
Response:

[
  {
    "id": 0,
    "uniqueCode": "string",
    "name": "string",
    "email": "string",
    "password": "string",
    "birthdate": "2024-05-25",
    "phone": "string",
    "professionalType": "REGISTERED",
    "registrationStatus": "ACTIVE",
    "visaDate": "2024-05-25",
    "registrationDate": "2024-05-25",
    "titles": [
      {
        "id": 0,
        "description": "string"
      }
    ]
  }
]
```

- Atualizar profissional
```
PUT http://localhost:8080/professional/update
Response:

{
  "id": 0,
  "uniqueCode": "string",
  "name": "string",
  "email": "string",
  "password": "string",
  "birthdate": "2024-05-25",
  "phone": "string",
  "professionalType": "REGISTERED",
  "registrationStatus": "ACTIVE",
  "visaDate": "2024-05-25",
  "registrationDate": "2024-05-25",
  "titles": [
    {
      "id": 0,
      "description": "string"
    }
  ]
}
```

- Deletar profissional por id
```
PUT http://localhost:8080/professional/delete/{id}
```

## Títulos

- Criar título
```
POST http://localhost:8080/title
JSON Raw Body:

{
  "description": "string"
}
```

- Listar títulos
```
GET http://localhost:8080/title
JSON Raw Body:

[
  {
    "id": 0,
    "description": "string"
  }
]
```

- Atualizar título
```
PUT http://localhost:8080/title/update
JSON Raw Body:

{
  "id": 0,
  "description": "string"
}
```

- Buscar título com id específico
```
GET http://localhost:8080/title/{id}
Response:

{
  "id": 0,
  "description": "string"
}
```

- Deletar título por id
```
DELETE http://localhost:8080/title/delete/{ID}
```


