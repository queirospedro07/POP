# SportHub - Relatório do Projeto

## Parte 1: Introdução, Objetivos e Arquitetura

---

## 1. Introdução

O presente relatório descreve o desenvolvimento do projeto **SportHub**, um sistema de gestão de reservas de espaços desportivos, realizado no âmbito da unidade curricular de Programação Orientada a Objetos (2025/26), na Escola Superior de Tecnologia e Gestão do IPVC.

O SportHub é uma aplicação desktop desenvolvida em **Java 17** com interface gráfica em **JavaFX**, que permite a gestão completa de reservas de espaços desportivos, suportando dois perfis de utilizador: **Cliente** e **Gestor de Espaço**.

A aplicação foi desenvolvida seguindo os princípios da programação orientada a objetos, nomeadamente encapsulamento, herança, polimorfismo e abstração, garantindo um código modular, reutilizável e de fácil manutenção.

---

## 2. Objetivos

Os principais objetivos deste trabalho são:

- Desenvolver uma aplicação funcional que permita a gestão de reservas de espaços desportivos
- Aplicar os conceitos de Programação Orientada a Objetos (POO) estudados na UC
- Implementar persistência de dados para manter informação entre execuções
- Criar uma interface gráfica profissional e intuitiva usando JavaFX
- Implementar regras de negócio realistas (conflitos de horário, pagamentos pendentes, preçários variáveis)
- Suportar múltiplos perfis de utilizador com permissões diferenciadas

---

## 3. Tecnologias Utilizadas

| Tecnologia | Versão | Função |
|------------|--------|--------|
| Java | 17 | Linguagem de programação principal |
| JavaFX | 17.0.2 | Interface gráfica (GUI) |
| Maven | 3.9.6 | Gestão de dependências e build |
| Serialização Java | - | Persistência de dados em ficheiros binários |

---

## 4. Arquitetura do Sistema

O projeto segue uma arquitetura em camadas (layered architecture), com separação clara de responsabilidades:

```
┌──────────────────────────────────────────┐
│              UI (JavaFX Views)            │  ← Interface Gráfica
├──────────────────────────────────────────┤
│            Service Layer                  │  ← Lógica de Negócio
├──────────────────────────────────────────┤
│            Data Layer                     │  ← Persistência
├──────────────────────────────────────────┤
│            Model Layer                    │  ← Classes de Domínio
└──────────────────────────────────────────┘
```

### 4.1 Camada de Modelo (`com.sporthub.model`)

Contém as classes que representam as entidades do domínio:

- **Utilizador** — Representa um utilizador (cliente ou gestor)
- **EspacoDesportivo** — Representa um espaço disponível para reserva
- **Reserva** — Representa uma reserva feita por um cliente
- **Precario** — Define o preço por hora para uma faixa horária
- **ServicoAdicional** — Serviço extra que pode ser adicionado a uma reserva
- **Enums** — TipoUtilizador, TipoEspaco, TipoServico, EstadoReserva

### 4.2 Camada de Dados (`com.sporthub.data`)

- **DataStore** — Singleton que gere toda a persistência (leitura/escrita de ficheiros)
- **DataInitializer** — Cria dados de demonstração na primeira execução

### 4.3 Camada de Serviço (`com.sporthub.service`)

- **AuthService** — Autenticação, registo e gestão de sessão
- **EspacoService** — CRUD de espaços, preçários e serviços
- **ReservaService** — Criação de reservas com validação de regras de negócio

### 4.4 Camada de Interface (`com.sporthub.ui`)

- **LoginView** — Ecrã de login e registo
- **DashboardView** — Layout principal com sidebar e área de conteúdo
- **EspacosListaView** — Listagem de espaços (cliente)
- **NovaReservaView** — Formulário de nova reserva (cliente)
- **MinhasReservasView** — Histórico de reservas (cliente)
- **GestorEspacosView** — Gestão de espaços (gestor)
- **NovoEspacoView** — Formulário de novo espaço (gestor)
- **GestorReservasView** — Gestão de reservas (gestor)
- **PerfilView** — Edição de dados pessoais (ambos)

