# LeiteVida - Sistema de Gerenciamento para Banco de Leite Humano

![Logo LeiteVida](./src/main/webapp/content/images/logo-full.png)

## 📋 Sobre o Projeto

LeiteVida é um sistema web desenvolvido para otimizar e gerenciar todos os processos operacionais de um Banco de Leite Humano (BLH). O sistema facilita o cadastro e acompanhamento de doadoras, o registro de coletas de leite materno, o controle de estoque e a distribuição do leite processado para pacientes neonatais.

### Principais funcionalidades:

- Cadastro e gerenciamento de doadoras
- Registro e rastreamento de coletas de leite materno
- Processamento e análise laboratorial das amostras
- Controle de estoque e validade dos lotes
- Gerenciamento de distribuição aos pacientes
- Geração de relatórios e estatísticas
- Controle de acesso por perfis de usuário

## 🚀 Tecnologias Utilizadas

- **Frontend**:

  - React/TypeScript
  - React Router DOM
  - Reactstrap (Bootstrap para React)
  - SCSS para estilos personalizados
  - FontAwesome para ícones
  - Axios para requisições HTTP

- **Backend**:
  - Spring Boot
  - Spring Security
  - JPA/Hibernate
  - Banco de dados relacional
  - RESTful API

## 📦 Estrutura do Projeto

O projeto segue uma arquitetura moderna de aplicação web com separação clara entre frontend e backend:

```
leite-vida/
│
├── src/main/
│   ├── java/           # Código backend Java/Spring Boot
│   ├── resources/      # Recursos para o backend
│   └── webapp/         # Aplicação frontend React
│       ├── app/        # Componentes React
│       │   ├── config/         # Configurações
│       │   ├── modules/        # Módulos da aplicação
│       │   │   ├── home/       # Página inicial
│       │   │   ├── doadora/    # Gerenciamento de doadoras
│       │   │   ├── coleta/     # Gestão de coletas
│       │   │   ├── estoque/    # Controle de estoque
│       │   │   └── administration/ # Funções administrativas
│       │   └── shared/         # Componentes compartilhados
│       ├── content/    # Recursos estáticos (imagens, etc.)
│       └── scss/       # Estilos globais
│
├── config/             # Configurações do projeto
├── node_modules/       # Dependências Node.js
└── package.json        # Definição de pacotes e scripts
```

## 🔧 Instalação e Execução

### Pré-requisitos

- Node.js (v14+)
- Java JDK 11+
- Maven 3.6+
- Banco de dados compatível (PostgreSQL recomendado)

### Passos para execução

1. Clone o repositório:

   ```bash
   git clone https://github.com/sua-organizacao/leite-vida.git
   cd leite-vida
   ```

2. Instale as dependências do frontend:

   ```bash
   npm install
   ```

3. Configure o banco de dados em `application.yml` ou através de variáveis de ambiente.

4. Execute o backend:

   ```bash
   ./mvnw spring-boot:run
   ```

5. Em outro terminal, execute o frontend:

   ```bash
   npm start
   ```

6. Acesse a aplicação em [http://localhost:8080](http://localhost:8080)

## 👤 Perfis de Usuário

O sistema oferece diferentes perfis de acesso:

1. **Administrador**: Acesso completo a todas as funcionalidades, incluindo gerenciamento de usuários.
2. **Operador**: Pode registrar doadoras, coletas e distribuições.
3. **Laboratório**: Responsável pelas análises e processamento do leite.
4. **Consulta**: Acesso apenas para visualização de dados, sem permissão de alteração.

## 📱 Interface do Sistema

### Tela de Login

![Tela de Login](./docs/images/login-screen.png)

A tela de login apresenta uma interface limpa e intuitiva com o logotipo do LeiteVida. Os usuários devem inserir seu CPF e senha para acessar o sistema.

### Dashboard

![Dashboard](./docs/images/dashboard.png)

O dashboard principal mostra estatísticas importantes:

- Número de doadoras cadastradas
- Volume de leite em estoque
- Volume de leite em processamento

Também apresenta acesso rápido às principais funcionalidades através de botões de ação.

### Listagem de Doadoras

![Lista de Doadoras](./docs/images/doadoras-list.png)

Interface para visualização e gerenciamento de doadoras cadastradas, com opções para filtrar, editar e adicionar novas doadoras.

### Relatórios

![Relatório de Coletas](./docs/images/relatorio-coletas.png)

O sistema permite gerar relatórios detalhados sobre coletas, processamento e distribuição, com filtros por período e possibilidade de exportação.

## 🔒 Segurança

- Autenticação por login e senha
- Controle de sessão
- Validação de permissões por rota
- Criptografia de dados sensíveis
- Proteção contra CSRF e XSS

## 📊 Funcionalidades de Relatórios

O sistema oferece diversos relatórios para acompanhamento e gestão:

- Relatório de doadoras ativas
- Relatório de coletas por período
- Relatório de estoque (volume disponível e validade)
- Relatório de distribuição (volume distribuído por paciente/hospital)
- Estatísticas de produção e distribuição

## 🤝 Contribuição

Para contribuir com o projeto:

1. Crie um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/nome-da-feature`)
3. Faça commit das suas alterações (`git commit -m 'Adiciona nova feature'`)
4. Envie para o branch (`git push origin feature/nome-da-feature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está licenciado sob a licença [MIT](LICENSE).

## 📞 Suporte

Para suporte, entre em contato através do email: suporte@leitevida.com.br
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
