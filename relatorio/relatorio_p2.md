# SportHub - Relatório do Projeto

## Parte 2: Funcionalidades e Regras de Negócio

---

## 8. Funcionalidades Implementadas

### 8.1 Sistema de Autenticação

#### Registo de Utilizador
- Qualquer pessoa pode registar-se como Cliente ou Gestor de Espaço
- Dados obrigatórios: nome, NIF (9 dígitos), data de nascimento, telefone, email, password
- Validações: email único, NIF com 9 dígitos, password mínima de 4 caracteres
- Classe responsável: `AuthService.registar()`

#### Login
- Autenticação por email e password
- Após login, o utilizador é redirecionado para o dashboard correspondente ao seu perfil
- Classe responsável: `AuthService.login()`

#### Logout
- Termina a sessão e regressa ao ecrã de login
- Classe responsável: `AuthService.logout()`

---

### 8.2 Funcionalidades do Cliente

#### 8.2.1 Consultar Espaços Disponíveis
- **Vista:** `EspacosListaView`
- Lista todos os espaços desportivos registados no sistema numa tabela
- Ao selecionar um espaço, mostra os detalhes: morada, localidade, tipo, preçário e serviços disponíveis
- Colunas: Designação, Morada, Localidade, Tipo

#### 8.2.2 Criar Nova Reserva
- **Vista:** `NovaReservaView`
- **Campos:** Espaço (dropdown), Data (date picker), Hora Início, Hora Fim
- **Serviços adicionais:** Checkboxes dinâmicos conforme o espaço selecionado
- **Calcular Preço:** Botão que calcula o preço estimado antes de confirmar
- **Validações realizadas:**
  - Data não pode ser no passado
  - Hora fim deve ser posterior à hora início
  - Cliente não pode ter reservas aceites não pagas
  - Espaço deve estar disponível no horário selecionado
  - Horário deve estar coberto pelo preçário do espaço
- Classe responsável: `ReservaService.criarReserva()`

#### 8.2.3 Consultar Histórico de Reservas
- **Vista:** `MinhasReservasView`
- Tabela com todas as reservas do cliente: ID, Espaço, Data, Horário, Preço Total, Estado, Pagamento
- Ao selecionar, mostra detalhes: preço base, serviços contratados, custo dos serviços, total
- Permite cancelar reservas que não estejam já canceladas

#### 8.2.4 Cancelar Reserva
- O cliente pode cancelar as suas reservas (desde que não estejam já canceladas)
- Pede confirmação antes de cancelar
- Classe responsável: `ReservaService.cancelarReserva()`

#### 8.2.5 Editar Perfil
- **Vista:** `PerfilView`
- Permite alterar: nome, NIF, data de nascimento, telefone, password
- O email não é editável (identificador único)
- Validações: campos obrigatórios, NIF 9 dígitos, password mínima 4 caracteres

---

### 8.3 Funcionalidades do Gestor de Espaço

#### 8.3.1 Listar Os Meus Espaços
- **Vista:** `GestorEspacosView`
- Lista os espaços do gestor com tabela: Designação, Morada, Localidade, Tipo
- Ao selecionar, mostra detalhes com preçários e serviços configurados
- Botões de ação: Editar, Gerir Preçário, Gerir Serviços, Eliminar

#### 8.3.2 Criar Novo Espaço
- **Vista:** `NovoEspacoView`
- **Campos:** Designação, Morada, Localidade, Tipo de Espaço (dropdown com 8 opções)
- O gestor pode adicionar quantos espaços entender
- Classe responsável: `EspacoService.criarEspaco()`

#### 8.3.3 Editar Espaço
- Diálogo modal que permite alterar: designação, morada, localidade, tipo
- Classe responsável: `EspacoService.atualizarEspaco()`

#### 8.3.4 Gerir Preçário
- Diálogo modal com:
  - Lista de preçários existentes
  - Botão para remover preçário selecionado
  - Formulário para adicionar novo preçário: Hora Início (spinner), Hora Fim (spinner), Preço/hora
- Validações: hora início < hora fim, preço positivo
- Classe responsável: `EspacoService.adicionarPrecario()`, `EspacoService.removerPrecario()`

