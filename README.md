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

Aplicação Java para gestão e análise de propriedades territoriais da Região Autónoma da Madeira, focada na redução da fragmentação territorial através de:

- 📥 **Carregamento de dados CSV** de registos de propriedades rurais
- 🗺️ **Análise de adjacências** entre propriedades usando teoria de grafos
- 📊 **Cálculos de áreas** por região administrativa (freguesia, município, ilha)
- 🧮 **Análise avançada** com componentes conexas (DFS) para propriedades contíguas
- 🔄 **Algoritmo de sugestão de trocas** entre proprietários para consolidação territorial
- 📈 **Visualizações interativas** com D3.js (grafos de adjacências e proprietários)
- 💾 **Exportações** em múltiplos formatos (JSON, HTML)
- 🖥️ **Interface gráfica** intuitiva com JavaFX

---

## 🎯 Objetivos do Projeto

Este projeto demonstra boas práticas de **Software Engineering** com desenvolvimento Ágil (Scrum):

| Sprint | Objetivos | Status |
|--------|-----------|--------|
| **Sprint 1**<br>(25 Out - 7 Nov) | ✅ SCM (Git workflow)<br>✅ CI/CD (GitHub Actions)<br>✅ Quality Analysis (SonarCloud)<br>✅ Testing (JUnit 5)<br>✅ License Management (MIT + SBOM) | **COMPLETO** 🎉 |
| **Sprint 2**<br>(8 Nov - 21 Nov) | ✅ CSV Loader<br>✅ Property Graph<br>✅ Area Analysis<br>✅ JSON Export | **COMPLETO** 🎉 |
| **Sprint 3**<br>(22 Nov - 5 Dez) | ✅ Owner Graph<br>✅ Exchange Algorithm<br>✅ JavaFX UI<br>✅ HTML Exports | **COMPLETO** 🎉 |
| **Sprint 4**<br>(6 Dez - 19 Dez) | 🔄 Advanced Features<br>🔄 Code Refactoring<br>🔄 Documentation<br>🔄 Final Report | **EM PROGRESSO** 💪 |

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

O projeto utiliza o dataset **Madeira-Moodle-1.1.csv** contendo informações de **35,045 propriedades rurais** da Região Autónoma da Madeira.

**Localização**: `src/main/resources/data/Madeira-Moodle-1.1.csv`

**Estatísticas do Dataset:**
- 📍 **35,045** propriedades
- 👥 **1,005** proprietários únicos
- 🔗 **14,988** relações de adjacência
- 🏘️ **54** freguesias
- 🏙️ **11** municípios
- 🏝️ **1** ilha (Madeira)

**Formato CSV - Campos:**
- `OBJECTID` - Identificador único
- `PAR_ID` - ID do prédio
- `PAR_NUM` - Número do prédio
- `Shape_Length` - Perímetro (metros)
- `Shape_Area` - Área (m²)
- `GEOMETRY` - Geometria em formato WKT (Well-Known Text)
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

### Quality Assurance & CI/CD
- **GitHub Actions** - CI/CD pipeline automático
- **SonarCloud** - Análise contínua de qualidade de código
- **JaCoCo** (0.8.11) - Code coverage measurement
- **CycloneDX** (2.7.11) - SBOM (Software Bill of Materials) generation
- **License Maven Plugin** (2.4.0) - Gestão automática de licenças
- **Checkstyle** (3.4.0) - Code style verification

---

## 📈 Estrutura do Projeto

```
ES-2025-Sannio-LandManagement/
├── .github/
│   └── workflows/
│       └── ci.yml                      # GitHub Actions CI/CD pipeline
├── src/
│   ├── main/
│   │   ├── java/es/
│   │   │   ├── Propriedade.java        # Modelo de dados de propriedade
│   │   │   ├── CSVLoader.java          # Carregamento e parsing de CSV
│   │   │   ├── GrafoAdjacencias.java   # Grafo de adjacências (propriedades)
│   │   │   ├── GrafoProprietarios.java # Grafo de proprietários
│   │   │   ├── AreaPropriedades.java   # Cálculo de áreas simples
│   │   │   ├── AreaAvancada.java       # Cálculo avançado com DFS
│   │   │   ├── SugestaoTroca.java      # Algoritmo de sugestões de trocas
│   │   │   ├── ExportadorJSON.java     # Exportação para JSON
│   │   │   ├── ExportadorAdjacenciasHTML.java    # Visualização D3.js (adjacências)
│   │   │   ├── ExportadorProprietariosHTML.java  # Visualização D3.js (proprietários)
│   │   │   ├── TestarExportadores.java # Gerador de index.html com estatísticas
│   │   │   └── GestaoTerritorioApp.java # Aplicação JavaFX principal
│   │   └── resources/
│   │       └── data/
│   │           └── Madeira-Moodle-1.1.csv  # Dataset (35k propriedades)
│   └── test/
│       └── java/es/
│           ├── PropriedadeTest.java         # Testes modelo
│           ├── CSVLoaderTest.java           # Testes loader
│           ├── GrafoAdjacenciasTest.java    # Testes grafo adjacências
│           ├── GrafoProprietariosTest.java  # Testes grafo proprietários
│           ├── AreaPropriedadesTest.java    # Testes cálculo área
│           ├── AreaAvancadaTest.java        # Testes área avançada
│           ├── SugestaoTrocaTest.java       # Testes algoritmo trocas
│           ├── ExportadorJSONTest.java      # Testes exportação JSON
│           └── TestarExportadoresTest.java  # Testes exportadores HTML
├── target/                              # Build output (gitignored)
│   ├── bom.json                         # SBOM formato CycloneDX JSON
│   ├── bom.xml                          # SBOM formato CycloneDX XML
│   ├── site/jacoco/                     # Relatórios de coverage
│   └── generated-sources/license/
│       └── THIRD-PARTY.txt              # Relatório de licenças
├── .gitignore
├── LICENSE                              # MIT License
├── NOTICE                               # Third-party attributions
├── pom.xml                              # Maven configuration
└── README.md                            # Este ficheiro
```

