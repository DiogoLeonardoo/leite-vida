# 🍼 Sistema Web de Gerenciamento de Banco de Leite Humano Materno

Este repositório contém o projeto de sistema web desenvolvido como Trabalho de Conclusão de Curso (TCC) para o curso superior de **Análise e Desenvolvimento de Sistemas** do Instituto Federal de Educação, Ciência e Tecnologia de Sergipe (IFS) - Campus Aracaju.

O sistema visa otimizar a gestão de um Banco de Leite Humano Materno (BLH), abordando os desafios operacionais e a necessidade de rastreabilidade do leite.

---

## 🧩 Sobre o Projeto

Os Bancos de Leite Humano (BLH) desempenham um papel crucial na promoção do aleitamento materno e na nutrição neonatal, contribuindo significativamente para a redução da mortalidade infantil.

Este projeto propõe uma solução web para informatizar a gestão de coletas, processamentos e distribuições de leite humano, oferecendo um ambiente estruturado e seguro para o controle interno de um BLH.

---

## ✅ Funcionalidades Principais

### 🔐 Módulo de Acesso e Usuários (Administrador)

- **Cadastro de Usuário**
- **Login e Recuperação de Senha**
- **Edição e Exclusão de Usuários**
- **Listagem de Usuários**

### 👩‍⚕️ Gerenciamento de Doadoras (Enfermeira)

- **Cadastro, Edição e Listagem de Doadoras**
- **Cadastro Integrado com Coleta**

### 🧑‍⚕️ Gerenciamento de Pacientes (Enfermeira)

- **Cadastro, Edição e Listagem de Pacientes**

### 🧪 Coletas e Processamento (Enfermeira e Laboratório)

- **Cadastro de Coletas**
- **Avaliação Microbiológica do Leite**
- **Rastreabilidade Completa (doadora → paciente)**

### 🧊 Gerenciamento de Estoque (Enfermeira e Laboratório)

- **Visualização e Filtros por Status/Tipo**
- **Controle de Validade**
- **Registros de Descarte Automatizados**

### 📦 Gerenciamento de Distribuições (Enfermeira)

- **Cadastro de Distribuições com base em prescrição**
- **Prescrição Médica com volume e frequência**

---

## 💡 Usabilidade e Desempenho

- Interface intuitiva e responsiva
- Feedback visual em tempo real
- Rápido tempo de resposta

---

## 🔒 Segurança

- Autenticação com base em perfis (Administrador, Enfermeira, Laboratório)
- Confidencialidade de dados sensíveis

---

## 📊 Relatórios e Dashboard

- Relatórios de doações, coletas e distribuições
- Dashboard com indicadores:
  - Volume total em estoque
  - Volume em análise
  - Doadoras ativas
  - Total de coletas realizadas

---

## 🛠️ Modelagem do Sistema

- **Elicitação de Requisitos**
- **Casos de Uso (UML)**
- **Diagramas BPMN**
- **Diagrama Entidade-Relacionamento (DER)**
- **Dicionarização do DER**
- **Diagrama de Classes**

---

## 🎨 Protótipo da Interface

O sistema foi prototipado com foco em **usabilidade**, **acessibilidade** e **responsividade**, garantindo uma navegação fluida e intuitiva. A tela de login é um exemplo do design proposto.

---

## 🚀 Como Rodar o Projeto

This application was generated using JHipster **8.11.0**. For full documentation, visit:  
📚 [https://www.jhipster.tech/documentation-archive/v8.11.0](https://www.jhipster.tech/documentation-archive/v8.11.0)

### ⚙️ Requisitos

- Java 17+
- Node.js (configurado automaticamente com `./npmw`)
- Docker (para serviços externos opcionais)
- Gradle

---

### 📦 Instalação

Clone o repositório:

```bash
git clone https://github.com/seu-usuario/leite-vida.git
cd leite-vida
```

Instale as dependências:

```bash
./npmw install
```

---

### 💻 Ambiente de Desenvolvimento

Execute os comandos abaixo **em dois terminais diferentes**:

```bash
./gradlew -x webapp
```

```bash
./npmw start
```

---

### 📦 Build para Produção

```bash
./gradlew -Pprod clean bootJar
java -jar build/libs/*.jar
```

Acesse o sistema em: [http://localhost:8080](http://localhost:8080)

---

### 🧪 Executar Testes

#### Backend (Spring Boot)

```bash
./gradlew test integrationTest jacocoTestReport
```

#### Frontend (React - Jest)

```bash
./npmw test
```

---

### 🐳 Docker (Serviços Externos)

```bash
docker compose -f src/main/docker/services.yml up -d
```

Para parar:

```bash
docker compose -f src/main/docker/services.yml down
```

---

### 🧠 Análise de Qualidade com SonarQube

```bash
docker compose -f src/main/docker/sonar.yml up -d

./gradlew -Pprod clean check jacocoTestReport sonarqube \
  -Dsonar.login=admin -Dsonar.password=admin
```

---

### 🧪 CI/CD (opcional)

Gere arquivos para CI com:

```bash
jhipster ci-cd
```

---

## 🤝 Contribuição

Este é um **projeto individual de TCC**, mas contribuições futuras são **bem-vindas** para evolução e aprimoramento da aplicação.

---

## 📛 Licença

Este projeto é de código aberto, mas orientado por fins acadêmicos. Consulte o autor para uso em produção.

---

## 📌 #leiteVida
