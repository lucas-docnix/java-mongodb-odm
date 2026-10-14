# java-mongodb-odm

API REST com operações CRUD completas (POST, GET, PUT e DELETE) para as entidades **Estudante** e **Faculdade**, usando **Spring Data MongoDB** como ODM (Object-Document Mapper).

Cada estudante pode estar vinculado a uma faculdade. O vínculo é feito por referência: o documento do estudante guarda apenas o `id` da faculdade, e o Spring carrega a faculdade completa ao ler o estudante.

## Tecnologias

- Java 17
- Spring Boot 3.3 (Web + Data MongoDB)
- MongoDB
- Maven

## Estrutura

```
src/main/java/br/ufg/inf/
├── App.java                          # classe principal
├── model/
│   ├── Estudante.java                # coleção "estudante"
│   └── Faculdade.java                # coleção "faculdade"
├── repository/
│   ├── EstudanteRepository.java
│   └── FaculdadeRepository.java
└── controller/
    ├── EstudanteController.java      # rotas /estudantes
    └── FaculdadeController.java      # rotas /faculdades
```

### Mapeamento ODM

| Anotação | Onde | Para quê |
|---|---|---|
| `@Document(collection = "...")` | `Estudante`, `Faculdade` | Mapeia a classe para uma coleção do MongoDB |
| `@Id` | campo `id` | Identificador do documento (`_id`) |
| `@DocumentReference` | `Estudante.faculdade` | Guarda uma referência para o documento da faculdade |

Os repositórios estendem `MongoRepository`, que já fornece `save`, `findAll`, `findById`, `existsById` e `deleteById`. O `EstudanteRepository` também declara `findByFaculdade` e `existsByFaculdade`, consultas que o Spring gera a partir do nome do método.

## Como executar

### Pré-requisitos

- Java 17+
- Maven
- MongoDB rodando em `localhost:27017` (ou Docker)

### 1. Subir o MongoDB com Docker

```bash
docker run -d --name mongo -p 27017:27017 mongo:7
```

Nas próximas vezes, basta `docker start mongo`. A conexão é configurada em `src/main/resources/application.properties` (banco `meu_banco`).

### 2. Rodar a aplicação

```bash
mvn spring-boot:run
```

A API fica disponível em `http://localhost:8080`.

## Endpoints

### Faculdades

| Método | Rota | Descrição | Respostas |
|---|---|---|---|
| POST | `/faculdades` | Cria uma faculdade | 200 |
| GET | `/faculdades` | Lista todas | 200 |
| GET | `/faculdades/{id}` | Busca por id | 200, 404 |
| GET | `/faculdades/{id}/estudantes` | Lista os estudantes da faculdade | 200, 404 |
| PUT | `/faculdades/{id}` | Atualiza | 200, 404 |
| DELETE | `/faculdades/{id}` | Remove | 204, 404, 409 se ainda houver estudantes vinculados |

### Estudantes

| Método | Rota | Descrição | Respostas |
|---|---|---|---|
| POST | `/estudantes` | Cria um estudante | 200, 400 se a faculdade informada não existir |
| GET | `/estudantes` | Lista todos | 200 |
| GET | `/estudantes/{id}` | Busca por id | 200, 404 |
| PUT | `/estudantes/{id}` | Atualiza | 200, 400, 404 |
| DELETE | `/estudantes/{id}` | Remove | 204, 404 |

O campo `faculdade` do estudante é opcional. Se for informado, o `id` precisa existir.

## Exemplos com curl

Os comandos abaixo usam a sintaxe do Bash (Linux, macOS ou Git Bash no Windows).

**Criar uma faculdade**

```bash
curl -X POST http://localhost:8080/faculdades \
-H "Content-Type: application/json" \
-d '{
        "nome": "Universidade Federal de Goiás",
        "sigla": "UFG",
        "cidade": "Goiânia"
      }'
```

A resposta traz o `id` gerado, que é usado nos próximos comandos.

**Criar um estudante vinculado à faculdade**

```bash
curl -X POST http://localhost:8080/estudantes \
-H "Content-Type: application/json" \
-d '{
        "nomeCompleto": "José da Silva",
        "dataDeNascimento": "1990-02-15",
        "matricula": 45678,
        "faculdade": { "id": "ID_DA_FACULDADE" }
      }'
```

**Consultar**

```bash
curl http://localhost:8080/estudantes
curl http://localhost:8080/estudantes/ID_DO_ESTUDANTE
curl http://localhost:8080/faculdades/ID_DA_FACULDADE/estudantes
```

**Atualizar**

```bash
curl -X PUT http://localhost:8080/estudantes/ID_DO_ESTUDANTE \
-H "Content-Type: application/json" \
-d '{
        "nomeCompleto": "José da Silva Souza",
        "dataDeNascimento": "1990-02-15",
        "matricula": 45678,
        "faculdade": { "id": "ID_DA_FACULDADE" }
      }'
```

O PUT substitui todos os campos. Se `faculdade` não for enviado, o estudante fica sem faculdade.

**Remover**

```bash
curl -i -X DELETE http://localhost:8080/estudantes/ID_DO_ESTUDANTE
curl -i -X DELETE http://localhost:8080/faculdades/ID_DA_FACULDADE
```

O `-i` mostra o código de status da resposta. Uma faculdade só pode ser removida depois que seus estudantes forem removidos; antes disso a resposta é `409 Conflict`.

## Conferindo os dados no MongoDB

```bash
docker exec -it mongo mongosh meu_banco --eval "db.estudante.find(); db.faculdade.find()"
```

No documento do estudante, o campo `faculdade` contém apenas o id da faculdade referenciada.

## Testes

```bash
mvn test
```

O teste de repositório (`EstudanteRepositoryTest`) precisa do MongoDB rodando.