---

## 🧪 Testing

### Executar testes

```bash
# Todos os testes
mvn test

# Com coverage report
mvn clean test jacoco:report

# Ver relatório HTML
open target/site/jacoco/index.html
```

### Cobertura de Testes

| Métrica | Valor | Status |
|---------|-------|--------|
| **Tests Total** | 92 | ✅ Passing |
| **Test Suites** | 9 | ✅ |
| **Line Coverage** | 80.6% | ✅ >80% |
| **Branch Coverage** | ~75% | ✅ |
| **Mutation Score** | 70%+ | ✅ (PIT) |

### Estrutura de Testes

```
es/
├── PropriedadeTest.java (10 tests)           # Modelo de dados
├── CSVLoaderTest.java (8 tests)              # Carregamento CSV
├── GrafoAdjacenciasTest.java (12 tests)      # Grafo adjacências
├── GrafoProprietariosTest.java (8 tests)     # Grafo proprietários
├── AreaPropriedadesTest.java (10 tests)      # Cálculo área simples
├── AreaAvancadaTest.java (8 tests)           # Cálculo área avançada (DFS)
├── SugestaoTrocaTest.java (15 tests)         # Algoritmo de trocas
├── ExportadorJSONTest.java (10 tests)        # Exportação JSON
└── TestarExportadoresTest.java (11 tests)    # Exportadores HTML
```

---

## 📊 Quality Gate (SonarCloud)

O projeto mantém padrões elevados de qualidade através de análise contínua:

| Metric | Status | Value | Target |
|--------|--------|-------|--------|
| **Quality Gate** | ✅ Passed | Grade A | A |
| **Security** | ✅ | 0 vulnerabilities | 0 |
| **Reliability** | ✅ | 0 bugs | 0 |
| **Maintainability** | ✅ | Rating A | A |
| **Coverage** | ✅ | 80.6% | >80% |
| **Duplications** | ✅ | 3.5% | <5% |
| **Technical Debt** | ✅ | <1 day | <5% |
| **Code Smells** | ⚠️ | 203 | <250 |

[🔗 Ver análise completa no SonarCloud](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)

---

## 🔄 CI/CD Pipeline

Pipeline automático com **GitHub Actions** executado em cada push/PR:

### Workflow Steps

```yaml
on: [push, pull_request]

jobs:
  build-and-test:
    - ☑️ Checkout code
    - ☑️ Setup JDK 17
    - ☑️ Cache Maven packages
    - ☑️ Build with Maven
    - ☑️ Run all 92 tests
    - ☑️ Generate JaCoCo coverage report
    - ☑️ SonarCloud quality analysis
    - ☑️ Generate SBOM (CycloneDX)
    - ☑️ Upload artifacts
```

**Triggers:**
- ✅ Push para `main`
- ✅ Pull Requests para `main`
- ✅ Manual workflow dispatch

**Artifacts Gerados Automaticamente:**
- 📦 JAR executável
- 📊 JaCoCo coverage report (HTML)
- ✅ Test results (XML)
- 📄 SBOM (bom.json, bom.xml)
- 📜 License report (THIRD-PARTY.txt)

