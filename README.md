# Buscaminas Distribuido - TCP Socket Game

Aplicación distribuida en Java basada en la arquitectura **Cliente-Servidor** sobre sockets TCP, gestionada con un proyecto multimódulo en **Gradle**. Permite a un cliente interactuar mediante una consola de comandos con un servidor remoto que procesa las reglas del juego de Buscaminas.

---

## Resumen del Proyecto

El proyecto implementa la lógica clásica de un Buscaminas dividiendo la responsabilidad en dos capas:
- **Servidor TCP (`:server`):** Mantiene el estado del tablero (`BoardGame`), procesa jugadas, valida victorias/derrotas y responde solicitudes en formato JSON.
- **Cliente TCP (`:client`):** Interfaz interactiva por consola que captura comandos del usuario, envía DTOs (`Request`) al servidor y renderiza los resultados visuales (`Response`) con soporte de colores ANSI.

La comunicación se gestiona serializando y deserializando peticiones usando la librería **Gson (2.10.1)**.

---

##  Estructura del Proyecto

```text
clase_tcp_udp/
├── client/                                  # Módulo Cliente
│   ├── build.gradle
│   └── src/main/java/co/icesi/buscaminas/client/
│       ├── dtos/                            # Cell, Request, Response
│       ├── BuscaminasTCPClient.java         # Socket TCP de envío/recepción
│       └── MainClient.java                  # Menú principal e interfaz de consola
│
├── server/                                  # Módulo Servidor
│   ├── build.gradle
│   └── src/main/java/co/icesi/buscaminas/
│       ├── controllers/                     # TCPController (Socket listener)
│       ├── model/                           # BoardGame, Cell
│       ├── services/                        # ServicesImpl (Lógica de negocio)
│       └── Main.java                        # Punto de entrada del servidor
│
├── build.gradle                             # Gradle raíz
├── settings.gradle                          # Definición de submódulos
└── gradlew / gradlew.bat                    # Gradle Wrapper

```

---

## Configuración de Red (Cambiar la IP)

Por defecto, la aplicación se conecta a `localhost` en el puerto `12345`. Para ejecutar el cliente y el servidor en máquinas distintas dentro de una misma red local:

1. Abre el archivo en el cliente:
`client/src/main/java/co/icesi/buscaminas/client/MainClient.java`
2. Modifica la variable de clase `HOST` reemplazando `"localhost"` por la dirección IP privada del equipo donde se ejecuta el servidor:

```java
public class MainClient {

    // ── CONFIGURACIÓN DE RED ──────────────────────────────
    private static final String HOST = "192.168.1.15"; // <-- Cambiar aquí por la IP del servidor
    private static final int PORT = 12345;              // <-- Puerto del servidor TCP
    // ──────────────────────────────────────────────────────

```

---

## Comandos de Ejecución

Abre dos terminales en la raíz del proyecto (`clase_tcp_udp`):

### 1️⃣ Compilar el proyecto completo

```powershell
.\gradlew build

```

### 2️⃣ Iniciar el Servidor TCP

Ejecuta el servidor en la primera terminal. Se quedará escuchando en el puerto configurado:

```powershell
.\gradlew :server:run

```

### 3️⃣ Iniciar el Cliente TCP (Modo Interactivo)

Ejecuta el cliente en la segunda terminal usando `--console=plain` para evitar que la barra de carga de Gradle distorsione el menú:

```powershell
.\gradlew :client:run --console=plain

```

---

## Acciones Soportadas (Protocolo TCP)

El menú interactivo permite enviar las siguientes acciones (`Request.action`) hacia el servidor:

| Opción | Acción | Descripción |
| --- | --- | --- |
| **1** | `INIT_GAME` | Inicia un nuevo tablero especificando filas (n), columnas (m) y cantidad de minas. |
| **2** | `SELECT_CELL` | Destapa una casilla (i, j). Si es una mina, desencadena derrota automática. |
| **3** | `MARK_CELL` | Coloca o remueve una bandera de advertencia `M` en (i, j). |
| **4** | `GET_BOARD` | Solicita la matriz actualizada con el estado visible de cada celda. |
| **5** | `SOW_ALL` | Se rinde y revela la ubicación de todas las minas en el tablero. |

---

## Requisitos del Sistema

* **JDK:** Java 17 o superior.
* **Entorno recomendado:** Visual Studio Code (con extensiones de Java).
