# Airport Modelling Project

A discrete-time, tick-based **airport runway operations simulator**, built as a JavaFX desktop application for the CS261 Group Project at the University of Warwick. It lets an airport operations manager configure a runway layout and a day's flight schedule, run a time-stepped simulation of arrivals, departures, holding patterns and emergencies, and then explore the results through interactive graphs, sortable/searchable tables and cross-run comparisons.

**Authors**: Adrian Barra Cornes, Rhys Beaman, Afra Hossain, Prisha Jaiswal, Yunas Kramer, Adi Minocha

---

## Table of Contents

- [Overview](#overview)
- [Technical Highlights](#technical-highlights)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [User Guide](#user-guide)
- [Modelling Assumptions & Restrictions](#modelling-assumptions--restrictions)
- [My Contributions — Adrian Barra Cornes](#my-contributions--adrian-barra-cornes)

---

## Overview

The codebase lives in **`airport-app`** — a Maven-managed, JavaFX desktop application written in Java. The simulation treats airport operations as a fixed-size tick loop: at every tick, aircraft are generated according to configurable arrival/departure flow rates, routed through holding patterns or take-off queues, and matched against available runways by a traffic-control component that enforces runway state, mixed-mode alternation, emergency priority, diversion avoidance and cancellation mitigation. Once the simulation completes, the results are aggregated into summary statistics, time-series graphs and detailed flight-level tables, and persisted locally so multiple simulation runs can be compared side by side.

## Technical Highlights

**Simulation engine**
- **Tick-driven event loop** (`SimLoop`, a `javafx.concurrent.Task<ModelOutput>`) runs the simulation off the JavaFX Application Thread, streaming progress back to the UI via `updateProgress` so the interface never blocks during a run.
- **Procedural flight generation** (`DataGen`) synthesises arrival and departure schedules for the simulated runtime from user-specified hourly flow rates, rather than relying on a static dataset.
- **Runway state machine** (`RunWay`): each runway tracks an `OperatingMode` (`LANDING` / `TAKEOFF` / `MIXED`) and an `OperationStatus` (`AVAILABLE` / `INSPECTION` / `SNOW` / `EQUIPFAIL`), with up to three scheduled interruption windows per runway applied automatically as simulated time passes.
- **Dynamic runway conversion**: if every runway of one exclusive type (all landing or all take-off) is closed by an interruption, `TrafficController` converts one runway of the opposite type into mixed-use for the remainder of the run — a one-way, stateful escalation rather than a static allocation.
- **Mixed-runway turn-taking**: mixed runways alternate between servicing an arrival and a departure, tracked per-runway via `mixedModeTurn`, so throughput is shared fairly between the two flows.
- **Priority queues with emergency pre-emption**: the holding pattern (`HoldingQueue`) and take-off queue (`TakeOffQueue`) are FIFO by default but let emergency aircraft jump onto any available mixed runway immediately, and let departures within 10 minutes of cancellation pre-empt their slot — reducing diversions and cancellations without violating first-come ordering for non-emergency traffic.
- **Three emergency classes** (mechanical failure, passenger health, and fuel emergencies triggered dynamically when an aircraft's holding fuel drops below 15 minutes), each resolved with equal priority, ordered by arrival time.
- **Statistical aggregation** (`ModelOutput`) computes running maxima and sums every tick (max/avg arrival & departure delay, max/avg queue wait, peak queue lengths) and builds the time-series data consumed by the chart views, keeping the per-tick cost of bookkeeping O(1).

**Results & analytics UI**
- Four **live-updating line charts** (arrival wait time, departure wait time, take-off queue length, holding pattern length vs. simulation time) backed by JavaFX `XYChart.Series`.
- **Five cross-referenced, sortable, time-range-searchable data tables** — arrivals, departures, take-off queue history, holding-pattern history and runway-by-runway flight allocation — built on `TreeMap<Integer, List<Row>>` structures keyed by tick, giving O(log n) range queries for the time-stamp search feature.
- A **slide-out side panel** with its own searchable tables for the full airplane schedule (by callsign) and runway configuration (by runway number).
- **Cross-run comparison**: completed simulations are persisted to a local `SimMetrics.csv`, and the Compare Simulations view supports multi-select (Ctrl+Click) comparison of key statistics across any number of historical runs.

## Architecture

```
airport-app/src/main/java/com/cs261/app/
├── App.java                  # JavaFX application entry point
├── controller/                # Simulation orchestration
│   ├── SimLoop.java            # Tick loop (background Task<ModelOutput>)
│   └── SimService.java         # Bridges UI requests to the simulation
├── model/                      # Core domain logic (no JavaFX dependencies)
│   ├── RunWay / RunWayMap       # Runway state machine + fleet-wide queries
│   ├── AirCraft / AirCraftMap    # Flight entities + lookup
│   ├── HoldingQueue / TakeOffQueue  # Priority-aware queues
│   ├── ArrivalModel / DepartureModel # Per-flow bookkeeping
│   ├── TrafficController         # Per-tick runway assignment & conflict rules
│   ├── DataGen                   # Procedural flight schedule generation
│   ├── ModelOutput                # Statistics + table/graph aggregation
│   └── Utils                      # Simulation constants & tick/time conversion
└── UI/                          # JavaFX FXML controllers (MVC view layer)
    ├── NewSim, RunwayInputController, InterruptionInputController  # Input wizard
    ├── SimulationWaitController                                     # Progress screen
    ├── OutputPageController, Globals                                # Results shell + shared state
    ├── Table*Controller (Arrivals/Departures/Holding/Queue/Runway)  # Data tables
    ├── *GraphController (Arrivals/Departures/HP/TQ)                 # Charts
    ├── ComparePane, CompareSimController                            # Cross-run comparison
    └── Navigation, PopupMenuController, UserGuidePopup              # App-wide navigation/chrome
```

The split between `model/` (pure simulation logic, independently unit-tested) and `UI/` (FXML-backed JavaFX controllers) keeps the simulation engine decoupled from presentation, so the core model can — and is — tested headlessly without starting the JavaFX runtime.

## Tech Stack

- **Language**: Java (compiled against release 25)
- **UI Framework**: JavaFX (`javafx-controls`, `javafx-fxml`) driven by FXML view definitions
- **Build**: Maven, with the `javafx-maven-plugin` wired to `com.cs261.app.App`
- **Testing**: JUnit 5 (Jupiter) — unit, integration and invariant test suites
- **Fonts/Assets**: bundled Inter variable font family for consistent cross-platform typography

## Getting Started

### Prerequisites
- JDK 25+
- Maven 3.9+

### Running the program
```bash
cd airport-app
mvn clean javafx:run
```

### Running the test suite
```bash
cd airport-app
mvn clean validate compile test
```

The test suite spans `src/test/java/com/cs261/app/`:
- **`model/`** — unit tests for `HoldingQueue`, `TakeOffQueue`, `RunWay`, map utilities and `Utils`
- **`integration/`** — `SimLoopScenarioTest`, driving the full tick loop end-to-end against scripted scenarios
- **`invariants/`** — `InvariantTest`, asserting properties that must hold across every tick (no duplicated aircraft, queue sizes never negative, runway occupancy consistency, etc.)

## User Guide

**Getting started**
To start the simulation, press **Load New Simulation** on the front page. On the input page, enter the required fields (marked with `*`), then press **Start Simulation**. Once processing finishes, the output page loads with the key statistics view; the dark blue side menu navigates between the other output pages.

**Input Page**
- *Runtime*: how long the airport is simulated for (days, hours, minutes) — determines the generated flight schedule.
- *Arrival/departure flow rate*: incoming/outgoing planes per hour (default 15 each).
- *Probability of failure*: chance of a mechanical-failure or passenger-health emergency occurring on an arrival.
- *Time before cancellation*: minutes a departure may wait before being cancelled (default 30).
- *Interruptions*: operational-status changes causing runway closure; each has a start time and duration, up to three per runway.
- *Runways*: 1–10 runways may be configured per airport.

**Output Page**
- *Key statistics*: an overview of airport traffic during the run, covering every key output statistic specified by **Dorset Software**.
- *Graphs*: queue wait time and queue length against simulation time, for identifying bottlenecks.
- *Departure / Arrival tables*: full detail on every generated departure/arrival flight.
- *Take-off queue / Holding pattern tables*: every flight that passed through each queue during the run.
- *Runway flight data*: which flights landed on which runway, sortable by runway number.
- *Time-stamp search*: available on every table, to filter flights by elapsed simulation time.
- *Side menu*: the blue `<` arrow opens a searchable (by callsign) flight-schedule table and a searchable (by runway number) runway table.

**Compare Simulations Page**
Exiting a simulation (via **Exit Simulation** on the Key Statistics menu) prompts a **Save Data** step, persisting the run for later comparison. **Compare Previous Simulations** on the front page lists past runs by the real-world date they were created (not renameable); use **Ctrl + Left-Click** to select or deselect multiple runs, each shown in its own column alongside its key statistics. All run data lives in a local `SimMetrics.csv` — deleting it discards all historical runs.

## Modelling Assumptions & Restrictions

- **Runway conversion**: an exclusive-use runway converts to mixed-use only once every runway of its own exclusive type has closed due to an interruption; this contributes to delay/wait statistics and is **not** reverted for the rest of the run.
- **Mixed-runway alternation**: mixed runways alternate arrivals/departures, starting on arrivals.
- **Runway occupancy time**: a plane spends 2 simulated minutes on the runway once assigned, approximating extended queue wait effects.
- **Diversion mitigation**: an emergency aircraft is placed on any available mixed runway immediately, out of turn, if no exclusive runway is free.
- **Cancellation mitigation**: a departure within 10 minutes of its cancellation deadline is placed on any available mixed runway immediately, out of turn.
- **Emergency wait allowance**: an emergency aircraft remains in the simulation for up to 4 minutes before being diverted, to reduce unnecessary diversions.
- Fuel emergencies trigger dynamically when a holding aircraft has 15 minutes of fuel remaining; mechanical and health emergencies are pre-determined by probability. All emergency types share equal priority, broken by arrival order.

---

## My Contributions — Adrian Barra Cornes

Across the project I made **117 commits** — the second-highest commit count on the team — concentrated on turning the simulation engine's raw output into a usable, navigable desktop application, and on configuring the simulation itself. In practice this meant owning almost the entire **results/analytics UI layer** and the **simulation input wizard**, plus a share of the output data model and the automated test suite.

### Results & analytics UI (primary ownership)
- Built **`OutputPageController`** and **`Globals`**, the two classes that tie the whole results experience together: `Globals` holds the shared, app-wide simulation state (the `ModelOutput` reference, chart series, and `TreeMap`/`HashMap`-backed lookup structures for every table), while `OutputPageController` drives page composition, the side navigation menu, and wiring the key-statistics view to that shared state.
- Implemented the **table controllers** for all five results tables — arrivals, departures, take-off queue, holding pattern and runway allocation (`TablesArrivalsController`, `TablesDeparturesController`, `TableQueueController`, `TableHoldingController`, `TableRunwayController`, `TableNavController`) — including the time-stamp range search backed by the `TreeMap` range-query helpers in `Globals`.
- Implemented the **chart controllers** (`ArrivalsGraphController`, `DeparturesGraphController`, `GraphsHPController`, `GraphsTQController`) that bind simulation time-series data to JavaFX `XYChart.Series` for the four wait-time/queue-length graphs.
- Authored the corresponding FXML layouts for every table and graph page (`tables(Arrivals/Departures/HP/Runway/TO).fxml`, `graphs(arrivals/departures).fxml`, `outputPage.fxml`, `keyData.fxml`, `popupMenu.fxml`) and the row/detail model classes backing them (`ArrivalRow`, `QueueRow`, `RunwayRow`, `AirCraftDetail`, `RunwayDetail`).
- Built the **Compare Simulations** feature end-to-end on the UI side (`ComparePane`, `CompareSimController`), including the multi-select (Ctrl+Click) comparison interaction against the persisted `SimMetrics.csv` run history.

### Simulation input & navigation
- Built the **simulation configuration wizard** — `RunwayInputController` and `InterruptionInputController`, plus their `RunwayInput.fxml` / `InterruptionInput.fxml` layouts — covering runway count/mode entry and the per-runway interruption (start time + duration) configuration described in the User Guide.
- Implemented **`NewSim`** and large parts of **`App`**, the application's page-flow and entry-point classes, and **`Navigation`**/`PopupMenuController`/`UserGuidePopup` for moving between the input, progress, output and comparison screens.
- Contributed to **`inputForm.fxml`**, the main simulation-parameters form (runtime, flow rates, failure probability, cancellation time).

### Simulation output model
- Contributed to **`ModelOutput`**, the aggregation class that turns raw per-tick simulation state into the key statistics, graph series and table rows consumed by the UI described above — the main point of contact between the `model/` engine and the `UI/` layer.

### Testing & stability
- Added and extended **unit test coverage for the runway state machine** (`RunWayTest`) and other model-layer tests, exercising runway mode conversion, interruption scheduling and status transitions.
- Investigated and fixed several **cross-cutting bugs surfaced during integration** between the simulation engine and the UI (e.g. table/graph data desynchronisation after a simulation run, input-form validation edge cases), as reflected in the commit history (`major bug fixed`, `fixed some errors and more checking`, `fixed a tiny few errors`).
- Performed a pass of **comment and dead-code cleanup** across the UI layer to keep the final codebase readable for handover/marking.

### Documentation
- Authored and maintained this `README.md`, including the setup instructions, user guide and modelling assumptions sections.
