# MESOS
> Progetto di laurea in Ingegneria informatica  
> **Politecnico di Milano**
> **[2025/2026]**
> 
> ***Il Gioco da tavolo Mesos e tutto il relativo materiale grafico è di esclusiva proprietà di Cranio Creations***

---

## 📖 Panoramica
Il progetto consiste nella realizzazione di un videogioco **Java** distribuito con tecnologia *client-server*, *thin client*, che sfruttasse i principi della progettazione orientata ad oggetti (**OOP**) per raggiungere con disciplina i requisiti prefissati dagli studenti tra quelli annoverati come possibili.
Vi sono i dettagli di progettazione nella cartella *deliveries*.

---

## Caratteristiche Principali
* **Logica di gioco fedele**: realizzazione completa delle regole del gioco da tavolo originale (gestione dei turni, risorse, acquisizione delle carte, interazioni, punteggi).
* **Compatibilità operativa**: eseguibile su *Windows*, *Linux* o *Mac*.*
* **Architettura pulita**: progettazione orientata agli oggetti (OOP) basata su pattern come *MVC (Model-View-Controller)*, *abstract factory* (per la connessione), *proxy* (per la connessione), *command* (per la TUI), *strategy* (per le carte) e tanti altri.*
* **Multigiocatore / Partite multiple**: singolo server concorrente in modo che possa gestire partite diverse contemporaneamente.*
* **Persistenza**: il server serba periodicamente lo stato della partita su disco e permette di riprendere la partita ai giocatori anche a seguito del crash del server stesso (i giocatori dovranno ricollegarsi con lo stesso nomignolo).*
* **Resilienza alle disconnessioni**: I giocatori disconnessi a seguito della caduta della rete, possono ricollegarsi e continuare la loro partita. Mentre un giocatore non è collegato, il gioco continua saltando i turni di quel giocatore.*
* **Interfaccia grafica (GUI) e testuale (TUI)**.*

---

### Istruzioni per l'avvio
1. clona il repository;
2. apri la cartella *deliveries*;
3. avvia un server con l'eseguibile [il tuo sistema operativo]_run_server.sh
4. avvia un client con l'eseguibile [il tuo sistema operativo]_run_client.sh
5. buon divertimento!

---

## 🛠️ Tecnologie Utilizzate
* **Linguaggio**: Java
* **Build Tool**: Maven
* **Framework GUI**: Swing
* **Versionamento**: Git & GitHub
   
*Il Gioco da tavolo Mesos e tutto il relativo materiale grafico è di esclusiva proprietà di Cranio Creations*

Grazie a: Massimiliano Ferretti, Giuseppe Abata, Zheng Wu.
