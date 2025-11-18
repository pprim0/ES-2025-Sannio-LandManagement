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
- **JTS** (1.19.0) - Geometria e processamento espacial

### Quality & CI/CD
- **GitHub Actions** - CI/CD pipeline
- **SonarCloud** - Análise de qualidade de código
- **JaCoCo** (0.8.11) - Code coverage
- **CycloneDX** (2.7.11) - SBOM generation
- **License Maven Plugin** (2.4.0) - License management
- **Checkstyle** (3.4.0) - Code style (temporariamente desativado)

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
│   │   │       ├── CSVLoader.java            # Carregamento CSV
│   │   │       ├── GrafoAdjacencias.java     # Grafo de adjacências
│   │   │       ├── AreaPropriedades.java     # Análise de áreas
│   │   │       ├── AreaAvancada.java         # Cálculo avançado (componentes conexas)
│   │   │       ├── GrafoProprietarios.java   # Grafo de proprietários
│   │   │       ├── SugestaoTroca.java        # Algoritmo de trocas
│   │   │       ├── ExportadorJSON.java       # Exportação JSON
│   │   │       ├── ExportadorAdjacenciasHTML.java  # Exportação HTML (adjacências)
│   │   │       ├── ExportadorProprietariosHTML.java # Exportação HTML (proprietários)
│   │   │       ├── TestarExportadores.java   # Testes de exportação
│   │   │       └── GestaoTerritorioApp.java  # Aplicação principal (JavaFX)
│   │   └── resources/
│   │       └── data/
│   │           └── Madeira-Moodle-1.1.csv    # Dataset
│   └── test/
│       └── java/
│           └── es/
│               ├── PropriedadeTest.java      # Testes unitários
│               ├── AreaAvancadaTest.java     # Testes área avançada
│               ├── TestarExportadoresTest.java # Testes exportadores
│               └── ... (92 testes no total)
├── target/                        # Build output (gitignored)
│   ├── bom.json                   # SBOM (CycloneDX JSON)
│   ├── bom.xml                    # SBOM (CycloneDX XML)
│   └── generated-sources/
│       └── license/
│           └── THIRD-PARTY.txt    # Third-party licenses
├── .gitignore
├── LICENSE                        # MIT License
├── NOTICE                         # Third-party attributions
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

- **Line Coverage:** 80.6%
- **Branch Coverage:** ~75%
- **Tests:** 92 passing
- **Test Suites:** 8

### Test Structure

```
es/
├── PropriedadeTest.java (10 tests)
├── CSVLoaderTest.java (8 tests)
├── GrafoAdjacenciasTest.java (12 tests)
├── AreaPropriedadesTest.java (10 tests)
├── AreaAvancadaTest.java (8 tests)
├── SugestaoTrocaTest.java (15 tests)
├── ExportadorJSONTest.java (10 tests)
└── TestarExportadoresTest.java (19 tests)
```

---

## 📊 Quality Gate

O projeto mantém standards de qualidade através do SonarCloud:

| Metric | Status | Value |
|--------|--------|-------|
| **Quality Gate** | ✅ Passed | Grade A |
| **Security** | ✅ | 0 vulnerabilities |
| **Reliability** | ✅ | 0 bugs |
| **Maintainability** | ✅ | Rating A |
| **Coverage** | ✅ | 80.6% |
| **Duplications** | ✅ | 3.5% |
| **Code Smells** | ⚠️ | 203 (baixa prioridade) |

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
    - Cache Maven packages
    - Build with Maven
    - Run tests
    - Generate JaCoCo report
    - SonarCloud analysis
    - Upload artifacts
```

**Triggers:**
- Push para `main` ou `develop`
- Pull Requests para `main`

**Artifacts gerados:**
- JAR executável
- JaCoCo coverage report
- Test results
- SBOM (bom.json, bom.xml)

[Ver workflows →](https://github.com/pprim0/ES-2025-Sannio-LandManagement/actions)

---

## 📜 License & SBOM Management

### License

Este projeto está licenciado sob a **MIT License** - ver ficheiro [LICENSE](LICENSE) para detalhes.

#### MIT License Summary

✅ Uso comercial  
✅ Modificação  
✅ Distribuição  
✅ Uso privado  

⚠️ Sem garantia  
⚠️ Limitação de responsabilidade  

### Software Bill of Materials (SBOM)

O projeto gera automaticamente um SBOM em formato **CycloneDX 1.5** com informação completa sobre:
- Todas as dependências do projeto
- Versões exatas utilizadas
- Licenças de cada dependência
- Hierarquia de dependências transitivas

**Formatos disponíveis:**
- 📄 [SBOM JSON](target/bom.json) - Formato CycloneDX JSON
- 📄 [SBOM XML](target/bom.xml) - Formato CycloneDX XML

#### Gerar SBOM

```bash
# Gerar SBOM (executado automaticamente em mvn package)
mvn cyclonedx:makeAggregateBom

