# SportHub - Relatório do Projeto

## Parte 3: Manual de Utilização e Conclusão

---

## 12. Manual de Utilização

### 12.1 Requisitos do Sistema
- Sistema operativo: Windows 10/11
- Java JDK 17 ou superior instalado
- Resolução mínima recomendada: 1280×720

### 12.2 Como Executar
1. Navegar até à pasta do projeto
2. Fazer duplo clique em `run.bat`
3. A aplicação abre automaticamente

Em alternativa, na linha de comandos:
```
tools\apache-maven-3.9.6\bin\mvn.cmd javafx:run
```

---

### 12.3 Ecrã de Login

Ao iniciar a aplicação, é apresentado o ecrã de login:

- **Email:** Introduzir o email de um utilizador registado
- **Password:** Introduzir a password correspondente
- **Botão "Entrar":** Efetua o login e navega para o dashboard
- **Botão "Criar nova conta":** Abre o formulário de registo

**Contas pré-criadas para teste:**
- Cliente: `pedro@email.com` / `1234`
- Gestor: `ana@email.com` / `1234`

---

### 12.4 Ecrã de Registo

Formulário para criar uma nova conta:
- Nome completo
- NIF (9 dígitos numéricos)
- Data de Nascimento (seletor de data)
- Telefone
- Email (deve ser único)
- Password (mínimo 4 caracteres)
- Tipo de conta: "Cliente" ou "Gestor de Espaco"

Após registo bem-sucedido, é redirecionado para o login.

---

### 12.5 Dashboard — Vista do Cliente

A sidebar apresenta as seguintes opções:

#### Menu Principal
1. **Espacos Disponiveis** — Consultar todos os espaços registados
2. **Nova Reserva** — Criar uma nova reserva
3. **As Minhas Reservas** — Ver histórico de reservas

#### Conta
4. **O Meu Perfil** — Editar dados pessoais

#### Consultar Espaços
- A tabela lista todos os espaços disponíveis
- Clicar num espaço mostra os detalhes: preçário por faixa horária e serviços disponíveis

#### Criar Nova Reserva (passo a passo)
1. Selecionar o espaço no dropdown
2. Escolher a data no calendário
3. Selecionar a hora de início e hora de fim
4. Marcar os serviços adicionais pretendidos (checkboxes)
5. Clicar "Calcular Preco" para ver o custo estimado
6. Clicar "Reservar" para confirmar

**Nota:** Se o cliente tiver reservas aceites não pagas, aparece um aviso e não consegue criar nova reserva.

#### Histórico de Reservas
- Tabela com colunas: ID, Espaço, Data, Horário, Preço Total, Estado, Pagamento
- Selecionar uma reserva mostra os detalhes completos
- Botão "Cancelar Reserva" permite cancelar (com confirmação)

---

### 12.6 Dashboard — Vista do Gestor

A sidebar apresenta as seguintes opções:

#### Menu Principal
1. **Os Meus Espacos** — Gerir espaços registados
2. **Novo Espaco** — Registar um novo espaço
3. **Reservas** — Gerir reservas de clientes

#### Conta
4. **O Meu Perfil** — Editar dados pessoais

#### Registar Novo Espaço
1. Preencher: Designação, Morada, Localidade
2. Selecionar o Tipo de Espaço (Futebol, Tenis, Piscina, Ginasio, Padel, Basquetebol, Voleibol, Outro)
3. Clicar "Registar Espaco"
4. Após criação, configurar preçário e serviços em "Os Meus Espacos"

#### Gerir Espaços Existentes
- Selecionar um espaço na tabela
- **Editar Espaço** — Altera designação, morada, localidade, tipo
- **Gerir Preçário** — Adicionar/remover faixas horárias com preço
- **Gerir Serviços** — Adicionar/remover serviços adicionais
- **Eliminar Espaço** — Remove o espaço (com confirmação)

#### Configurar Preçário (exemplo)
1. Abrir "Gerir Preçário" de um espaço
2. Definir hora início: 8, hora fim: 14, preço: 15.00
3. Clicar "Adicionar"
4. Definir hora início: 18, hora fim: 23, preço: 25.00
5. Clicar "Adicionar"
6. Resultado: o espaço está disponível das 8h-14h a 15€/h e das 18h-23h a 25€/h

#### Gerir Reservas
- Tabela com todas as reservas dos espaços do gestor
- **Filtrar por espaço:** Dropdown para ver apenas reservas de um espaço
- **Filtrar por estado:** Todos, Pendente, Aceite, Cancelada
- **Ações:**
  - "Aceitar" — Muda estado de Pendente para Aceite
  - "Cancelar" — Cancela a reserva
  - "Marcar Paga" — Regista que o cliente pagou
  - "Marcar Nao Paga" — Reverte o pagamento

