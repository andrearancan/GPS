# Come avviare il server

Affinchè il server sia funzionante è necessario aggiungere alla cartella del progetto **spring-server**, Maperitive scaricabile a questo sito http://maperitive.net/.

Una volta scaricato ed estratto dall'archivio bisogna aggiungere una cartella chiamata "Mappe", dove verrà salvato il risultato del download del server.

Infine va modificato il file default.mscript, una copia di questo file è presente nel progetto con lo stesso nome e va sostituito a quello nella cartella Scripts.

# Cambiare indirizzo IP al client

Aprire il progetto **GPS**, nella classe ApiClient.java modificare il campo basePath = "_indirizzo del server_".
Per comunicare correttamente col server leggere Readme di spring-server.

 **Tutte le spiegazioni e il funzionamento sono contenute nel file Geocomp.pdf**
