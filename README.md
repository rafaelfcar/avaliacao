# Avaliação Java – Controle de Pessoa API

API REST de controle de pessoas desenvolvida em Spring Boot para avaliação.

## Tecnologias
- Java 17
- Spring Boot
- Maven

## Decisões Técnicas
- A aplicação utiliza armazenamento em memória e processamento síncrono por simplicidade e adequação ao escopo.

## Executar
mvn spring-boot:run

## Endpoints

- GET /pessoa
- GET /pessoa/{id}
- POST /pessoa
- PUT /pessoa/{id}
- PATCH /pessoa/{id}
- DELETE /pessoa/{id}

- GET /pessoa/{id}/age?output=years|months|days
- GET /pessoa/{id}/salary?output=full|min

## Observações

- Não foi utilizado banco de dados por não ser exigido no escopo da avaliação.