[🔗 Ver workflows no GitHub Actions](https://github.com/pprim0/ES-2025-Sannio-LandManagement/actions)

---

## 📜 License & SBOM Management

### License

Este projeto está licenciado sob a **MIT License** - ver ficheiro [LICENSE](LICENSE) para detalhes completos.

#### MIT License - Permissões

✅ **Uso comercial** - Pode ser usado em projetos comerciais  
✅ **Modificação** - Pode ser modificado livremente  
✅ **Distribuição** - Pode ser distribuído  
✅ **Uso privado** - Pode ser usado privadamente  
✅ **Sublicenciamento** - Pode ser sublicenciado  

#### MIT License - Condições

⚠️ **Aviso de licença e copyright** - Deve incluir aviso em todas as cópias  
⚠️ **Sem garantia** - Software fornecido "as is"  
⚠️ **Limitação de responsabilidade** - Autores não são responsáveis por danos  

---

### Software Bill of Materials (SBOM)

O projeto gera automaticamente um **SBOM completo** em formato **CycloneDX 1.5** incluindo:

- ✅ Inventário completo de todas as dependências
- ✅ Versões exatas de cada componente
- ✅ Licenças de cada dependência
- ✅ Hierarquia de dependências transitivas
- ✅ Hashes e checksums
- ✅ CPE (Common Platform Enumeration)
- ✅ PURL (Package URL)

**Formatos Disponíveis:**
- 📄 [SBOM JSON](target/bom.json) - CycloneDX JSON format
- 📄 [SBOM XML](target/bom.xml) - CycloneDX XML format

#### Gerar SBOM

```bash
# Gerar SBOM (executado automaticamente em mvn package)
mvn cyclonedx:makeAggregateBom

# Output:
# ✅ target/bom.json (formato JSON)
# ✅ target/bom.xml (formato XML)
```

---

### Third-Party Licenses

Relatórios completos de licenças de todas as dependências:

- 📄 [THIRD-PARTY.txt](target/generated-sources/license/THIRD-PARTY.txt) - Lista completa
- 📄 [NOTICE](NOTICE) - Atribuições e reconhecimentos

#### Gerar Relatório de Licenças

```bash
# Gerar relatório de licenças
mvn license:add-third-party

# Output: target/generated-sources/license/THIRD-PARTY.txt
```

---

### Principais Dependências e Licenças

| Dependência | Versão | Licença | Uso no Projeto | Compatível MIT? |
|-------------|--------|---------|----------------|-----------------|
| **JGraphT** | 1.5.2 | LGPL 2.1 / EPL 2.0 | Estruturas de grafos (adjacências, proprietários) | ✅ Sim |
| **JavaFX** | 21.0.1 | GPL v2 + Classpath Exception | Interface gráfica (UI) | ✅ Sim |
| **JUnit 5** | 5.10.1 | Eclipse Public License 2.0 | Framework de testes unitários | ✅ Sim |
| **GSON** | 2.10.1 | Apache License 2.0 | Serialização/deserialização JSON | ✅ Sim |
| **Commons CSV** | 1.10.0 | Apache License 2.0 | Parsing de ficheiros CSV | ✅ Sim |
| **JTS** | 1.19.0 | EDL 1.0 / EPL 2.0 | Processamento geométrico (WKT) | ✅ Sim |

**Nota:** Todas as dependências foram verificadas para compatibilidade com MIT License. ✅

---

## 📐 Metodologia Ágil (Scrum)

### Framework Scrum

- 📅 **Sprint Duration:** 2 semanas
- 🎯 **Sprint Planning:** Início de cada sprint
- 📊 **Daily Standups:** Simulados via Trello
- 🎉 **Sprint Review:** Demonstração no fim do sprint
- 🔄 **Sprint Retrospective:** Lições aprendidas e melhorias

### Project Management

**Ferramenta:** Trello Board com GitHub Power-Up

[🔗 Ver Trello Board](https://trello.com/b/uyY3kYgO/es-2025-land-management-system)

**Estrutura de User Stories:**
- ✍️ **Formato:** "Como [role], quero [feature], para [benefit]"
- 🔢 **Story Points:** Fibonacci (1, 2, 3, 5, 8, 13, 21)
- 🎯 **Priorização:** MoSCoW (Must, Should, Could, Won't)

**Tracking & Traceability:**
- 📝 Cada commit referencia user story: `feat(US-01): implementar CSV loader`
- 🔗 Pull requests vinculados a issues do GitHub
- 📈 Burndown charts atualizados no Trello
- ✅ Definition of Done verificada para cada story

---

## 🔧 Build & Development

### Maven Goals Principais

```bash
# Build completo com todos os testes
mvn clean install

# Apenas compilar (sem testes)
mvn compile

# Executar testes unitários
mvn test

# Executar todos os checks (tests + quality)
mvn verify

# Executar aplicação JavaFX
mvn javafx:run

# Gerar SBOM (Software Bill of Materials)
mvn cyclonedx:makeAggregateBom

# Verificar licenças de dependências
mvn license:add-third-party

# Gerar JavaDoc
mvn javadoc:javadoc

# Gerar site do projeto com todos os reports
mvn site
```

### Profiles Maven

```bash
# Profile de produção (otimizado)
mvn clean install -Pproduction

# Profile de desenvolvimento (com debug)
mvn clean install -Pdevelopment

# Skip tests (só para desenvolvimento rápido)
mvn clean install -DskipTests
```

---

## 🎨 Features Principais

### 1. Carregamento de Dados
- ✅ Parsing robusto de CSV com 35k+ registos
- ✅ Validação de dados geométricos (WKT)
- ✅ Tratamento de erros e logging

### 2. Análise de Grafos
- ✅ Grafo de adjacências (14,988 conexões)
- ✅ Grafo de proprietários (1,005 nós)
- ✅ Algoritmos de travessia (BFS, DFS)
- ✅ Detecção de componentes conexas

### 3. Cálculos de Área
- ✅ Área média simples por região
- ✅ Área média avançada (componentes conexas)
- ✅ Filtros por freguesia/município/ilha
- ✅ Análise estatística

### 4. Sugestões de Trocas
- ✅ Algoritmo heurístico multi-critério
- ✅ Maximização de área média
- ✅ Minimização de custos de transação
- ✅ Consideração de características similares

### 5. Visualizações
- ✅ Grafos interativos com D3.js
- ✅ Estatísticas em tempo real
- ✅ Exportação HTML standalone
- ✅ Interface JavaFX intuitiva

### 6. Exportações
- ✅ JSON (estruturado)
- ✅ HTML (visualizações)
- ✅ Relatórios PDF (futuro)

---

## 👨‍🎓 Autor

**Pedro Primo**  
Erasmus Student - Software Engineering  
Università degli Studi del Sannio  
Academic Year: 2024/2025  
Supervisor: Professor Massimiliano Di Penta

---

## 🔗 Links Úteis

- 📦 [GitHub Repository](https://github.com/pprim0/ES-2025-Sannio-LandManagement)
- 📊 [SonarCloud Dashboard](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)
- ⚙️ [GitHub Actions CI/CD](https://github.com/pprim0/ES-2025-Sannio-LandManagement/actions)
- 📋 [Trello Board](https://trello.com/b/uyY3kYgO/es-2025-land-management-system)
- 📄 [Project Documentation](https://pprim0.github.io/ES-2025-Sannio-LandManagement/)
- 📧 [Issues & Bug Reports](https://github.com/pprim0/ES-2025-Sannio-LandManagement/issues)

---

## 📞 Contacto

Para questões sobre o projeto:
- **GitHub Issues:** [Criar issue](https://github.com/pprim0/ES-2025-Sannio-LandManagement/issues)
- **GitHub Discussions:** [Iniciar discussão](https://github.com/pprim0/ES-2025-Sannio-LandManagement/discussions)
- **Email Académico:** pedroprimo@estudante.unisannio.it

---

## 🙏 Acknowledgments

Este projeto utiliza as seguintes bibliotecas open-source. Profundo agradecimento aos seus criadores e mantenedores:

- **JGraphT Team** - Biblioteca robusta de teoria de grafos
- **OpenJFX Community** - Framework moderno para interfaces gráficas
- **JUnit Team** - Framework líder em testes unitários
- **Google GSON Team** - Biblioteca eficiente para JSON
- **Apache Software Foundation** - Commons CSV e outras ferramentas
- **LocationTech** - JTS Topology Suite para geometria

Ver [NOTICE](NOTICE) para atribuições completas e detalhadas.

---

## 🏆 Achievements

### Software Engineering Best Practices

✅ **Version Control:** Git workflow profissional com branches e PRs  
✅ **CI/CD:** Pipeline automático com GitHub Actions  
✅ **Quality Assurance:** SonarCloud Grade A mantido  
✅ **Testing:** 92 testes unitários com 80%+ coverage  
✅ **Documentation:** JavaDoc completo + README detalhado  
✅ **License Management:** MIT License + SBOM automático  
✅ **Agile Methodology:** Scrum com 4 sprints de 2 semanas  
✅ **Code Standards:** Checkstyle + SpotBugs configurados  

### Technical Achievements

🎯 **35,045** propriedades processadas com sucesso  
🎯 **14,988** relações de adjacência mapeadas  
🎯 **1,005** proprietários únicos identificados  
🎯 **92** testes unitários implementados  
🎯 **80.6%** code coverage alcançado  
🎯 **0** bugs de segurança ou reliability  

---

<div align="center">

**Desenvolvido com ❤️ para Software Engineering**

**Università degli Studi del Sannio | 2024/2025**

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=pprim0_ES-2025-Sannio-LandManagement&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=pprim0_ES-2025-Sannio-LandManagement&metric=coverage)](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

---

**"Reducing territorial fragmentation through software engineering excellence"**

</div>