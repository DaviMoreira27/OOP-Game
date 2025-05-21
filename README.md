# Project Name
(commit test)
A game developed using Java and OOP concepts for the SSC0504 class in the Systems Information course at USP.

## Makefile Commands

### `make build`

Compiles all `.java` files from the `src/` directory and outputs the compiled `.class` files into the `build/` directory.

```bash
make build
```

### `make run`

Compiles the project (if not already compiled) and runs the main class `(Main)` using the compiled files from `build/`.

```bash
make build
```

### `make export`

Packages all compiled `.class` files into a single JAR file named `PROJECT-NAME-HERE.jar` in the `dist/` directory. It also sets the entry point `(Main-Class)` so that the JAR can be run directly.

```bash
make build
```

## TODO

[X] No clique do mouse, o personagem controlavel deve dar tiros

[] Adicionar mais inimigos que atiram, e perseguem o personagem

[X] Implementar barra de vida no personagem principal e nos inimigos

[-] Adicionar dano e colisões

[] Adicionar bosses, inimigos com mais vida, dano e velocidade. Ao derrotar-los, transportar o personagem até a próxima fase

[] Adicionar objetos e paredes no mapa

[] Bloquear o personagem de atravessar paredes e objetos

[] Adicionar save no jogo

[] Deve ser possível adicionar inimigos dinamicamente no jogo