---

### 12.7 Edição de Perfil

Disponível para ambos os perfis:
- Permite alterar nome, NIF, data de nascimento e telefone
- O email não pode ser alterado (é o identificador da conta)
- Para mudar a password, preencher o campo "Password" (deixar vazio mantém a atual)
- Clicar "Guardar Alteracoes" para salvar

---

## 13. Conceitos de POO Aplicados

### 13.1 Encapsulamento
- Todos os atributos das classes são `private`
- Acesso controlado através de getters e setters
- Lógica interna protegida (ex: `DataStore` com construtor privado)

### 13.2 Classes e Objetos
- 9 classes de modelo representando as entidades do domínio
- Cada classe tem responsabilidade única e bem definida

### 13.3 Enumerações (Enums)
- `TipoUtilizador` — CLIENTE, GESTOR
- `TipoEspaco` — FUTEBOL, TENIS, PISCINA, GINASIO, PADEL, BASQUETEBOL, VOLEIBOL, OUTRO
- `TipoServico` — UNITARIO, POR_HORA
- `EstadoReserva` — PENDENTE, ACEITE, CANCELADA

### 13.4 Padrão Singleton
- `DataStore` utiliza o padrão Singleton para garantir uma única instância de acesso aos dados

### 13.5 Composição
- `EspacoDesportivo` contém listas de `Precario` e `ServicoAdicional`
- `Reserva` contém lista de `ServicoAdicional`

### 13.6 Polimorfismo
- Métodos `toString()` sobrescritos em todas as classes de modelo
- `ServicoAdicional.calcularCusto()` comporta-se diferentemente conforme o `TipoServico`

### 13.7 Serialização
- Todas as classes de modelo implementam `Serializable`
- Permite guardar e restaurar o estado completo dos objetos

### 13.8 Streams e Lambdas
- Uso extensivo de Java Streams para filtragem, mapeamento e agregação
- Expressões lambda em event handlers do JavaFX

---

## 14. Padrões de Design Utilizados

| Padrão | Onde | Propósito |
|--------|------|-----------|
| Singleton | DataStore | Instância única de acesso a dados |
| MVC (adaptado) | Model/Service/UI | Separação de responsabilidades |
| Service Layer | AuthService, EspacoService, ReservaService | Isolamento da lógica de negócio |
| Observer | JavaFX Properties/Listeners | Atualização reativa da interface |

---

## 15. Dificuldades Encontradas

- Configuração do ambiente JavaFX com módulos Java
- Gestão da serialização com objetos compostos (listas dentro de objetos)
- Validação de conflitos de horários entre reservas
- Manutenção da consistência de dados entre vistas diferentes

---

## 16. Possíveis Melhorias Futuras

- Ligação a base de dados (MySQL/PostgreSQL) em vez de ficheiros
- Sistema de notificações para o cliente (reserva aceite/cancelada)
- Estatísticas e relatórios para o gestor (faturação, ocupação)
- Pesquisa e filtros avançados nos espaços (por localidade, tipo, preço)
- Sistema de avaliações e comentários dos espaços
- Recuperação de password por email

---

## 17. Conclusão

O projeto SportHub cumpre todos os requisitos funcionais propostos no enunciado:

- ✅ Dois perfis de utilizador (Cliente e Gestor)
- ✅ Registo livre de utilizadores
- ✅ Gestão completa de espaços desportivos
- ✅ Sistema de reservas com preçários variáveis
- ✅ Serviços adicionais (unitários e por hora)
- ✅ Validação de conflitos de horário
- ✅ Regra de pagamentos pendentes
- ✅ Interface gráfica em JavaFX
- ✅ Persistência de dados entre execuções
- ✅ Gestão de reservas pelo gestor (aceitar, cancelar, pagamentos)

A aplicação demonstra a aplicação prática dos conceitos de Programação Orientada a Objetos, incluindo encapsulamento, composição, padrões de design, e utilização de tecnologias modernas do ecossistema Java.

---

## 18. Referências

- Oracle. *Java SE 17 Documentation*. https://docs.oracle.com/en/java/javase/17/
- OpenJFX. *JavaFX 17 Documentation*. https://openjfx.io/javadoc/17/
- Apache Maven. *Maven Getting Started Guide*. https://maven.apache.org/guides/

---

**Programação Orientada a Objetos — 2025/26**  
Escola Superior de Tecnologia e Gestão — IPVC

**Autores:**
- [Nome do aluno 1]
- [Nome do aluno 2]