# Output:
# - target/bom.json
# - target/bom.xml
```

### Third-Party Licenses

Lista completa de licenças de dependências:
- 📄 [THIRD-PARTY.txt](target/generated-sources/license/THIRD-PARTY.txt)
- 📄 [NOTICE](NOTICE) - Atribuições e reconhecimentos

#### Gerar relatório de licenças

```bash
# Gerar relatório de licenças de todas as dependências
mvn license:add-third-party

# Output: target/generated-sources/license/THIRD-PARTY.txt
```

### Principais Dependências e Licenças

| Dependência | Versão | Licença | Uso |
|-------------|--------|---------|-----|
| **JGraphT** | 1.5.2 | LGPL 2.1 / EPL 2.0 | Estruturas de grafos |
| **JavaFX** | 21.0.1 | GPL + Classpath Exception | Interface gráfica |
| **JUnit 5** | 5.10.1 | Eclipse Public License 2.0 | Testes unitários |
| **GSON** | 2.10.1 | Apache License 2.0 | Serialização JSON |
| **Commons CSV** | 1.10.0 | Apache License 2.0 | Processamento CSV |
| **JTS** | 1.19.0 | Eclipse Distribution License 1.0 | Geometria espacial |

Todas as dependências foram verificadas para compatibilidade com MIT License.

---

## 📐 Metodologia Ágil

### Scrum Framework

- **Sprint Duration:** 2 semanas
- **Planning:** Início de cada sprint
- **Daily Standups:** (simulados via Trello)
- **Sprint Review:** Fim de cada sprint
- **Sprint Retrospective:** Lições aprendidas

### Project Management

**Trello Board:** [Ver board →](https://trello.com/b/uyY3kYgO/es-2025-land-management-system)

**User Stories:**
- Formato: "Como [role], quero [feature], para [benefit]"
- Story Points: Fibonacci (1, 2, 3, 5, 8, 13)
- Priorização: MoSCoW (Must, Should, Could, Won't)

**Tracking:**
- Cada commit referencia user story: `feat(US-01): ...`
- Pull requests vinculados a issues
- Burndown charts no Trello

---

## 🔧 Build & Development

### Maven Goals

```bash
# Build completo
mvn clean install

# Apenas compilar
mvn compile

# Executar testes
mvn test

# Gerar todos os relatórios
mvn verify

# Executar aplicação
mvn javafx:run

# Gerar SBOM
mvn cyclonedx:makeAggregateBom

# Verificar licenças
mvn license:add-third-party

# Gerar JavaDoc
mvn javadoc:javadoc
```

### Profiles Maven

```bash
# Produção (otimizado)
mvn clean install -P production

# Desenvolvimento (com debug)
mvn clean install -P development
```

---

## 👨‍🎓 Autor

**Pedro Primo**  
Software Engineering - Università degli Studi del Sannio  
Academic Year: 2024/2025  
Professor: Massimiliano Di Penta

---

## 🔗 Links Úteis

- 📦 [GitHub Repository](https://github.com/pprim0/ES-2025-Sannio-LandManagement)
- 📊 [SonarCloud Dashboard](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)
- ⚙️ [GitHub Actions](https://github.com/pprim0/ES-2025-Sannio-LandManagement/actions)
- 📋 [Trello Board](https://trello.com/b/uyY3kYgO/es-2025-land-management-system)
- 📄 [Project Documentation](https://pprim0.github.io/ES-2025-Sannio-LandManagement/)

---

## 📞 Contacto

Para questões sobre o projeto:
- **GitHub Issues:** [Criar issue](https://github.com/pprim0/ES-2025-Sannio-LandManagement/issues)
- **Email:** pedroprimo@estudante.unisannio.it

---

## 🙏 Acknowledgments

Este projeto utiliza as seguintes bibliotecas open-source. Agradecimentos aos seus criadores e mantenedores:

- **JGraphT** - Biblioteca de teoria de grafos
- **JavaFX** - Framework para interfaces gráficas
- **JUnit** - Framework de testes
- **Apache Commons** - Utilitários Java
- **GSON** - Biblioteca JSON
- **JTS Topology Suite** - Processamento geométrico

Ver [NOTICE](NOTICE) para atribuições completas.

---

<div align="center">

**Desenvolvido com ❤️ para Software Engineering**

**Università degli Studi del Sannio | 2024/2025**

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=pprim0_ES-2025-Sannio-LandManagement&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)

</div>