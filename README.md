# 🏰 DungeonGame - Aventura Educativa de Programación

Un juego de aventura educativo desarrollado en Java que enseña conceptos de programación a través de combates con preguntas técnicas, exploración de mazmorras temáticas y progresión por niveles de desarrollador.

## 📋 Índice

- [🎮 Descripción del Juego](#-descripción-del-juego)
- [🚀 Historia de Desarrollo](#-historia-de-desarrollo)
- [🎯 Características Principales](#-características-principales)
- [⌨️ Comandos del Juego](#️-comandos-del-juego)
- [🗺️ Estructura del Juego](#️-estructura-del-juego)
- [🏗️ Arquitectura del Código](#️-arquitectura-del-código)
- [🛠️ Instalación y Ejecución](#️-instalación-y-ejecución)
- [🎓 Objetivos Educativos](#-objetivos-educativos)
- [📈 Progresión del Juego](#-progresión-del-juego)
- [🔧 Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [📚 Glosario de Términos](#-glosario-de-términos)

---

## 🎮 Descripción del Juego

**DungeonGame** es una aventura educativa donde el jugador asume el rol de un desarrollador que debe evolucionar desde **Trainee** hasta **Senior Developer**. El objetivo principal es encontrar los tesoros del conocimiento de cada nivel derrotando bugs enemigos mediante respuestas correctas a preguntas de programación.

### 🌟 Historia del Juego

En el reino del código existe una leyenda sobre un poderoso **Bug Ancestral** que perturba la armonía de los programas. Solo encontrando la llave del tesoro supremo podrás restaurar la paz y convertirte en un verdadero **Senior Developer**.

---

## 🚀 Historia de Desarrollo

### 🎯 **Motivación**
El proyecto nació de la necesidad de crear una herramienta educativa interactiva que hiciera el aprendizaje de programación más divertido y memorable, combinando mecánicas de juego con conceptos técnicos reales.

### 📅 **Cronología de Desarrollo**

#### **Fase 1: Concepto y Diseño (Julio 2025)**
- **Idea inicial**: Crear un juego educativo basado en texto
- **Decisión de temática**: Mazmorras con bugs como enemigos
- **Definición de mecánicas**: Combate basado en preguntas técnicas

#### **Fase 2: Arquitectura Base (Julio 2025)**
- Implementación del patrón **MVC** (Model-View-Controller)
- Creación de interfaces principales (`Room`, `GameObject`, `Interactable`)
- Desarrollo del sistema de jugador con estadísticas

#### **Fase 3: Sistema de Combate (Julio 2025)**
- Diseño de la clase `BugEnemy` con tipos de bugs
- Implementación del sistema de preguntas educativas
- Creación de mecánicas de respuesta (A/B/C/D o texto completo)

#### **Fase 4: Mapas y Navegación (Julio 2025)**
- Desarrollo del sistema de mapas por niveles
- Implementación de navegación con W/A/S/D
- Creación de diferentes tipos de salas (Tesoros, Enemigos, Bloqueadas)

#### **Fase 5: Pulido y UX (Julio 2025)**
- Mejora significativa de la interfaz de usuario
- Implementación de pistas contextuales
- Adición de mapas visuales y ayudas de navegación

#### **Fase 6: Depuración y Optimización (Julio 2025)**
- Corrección de errores de compilación
- Optimización del flujo de juego
- Mejora de mensajes y tutorial para nuevos jugadores

---

## 🎯 Características Principales

### 🎮 **Mecánicas de Juego**
- **Progresión por Niveles**: Trainee → Junior → Senior
- **Sistema de Combate Educativo**: Preguntas de programación
- **Exploración de Mapas**: Mazmorras temáticas por nivel
- **Inventario y Objetos**: Tesoros, llaves y herramientas
- **Navegación Visual**: Mapas en tiempo real

### 🧠 **Componente Educativo**
- **100+ Preguntas** de programación clasificadas por dificultad
- **Temas Cubiertos**: POO, Algoritmos, Patrones de Diseño, Arquitectura
- **Explicaciones Educativas**: Feedback inmediato tras cada respuesta
- **Progresión Adaptativa**: Dificultad creciente por nivel

### 🏗️ **Calidad del Código**
- **Patrones de Diseño**: Factory, Template Method, Strategy
- **Principios SOLID**: Especialmente SRP e ISP
- **Arquitectura Limpia**: Separación clara de responsabilidades
- **Manejo de Errores**: Sistema robusto de recovery

---

## ⌨️ Comandos del Juego

### 🎯 **Comando Principal**
```
F - INTERACTUAR
```
**¡El comando más importante!** Úsalo en cada sala para:
- 💰 Recoger tesoros y llaves
- ⚔️ Luchar contra enemigos
- 🔒 Abrir puertas bloqueadas
- 🏠 Explorar áreas

### 🧭 **Movimiento**
```
W - Mover hacia el Norte ⬆️
A - Mover hacia el Oeste ⬅️
S - Mover hacia el Sur ⬇️
D - Mover hacia el Este ➡️
```

### 📊 **Información**
```
V      - Ver inventario y objetos
STATUS - Ver mapa completo y estadísticas
H      - Mostrar ayuda completa
```

### ⚙️ **Sistema**
```
X    - Usar objeto del inventario
SAVE - Guardar partida (futuro)
LOAD - Cargar partida (futuro)
Q    - Salir del juego
```

---

## 🗺️ Estructura del Juego

### 📊 **Niveles de Dificultad**

| Nivel | Tamaño | Temas | Enemigos | Objetivo |
|-------|--------|-------|----------|----------|
| **Trainee** | 5x5 | Conceptos Básicos | SyntaxError, NullPointer | Tesoro del Conocimiento Trainee |
| **Junior** | 6x6 | Algoritmos, Patrones | LogicError, RuntimeError | Tesoro del Conocimiento Junior |
| **Senior** | 8x8 | Arquitectura, SOLID | MemoryLeak, InfiniteLoop | Tesoro del Conocimiento Supremo |

### 🏛️ **Tipos de Salas**

- **🏠 Salas Vacías**: Exploración y descanso
- **💰 Salas de Tesoros**: Objetos valiosos y llaves
- **⚔️ Salas de Enemigos**: Combate con preguntas
- **🔒 Salas Bloqueadas**: Requieren llaves específicas
- **🗝️ Salas Secretas**: Contenido oculto especial

---

## 🏗️ Arquitectura del Código

### 📁 **Estructura de Paquetes**
```
src/
├── game/
│   ├── GameEngine.java      # Motor principal del juego
│   └── GameMap.java         # Sistema de mapas
├── models/
│   ├── player/              # Sistema del jugador
│   ├── enemies/             # Enemigos y combate
│   └── items/               # Objetos e inventario
├── rooms/                   # Tipos de salas
├── interfaces/              # Contratos y abstracciones
└── utils/                   # Utilidades y constantes
```

### 🎨 **Patrones Implementados**

#### **Factory Pattern**
```java
// Creación de objetos específicos
TreasureRoomFactory.createDeveloperToolsVault()
EnemyRoomFactory.createTraineeBugRoom()
EmptyRoomFactory.createMainHall()
```

#### **Template Method**
```java
// Flujo común con pasos específicos
public abstract class BaseRoom implements Room {
    public final void interact(Player player) {
        onFirstVisit(player);
        executeRoomInteraction(player); // Implementado por subclases
        onInteractionComplete(player);
    }
}
```

#### **Strategy Pattern**
```java
// Diferentes comportamientos por tipo de sala
interface Room {
    void interact(Player player);
}
// Implementaciones: EmptyRoom, TreasureRoom, EnemyRoom, LockedRoom
```

### 🔧 **Principios SOLID Aplicados**

- **SRP**: Cada clase tiene una responsabilidad específica
- **OCP**: Extensible sin modificar código existente
- **LSP**: Las subclases son intercambiables
- **ISP**: Interfaces específicas (`Room`, `GameObject`, `Interactable`)
- **DIP**: Dependencias en abstracciones, no en implementaciones

---

## 🛠️ Instalación y Ejecución

### 📋 **Requisitos**
- **Java 21** o superior
- **Maven 3.6+** para gestión de dependencias
- **IDE** recomendado: IntelliJ IDEA o Eclipse

### 🚀 **Pasos de Instalación**

1. **Clonar el repositorio**
```bash
git clone https://github.com/tu-usuario/DungeonGame.git
cd DungeonGame
```

2. **Compilar el proyecto**
```bash
mvn clean compile
```

3. **Ejecutar el juego**
```bash
mvn exec:java -Dexec.mainClass="Main"
```

### ▶️ **Ejecución Alternativa**
```bash
# Desde la carpeta target/classes
java Main
```

---

## 🎓 Objetivos Educativos

### 📚 **Conceptos Cubiertos**

#### **Nivel Trainee**
- Variables y tipos de datos
- Estructuras de control (if, while, for)
- Conceptos básicos de POO
- Manejo básico de excepciones

#### **Nivel Junior**
- Estructuras de datos (Arrays, Lists, Maps)
- Algoritmos de ordenamiento y búsqueda
- Patrones de diseño básicos (Singleton, Factory)
- Complejidad algorítmica (Big O)

#### **Nivel Senior**
- Arquitectura de software (Microservicios, SOLID)
- Patrones arquitectónicos avanzados (CQRS, Saga)
- Principios de diseño y mejores prácticas
- Gestión de sistemas distribuidos

### 🎯 **Metodología de Enseñanza**
- **Aprendizaje Activo**: El jugador debe responder correctamente para avanzar
- **Feedback Inmediato**: Explicaciones tras cada respuesta
- **Progresión Gradual**: Dificultad creciente y adaptativa
- **Gamificación**: Puntos, niveles y recompensas por conocimiento

---

## 📈 Progresión del Juego

### 🎮 **Flujo Típico de Juego**

1. **Inicio**: Tutorial y creación de personaje
2. **Exploración**: Navegación por el mapa del nivel
3. **Recolección**: Búsqueda de tesoros y llaves
4. **Combate**: Enfrentamiento con enemigos mediante preguntas
5. **Progresión**: Obtención del tesoro final del nivel
6. **Avance**: Transición al siguiente nivel de dificultad

### 🏆 **Condiciones de Victoria**

- **Por Nivel**: Obtener el "Tesoro del Conocimiento" correspondiente
- **Final**: Derrotar al Bug Ancestral y obtener el "Tesoro del Conocimiento Supremo"

### 📊 **Sistema de Estadísticas**
- Salas exploradas
- Enemigos derrotados
- Tesoros encontrados
- Experiencia acumulada
- Tiempo de juego

---

## 🔧 Tecnologías Utilizadas

### 💻 **Core Technologies**
- **Java 21**: Lenguaje principal con features modernas
- **Maven**: Gestión de dependencias y ciclo de vida
- **Scanner**: Entrada de usuario por consola
- **Collections Framework**: Manejo eficiente de datos

### 🎨 **Patrones y Arquitecturas**
- **MVC Pattern**: Separación de lógica, modelo y vista
- **Factory Pattern**: Creación de objetos complejos
- **Template Method**: Algoritmos con pasos variables
- **Strategy Pattern**: Comportamientos intercambiables

### 🛡️ **Calidad y Robustez**
- **Exception Handling**: Manejo robusto de errores
- **Input Validation**: Validación exhaustiva de entradas
- **Memory Management**: Uso eficiente de recursos
- **Debug System**: Sistema de depuración integrado

---

## 📚 Glosario de Términos

### 🎮 **Términos de Juego**

| Término | Definición | Ejemplo |
|---------|------------|---------|
| **Bug Ancestral** | El enemigo final del juego, representa el error más poderoso del reino del código | "Solo derrotando al Bug Ancestral puedes completar el juego" |
| **Tesoro del Conocimiento** | Objeto final de cada nivel que representa el dominio de conceptos de programación | "Tesoro del Conocimiento Trainee", "Tesoro del Conocimiento Supremo" |
| **Llave del Saber** | Objeto especial que desbloquea áreas restringidas en cada nivel | "Llave del Conocimiento Trainee" abre la puerta del boss |
| **Sanctum** | Sala sagrada donde residen los objetos más valiosos del juego | "Sanctum de POO", "Sanctum del Maestro Trainee" |
| **Mundo de Nivel** | Mapa temático correspondiente a cada nivel de dificultad | "Mundo Trainee", "Mundo Junior", "Mundo Senior" |

### 👨‍💻 **Niveles de Desarrollador**

| Término | Descripción | Características |
|---------|-------------|-----------------|
| **Trainee** | Nivel inicial del desarrollador en formación | Conceptos básicos, preguntas simples, mapa 5x5 |
| **Junior** | Desarrollador con conocimientos intermedios | Algoritmos y patrones, preguntas complejas, mapa 6x6 |
| **Senior** | Desarrollador experto con dominio avanzado | Arquitectura de software, preguntas expertas, mapa 8x8 |
| **Maestro Trainee** | Boss del primer nivel, guardián del conocimiento básico | Enemigo con múltiples preguntas sobre fundamentos |
| **Arquitecto Junior** | Boss del segundo nivel, experto en conceptos intermedios | Enemigo especializado en algoritmos y patrones |

### 🐛 **Tipos de Bugs (Enemigos)**

| Tipo de Bug | Descripción | Nivel de Aparición |
|-------------|-------------|-------------------|
| **SyntaxError** | Error básico de sintaxis en el código | Trainee |
| **NullPointer** | Error por referencias nulas no controladas | Trainee/Junior |
| **LogicError** | Error en la lógica del programa | Junior |
| **RuntimeError** | Error que ocurre durante la ejecución | Junior/Senior |
| **MemoryLeak** | Fuga de memoria por mal manejo de recursos | Senior |
| **InfiniteLoop** | Bucle sin condición de salida que bloquea el sistema | Senior (Boss Final) |

### 🏛️ **Tipos de Salas**

| Tipo | Icono | Propósito | Mecánica |
|------|-------|-----------|----------|
| **Sala Principal** | 🏠 | Punto de inicio y orientación | Navegación central |
| **Laboratorio** | ⚗️ | Salas temáticas de aprendizaje | Contienen enemigos específicos |
| **Biblioteca** | 📚 | Repositorios de conocimiento | Contienen tesoros educativos |
| **Academia** | 🎓 | Centros de enseñanza especializada | Enemigos con preguntas temáticas |
| **Fortaleza** | 🏰 | Salas de desafío avanzado | Enemigos de mayor dificultad |
| **Cámara del Tesoro** | 💎 | Sala final con recompensa principal | Tesoro del Conocimiento |
| **Portal/Puerta** | 🚪 | Transición entre áreas | Requiere llaves específicas |

### 💰 **Objetos y Herramientas**

| Objeto | Tipo | Función | Rareza |
|--------|------|---------|--------|
| **Git** | Tesoro | Control de versiones, recupera vida | Poco Común |
| **IntelliJ IDEA** | Tesoro | IDE poderoso, aumenta poder | Raro |
| **Maven** | Tesoro | Gestión de dependencias, recupera vida | Común |
| **Docker** | Tesoro | Contenedores, aumenta defensa | Épico |
| **Stack Overflow** | Tesoro | Fuente de conocimiento supremo | Legendario |
| **Debugger** | Herramienta | Encuentra errores en el código | Poco Común |
| **Profiler** | Herramienta | Análisis de rendimiento | Raro |
| **Manual de Fundamentos** | Documento | Conceptos básicos de programación | Poco Común |

### 🎯 **Mecánicas de Juego**

| Término | Descripción | Uso |
|---------|-------------|-----|
| **Interacción (F)** | Acción principal para explorar salas | Esencial para obtener objetos y luchar |
| **Combate Educativo** | Sistema de lucha basado en preguntas | Responder correctamente para ganar |
| **Pool de Preguntas** | Conjunto de preguntas por nivel de dificultad | Se seleccionan aleatoriamente |
| **Progresión Adaptativa** | Dificultad que aumenta gradualmente | Trainee → Junior → Senior |
| **Mapa Visual** | Representación ASCII de la posición del jugador | [P] = Jugador, [✓] = Visitada |
| **Sistema de Recovery** | Mecanismo de recuperación ante errores | Opciones de continuar, reiniciar o salir |

### 🏆 **Objetivos y Logros**

| Término | Significado | Condición |
|---------|-------------|-----------|
| **Completación de Nivel** | Obtener el tesoro principal del nivel actual | Encontrar "Tesoro del Conocimiento X" |
| **Evolución de Desarrollador** | Avanzar al siguiente nivel de expertise | Completar nivel anterior |
| **Dominio del Conocimiento** | Comprender conceptos del nivel actual | Responder correctamente a enemigos |
| **Victoria Final** | Completar todo el juego | Obtener "Tesoro del Conocimiento Supremo" |
| **Maestría del Código** | Estado final del jugador | Convertirse en Senior Developer |

### 🎮 **Comandos y Controles**

| Comando | Función Completa | Contexto de Uso |
|---------|------------------|-----------------|
| **F (Interactuar)** | Acción principal del juego | En cualquier sala para explorar |
| **W/A/S/D** | Navegación direccional | Movimiento entre salas conectadas |
| **STATUS** | Vista completa del estado | Mapa visual, estadísticas, progreso |
| **V (Ver)** | Inspeccionar inventario | Lista de objetos recolectados |
| **X (Usar)** | Activar objeto del inventario | Consumir tesoros, usar llaves |
| **H (Help)** | Sistema de ayuda contextual | Comandos disponibles y pistas |

---

## 🎯 **Características Destacadas del Desarrollo**

### 💡 **Innovaciones Implementadas**
- **Combate Educativo**: Primer RPG textual enfocado en programación
- **Mapas Visuales**: Representación ASCII de la posición del jugador
- **Tutorial Adaptativo**: Pistas contextuales según el progreso
- **Sistema de Recovery**: Recuperación automática de errores

### 🔄 **Iteraciones de Mejora**
1. **v1.0**: Funcionalidad básica y combate
2. **v1.5**: Sistema de mapas y navegación
3. **v2.0**: Mejoras significativas en UX
4. **v2.1**: Corrección de bugs y optimizaciones

### 📝 **Lessons Learned**
- **UX es Crucial**: La experiencia del usuario determina el éxito
- **Debugging Iterativo**: Los errores pequeños pueden quebrar todo
- **Feedback Temprano**: Las pruebas con usuarios reales son invaluables
- **Documentación Clara**: El código autodocumentado facilita el mantenimiento

---

## 🚀 **Futuras Mejoras**

### 🔮 **Roadmap**
- [ ] Sistema de guardado/carga persistente
- [ ] Multijugador con ranking
- [ ] Editor de preguntas personalizado
- [ ] Interfaz gráfica (GUI)
- [ ] Soporte para más lenguajes de programación
- [ ] Sistema de hints adaptativos
- [ ] Integración con plataformas educativas

---

## 👨‍💻 **Acerca del Desarrollo**

**DungeonGame** fue desarrollado como proyecto educativo para demostrar el dominio de conceptos avanzados de programación Java, incluyendo POO, patrones de diseño, arquitectura de software y mejores prácticas de desarrollo.

El proyecto representa aproximadamente **40+ horas** de desarrollo distribuidas en diseño, implementación, testing y documentación.

### 🎖️ **Logros del Proyecto**
- ✅ **Arquitectura Limpia**: Código mantenible y extensible
- ✅ **Experiencia de Usuario**: Interfaz intuitiva y guiada
- ✅ **Valor Educativo**: Herramienta de aprendizaje efectiva
- ✅ **Robustez**: Manejo completo de casos edge y errores
- ✅ **Escalabilidad**: Fácil adición de nuevos niveles y contenido

---

**¡Embárcate en tu aventura como desarrollador y conviértete en un Senior Developer!** 🎮👨‍💻

---

## 📞 **Contacto**

Si tienes preguntas sobre el proyecto o quieres contribuir, no dudes en contactar.

**¡Happy Coding!** 🎯