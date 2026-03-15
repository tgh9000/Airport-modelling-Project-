# airport-modelling-Project-

This is the codebase for the CS261 Group 3 Airport Modelling Project @ Uni of Warwick. 

**Authors**:
Adrian Barra Cornes, Rhys Beaman, Afra Hossain, Prisha Jaiswal, Yunas Kramer and Adi Minocha

## Description

We created this simulation software to allow airport managers to test configurations of airport runways, subject to modelling constructions. The codebase is in the folder **airport-app** . Please do not delete the pom.xml file.

## Running the program from commandline using Maven
cd airport-app  
mvn clean javafx:run

## Testing the program from commandline using Maven
cd airport-app  
mvn clean validate compile test

## Technologies used
- Language: Java  
- JavaFX for UI  
- Maven for build automation and project management

## User Guide  
**Getting started**  
To start the simulation, press 'Load New Simulation' on the front page. On the input page, please enter the required fields as indicated by the * symbols. Once the fields have been entered, press 'Start Simulation' for the simulation to start processing. Once the simulation has finished processing, the output page will be loaded. The first page are the key statistics. The dark blue menu on the left can be used to navigate to further output pages.  

**Input Page**    
*Runtime*: The runtime input is how long the function of the airport will be simulated for. This determines the airport flight schedule. The runtime of the simulation can be entered in days, hours and minutes.  
*Arrival and departure flow rate*: The arrival flow rate is the number of incoming planes per hour in the simulation. The departure flow rate is the number of outgoing planes per hour in the simulation. The default flow rate for arrivals and departures is 15.  
*Probability of failure*: the chance of a mechanical failure emergency or a passenger health emergency occuring in an arrival plane.   
*Time that can pass before cancellation*: The number of minutes a departure flight can wait before being cancelled. The default value is 30 minutes.  
*Interruptions*: A change in operational status which causes runway closure. For each interruption added, the start time and duration of the interruption can be inputted. Each runway can have up to three interruptions during runtime.  
*Start time of interruption*: Time elapsed in simulation before the interruption occurs.  
*Duration of interruption*: How long the interruption will last for.  
*Runways*: 1 to 10 (inclusive) runways may be added for the simulated airport.  

**Output Page**  
The *key statistics* are an overview of the airport traffic during simulation. All the key output statistics outlined by **Dorset Software** have been included.  
*Graphs*: the graphs of each queue wait time against simulation time have been provided to identify bottlenecks in traffic.
The graphs of queue lengths over time have been provided to show the changing structure of airport queues during runtime.  
*Departure table*: The departure table provides information on every departure flight generated during take off.  
*Arrival table*: The arrival table provides information on every arrival flight generated during landing.  
*Take off queue table*: Provides information on every flight which has been in the take off queue during runtime.  
*Holding pattern table*: Provides information on every flight which has been in the holding pattern during runtime.  
*Runway flight data*: Provides information on which flights landed on which runways. The table can be sorted by runway number.  
*Time stamp search*: For each table where the search function is available, the time elapsed in minutes may be inputted to search for the flights simulated during the inputted times.  
*Side menu*: By pressing the blue '<' arrow button on the right side, a searchable (by callsign) table of the airplane schedule can be accessed for further details. A searchable table of runways (by runway number) can be accessed as well.  

**Compare Simulations Page**  
The simulation may be exited by clicking the 'Exit Simulation' button on the menu in the 'Key Statistics' page. This will show a 'Save Data' prompt, which will save the data for comparisons before exitting the current simulation run.  
The 'Compare Previous Simulations button on the front page menu will provide a list of previous simulations which may be selected to view past statistics. 
Multiple simulations can be selected from the list at a time. Use CTRL + LEFT-CLICK to select a simulation to view. The key statistics for the selected simulation will appear under the respective date in the right hand panel.  
Simulations can be de-selected by using CTRL + LEFT-CLICK on a highlighted simulation name in the list.  
Please note the simulation will be named under the real-world date it was created on. This is not changable.  
The data for all simulations is saved locally as the csv **'SimMetrics.csv'**. Deleteing this csv will result in the loss of all previous data.  

**Restrictions and Assumptions**  
*Runway conversions*: An exclusive use (take off only/ landing only) runway will only be converted into a mixed use runway if all runways of an exclusive type have been closed due to user-inputted interruptions. This will contribute to the delay and wait time statistics. This is a special case modelling scenario and the runways will NOT be converted back for the rest of the simulation.  
*Mixed runway use*: mixed runways will alternate between handling arrivals and departures, starting on arrivals.   
*Runway time*: Once a plane has been allocated a runway for arrival/departure, it will spend '2 minutes' on the runway. Please note that this simulates extended queue wait times.  
*Diversion reduction*: The simulation will attempt to reduce diversions by placing any planes in emergency on an available mixed runway immediately if no exclusive runways are available, regardless of mixed runway turn.   
*Cancellation reduction*: The simulation will attempt to reduce cancellations by place any planes which are 10 minutes away from being cancelled onto an available mixed runway immediately, regardless of mixed runway turn.  
*Emergency wait time*: Even if it does not land immediately, an emergency plane will remain in the simulation for 4 minutes in an attempt to reduce diversions. 
Fuel emergencies are decided during runtime. Fuel emergencies will be triggered if an aircraft in the holding queue only has 15 minutes of fuel left. Health and mechanical emergencies are pre-determined based on probability.  
All emergencies are treated with equal priority. Emergency planes that arrived earlier have higher priority than those that arrived later.  


