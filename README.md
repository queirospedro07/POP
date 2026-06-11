# SportHub

Sistema de gestao de reservas de espacos desportivos, desenvolvido em Java 17 com interface grafica JavaFX.

## Como Executar

**Opcao 1 - Script (Windows):**
```
run.bat
```

**Opcao 2 - Maven:**
```
mvn javafx:run
```

**Opcao 3 - IDE (IntelliJ / Eclipse):**  
Abrir como projeto Maven e executar `com.sporthub.MainApp`.

## Contas de Teste

| Perfil  | Email            | Password |
|---------|------------------|----------|
| Cliente | pedro@email.com  | 1234     |
| Gestor  | ana@email.com    | 1234     |

## Funcionalidades

### Cliente
- Visualizar espacos desportivos e respetiva informacao
- Criar reservas com selecao de servicos adicionais
- Calcular preco estimado antes de reservar
- Consultar historico de reservas
- Cancelar reservas
- Editar perfil pessoal

### Gestor de Espaco
- Criar, editar e eliminar espacos desportivos
- Definir precarios por faixa horaria
- Configurar servicos adicionais (unitarios ou por hora)
- Aceitar ou cancelar reservas de clientes
- Marcar reservas como pagas
- Filtrar reservas por espaco e estado
- Editar perfil pessoal

### Regras de Negocio
- Cliente com reservas aceites nao pagas nao pode fazer novas reservas
- Reservas so sao permitidas em horarios com precario definido
- Um espaco nao pode ter duas reservas no mesmo periodo
- Preco varia conforme a faixa horaria

## Estrutura do Projeto

```
src/main/java/com/sporthub/
├── MainApp.java              Ponto de entrada
├── model/                    Classes de dominio
│   ├── Utilizador.java
│   ├── EspacoDesportivo.java
│   ├── Reserva.java
│   ├── Precario.java
│   ├── ServicoAdicional.java
│   └── Enums (TipoUtilizador, TipoEspaco, TipoServico, EstadoReserva)
├── data/                     Persistencia
│   ├── DataStore.java
│   └── DataInitializer.java
├── service/                  Logica de negocio
│   ├── AuthService.java
│   ├── EspacoService.java
│   └── ReservaService.java
└── ui/                       Interface grafica
    ├── LoginView.java
    ├── DashboardView.java
    ├── EspacosListaView.java
    ├── NovaReservaView.java
    ├── MinhasReservasView.java
    ├── GestorEspacosView.java
    ├── GestorReservasView.java
    ├── NovoEspacoView.java
    └── PerfilView.java
```

## Tecnologias
- Java 17
- JavaFX 17
- Serializacao Java (persistencia em ficheiro)
- Maven (gestao de dependencias)

## Autores
- [Nome do aluno 1]
- [Nome do aluno 2]

**Programacao Orientada a Objetos - 2025/26**  
Escola Superior de Tecnologia e Gestao - IPVC