---

## 5. Estrutura de Ficheiros

```
sporthub/
├── src/main/java/
│   ├── module-info.java
│   └── com/sporthub/
│       ├── MainApp.java
│       ├── model/
│       │   ├── Utilizador.java
│       │   ├── EspacoDesportivo.java
│       │   ├── Reserva.java
│       │   ├── Precario.java
│       │   ├── ServicoAdicional.java
│       │   ├── TipoUtilizador.java
│       │   ├── TipoEspaco.java
│       │   ├── TipoServico.java
│       │   └── EstadoReserva.java
│       ├── data/
│       │   ├── DataStore.java
│       │   └── DataInitializer.java
│       ├── service/
│       │   ├── AuthService.java
│       │   ├── EspacoService.java
│       │   └── ReservaService.java
│       └── ui/
│           ├── LoginView.java
│           ├── DashboardView.java
│           ├── EspacosListaView.java
│           ├── NovaReservaView.java
│           ├── MinhasReservasView.java
│           ├── GestorEspacosView.java
│           ├── GestorReservasView.java
│           ├── NovoEspacoView.java
│           └── PerfilView.java
├── src/main/resources/
│   └── styles.css
├── pom.xml
├── run.bat
└── relatorio/
    ├── relatorio_p1.md
    ├── relatorio_p2.md
    └── relatorio_p3.md
```

---

## 6. Diagrama de Classes (Simplificado)

```
                    ┌─────────────────┐
                    │   Utilizador    │
                    ├─────────────────┤
                    │ id: int         │
                    │ nome: String    │
                    │ nif: String     │
                    │ email: String   │
                    │ password: String│
                    │ tipo: TipoUtil. │
                    └────────┬────────┘
                             │
              ┌──────────────┼──────────────┐
              │                             │
     ┌────────▼────────┐          ┌────────▼────────┐
     │  (tipo=CLIENTE) │          │  (tipo=GESTOR)  │
     │  Faz reservas   │          │  Gere espaços   │
     └────────┬────────┘          └────────┬────────┘
              │                             │
              │ clienteId                   │ gestorId
              ▼                             ▼
     ┌─────────────────┐          ┌─────────────────────┐
     │     Reserva     │          │  EspacoDesportivo   │
     ├─────────────────┤          ├─────────────────────┤
     │ id, data        │◄────────►│ id, designacao      │
     │ horaInicio/Fim  │ espacoId │ morada, localidade  │
     │ precoBase       │          │ tipoEspaco          │
     │ estado          │          │ precarios: List     │
     │ paga: boolean   │          │ servicos: List      │
     │ servicos: List  │          └─────────────────────┘
     └─────────────────┘                    │
                                            │ contém
                              ┌─────────────┼─────────────┐
                              ▼                           ▼
                    ┌─────────────────┐         ┌─────────────────┐
                    │    Precario     │         │ ServicoAdicional│
                    ├─────────────────┤         ├─────────────────┤
                    │ horaInicio      │         │ id, nome        │
                    │ horaFim         │         │ preco           │
                    │ precoPorHora    │         │ tipoServico     │
                    └─────────────────┘         └─────────────────┘
```

---

## 7. Persistência de Dados

Os dados são persistidos em ficheiros binários usando serialização Java (ObjectOutputStream / ObjectInputStream), guardados na pasta `data/`:

| Ficheiro | Conteúdo |
|----------|----------|
| `utilizadores.dat` | Lista de todos os utilizadores registados |
| `espacos.dat` | Lista de espaços desportivos |
| `reservas.dat` | Lista de todas as reservas |

A classe `DataStore` funciona como Singleton e é responsável por:
- Carregar dados na inicialização da aplicação
- Guardar automaticamente após cada operação de escrita
- Gerir IDs únicos auto-incrementados para cada entidade
