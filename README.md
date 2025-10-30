# 🏞️ Land Management System

Sistema de gestão territorial para análise de propriedades rurais, desenvolvido no âmbito da disciplina de Software Engineering na Università degli Studi del Sannio.

## 📊 Quality Metrics

[![Build Status](https://github.com/pprim0/ES-2025-Sannio-LandManagement/actions/workflows/ci.yml/badge.svg)](https://github.com/pprim0/ES-2025-Sannio-LandManagement/actions/workflows/ci.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=pprim0_ES-2025-Sannio-LandManagement&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=pprim0_ES-2025-Sannio-LandManagement&metric=coverage)](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=pprim0_ES-2025-Sannio-LandManagement&metric=bugs)](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=pprim0_ES-2025-Sannio-LandManagement&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=pprim0_ES-2025-Sannio-LandManagement&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

---

## 📋 Descrição

Aplicação Java para gestão e análise de propriedades territoriais da Região Autónoma da Madeira, incluindo:

- 📥 **Carregamento de dados CSV** de registos de propriedades
- 🗺️ **Análise de adjacências** entre propriedades (grafos)
- 📊 **Cálculos de áreas** por região administrativa
- 🔄 **Algoritmo de sugestão de trocas** de propriedades entre proprietários
- 📈 **Visualizações e exportações** (JSON, HTML)
- 🖥️ **Interface gráfica** com JavaFX

---

## 🎯 Objetivos do Projeto

Este projeto demonstra boas práticas de **Software Engineering**:

| Sprint | Objetivos | Status |
|--------|-----------|--------|
| **Sprint 1** (25 Out - 7 Nov) | ✅ SCM (Git workflow)<br>✅ CI/CD (GitHub Actions)<br>✅ Quality Analysis (SonarCloud)<br>✅ Testing (JUnit 5)<br>✅ License Management (MIT + SBOM) | **COMPLETO** 🎉 |
| **Sprint 2** (8 Nov - 21 Nov) | 🔄 CSV Loader<br>🔄 Property Graph<br>🔄 Area Analysis<br>🔄 JSON Export | Em progresso |
| **Sprint 3** (22 Nov - 5 Dez) | 🔄 Owner Graph<br>🔄 Exchange Algorithm<br>🔄 JavaFX UI<br>🔄 HTML Exports | Planeado |
| **Sprint 4** (6 Dez - 19 Dez) | 🔄 Advanced Features<br>🔄 Code Refactoring<br>🔄 Documentation<br>🔄 Final Report | Planeado |

---

## 🚀 Getting Started

### Pré-requisitos

- **Java 17+**
- **Maven 3.8+**
- **Git**

### Instalação

```bash
# Clonar repositório
git clone https://github.com/pprim0/ES-2025-Sannio-LandManagement.git
cd ES-2025-Sannio-LandManagement

# Compilar
mvn clean install

# Executar testes
mvn test

# Gerar relatório de coverage
mvn jacoco:report
```

### Executar a aplicação

```bash
# Via Maven
mvn javafx:run

# Via JAR
java -jar target/land-management-system-1.0.0-SNAPSHOT.jar
```

---

## 📊 Dataset

O projeto utiliza o dataset **Madeira-Moodle-1.1.csv** contendo informações de propriedades rurais da Região Autónoma da Madeira.

**Localização**: `src/main/resources/data/Madeira-Moodle-1.1.csv`

**Formato**: CSV com campos:
- `OBJECTID` - Identificador único
- `PAR_ID` - ID do prédio
- `PAR_NUM` - Número do prédio
- `Shape_Length` - Perímetro
- `Shape_Area` - Área
- `GEOMETRY` - Geometria em formato WKT
- `OWNER` - Proprietário
- `FREGUESIA` - Freguesia
- `MUNICIPIO` - Município
- `ILHA` - Ilha

---

## 🛠️ Tecnologias

### Core
- **Java 17** - Linguagem de programação
- **Maven 3.9** - Build e gestão de dependências

### Frameworks & Libraries
- **JUnit 5** (5.10.1) - Testes unitários
- **GSON** (2.10.1) - Serialização JSON
- **JGraphT** (1.5.2) - Estruturas de dados em grafo
- **Apache Commons CSV** (1.10.0) - Processamento CSV
- **JavaFX** (21.0.1) - Interface gráfica

### Quality & CI/CD
- **GitHub Actions** - CI/CD pipeline
- **SonarCloud** - Análise de qualidade de código
- **JaCoCo** (0.8.11) - Code coverage
- **Checkstyle** (3.3.1) - Code style (desativado temporariamente)
- **SpotBugs** (4.8.6.0) - Bug detection (desativado temporariamente)

---

## 📈 Estrutura do Projeto

```
ES-2025-Sannio-LandManagement/
├── .github/
│   └── workflows/
│       └── ci.yml                 # GitHub Actions CI/CD
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── es/
│   │   │       ├── Propriedade.java          # Modelo de propriedade
│   │   │       ├── CSVLoader.java            # Carregamento CSV (Sprint 2)
│   │   │       ├── GrafoAdjacencias.java     # Grafo de adjacências (Sprint 2)
│   │   │       ├── AreaPropriedades.java     # Análise de áreas (Sprint 2)
│   │   │       ├── GrafoProprietarios.java   # Grafo de proprietários (Sprint 3)
│   │   │       ├── SugestaoTroca.java        # Algoritmo de trocas (Sprint 3)
│   │   │       └── GestaoTerritorioApp.java  # Aplicação principal (Sprint 3)
│   │   └── resources/
│   │       └── data/
│   │           └── Madeira-Moodle-1.1.csv    # Dataset
│   └── test/
│       └── java/
│           └── es/
│               └── PropriedadeTest.java      # Testes unitários
├── target/                        # Build output (gitignored)
├── .gitignore
├── LICENSE                        # MIT License
├── pom.xml                        # Maven configuration
└── README.md                      # Este ficheiro
```

---

## 🧪 Testing

### Executar testes

```bash
# Todos os testes
mvn test

# Com coverage report
mvn clean test jacoco:report

# Ver relatório
open target/site/jacoco/index.html
```

### Current Coverage

- **Line Coverage:** 100%
- **Branch Coverage:** 100%
- **Tests:** 1 passing

---

## 📊 Quality Gate

O projeto mantém standards de qualidade através do SonarCloud:

- ✅ **Security:** 0 vulnerabilities
- ✅ **Reliability:** 0 bugs
- ✅ **Maintainability:** Rating A
- ✅ **Coverage:** 100%
- ✅ **Duplications:** 0.0%

[Ver análise completa no SonarCloud →](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)

---

## 🔄 CI/CD Pipeline

O projeto utiliza **GitHub Actions** para CI/CD automático:

### Workflow (`.github/workflows/ci.yml`)

```yaml
on: [push, pull_request]

jobs:
  build:
    - Checkout code
    - Setup JDK 17
    - Build with Maven
    - Run tests
    - Generate JaCoCo report
    - SonarCloud analysis
```

**Triggers:**
- Push para `main` ou `develop`
- Pull Requests para `main`

[Ver workflows →](https://github.com/pprim0/ES-2025-Sannio-LandManagement/actions)

---

## 📐 Metodologia Ágil

### Scrum Framework

- **Sprint Duration:** 2 semanas
- **Planning:** Início de cada sprint
- **Daily Standups:** (simulados via Trello)
- **Sprint Review:** Fim de cada sprint
- **Sprint Retrospective:** Lições aprendidas

### Project Management

**Trello Board:** [Ver board →](https://trello.com/b/YOUR_BOARD_ID)

**User Stories:**
- Formato: "Como [role], quero [feature], para [benefit]"
- Story Points: Fibonacci (1, 2, 3, 5, 8, 13)
- Priorização: MoSCoW (Must, Should, Could, Won't)

---

## 👨‍🎓 Autor

**Pedro Primo**  
Software Engineering - Università degli Studi del Sannio  
Academic Year: 2024/2025  
Professor: Massimiliano Di Penta

---

## 📄 Licença

Este projeto está licenciado sob a **MIT License** - ver ficheiro [LICENSE](LICENSE) para detalhes.

### MIT License Summary

✅ Uso comercial  
✅ Modificação  
✅ Distribuição  
✅ Uso privado  

⚠️ Sem garantia  
⚠️ Limitação de responsabilidade  

---

## 🔗 Links Úteis

- [GitHub Repository](https://github.com/pprim0/ES-2025-Sannio-LandManagement)
- [SonarCloud Dashboard](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)
- [GitHub Actions](https://github.com/pprim0/ES-2025-Sannio-LandManagement/actions)
- [Trello Board](https://trello.com/b/uyY3kYgO/es-2025-land-management-system)

---

## 📞 Contacto

Para questões sobre o projeto:
- **GitHub Issues:** [Criar issue](https://github.com/pprim0/ES-2025-Sannio-LandManagement/issues)
- **Email:** pedroprimo@estudante.unisannio.it

---

<div align="center">

**Desenvolvido com ❤️ para Software Engineering**

**Università degli Studi del Sannio | 2024/2025**

</div>
