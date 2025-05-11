# Project Name

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