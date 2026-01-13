# 🏞️ Land Management System

Territorial management system for analyzing rural properties, developed as part of the Software Engineering course at Università degli Studi del Sannio.

## 📊 Quality Metrics

[![Build Status](https://github.com/pprim0/ES-2025-Sannio-LandManagement/actions/workflows/ci.yml/badge.svg)](https://github.com/pprim0/ES-2025-Sannio-LandManagement/actions/workflows/ci.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=pprim0_ES-2025-Sannio-LandManagement&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=pprim0_ES-2025-Sannio-LandManagement&metric=coverage)](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=pprim0_ES-2025-Sannio-LandManagement&metric=bugs)](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=pprim0_ES-2025-Sannio-LandManagement&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=pprim0_ES-2025-Sannio-LandManagement&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

---

## 📋 Overview

Java application for managing and analyzing territorial properties in the Autonomous Region of Madeira, focused on reducing territorial fragmentation through:

- 📥 **CSV data loading** with robust parsing (Apache Commons CSV)
- 🗺️ **Adjacency analysis** between properties using graph theory
- 📊 **Area calculations** by administrative region (parish, municipality, island)
- 🧮 **Advanced analysis** with connected components (DFS) for contiguous properties
- 🔄 **Exchange suggestion algorithm** between owners for territorial consolidation
- 📈 **Interactive visualizations** with vis.js (property and owner graphs)
- 💾 **Multi-format exports** (JSON, HTML)
- 🖥️ **Intuitive GUI** with JavaFX
- 📝 **Professional logging** with SLF4J + Logback

---

## 🎯 Project Objectives

This project demonstrates **Software Engineering** best practices with Agile development (Scrum):

| Sprint | Objectives | Status |
|--------|-----------|--------|
| **Sprint 1**<br>(Oct 25 - Nov 7) | ✅ SCM (Git workflow)<br>✅ CI/CD (GitHub Actions)<br>✅ Quality Analysis (SonarCloud)<br>✅ Testing (JUnit 5)<br>✅ License Management (MIT + SBOM) | **COMPLETE** 🎉 |
| **Sprint 2**<br>(Nov 8 - Nov 21) | ✅ CSV Loader (Apache Commons)<br>✅ Property Graph (JGraphT)<br>✅ Area Analysis (Simple + Advanced)<br>✅ JSON Export | **COMPLETE** 🎉 |
| **Sprint 3**<br>(Nov 22 - Dec 5) | ✅ Owner Graph<br>✅ Exchange Algorithm<br>✅ JavaFX UI<br>✅ HTML Exports (vis.js) | **COMPLETE** 🎉 |
| **Sprint 4**<br>(Dec 6 - Dec 19) | ✅ Professional Refactoring<br>✅ SLF4J Logging Implementation<br>✅ Code Quality Polish<br>✅ Final Documentation | **COMPLETE** 🎉 |

---

## 🚀 Getting Started

### Prerequisites

- **Java 21** (LTS)
- **Maven 3.8+**
- **Git**

### Installation

```bash
# Clone repository
git clone https://github.com/pprim0/ES-2025-Sannio-LandManagement.git
cd ES-2025-Sannio-LandManagement

# Build project
mvn clean install

# Run tests
mvn test

# Generate coverage report
mvn clean test jacoco:report
```

### Run the Application

```bash
# Via Maven
mvn javafx:run

# Via JAR
java -jar target/land-management-system-1.0.0-SNAPSHOT.jar
```

---

## 📊 Dataset

The project uses the **Madeira-Moodle-1.1.csv** dataset containing information about **35,123 rural properties** from the Autonomous Region of Madeira.

**Location**: `src/main/resources/data/Madeira-Moodle-1.1.csv`

**Dataset Statistics:**
- 📍 **35,123** properties
- 👥 **1,005** unique owners
- 🔗 **14,988** adjacency relationships
- 🏘️ **54** parishes
- 🏙️ **11** municipalities
- 🏝️ **1** island (Madeira)

**CSV Format - Fields:**
- `OBJECTID` - Unique identifier
- `PAR_ID` - Property ID
- `PAR_NUM` - Property number
- `Shape_Length` - Perimeter (meters)
- `Shape_Area` - Area (m²)
- `GEOMETRY` - Geometry in WKT format (Well-Known Text)
- `OWNER` - Owner name
- `FREGUESIA` - Parish
- `MUNICIPIO` - Municipality
- `ILHA` - Island

---

## 🛠️ Technologies

### Core
- **Java 21** (LTS) - Programming language with modern features
- **Maven 3.9** - Build and dependency management

### Frameworks & Libraries
- **JUnit Jupiter** (5.10.1) - Unit testing framework
- **JaCoCo** (0.8.11) - Code coverage measurement
- **GSON** (2.10.1) - JSON serialization/deserialization
- **JGraphT** (1.5.2) - Graph data structures and algorithms
- **Apache Commons CSV** (1.10.0) - Professional CSV parsing
- **JavaFX** (21.0.1) - Modern GUI framework
- **JTS Core** (1.19.0) - Geometry and spatial processing
- **SLF4J** (2.0.9) - Logging facade
- **Logback Classic** (1.4.14) - Logging implementation

### Quality Assurance & CI/CD
- **GitHub Actions** - Automated CI/CD pipeline
- **SonarCloud** - Continuous code quality analysis
- **CycloneDX** (2.7.11) - SBOM (Software Bill of Materials) generation
- **License Maven Plugin** (2.4.0) - Automatic license management
- **Checkstyle** (3.4.0) - Code style verification (Sun checks)
- **PMD** (3.21.2) - Static analysis for code quality

---

## 📈 Architecture Overview

### Layer Architecture (MVC + Strategy Pattern)

```
┌─────────────────────────────────────────┐
│      Presentation Layer (UI)            │
│  ┌───────────────────────────────────┐  │
│  │ GestaoTerritorioApp.java (JavaFX) │  │
│  └───────────────────────────────────┘  │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│       Business Logic Layer              │
│  ┌───────────────────────────────────┐  │
│  │ AreaAvancada.java (DFS algorithm) │  │
│  │ SugestaoTroca.java (Basic)        │  │
│  │ SugestaoTrocaAvancada.java        │  │
│  │   (Multi-criteria heuristic)      │  │
│  │ GrafoAdjacencias.java (JGraphT)   │  │
│  │ GrafoProprietarios.java           │  │
│  └───────────────────────────────────┘  │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│      Data Access & Utils Layer          │
│  ┌───────────────────────────────────┐  │
│  │ CSVLoader.java (Apache Commons)   │  │
│  │ JSONExporter.java (GSON)          │  │
│  │ ExportadorAdjacenciasHTML.java    │  │
│  │ ExportadorProprietariosHTML.java  │  │
│  │ AreaPropriedades.java (Simple)    │  │
│  │ TestarExportadores.java (Utils)   │  │
│  └───────────────────────────────────┘  │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│            Model Layer                  │
│  ┌───────────────────────────────────┐  │
│  │ Propriedade.java (Domain Model)   │  │
│  └───────────────────────────────────┘  │
└─────────────────────────────────────────┘
```

### Design Patterns
- **MVC (Model-View-Controller)** - Clear separation UI/Logic/Data
- **Strategy Pattern** - Area calculation algorithms (Simple vs Advanced DFS)
- **Facade Pattern** - Simplified API for complex graph operations

---

## 📂 Project Structure

```
ES-2025-Sannio-LandManagement/
├── .github/
│   └── workflows/
│       └── ci.yml                           # GitHub Actions CI/CD
├── src/
│   ├── main/
│   │   ├── java/es/
│   │   │   ├── Propriedade.java             # Domain model
│   │   │   ├── CSVLoader.java               # CSV loading (Apache Commons)
│   │   │   ├── GrafoAdjacencias.java        # Property adjacency graph
│   │   │   ├── GrafoProprietarios.java      # Owner graph
│   │   │   ├── AreaPropriedades.java        # Simple area calculations
│   │   │   ├── AreaAvancada.java            # DFS for contiguous areas
│   │   │   ├── SugestaoTroca.java           # Basic exchange algorithm
│   │   │   ├── SugestaoTrocaAvancada.java   # Advanced multi-criteria
│   │   │   ├── JSONExporter.java            # JSON export (GSON)
│   │   │   ├── ExportadorAdjacenciasHTML.java    # Property graph viz
│   │   │   ├── ExportadorProprietariosHTML.java  # Owner graph viz
│   │   │   ├── GestaoTerritorioApp.java     # JavaFX GUI
│   │   │   └── TestarExportadores.java      # HTML utilities
│   │   └── resources/
│   │       ├── data/
│   │       │   └── Madeira-Moodle-1.1.csv   # 35k properties dataset
│   │       └── logback.xml                  # Logging config
│   └── test/
│       └── java/es/
│           ├── PropriedadeTest.java          # Model tests (2)
│           ├── CSVLoaderTest.java            # CSV loading tests (2)
│           ├── GrafoAdjacenciasTest.java     # Adjacency tests (8)
│           ├── GrafoProprietariosTest.java   # Owner graph tests (10)
│           ├── AreaPropriedadesTest.java     # Simple area tests (12)
│           ├── AreaAvancadaTest.java         # DFS algorithm tests (8)
│           ├── SugestaoTrocaTest.java        # Basic exchange tests (6)
│           ├── SugestaoTrocaAvancadaTest.java # Advanced tests (6)
│           ├── JSONExporterTest.java         # JSON export tests (10)
│           ├── ExportadorAdjacenciasHTMLTest.java    # HTML tests (5)
│           ├── ExportadorProprietariosHTMLTest.java  # HTML tests (5)
│           └── TestarExportadoresTest.java   # Utility tests (16)
├── target/                                   # Build output (gitignored)
│   ├── bom.json                              # SBOM (CycloneDX JSON)
│   ├── bom.xml                               # SBOM (CycloneDX XML)
│   ├── site/jacoco/                          # Coverage reports
│   └── generated-sources/license/
│       └── THIRD-PARTY.txt                   # License report
├── .gitignore
├── LICENSE                                   # MIT License
├── NOTICE                                    # Third-party attributions
├── pom.xml                                   # Maven configuration
└── README.md                                 # This file
```

---

## 🧪 Testing

### Run Tests

```bash
# All tests
mvn test

# With coverage report
mvn clean test jacoco:report

# View HTML report
open target/site/jacoco/index.html
```

### Test Coverage (Updated December 2025)

| Metric | Value | Status |
|---------|-------|--------|
| **Total Tests** | **90** | ✅ All Passing |
| **Test Classes** | **12** | ✅ |
| **Instruction Coverage** | **95%** | ✅ **Excellent** |
| **Branch Coverage** | **82%** | ✅ **Excellent** |
| **Lines Covered** | 2,697 / 2,818 | ✅ |
| **Classes Covered** | 13 / 13 | ✅ 100% |

### Test Distribution

| Type | Count | Purpose |
|------|-------|---------|
| **Unit Tests** | 52 | Isolated class testing |
| **Integration Tests** | 22 | Component interaction |
| **End-to-End Tests** | 16 | Complete workflows |

### Test Structure by Class

```
es/
├── PropriedadeTest.java (2 tests)                   # Data model
├── CSVLoaderTest.java (2 tests)                     # CSV with edge cases
├── GrafoAdjacenciasTest.java (8 tests)              # Adjacency graph
├── GrafoProprietariosTest.java (10 tests)           # Owner graph
├── AreaPropriedadesTest.java (12 tests)             # Simple calculations
├── AreaAvancadaTest.java (8 tests)                  # DFS algorithm
├── SugestaoTrocaTest.java (6 tests)                 # Basic exchange
├── SugestaoTrocaAvancadaTest.java (6 tests)         # Advanced exchange
├── JSONExporterTest.java (10 tests)                 # JSON validation
├── ExportadorAdjacenciasHTMLTest.java (5 tests)     # Property graph viz
├── ExportadorProprietariosHTMLTest.java (5 tests)   # Owner graph viz
└── TestarExportadoresTest.java (16 tests)           # HTML utilities
```

**Critical Test Case:**
- `testDFS_WithCycles` (AreaAvancadaTest) - Prevents stack overflow in circular adjacencies with visited set tracking. Essential for 35k property dataset where cycles are inevitable.

**Testing Philosophy:**
- ✅ **AAA Pattern** (Arrange-Act-Assert)
- ✅ **Edge cases** thoroughly tested (empty, null, malformed data)
- ✅ **Integration tests** for CSV→Graph→Export pipeline
- ✅ **Naming**: `testMethod_Scenario_ExpectedResult`

---

## 📊 Quality Gate (SonarCloud)

The project maintains high quality standards through continuous analysis:

| Metric | Status | Value | Target |
|--------|--------|-------|--------|
| **Quality Gate** | ✅ Passed | **Grade A** | A |
| **Security** | ✅ Excellent | **0 vulnerabilities** | 0 |
| **Reliability** | ✅ Excellent | **0 bugs** | 0 |
| **Maintainability** | ✅ Excellent | **Rating A** | A |
| **Coverage** | ✅ Outstanding | **95%** | >80% |
| **Duplications** | ✅ Excellent | **3.2%** | <5% |
| **Code Smells** | ⚠️ Minor | **189 (all minor)** | <250 |

[🔗 View complete analysis on SonarCloud](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)

---

## 🔄 CI/CD Pipeline

Automated pipeline with **GitHub Actions** executed on every push/PR:

### Workflow Steps

```yaml
name: CI/CD Pipeline

on: [push, pull_request]

jobs:
  build-and-test:
    runs-on: ubuntu-latest
    steps:
      - ✅ Checkout code (actions/checkout@v4)
      - ✅ Setup JDK 21 (Temurin distribution)
      - ✅ Cache Maven packages
      - ✅ Build with Maven (mvn clean install)
      - ✅ Run all 90 tests (mvn test)
      - ✅ Generate JaCoCo coverage (95%)
      - ✅ SonarCloud analysis (Grade A)
      - ✅ Checkstyle verification (0 violations)
      - ✅ Generate SBOM (CycloneDX)
      - ✅ Upload artifacts
```

**CI/CD Statistics:**
- ✅ **Total Runs:** 40
- ✅ **Current Streak:** 7 consecutive successful builds (100%)
- ✅ **Build Time:** 1-2 minutes (optimized with Maven cache)

**Triggers:**
- ✅ Push to `main` branch
- ✅ Pull Requests to `main`
- ✅ Manual workflow dispatch

[🔗 View workflows on GitHub Actions](https://github.com/pprim0/ES-2025-Sannio-LandManagement/actions)

---

## 📜 License & SBOM Management

### License

This project is licensed under the **MIT License** - see [LICENSE](LICENSE) file for complete details.

#### MIT License - Permissions

✅ **Commercial use** - Can be used in commercial projects  
✅ **Modification** - Can be modified freely  
✅ **Distribution** - Can be distributed  
✅ **Private use** - Can be used privately  

**Why MIT?**
- 🎯 **Permissive** - Maximum freedom for users
- 🎯 **Simple** - Only 171 words of legal text
- 🎯 **Popular** - 55% of GitHub uses MIT
- 🎯 **Compatible** - All 8 production dependencies verified

---

### Software Bill of Materials (SBOM)

The project automatically generates a **complete SBOM** in **CycloneDX 1.5** format:

- ✅ **19 components** (from 26 total project dependencies)
- ✅ Exact versions with SHA-256 hashes
- ✅ License information for each
- ✅ Transitive dependency hierarchy
- ✅ CPE and PURL identifiers
- ✅ Vulnerability scanning ready

**Why 19 and not 26?**
- 19 components are **runtime/compile scope** (production)
- 7 are **test scope** (not shipped with final artifact)
- SBOM correctly excludes test dependencies

**Available Formats:**
- 📄 `target/bom.json` - CycloneDX JSON (recommended)
- 📄 `target/bom.xml` - CycloneDX XML

#### Generate SBOM

```bash
mvn cyclonedx:makeAggregateBom

# Verify components
grep -c "<component" target/bom.xml  # Should show: 19
```

**Why CycloneDX?**
- 🎯 **OWASP standard** for supply chain security
- 🎯 **Security-focused** - Native CVE tracking
- 🎯 **Modern** - Post-SolarWinds and Log4Shell

---

### Main Dependencies and Licenses

| Dependency | Version | License | MIT Compatible? |
|-------------|--------|---------|-----------------|
| **JGraphT Core** | 1.5.2 | LGPL 2.1 / EPL 2.0 | ✅ Yes |
| **JavaFX Controls** | 21.0.1 | GPL v2 + Classpath Exception | ✅ Yes |
| **JavaFX FXML** | 21.0.1 | GPL v2 + Classpath Exception | ✅ Yes |
| **Apache Commons CSV** | 1.10.0 | Apache 2.0 | ✅ Yes |
| **GSON** | 2.10.1 | Apache 2.0 | ✅ Yes |
| **JTS Core** | 1.19.0 | EDL 1.0 / EPL 2.0 | ✅ Yes |
| **SLF4J API** | 2.0.9 | MIT | ✅ Yes |
| **Logback Classic** | 1.4.14 | EPL 1.0 / LGPL 2.1 | ✅ Yes |

**Generate License Report:**
```bash
mvn license:add-third-party
# Output: target/generated-sources/license/THIRD-PARTY.txt
```

---

## 📐 Agile Methodology (Scrum)

### Scrum Framework

- 📅 **Sprint Duration:** 2 weeks
- 🎯 **Total Sprints:** 4 (Oct 25 - Dec 19)
- 🏃 **Velocity:** 25-30 story points per sprint
- 📊 **Tracking:** Trello with GitHub integration

### Project Management

**Tool:** Trello Board

[🔗 View Trello Board](https://trello.com/b/uyY3kYgO/es-2025-land-management-system)

**User Story Format:**
- 🔢 Story Points: Fibonacci (1, 2, 3, 5, 8, 13)
- ✅ Definition of Done: Tests >80% coverage, reviewed, merged

---

## 🔧 Build & Development

### Essential Maven Commands

```bash
# Build and test
mvn clean install                    # Complete build
mvn test                             # Run 90 tests
mvn clean test jacoco:report         # Coverage report

# Quality checks
mvn checkstyle:check                 # 0 violations ✅
mvn pmd:check                        # 6 minor violations ⚠️

# SBOM and licenses
mvn cyclonedx:makeAggregateBom       # Generate SBOM
mvn license:add-third-party          # License report

# Run application
mvn javafx:run                       # Launch GUI
```

### Key Maven Profiles

```bash
# Production build (optimized)
mvn clean install -Pproduction

# Quick build (skip tests - use sparingly)
mvn clean install -DskipTests
```

---

## 🎨 Main Features

### 1. Data Loading
- ✅ Apache Commons CSV for professional parsing
- ✅ Handles 35,123 properties with quoted fields
- ✅ UTF-8 encoding for Portuguese characters
- ✅ WKT geometry validation via JTS
- ✅ Structured error handling with SLF4J logging

### 2. Graph Analysis
- ✅ **Property adjacency graph** - 14,988 edges via JGraphT
- ✅ **Owner graph** - 1,005 nodes
- ✅ **DFS traversal** for connected components
- ✅ **Efficient lookup** - O(1) neighbor access via hash maps

### 3. Area Calculations
- ✅ **Simple average** - Arithmetic mean by region
- ✅ **Advanced DFS** - Connected components for contiguous parcels
- ✅ **Filters** - By parish/municipality/island
- ✅ **Statistics** - Min, max, median, std deviation

### 4. Exchange Suggestions
- ✅ **Multi-criteria heuristic** - Score ≥0.7 threshold
- ✅ **Optimization goals** - Area maximization, cost minimization
- ✅ **Similarity matching** - Perimeter and location
- ✅ **Consolidation benefit** - Reduces fragmentation

### 5. Visualizations
- ✅ **Interactive graphs** - vis.js library
- ✅ **Real-time stats** - Property count, edge count
- ✅ **Standalone HTML** - No server required
- ✅ **Auto browser launch** - Seamless UX

### 6. Exports
- ✅ JSON (structured data via GSON)
- ✅ HTML (interactive visualizations)
- ✅ SBOM (CycloneDX JSON/XML)
- ✅ License reports (THIRD-PARTY.txt)

---

## 🏗️ Code Quality Practices

### Design Principles Applied
- ✅ **SOLID** - Single Responsibility, Open/Closed, Liskov, Interface Segregation, Dependency Inversion
- ✅ **DRY** - Don't Repeat Yourself (code reuse)
- ✅ **KISS** - Keep It Simple, Stupid
- ✅ **YAGNI** - You Aren't Gonna Need It
- ✅ **Separation of Concerns** - Clear layer boundaries

### Professional Standards
- ✅ **Checkstyle compliance** - Sun Checks (0 violations)
- ✅ **PMD analysis** - 6 minor violations (priority 4)
- ✅ **Defensive programming** - Input validation everywhere
- ✅ **Professional logging** - SLF4J with hierarchical levels
- ✅ **Comprehensive testing** - 95% instruction coverage

---

## 🚀 Recent Professional Improvements (December 2025)

### 1. Apache Commons CSV Integration
**Problem:** Manual `String.split(";")` failed on quoted fields like `"Silva; João"`

**Solution:** Industry-standard Apache Commons CSV
```java
CSVParser parser = CSVFormat.DEFAULT
    .builder()
    .setDelimiter(';')
    .setHeader()
    .build()
    .parse(reader);
```

**Benefits:** Handles edge cases, UTF-8, 10M+ downloads/month

---

### 2. SLF4J + Logback Professional Logging
**Problem:** Primitive `System.out.println` and `printStackTrace()`

**Solution:** Professional logging infrastructure
```java
logger.info("Loading properties from: {}", filename);
logger.error("Failed to load properties", exception);
```

**Benefits:** Hierarchical levels, timestamps, thread context, production-ready

---

### 3. CI/CD Antipatterns Avoided
Following **Vassallo & Di Penta, ICSE 2019** research:
- ✅ **Broken Master** - Pipeline caught 3 failures on Dec 9
- ✅ **Skipping Tests** - 90 tests mandatory in each build
- ✅ **Infrequent Commits** - 46 commits across 4 sprints
- ✅ **Slow Builds** - 1-2 minutes with Maven cache
- ✅ **Poor Observability** - README badges + SonarCloud
- ✅ **Missing Quality Checks** - Checkstyle + PMD + SonarCloud

---

## 👨‍🎓 Author

**Pedro Primo**  
Erasmus Student - Software Engineering  
Università degli Studi del Sannio  
Academic Year: 2025/2026  
Supervisor: Professor Massimiliano Di Penta

---

## 🔗 Useful Links

- 📦 [GitHub Repository](https://github.com/pprim0/ES-2025-Sannio-LandManagement)
- 📊 [SonarCloud Dashboard](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)
- ⚙️ [GitHub Actions CI/CD](https://github.com/pprim0/ES-2025-Sannio-LandManagement/actions)
- 📋 [Trello Board](https://trello.com/b/uyY3kYgO/es-2025-land-management-system)

---

## 🙏 Acknowledgments

Deep gratitude to the open-source community and maintainers of:

- **JGraphT Team** - Graph theory library
- **OpenJFX Community** - Modern GUI framework
- **JUnit Team** - Unit testing framework
- **Google GSON Team** - JSON library
- **Apache Software Foundation** - Commons CSV
- **LocationTech** - JTS Topology Suite
- **QOS.ch** - SLF4J and Logback
- **OWASP** - CycloneDX SBOM tooling

See [NOTICE](NOTICE) for complete attributions.

---

<div align="center">

**Developed for Software Engineering**

**Università degli Studi del Sannio | 2025/2026**

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=pprim0_ES-2025-Sannio-LandManagement&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=pprim0_ES-2025-Sannio-LandManagement&metric=coverage)](https://sonarcloud.io/summary/new_code?id=pprim0_ES-2025-Sannio-LandManagement)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

</div>
