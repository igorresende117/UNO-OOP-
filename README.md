# UNO & Standard Card Game Engine

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-green?style=for-the-badge)

A complete, Object-Oriented card game engine developed in Java. This project supports both the official UNO rules and a standard 52-card deck mode, featuring a graphical user interface (GUI) built with Java Swing.

## Features

*   **Dual Game Modes:** Play official UNO or a standard card game (Convencional).
*   **Graphical Interface:** Fully interactive GUI using Java Swing (`BorderLayout` and `BoxLayout`), with visual card management, scrolling hands, and action pop-ups.
*   **Custom Data Structures:** Turn management is handled efficiently using a custom **Doubly Circular Linked List** (`ListaJogadores`), allowing seamless clockwise and counter-clockwise turn rotations.
*   **Advanced OOP Principles:** Strong use of inheritance, polymorphism, and encapsulation (e.g., Abstract `Carta` and `Baralho` classes extended by UNO and Standard variations).
*   **Dynamic Deck Management:** Automatic reshuffling of the discard pile into the draw pile when cards run out.

## Screenshots

### Initial Configuration
![Tela Inicial](./assets/tela_inicial.png)

### Game Table
![Mesa de Jogo](./assets/tela_jogo.png)

### Victory Screen
![Vitoria](./assets/vitoria.png)

## Versions

This repository contains two major versions of the project:
*   **V2 (Main Branch):** The current version featuring the Java Swing Graphical User Interface.
*   **V1 (Console Tag):** The legacy terminal/console-based version. You can access it via the `v1.0-console` tag in the repository releases.

## Technologies Used

*   **Language:** Java
*   **GUI Library:** Java Swing & AWT

## How to Run

You can download the latest `.jar` executable from the [Releases](https://github.com/igorresende117/UNO-OOP-/releases) page and run it directly. 

To run from the source code:
1.  Clone the repository:
    ```bash
    git clone [https://github.com/igorresende117/UNO-OOP-.git](https://github.com/igorresende117/UNO-OOP-.git)
    ```
2.  Navigate to the project directory:
    ```bash
    cd UNO-OOP-
    ```
3.  Compile the source code:
    ```bash
    javac -d bin src/**/*.java
    ```
4.  Run the application:
    ```bash
    java -cp bin APP
    ```

## Architecture Overview

The system is divided into clear, highly cohesive packages:
*   `gui`: Handles the visual presentation (Swing frames, panels, and event listeners).
*   `modelo`: The core game engine (`Jogo`) managing rules and states.
*   `cartas` & `baralhos`: Card generation, effects, and deck behaviors using abstraction.
*   `jogadores`: Player state and node management for the linked list.

## License

This project is [MIT](https://choosealicense.com/licenses/mit/) licensed.