#### 8.3.5 Gerir Serviços Adicionais
- Diálogo modal com:
  - Lista de serviços existentes
  - Botão para remover serviço
  - Formulário para adicionar: Nome, Descrição, Preço, Tipo (Unitário ou Por Hora)
- Classe responsável: `EspacoService.adicionarServico()`, `EspacoService.removerServico()`

#### 8.3.6 Eliminar Espaço
- Remove um espaço após confirmação
- Classe responsável: `EspacoService.removerEspaco()`

#### 8.3.7 Gerir Reservas
- **Vista:** `GestorReservasView`
- Tabela com todas as reservas dos seus espaços: ID, Cliente, Espaço, Data, Horário, Total, Estado, Paga
- **Filtros:** Por espaço (dropdown), Por estado (Todos/Pendente/Aceite/Cancelada)
- **Ações disponíveis:**
  - **Aceitar** — aceita uma reserva pendente
  - **Cancelar** — cancela uma reserva
  - **Marcar Paga** — marca a reserva como paga
  - **Marcar Não Paga** — reverte o estado de pagamento
- Classes responsáveis: `ReservaService.aceitarReserva()`, `ReservaService.cancelarReserva()`, `ReservaService.marcarPagamento()`

#### 8.3.8 Editar Perfil
- Mesma funcionalidade que o cliente (vista partilhada: `PerfilView`)

---

## 9. Regras de Negócio

| # | Regra | Implementação |
|---|-------|---------------|
| 1 | Um cliente não pode reservar se tiver reservas aceites não pagas | `ReservaService.clienteTemReservasNaoPagas()` |
| 2 | Um espaço só pode ter uma reserva num dado momento | `ReservaService.espacoDisponivel()` |
| 3 | Reservas só são possíveis em horários com preçário definido | `ReservaService.calcularPrecoBase()` retorna -1 se não coberto |
| 4 | O preço varia conforme a faixa horária | Cada hora é calculada individualmente pelo preçário |
| 5 | Serviços adicionais podem ser unitários ou por hora | `ServicoAdicional.calcularCusto(duracaoHoras)` |
| 6 | Reservas iniciam com estado "Pendente" | Construtor de `Reserva` |
| 7 | Apenas o gestor pode aceitar/cancelar reservas dos seus espaços | `GestorReservasView` filtra por gestor |
| 8 | Não é possível reservar no passado | Validação em `ReservaService.criarReserva()` |
| 9 | Hora de fim deve ser posterior à hora de início | Validação em `ReservaService.criarReserva()` |
| 10 | Email é único no sistema | Validação em `AuthService.registar()` |

---

## 10. Cálculo de Preços

### Preço Base
O preço base de uma reserva é calculado hora a hora. Para cada hora do intervalo reservado, o sistema procura no preçário do espaço o valor correspondente:

```
Exemplo: Reserva das 09:00 às 12:00 num ginásio com preçário:
  - 08:00-14:00 → 15.00 EUR/hora
  - 18:00-23:00 → 25.00 EUR/hora

Cálculo: 3 horas × 15.00 EUR = 45.00 EUR
```

### Serviços Adicionais
- **Unitário:** Cobrado uma vez independentemente da duração (ex: aluguer de raquete = 5.00 EUR)
- **Por Hora:** Multiplicado pela duração (ex: iluminação noturna 3.00 EUR/hora × 3 horas = 9.00 EUR)

### Preço Total
```
Preço Total = Preço Base + Soma dos Custos dos Serviços Adicionais
```

---

## 11. Fluxo de Estados de uma Reserva

```
                 ┌──────────┐
    Criação ───► │ PENDENTE │
                 └─────┬────┘
                       │
           ┌───────────┼───────────┐
           ▼                       ▼
    ┌─────────────┐         ┌───────────┐
    │   ACEITE    │         │ CANCELADA │
    └──────┬──────┘         └───────────┘
           │                       ▲
           │ (cancelar)            │
           └───────────────────────┘
```

- **PENDENTE** — Estado inicial após criação pelo cliente
- **ACEITE** — O gestor aprovou a reserva
- **CANCELADA** — Cancelada pelo cliente ou pelo gestor

O pagamento é um campo independente do estado (uma reserva aceite pode estar paga ou não paga).
