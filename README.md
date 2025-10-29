# 🏞️ Land Management System

Sistema de gestão territorial para análise de propriedades rurais, desenvolvido no âmbito da disciplina de Software Engineering na Università degli Studi del Sannio.

## 📋 Descrição

Aplicação Java para gestão e análise de propriedades territoriais, incluindo:
- Carregamento de dados CSV de registos de propriedades
- Análise de adjacências entre propriedades (grafos)
- Cálculos de áreas por região
- Algoritmo de sugestão de trocas de propriedades
- Visualizações e exportações (JSON, HTML)

## 🎯 Objetivos do Projeto

Este projeto demonstra boas práticas de Software Engineering:
- ✅ **SCM**: Git workflow com branches e pull requests
- ✅ **CI/CD**: GitHub Actions com builds automáticos
- ✅ **Quality Analysis**: SonarCloud, Checkstyle, SpotBugs
- ✅ **Testing**: JUnit 5 com >80% code coverage
- ✅ **License Management**: MIT License + SBOM
- ✅ **Agile**: Desenvolvimento com Scrum (sprints de 2 semanas)

## 🚀 Getting Started

### Pré-requisitos
- Java 17+
- Maven 3.8+

### Instalação
```bash
# Clonar repositório
git clone https://github.com/pprim0/ES-2025-SANNIO-LANDMANAGEMENT.git
cd ES-2025-SANNIO-LANDMANAGEMENT

# Compilar
mvn clean install

# Executar testes
mvn test
```

## 📊 Dataset

O projeto utiliza o dataset `Madeira-Moodle-1.1.csv` contendo informações de propriedades rurais da Região Autónoma da Madeira.

**Localização**: `src/main/resources/data/Madeira-Moodle-1.1.csv`

## 🛠️ Tecnologias

- **Java 17**
- **Maven** - Build e gestão de dependências
- **JUnit 5** - Testes unitários
- **GSON** - Serialização JSON
- **JGraphT** - Estruturas de dados em grafo
- **JavaFX** - Interface gráfica
- **GitHub Actions** - CI/CD
- **SonarCloud** - Análise de qualidade de código

## 📈 Quality Metrics

[![Build Status](https://github.com/pprim0/ES-2025-SANNIO-LANDMANAGEMENT/actions/workflows/ci.yml/badge.svg)](https://github.com/pprim0/ES-2025-SANNIO-LANDMANAGEMENT/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=YOUR_PROJECT_KEY&metric=alert_status)](https://sonarcloud.io/dashboard?id=YOUR_PROJECT_KEY)

## 👨‍🎓 Autor

**Pedro Primo**  
Software Engineering - Università degli Studi del Sannio  
Professor: Massimiliano Di Penta  
Ano Académico: 2024/2025

## 📄 Licença

Este projeto está licenciado sob a [MIT License](LICENSE).
